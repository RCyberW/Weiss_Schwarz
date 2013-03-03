package Connection;

import java.io.*;

import java.net.*;

public class TCPClient

{

	public static void main(String[] args) throws IOException

	{

		System.out.println("TCP CLIENT");

		System.out.println("Enter the host name to connect");

		DataInputStream inp = new DataInputStream(System.in);

		@SuppressWarnings("deprecation")
		String str = inp.readLine();

		Socket clientsoc = new Socket(str, 9);

		PrintWriter out = new PrintWriter(clientsoc.getOutputStream(), true);

		BufferedReader in = new BufferedReader(new

		InputStreamReader(clientsoc.getInputStream()));

		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));

		String userinput;

		try

		{

			while (true)

			{

				System.out.println("Sever Says : " + in.readLine());

				userinput = stdin.readLine();

				out.println(userinput);

			}

		}

		catch (Exception e)

		{

			System.exit(0);

		}

	}
}