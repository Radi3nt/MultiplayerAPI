package fr.radi3nt.multi.packets.process.sending;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.data.types.PacketOut;

public class PacketEncoder {

    public void encode(PacketOut packet, PacketDataBuffer packetDataBuffer) {
        packetDataBuffer.write(new IntSerializer(packet.getType().getId()));
        packet.save(packetDataBuffer);
    }

}
