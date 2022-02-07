package fr.radi3nt.multi.packets.module;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;

public interface PacketProperty {

    PacketDataBuffer encode();
    void decode(PacketDataBuffer packetDataBuffer);

}
