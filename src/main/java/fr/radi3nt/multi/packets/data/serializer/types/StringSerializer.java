package fr.radi3nt.multi.packets.data.serializer.types;

import fr.radi3nt.multi.packets.data.serializer.PacketSerializer;

import java.nio.ByteBuffer;

public class StringSerializer implements PacketSerializer {

    private String string;

    public StringSerializer(String string) {
        this.string = string;
    }

    public StringSerializer() {
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        IntSerializer intSerializer = new IntSerializer(string.length());
        intSerializer.serialize(buffer);

        for (int i = 0; i < string.length(); i++) {
            CharSerializer charSerializer = new CharSerializer(string.charAt(i));
            charSerializer.serialize(buffer);
        }

    }

    @Override
    public void read(ByteBuffer buffer) {
        IntSerializer intSerializer = new IntSerializer();
        intSerializer.read(buffer);
        int size = intSerializer.getInteger();

        string = "";

        CharSerializer charSerializer = new CharSerializer();
        for (int i = 0; i < size; i++) {
            charSerializer.read(buffer);
            string += charSerializer.getChar();
        }
    }

    @Override
    public int getBytesSize() {
        return string.length()*Character.BYTES+Integer.BYTES;
    }

    public String getString() {
        return string;
    }
}
