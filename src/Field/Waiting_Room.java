package Field;

// Waiting_Room field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import CardAssociation.Card;
import Game.Phase;
import Game.Player;

public class Waiting_Room extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8827834868626500770L;

	public ArrayList<Card> waitingRoom;
	private boolean isPaint = false;

	public Waiting_Room(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Waiting-Room", player);

		waitingRoom = new ArrayList<Card>();
	}

	public void setCard(Card c) {
		waitingRoom.add(c);
	}

	public ArrayList<Card> showCards() {
		return waitingRoom;
	}

	public Card removeCard(Card c) {
		if (waitingRoom.remove(c))
			return c;
		return null;
	}

	public void shuffle() {
		Collections.shuffle(waitingRoom);
	}

	public Card showCard() {
		if (waitingRoom.size() > 0)
			return waitingRoom.get(waitingRoom.size() - 1);
		return null;
	}

	@Override
	public boolean containCards() {
		return waitingRoom.size() > 0;
	}

	private void displayDeck() {
		DisplayList displayGui = new DisplayList(waitingRoom, associatedPlayer);

		displayGui.buildSelector();
		displayGui.setVisible(true);
	}

	@Override
	public void paint(Graphics g, Card c) {

		if (isPaint) {
			System.out.println("paint first");
			isPaint = false;
		}

		if (containCards()) {
			// System.err.println("painting " + getLast().toString() + "....");
			showCard().setDisplay(true, false);
			showCard().toCanvas().setLocation(x, y);
			showCard().toCanvas().paint(g);
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		// g.drawString(zoneName, x + 10, y + 20);
		g.drawString("Waiting room: " + waitingRoom.size() + "", this.x,
				this.y - 10);
	}

	@Override
	public Card selectCard(MouseEvent e) {
		if (containCards()) {
			if (showCard().getCardBound().contains(e.getX(), e.getY())) {
				Card c = showCard();
				if (e.getButton() == MouseEvent.BUTTON1)
					waitingRoom.remove(waitingRoom.size() - 1);
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
