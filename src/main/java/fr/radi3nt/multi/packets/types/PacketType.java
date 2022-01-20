package fr.radi3nt.multi.packets.types;

import fr.radi3nt.multi.packets.data.Packet;

import java.util.function.Supplier;

public class PacketType {

    private final int id;

    public PacketType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
