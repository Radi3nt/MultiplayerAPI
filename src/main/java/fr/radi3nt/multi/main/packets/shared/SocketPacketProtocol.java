package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.logic.PacketProtocol;
import fr.radi3nt.multi.packets.module.list.PacketPropertyList;
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

public class SocketPacketProtocol implements PacketProtocol {

    private static final int MIN_BYTES_TO_ENCRYPT = 600;
    private static final int BYTES_SIZE_DECOMPRESSED = Integer.BYTES;
    private static final int BYTES_SIZE_COMPRESSED = Integer.BYTES;

    private final CommunicationManager communicationManager;
    private final ListenerManager listenerManager;

    private final PacketEncoder packetEncoder;
    private final PacketDecoder packetDecoder;


    private final PacketEncryptor packetEncryptor;
    private final PacketDecrypter packetDecrypter;

    private final PacketCompressor packetCompressor;
    private final PacketDecompressor packetDecompressor;

    private final PacketPropertyList packetPropertyList;


    private final List<PacketInterceptor> packetInterceptors = new ArrayList<>();

    public SocketPacketProtocol(CommunicationManager communicationManager, ListenerManager listenerManager, PacketIdentifier packetIdentifier, PacketEncryptor packetEncryptor, PacketDecrypter packetDecrypter, PacketCompressor packetCompressor, PacketDecompressor packetDecompressor, PacketPropertyList packetPropertyList) {
        this.communicationManager = communicationManager;
        this.listenerManager = listenerManager;
        this.packetEncryptor = packetEncryptor;
        this.packetDecrypter = packetDecrypter;
        this.packetCompressor = packetCompressor;
        this.packetDecompressor = packetDecompressor;
        this.packetPropertyList = packetPropertyList;
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
        byte[] bytes = new byte[size];

        int readData = 0;
        while (readData<size) {
            readData += dataInputStream.read(bytes, readData, size-readData);
        }

        PacketDataBuffer received = new ByteBufferPacketDataBuffer(ByteBuffer.wrap(bytes));

        PacketIn packetIn = packetPropertyList.decode(received);
        notifyIn(packetIn);

        /*
        PacketDataBuffer packetDataBuffer = new ByteBufferPacketDataBuffer(ByteBuffer.allocate(bytes.length));
        packetDataBuffer.write(bytes);
        packetDataBuffer.getBuffer().flip();


        PacketDataBuffer written = packetDataBuffer;

        PacketIn packet = null;

        //System.out.println("receive size: " + PacketDataBuffer.getSize());
        if (useCompress) {
            try {
                written = packetDecompressor.decompress(packetDataBuffer);
            } catch (DataFormatException e) {
                e.printStackTrace();
            }

            written.getBuffer().flip();
        }

        packet = packetDecoder.decode(written);




        notifyIn(packet);

         */
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


        PacketDataBuffer content = packetPropertyList.encode(packet);
        PacketDataBuffer result = new ByteBufferPacketDataBuffer(ByteBuffer.allocate(content.getSize()+Integer.BYTES));

        result.write(new IntSerializer(content.getSize()));
        content.getBuffer().flip();
        result.write(content);

        communicationManager.send(new SocketRequest(result.getContent()));

        /*
        PacketDataBuffer packetDataBuffer = new ByteBufferPacketDataBuffer(ByteBuffer.allocate(packet.getByteSize()+ BYTES_SIZE_COMPRESSED + BYTES_SIZE_DECOMPRESSED +Byte.BYTES));

        boolean needCompress = packetDataBuffer.getSize()>=MIN_BYTES_TO_ENCRYPT;
        if (!needCompress) {
            packetDataBuffer.write(new IntSerializer(packetDataBuffer.getSize()-BYTES_SIZE_COMPRESSED));
            packetDataBuffer.write(new IntSerializer(-1));
        }

        packetEncoder.encode(packet, packetDataBuffer);
        packetDataBuffer.getBuffer().flip();

        PacketDataBuffer actualSerializer = packetDataBuffer;
        if (needCompress) {
            actualSerializer = packetCompressor.compress(packetDataBuffer);
            actualSerializer.getBuffer().flip();
        }

        communicationManager.send(new SocketRequest(actualSerializer.getContent()));

         */
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
