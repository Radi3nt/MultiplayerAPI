package fr.radi3nt.multi.packets.process;

import fr.radi3nt.multi.packets.data.serializer.PacketDataSerializer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

public class CipherEncryptionHandler implements PacketEncryptionHandler {

    private final Cipher encrypt;
    private final Cipher decrypt;

    public CipherEncryptionHandler(Cipher cipher, Cipher decrypt) {
        this.encrypt = cipher;
        this.decrypt = decrypt;
    }

    @Override
    public void encrypt(PacketDataSerializer toEncrypt, PacketDataSerializer toWrite) {
        try {
            encrypt.doFinal(toEncrypt.getBuffer(), toWrite.getBuffer());
        } catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
            throw new UnsupportedOperationException("Cannot encrypt", e);
        }
    }

    @Override
    public void decrypt(PacketDataSerializer toDecrypt, PacketDataSerializer toWrite) {
        try {
            decrypt.doFinal(toDecrypt.getBuffer(), toWrite.getBuffer());
        } catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
            throw new UnsupportedOperationException("Cannot decrypt", e);
        }
    }
}
