package fr.radi3nt.multi.packets.process.recieving;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.process.PacketEncryptionHandler;

public class PacketDecrypter {

    private final PacketEncryptionHandler packetEncryptionHandler;

    public PacketDecrypter(PacketEncryptionHandler packetEncryptionHandler) {
        this.packetEncryptionHandler = packetEncryptionHandler;
    }

    public void decrypt(PacketDataSerializer data, PacketDataSerializer toWrite) {
        packetEncryptionHandler.decrypt(data, toWrite);
    }

}
