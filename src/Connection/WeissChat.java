package Connection;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class WeissChat implements Runnable {

	private int clientPort;
	private ServerSocket serverSocket;
	private String serverName;

	public WeissChat(int port) throws IOException {
		// serverSocket.setSoTimeout(10000);

//		InetAddress clientAddr = InetAddress.getLocalHost();
//		SocketAddress clientEndpoint = new InetSocketAddress(clientAddr, port);
//		clientPort = port;

		InetAddress serverAddr = InetAddress.getLocalHost();
		SocketAddress serverEndpoint = new InetSocketAddress(serverAddr, port);
		serverSocket = new ServerSocket();
		serverSocket.bind(serverEndpoint);

		serverName = "Cyber";
	}

	public void run() {

		serverStart();
	}

	public void serverStart() {
		while (true) {
			try {
				System.out.println("Waiting for client on port "
						+ serverSocket.getLocalSocketAddress().toString()
						+ "...");
				// server establishing connection
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());
				//==============================================
				System.out.println("Connecting to " + serverName + " on port "
						+ clientPort);

				// client establishing connection
				Socket client = new Socket(serverName, clientPort);
				System.out.println(client.getPort());
				System.out.println("Just connected to "
						+ client.getRemoteSocketAddress());
				//==============================================
				
				// client sending request
				OutputStream outToServer = client.getOutputStream();
				DataOutputStream outClient = new DataOutputStream(outToServer);
				outClient.writeUTF("Hello from " + client.getLocalSocketAddress());
				//==============================================
				
				// server receiving request
				DataInputStream serverIn = new DataInputStream(
						server.getInputStream());
				System.out.println(serverIn.readUTF());

				String reply = "";
				//=============================================
				
				// server sending reply
				DataOutputStream outServer = new DataOutputStream(
						server.getOutputStream());
				outServer.writeUTF("Thank you for connecting to "
						+ server.getLocalSocketAddress() + "\n" + reply);
				//==============================================
				
				// receiving reply
				InputStream inFromServer = client.getInputStream();
				DataInputStream inClient = new DataInputStream(inFromServer);
				System.out.println("Server says " + inClient.readUTF());
				//==============================================
				
				// end connection
				client.close();
				
				// end connection
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
}

class MainThreadClass {
	public static void main(String args[]) {
		WeissChat myRunnable;
		try {
			myRunnable = new WeissChat(10);
			Thread t = new Thread(myRunnable);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
