package fr.radi3nt.multi.packets.data.serializer.types;

import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;

import java.nio.ByteBuffer;

public class IntSerializer implements PacketSerializer {

    private int integer;

    public IntSerializer(int integer) {
        this.integer = integer;
    }

    public IntSerializer() {
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.putInt(integer);
    }

    @Override
    public void read(ByteBuffer buffer) {
        integer = buffer.getInt();
    }

    @Override
    public int getBytesSize() {
        return Integer.BYTES;
    }

    public int getInteger() {
        return integer;
    }
}
