package Field;

// Front_Row field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import CardAssociation.*;
import Game.Player;

public class Front_Row extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 832769288375275859L;

	// public ArrayList<Card> frontRow;
	Card frontCard;

	public Front_Row(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Front-Row", player);

		// frontRow = new ArrayList<Card>();

		frontCard = null;
	}

	public void setCard(Card c) {
		// frontRow.add(c);
		c.setCurrentState(State.STAND);
		removeCard();
		frontCard = c;
		repaint();
	}

	// public ArrayList<Card> showCards() {
	// return frontRow;
	// }

	public Card showCard() {
		return frontCard;
	}

	public Card removeCard() {
		// if (frontRow.remove(c))
		// return c;
		Card c = frontCard;
		frontCard = null;
		return c;
	}

	public boolean snapToZone(Card c) {

		if (c.getT() == Type.CHARACTER && c.getCardBound().intersects(rect)) {
			c.getCardBound().setLocation(x, y);
			return true;
		}

		return false;
	}

	@Override
	public boolean containCards() {
		// return frontRow.size() > 0;
		// System.err.println("Front_Row " + frontCard != null);
		return frontCard != null;
	}

	@Override
	public void paint(Graphics g, Card c) {
		if (showCard() != null) {
			// frontCard.paint(g, this.x, this.y, true, false);
			frontCard.setDisplay(true, false);
			frontCard.toCanvas().setLocation(x, y);
			frontCard.toCanvas().paint(g);
			System.out.println("FRONT_ROW: " + frontCard.getCardName());
		} else {
			g.drawImage(bi, x, y, null);
		}

		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString(zoneName, x + 10, y + 20);
	}

	// TODO: HIGH PRIORITY FIX THE REMOVING OF CARD WHEN SHOW

	// @Override
	// public Card selectCard(MouseEvent e) {
	// if (containCards() && frontCard.getCardBound().contains(e.getPoint())) {
	// return showCard();
	// }
	// return null;
	// }

	@Override
	public void mouseReleased(MouseEvent e) {
		if (containCards() == false)
			return;

		associatedPlayer.getField().setSelected(frontCard);

		if (e.getButton() == MouseEvent.BUTTON3) {
			constructPopup(e);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			if (frontCard.getCardBound().contains(e.getPoint()))
				removeCard();
		}
	}

	protected void constructPopup(MouseEvent e) {
		JPopupMenu popmenu = new JPopupMenu();

		JMenuItem standAction = new JMenuItem("stand");
		standAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard.setCurrentState(State.STAND);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(standAction);

		JMenuItem restAction = new JMenuItem("rest");
		restAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard.setCurrentState(State.REST);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(restAction);

		JMenuItem reverseAction = new JMenuItem("reverse");
		reverseAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frontCard.setCurrentState(State.REVERSE);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(reverseAction);

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}
}
