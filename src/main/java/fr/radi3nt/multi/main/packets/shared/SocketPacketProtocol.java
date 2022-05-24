package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.logic.PacketProtocol;
import fr.radi3nt.multi.packets.module.list.PacketPropertyList;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.CommunicationManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ListenerManager;
import fr.radi3nt.multi.sockets.shared.distant.request.impl.SocketRequest;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SocketPacketProtocol implements PacketProtocol {

    private final CommunicationManager communicationManager;
    private final PacketPropertyList packetPropertyList;


    private final List<PacketInterceptor> packetInterceptors = new ArrayList<>();

    public SocketPacketProtocol(CommunicationManager communicationManager, ListenerManager listenerManager, PacketPropertyList packetPropertyList) {
        this.communicationManager = communicationManager;
        this.packetPropertyList = packetPropertyList;

        listenerManager.addListener(dataInputStream -> {
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

        communicationManager.send(new SocketRequest(result.getBuffer()));

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
