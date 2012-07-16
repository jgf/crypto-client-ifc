package de.uni.trier.infsec.pkenc;

import de.uni.trier.infsec.crypto.CryptoLib;
import de.uni.trier.infsec.crypto.KeyPair;

/**
 * Ideal functionality for public-key encryption: Decryptor
 */
public final class Decryptor {
	
	private byte[] privKey; 
	private byte[] publKey;
	private MessagePairList log;

	public Decryptor() {
		KeyPair keypair = CryptoLib.generateKeyPair();
		publKey = MessageTools.copyOf(keypair.publicKey);  
		privKey = MessageTools.copyOf(keypair.privateKey); 
	}

    public Encryptor getEncryptor() {
        return new Encryptor(log,publKey);
    }

	public byte[] decrypt(byte[] message) {
		byte[] messageCopy = MessageTools.copyOf(message); 
		if (!log.contains(messageCopy)) {
			return MessageTools.copyOf( CryptoLib.decrypt(MessageTools.copyOf(privKey), messageCopy) );
		} else {
			return MessageTools.copyOf( log.lookup(messageCopy) );
		}
	}
}
