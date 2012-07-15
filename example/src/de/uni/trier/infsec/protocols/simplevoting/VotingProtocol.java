package de.uni.trier.infsec.protocols.simplevoting;

import java.security.SecureRandom;
import java.util.ArrayList;

import org.bouncycastle.crypto.CryptoException;

import de.uni.trier.infsec.functionalities.pkenc.ideal.Decryptor;
import de.uni.trier.infsec.lib.crypto.PublicKeyRepository;
import de.uni.trier.infsec.protocols.simplevoting.VotingServer.NodeList;

public class VotingProtocol {
	public enum Votes {
		Candidate1, Candidate2, Candidate3
	}

	public static int VOTERS_COUNT = 10;

	private ArrayList<Voter> voterList 	= new ArrayList<Voter>();
	private NodeList credentials 		= null;
	private Decryptor serverEnc 		= null;

	public static void main(String[] args) throws Exception {
		VotingProtocol protocol = new VotingProtocol();
		protocol.prepareProtocol();
		protocol.run();
	}

	private void prepareProtocol() throws Exception {
		serverEnc = new Decryptor();
		PublicKeyRepository.registerPublicKey("SERVER", serverEnc.getEncryptor());

		for (int i = 0; i < VOTERS_COUNT; i++) {
			String identifier = "Voter" + i;
			
			Decryptor privKeyVoter = new Decryptor();
			PublicKeyRepository.registerPublicKey(identifier, privKeyVoter.getEncryptor());

			byte[] credential = generateNonce(128); // TODO: Length of Nonce?
			if (credentials == null) {
				credentials = new NodeList(credential, null);
			} else {				
				credentials.add(credential);
			}
			Votes vote = Votes.values()[(int) (Math.random() * 3) % 3];
			byte[] credentialEnc = PublicKeyRepository.getPublicKey(identifier).encrypt(credential);
			byte[] mVote = vote.toString().getBytes();
			
			Voter voter = new Voter(privKeyVoter, credentialEnc, PublicKeyRepository.getPublicKey("SERVER"), mVote);
			voterList.add(voter);
		}
		System.out.println("STARTING PROTOCOL\n");
	}

	private void run() throws Exception, InterruptedException, CryptoException {
		VotingServer server = new VotingServer(serverEnc, credentials);

		for (Voter voter : voterList) {
			voter.vote();
		}
		
		server.collectVotes();
	}
	
	public static byte[] generateNonce(int length) {
		SecureRandom random = new SecureRandom();
		byte[] out = new byte[length];
		random.nextBytes(out);
		return out;
	}
}
