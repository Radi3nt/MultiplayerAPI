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

    private PacketDataBuffer encode_() throws IOException {
        int len;
        byte[] buff = new byte[64];
        ByteArrayInputStream in = new ByteArrayInputStream(contentToSend.getContent());
        DeflaterInputStream deflater = new DeflaterInputStream(in, new Deflater());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            while ((len = deflater.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            PacketDataBuffer packetDataBuffer = new ByteBufferPacketDataBuffer(ByteBuffer.allocate(len+Integer.BYTES));
            packetDataBuffer.write(new IntSerializer(contentToSend.getSize()));
            packetDataBuffer.write(out.toByteArray());
            return packetDataBuffer;
        } finally {
            out.close();
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

    private PacketDataBuffer decode_(PacketDataBuffer packetDataBuffer) throws IOException {
        int decompressedSize = packetDataBuffer.read(new IntSerializer()).getInteger();

        InflaterInputStream gzis = new InflaterInputStream(new ByteArrayInputStream(packetDataBuffer.read(decompressedSize)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while(gzis.available() > 0) {
            int l = gzis.read(buffer);
            if(l > 0)
                out.write(buffer, 0, l);
        }
        return new ByteBufferPacketDataBuffer(ByteBuffer.wrap(out.toByteArray()));
    }



    public void setContentToSend(PacketDataBuffer contentToSend) {
        this.contentToSend = contentToSend;
    }

    public PacketDataBuffer getContentToReceive() {
        return contentToReceive;
    }
}
