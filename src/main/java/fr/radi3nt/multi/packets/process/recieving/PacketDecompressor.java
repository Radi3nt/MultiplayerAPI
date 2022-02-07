package fr.radi3nt.multi.packets.process.recieving;

import fr.radi3nt.multi.main.packets.shared.ByteBufferPacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PacketDecompressor {

    private final byte[] resultArray = new byte[1000000]; //todo can limit things....
    private final Inflater inflater;

    public PacketDecompressor(Inflater inflater) {
        this.inflater = inflater;
    }

    public PacketDataBuffer decompress(PacketDataBuffer toCompress) throws DataFormatException {
        this.inflater.setInput(toCompress.getContent(), 0, toCompress.getSize());

        int actualBytesSize = 0;
        while(!this.inflater.finished()) {
            actualBytesSize += this.inflater.inflate(this.resultArray, actualBytesSize, this.resultArray.length-actualBytesSize);
        }
        PacketDataBuffer written = new ByteBufferPacketDataBuffer(ByteBuffer.allocate(actualBytesSize));
        written.writeBytes(this.resultArray, 0, actualBytesSize);

        this.inflater.reset();
        return written;
    }

}
