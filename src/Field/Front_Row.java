package Field;

// Front_Row field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import CardAssociation.*;
import Game.Phase;
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

	@Override
	public Card selectCard(MouseEvent e) {
		if (containCards()
				&& frontCard.getCardBound().contains(e.getX(), e.getY())) {
			return removeCard();
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Card card = selectCard(e);
		if (containCards() == false || card == null)
			return;
		if (e.getButton() == MouseEvent.BUTTON3) {
			System.out.println("playing " + card.getCardName()
					+ " in a different position");
			setCard(card);
			if (card != null) {
				if (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE) {
					// during main phase, activate card effects
					if (card.getCurrentState() == State.STAND) {
						card.setCurrentState(State.REST);
					} else {
						card.setCurrentState(State.STAND);
					}
				} else if (associatedPlayer.getCurrentPhase() == Phase.ATTACK_PHASE) {
					// during attack phase, rest/reverse/stand cards
					System.out.println("FRONT_ROW: attack phase "
							+ card.getCardName());
					if (card.getCurrentState() == State.REST) {
						card.setCurrentState(State.REVERSE);
					} else if (card.getCurrentState() == State.REVERSE) {
						card.setCurrentState(State.STAND);
					} else {
						card.setCurrentState(State.REST);
					}
				}
			}
		}
	}
}
