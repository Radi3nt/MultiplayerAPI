package fr.radi3nt.multi.packets.data.serializer;

import java.nio.ByteBuffer;

public interface PacketDataSerializer {

    void write(byte[] bytes);
    void writeBytes(byte[] array, int offset, int limit);
    void write(PacketSerializer packetSerializer);

    byte[] read(int ahead);
    <T extends PacketSerializer> T read(T packetSerializer);

    void reset();
    void skip(int bytes);

    int getSize();
    byte[] getContent();
    ByteBuffer getBuffer();

    boolean hasBytes();
}
