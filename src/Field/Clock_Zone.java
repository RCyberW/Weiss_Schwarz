package Field;

// Clock_Zone field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import CardAssociation.Card;
import CardAssociation.State;
import Game.Game;
import Game.Phase;
import Game.Player;

public class Clock_Zone extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -841706780509169529L;

	public ArrayList<Card> clockZone;

	private Card selected;
	private int swappedIndex;

	public Clock_Zone(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Clock", player);

		clockZone = new ArrayList<Card>();
	}

	public void setCard(Card c) {
		clockZone.add(c);
		// System.out.println("Clock_Zone " + clockZone);
	}

	public ArrayList<Card> showCards() {
		return clockZone;
	}

	// remove a card from clock zone
	public Card removeCard(Card c) {
		if (clockZone.remove(c))
			return c;
		return null;
	}

	// return the number of damage in the clock zone
	public int getDamage() {
		return clockZone.size();
	}

	public Card shift(Card insert) {
		Card card = null;
		if (insert != null && selected != null) {
			System.out.println("Shifting " + insert.getCardName());
			card = removeCard(selected);
			clockZone.add(swappedIndex, insert);
			associatedPlayer.getHand().setCard(selected);
			associatedPlayer.getHand().playCard(insert);
		}
		return card;
	}

	public Card showCard() {
		if (clockZone.size() > 0)
			return clockZone.get(clockZone.size() - 1);
		return null;
	}

	@Override
	public boolean containCards() {
		return clockZone.size() > 0;
	}

	@Override
	public void paint(Graphics g, Card c) {
		for (int i = 0; i < clockZone.size(); i++) {
			Card thisCard = clockZone.get(i);

			thisCard.setDisplay(true, false);
			thisCard.toCanvas().setLocation(
					(int) (x + 110 * i * Game.gameScale), y);
			if (selected != null
					&& thisCard.getCardName().equals(selected.getCardName())) {
				swappedIndex = i;
				thisCard.toCanvas().setLocation(
						(int) (x + 110 * i * Game.gameScale), y - 10);
			}
			thisCard.toCanvas().paint(g);
		}
		if (!containCards()) {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString("Damage count: " + clockZone.size() + "", x, y - 10);
	}

	@Override
	public Card selectCard(MouseEvent e) {
		String output;

		for (int i = 0; i < clockZone.size(); i++) {
			Card card = clockZone.get(i);
			output = "CLOCK: x = " + e.getX() + ", y = " + e.getY() + " "
					+ card.getCardName() + " : " + card.getCardBound().x
					+ " + " + card.getCardBound().width + " , "
					+ card.getCardBound().y + " + "
					+ card.getCardBound().height;
			if (card.getCardBound().contains(e.getX(), e.getY())) {
				// System.out.println("click: " + i + ". " + card);
				swappedIndex = i;
				if (e.getButton() == MouseEvent.BUTTON1) {
					clockZone.remove(i);
				}
				output += " match!";
				System.out.println(output);
				return card;
			}
			System.out.println(output);
		}
		return null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Card card = selectCard(e);
		if (containCards() == false || card == null)
			return;
		if (e.getButton() == MouseEvent.BUTTON1) {
			selected = selectCard(e);
			shift(associatedPlayer.getHand().getSelected());
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE) {
				// to shift, select a card from hand first, then select a
				// card from the clock
				if (selected == null)
					selected = selectCard(e);
				shift(associatedPlayer.getHand().getSelected());
			}
		}
	}

	public boolean isList() {
		return true;
	}

	public Card getSelected() {
		return selected;
	}
}
