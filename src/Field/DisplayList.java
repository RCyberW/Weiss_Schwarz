package Field;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import CardAssociation.*;
import Game.Player;

public class DisplayList extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3238961175016650183L;

	protected ArrayList<Card> cardList;
	protected ArrayList<Card> showList;
	protected Card selectedCard;
	protected Player thisPlayer;
	protected Box displayInfo;
	protected int cardsPerRow;

	public DisplayList() {
		cardList = new ArrayList<Card>();
		showList = new ArrayList<Card>();
		thisPlayer = new Player();
		displayInfo = Box.createHorizontalBox();
		cardsPerRow = 10;
	}

	public DisplayList(ArrayList<Card> importList, Player p) {
		this();
		cardList = importList;
		thisPlayer = p;
		// toFront();
	}

	protected JScrollPane fillPane() {
		JPanel displayPanel = new JPanel();

		Box displayArea = Box.createVerticalBox();
		Box row = Box.createHorizontalBox();

		for (int i = 0; i < cardList.size(); i++) {
			if (i % cardsPerRow == 0 && i != 0) {
				row.setAlignmentY(LEFT_ALIGNMENT);
				displayArea.add(row);
				row = Box.createHorizontalBox();
			}
			final Card thisCard = cardList.get(cardList.size() - 1 - i);
			JLabel tempLab = thisCard.initiateImage();

			MouseListener listener = new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					selectedCard = thisCard;
					refresh();
				}
			};

			tempLab.addMouseListener(listener);
			row.add(tempLab);
		}
		// if ((cardList.size() + 1) % cardsPerRow != 0) {
		row.setAlignmentY(LEFT_ALIGNMENT);
		displayArea.add(row);
		// }

		displayPanel.add(displayArea);
		displayArea.setAlignmentY(LEFT_ALIGNMENT);

		return new JScrollPane(displayPanel);
	}

	protected Box setButtons() {
		Box buttonRow = Box.createHorizontalBox();

		JButton submit = new JButton("Select");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thisPlayer.getField().getRandomZone().setCard(selectedCard);
				cardList.remove(selectedCard);
				dispose();
				thisPlayer.getField().repaintElements();
			}
		});

		buttonRow.add(submit);

		if (cardList.size() > 0) {
			JButton toRoom = new JButton("Top to Waiting Room");
			toRoom.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					thisPlayer.getField().getWaitingRoom()
							.setCard(cardList.get(0));
					thisPlayer.getField().getDeckZone().removeTop();
					dispose();
					thisPlayer.getField().repaintElements();
				}
			});

			JButton toBottom = new JButton("Top to Bottom");
			toBottom.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					thisPlayer.getField().getDeckZone()
							.setBotCard(cardList.get(0));
					thisPlayer.getField().getDeckZone().removeTop();
					dispose();
					thisPlayer.getField().repaintElements();
				}
			});
			buttonRow.add(toBottom);
			buttonRow.add(toRoom);
		}

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				thisPlayer.getField().repaintElements();
			}
		});

		buttonRow.add(cancel);

		buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);
		return buttonRow;
	}

	protected Box displaySelect() {
		if (selectedCard == null) {
			displayInfo.setPreferredSize(new Dimension(500, 150));
			return displayInfo;
		}

		int height = 100, width = 50;

		displayInfo.setPreferredSize(new Dimension(500, 150));

		try {
			height = selectedCard.getCardImage().getHeight(null);
			width = selectedCard.getCardImage().getWidth(null);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JPanel image = selectedCard.displayImage(width, height);
		displayInfo.add(image);

		Box cardInfo = Box.createVerticalBox();

		JTextArea cardTitle = new JTextArea(selectedCard.getCardName());
		cardTitle.setLineWrap(true);
		cardTitle.setWrapStyleWord(true);
		cardTitle.setEditable(false);

		JTextArea cardNumber = new JTextArea("level: "
				+ selectedCard.getLevel() + " cost: " + selectedCard.getCost()
				+ " trigger: " + selectedCard.getTrigger());
		cardNumber.setLineWrap(true);
		cardNumber.setWrapStyleWord(true);
		cardNumber.setEditable(false);

		JTextArea power = new JTextArea("power: " + selectedCard.getPower()
				+ " soul: " + selectedCard.getSoul());
		power.setLineWrap(true);
		power.setWrapStyleWord(true);
		power.setEditable(false);

		JTextArea text = new JTextArea(selectedCard.getEffects());
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);

		JScrollPane effect = new JScrollPane(text);
		cardInfo.add(cardTitle);
		cardInfo.add(cardNumber);
		cardInfo.add(power);
		cardInfo.add(effect);
		displayInfo.add(cardInfo);

		displayInfo.setAlignmentY(LEFT_ALIGNMENT);
		displayInfo.setAlignmentY(TOP_ALIGNMENT);

		return displayInfo;
	}

	protected void buildSelector() {
		displaySelect();

		JPanel buttons = new JPanel();
		buttons.add(setButtons());
		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);

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
		
		setResizable(false);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		DisplayList displayGui = new DisplayList();
		displayGui.setDefaultCloseOperation(EXIT_ON_CLOSE);
		displayGui.buildSelector();
		displayGui.setVisible(true);
	}
}
