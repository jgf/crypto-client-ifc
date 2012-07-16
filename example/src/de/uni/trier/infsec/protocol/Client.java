package de.uni.trier.infsec.protocol;

import de.uni.trier.infsec.network.Network;
import de.uni.trier.infsec.network.NetworkError;
// import de.uni.trier.infsec.pkenc.PKEnc;
import de.uni.trier.infsec.pkenc.Encryptor;

/**
 *  Client of a simple protocol: it encrypts a given message and sends
 *  it over the network.
 * 
 *
 *  @author Andreas Koch (University of Trier)
 *  @author Tomasz Truderung (University of Trier)
 */
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
