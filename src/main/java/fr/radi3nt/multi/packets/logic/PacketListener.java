package fr.radi3nt.multi.packets.logic;

import fr.radi3nt.multi.packets.data.Packet;

public interface PacketListener<T extends Packet> {

    void listen(T packet);

}
