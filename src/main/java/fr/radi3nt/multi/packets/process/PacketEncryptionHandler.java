package fr.radi3nt.multi.packets.process;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;

public interface PacketEncryptionHandler {

    void encrypt(PacketDataSerializer toEncrypt, PacketDataSerializer toWrite);
    void decrypt(PacketDataSerializer toDecrypt, PacketDataSerializer toWrite);

}
