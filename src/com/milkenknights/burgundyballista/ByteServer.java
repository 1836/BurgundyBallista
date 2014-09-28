package com.milkenknights.burgundyballista;

import edu.wpi.first.wpilibj.Timer;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.ServerSocketConnection;

public class ByteServer {
	private ServerSocketConnection scn;
	private SocketConnection sc;
	
	private class ConnectionHandler implements Runnable {
		ServerSocketConnection scn;
		//SocketConnection connection;
		
		private int currentByte;
		public int getCurrentByte() {
			return currentByte;
		}
		
		public ConnectionHandler(ServerSocketConnection scn) {
			this.scn = scn;
			currentByte = 0;
		}
		
		public void run() {
			while (true) {
				System.out.println("waiting for a connection");
				try {
					SocketConnection connection = (SocketConnection) scn.acceptAndOpen();
					System.out.println("got a connection");
					double lastMessageTime = Timer.getFPGATimestamp();
					double timeout = 0.5;
					int timeout_count = 0;
					InputStream is = connection.openInputStream();

					while (true) {
						// TODO: organize these if statements to be less redundant
						if (currentByte == 0 || is.available() > 0) {
							int b = is.read();
							if (b == -1) {
								break;
							}
							currentByte = b;

							System.out.println("Got byte "+currentByte);
							lastMessageTime = Timer.getFPGATimestamp();
							timeout_count = 0;
						} else {
							if (Timer.getFPGATimestamp() - lastMessageTime > timeout) {
								currentByte = 0;
								System.out.println("timed out "+lastMessageTime);
								timeout_count++;
								if (timeout_count > 3) {
									break;
								}
							} else if (is.available() > 0) {
								int b = is.read();
								if (b == -1) {
									break;
								}
								currentByte = b;
								System.out.println("Got byte "+currentByte);
								timeout_count = 0;
								lastMessageTime = Timer.getFPGATimestamp();
							}
						}
					}
				} catch (IOException e) {
					currentByte = 0;
					System.out.println("Socket failure!!!!");
					e.printStackTrace();
				}
			}
		}
	}
	
	ConnectionHandler ch;

	public ByteServer(int listenPort) {
		try {
			scn = (ServerSocketConnection)
					Connector.open("serversocket://:" + listenPort);
			ch = new ConnectionHandler(scn);
			Thread t = new Thread(ch);
			t.start();
		} catch (IOException e) {
			System.out.println("Socket failure.");
			e.printStackTrace();
		}
	}
	
	public int getCurrentByte() {
		return ch.getCurrentByte();
	}
}
