package Field;

// Level_Zone field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import CardAssociation.*;
import Game.Player;

public class Level_Zone extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1457826458085816273L;

	public ArrayList<Card> levelZone;

	private Card selected;
	private int swappedIndex;

	public Level_Zone(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Level", player);

		levelZone = new ArrayList<Card>();
	}

	public void setCard(Card c) {
		c.setCurrentState(State.REST);
		levelZone.add(c);
	}

	public ArrayList<Card> showCards() {
		return levelZone;
	}

	public Card removeCard(Card c) {
		if (levelZone.remove(c))
			return c;
		return null;
	}

	public int currentLevel() {
		return levelZone.size();
	}

	public Card shift(Card insert) {
		Card card = selected;
		Card temp = null;

		temp = insert;
		insert = card;
		card = temp;

		// card = removeCard(selected);
		// levelZone.add(swappedIndex, insert);

		return card;
	}

	public Card showCard() {
		if (levelZone.size() > 0)
			return levelZone.get(levelZone.size() - 1);
		return null;
	}

	@Override
	public boolean containCards() {
		return levelZone.size() > 0;
	}

	@Override
	public void paint(Graphics g, Card c) {
		for (int i = 0; i < levelZone.size(); i++) {
			Card thisCard = levelZone.get(i);

			thisCard.setDisplay(true, true);
			thisCard.toCanvas().setLocation(x, y - 50 * i);
			if (selected != null && thisCard.getUniqueID().equals(selected.getUniqueID())) {
				// swappedIndex = i;
				thisCard.toCanvas().setLocation(x + 10, y - 50 * i);
			}
			thisCard.toCanvas().paint(g);
		}
		if (!containCards()) {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString("Level count: " + levelZone.size() + "", x, y - 10);
	}

	@Override
	public Card selectCard(MouseEvent e) {
		if (containCards()) {
			for (int i = levelZone.size() - 1; i >= 0; i--) {
				Card c = levelZone.get(i);
				if (c.getCardBound().contains(e.getX(), e.getY())) {
					// levelZone.remove(i);
					return c;
				}
			}
		}
		return null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Card card = selectCard(e);
		if (containCards() == false || card == null)
			return;
		selected = card;
		associatedPlayer.getField().setSelected(card);

		if (e.getButton() == MouseEvent.BUTTON1) {
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			constructPopup(e);
		}
		// TODO: swapping
	}

	public boolean isList() {
		return true;
	}

	public int getCount() {
		return levelZone.size();
	}

	@Override
	protected void constructPopup(MouseEvent e) {
		JPopupMenu popmenu = new JPopupMenu();

		JMenuItem swapAction = new JMenuItem("to resolution area");
		swapAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Card temp = associatedPlayer.getField().getRandomZone().showCard();
				if (temp != null && selected != null) {
					associatedPlayer.getField().getRandomZone().removeCard();
					associatedPlayer.getField().getRandomZone().setCard(selected);
					
					temp.setCurrentState(State.REST);
					
					levelZone.remove(swappedIndex);
					levelZone.add(swappedIndex, temp);

					associatedPlayer.getField().repaintElements();
				}
			}
		});
		popmenu.add(swapAction);

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}
}
