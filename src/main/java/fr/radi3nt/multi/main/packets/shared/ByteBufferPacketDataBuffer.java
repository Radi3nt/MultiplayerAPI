package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;

import java.nio.ByteBuffer;

public class ByteBufferPacketDataBuffer implements PacketDataBuffer {

    private final ByteBuffer buffer;

    public ByteBufferPacketDataBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void write(byte[] bytes) {
        buffer.put(bytes);
    }

    @Override
    public void writeBytes(byte[] array, int offset, int limit) {
        buffer.put(array, offset, limit);
    }

    @Override
    public void write(PacketSerializer packetSerializer) {
        packetSerializer.serialize(buffer);
    }

    @Override
    public void write(PacketDataBuffer packetDataBuffer) {
        buffer.put(packetDataBuffer.getBuffer());
    }

    @Override
    public byte[] read(int ahead) {
        byte[] bytes = new byte[ahead];
        buffer.get(bytes);
        return bytes;
    }

    @Override
    public <T extends PacketSerializer> T read(T packetSerializer) {
        packetSerializer.read(buffer);
        return packetSerializer;
    }

    @Override
    public void clear() {
        buffer.clear();
    }

    @Override
    public void skip(int bytes) {
        buffer.position(buffer.position()+bytes);
    }

    @Override
    public int getSize() {
        return buffer.limit();
    }

    @Override
    public byte[] getContent() {
        int pos = buffer.position();
        buffer.position(0);
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);

        buffer.position(pos);
        return bytes;
    }

    @Override
    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public boolean hasBytes() {
        return buffer.remaining()>0;
    }
}
