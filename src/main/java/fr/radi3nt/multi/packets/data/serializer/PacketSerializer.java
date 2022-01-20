package fr.radi3nt.multi.packets.data.serializer;

import java.nio.ByteBuffer;

public interface PacketSerializer {

    void serialize(ByteBuffer buffer);
    void read(ByteBuffer buffer);

    int getBytesSize();

}
