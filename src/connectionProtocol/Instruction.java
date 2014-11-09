package connectionProtocol;

import gamePlay.Keyword;
import gameField.Zone;

import java.util.HashMap;

import gamePlay.Player;

import deckComponents.Card;

/**
 * @author Frank Chen
 * @version 0.1
 * @since 2014-01-01
 */
public class Instruction extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2509094028695992976L;
	private Keyword action;
	private Zone sourceZone;
	private Zone targetZone;
	private Card card;
	private HashMap<String, String> instructionProperties; // other properties
															// of the
															// instruction (i.e.
															// number of cards)

	public Instruction(Player sourcePlayer, Player targetPlayer) {
		super(sourcePlayer, targetPlayer);
		// TODO Auto-generated constructor stub
		setInstructionProperties(new HashMap<String, String>());
	}

	public void insertInstruction(Card card, Keyword action, Zone srcZone,
			Zone trgZone) {
		setCard(card);
		setAction(action);
		setSourceZone(srcZone);
		setTargetZone(trgZone);
		super.setChatMessage(card.getCardName() + " " + action.toString()
				+ " FROM " + srcZone.toString() + " TO " + trgZone.toString());
	}

	/**
	 * @return the keyword
	 */
	public Keyword getAction() {
		return action;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setAction(Keyword keyword) {
		this.action = keyword;
	}

	/**
	 * @return the card
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * @param card
	 *            the card to set
	 */
	public void setCard(Card card) {
		this.card = card;
	}

	/**
	 * @return the instructionProperties
	 */
	public HashMap<String, String> getInstructionProperties() {
		return instructionProperties;
	}

	/**
	 * @param instructionProperties
	 *            the instructionProperties to set
	 */
	public void setInstructionProperties(
			HashMap<String, String> instructionProperties) {
		this.instructionProperties = instructionProperties;
	}

	/**
	 * @param key
	 *            the type of the property
	 * @param detail
	 *            the specific value of the property
	 */
	public String addProperty(String key, String detail) {
		return instructionProperties.put(key, detail);
	}

	public String getType() {
		return "Instruction";
	}

	/**
	 * @return the sourceZone
	 */
	public Zone getSourceZone() {
		return sourceZone;
	}

	/**
	 * @param sourceZone
	 *            the sourceZone to set
	 */
	public void setSourceZone(Zone sourceZone) {
		this.sourceZone = sourceZone;
	}

	/**
	 * @return the targetZone
	 */
	public Zone getTargetZone() {
		return targetZone;
	}

	/**
	 * @param targetZone
	 *            the targetZone to set
	 */
	public void setTargetZone(Zone targetZone) {
		this.targetZone = targetZone;
	}

}
