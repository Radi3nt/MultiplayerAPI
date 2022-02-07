package fr.radi3nt.multi.packets.process.recieving;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.process.PacketEncryptionHandler;

public class PacketDecrypter {

    private final PacketEncryptionHandler packetEncryptionHandler;

    public PacketDecrypter(PacketEncryptionHandler packetEncryptionHandler) {
        this.packetEncryptionHandler = packetEncryptionHandler;
    }

    public void decrypt(PacketDataBuffer data, PacketDataBuffer toWrite) {
        packetEncryptionHandler.decrypt(data, toWrite);
    }

}
