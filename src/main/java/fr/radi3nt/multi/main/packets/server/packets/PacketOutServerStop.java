package fr.radi3nt.multi.main.packets.server.packets;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.serializer.types.StringSerializer;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketOutServerStop implements PacketOut {

    public static final PacketType PACKET_TYPE = new PacketType(2);
    private final String reason;

    public PacketOutServerStop(String reason) {
        this.reason = reason;
        if (reason.length()>Short.MAX_VALUE) {
            throw new UnsupportedOperationException("Reason is longer than " + Short.MAX_VALUE);
        }
    }

    @Override
    public void save(PacketDataSerializer packetDataSerializer) {
        packetDataSerializer.write(new StringSerializer(reason));
    }

    @Override
    public int getByteSize() {
        return reason.length()*Character.BYTES+Integer.BYTES;
    }

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }
}
