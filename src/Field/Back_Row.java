package Field;

// Back_Row field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import CardAssociation.*;
import Game.Player;

public class Back_Row extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6095129785625327860L;

	// public ArrayList<Card> backRow;
	Card backCard;

	public Back_Row(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Back-Row", player);

		// backRow = new ArrayList<Card>();

		backCard = null;
	}

	public void setCard(Card c) {
		removeCard();
		backCard = c;
		repaint();
	}

	// public ArrayList<Card> showCards() {
	// return backRow;
	// }

	public Card showCard() {
		return backCard;
	}

	public Card removeCard() {
		Card c = backCard;
		backCard = null;
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
		// return backRow.size() > 0;
		return backCard != null;
	}

	@Override
	public void paint(Graphics g, Card c) {
		if (showCard() != c && showCard() != null) {
			// backCard.toCanvas().paintCard(g, this.x, this.y, true, false);
			backCard.setDisplay(true, false);
			backCard.toCanvas().setLocation(x, y);
			backCard.toCanvas().paint(g);
			System.out.println("BACK_ROW: " + backCard.getCardName());
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString(zoneName, x + 10, y + 20);
	}

	// @Override
	// public Card selectCard(MouseEvent e) {
	// if (containCards()
	// && backCard.getCardBound().contains(e.getX(), e.getY())) {
	// return showCard();
	// }
	// return null;
	// }

	@Override
	public void mouseReleased(MouseEvent e) {
		if (containCards() == false)
			return;
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (backCard.getCurrentState() == State.STAND) {
				backCard.setCurrentState(State.REST);
			} else if (backCard.getCurrentState() == State.REST) {
				backCard.setCurrentState(State.REVERSE);
			} else {
				backCard.setCurrentState(State.STAND);
			}
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			removeCard();
		}
	}
}
