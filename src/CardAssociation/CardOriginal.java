/**
 * @file Card.java
 * @author Jia Chen
 * @date Sept 05, 2011
 * @description 
 * 		Card.java is an object representation of the card used in
 * 		this application.
 */

package CardAssociation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CardOriginal extends JLabel
		implements
			Serializable,
			MouseListener,
			MouseMotionListener,
			Comparable<Object> {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 5876059325645604130L;
	private static final long serialVersionUID = 5876059325645604132L;

	// card properties
	private String id;
	private String name;
	private int dupCount = 0;
	private ArrayList<String> effects;
	private int power;
	private Trigger trigger;
	private int level;
	private int cost;
	private int soul;
	private Type t;
	private CCode c;
	private String trait1;
	private String trait2;
	private String flavorText;
	private File imageFile;

	private State currentState;

	private Rectangle cardBound;
	private BufferedImage cardImage;

	private Rectangle cardBound2;

	// create a card
	public CardOriginal(String id, String name) {
		setID(id);
		setName(name);
		effects = new ArrayList<String>();
		flavorText = "";
		setCurrentState(State.NONE);
	}

	// create a card v2
	public CardOriginal() {
		effects = new ArrayList<String>();
		flavorText = "";
		setCurrentState(State.NONE);
	}

	public void setID(String id) {
		this.id = id;
	}

	// set the card image
	public void setImage(File f) {
		imageFile = f;
	}

	// get the card image
	public File getImage() {
		return imageFile;
	}

	// display the card image
	public JPanel displayImage(int w, int h) {

		JPanel imagePane = new JPanel();
		imagePane.setPreferredSize(new Dimension(w, h));

		try {
			final URL imageSrc = (imageFile.toURI()).toURL();

			class ImageDisplay extends Canvas {

				private static final long serialVersionUID = -6103863323481913327L;

				BufferedImage bg;
				int width;
				int height;

				ImageDisplay() {
					try {
						bg = ImageIO.read(imageSrc);
						width = bg.getWidth();
						height = bg.getHeight();
					} catch (IOException e) {
						System.out.println("Unable to find image "
								+ imageSrc.getPath());
					}
				}

				public Dimension getPreferredSize() {
					return new Dimension(width, height);
				}

				public void paint(Graphics g) {
					g.drawImage(bg, 0, 0, null);
				}
			}

			ImageDisplay image = new ImageDisplay();

			imagePane.add(image);

			return imagePane;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	// display the card information
	/*
	 * public JPanel getInfoPane(int w, int h) {
	 * 
	 * // Font font = new Font("Courier New", Font.BOLD, 12);
	 * 
	 * JPanel infoPanel = new JPanel(); infoPanel.setPreferredSize(new
	 * Dimension(w, h));
	 * 
	 * GroupLayout layout = new GroupLayout(infoPanel);
	 * infoPanel.setLayout(layout); layout.setAutoCreateGaps(true);
	 * layout.setAutoCreateContainerGaps(true);
	 * 
	 * JTextArea description = new JTextArea(10, 10); if (c == CCode.RED)
	 * description.setBackground(Color.PINK); else if (c == CCode.BLUE)
	 * description.setBackground(Color.CYAN); else if (c == CCode.YELLOW)
	 * description.setBackground(Color.YELLOW); else if (c == CCode.GREEN)
	 * description.setBackground(Color.GREEN); // description.setFont(font);
	 * description.setLineWrap(true); description.setWrapStyleWord(true);
	 * description.setEditable(false);
	 * 
	 * String cardText = ""; if (t == Type.CHARACTER) { if
	 * (!getTrait1().equals("N/A")) cardText += getTrait1(); if
	 * (!getTrait2().equals("N/A")) cardText += " | " + getTrait2(); cardText +=
	 * "\n\n"; } cardText += getEffects() + "\n"; if
	 * (!getFlavorText().equals("")) { cardText += "Flavor Text: \n" +
	 * getFlavorText(); } description.setText(cardText);
	 * 
	 * description.setCaretPosition(0); JScrollPane descContainer = new
	 * JScrollPane(description);
	 * 
	 * JLabel nameLabel = new JLabel(); // nameLabel.setFont(font);
	 * nameLabel.setText(name);
	 * 
	 * JLabel idLabel = new JLabel(id); // idLabel.setFont(font); JLabel
	 * typeLabel = new JLabel(t.toString()); // typeLabel.setFont(font); JLabel
	 * levelLabel = new JLabel("Level: " + level); // levelLabel.setFont(font);
	 * JLabel costLabel = new JLabel("Cost: " + cost); //
	 * costLabel.setFont(font); JLabel soulLabel = new JLabel("Trigger: " +
	 * trigger.toString()); // soulLabel.setFont(font); JLabel powerLabel = new
	 * JLabel("Power: " + power); // powerLabel.setFont(font); JLabel
	 * damageLabel = new JLabel("Soul: " + soul); // damageLabel.setFont(font);
	 * layout.setAutoCreateGaps(true); layout.setAutoCreateContainerGaps(true);
	 * 
	 * layout.setHorizontalGroup(layout .createParallelGroup(LEADING)
	 * .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 350,
	 * GroupLayout.PREFERRED_SIZE) .addGroup( layout.createSequentialGroup()
	 * .addGroup( layout.createParallelGroup(LEADING) .addGroup(
	 * layout.createSequentialGroup() .addComponent( idLabel,
	 * GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)) .addGroup(
	 * layout.createSequentialGroup() .addComponent( levelLabel,
	 * GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
	 * .addComponent( costLabel))) .addGroup(
	 * layout.createParallelGroup(LEADING) .addGroup(
	 * layout.createSequentialGroup() .addComponent( typeLabel)) .addGroup(
	 * layout.createSequentialGroup() .addComponent( powerLabel,
	 * GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))) .addGroup(
	 * layout.createParallelGroup(LEADING) .addGroup(
	 * layout.createSequentialGroup() .addComponent( soulLabel,
	 * GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)) .addGroup(
	 * layout.createSequentialGroup() .addComponent( damageLabel))))
	 * .addComponent(descContainer, GroupLayout.PREFERRED_SIZE, 350,
	 * GroupLayout.PREFERRED_SIZE));
	 * 
	 * layout.setVerticalGroup(layout .createSequentialGroup() .addGroup(
	 * layout.createParallelGroup(LEADING).addComponent( nameLabel)) .addGroup(
	 * layout.createParallelGroup(LEADING)
	 * .addComponent(idLabel).addComponent(typeLabel) .addComponent(soulLabel))
	 * .addGroup( layout.createParallelGroup(LEADING) .addComponent(levelLabel)
	 * .addComponent(costLabel) .addComponent(powerLabel)
	 * .addComponent(damageLabel)) .addGroup(
	 * layout.createParallelGroup(LEADING).addComponent( descContainer)));
	 * 
	 * // System.out.println("getInfoPane");
	 * 
	 * return infoPanel;
	 * 
	 * }
	 */

	public JPanel getInfoPane(int w, int h) {

		// Font font = new Font("Courier New", Font.BOLD, 12);

		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(w, h));

		GroupLayout layout = new GroupLayout(infoPanel);
		infoPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JTextArea description = new JTextArea(10, 10);
		if (c == CCode.RED)
			description.setBackground(Color.PINK);
		else if (c == CCode.BLUE)
			description.setBackground(Color.CYAN);
		else if (c == CCode.YELLOW)
			description.setBackground(Color.YELLOW);
		else if (c == CCode.GREEN)
			description.setBackground(Color.GREEN);
		// description.setFont(font);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);

		String cardText = "";
		if (t == Type.CHARACTER) {
			if (!getTrait1().equals("N/A"))
				cardText += getTrait1();
			if (!getTrait2().equals("N/A"))
				cardText += " | " + getTrait2();
			cardText += "\n\n";
		}
		cardText += getEffects() + "\n";
		if (!getFlavorText().equals("")) {
			cardText += "Flavor Text: \n" + getFlavorText();
		}
		description.setText(cardText);

		description.setCaretPosition(0);
		JScrollPane descContainer = new JScrollPane(description);

		JLabel nameLabel = new JLabel();
		// nameLabel.setFont(font);
		nameLabel.setText(name);

		JLabel idLabel = new JLabel(id);
		// idLabel.setFont(font);
		JLabel typeLabel = new JLabel(t.toString());
		// typeLabel.setFont(font);
		JLabel levelLabel = new JLabel("Level: " + level);
		// levelLabel.setFont(font);
		JLabel costLabel = new JLabel("Cost: " + cost);
		// costLabel.setFont(font);
		JLabel soulLabel = new JLabel("Trigger: " + trigger.toString());
		// soulLabel.setFont(font);
		JLabel powerLabel = new JLabel("Power: " + power);
		// powerLabel.setFont(font);
		JLabel damageLabel = new JLabel("Soul: " + soul);
		// damageLabel.setFont(font);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createParallelGroup()
				.addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 350,
						GroupLayout.PREFERRED_SIZE)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup()
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		idLabel,
																		GroupLayout.PREFERRED_SIZE,
																		125,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		levelLabel,
																		GroupLayout.PREFERRED_SIZE,
																		60,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(
																		costLabel)))
								.addGroup(
										layout.createParallelGroup()
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		typeLabel))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		powerLabel,
																		GroupLayout.PREFERRED_SIZE,
																		90,
																		GroupLayout.PREFERRED_SIZE)))
								.addGroup(
										layout.createParallelGroup()
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		soulLabel,
																		GroupLayout.PREFERRED_SIZE,
																		130,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		damageLabel))))
				.addComponent(descContainer, GroupLayout.PREFERRED_SIZE, 350,
						GroupLayout.PREFERRED_SIZE));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup().addComponent(nameLabel))
				.addGroup(
						layout.createParallelGroup().addComponent(idLabel)
								.addComponent(typeLabel)
								.addComponent(soulLabel))
				.addGroup(
						layout.createParallelGroup().addComponent(levelLabel)
								.addComponent(costLabel)
								.addComponent(powerLabel)
								.addComponent(damageLabel))
				.addGroup(
						layout.createParallelGroup()
								.addComponent(descContainer)));

		// System.out.println("getInfoPane");

		return infoPanel;

	}

	// used in Deck.java to check how many copies of the card is there
	public int getCardCount() {
		return dupCount;
	}

	// check if two cards are the same
	public boolean equals(Object o) {
		CardOriginal c = (CardOriginal) o;
		return c.getName().equals(name);
	}

	public int compareTo(Object arg0) {
		return id.compareTo(((CardOriginal) arg0).id);
	}

	// get the card ID of the card
	public String getID() {
		return id;
	}

	// used in Deck.java to increment the number of copies of the card
	public void addCount() {
		dupCount++;
	}

	public void setCount(int dupCount) {
		this.dupCount = dupCount;
	}

	// set the card name of the card
	public void setName(String name) {
		this.name = name;
	}

	// get the card name of the card
	public String getName() {
		return name;
	}

	// get the card effects
	public String getEffects() {
		String result = "";

		for (int i = 0; i < effects.size(); i++) {
			result += effects.get(i) + "\n";
		}

		return result;
	}

	// set the card effects
	public void addEffect(String e) {
		effects.add(e);
	}

	// set the power value of the card
	public void setPower(int power) {
		this.power = power;
	}

	// get the power value of the card
	public int getPower() {
		return power;
	}

	// set the soul count of the card
	public void setSoul(int soul) {
		this.soul = soul;
	}

	// get the soul count of the card
	public int getSoul() {
		return soul;
	}

	// set the color of the card
	public void setC(CCode c) {
		this.c = c;
	}

	// get the color of the card
	public CCode getC() {
		return c;
	}

	// set the first trait of the card
	public void setTrait1(String trait1) {
		this.trait1 = trait1;
	}

	// get the first trait of the card
	public String getTrait1() {
		return trait1;
	}

	// set the second trait of the card
	public void setTrait2(String trait2) {
		this.trait2 = trait2;
	}

	// get the second trait of the card
	public String getTrait2() {
		return trait2;
	}

	// set the level of the card
	public void setLevel(int level) {
		this.level = level;
	}

	// get the level of the card
	public int getLevel() {
		return level;
	}

	// set the cost of the card
	public void setCost(int cost) {
		this.cost = cost;
	}

	// get the cost of the card
	public int getCost() {
		return cost;
	}

	// set the trigger information of the card
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
		if (this.trigger == null)
			this.trigger = Trigger.NONE;
	}

	// get the trigger information of the card
	public Trigger getTrigger() {
		return trigger;
	}

	// get the card type
	public void setT(Type t) {
		this.t = t;
	}

	// set the card type
	public Type getT() {
		return t;
	}

	// return a String representation of the card
	public String toString() {
		if (cardBound != null)
			System.out.println("hitbox (" + cardBound.x + ", " + cardBound.y
					+ ")");
		return id + ":" + name;
	}

	// used in Deck.java to reset the number of cards in the deck
	public void resetCount() {
		dupCount = 0;
	}

	// used in Deck.java to decrement the number of copies of the card
	public void removeCount() {
		dupCount--;
	}

	// used in Game.java to set the current state of the card
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	// used in Game.java to get the current state of the card
	public State getCurrentState() {
		return currentState;
	}

	public boolean meetsRequirement(String sId, String sName, CCode sColor,
			Type sType, int sLevel, int sCost, Trigger sTrigger, int sPower,
			int sSoul, String sTrait, String sAbility) {

		boolean isMet = true;

		if (!id.isEmpty()) {
			isMet = isMet && id.toLowerCase().contains(sId.toLowerCase());
		}

		if (!sName.isEmpty()) {

			isMet = isMet && name.toLowerCase().contains(sName.toLowerCase());
		}

		if (sColor != null) {
			isMet = isMet && (sColor == c);
		}

		if (sType != null) {
			isMet = isMet && (sType == t);
		}

		if (sLevel > -1) {
			isMet = isMet && (sLevel == level);
		}

		if (sCost > -1) {
			isMet = isMet && (sCost == cost);
		}

		if (sTrigger != null) {
			isMet = isMet && (sTrigger == trigger);
		}

		if (sPower > -1) {
			isMet = isMet && (sPower == power);
		}

		if (sSoul > -1) {
			isMet = isMet && (sSoul == soul);
		}

		if (!sTrait.isEmpty()) {
			isMet = isMet
					&& (trait1.toLowerCase().contains(sTrait) || trait2
							.toLowerCase().contains(sTrait));
		}

		if (!sAbility.isEmpty()) {

			String[] parts = sAbility.split(" ");

			for (int i = 0; i < parts.length; i++) {
				isMet = isMet
						&& getEffects().toLowerCase().contains(
								parts[i].toLowerCase());
			}
		}

		return isMet;
	}

	public void setFlavorText(String flavorText) {
		this.flavorText = flavorText;
	}

	public String getFlavorText() {
		return flavorText;
	}

	public void setCardBound2(int x, int y, int width, int height) {
		cardBound2 = new Rectangle((int) (x), (int) (y),
				(int) (width / Game.Game.gameScale),
				(int) (height / Game.Game.gameScale));
	}

	public Rectangle getCardBound2() {
		return cardBound2;
	}

	public Rectangle getCardBound() {
		// System.out.println(name + " " + cardBound.getX() + ", "
		// + cardBound.getY());
		return cardBound;
	}

	public void paint(Graphics g, int x, int y, boolean isFaceUp,
			boolean isHorizontal) {

		// System.out.println("card paint : " + name + " : " + x + ", " + y);

		Graphics2D g2 = (Graphics2D) g;
		// g2.scale(Game.Game.gameScale, Game.Game.gameScale);

		if (currentState == State.REST) {
			isHorizontal = true;
		} else if (currentState == State.STAND) {
			isHorizontal = false;
		}

		try {
			// cardImage = ImageIO.read((imageFile.toURI()).toURL());
			if (!isFaceUp) {
				cardImage = ImageIO.read(((new File(
						"FieldImages/cardBack-s.jpg")).toURI()).toURL());
			} else {
				cardImage = ImageIO.read((imageFile.toURI()).toURL());
			}

			cardBound = new Rectangle((int) (x * Game.Game.gameScale), (int) (y
					* Game.Game.gameScale + Game.Game.translatedY),
					(int) (cardImage.getWidth(null) * Game.Game.gameScale),
					(int) (cardImage.getHeight(null) * Game.Game.gameScale));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (isHorizontal) {
			AffineTransform at = new AffineTransform();
			at.scale(1, 1);
			at.rotate(90 * Math.PI / 180, cardImage.getWidth() / 2,
					cardImage.getHeight() / 2);

			BufferedImageOp newImg = new AffineTransformOp(at,
					AffineTransformOp.TYPE_BILINEAR);

			BufferedImage transformedCard = newImg.filter(cardImage, null);
			g2.drawImage(transformedCard, x, y, null);
		} else {
			g2.drawImage(cardImage, x, y, null);
		}
		// g.drawImage(cardImage, x, y, null);
		g.drawString(name, x + 10, y + 10);
	}

	public void paint(Graphics g) {
		paint(g, 10, 10, true, false);
	}

	public BufferedImage getCardImage() {
		return cardImage;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (cardBound.contains(e.getX(), e.getY()))
			System.out.println("Clicked " + name);

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (cardBound.contains(e.getX(), e.getY()))
			System.out.println("Pressed " + name);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// if (cardBound.contains(e.getX(), e.getY()))
		// System.out.println("Released " + name);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public CardOriginal clone() {
		CardOriginal cloned = new CardOriginal(id, name);

		cloned.setCount(dupCount);
		cloned.setEffects(effects);
		cloned.setPower(power);
		cloned.setTrigger(trigger);
		cloned.setLevel(level);
		cloned.setCost(cost);
		cloned.setSoul(soul);
		cloned.setT(t);
		cloned.setC(c);
		cloned.setTrait1(trait1);
		cloned.setTrait2(trait2);
		cloned.setFlavorText(flavorText);
		cloned.setImage(imageFile);

		return cloned;
	}

	private void setEffects(ArrayList<String> effects) {
		this.effects = effects;
	}

	public void paintCard(Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		try {

			cardImage = ImageIO.read((imageFile.toURI()).toURL());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		g2.drawImage(cardImage, x, y, null);

	}

	public JLabel getLabel() throws MalformedURLException, IOException {

		JLabel retLabel;

		if (imageFile == null) {
			System.out.println("DEFAULT IMAGE");
			Image image = ImageIO
					.read(((new File("FieldImages/cardBack-s.jpg")).toURI())
							.toURL());
			ImageIcon img = new ImageIcon(image.getScaledInstance(
					(int) (103 * 0.44), (int) (150 * 0.44), Image.SCALE_SMOOTH));
			retLabel = new JLabel(img);
			return retLabel;
		}
		Image image;
		image = ImageIO.read((imageFile.toURI()).toURL());
		ImageIcon img = new ImageIcon(image.getScaledInstance(
				(int) (image.getWidth(null) * 0.44),
				(int) (image.getHeight(null) * 0.44), Image.SCALE_SMOOTH));

		retLabel = new JLabel();
		retLabel.setIcon(img);
		return retLabel;
	}

}
