package Game;

import Connection.Connector;

public class Runner {

	public static Connector connector;

	public static void main(String[] args) {
		Player player1 = new Player();
		player1.buildAndDisplay();

		while (!player1.isReady()) {
			System.err.println("Waiting...");

		}
		/*
		 * Player player2 = new Player(); player2.buildAndDisplay();
		 * 
		 * // connector = new Connector("Cyber", 5000); // connector = new
		 * Connector("174.77.36.43", 9090); // connector.messenger(player1); //
		 * connector.messenger(player2);
		 * 
		 * Game game1 = new Game(player1, player2); Game game2 = new
		 * Game(player2, player1); player1.setGame(game1);
		 * player2.setGame(game2); while (!player1.isReady() ||
		 * !player2.isReady()) {
		 * 
		 * } player1.setCurrentPhase(Phase.DRAW_PHASE);
		 * player2.setCurrentPhase(Phase.STAND_PHASE);
		 */
		player1.buildGame();
		// player2.buildGame();
	}
}
