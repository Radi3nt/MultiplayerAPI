package fr.radi3nt.multi.packets.process.sending;

import fr.radi3nt.multi.main.packets.shared.ByteBufferPacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.ByteSerializer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;

import java.nio.ByteBuffer;
import java.util.zip.Deflater;

public class PacketCompressor {

    private final byte[] resultArray = new byte[8192];
    private final Deflater deflater;

    public PacketCompressor() {
        this.deflater = new Deflater();
    }

    public PacketDataBuffer compress(PacketDataBuffer toCompress) {
        this.deflater.setInput(toCompress.getContent(), 0, toCompress.getSize());
        this.deflater.finish();

        int actualBytesSize = 0;

        while(!this.deflater.finished()) {
            actualBytesSize += this.deflater.deflate(this.resultArray, actualBytesSize, this.resultArray.length-actualBytesSize); //todo when packet too big, infinite loop ?
        }


        PacketDataBuffer writted = new ByteBufferPacketDataBuffer(ByteBuffer.allocate(actualBytesSize+Integer.BYTES+Byte.BYTES));
        writted.write(new IntSerializer(actualBytesSize+Byte.BYTES));
        writted.write(new ByteSerializer((byte) 0x01));
        writted.writeBytes(this.resultArray, 0, actualBytesSize);

        this.deflater.reset();
        return writted;
    }

}
