package connectionProtocol;

import gamePlay.Player;

/**
 * @author Frank Chen
 * @version 0.1
 * @since 2014-01-01
 */
public class InstructionInterpreter {

	private Player player;

	public void processInstruction(Instruction instruction) {
		// process the instruction on how the card is to be placed
		switch (instruction.getAction()) {
		case DECK_DRAW:
			draw();
			break;
		case DRAW_PHASE:
			drawPhase(instruction);
			break;
		case MAIN_PHASE:
			mainPhase(instruction);
			break;
		case ATTACK_PHASE:
			attackPhase(instruction);
			break;
		case END_PHASE:
			endPhase(instruction);
			break;
		default:
			System.out.println(instruction.getSourcePlayer() + " "
					+ instruction.getAction() + " from "
					+ instruction.getSourceZone() + " to "
					+ instruction.getTargetZone());
			break;
		}
	}

	public void drawPhase(Instruction instruction) {
		instruction.getSourcePlayer().getField().getDeckZone().drawCard();
	}

	public void mainPhase(Instruction instruction) {
		instruction.getSourcePlayer();
	}

	public void attackPhase(Instruction instruction) {
		instruction.getSourcePlayer();
		instruction.getTargetPlayer();

	}

	public void endPhase(Instruction instruction) {
		instruction.getSourcePlayer();
	}

	public void draw() {

	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

}
