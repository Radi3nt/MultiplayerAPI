package fr.radi3nt.multi.packets.module.types;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.module.PacketDataBufferProvider;
import fr.radi3nt.multi.packets.module.PacketProperty;

public class PacketTotalSizeProperty implements PacketProperty {

    private final PacketDataBuffer packetDataBuffer;
    private int size;

    public PacketTotalSizeProperty(PacketDataBufferProvider packetDataBufferProvider) {
        packetDataBuffer = packetDataBufferProvider.createBuffer(Integer.BYTES);;
    }

    @Override
    public PacketDataBuffer encode() {
        packetDataBuffer.clear();
        packetDataBuffer.write(new IntSerializer(size));
        return packetDataBuffer;
    }

    @Override
    public void decode(PacketDataBuffer packetDataBuffer) {
        size = packetDataBuffer.read(new IntSerializer()).getInteger();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
