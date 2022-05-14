package fr.radi3nt.multi.packets.types;

public class IdPacketType implements PacketType {

    private final int id;

    public IdPacketType(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
