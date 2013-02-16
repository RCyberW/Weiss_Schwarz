package Connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Game.Game;
import Game.Player;

public class Receiver extends Thread {

	JButton button;
	JLabel label;
	JPanel panel;
	JTextArea textArea = new JTextArea();
	ServerSocket serverSocket = null;
	Socket server;
	OutputStream outFromServer;
	InputStream inFromServer;
	ObjectOutputStream out;
	ObjectInputStream in;
	String line;

	Game[] ongoingGames;
	Player[] onlinePlayers;
	int playerCount;
	InetAddress addr;

	public Receiver() { // Begin Constructor
		try {
			addr = InetAddress.getLocalHost();
			SocketAddress endpoint = new InetSocketAddress(addr, 5000);
			serverSocket = new ServerSocket();
			serverSocket.bind(endpoint);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("1. Starting Weiss Server port "
				+ serverSocket.getLocalSocketAddress().toString() + "...");

		onlinePlayers = new Player[100];
		ongoingGames = new Game[50];
		playerCount = 0;

	} // End Constructor

	private void addPlayer(Player player) {
		System.out.println("add player");
		for (; playerCount < onlinePlayers.length; playerCount++) {
			if (onlinePlayers[playerCount] == null) {
				onlinePlayers[playerCount] = player;
				player.setSessionID(playerCount);
				try {
					DataOutputStream dout = new DataOutputStream(server
							.getOutputStream());
					dout.writeInt(playerCount);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	private void removePlayer(Player player) {
		int removeID = player.getSessionID();
		if (removeID > -1) {
			onlinePlayers[removeID] = null;
			playerCount = removeID;
		}
	}

	public void playGame(Player p) {
		System.out.println("this is to play game");

		if (p == null)
			return;

		for (int i = 0; i < onlinePlayers.length; i++) {
			if (onlinePlayers[i] != null)
				System.out.println(onlinePlayers[i].getSessionID());
		}

		Game game = null;

		for (int i = 0; i < onlinePlayers.length; i++) {
			if (onlinePlayers[i] != null) {
				if (onlinePlayers[i].isInGame()) {
				} else {
					if (p.getSessionID() != onlinePlayers[i].getSessionID()
							&& !onlinePlayers[i].getSelectedDeck().isEmpty()) {
						p.setInGame(true);
						onlinePlayers[i].setInGame(true);
						game = new Game(p, onlinePlayers[i]);

						for (int x = 0; x < ongoingGames.length; x++) {
							if (ongoingGames[x] == null) {
								ongoingGames[x] = game;
								game.setGameID(x);
								break;
							}
						}

						p.setGame(game);
						onlinePlayers[i].setGame(game);
						break;
					}
				}
			}
		}

		try {
			ObjectOutputStream oout = new ObjectOutputStream(server
					.getOutputStream());
			if (game != null)
				oout.writeObject(game);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				server = serverSocket.accept();
				System.out.println("2. Connected to "
						+ server.getRemoteSocketAddress());
				in = new ObjectInputStream(server.getInputStream());

				Object o = null;
				try {
					System.out.println("Server test read object");
					o = in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				try {
					String str = (String) o;
					System.out.println("3. " + str);
					if (str.startsWith("match game")) {
						Player p = null;
						int sID = Integer.parseInt(str.substring(
								"match game".length()).trim());
						for (int i = 0; i < onlinePlayers.length; i++) {
							if (onlinePlayers[i].getSessionID() == sID) {
								p = onlinePlayers[i];
								break;
							}
						}
						System.out.println("hello world");
						playGame(p);
					}
				} catch (ClassCastException e0) {
					try {
						Player p = (Player) o;
						if (p.getLogedOut()) {
							addPlayer(p);
						} else {
							removePlayer(p);
						}
						p.setLogedOut();
					} catch (ClassCastException e1) {
						try {
							System.out.println("cast field");
						} catch (ClassCastException e2) {
							System.out.println("FAILED ALLL!!!!");
							e2.printStackTrace();
						}
					}
				}

				// out = new ObjectOutputStream(server.getOutputStream());
				// out.writeUTF("4. Thank you for connecting to "
				// + server.getLocalSocketAddress() + "\nGoodbye!");
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	protected void finalize() {
		// Clean up
		try {
			in.close();
			out.close();
			server.close();
		} catch (IOException e) {
			System.out.println("Could not close.");
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		Receiver frame = new Receiver();
		frame.start();
		// frame.setTitle("Server Program");
		// WindowListener l = new WindowAdapter() {
		// public void windowClosing(WindowEvent e) {
		// System.exit(0);
		// }
		// };
		// frame.addWindowListener(l);
		// frame.pack();
		// frame.setVisible(true);
		// frame.listenSocket();
	}

}
