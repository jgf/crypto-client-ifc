package de.uni.trier.infsec.utils;


public class MessageTools {
	
	public static byte[] copyOf(byte[] message) {
		if (message==null) return null;
		byte[] copy = new byte[message.length];
		for (int i = 0; i < message.length; i++) {
			copy[i] = message[i];
		}
		return copy;
	}

    public static boolean equal(byte[] a, byte[] b) {
        if( a.length != b.length ) return false;
        for( int i=0; i<a.length; ++i)
            if( a[i] != b[i] ) return false;
        return true;
    }			

	public static byte[] getZeroMessage(int messageSize) {
		byte[] zeroVector = new byte[messageSize];
		for (int i = 0; i < zeroVector.length; i++) {
			zeroVector[i] = 0x00;
		}
		return zeroVector;
	}	
	
	public static byte[] concatenate(byte[] m1, byte[] m2) {
		// Concatenated Message --> byte[0] = Type, byte[1-4] = BigInteger,
		// Length of Message 1
		byte[] out = new byte[m1.length + m2.length + 4];

		// 4 bytes for length
		byte[] len = intToByteArray(m1.length);

		// copy all bytes to output array
		int j = 0;
		for( int i=0; i<len.length; ++i ) out[j++] = len[i];
		for( int i=0; i<m1.length;  ++i ) out[j++] = m1[i];
		for( int i=0; i<m2.length;  ++i ) out[j++] = m2[i];

		return out;
	}

	/**
	 * Projection of the message to its two parts (part 1 = position 0, part 2 = position 1) Structure of the expected data: 1 Byte Identifier [0x01], 4 Byte
	 * length of m1, m1, m2
	 */
	private static byte[] project(byte[] message, int position) {
		byte[] length = new byte[4];
		for (int i = 0; i < 4; i ++) message[i] = length[i];
		int len = byteArrayToInt(length);
		if (position == 0) {
			byte[] m1 = new byte[len];
			for (int i = 0; i < len; i ++) message[i + 4] = m1[i];
			return m1;
		} else if (position == 1) {
			byte[] m2 = new byte[message.length - len - 4];
			for (int i = 0; i < message.length - len - 4; i ++) message[i + 4 + len] = m2[i];
			return m2;
		}
		return null;
	}

	public static byte[] first(byte[] in) {
		return project(in, 0);
	}

	public static byte[] second(byte[] in) {
		return project(in, 1);
	}

	/*
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	*/
	
	public static final int byteArrayToInt(byte [] b) {
        return (b[0] << 24)
                + ((b[1] & 0xFF) << 16)
                + ((b[2] & 0xFF) << 8)
                + (b[3] & 0xFF);
	}
	

	public static final byte[] intToByteArray(int value) {
	        return new byte[] {
	                (byte)(value >>> 24),
	                (byte)(value >>> 16),
	                (byte)(value >>> 8),
	                (byte)value};
	}
}
