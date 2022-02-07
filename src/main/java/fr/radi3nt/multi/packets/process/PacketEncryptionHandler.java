package fr.radi3nt.multi.packets.process;

import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;

public interface PacketEncryptionHandler {

    void encrypt(PacketDataBuffer toEncrypt, PacketDataBuffer toWrite);
    void decrypt(PacketDataBuffer toDecrypt, PacketDataBuffer toWrite);

}
