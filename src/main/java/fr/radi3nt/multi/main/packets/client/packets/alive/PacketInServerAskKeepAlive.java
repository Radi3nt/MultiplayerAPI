package fr.radi3nt.multi.main.packets.client.packets.alive;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketInServerAskKeepAlive implements PacketIn {

    public static final PacketType PACKET_TYPE = new PacketType(0);

    @Override
    public void load(PacketDataSerializer packetDataSerializer) {

    }

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }
}
