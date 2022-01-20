package fr.radi3nt.multi.main.packets.client.packets.alive;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketOutResponseKeepAlive implements PacketOut {

    public static final PacketType PACKET_TYPE = new PacketType(1);

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
