package fr.radi3nt.multi.packets.module;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;

public interface PacketDataBufferProvider {

    PacketDataBuffer createBuffer(int size);

}
