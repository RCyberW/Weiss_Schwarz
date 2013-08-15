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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import CardAssociation.Card;
import CardAssociation.Deck;

public class MainClass {

	private static Deck currentDeck;
	private static HashMap<String, Card> cardHolder;
	private Box displayInfo;
	private Box option;
	private Card selectedCard;
	private JFrame testFrame;

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
		final String selectedDeck = "dc-sara";

		testFrame = new JFrame("Testificate MD");
		testFrame.setPreferredSize(new Dimension(1000, 700));

		final JLayeredPane contentPanel = new JLayeredPane();
		// contentPanel.setBounds(0, 0, 1000, 720);

		testFrame.add(sideBar(), BorderLayout.WEST);

		currentDeck = new Deck();
		currentDeck.loadRaw(new File("Deck/" + selectedDeck), cardHolder);
		System.out.println(selectedDeck + " has "
		   + currentDeck.getPlayingDeck().size() + " cards");

		int x = 10, y = 10, bigBound = 0;

		ArrayList<Card> uniqueCards = currentDeck.getUnique();

		for (int i = 0; i < uniqueCards.size(); i++) {
			final Card card = uniqueCards.get(i);
			if (x + 120 > 650) {
				System.out.println("x = " + x);
				x = 10;
				System.out.println("bigBound = " + bigBound);
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

							System.out.println("selectedCard = "
							   + selectedCard.getCardName());
							setCardInfo();

						}
					});
					cardLabel.setBounds(x, yprime, image.getWidth(null),
					   image.getHeight(null));
					if (z >= card.getCardCount())
						break;
					contentPanel.add(cardLabel, new Integer(0), 0);
					x += image.getWidth(null) * 0.1;
					yprime += image.getHeight(null) * 0.1;
					if (yprime + image.getHeight(null) > bigBound) {
						bigBound = yprime + image.getHeight(null);
						System.out.printf("new bigBound = %d(%d)\n", bigBound, z);
					}
				}

				x += image.getWidth(null) + 10;

			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		System.out.println("contentPanel ("
		   + contentPanel.getPreferredSize().getWidth() + " x "
		   + contentPanel.getPreferredSize().getHeight() + ")");

		contentPanel.setPreferredSize(new Dimension(600, bigBound));

		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(contentPanel);

		testFrame.add(jsp, BorderLayout.CENTER);

		testFrame.pack();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setVisible(true);
	}

	private Box sideBar() {
		Box navigationBar = Box.createVerticalBox();

		displayInfo = Box.createVerticalBox();
		option = Box.createVerticalBox();

		setCardInfo();
		optionBox();

		navigationBar.add(displayInfo);
		navigationBar.add(Box.createVerticalGlue());
		navigationBar.add(option);
		navigationBar.add(Box.createVerticalStrut(10));

		return navigationBar;
	}

	public void optionBox() {
		JButton saveImg = new JButton("Export as image");

		ActionListener listener2 = new ActionListener() {
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
		saveImg.addActionListener(listener2);

		option.add(saveImg);
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