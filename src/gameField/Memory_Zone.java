package gameField;

// Memory_Zone field display information

import gamePlay.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import deckComponents.*;


public class Memory_Zone extends Zone {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8434398384511720645L;

	public ArrayList<Card> memoryZone;

	public Memory_Zone(String imageFileName, int xa, int ya, Player player, FieldZone zone) {
		super(imageFileName, xa, ya, zone, player);

		memoryZone = new ArrayList<Card>();
	}

	public void setCard(Card c) {
		c.setCurrentState(State.REST);
		memoryZone.add(c);
	}

	public ArrayList<Card> showCards() {
		return memoryZone;
	}

	public Card removeCard() {
		if (!containCards())
			return null;
		Card c = memoryZone.remove(memoryZone.size() - 1);
		associatedPlayer.getField().repaintElements();
		return c;
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
		displayGui.toFront();
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

		g.drawString(zoneName + ": " + memoryZone.size(), x + 10, y + 20);
	}

	// @Override
	// public Card selectCard(MouseEvent e) {
	// if (containCards()) {
	// if (showCard().getCardBound().contains(e.getX(), e.getY())) {
	// Card c = showCard();
	// // memoryZone.remove(memoryZone.size() - 1);
	// return c;
	// }
	// }
	// return null;
	// }

	@Override
	public void mouseReleased(MouseEvent e) {
		Card card = selectCard(e);
		if (containCards() == false || card == null)
			return;

		associatedPlayer.getField().setSelected(card);

		if (e.getButton() == MouseEvent.BUTTON3) {
			constructPopup(e);
		}
	}

	public boolean isList() {
		return true;
	}

	public int getCount() {
		return memoryZone.size();
	}

	protected void constructPopup(MouseEvent e) {
		JPopupMenu popmenu = new JPopupMenu();

		JMenuItem searchAction = new JMenuItem("search");
		searchAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayDeck();
			}
		});
		popmenu.add(searchAction);

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}
}
