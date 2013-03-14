package Game;

import Connection.Connector;

public class Runner {

	public static Connector connector;

	public static void main(String[] args) {
		Player player1 = new Player();
		player1.buildAndDisplay();
	}
}
