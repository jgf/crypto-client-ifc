package de.uni.trier.infsec.environment;

class Node {
	int value;
	Node next;
	Node(int v, Node n) {
		value = v; next = n;
	}
}

public class Environment {
	
	private static boolean result; // the LOW variable
	
	private static Node list = null;
	private static boolean listInitialized = false;
		
	private static Node initialValue() {
		// Unknown specification of the following form:
		// return new Node(U1, new Node(U2, ...));
		// where U1, U2, ...Un are constant integers.
		return new Node(1, new Node(7,null));  // just an example
	}

    public static int untrustedInput() {
    	if (!listInitialized) {
    		list = initialValue();
    	    listInitialized = true;        
    	}
    	if (list==null) return 0;
    	int tmp = list.value;
    	list = list.next;
    	return tmp;
	}
		
    public static void untrustedOuput(int x) {
		if (untrustedInput()==0) {
			result = (x==untrustedInput());
			throw new Error();  // abort
		}
	}
}        
