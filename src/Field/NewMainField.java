package Field;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import CardAssociation.Card;
import CardAssociation.Deck;
import Game.Phase;
import Game.Player;

public class NewMainField extends Canvas implements Serializable,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -2417240192973578906L;

	Rectangle rect;
	BufferedImage bg;
	Graphics2D cardImage;
	// Graphics2D bg2d;
	// BufferedImage bi;
	// Graphics2D big;
	int w, h;

	private Card selectedCard;

	// True if the user pressed, dragged or released the mouse outside of the
	// rectangle; false otherwise.
	boolean pressOut = false;

	// Holds the coordinates of the user's last mousePressed event.
	int last_x, last_y;
	boolean firstTime = true;
	// TexturePaint fillPolka, strokePolka;
	TexturePaint fillImage;
	Rectangle area;

	// Field Elements
	private ArrayList<FieldElement> elements;
	private Clock_Zone cz;
	private Stock_Zone sz;

	private Front_Row fr1;
	private Front_Row fr2;
	private Front_Row fr3;

	private Back_Row br1;
	private Back_Row br2;

	private Waiting_Room wr;
	private Deck_Zone dz;

	private Memory_Zone mz;
	private Level_Zone lz;
	private Climax_Zone az;

	private Random_Zone rz;

	private Player associatedPlayer;

	// private ArrayList<BufferedImage> phaseImages;

	public NewMainField(URL imageSrc, Player player) {
		setBackground(Color.cyan);
		addMouseMotionListener(this);
		addMouseListener(this);

		associatedPlayer = player;
		selectedCard = null;

		try {

			BufferedImage before = ImageIO.read(imageSrc);
			w = (int) (Game.Game.maxWidth * Game.Game.gameScale);
			h = (int) (Game.Game.maxHeight * Game.Game.gameScale);
			bg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

			AffineTransform at = new AffineTransform();
			at.scale(Game.Game.gameScale, Game.Game.gameScale);
			AffineTransformOp scaleOp = new AffineTransformOp(at,
					AffineTransformOp.TYPE_BILINEAR);
			bg = scaleOp.filter(before, null);

			System.out.println(imageSrc.getPath());

			cardImage = bg.createGraphics();
			cardImage.dispose();

			rect = new Rectangle(0, 0, bg.getHeight(null), bg.getWidth(null));

		} catch (IOException e) {
			System.out.println("Image could not be read");
			System.exit(1);
		}
		createElements();

	}

	public NewMainField(Player player) {
		associatedPlayer = player;
		createElements();

	}

	private void createElements() {
		elements = new ArrayList<FieldElement>();

		fr1 = new Front_Row("FieldImages/Vertical.png", 350, 0,
				associatedPlayer);
		fr2 = new Front_Row("FieldImages/Vertical.png", 550, 0,
				associatedPlayer);
		fr3 = new Front_Row("FieldImages/Vertical.png", 750, 0,
				associatedPlayer);
		mz = new Memory_Zone("FieldImages/Horizontal.png", 1000, 0,
				associatedPlayer);
		rz = new Random_Zone("FieldImages/Vertical.png", 50, 0,
				associatedPlayer);

		sz = new Stock_Zone("FieldImages/Horizontal.png", 50, 200,
				associatedPlayer);
		az = new Climax_Zone("FieldImages/Horizontal.png", 250, 200,
				associatedPlayer);
		br1 = new Back_Row("FieldImages/Vertical.png", 450, 200,
				associatedPlayer);
		br2 = new Back_Row("FieldImages/Vertical.png", 650, 200,
				associatedPlayer);
		dz = new Deck_Zone("FieldImages/Vertical.png", 1050, 200,
				associatedPlayer);

		lz = new Level_Zone("FieldImages/Horizontal.png", 50, 480,
				associatedPlayer);
		cz = new Clock_Zone("FieldImages/Vertical.png", 250, 380,
				associatedPlayer);
		wr = new Waiting_Room("FieldImages/Vertical.png", 1050, 400,
				associatedPlayer);

		elements.add(cz);
		elements.add(sz);
		elements.add(fr1);
		elements.add(fr2);
		elements.add(fr3);
		elements.add(wr);
		elements.add(dz);
		elements.add(br1);
		elements.add(br2);
		elements.add(mz);
		elements.add(lz);
		elements.add(az);
		elements.add(rz);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1) {
			if (e.getButton() == MouseEvent.BUTTON1
					&& (associatedPlayer.getCurrentPhase() == Phase.MAIN_PHASE || associatedPlayer
							.getCurrentPhase() == Phase.ATTACK_PHASE)) {
				if (selectedCard == null) {
					// picking up a card from a zone
					for (FieldElement fe : elements) {
						fe.mouseClicked(e);
						if (!fe.isList()) {
							selectedCard = fe.selectCard(e);
						}
						if (selectedCard != null) {
							System.out.println("taking up  "
									+ selectedCard.getCardName());
							break;
						}
					}
				} else {
					// placing a card to a zone
					for (FieldElement fe : elements) {
						if (fe.contains(e.getX(), e.getY())) {
							System.out
									.println("clicked on " + fe.toString()
											+ "(" + e.getX() + "," + e.getY()
											+ ") placing "
											+ selectedCard.getCardName());
							Card tempCard = null;
							if (!fe.isList()) {
								tempCard = fe.selectCard(e);
								if (tempCard != null)
									System.out.println("picking up "
											+ selectedCard.getCardName());
							}
							fe.setCard(selectedCard);
							selectedCard = tempCard;
							break;
						}
					}
					if (selectedCard != null)
						System.out.println("holding "
								+ selectedCard.getCardName());
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				// default action on each zone
				for (FieldElement fe : elements) {
					if (fe.contains(e.getX(), e.getY())) {
						fe.mouseClicked(e);
					}
				}
			}
		}

		repaint();

	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}

	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(bg, 0, 0, null);
		for (FieldElement e : elements) {
			e.paint(g2, selectedCard);
		}

	}

	public void updateLocation(MouseEvent e) {
		if (selectedCard != null) {
			selectedCard.toCanvas().setLocation(e.getX(), e.getY());
		}
	}

	public Front_Row getFrontRow1() {
		return fr1;
	}

	public Front_Row getFrontRow2() {
		return fr2;
	}

	public Front_Row getFrontRow3() {
		return fr3;
	}

	public Back_Row getBackRow1() {
		return br1;
	}

	public Back_Row getBackRow2() {
		return br2;
	}

	public Deck_Zone getDeckZone() {
		return dz;
	}

	public Level_Zone getLevelZone() {
		return lz;
	}

	public Clock_Zone getClockZone() {
		return cz;
	}

	public Waiting_Room getWaitingRoom() {
		return wr;
	}

	public Climax_Zone getClimaxZone() {
		return az;
	}

	public Random_Zone getRandomZone() {
		return rz;
	}

	public Stock_Zone getStockZone() {
		return sz;
	}

	public void prepare(Deck d) {
		dz.loadDeck(d);
	}

	public Card getSelected() {
		Card temp = selectedCard;
		selectedCard = null;
		return temp;
	}

	public void setSelected(Card card) {
		selectedCard = card;
	}
}
