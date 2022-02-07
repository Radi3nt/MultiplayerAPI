package fr.radi3nt.multi.packets.process.recieving;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.types.PacketIdentifier;

public class PacketDecoder {

    private final PacketIdentifier packetIdentifier;

    public PacketDecoder(PacketIdentifier packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public PacketIn decode(PacketDataBuffer packetDataBuffer) {
        int packetId = packetDataBuffer.read(new IntSerializer()).getInteger();
        PacketIn packet = packetIdentifier.newPacket(packetId);
        packet.load(packetDataBuffer);
        return packet;
    }

}
