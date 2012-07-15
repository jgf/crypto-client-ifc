package de.uni.trier.infsec.lib.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.uni.trier.infsec.environment.network.NetworkError;

// [tt] Should have the same interface as environment.network.Network, but provide a "real" networking.
// (perhaps interface needs to be extended)
// TODO [AK] This very simple implementation is stateless, so it can be used by a single call... 
// Do we prefer some kind of state? (Add connect-String as parameter or make it stateful and add a connect-Method?)
public class Network {
	
	public static final int DEFAULT_PORT = 4242;
	public static final String DEFAULT_SERVER = "127.0.0.1";
	
	private static Socket socket = null;
	
	public static boolean connectToServer(String server, int port) {
		try {
			socket = new Socket(server, port);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
		
	public static boolean waitForClient(int port) {
		try {
			ServerSocket ss = new ServerSocket(port);
			socket = ss.accept();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static void disconnect() {
		try {
			if (socket != null) socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// NetworkOut is actually stateless - so connect, send a message and disconnect.
	public static void networkOut(byte[] outEnc) throws NetworkError {
		try {
			if (socket == null) 
				return;
			socket.getOutputStream().write(outEnc.length);
			socket.getOutputStream().write(outEnc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	// Actually networkIn calls blocking read on Socket. 
	// If we want to run it on one machine, we have to care for threading...
	public static byte[] networkIn() throws NetworkError {
		if (socket == null) 
			return null;
		
		byte[] buffer = null;
		try {
			int length = socket.getInputStream().read();
			buffer = new byte[length];
			socket.getInputStream().read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
}
