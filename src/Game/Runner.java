package Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

// Version: 2.6.15

public class Runner {

	// public static Connector connector;

	private static boolean release = false;

	public static void main(String[] args) {
		if (release) {
			PrintStream debugLog;
			try {
				debugLog = new PrintStream(new File("Debug.llog"));
				System.setOut(debugLog);
				System.setErr(debugLog);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		Player player1 = new Player();
		player1.buildAndDisplay();
	}
}
