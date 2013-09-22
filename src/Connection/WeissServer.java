package Connection;

// File Name GreetingServer.java

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class WeissServer extends Thread {
	private ServerSocket serverSocket;
	private ArrayList<SocketAddress> playerList;

	public WeissServer(int port) throws IOException {

		InetAddress addr = InetAddress.getLocalHost();
		SocketAddress endpoint = new InetSocketAddress(addr, port);
		serverSocket = new ServerSocket();
		serverSocket.bind(endpoint);

		playerList = new ArrayList<SocketAddress>();
		// serverSocket.setSoTimeout(10000);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client on port "
						+ serverSocket.getLocalSocketAddress().toString()
						+ "...");
				// establishing connection
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());
				playerList.add(server.getRemoteSocketAddress());

				// receiving request
				DataInputStream in = new DataInputStream(
						server.getInputStream());
				System.out.println(in.readUTF());

				String reply = "";
				
				reply = randomPlayer(server.getRemoteSocketAddress());
				
				// sending reply
				DataOutputStream out = new DataOutputStream(
						server.getOutputStream());
				out.writeUTF("Thank you for connecting to "
						+ server.getLocalSocketAddress() + "\n" + reply);

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
	
	public String randomPlayer(SocketAddress player) {
		String reply = "";
		SocketAddress opponent = null;
		
		if (playerList.size() > 1) {
			// match players at random
			// remove the matched players from the list
			// create a room for the 2 players chosen
			while (opponent == null && player == opponent)
				opponent = playerList.get((int)(Math.random() * playerList.size()));
			
			reply = opponent.toString();
			privateMatch(player, opponent);
		} else {
			// wait for more players
			reply = "Waiting for more players...";
		}
		
		return reply;
	}
	
	public void privateMatch(SocketAddress player1, SocketAddress player2) {
		
	}

	public static void main(String[] args) {
		int port = 9090;
		try {
			Thread t = new WeissServer(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}