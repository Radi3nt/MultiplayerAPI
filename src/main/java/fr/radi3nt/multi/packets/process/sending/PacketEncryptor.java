package fr.radi3nt.multi.packets.process.sending;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;
import fr.radi3nt.multi.packets.process.PacketEncryptionHandler;

public class PacketEncryptor {

    private final PacketEncryptionHandler packetEncryptionHandler;

    public PacketEncryptor(PacketEncryptionHandler packetEncryptionHandler) {
        this.packetEncryptionHandler = packetEncryptionHandler;
    }

    public void encrypt(PacketDataSerializer data, PacketDataSerializer toWrite) {
        packetEncryptionHandler.encrypt(data, toWrite);
    }

}
