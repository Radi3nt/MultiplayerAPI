package fr.radi3nt.multi.packets.data.serializer.types;

import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;

import java.nio.ByteBuffer;

public class FloatSerializer implements PacketSerializer {

    private float aFloat;

    public FloatSerializer(float aFloat) {
        this.aFloat = aFloat;
    }

    public FloatSerializer() {
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.putFloat(aFloat);
    }

    @Override
    public void read(ByteBuffer buffer) {
        aFloat = buffer.getFloat();
    }


    public float getFloat() {
        return aFloat;
    }

    @Override
    public int getBytesSize() {
        return Float.SIZE;
    }
}
