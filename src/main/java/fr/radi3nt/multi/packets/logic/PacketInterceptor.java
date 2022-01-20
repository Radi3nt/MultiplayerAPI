package fr.radi3nt.multi.packets.logic;

import fr.radi3nt.multi.packets.data.Packet;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;

public interface PacketInterceptor {

    PacketIn onPacketIn(PacketIn packet);
    PacketOut onPacketOut(PacketOut packet);

}
