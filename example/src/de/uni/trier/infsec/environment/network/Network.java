package de.uni.trier.infsec.environment.network;

import de.uni.trier.infsec.environment.Environment;


public class Network {

	public static void networkOut(byte[] outEnc) throws NetworkError {
		// input
		Environment.untrustedOuput(0x55);
		Environment.untrustedOuput(outEnc.length);		
		for (int i = 0; i < outEnc.length; i++) {
			Environment.untrustedOuput(outEnc[i]);			
		}
		// output
		if (Environment.untrustedInput()==0) throw new NetworkError();
	}

	public static byte[] networkIn() throws NetworkError {
		// input
		Environment.untrustedOuput(0x56);
		
		// output
		if (Environment.untrustedInput()==0) throw new NetworkError();
		int len = Environment.untrustedInput();
		if (len<0) return null;
		byte[] val = new byte[len];
		for (int i = 0; i < len; i++) {
			val[i] = (byte) Environment.untrustedInput();
		}
		return val;
	}
}
