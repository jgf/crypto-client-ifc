package de.uni.trier.infsec.protocols.simpleprotocol;

import de.uni.trier.infsec.environment.network.Network;
import de.uni.trier.infsec.environment.network.NetworkError;
import de.uni.trier.infsec.functionalities.pkenc.ideal.Decryptor;
import de.uni.trier.infsec.functionalities.pkenc.ideal.Encryptor;

public class Setup {
	
	static private boolean secret = false; // SECRET -- an arbitrary value put here
		
	public static void main(String[] args) throws NetworkError {		
				
		// Public-key encryption functionality for Server 
		Decryptor serverDec = new Decryptor();
		Encryptor serverEnc = serverDec.getEncryptor();
		Network.networkOut(serverEnc.getPublicKey()); // the public key of Bob is published
		
		// Creating the server
		Server server = new Server(serverDec);
		
		// The adversary decides how many clients we create:
		while( Network.networkIn() != null )  { 
			// determine the value the client encrypts:
			// the adversary gives two values
			byte s1 = Network.networkIn()[0]; 
			byte s2 = Network.networkIn()[0];
			// and one of them is picked depending on the value of the secret bit
			byte s = secret ? s1 : s2; 
			Client client = new Client(serverEnc, s);
			
			// initialize the client protocol (Alice sends out an encrypted value s to the network)
			client.onInit();
			// read a message from the network...
			byte[] message = Network.networkIn();
			// ... and deliver it to the server (server will decrypt it)
			server.onReceive(message);			
		}
	}
}
