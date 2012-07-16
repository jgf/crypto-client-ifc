package de.uni.trier.infsec.protocol;

import de.uni.trier.infsec.network.Network;
import de.uni.trier.infsec.network.NetworkError;
import de.uni.trier.infsec.pkenc.Encryptor;

final public class Client {
	private Encryptor BobPKE;
	private byte[] message;

	public Client(Encryptor BobPKE, byte message) {
		this.BobPKE = BobPKE;
		this.message = new byte[] {message}; 
	}

	public void onInit() throws NetworkError {
		byte[] encMessage = BobPKE.encrypt(message);
		Network.networkOut(encMessage);
	}
}
