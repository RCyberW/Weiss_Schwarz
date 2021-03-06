package gameField;

// Climax_Zone field display information

import gamePlay.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import deckComponents.Card;
import deckComponents.State;
import deckComponents.Type;


public class Climax_Zone extends Zone {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2043522555435456768L;

	public Card climaxCard;

	public Climax_Zone(String imageFileName, int xa, int ya, Player player, FieldZone zone) {
		super(imageFileName, xa, ya, zone, player);
	}

	public void setCard(Card c) {
		climaxCard = c;
		climaxCard.setCurrentState(State.REST);
	}

	public Card showCard() {
		return climaxCard;
	}

	public Card removeCard() {
		Card c = climaxCard;
		climaxCard = null;
		return c;
	}

	public boolean snapToZone(Card c) {

		if (c.getT() == Type.CLIMAX && c.getCardBound().intersects(rect) && !containCards()) {
			c.getCardBound().setLocation(x, y);
			return true;
		}

		return false;
	}

	@Override
	public boolean containCards() {
		return climaxCard != null;
	}

	@Override
	public void paint(Graphics g, Card c) {
		if (showCard() != c && showCard() != null) {
			// climaxCard.paint(g, this.x, this.y, true, true);
			climaxCard.setDisplay(true, true);
			climaxCard.toCanvas().setLocation(x, y);
			climaxCard.toCanvas().paint(g);
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString(zoneName.toString(), x + 10, y + 20);
	}

	// @Override
	// public Card selectCard(MouseEvent e) {
	// if (climaxCard != null
	// && climaxCard.getCardBound().contains(e.getX(), e.getY())) {
	// return removeCard();
	// }
	// return null;
	// }

	@Override
	public void mouseReleased(MouseEvent e) {
		if (containCards() == false)
			return;

		if (climaxCard.getCardBound().contains(e.getPoint()))
			associatedPlayer.getField().setSelected(climaxCard);

		if (e.getButton() == MouseEvent.BUTTON3) {
			associatedPlayer.getField().getWaitingRoom().setCard(climaxCard);
			removeCard();
		}
	}

	@Override
	protected void constructPopup(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
