package de.uni.trier.infsec.functionalities.pkenc.ideal;

import de.uni.trier.infsec.environment.crypto.CryptoLib;
import static de.uni.trier.infsec.utils.MessageTools.getZeroMessage;
import static de.uni.trier.infsec.utils.MessageTools.copyOf;

/**
 * Ideal functionality for public-key encryption: Encryptor
 */
public final class Encryptor {

	private MessagePairList log;
	private byte[] publKey;
	
	Encryptor(MessagePairList mpl, byte[] publicKey) { 
		log = mpl;		
		publKey = publicKey;
	}
		
	public byte[] getPublicKey() {
		return copyOf(publKey);
	}
	
	public byte[] encrypt(byte[] message) {
		byte[] messageCopy = copyOf(message);
		byte[] randomCipher = copyOf(CryptoLib.pke_encrypt(getZeroMessage(1), // note that the length is fixed
													   copyOf(publKey)));
		if( randomCipher == null ) return null;
		log.add(messageCopy, randomCipher);
		return copyOf(randomCipher);
	}
}
