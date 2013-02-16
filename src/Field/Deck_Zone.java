package Field;

// Deck_Zone field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import CardAssociation.Card;
import CardAssociation.Deck;
import Game.Phase;
import Game.Player;

public class Deck_Zone extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2052149550067723825L;

	public ArrayList<Card> deckZone;
	public Deck currentDeck;

	protected Card selectedFromSearch;

	protected int selectedFromSearchIndex;

	public Deck_Zone(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Deck", player);

		deckZone = new ArrayList<Card>();
	}

	public void loadDeck(Deck deck) {
		currentDeck = deck;
		deckZone = currentDeck.getPlayingDeck();
		Collections.shuffle(deckZone);
		System.err.println("Deck size: " + deckZone.size());
	}

	public void shuffle() {
		Collections.shuffle(deckZone);
	}

	public Card drawCard() {
		Card card = deckZone.remove(deckZone.size() - 1);
		associatedPlayer.getHand().setCard(card);
		return card;
	}

	@Override
	public boolean containCards() {
		return deckZone.size() > 0;
	}

	public Card showCard() {
		if (deckZone.size() > 0)
			return deckZone.get(deckZone.size() - 1); // take a look at the top
														// card of the deck
		return null;
	}

	public void setCard(Card c) {
		deckZone.add(c); // add card to the top of the deck
	}

	public Card removeCard(Card c) {
		if (deckZone.remove(c))
			return c;
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Card selected = selectCard(e);
		if (selected == null || containCards() == false)
			return;
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (associatedPlayer.getCurrentPhase() == Phase.DRAW_PHASE) {
				// drawCard();
			} else if (associatedPlayer.getCurrentPhase() == Phase.ATTACK_PHASE) {
				Card stockCard = deckZone.remove(deckZone.size() - 1);
				associatedPlayer.getField().getRandomZone().setCard(stockCard);
			} else if (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE) {
				drawCard();
			}
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			displayDeck();
		}

	}

	private void displayDeck() {
		DisplayList displayGui = new DisplayList(deckZone, associatedPlayer);

		displayGui.buildSelector();
		displayGui.setVisible(true);
	}

	@Override
	public void paint(Graphics g, Card c) {
		// for (int i = 0; i < deckZone.size(); i++) {
		// Card card = deckZone.get(i);
		// if (card != c) {
		// System.err.println("painting " + card.toString() + "....");
		// card.paint(g, this.x, this.y, true, false);
		// }
		// }

		if (containCards()) {
			// System.err.println("painting " + getLast().toString() + "....");
			// showCard().paint(g, this.x, this.y, false, false);
			showCard().setDisplay(false, false);
			showCard().toCanvas().setLocation(x, y);
			showCard().toCanvas().paint(g);
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		// g.drawString(zoneName, x + 10, y + 20);
		g.drawString("Cards remain: " + deckZone.size() + "", this.x,
				this.y - 10);
	}

	@Override
	// right click access top of the deck
	// left click access bottom of the deck
	public Card selectCard(MouseEvent e) {
		if (containCards()) {
			if (showCard().getCardBound().contains(e.getX(), e.getY())) {
				Card c = showCard();
				if (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE) {
					if (e.getButton() == MouseEvent.BUTTON3)
						c = deckZone.remove(deckZone.size() - 1);
					else if (e.getButton() == MouseEvent.BUTTON1)
						c = deckZone.remove(0);
				}
				return c;
			}
		}
		return null;
	}

	public boolean isList() {
		return true;
	}
}
