package de.uni.trier.infsec.protocol;

import de.uni.trier.infsec.pkenc.Decryptor;

final public class Server {
	private Decryptor BobPKE;
	private byte[] receivedMessage = null;

	public Server(Decryptor BobPKE) {
		this.BobPKE = BobPKE;		
	}

	public void onReceive(byte[] message) { 
		receivedMessage = BobPKE.decrypt(message);
	}
}
