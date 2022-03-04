package fr.radi3nt.multi.packets.module.types;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.module.PacketDataBufferProvider;
import fr.radi3nt.multi.packets.module.PacketProperty;

public class PacketSizeProperty implements PacketProperty {

    private final PacketDataBuffer packetDataBuffer;
    private int sizeToEncode;
    private int sizeDecoded;

    public PacketSizeProperty(PacketDataBufferProvider packetDataBufferProvider) {
        packetDataBuffer = packetDataBufferProvider.createBuffer(Integer.BYTES);
    }

    @Override
    public PacketDataBuffer encode() {
        packetDataBuffer.clear();
        packetDataBuffer.write(new IntSerializer(sizeToEncode));
        return packetDataBuffer;
    }

    @Override
    public void decode(PacketDataBuffer packetDataBuffer) {
        sizeDecoded = packetDataBuffer.read(new IntSerializer()).getInteger();
    }

    public void setSizeToEncode(int sizeToEncode) {
        this.sizeToEncode = sizeToEncode;
    }

    public int getSizeDecoded() {
        return sizeDecoded;
    }
}
