package Connection;

// File Name GreetingClient.java

import java.net.*;
import java.io.*;

public class WeissClient extends Thread{
	String serverName;
	int port;
	
	public WeissClient(int port) throws IOException {
		// serverSocket.setSoTimeout(10000);
		
		InetAddress addr = InetAddress.getLocalHost();
		SocketAddress endpoint = new InetSocketAddress(addr, port);
		this.port = port;
	}
	
	public void run() {
		serverName = "Cyber";
		// String serverName = "174.77.36.43";
		try {
			System.out.println("Connecting to " + serverName + " on port "
					+ port);
			
			// establishing connection
			Socket client = new Socket(serverName, port);
			System.out.println(client.getPort());
			System.out.println("Just connected to "
					+ client.getRemoteSocketAddress());
			
			// sending request
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			
			// receiving reply
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Server says " + in.readUTF());
			
			// end connection
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		int port = 9090;
		try {
			Thread t = new WeissClient(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}