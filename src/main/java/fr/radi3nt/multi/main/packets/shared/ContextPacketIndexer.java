package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.types.PacketIdentifier;
import fr.radi3nt.multi.packets.types.PacketType;

import java.util.*;
import java.util.function.Supplier;

public class ContextPacketIndexer implements PacketIdentifier {

    private final Map<PacketType, Supplier<PacketIn>> packetTypes = new HashMap<>();

    @Override
    public PacketIn newPacket(int id) {
        for (PacketType packetType : packetTypes.keySet()) {
            if (packetType.getId()==id) {
                return packetTypes.get(packetType).get();
            }
        }
        return null;
    }

    public void addInPacketType(PacketType packetType, Supplier<PacketIn> supplier) {
        packetTypes.put(packetType, supplier);
    }
}
