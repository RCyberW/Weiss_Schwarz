import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import CardAssociation.CCode;
import CardAssociation.Card;
import CardAssociation.Trigger;
import CardAssociation.Type;

public class MainClass {

	private ArrayList<Card> completeList;
	private ArrayList<Card> resultList;
	private JTable listTable;
	private JFrame testRF;
	private JTextField inputField;

	public static void main(String[] args) {
		MainClass test = new MainClass();
		test.tester();
		
	}

	public void tester() {
		deserializer();
		testRF = new JFrame("RefreshTest");

		inputField = new JTextField();

		final KeyListener searchFieldListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				resultList.clear();
				for (Card c : completeList) {
					String cardID = inputField.getText();
					if (c.meetsRequirement(cardID, "", CCode.ALL, Type.ALL, -1,
							-1, Trigger.ALL, -1, -1, "", ""))
						resultList.add(c);
				}
				populate();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		};

		inputField.addKeyListener(searchFieldListener);
		build(true);
	}
	
	private void build(boolean bool) {
		populate();
		JScrollPane jsp = new JScrollPane(listTable);

		testRF.add(inputField, BorderLayout.NORTH);
		testRF.add(jsp, BorderLayout.SOUTH);

		testRF.pack();
		testRF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testRF.setVisible(true);
	}

	private void populate() {
		String[] columnNames = { "ID", "Name", "Color", "Type", "Level",
				"Cost", "Soul", "Power" };
		final ArrayList<Card> allCards = resultList;

		Object[][] cardData = new Object[allCards.size()][columnNames.length];

		for (int i = 0; i < allCards.size(); i++) {
			Card card = allCards.get(i);
			cardData[i][0] = card.getID();
			cardData[i][1] = card.getCardName();
			cardData[i][2] = card.getC();
			cardData[i][3] = card.getT();
			cardData[i][4] = card.getLevel();
			cardData[i][5] = card.getCost();
			cardData[i][6] = card.getSoul();
			cardData[i][7] = card.getPower();
		}
		listTable = new JTable(cardData, columnNames) {
			private static final long serialVersionUID = 3570425890676389430L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};
		
	}

	@SuppressWarnings("unchecked")
	private void deserializer() {

		// FileInputStream fileInput;
		InputStream fileInput;
		ObjectInputStream objectInput;

		try {
			// fileInput = new FileInputStream();
			fileInput = getClass().getResourceAsStream(
					"src/DeckBuilder/CardDatav2");
			objectInput = new ObjectInputStream(fileInput);

			completeList = (ArrayList<Card>) objectInput.readObject();
			resultList = (ArrayList<Card>) completeList.clone();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}