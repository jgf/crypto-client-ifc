package de.uni.trier.infsec.lib.crypto;

import java.util.HashMap;

import de.uni.trier.infsec.functionalities.pkenc.ideal.Encryptor;

/**
 * 
 * We do not explicitly handle key distribution; instead we assume some
 * public-key infrastructure. PublicKeyRepository encapsulates this
 * infrastructure allowing everyone to obtain the public key (more precisely, his
 * public interface) of every participant using his/her identifier. 
 * 
 * Each participant must register his public interface to his identifier. 
 */
public class PublicKeyRepository {

	private static HashMap<String, Encryptor> map = new HashMap<String, Encryptor>();
	
	public static Encryptor getPublicKey(String identifier) {
		return map.get(identifier);
	}
	
	public static void registerPublicKey(String identifier, Encryptor publicKey) {
		map.put(identifier, publicKey);
	}
	
}
