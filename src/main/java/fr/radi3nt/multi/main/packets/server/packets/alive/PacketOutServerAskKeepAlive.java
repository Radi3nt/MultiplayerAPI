package fr.radi3nt.multi.main.packets.server.packets.alive;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.types.PacketType;

public class PacketOutServerAskKeepAlive implements PacketOut {

    public static final PacketType PACKET_TYPE = new PacketType(0);

    @Override
    public void save(PacketDataBuffer packetDataBuffer) {

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
