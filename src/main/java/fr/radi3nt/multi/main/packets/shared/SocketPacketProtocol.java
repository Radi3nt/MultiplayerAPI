package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.serializer.types.ByteSerializer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.logic.PacketProtocol;
import fr.radi3nt.multi.packets.process.recieving.PacketDecoder;
import fr.radi3nt.multi.packets.process.recieving.PacketDecompressor;
import fr.radi3nt.multi.packets.process.recieving.PacketDecrypter;
import fr.radi3nt.multi.packets.process.sending.PacketCompressor;
import fr.radi3nt.multi.packets.process.sending.PacketEncoder;
import fr.radi3nt.multi.packets.process.sending.PacketEncryptor;
import fr.radi3nt.multi.packets.types.PacketIdentifier;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.CommunicationManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ListenerManager;
import fr.radi3nt.multi.sockets.shared.distant.request.impl.SocketRequest;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class SocketPacketProtocol implements PacketProtocol {

    private static final int MIN_BYTES_TO_ENCRYPT = 600;

    private final CommunicationManager communicationManager;
    private final ListenerManager listenerManager;

    private final PacketEncoder packetEncoder;
    private final PacketDecoder packetDecoder;


    private final PacketEncryptor packetEncryptor;
    private final PacketDecrypter packetDecrypter;

    private final PacketCompressor packetCompressor;
    private final PacketDecompressor packetDecompressor;



    private final List<PacketInterceptor> packetInterceptors = new ArrayList<>();

    public SocketPacketProtocol(CommunicationManager communicationManager, ListenerManager listenerManager, PacketIdentifier packetIdentifier, PacketEncryptor packetEncryptor, PacketDecrypter packetDecrypter, PacketCompressor packetCompressor, PacketDecompressor packetDecompressor) {
        this.communicationManager = communicationManager;
        this.listenerManager = listenerManager;
        this.packetEncryptor = packetEncryptor;
        this.packetDecrypter = packetDecrypter;
        this.packetCompressor = packetCompressor;
        this.packetDecompressor = packetDecompressor;
        this.packetEncoder = new PacketEncoder();
        this.packetDecoder = new PacketDecoder(packetIdentifier);

        this.listenerManager.addListener(dataInputStream -> {
            try {
                receivePacket(dataInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void receivePacket(DataInputStream dataInputStream) throws IOException {
        int size = dataInputStream.available();
        byte[] bytes = new byte[size-Byte.BYTES];
        boolean useCompress = dataInputStream.readByte()==0x01;
        dataInputStream.read(bytes);

        PacketDataSerializer packetDataSerializer = new ByteBufferPacketDataSerializer(ByteBuffer.allocate(bytes.length));
        packetDataSerializer.write(bytes);
        packetDataSerializer.getBuffer().flip();


        PacketDataSerializer written = packetDataSerializer;

        PacketIn packet = null;

        //System.out.println("receive size: " + packetDataSerializer.getSize());
        if (useCompress) {
            try {
                written = packetDecompressor.decompress(packetDataSerializer);
            } catch (DataFormatException e) {
                e.printStackTrace();
            }

            written.getBuffer().flip();
        }

        packet = packetDecoder.decode(written);


        notifyIn(packet);
    }

    private void notifyIn(PacketIn packet) {
        for (PacketInterceptor packetInterceptor : packetInterceptors) {
            packet = packetInterceptor.onPacketIn(packet);
        }
    }

    private PacketOut notifyOut(PacketOut packet) {
        for (PacketInterceptor packetInterceptor : packetInterceptors) {
            packet = packetInterceptor.onPacketOut(packet);
        }
        return packet;
    }

    @Override
    public void sendPacket(PacketOut packet) {
        packet = notifyOut(packet);
        if (packet==null)
            return;

        PacketDataSerializer packetDataSerializer = new ByteBufferPacketDataSerializer(ByteBuffer.allocate(packet.getByteSize()+Integer.BYTES+Integer.BYTES+Byte.BYTES));

        boolean needCompress = packetDataSerializer.getSize()>=MIN_BYTES_TO_ENCRYPT;
        if (!needCompress) {
            packetDataSerializer.write(new IntSerializer(packetDataSerializer.getSize()-Integer.BYTES));
            packetDataSerializer.write(new ByteSerializer((byte) 0x00));
        }
        //System.out.println("sent size: " + (packetDataSerializer.getSize()-Integer.BYTES));

        packetEncoder.encode(packet, packetDataSerializer);
        packetDataSerializer.getBuffer().flip();

        PacketDataSerializer actualSerializer = packetDataSerializer;
        if (needCompress) {
            actualSerializer = packetCompressor.compress(packetDataSerializer);
            actualSerializer.getBuffer().flip();

            //System.out.println("sent compress: "+ (actualSerializer.getSize()-Integer.BYTES-Byte.BYTES));
        }

        communicationManager.send(new SocketRequest(actualSerializer.getContent()));
    }

    @Override
    public void addInterceptor(PacketInterceptor packetInterceptor) {
        packetInterceptors.add(packetInterceptor);
    }

    @Override
    public void removeInterceptor(PacketInterceptor packetInterceptor) {
        packetInterceptors.remove(packetInterceptor);
    }
}
