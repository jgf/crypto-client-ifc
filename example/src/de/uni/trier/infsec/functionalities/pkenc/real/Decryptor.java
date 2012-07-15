package de.uni.trier.infsec.functionalities.pkenc.real;

import de.uni.trier.infsec.environment.crypto.KeyPair;
import de.uni.trier.infsec.lib.crypto.Encryption;

/**
 * Real functionality for public-key encryption: Decryptor
 */
public final class Decryptor {
	
	private byte[] privKey = null; 
	private byte[] publKey = null;

	public Decryptor() {
		KeyPair keypair = Encryption.generateKeyPair();
		publKey = keypair.publicKey;  
		privKey = keypair.privateKey; 
	}

    public Encryptor getEncryptor() {
        return new Encryptor(publKey);
    }

	public byte[] decrypt(byte[] message) {
		return Encryption.decrypt(privKey, message);
	}
}
