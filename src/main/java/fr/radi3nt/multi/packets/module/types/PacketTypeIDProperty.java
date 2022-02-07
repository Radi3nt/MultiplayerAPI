package fr.radi3nt.multi.packets.module.types;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.module.PacketDataBufferProvider;
import fr.radi3nt.multi.packets.module.PacketProperty;
import fr.radi3nt.multi.packets.types.PacketIdentifier;

public class PacketTypeIDProperty implements PacketProperty {

    private final PacketDataBuffer packetDataBuffer;
    private final PacketIdentifier packetIdentifier;
    private PacketOut packetOut;
    private PacketIn packetIn;

    public PacketTypeIDProperty(PacketDataBufferProvider packetDataBufferProvider, PacketIdentifier packetIdentifier) {
        this.packetDataBuffer = packetDataBufferProvider.createBuffer(Integer.BYTES);
        this.packetIdentifier = packetIdentifier;
    }

    @Override
    public PacketDataBuffer encode() {
        packetDataBuffer.clear();
        packetDataBuffer.write(new IntSerializer(packetOut.getType().getId()));
        return packetDataBuffer;
    }

    @Override
    public void decode(PacketDataBuffer packetDataBuffer) {
        packetIn = packetIdentifier.newPacket(packetDataBuffer.read(new IntSerializer()).getInteger());
    }

    public PacketOut getPacketOut() {
        return packetOut;
    }

    public void setPacketOut(PacketOut packetOut) {
        this.packetOut = packetOut;
    }

    public PacketIn getPacketIn() {
        return packetIn;
    }

    public void setPacketIn(PacketIn packetIn) {
        this.packetIn = packetIn;
    }
}
