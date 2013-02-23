package Field;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import CardAssociation.Card;
import CardAssociation.Type;
import Game.Game;
import Game.Phase;
import Game.Player;

public class Hand extends FieldElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7959268439908932650L;
	private ArrayList<Card> handCards;

	// private Player associatedPlayer;
	private int selectedIndex = -1;
	private Card selected = null;

	public Hand(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Hand", player);
		handCards = new ArrayList<Card>();
		// associatedPlayer = player;
	}

	public void setCard(Card c) {
		if (c != null)
			handCards.add(c);
	}

	public ArrayList<Card> showHand() {
		return handCards;
	}

	public Card playCard(Card c) {
		if (handCards.remove(c))
			return c;
		return null;
	}

	@Override
	public boolean containCards() {
		return handCards.size() > 0;
	}

	@Override
	public void paint(Graphics g, Card c) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.pink);
		g2.setBackground(Color.getHSBColor(61, 87, 64));
		g2.fillRect(0, 0, Game.maxWidth, Game.maxHeight);
		for (int i = 0; i < handCards.size(); i++) {
			Card thisCard = handCards.get(i);
			// System.out.print(thisCard.getCardName() + ", ");
			thisCard.setDisplay(true, false);
			thisCard.toCanvas().setLocation(
					(int) ((x + 110 * i) * Game.gameScale), (int) (y));
			if (selected != null
					&& thisCard.getCardName().equals(selected.getCardName())) {
				thisCard.toCanvas().setLocation(
						(int) ((x + 110 * i) * Game.gameScale), y - 10);
			}
			thisCard.toCanvas().paint(g2);
		}

		System.out.println("HAND " + handCards);

		g2.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g2.setColor(Color.BLUE);

		g2.drawString("Player Hand", this.x + 10, this.y + 20);
	}

	private void constructPopup(MouseEvent e) {

		final JPopupMenu popmenu = new JPopupMenu();

		JMenuItem fieldAction = new JMenuItem("to field");
		fieldAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toField();
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(fieldAction);

		JMenuItem waitingRoomAction = new JMenuItem("to waiting room");
		waitingRoomAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toWaitingRoom();
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(waitingRoomAction);

		JMenuItem deckTopAction = new JMenuItem("to top deck");
		deckTopAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toDeck(true);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(deckTopAction);

		JMenuItem deckBotAction = new JMenuItem("to bottom deck");
		deckBotAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toDeck(false);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(deckBotAction);

		JMenuItem memoryAction = new JMenuItem("to memory");
		memoryAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toMemory();
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(memoryAction);

		JMenuItem clockAction = new JMenuItem("to clock");
		clockAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toClock();
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(clockAction);

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void toMemory() {
		associatedPlayer.getField().getMemoryZone().setCard(selected);
	}

	private void toDeck(boolean toTop) {
		if (toTop)
			associatedPlayer.getField().getDeckZone().setCard(selected);
		else
			associatedPlayer.getField().getDeckZone().setBotCard(selected);
	}

	private void toWaitingRoom() {
		associatedPlayer.getField().getWaitingRoom().setCard(selected);
	}

	private void toClock() {
		if (selectedIndex > -1) {
			Card temp = handCards.remove(selectedIndex);
			selectedIndex = -1;
			associatedPlayer.getField().getClockZone().setCard(temp);
			if (associatedPlayer.getCurrentPhase() == Phase.CLOCK_PHASE) {
				associatedPlayer.getField().getDeckZone().drawCard();
				associatedPlayer.getField().getDeckZone().drawCard();
			}
		}

	}

	private void toField() {
		if (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE) {
			if (selected.getT() == Type.CHARACTER) {
				if (!associatedPlayer.getField().getFrontRow1().containCards()) {
					associatedPlayer.getField().getFrontRow1()
							.setCard(selected);
				} else if (!associatedPlayer.getField().getFrontRow2()
						.containCards()) {
					associatedPlayer.getField().getFrontRow2()
							.setCard(selected);
				} else if (!associatedPlayer.getField().getFrontRow3()
						.containCards()) {
					associatedPlayer.getField().getFrontRow3()
							.setCard(selected);
				} else if (!associatedPlayer.getField().getBackRow1()
						.containCards()) {
					associatedPlayer.getField().getBackRow1().setCard(selected);
				} else if (!associatedPlayer.getField().getBackRow2()
						.containCards()) {
					associatedPlayer.getField().getBackRow2().setCard(selected);
				} else {
					selectedIndex = -1;
				}
				if (selectedIndex > -1) {
					handCards.remove(selectedIndex);
				}
			} else if (selected.getT() == Type.EVENT) {
				associatedPlayer.getField().getRandomZone().setCard(selected);
				if (selectedIndex > -1) {
					handCards.remove(selectedIndex);
				}
			}
		} else if (associatedPlayer.getCurrentPhase() == Phase.CLIMAX_PHASE
				&& selected.getT() == Type.CLIMAX) {
			associatedPlayer.getField().getClimaxZone().setCard(selected);
			if (selectedIndex > -1) {
				handCards.remove(selectedIndex);
			}
		}
	}

	@Override
	public Card selectCard(MouseEvent e) {
		String output;
		Card card, result = null;
		for (int i = 0; i < handCards.size(); i++) {
			card = handCards.get(i);
			output = "HAND: x = " + e.getX() + ", y = " + e.getY() + " "
					+ card.getCardName() + " : " + card.getCardBound().x
					+ " + " + card.getCardBound().width + " , "
					+ card.getCardBound().y + " + "
					+ card.getCardBound().height;
			if (card.getCardBound().contains(e.getPoint())) {
				selectedIndex = i;
				output += " match!";
				result = card;
			}
			System.out.println(output);
			// System.out.println();
		}
		return result;
	}

	@Override
	public Card showCard() {
		if (handCards.size() > 0)
			return handCards.get(handCards.size() - 1);
		return null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (containCards() == false)
			return;
		selected = selectCard(e);
		if (selected == null)
			return;

		if (e.getButton() == MouseEvent.BUTTON3) {
			constructPopup(e);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			repaint();
		}
		// selected = card;

	}

	public void paint(Graphics g) {
		paint(g, null);
	}

	public boolean isList() {
		return true;
	}

	public Card getSelected() {
		return selected;
	}
}
