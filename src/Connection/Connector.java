package Connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import Field.NewMainField;
import Game.Game;
import Game.Player;

public class Connector implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7235881471609989005L;
	Socket client;
	OutputStream outToServer;
	InputStream inFromServer;
	ObjectOutputStream out;
	ObjectInputStream in;
	Game createdGame;

	// Connector Constructor
	public Connector(String serverName, int port) {
		try {
			System.out.println("Connecting to " + serverName + " on port "
					+ port);
			client = new Socket(serverName, port);
			System.out.println(client.getPort());
			System.out.println("Just connected to "
					+ client.getRemoteSocketAddress());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// closing connection
	public void close() {
		try {
			in.close();
			out.close();
			client.close();
		} catch (IOException e) {
			System.out.println("Could not close.");
			System.exit(-1);
		}
	}

	// connection check
	public boolean isConnected() {
		return client.isConnected();
	}

	// send message to server
	public void messenger(NewMainField field) {
		try {
			outToServer = client.getOutputStream();
			out = new ObjectOutputStream(outToServer);
			out.writeObject(field);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void messenger(Player player) {
		try {
			outToServer = client.getOutputStream();
			out = new ObjectOutputStream(outToServer);
			out.writeObject(player);

			DataInputStream din = new DataInputStream(client.getInputStream());
			System.out.println("sessionID should be...");
			int test = din.readInt();
			System.out.println("sessionID should be " + test);
			if (test > -1)
				player.setSessionID(test);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void messenger(String text, Player player) {
		try {
			outToServer = client.getOutputStream();
			out = new ObjectOutputStream(outToServer);
			out.writeObject(text);
			System.out.println("I say: " + text);

			ObjectInputStream oin = new ObjectInputStream(client
					.getInputStream());
			System.out.println("read should be...");
			createdGame = (Game) oin.readObject();
			if (createdGame != null)
				player.setGame(createdGame);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// get messages from server
	public NewMainField mailField() {
		NewMainField field = null;

		try {
			inFromServer = client.getInputStream();
			in = new ObjectInputStream(inFromServer);
			field = (NewMainField) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return field;
	}

	public String mailMessage() {
		String message = null;

		try {
			inFromServer = client.getInputStream();
			in = new ObjectInputStream(inFromServer);
			message = (String) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return message;
	}

	public static void main(String[] args) {
		new Connector("Cyber", 5000);
	}

}
