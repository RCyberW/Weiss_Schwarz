package Field;

// Deck_Zone field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

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

	private void constructPopup(MouseEvent e) {
		JPopupMenu popmenu = new JPopupMenu();

		JMenuItem drawAction = new JMenuItem("draw");
		drawAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawCard();
			}
		});
		popmenu.add(drawAction); // prompt for moving cards from hand
									// to top of deck

		JMenuItem shuffleAction = new JMenuItem("shuffle");
		shuffleAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shuffle();
			}
		});
		popmenu.add(shuffleAction); // refresh or normal shuffle

		JMenuItem checkAction = new JMenuItem("check top");
		checkAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCard();
			}
		});
		popmenu.add(checkAction); // move to waiting
									// room/top/bottom/reveal
		JMenuItem millAction = new JMenuItem("brainstorm");
		millAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resolutionCard();
			}
		});
		popmenu.add(millAction); // goes to resolution

		JMenuItem triggerAction = new JMenuItem("trigger");
		triggerAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resolutionCard();
			}
		});
		popmenu.add(triggerAction); // goes to resolution

		JMenuItem damageAction = new JMenuItem("damage check");
		damageAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resolutionCard();
			}
		});
		popmenu.add(damageAction); // goes to resolution

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}

	protected void resolutionCard() {
		System.out.println(showCard());
		Card card = showCard();
		deckZone.remove(deckZone.size() - 1);
		associatedPlayer.getField().getRandomZone().setCard(card);

		associatedPlayer.getField().repaint();

		associatedPlayer.getField().requestFocusInWindow();
		this.transferFocus();

		System.out.println(showCard());
	}

	protected void stockCard() {
		Card card = showCard();
		removeCard(card);
		associatedPlayer.getField().getRandomZone().setCard(card);
	}

	protected void discardCard() {
		Card card = showCard();
		removeCard(card);
		associatedPlayer.getField().getRandomZone().setCard(card);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (containCards() == false)
			return;
		Card card = selectCard(e);
		if (card == null)
			return;

		if (e.getButton() == MouseEvent.BUTTON3) {
			constructPopup(e);

			/*
			 * if (associatedPlayer.getCurrentPhase() == Phase.DRAW_PHASE) { //
			 * drawCard(); } else if (associatedPlayer.getCurrentPhase() ==
			 * Phase.ATTACK_PHASE) { Card stockCard =
			 * deckZone.remove(deckZone.size() - 1);
			 * associatedPlayer.getField().getRandomZone().setCard(stockCard); }
			 * else if (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE)
			 * { drawCard(); }
			 */
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
		if (containCards()) {
			showCard().setDisplay(false, false);
			showCard().toCanvas().setLocation(x, y);
			showCard().toCanvas().paint(g);
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString("Cards remain: " + deckZone.size() + "", this.x,
				this.y - 10);
	}

	@Override
	// right click access top of the deck
	// left click access bottom of the deck
	public Card selectCard(MouseEvent e) {
		if (!containCards())
			return null;
		if (showCard().getCardBound().contains(e.getPoint())) {
			Card c = showCard();
			// if (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE) {
			// if (e.getButton() == MouseEvent.BUTTON3)
			// c = deckZone.remove(deckZone.size() - 1);
			// else if (e.getButton() == MouseEvent.BUTTON1)
			// c = deckZone.remove(0);
			// }
			return c;
		}

		return null;
	}

	public boolean isList() {
		return true;
	}
}
