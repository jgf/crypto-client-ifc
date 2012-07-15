package de.uni.trier.infsec.lib.test;

import java.util.Arrays;

import junit.framework.TestCase;
import de.uni.trier.infsec.lib.crypto.Encryption;

import org.junit.Test;

import de.uni.trier.infsec.environment.crypto.KeyPair;

public class TestCrypto extends TestCase {

	
	public static byte[] TEST_DATA = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
	
	@Test
	public void testCrypto() {
		
		KeyPair kp = Encryption.generateKeyPair();
		byte[] pubKey = kp.publicKey;
		byte[] privKey = kp.privateKey;
		
		byte[] enc = Encryption.encrypt(TEST_DATA, pubKey);
		byte[] dec = Encryption.decrypt(enc, privKey);
		
		assertTrue(Arrays.equals(TEST_DATA, dec));
	}
	
}
