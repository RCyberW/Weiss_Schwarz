package Field;

// Level_Zone field display information

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import CardAssociation.*;
import Game.Game;
import Game.Player;

public class Level_Zone extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1457826458085816273L;

	public ArrayList<Card> levelZone;

	private Card selected;
	private int swappedIndex;

	public Level_Zone(String imageFileName, int xa, int ya, Player player,
	   int offset) {
		super(imageFileName, xa, ya, "Level", player, offset);

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

	public boolean contains(Point p) {
		for (int i = 0; i < levelZone.size(); i++) {
			Card card = levelZone.get(i);
			if (card.getCardBound().contains(p)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean containCards() {
		return levelZone.size() > 0;
	}

	@Override
	public void paint(Graphics g, Card c) {
		for (int i = 0; i < levelZone.size(); i++) {
			Card thisCard = levelZone.get(i);
			System.out.println("Level zone " + (thisCard.getCurrentState() == State.REST));
			if (offsetHeight > 0)
				thisCard.toCanvas().setLocation((int) (x * Game.gameScale),
				   (int) (y - (50 * Game.gameScale) * i));
			else
				thisCard.toCanvas().setLocation((int) (x * Game.gameScale),
				   (int) (y + (50 * Game.gameScale) * i));
			if (selected != null
			   && thisCard.getUniqueID().equals(selected.getUniqueID())) {
				// swappedIndex = i;
				// thisCard.toCanvas().setLocation(x + 10, y - 50 * i);
			}
			thisCard.toCanvas().paint(g);
		}
		if (!containCards()) {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		if (MainField.debug)
			g.drawString("Level count: " + levelZone.size() + "", x, y - 10);
	}

	@Override
	public Card selectCard(MouseEvent e) {
		String output;
		if (containCards()) {
			for (int i = 0; i < levelZone.size(); i++) {
				Card card = levelZone.get(i);
				output = "LEVEL: x = " + e.getX() + ", y = " + e.getY() + " "
				   + card.getCardName() + " : " + card.getCardBound().x + " + "
				   + card.getCardBound().width + " , " + card.getCardBound().y
				   + " + " + card.getCardBound().height;
				if (card.getCardBound().contains(e.getPoint())) {
					output += " match! " + swappedIndex;
					System.out.println(output);
					// levelZone.remove(i);
					return card;
				}
				System.out.println(output);
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
				Card temp = associatedPlayer.getField().getDefenderRandomZone()
				   .showCard();
				if (temp != null && selected != null) {
					associatedPlayer.getField().getDefenderRandomZone().removeCard();
					associatedPlayer.getField().getDefenderRandomZone()
					   .setCard(selected);

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
