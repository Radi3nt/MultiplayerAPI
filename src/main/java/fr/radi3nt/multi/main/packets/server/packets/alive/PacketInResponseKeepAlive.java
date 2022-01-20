package fr.radi3nt.multi.main.packets.server.packets.alive;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketInResponseKeepAlive implements PacketIn {

    public static final PacketType PACKET_TYPE = new PacketType(1);

    @Override
    public void load(PacketDataSerializer packetDataSerializer) {

    }

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }
}
