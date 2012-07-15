package de.uni.trier.infsec.functionalities.pkenc.ideal;

import de.uni.trier.infsec.environment.crypto.CryptoLib;
import de.uni.trier.infsec.environment.crypto.KeyPair;
import static de.uni.trier.infsec.utils.MessageTools.copyOf;


/**
 * Ideal functionality for public-key encryption: Decryptor
 */
public final class Decryptor {
	
	private byte[] privKey; 
	private byte[] publKey;
	private MessagePairList log;

	public Decryptor() {
		KeyPair keypair = CryptoLib.generateKeyPair();
		publKey = copyOf(keypair.publicKey);  
		privKey = copyOf(keypair.privateKey); 
	}

    public Encryptor getEncryptor() {
        return new Encryptor(log,publKey);
    }

	public byte[] decrypt(byte[] message) {
		byte[] messageCopy = copyOf(message); 
		if (!log.contains(messageCopy)) {
			return copyOf( CryptoLib.pke_decrypt(copyOf(privKey), messageCopy) );
		} else {
			return copyOf( log.lookup(messageCopy) );
		}
	}
}
