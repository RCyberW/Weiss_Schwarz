package Connection;

import java.util.UUID;

import Field.FieldElement;
import Game.Player;

public class Instruction {
	
	private Player associatedPlayer;
	private UUID cardUUID;
	private FieldElement sourceElement;
	private FieldElement targetElement;
	private InstructionAction action;
	
	public Instruction(Player p, UUID uid, FieldElement srcEle, FieldElement trgEle) {
		associatedPlayer = p;
		cardUUID = uid;
		sourceElement = srcEle;
		targetElement = trgEle;
	}
	
	public Player getAssociatedPlayer() {
		return associatedPlayer;
	}
	
	public UUID getCardUUID() {
		return cardUUID;
	}
	
	public FieldElement getSourceElement() {
		return sourceElement;
	}
	
	public FieldElement getTargetElement() {
		return targetElement;
	}
	
	public InstructionAction getAction() {
		return action;
	}

}
