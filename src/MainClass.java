import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import CardAssociation.Card;
import CardAssociation.Deck;

public class MainClass {

	private static Deck currentDeck;
	private static HashMap<String, Card> cardHolder;

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
		final String selectedDeck = "Cafe";
		final JFrame testFrame = new JFrame("Testificate MD");

		final JPanel internalPanel = new JPanel();
		internalPanel.setLayout(new GridLayout(6, 9));

		currentDeck = new Deck();
		currentDeck.loadRaw(new File("Deck/" + selectedDeck), cardHolder);
		System.out.println(selectedDeck + " has "
				+ currentDeck.getPlayingDeck().size() + " cards");

		for (int i = 0; i < currentDeck.getCards().size(); i++) {
			Image image;
			try {
				image = currentDeck.getCards().get(i).getCardImage();
				// ImageIcon img = new ImageIcon(image);
				ImageIcon img = new ImageIcon(image.getScaledInstance(
						(int) (image.getWidth(null) * 0.33),
						(int) (image.getHeight(null) * 0.33),
						Image.SCALE_SMOOTH));
				internalPanel.add(new JLabel(img));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		JButton saveImg = new JButton("Export as image");

		ActionListener listener2 = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				JFrame win = (JFrame) SwingUtilities
						.getWindowAncestor(internalPanel);
				// JFrame win = new JFrame();
				Dimension size = internalPanel.getSize();
				BufferedImage image = (BufferedImage) win.createImage(
						size.width, size.height);
				System.out.println(size.width + " x " + size.height);
				Graphics g = image.getGraphics();
				win.paint(g);
				g.dispose();
				try {
					ImageIO.write(image, "png", new File(selectedDeck + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};

		saveImg.addActionListener(listener2);

		internalPanel.add(saveImg);

		testFrame.setContentPane(internalPanel);
		testFrame.pack();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setVisible(true);
	}

	public static void main(String[] args) {
		MainClass testClass = new MainClass();
		testClass.buildAndDisplay();
	}
}