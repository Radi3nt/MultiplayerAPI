package fr.radi3nt.multi.packets.data.serializer;

import java.nio.ByteBuffer;

public interface PacketSerializer { //todo separate in two classes (read / write)

    void serialize(ByteBuffer buffer);
    void read(ByteBuffer buffer);

    int getBytesSize();

}
