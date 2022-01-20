package fr.radi3nt.multi.packets.process.recieving;

import fr.radi3nt.multi.main.packets.shared.ByteBufferPacketDataSerializer;
import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PacketDecompressor {

    private final byte[] resultArray = new byte[1000000]; //todo can limit things....
    private final Inflater inflater;

    public PacketDecompressor(Inflater inflater) {
        this.inflater = inflater;
    }

    public PacketDataSerializer decompress(PacketDataSerializer toCompress) throws DataFormatException {
        this.inflater.setInput(toCompress.getContent(), 0, toCompress.getSize());

        int actualBytesSize = 0;
        while(!this.inflater.finished()) {
            actualBytesSize += this.inflater.inflate(this.resultArray, actualBytesSize, this.resultArray.length-actualBytesSize);
        }
        PacketDataSerializer written = new ByteBufferPacketDataSerializer(ByteBuffer.allocate(actualBytesSize));
        written.writeBytes(this.resultArray, 0, actualBytesSize);

        this.inflater.reset();
        return written;
    }

}
