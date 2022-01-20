package fr.radi3nt.multi.packets.logic;

import fr.radi3nt.multi.packets.data.Packet;
import fr.radi3nt.multi.packets.data.types.PacketOut;

public interface PacketProtocol {
    
    void sendPacket(PacketOut packet);

    void addInterceptor(PacketInterceptor packetInterceptor);
    void removeInterceptor(PacketInterceptor packetInterceptor);

}
