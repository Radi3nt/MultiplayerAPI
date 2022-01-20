package fr.radi3nt.multi.packets.data.serializer.types;

import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;

import java.nio.ByteBuffer;

public class CharSerializer implements PacketSerializer {

    private char character;

    public CharSerializer(char character) {
        this.character = character;
    }

    public CharSerializer() {

    }

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.putChar(character);
    }

    @Override
    public void read(ByteBuffer buffer) {
        character = buffer.getChar();
    }

    @Override
    public int getBytesSize() {
        return Character.BYTES;
    }

    public char getChar() {
        return character;
    }
}
