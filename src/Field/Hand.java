package Field;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import CardAssociation.Card;
import CardAssociation.State;
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

	public Hand(String imageFileName, int xa, int ya, Player player, int offset) {
		super(imageFileName, xa, ya, "Hand", player, offset);
		handCards = new ArrayList<Card>();
		// associatedPlayer = player;
	}

	public void setCard(Card c) {
		c.setCurrentState(State.STAND);
		if (c != null)
			handCards.add(c);
	}

	public ArrayList<Card> showHand() {
		return handCards;
	}

	public Card removeCard(Card c) {
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
		g2.setColor(Color.WHITE);
		// g2.setBackground(Color.getHSBColor(61, 87, 64));
		g2.fillRect(0, 0, Game.maxWidth, Game.maxHeight);
		for (int i = 0; i < handCards.size(); i++) {
			Card thisCard = handCards.get(i);
			// System.out.print(thisCard.getCardName() + ", ");
			thisCard.toCanvas().setLocation(
				(int) ((x + 110 * i) * Game.gameScale), (int) (y + 5));
			if (selected != null
				&& thisCard.getUniqueID().equals(selected.getUniqueID())) {
				thisCard.toCanvas().setLocation(
					(int) ((x + 110 * i) * Game.gameScale), y);
			}
			thisCard.toCanvas().paint(g2);
		}

		System.out.println("HAND " + handCards);

		// g2.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		// g2.setColor(Color.BLUE);

		// g2.drawString("Player Hand", this.x + 10, this.y + 20);
	}

	protected void constructPopup(MouseEvent e) {

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

		// JMenuItem removePlease = new JMenuItem("removePlease");
		// removePlease.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// associatedPlayer.getField().getLevelZone().setCard(selected);
		// handCards.remove(selectedIndex);
		// associatedPlayer.getField().repaintElements();
		// }
		// });
		// popmenu.add(removePlease);

		JMenuItem resolveAction = new JMenuItem("to resolution area");
		resolveAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				associatedPlayer.getField().getDefenderRandomZone().setCard(selected);
				handCards.remove(selectedIndex);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(resolveAction);

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void toMemory() {
		associatedPlayer.getField().getDefenderMemoryZone().setCard(selected);
		handCards.remove(selectedIndex);
	}

	private void toDeck(boolean toTop) {
		if (toTop)
			associatedPlayer.getField().getDefenderDeckZone().setCard(selected);
		else
			associatedPlayer.getField().getDefenderDeckZone().setBotCard(selected);
		handCards.remove(selectedIndex);
	}

	private void toWaitingRoom() {
		associatedPlayer.getField().getDefenderWaitingRoom().setCard(selected);
		handCards.remove(selectedIndex);
	}

	private void toClock() {
		if (selectedIndex > -1) {
			Card temp = handCards.remove(selectedIndex);
			selectedIndex = -1;
			associatedPlayer.getField().getDefenderClockZone().setCard(temp);
			if (associatedPlayer.getCurrentPhase() == Phase.CLOCK_PHASE) {
				associatedPlayer.getField().getDefenderDeckZone().drawCard();
				associatedPlayer.getField().getDefenderDeckZone().drawCard();
				associatedPlayer.setCurrentPhase(Phase.MAIN_PHASE);
			}
		}

	}

	private void toField() {
		if (associatedPlayer.getCurrentPhase() != Phase.CLIMAX_PHASE
			&& associatedPlayer.getCurrentPhase() != Phase.CLOCK_PHASE) {
			if (selected.getT() == Type.CHARACTER) {
				if (!associatedPlayer.getField().getDefenderFrontRow1().containCards()) {
					associatedPlayer.getField().getDefenderFrontRow1().setCard(selected);
				} else if (!associatedPlayer.getField().getDefenderFrontRow2()
					.containCards()) {
					associatedPlayer.getField().getDefenderFrontRow2().setCard(selected);
				} else if (!associatedPlayer.getField().getDefenderFrontRow3()
					.containCards()) {
					associatedPlayer.getField().getDefenderFrontRow3().setCard(selected);
				} else if (!associatedPlayer.getField().getDefenderBackRow1()
					.containCards()) {
					associatedPlayer.getField().getDefenderBackRow1().setCard(selected);
				} else if (!associatedPlayer.getField().getDefenderBackRow2()
					.containCards()) {
					associatedPlayer.getField().getDefenderBackRow2().setCard(selected);
				} else {
					selectedIndex = -1;
				}
				if (selectedIndex > -1) {
					handCards.remove(selectedIndex);
				}
			} else if (selected.getT() == Type.EVENT) {
				associatedPlayer.getField().getDefenderRandomZone().setCard(selected);
				if (selectedIndex > -1) {
					handCards.remove(selectedIndex);
				}
			}
		} else if (associatedPlayer.getCurrentPhase() == Phase.CLIMAX_PHASE
			&& selected.getT() == Type.CLIMAX) {
			associatedPlayer.getField().getDefenderClimaxZone().setCard(selected);
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
				+ card.getCardName() + "[" + card.getUniqueID() + "]: "
				+ card.getCardBound().x + " + " + card.getCardBound().width + " , "
				+ card.getCardBound().y + " + " + card.getCardBound().height;
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
		
		System.out.println("Hand click: " + e.getX() + ", " + e.getY());

		associatedPlayer.getField().setSelected(selected);

		if (e.getButton() == MouseEvent.BUTTON3) {
			constructPopup(e);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			Clock_Zone tempClock = associatedPlayer.getField().getDefenderClockZone();

			if (tempClock.isShiftMode()) {
				System.out.println("HAND SHIFTING");
				tempClock.shift(selected);
				associatedPlayer.getField().repaintElements();
				handCards.remove(selectedIndex);
				// handCards.add(card);
			}

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

	public int getCount() {
		return handCards.size();
	}

	public Card getSelected() {
		return selected;
	}

	public void preGameDiscard(Game game) {
		PreGameDisplay pgd = new PreGameDisplay(handCards, associatedPlayer);
		pgd.execute();
	}

	public void clear() {
		handCards.clear();
	}
}

class PreGameDisplay extends DisplayList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4051710667562173041L;

	PreGameDisplay(ArrayList<Card> importList, Player p) {
		super(importList, p);
		super.setTitle("Select Card(s) to Drop " + thisPlayer.getPlayerID());
	}

	protected Box setButtons() {
		Box buttonRow = Box.createHorizontalBox();

		JButton add = new JButton("add");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean toAdd = true;
				for (int i = 0; i < showList.size(); i++) {
					if (showList.get(i).getUniqueID()
						.equals(selectedCard.getUniqueID()))
						toAdd = false;
				}
				if (toAdd) {
					System.out.println("QUEUING " + selectedCard.getCardName() + "["
						+ selectedCard.getUniqueID() + "]");
					showList.add(selectedCard);
				}
				refresh();
			}
		});
		buttonRow.add(add);

		JButton remove = new JButton("remove");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("DE-QUEUING " + selectedCard.getCardName() + "["
					+ selectedCard.getUniqueID() + "]");
				showList.remove(selectedCard);
				refresh();
			}
		});
		buttonRow.add(remove);

		JButton discard = new JButton("drop and ready");
		discard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Card card : showList) {
					thisPlayer.getField().getDefenderWaitingRoom().setCard(card);
					thisPlayer.getHand().removeCard(card);
					thisPlayer.getField().getDefenderDeckZone().drawCard();
					System.out.println("POPPING " + card.getCardName() + "["
						+ card.getUniqueID() + "]");
				}
				System.out.println("HAND SIZE: " + thisPlayer.getHand().getCount());
				dispose();

				thisPlayer.drawField();

				thisPlayer.getField().repaintElements();
			}
		});
		buttonRow.add(discard);

		buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);
		return buttonRow;
	}

	protected JScrollPane displayList() {
		String[] cardNames = new String[showList.size()];
		for (int i = 0; i < showList.size(); i++) {
			cardNames[i] = showList.get(i).getCardName();
		}

		@SuppressWarnings({ })
		final JList displayShow = new JList(cardNames);
		displayShow.setPrototypeCellValue("Index 1234567890");
		MouseListener listener = new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				selectedCard = showList.get(displayShow.getSelectedIndex());
				refresh();
			}
		};
		displayShow.addMouseListener(listener);
		JScrollPane jsp = new JScrollPane(displayShow);
		return jsp;
	}

	protected void buildSelector() {
		displaySelect();

		JPanel buttons = new JPanel();
		buttons.add(setButtons());
		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);

		displayInfo.add(displayList());

		add(displayInfo, BorderLayout.PAGE_START);
		add(fillPane(), BorderLayout.CENTER);
		add(buttons, BorderLayout.PAGE_END);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);

	}

	protected void refresh() {
		displayInfo.removeAll();
		displayInfo.validate();
		displaySelect();
		displayInfo.add(displayList());

		setResizable(false);
		pack();
		setVisible(true);
	}

	public boolean execute() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		buildSelector();
		setVisible(true);
		return true;
	}

}
