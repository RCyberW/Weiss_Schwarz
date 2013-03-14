package Field;

// Waiting_Room field display information

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
import CardAssociation.State;
import Game.Phase;
import Game.Player;

public class Waiting_Room extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8827834868626500770L;

	public ArrayList<Card> waitingRoom;

	public Waiting_Room(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Waiting-Room", player);

		waitingRoom = new ArrayList<Card>();
	}

	public void setCard(Card c) {
		c.setCurrentState(State.STAND);
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

	// @Override
	// public Card selectCard(MouseEvent e) {
	// if (containCards()) {
	// if (showCard().getCardBound().contains(e.getX(), e.getY())) {
	// Card c = showCard();
	// if (e.getButton() == MouseEvent.BUTTON1)
	// waitingRoom.remove(waitingRoom.size() - 1);
	// return c;
	// }
	// }
	// return null;
	// }

	private void constructPopup(MouseEvent e) {
		final JPopupMenu popmenu = new JPopupMenu();

		JMenuItem searchAction = new JMenuItem("search");
		searchAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayDeck();
			}
		});
		popmenu.add(searchAction); // search the waiting room

		JMenuItem refreshAction = new JMenuItem("refresh");
		refreshAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDeck();
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(refreshAction); // search the waiting room

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}

	protected void refreshDeck() {
		for (Card card : waitingRoom) {
			associatedPlayer.getField().getDeckZone().setCard(card);
		}
		associatedPlayer.getField().getDeckZone().shuffle();

		waitingRoom.clear();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Card card = selectCard(e);
		if (containCards() == false || card == null)
			return;
		
		associatedPlayer.getField().setSelected(showCard());
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (associatedPlayer.getCurrentPhase() == Phase.DRAW_PHASE) {
			} else if (associatedPlayer.getCurrentPhase() == Phase.ATTACK_PHASE) {
			} else {
				constructPopup(e);
			}
		}
	}

	public boolean isList() {
		return true;
	}

	public int getCount() {
		return waitingRoom.size();
	}
}
