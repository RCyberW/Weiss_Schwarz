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

		final JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(6, 9));

		currentDeck = new Deck();
		currentDeck.loadRaw(new File("Deck/" + selectedDeck), cardHolder);
		System.out.println(selectedDeck + " has " + currentDeck.getPlayingDeck().size() + " cards");

		for (int i = 0; i < currentDeck.getCards().size(); i++) {
			Image image;
			try {
				image = currentDeck.getCards().get(i).getCardImage();
				// ImageIcon img = new ImageIcon(image);
				ImageIcon img = new ImageIcon(image.getScaledInstance((int) (image.getWidth(null)), (int) (image.getHeight(null)), Image.SCALE_SMOOTH));
				contentPanel.add(new JLabel(img));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		
		final JPanel internalPanel = contentPanel;

		JButton saveImg = new JButton("Export as image");

		ActionListener listener2 = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					JFrame win = new JFrame();
					win.setContentPane(internalPanel);
					win.paint(internalPanel.getGraphics());
					Dimension size = internalPanel.getSize();
					BufferedImage image = new BufferedImage((int) size.getWidth(), (int) size.getHeight(), BufferedImage.TYPE_INT_RGB);

					Graphics g = image.getGraphics();
					internalPanel.paint(g);
					// g.dispose();

					ImageIO.write(image, "png", new File(selectedDeck + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};

		saveImg.addActionListener(listener2);

		

		testFrame.setContentPane(contentPanel);
		testFrame.add(saveImg);
		testFrame.pack();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setVisible(true);
	}

	public static void main(String[] args) {
		MainClass testClass = new MainClass();
		testClass.buildAndDisplay();
	}
}