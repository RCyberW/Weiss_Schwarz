package Field;

// Memory_Zone field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import CardAssociation.*;
import Game.Phase;
import Game.Player;

public class Memory_Zone extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8434398384511720645L;

	public ArrayList<Card> memoryZone;

	public Memory_Zone(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Memory", player);

		memoryZone = new ArrayList<Card>();
	}

	public void setCard(Card c) {
		c.setCurrentState(State.REST);
		memoryZone.add(c);
	}

	public ArrayList<Card> showCards() {
		return memoryZone;
	}

	public Card removeCard(Card c) {
		if (memoryZone.remove(c))
			return c;
		return null;
	}

	public Card showCard() {
		if (memoryZone.size() > 0)
			return memoryZone.get(memoryZone.size() - 1);
		return null;
	}

	@Override
	public boolean containCards() {
		return memoryZone.size() > 0;
	}

	private void displayDeck() {
		DisplayList displayGui = new DisplayList(memoryZone, associatedPlayer);

		displayGui.buildSelector();
		displayGui.setVisible(true);
	}

	@Override
	public void paint(Graphics g, Card c) {
		// for (int i = 0; i < memoryZone.size(); i++) {
		// Card thisCard = memoryZone.get(i);
		// if (thisCard != c)
		// thisCard.paint(g, this.x, this.y, true, true);
		// }

		if (showCard() != c && showCard() != null) {
			// System.err.println("painting " + getLast().toString() + "....");
			// showCard().paint(g, this.x, this.y, true, false);
			showCard().setDisplay(true, true);
			showCard().toCanvas().setLocation(x, y);
			showCard().toCanvas().paint(g);
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString(zoneName, x + 10, y + 20);
		g.drawString(memoryZone.size() + "", this.x + 10, this.y + 50);
	}

	@Override
	public Card selectCard(MouseEvent e) {
		if (containCards()) {
			if (showCard().getCardBound().contains(e.getX(), e.getY())) {
				Card c = showCard();
				memoryZone.remove(memoryZone.size() - 1);
				return c;
			}
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Card card = selectCard(e);
		if (containCards() == false || card == null)
			return;
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (associatedPlayer.getCurrentPhase() == Phase.DRAW_PHASE) {
			} else if (associatedPlayer.getCurrentPhase() == Phase.ATTACK_PHASE) {
			} else {
				displayDeck();
			}
		}
	}

	public boolean isList() {
		return true;
	}
}
