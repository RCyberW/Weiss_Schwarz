package Field;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import CardAssociation.Card;
import Game.Player;

public class DisplayList extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3238961175016650183L;

	private ArrayList<Card> cardList;
	protected Card selectedCard;
	protected Player thisPlayer;
	
	public DisplayList() {
		cardList = new ArrayList<Card>();
		thisPlayer = new Player();
	}

	public DisplayList(ArrayList<Card> importList, Player p) {
		cardList = importList;
		thisPlayer = p;
	}

	public JScrollPane fillPane() {
		JPanel displayPanel = new JPanel();

		Box displayArea = Box.createVerticalBox();
		Box row = Box.createHorizontalBox();

		for (int i = 0; i <= cardList.size(); i++) {
			if (i % 7 == 0 && i != 0) {
				displayArea.add(row);
				row = Box.createHorizontalBox();
			}
			final Card thisCard = cardList.get(i);
			JLabel tempLab = thisCard.initiateImage();

			MouseListener listener = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					selectedCard = thisCard;
				}
			};

			tempLab.addMouseListener(listener);
			row.add(tempLab);
		}
		if ((cardList.size() + 1) % 7 != 0) {
			displayArea.add(row);
		}

		displayPanel.add(displayArea);

		return new JScrollPane(displayPanel);
	}
	
	public Box setButtons() {
		Box buttonRow = Box.createHorizontalBox();
		
		JButton submit = new JButton("Select");
		submit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				thisPlayer.getField().getRandomZone().setCard(selectedCard);
				dispose();
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		buttonRow.add(submit);
		buttonRow.add(cancel);
		
		return buttonRow;
	}
	
	public void buildSelector() {
		add(fillPane(), BorderLayout.CENTER);		
		add(setButtons(), BorderLayout.PAGE_END);
		pack();
	}
	
	public static void main(String[] args) {
		DisplayList displayGui = new DisplayList();
		displayGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		displayGui.buildSelector();
		displayGui.setVisible(true);
	}
}
