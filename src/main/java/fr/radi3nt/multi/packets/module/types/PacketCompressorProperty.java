package fr.radi3nt.multi.packets.module.types;

import com.sun.xml.internal.ws.api.message.Packet;
import fr.radi3nt.multi.main.packets.shared.ByteBufferPacketDataBuffer;
import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.module.PacketProperty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.*;

public class PacketCompressorProperty implements PacketProperty {

    private PacketDataBuffer content;

    public PacketCompressorProperty(Deflater deflater, Inflater inflater) {
        this.deflater = deflater;
        this.inflater = inflater;
    }

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
        ByteArrayInputStream in = new ByteArrayInputStream(content.getContent());
        DeflaterInputStream deflater = new DeflaterInputStream(in, new Deflater());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            while ((len = deflater.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            return new ByteBufferPacketDataBuffer(ByteBuffer.wrap(out.toByteArray()));
        } finally {
            out.close();
            deflater.close();
            in.close();
        }
    }

    @Override
    public void decode(PacketDataBuffer packetDataBuffer) {
        content = decode_(packetDataBuffer);
    }

    private PacketDataBuffer decode_(PacketDataBuffer packetDataBuffer) {
        InflaterInputStream gzis = new InflaterInputStream(new ByteArrayInputStream(packetDataBuffer.getContent()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while(gzis.available() > 0) {
            int l = gzis.read(buffer);
            if(l > 0)
                out.write(buffer, 0, l);
        }
        return new ByteBufferPacketDataBuffer(ByteBuffer.wrap(out.toByteArray()));
    }

    public PacketDataBuffer getContent() {
        return content;
    }

    public void setContent(PacketDataBuffer content) {
        this.content = content;
    }
}
