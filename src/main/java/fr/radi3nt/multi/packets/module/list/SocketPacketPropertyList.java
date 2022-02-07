package fr.radi3nt.multi.packets.module.list;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.module.PacketDataBufferProvider;
import fr.radi3nt.multi.packets.module.types.PacketContentProperty;
import fr.radi3nt.multi.packets.module.types.PacketTotalSizeProperty;
import fr.radi3nt.multi.packets.module.types.PacketTypeIDProperty;
import fr.radi3nt.multi.packets.types.PacketIdentifier;

public class SocketPacketPropertyList implements PacketPropertyList {

    private final PacketDataBufferProvider packetDataBufferProvider;
    private final PacketTypeIDProperty packetTypeIDProperty;
    private final PacketTotalSizeProperty packetTotalSizeProperty;
    private final PacketContentProperty packetContentProperty;

    public SocketPacketPropertyList(PacketDataBufferProvider packetDataBufferProvider, PacketIdentifier packetIdentifier) {
        this.packetDataBufferProvider = packetDataBufferProvider;
        packetTypeIDProperty = new PacketTypeIDProperty(packetDataBufferProvider, packetIdentifier);
        packetTotalSizeProperty = new PacketTotalSizeProperty(packetDataBufferProvider);
        packetContentProperty = new PacketContentProperty(packetDataBufferProvider);
    }

    @Override
    public PacketDataBuffer encode(PacketOut packet) {
        packetTypeIDProperty.setPacketOut(packet);
        PacketDataBuffer id = packetTypeIDProperty.encode();

        packetContentProperty.setPacketOut(packet);
        PacketDataBuffer content = packetContentProperty.encode();

        int totalBufferSize = content.getSize()+id.getSize();

        PacketDataBuffer result = packetDataBufferProvider.createBuffer(totalBufferSize);

        id.getBuffer().flip();
        content.getBuffer().flip();

        result.write(id);
        result.write(content);

        return result;
    }

    @Override
    public PacketIn decode(PacketDataBuffer buffer) {
        packetTypeIDProperty.decode(buffer);
        PacketIn packetIn = packetTypeIDProperty.getPacketIn();

        packetContentProperty.setPacketIn(packetIn);
        packetContentProperty.decode(buffer);

        return packetIn;
    }

}
