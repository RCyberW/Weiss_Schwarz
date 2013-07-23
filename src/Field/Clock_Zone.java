package Field;

// Clock_Zone field display information

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import CardAssociation.Card;
import CardAssociation.State;
import Game.Game;
import Game.Player;

public class Clock_Zone extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -841706780509169529L;

	public ArrayList<Card> clockZone;

	private Card selected;
	private int swappedIndex;
	private boolean shiftMode;

	public Clock_Zone(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Clock", player);

		clockZone = new ArrayList<Card>();
	}

	public void setCard(Card c) {
		c.setCurrentState(State.STAND);
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

	public void shift(Card insert) {
		if (insert != null && selected != null) {
			associatedPlayer.getHand().setCard(selected);

			clockZone.remove(swappedIndex);
			clockZone.add(swappedIndex, insert);

			associatedPlayer.getField().repaintElements();
		}
	}

	protected void constructPopup(MouseEvent e) {
		JPopupMenu popmenu = new JPopupMenu();

		if (isShiftMode()) {
			JMenuItem shiftAction = new JMenuItem("cancel shift");
			shiftAction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setShiftMode(false);
					associatedPlayer.getField().repaintElements();
				}
			});
			popmenu.add(shiftAction);
		} else {
			JMenuItem waitingAction = new JMenuItem("to waiting room");
			waitingAction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					toWaitingRoom();
					associatedPlayer.getField().repaintElements();
				}
			});
			popmenu.add(waitingAction);

			JMenuItem handAction = new JMenuItem("to hand");
			handAction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					toHand();
					associatedPlayer.getField().repaintElements();
				}
			});
			popmenu.add(handAction);

			System.out.println("CLOCK_ZONE selectedIndex = "
				+ clockZone.indexOf(selected));

			if (clockZone.size() >= 7 && clockZone.indexOf(selected) < 7) {
				JMenuItem levelAction = new JMenuItem("to level");
				levelAction.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						toLevel();
						associatedPlayer.getField().repaintElements();
					}
				});
				popmenu.add(levelAction);
			}

			JMenuItem shiftAction = new JMenuItem("shift");
			shiftAction.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setShiftMode(true);
					associatedPlayer.getField().repaintElements();
				}
			});
			popmenu.add(shiftAction);
		}

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void toLevel() {
		if (clockZone.size() >= 7) {
			associatedPlayer.getField().getLevelZone().setCard(selected);
			clockZone.remove(swappedIndex);
			for (int i = 0; i < 6; i++) {
				Card card = clockZone.get(0);
				clockZone.remove(0);
				associatedPlayer.getField().getWaitingRoom().setCard(card);
			}
		}
	}

	private void toHand() {
		if (selected != null) {
			associatedPlayer.getHand().setCard(selected);
			clockZone.remove(swappedIndex);
			selected = null;
		}
	}

	private void toWaitingRoom() {
		// Card card = clockZone.get(swappedIndex);
		if (selected != null) {
			associatedPlayer.getField().getWaitingRoom().setCard(selected);
			clockZone.remove(swappedIndex);
			selected = null;
		}
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

			if (selected != null
				&& thisCard.getUniqueID().equals(selected.getUniqueID())
				&& swappedIndex == i) {
				swappedIndex = i;
				thisCard.toCanvas().setLocation(
					(int) (x + 110 * i * Game.gameScale), y);
			} else {
				thisCard.toCanvas().setLocation(
					(int) (x + 110 * i * Game.gameScale), y);
			}
			thisCard.toCanvas().paint(g);
		}

		if (shiftMode) {
			Graphics2D g2 = (Graphics2D) g;
			Card thisCard = clockZone.get(swappedIndex);

			Color curr = g2.getColor();
			g2.setColor(Color.BLUE);
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(6));
			g2.drawRect((int) thisCard.getCardBound().getX(), (int) thisCard
				.getCardBound().getY(), (int) thisCard.getCardBound().getWidth(),
				(int) thisCard.getCardBound().getHeight());
			g2.setStroke(oldStroke);
			g2.setColor(curr);
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
				+ card.getCardName() + " : " + card.getCardBound().x + " + "
				+ card.getCardBound().width + " , " + card.getCardBound().y + " + "
				+ card.getCardBound().height;
			if (card.getCardBound().contains(e.getPoint())) {
				// System.out.println("click: " + i + ". " + card);
				swappedIndex = i;
				// if (e.getButton() == MouseEvent.BUTTON1) {
				// clockZone.remove(i);
				// }
				output += " match! " + swappedIndex;
				System.out.println(output);
				return card;
			}
			System.out.println(output);
		}
		return null;
	}

	public boolean contains(Point p) {
		for (int i = 0; i < clockZone.size(); i++) {
			Card card = clockZone.get(i);
			if (card.getCardBound().contains(p)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		selected = selectCard(e);
		if (containCards() == false || selected == null)
			return;

		associatedPlayer.getField().setSelected(selected);

		if (e.getButton() == MouseEvent.BUTTON1) {
			associatedPlayer.getField().repaintElements();
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			// to shift, select a card from hand first, then select a
			// card from the clock
			constructPopup(e);
			// shift(associatedPlayer.getHand().getSelected());
		}

		setShiftMode(false);
	}

	public boolean isList() {
		return true;
	}

	public Card getSelected() {
		return selected;
	}

	public int getCount() {
		return clockZone.size();
	}

	public boolean isShiftMode() {
		return shiftMode;
	}

	public void setShiftMode(boolean shiftMode) {
		this.shiftMode = shiftMode;
	}
}
