package Connection;

import java.util.UUID;

import CardAssociation.*;
import Field.*;
import Game.*;
// This class is designed for reading instructions that are 
// send between the clients for the program to be able to 
// compile and execute the instructions

public class InstructionReader {

	// Instructions will include, but are not exclusive to
	// Alice "played card uuid1 to (zone)"
	// Alice "salvaged card uuid1 to (hand/deck)"
	// Alice "drew card uuid1 (hidden)"
	// Alice "searched for card uuid1 add to (zone)"
	// Alice "stocked card uuid1 (hidden/shown)"
	// Alice "send card uuid1 to (zone)"

	// Reads a specific instruction file and execute the instruction
	public void reader(Instruction instruction) {

		switch (instruction.getAction()) {
		case PLAY:
			play(instruction.getAssociatedPlayer(), instruction.getCardUUID(), instruction.getSourceElement());
			break;
		case SALVAGE:
			salvage(instruction.getAssociatedPlayer(), instruction.getCardUUID(), instruction.getSourceElement());
			break;
		case DRAW:
			draw(instruction.getAssociatedPlayer(), instruction.getCardUUID(), instruction.getSourceElement());
			break;
		case SEARCH:
			search(instruction.getAssociatedPlayer(), instruction.getCardUUID(), instruction.getSourceElement());
			break;
		case STOCK:
			stock(instruction.getAssociatedPlayer(), instruction.getCardUUID(), instruction.getSourceElement());
			break;
		case MOVE:
			move(instruction.getAssociatedPlayer(), instruction.getCardUUID(), instruction.getSourceElement(), instruction.getTargetElement());
			break;
		default:
			break;
		}

	}

	private void play(Player p, UUID uuid, FieldElement f) {
		Card c = p.getCurrentDeck().findCard(uuid.toString());
		FieldElement ele = p.getField().findZone(f);

		ele.setCard(c);
	}

	private void salvage(Player p, UUID uuid, FieldElement f) {
		// assume I'll pass in waiting room reference...or else hell will break
		// loose

		Card c = p.getCurrentDeck().findCard(uuid.toString());
		Waiting_Room ele = (Waiting_Room) p.getField().findZone(f);

		ele.removeCard(c);
		p.getHand().setCard(c);
	}

	private void draw(Player p, UUID uuid, FieldElement f) {
		Card c = p.getCurrentDeck().findCard(uuid.toString());
		Deck_Zone ele = (Deck_Zone) p.getField().findZone(f);

		ele.removeCard(c);
		p.getHand().setCard(c);
	}

	private void search(Player p, UUID uuid, FieldElement f) {
		Card c = p.getCurrentDeck().findCard(uuid.toString());
		FieldElement ele = p.getField().findZone(f);

		ele.setCard(c);
	}

	private void stock(Player p, UUID uuid, FieldElement f) {
		Card c = p.getCurrentDeck().findCard(uuid.toString());
		Deck_Zone ele = (Deck_Zone) p.getField().findZone(f);

		ele.stockCard(c);
	}

	private void move(Player p, UUID uuid, FieldElement f1, FieldElement f2) {
		Card c = p.getCurrentDeck().findCard(uuid.toString());
		FieldElement ele1 = p.getField().findZone(f1);
		FieldElement ele2 = p.getField().findZone(f2);

		ele1.removeCard(c);
		ele2.setCard(c);
	}
}
