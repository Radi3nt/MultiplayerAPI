package fr.radi3nt.multi.packets.module.list;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;

public interface PacketPropertyList {

    PacketDataBuffer encode(PacketOut packet);
    PacketIn decode(PacketDataBuffer steam);

}
