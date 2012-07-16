package de.uni.trier.infsec.crypto;

import de.uni.trier.infsec.environment.Environment;

/**
 *  @author Andreas Koch (University of Trier)
 *  @author Tomasz Truderung (University of Trier)
 */
public class CryptoLib {

	public static byte[] encrypt(byte[] in, byte[] publKey) {
		// input
		Environment.untrustedOuput(0x66); // Function code for encryption
		Environment.untrustedOuput(in.length);
		for (int i = 0; i < in.length; i++) {
			byte b = in[i];
			Environment.untrustedOuput(b);
		}
		Environment.untrustedOuput(publKey.length);
		for (int i = 0; i < publKey.length; i++) {
			byte b = publKey[i];
			Environment.untrustedOuput(b);
		}
		
		// output
		int len = Environment.untrustedInput();
		if (len<0) return null;
		byte[] returnval = new byte[len];
		for (int i = 0; i < len; i++) {
			returnval[i] = (byte) Environment.untrustedInput();
		}
		return returnval;
	}

	public static byte[] decrypt(byte[] message, byte[] privKey) {
		// input
		Environment.untrustedOuput(0x77); // Function code for decryption
		Environment.untrustedOuput(message.length);
		for (int i = 0; i < message.length; i++) {
			byte b = message[i];
			Environment.untrustedOuput(b);			
		}
		Environment.untrustedOuput(privKey.length);
		for (int i = 0; i < privKey.length; i++) {
			byte b = privKey[i];
			Environment.untrustedOuput(b);
		}
		
		// output
		int len = Environment.untrustedInput();
		if (len<0) return null;
		byte[] returnval = new byte[len];
		for (int i = 0; i < len; i++) {
			returnval[i] = (byte) Environment.untrustedInput();
		}
		return returnval;
	}

	public static KeyPair generateKeyPair() {
		// input
		Environment.untrustedOuput(0x88); // Function code for generateKeyPair
		
		// ouptut
		KeyPair returnval = new KeyPair();
		returnval.privateKey = null;
		int len = Environment.untrustedInput();
		if (len>=0) {
			returnval.privateKey = new byte[len];
			for (int i = 0; i < len; i++) {
				returnval.privateKey[i] = (byte) Environment.untrustedInput();
			}
		}
		returnval.publicKey = null;
		len = Environment.untrustedInput();
		if (len>=0) {
			returnval.publicKey= new byte[len];
			for (int i = 0; i < len; i++) {
				returnval.publicKey[i] = (byte) Environment.untrustedInput();
			}
		}
		return returnval;
	}	
}
