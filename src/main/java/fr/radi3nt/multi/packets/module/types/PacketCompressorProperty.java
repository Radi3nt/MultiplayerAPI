package fr.radi3nt.multi.packets.module.types;

import fr.radi3nt.multi.main.packets.shared.ByteBufferPacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.types.IntSerializer;
import fr.radi3nt.multi.packets.module.PacketProperty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.*;

public class PacketCompressorProperty implements PacketProperty {

    private final ByteArrayOutputStream encodeOut = new ByteArrayOutputStream();
    private final ByteArrayOutputStream decodeOut = new ByteArrayOutputStream();
    private PacketDataBuffer contentToSend;
    private PacketDataBuffer contentToReceive;

    @Override
    public PacketDataBuffer encode() {
        try {
            return encode_();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized PacketDataBuffer encode_() throws IOException {
        int len;
        byte[] buff = new byte[4096];
        ByteArrayInputStream in = new ByteArrayInputStream(contentToSend.getContent());
        DeflaterInputStream deflater = new DeflaterInputStream(in, new Deflater());

        try {
            while ((len = deflater.read(buff)) > 0) {
                encodeOut.write(buff, 0, len);
            }
            PacketDataBuffer packetDataBuffer = new ByteBufferPacketDataBuffer(ByteBuffer.allocate(len+Integer.BYTES));
            packetDataBuffer.write(new IntSerializer(contentToSend.getSize()));
            packetDataBuffer.write(encodeOut.toByteArray());
            return packetDataBuffer;
        } finally {
            encodeOut.reset();
            deflater.close();
            in.close();
        }
    }

    @Override
    public void decode(PacketDataBuffer packetDataBuffer) {
        contentToReceive = null;
        try {
            contentToReceive = decode_(packetDataBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized PacketDataBuffer decode_(PacketDataBuffer packetDataBuffer) throws IOException {
        int decompressedSize = packetDataBuffer.read(new IntSerializer()).getInteger();

        InflaterInputStream inflatorStream = new InflaterInputStream(new ByteArrayInputStream(packetDataBuffer.read(decompressedSize)));
        byte[] buffer = new byte[decompressedSize];
        while(inflatorStream.available() > 0) {
            int l = inflatorStream.read(buffer);
            if(l > 0)
                decodeOut.write(buffer, 0, l);
        }
        ByteBufferPacketDataBuffer byteBufferPacketDataBuffer = new ByteBufferPacketDataBuffer(ByteBuffer.wrap(decodeOut.toByteArray()));
        decodeOut.reset();
        return byteBufferPacketDataBuffer;
    }

    public void setContentToSend(PacketDataBuffer contentToSend) {
        this.contentToSend = contentToSend;
    }

    public PacketDataBuffer getContentToReceive() {
        return contentToReceive;
    }
}
