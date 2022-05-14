package fr.radi3nt.multi.packets.data.serializer.types;

import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;
import fr.radi3nt.multi.packets.data.serializer.types.StringSerializer;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDSerializer implements PacketSerializer {

    private UUID uuid;

    public UUIDSerializer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUIDSerializer() {
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        StringSerializer stringSerializer = new StringSerializer(uuid.toString());
        stringSerializer.serialize(buffer);
    }

    @Override
    public void read(ByteBuffer buffer) {
        StringSerializer stringSerializer = new StringSerializer();
        stringSerializer.read(buffer);
        uuid = UUID.fromString(stringSerializer.getString());
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getBytesSize() {
        return new StringSerializer(uuid.toString()).getBytesSize();
    }
}
