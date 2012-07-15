package de.uni.trier.infsec.lib.crypto;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.uni.trier.infsec.environment.crypto.KeyPair;

public class Encryption {

	private static int pkKeySize = 1024;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static byte[] encrypt(byte[] message, byte[] publicKey) {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
			//for private keys use PKCS8EncodedKeySpec; for public keys use X509EncodedKeySpec
			X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKey);
			PublicKey pk = kf.generatePublic(ks);

			Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			c.init(Cipher.ENCRYPT_MODE, pk);
			byte[] out = c.doFinal(message);
			return out;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(byte[] message, byte[] privKey) {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
			//for private keys use PKCS8EncodedKeySpec; for public keys use X509EncodedKeySpec
			PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(privKey);
			PrivateKey pk = kf.generatePrivate(ks);
			Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			c.init(Cipher.DECRYPT_MODE, pk);
			byte[] out = c.doFinal(message);
			return out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static KeyPair generateKeyPair() {
		KeyPair out = new KeyPair();
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA", "BC");
			keyPairGen.initialize(pkKeySize);
			java.security.KeyPair pair = keyPairGen.generateKeyPair();
			out.privateKey = pair.getPrivate().getEncoded();
			out.publicKey  = pair.getPublic().getEncoded();
			return out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
