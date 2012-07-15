package de.uni.trier.infsec.lib.test;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import org.junit.Test;

import de.uni.trier.infsec.lib.network.Network;

public class TestNetwork extends TestCase {

	public static byte[] TEST_DATA = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
	byte[] received = null;
	Semaphore sem = new Semaphore(1);
	
	Runnable r1 = new Runnable() {
		@Override
		public void run() {
			try {
				Network.waitForClient(Network.DEFAULT_PORT);
				Network.networkOut(TEST_DATA);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	Runnable r2 = new Runnable() {
		@Override
		public void run() {
			try {
				while (!Network.connectToServer(Network.DEFAULT_SERVER, Network.DEFAULT_PORT + 1));
				received = Network.networkIn();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sem.release();
			}
		};
	};
	
	@Test
	public void testNetworking() throws Exception {
		new Thread(r1).start();
		Thread.sleep(100);
		Socket s = new Socket(Network.DEFAULT_SERVER, Network.DEFAULT_PORT);
		int len = s.getInputStream().read();
		byte[] res1 = new byte[len];
		s.getInputStream().read(res1);
		
		assertTrue(Arrays.equals(TEST_DATA, res1));
		
		sem.acquire();
		new Thread(r2).start();
		ServerSocket ss = new ServerSocket(Network.DEFAULT_PORT + 1);
		s = ss.accept();
		s.getOutputStream().write(TEST_DATA.length);
		s.getOutputStream().write(TEST_DATA);
		sem.acquire();
		assertTrue(Arrays.equals(TEST_DATA, received));
	}
	
}
