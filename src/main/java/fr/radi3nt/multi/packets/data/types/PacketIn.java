package fr.radi3nt.multi.packets.data.types;

import fr.radi3nt.multi.packets.data.Packet;
import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;

public interface PacketIn extends Packet {

    void load(PacketDataBuffer packetDataBuffer);

}
