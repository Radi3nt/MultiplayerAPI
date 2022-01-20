package fr.radi3nt.multi.packets.process.sending;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketOut;

public class PacketEncoder {

    public void encode(PacketOut packet, PacketDataSerializer packetDataSerializer) {
        packetDataSerializer.write(new IntSerializer(packet.getType().getId()));
        packet.save(packetDataSerializer);
    }

}
