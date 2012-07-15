package de.uni.trier.infsec.protocols.simplevoting;

import de.uni.trier.infsec.environment.network.Network;
import de.uni.trier.infsec.environment.network.NetworkError;
import de.uni.trier.infsec.functionalities.pkenc.ideal.Decryptor;
import de.uni.trier.infsec.protocols.simplevoting.VotingProtocol.Votes;
import de.uni.trier.infsec.utils.MessageTools;

public class VotingServer {

	private Decryptor serverDec = null;
	
	static class NodeMap {
		byte[] valueA;
		byte[] valueB;
		NodeMap next;
		NodeMap(byte[] vA, byte[] vB , NodeMap n) {
			valueA = vA; valueB = vB; next = n;
		}
		
		void put(byte[] vA, byte[] vB) {
			if (valueA == vA) valueB = vB; 
			else if (next != null) next.put(vA, vB); 
			else next = new NodeMap(vA, vB, null);
		}
		
		void clear() { valueA = null; valueB = null; if (next != null) next.clear(); next = null;}
	}
	
	static class NodeList {
		byte[] value;
		NodeList next;
		NodeList(byte[] v, NodeList n) {
			value = v; next = n;
		}
		
		void add(byte[] v) {
			if (next != null) next.add(v); 
			else next = new NodeList(v, null);
		}
		void clear() { value = null; if (next != null) next.clear(); next = null;}
	}
	
	static class NodeCount {
		byte[] value;
		int count;
		NodeCount next;
		NodeCount(byte[] v, NodeCount n, int c) {
			value = v; next = n; count = c;
		}
		
		void inc(byte[] v) {
			if (value == v) count++; 
			else if (next != null) next.inc(v); 
			else next = new NodeCount(v, null, 1);
		}
		void clear() { value = null; count = 0; if (next != null) next.clear(); next = null;}
	}
	
	

	private NodeMap 	votes 	 	= null;
	private NodeList 	credentials = null;
	private NodeList 	ballot 	 	= null;
	private NodeCount 	result 		= null;

	public VotingServer(Decryptor serverDec, NodeList credentials) {
		this.credentials = credentials;
		this.serverDec = serverDec;
	}

	public void collectVotes() throws NetworkError {
		///////// COLLECTING VOTES /////////
		byte[] in = Network.networkIn();
		while (in != null && in.length != 0) {
			if (in != null) {
				System.out.println("[SERVER]\tReceived vote:\t" + in.toString());
				if (ballot == null) {
					ballot = new NodeList(in, null);
				} else {					
					ballot.add(in);
				}
			}
			in = Network.networkIn();
		}
		
		decryptVotes();
		countAndPublish();
	}

	private void countAndPublish() {
		NodeList credential = credentials;
		
		while (credential != null) { // check for each credential if a vote has been casted			
			byte[] theVote = null;
			
			NodeMap voteTmp = votes;
			while (voteTmp != null) {
				if (voteTmp.valueA == credential.value) {
					theVote = voteTmp.valueB;
				}
			}
			
			if (theVote == null) {
				System.out.println("[SERVER]\tNo vote for credential\t" + credential.toString());
				credential = credential.next;
				continue;
			}
			System.out.println("[SERVER]\tCounting vote for credential\t" + credential.toString());

			if (result == null) {
				result = new NodeCount(theVote, null, 1);
			} else {
				result.inc(theVote);
			}
			credential = credential.next;
		}

		// ///////////////////////// GENERATING RESULTS ////////////////////////////////////

		System.out.println("\n*******************************************************");
		System.out.println("*********************** RESULTS ***********************");
		System.out.println("*******************************************************\n");

		for (Votes v : Votes.values()) { // Get the results for all possible votes
			NodeCount tmp = result;
			int c = 0;
			while (tmp != null) {
				if (tmp.value == v.toString().getBytes()) {
					c = tmp.count;
				}
			}
			System.out.println("[RESULT]\t" + v.toString() + " has " + (c == 0 ? "no" : c + " votes"));
		}
		System.exit(0);
	}

	private void decryptVotes() {
		votes.clear();
		NodeList m = ballot;
		while (m != null) {
			try {
				byte[] mDec 		= serverDec.decrypt(m.value); // Decrypt the vote using servers private key
				byte[] credential 	= MessageTools.first(mDec); // part1 is the credential
				byte[] vote 		= MessageTools.second(mDec); // part2 the choice
				votes.put(credential, vote);
			} catch (Exception ce) {
				// An invalid vote has been casted
				System.out.println("[SERVER]\tInvalid vote has been casted. " + m.toString());
			}
			m = m.next;
		}
	}
}
