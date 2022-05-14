package fr.radi3nt.multi.packets.data.serializer.types;

import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;

import java.nio.ByteBuffer;

public class LongSerializer implements PacketSerializer {

    private long aLong;

    public LongSerializer(long aLong) {
        this.aLong = aLong;
    }

    public LongSerializer() {
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.putLong(aLong);
    }

    @Override
    public void read(ByteBuffer buffer) {
        aLong = buffer.getLong();
    }

    public void setLong(long aLong) {
        this.aLong = aLong;
    }

    public long getLong() {
        return aLong;
    }

    @Override
    public int getBytesSize() {
        return Long.BYTES;
    }
}
