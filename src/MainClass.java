import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import CardAssociation.Card;
import CardAssociation.Deck;

public class MainClass {

	private static Deck currentDeck;
	private static HashMap<String, Card> cardHolder;
	private Box displayInfo;
	private Box option;
	private Card selectedCard;
	private JFrame testFrame;
	private DisplayMode displayMode = DisplayMode.STACKMODE;
	private JScrollPane deckDisplay;
	final private String selectedDeck = "dc-sara";

	private enum DisplayMode {
		GRIDMODE("GridMode"), STACKMODE("StackMode");

		String mode;

		DisplayMode(String mode) {
			this.mode = mode;
		}

		public String toString() {
			return mode;
		}

	}

	@SuppressWarnings("unchecked")
	private void deserializer() {

		// FileInputStream fileInput;
		InputStream fileInput;
		ObjectInputStream objectInput;

		try {
			// fileInput = new FileInputStream();
			System.out.println("Opening data");
			fileInput = getClass().getResourceAsStream("/resources/CardDatav2");
			objectInput = new ObjectInputStream(fileInput);

			objectInput.readObject();
			cardHolder = (HashMap<String, Card>) objectInput.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void buildAndDisplay() {
		deserializer();

		currentDeck = new Deck();
		currentDeck.loadRaw(new File("Deck/" + selectedDeck), cardHolder);

		testFrame = new JFrame("Testificate MD");
		testFrame.setPreferredSize(new Dimension(1000, 700));

		// contentPanel.setBounds(0, 0, 1000, 720);
		deckDisplay = new JScrollPane();
		displayDeck();

		testFrame.add(sideBar(), BorderLayout.WEST);
		testFrame.add(deckDisplay, BorderLayout.CENTER);

		testFrame.pack();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setVisible(true);
	}

	private void displayDeck() {
		deckDisplay.removeAll();
		deckDisplay.validate();

		System.out.println("reset display view " + displayMode.toString());

		JComponent contentPanel;

		if (displayMode == DisplayMode.STACKMODE) {
			contentPanel = new JLayeredPane();
		} else {
			contentPanel = new JPanel();
		}

		int x = 10, y = 10, bigBound = 0;

		ArrayList<Card> uniqueCards = currentDeck.getUnique();

		for (int i = 0; i < uniqueCards.size(); i++) {
			final Card card = uniqueCards.get(i);
			if (x + 120 > 650) {
				x = 10;
				y = 10 + bigBound;
				bigBound = 0;
			}

			Image image;
			try {
				image = card.getCardImage();
				ImageIcon img = new ImageIcon(image.getScaledInstance(
				   (int) (image.getWidth(null)), (int) (image.getHeight(null)),
				   Image.SCALE_SMOOTH));
				JLabel cardLabel = new JLabel(img);

				int yprime = y;

				for (int z = 0; z < card.getCardCount(); z++) {
					cardLabel = new JLabel(img);

					cardLabel.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							selectedCard = card;
							setCardInfo();
						}
					});

					if (displayMode == DisplayMode.STACKMODE)
						cardLabel.setBounds(x, yprime, image.getWidth(null),
						   image.getHeight(null));
					if (z >= card.getCardCount())
						break;
					contentPanel.add(cardLabel, new Integer(0), 0);
					if (displayMode == DisplayMode.STACKMODE) {
						x += image.getWidth(null) * 0.1;
						yprime += image.getHeight(null) * 0.1;
					} else {
						x += image.getWidth(null);
					}
					if (yprime + image.getHeight(null) > bigBound) {
						bigBound = yprime + image.getHeight(null);
						// System.out.printf("new bigBound = %d(%d)\n", bigBound, z);
					}
				}

				x += image.getWidth(null) + 10;

			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		if (displayMode == DisplayMode.STACKMODE)
			contentPanel.setPreferredSize(new Dimension(600, bigBound));
		
		// deckDisplay = new JScrollPane();
		deckDisplay.setViewportView(contentPanel);
		System.out.println("new view " + displayMode.toString());
		testFrame.setVisible(true);
	}

	private Box sideBar() {
		Box navigationBar = Box.createVerticalBox();

		displayInfo = Box.createVerticalBox();
		option = Box.createVerticalBox();

		setCardInfo();
		optionBox();

		navigationBar.add(displayInfo);
		navigationBar.add(new JSeparator());
		navigationBar.add(Box.createVerticalGlue());
		navigationBar.add(option);
		navigationBar.add(Box.createVerticalStrut(10));

		return navigationBar;
	}

	private void optionBox() {
		option.removeAll();
		option.revalidate();

		JButton saveImg = new JButton("Export as image");
		JButton changeView = new JButton("Stack View");
		if (displayMode == DisplayMode.STACKMODE)
			changeView.setText("Grid View");
		JButton printTranslation = new JButton("Print Translation");
		JButton printProxy = new JButton("Print Proxy Size");

		changeView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (displayMode == DisplayMode.STACKMODE) {
					displayMode = DisplayMode.GRIDMODE;
				} else {
					displayMode = DisplayMode.STACKMODE;
				}

				displayDeck();
				optionBox();
			}
		});

		ActionListener printScreen = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// try {
				// JFrame win = new JFrame();
				// win.setContentPane(contentPanel);
				// win.paint(contentPanel.getGraphics());
				// Dimension size = contentPanel.getPreferredSize();
				// BufferedImage image = new BufferedImage((int) size.getWidth(),
				// (int) size.getHeight(), BufferedImage.TYPE_INT_RGB);
				//
				// Graphics g = image.getGraphics();
				// contentPanel.paint(g);
				// // g.dispose();
				//
				// ImageIO.write(image, "png", new File(selectedDeck + ".png"));
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			}
		};
		saveImg.addActionListener(printScreen);

		option.add(saveImg);
		option.add(changeView);
		option.add(printTranslation);
		option.add(printProxy);
	}

	private void setCardInfo() {
		displayInfo.removeAll();
		displayInfo.revalidate();

		Dimension dim = new Dimension(370, 400);
		Box displayArea = Box.createVerticalBox();

		if (selectedCard == null) {
			displayArea.setPreferredSize(dim);
		} else {

			int height = 100, width = 50;

			displayArea.setPreferredSize(dim);
			displayArea.setMinimumSize(dim);
			displayArea.setMaximumSize(dim);

			try {
				height = selectedCard.getCardImage().getHeight(null);
				width = selectedCard.getCardImage().getWidth(null);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JPanel image = selectedCard.displayImage(width, height);
			displayArea.add(image);
			displayArea.add(selectedCard.getInfoPane((int) dim.getWidth(),
			   (int) dim.getHeight()));
		}

		displayInfo.add(displayArea);
		displayInfo.setAlignmentX(Component.TOP_ALIGNMENT);

		testFrame.setVisible(true);
	}

	public static void main(String[] args) {
		MainClass testClass = new MainClass();
		testClass.buildAndDisplay();
	}
}