package fr.radi3nt.multi.packets.module.types;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.module.PacketDataBufferProvider;
import fr.radi3nt.multi.packets.module.PacketProperty;

public class PacketContentProperty implements PacketProperty {

    private final PacketDataBufferProvider packetDataBufferProvider;
    private PacketOut packetOut;
    private PacketIn packetIn;

    public PacketContentProperty(PacketDataBufferProvider packetDataBufferProvider) {
        this.packetDataBufferProvider = packetDataBufferProvider;
    }

    @Override
    public PacketDataBuffer encode() {
        PacketDataBuffer packetDataBuffer = packetDataBufferProvider.createBuffer(packetOut.getByteSize());
        packetOut.save(packetDataBuffer);
        return packetDataBuffer;
    }

    @Override
    public void decode(PacketDataBuffer packetDataBuffer) {
        packetIn.load(packetDataBuffer);
    }

    public void setPacketOut(PacketOut packetOut) {
        this.packetOut = packetOut;
    }

    public void setPacketIn(PacketIn packetIn) {
        this.packetIn = packetIn;
    }
}
