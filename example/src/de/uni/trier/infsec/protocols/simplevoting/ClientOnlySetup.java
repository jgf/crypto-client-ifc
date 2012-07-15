package de.uni.trier.infsec.protocols.simplevoting;

import de.uni.trier.infsec.environment.network.Network;
import de.uni.trier.infsec.environment.network.NetworkError;
import de.uni.trier.infsec.functionalities.pkenc.ideal.Decryptor;
import de.uni.trier.infsec.functionalities.pkenc.ideal.Encryptor;


// Creates one client and make her vote.
//
public class ClientOnlySetup {
	
	static private boolean secret = false; // SECRET -- an arbitrary value put here

	public static void main(String args[]) throws NetworkError 
	{	
		// generate keys: 
		Decryptor voterDec = new Decryptor();
		Encryptor voterEnc = voterDec.getEncryptor();
		Decryptor serverDec = new Decryptor();
		Encryptor serverEnc = serverDec.getEncryptor();
	
		// publish public keys:
		Network.networkOut(serverEnc.getPublicKey());
		Network.networkOut(voterEnc.getPublicKey());
		
		// determine the value the client encrypts:
		byte vote1 = Network.networkIn()[0]; // the adversary gives two values (bytes)
		byte vote2 = Network.networkIn()[0];
		byte vote[] = new byte[1]; 
		vote[0] = vote2;    // and one of them is picked as the vote (depending on the value of the secret bit)
		if (secret) {
			try {
				vote[0] = vote1;
			} catch (Throwable t) {}
		}

		// let the adversary determine the credential (even in this case the voter choice should remain secret)
		byte[] credential = Network.networkIn();
		
		Voter voter = new Voter(voterDec, credential, serverEnc, vote);
		voter.vote();
	}
}
