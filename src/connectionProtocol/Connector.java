package connectionProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


/**
 * @author Frank Chen
 * @version 0.1
 * @since 2014-01-01
 */
public class Connector extends Thread {
	private ServerSocket serverSocket;
	private Message messageToClient;
	private InstructionInterpreter interpreter;

	public Connector(int port) {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			SocketAddress endpoint = new InetSocketAddress(addr, port);
			serverSocket = new ServerSocket();
			serverSocket.bind(endpoint);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		interpreter = new InstructionInterpreter();
	}

	public void writeMessage(Message message) {
		messageToClient = message;

	}

	public void run() {
		try {
			System.out.println("Waiting for client on port "
					+ serverSocket.getLocalSocketAddress().toString() + "...");

			boolean stillConnected = true;
			while (stillConnected) {
				// establishing connection
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());

				// receiving request
				ObjectInputStream in = new ObjectInputStream(
						server.getInputStream());
				Message messageFromClient = (Message) in.readObject();
				if (messageFromClient == null) {
					System.out.println("RECIEVED NULL MESSAGE FROM CLIENT");
				} else {
					System.out.println(messageFromClient.toString());

					// read the message
					if (messageFromClient.getType().equals("Message")) {

					} else {
						interpreter
								.processInstruction((Instruction) messageFromClient);
					}
				}

				// sending reply
				ObjectOutputStream out = new ObjectOutputStream(
						server.getOutputStream());
				out.writeObject(messageToClient);
				server.close();
			}

		} catch (SocketTimeoutException s) {
			System.out.println("Socket timed out!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = 7777;
		Thread t = new Connector(port);
		t.start();
	}
}
