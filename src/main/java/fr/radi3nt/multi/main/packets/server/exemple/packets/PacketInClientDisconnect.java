package fr.radi3nt.multi.main.packets.server.exemple.packets;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.types.IdPacketType;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketInClientDisconnect implements PacketIn {

    public static final PacketType PACKET_TYPE = new IdPacketType(0);

    @Override
    public void load(PacketDataBuffer packetDataBuffer) {

    }

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }
}
