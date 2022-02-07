package fr.radi3nt.multi.main.packets.client.packets;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.StringSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketInServerStop implements PacketIn {

    public static final PacketType PACKET_TYPE = new PacketType(2);
    private String reason;

    @Override
    public void load(PacketDataBuffer packetDataBuffer) {
        reason = packetDataBuffer.read(new StringSerializer()).getString();
    }

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }

    public String getReason() {
        return reason;
    }
}
