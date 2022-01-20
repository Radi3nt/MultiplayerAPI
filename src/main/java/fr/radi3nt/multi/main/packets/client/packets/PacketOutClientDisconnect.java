package fr.radi3nt.multi.main.packets.client.packets;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketOutClientDisconnect implements PacketOut {

    private static final PacketType PACKET_TYPE = new PacketType(3);

    @Override
    public void save(PacketDataSerializer packetDataSerializer) {

    }

    @Override
    public int getByteSize() {
        return 0;
    }

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }
}
