package fr.radi3nt.multi.packets.types;

import fr.radi3nt.multi.packets.data.Packet;
import fr.radi3nt.multi.packets.data.types.PacketIn;

public interface PacketIdentifier {

    PacketIn newPacket(int id);

}
