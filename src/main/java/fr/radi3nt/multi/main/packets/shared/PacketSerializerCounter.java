package fr.radi3nt.multi.main.packets.shared;

public class PacketSerializerCounter {

    public static int bytesByte() {
        return Byte.BYTES;
    }
    public static int bytesBytes(int length) {
        return Integer.BYTES+Byte.BYTES*length;
    }
    public static int bytesFloat() {
        return Float.BYTES;
    }
    public static int bytesBoolean() {
        return Byte.BYTES;
    }
    public static int bytesInt() {
        return Integer.BYTES;
    }
    public static int bytesDouble() {
        return Double.BYTES;
    }
    public static int byteLong() {
        return Long.BYTES;
    }
    public static int byteString(int length) {
        return Integer.BYTES+Character.BYTES*length;
    }

}
