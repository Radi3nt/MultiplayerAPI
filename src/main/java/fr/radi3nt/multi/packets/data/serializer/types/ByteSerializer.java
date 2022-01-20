package fr.radi3nt.multi.packets.data.serializer.types;

import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;

import java.nio.ByteBuffer;

public class ByteSerializer implements PacketSerializer {

    private byte actualByte;

    public ByteSerializer(byte actualByte) {
        this.actualByte = actualByte;
    }

    public ByteSerializer() {
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.put(actualByte);
    }

    @Override
    public void read(ByteBuffer buffer) {
        actualByte = buffer.get();
    }

    @Override
    public int getBytesSize() {
        return Byte.BYTES;
    }

    public byte getByte() {
        return actualByte;
    }
}
