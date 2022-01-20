package fr.radi3nt.multi.packets.process.recieving;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.types.PacketIdentifier;

public class PacketDecoder {

    private final PacketIdentifier packetIdentifier;

    public PacketDecoder(PacketIdentifier packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public PacketIn decode(PacketDataSerializer packetDataSerializer) {
        int packetId = packetDataSerializer.read(new IntSerializer()).getInteger();
        PacketIn packet = packetIdentifier.newPacket(packetId);
        packet.load(packetDataSerializer);
        return packet;
    }

}
