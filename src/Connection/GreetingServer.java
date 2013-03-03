package Connection;
// File Name GreetingServer.java

import java.net.*;
import java.io.*;

public class GreetingServer extends Thread {
	private ServerSocket serverSocket;

	public GreetingServer(int port) throws IOException {
		
		InetAddress addr = InetAddress.getLocalHost();
		SocketAddress endpoint = new InetSocketAddress(addr, port);
		serverSocket = new ServerSocket();
		serverSocket.bind(endpoint);
		//serverSocket.setSoTimeout(10000);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client on port "
						+ serverSocket.getLocalSocketAddress().toString() + "...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(
						server.getInputStream());
				System.out.println(in.readUTF());
				DataOutputStream out = new DataOutputStream(
						server.getOutputStream());
				out.writeUTF("Thank you for connecting to "
						+ server.getLocalSocketAddress() + "\nGoodbye!");
				server.close();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		int port = 9090;
		try {
			Thread t = new GreetingServer(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}