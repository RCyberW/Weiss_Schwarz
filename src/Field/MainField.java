package Field;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import CardAssociation.Card;
import CardAssociation.Deck;
import Game.Player;

public class MainField extends Canvas implements Serializable, MouseListener,
   MouseMotionListener {

	private static final long serialVersionUID = -2417240192973578906L;

	BufferedImage bg;
	// Graphics2D bg2d;
	// BufferedImage bi;
	// Graphics2D big;
	int w, h;

	private Card selectedCard;
	private Card latestSelectedCard;
	private FieldElement lastSelected;

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

	public MainField(Player player) {
		// InputStream imageSrc = getClass().getResourceAsStream("/" + new
		// File("/resources/FieldImages/" + "Background.png").getPath());

		setBackground(Color.cyan);
		addMouseMotionListener(this);
		addMouseListener(this);

		associatedPlayer = player;
		selectedCard = null;

		try {
			// System.out.println(getClass().getResource(
			// "/resources/FieldImages/" + "Background.png"));
			BufferedImage playmat = ImageIO.read(getClass().getResource(
			   "/resources/FieldImages/" + "Background.png"));
			Image img = playmat.getScaledInstance(playmat.getWidth(), 600,
			   Image.SCALE_SMOOTH);

			BufferedImage before = new BufferedImage(playmat.getWidth(), 600,
			   BufferedImage.TYPE_INT_RGB);
			before.getGraphics().drawImage(img, 0, 0, null);

			AffineTransform at = new AffineTransform();
			at.scale(Game.Game.gameScale, Game.Game.gameScale);
			AffineTransformOp scaleOp = new AffineTransformOp(at,
			   AffineTransformOp.TYPE_BILINEAR);
			bg = scaleOp.filter(before, null);
			w = bg.getWidth();
			h = bg.getHeight();

		} catch (IOException e) {
			System.out.println("Image could not be read");
			System.exit(1);
		}

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				repaint();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				repaint();
			}

		});

		createElements();

	}

	private void createElements() {

		elements = new ArrayList<FieldElement>();

		fr1 = new Front_Row("Vertical.png", 350, 0, associatedPlayer);
		fr2 = new Front_Row("Vertical.png", 550, 0, associatedPlayer);
		fr3 = new Front_Row("Vertical.png", 750, 0, associatedPlayer);
		mz = new Memory_Zone("Horizontal.png", 1000, 0, associatedPlayer);
		rz = new Random_Zone("Vertical.png", 50, 0, associatedPlayer);

		sz = new Stock_Zone("Horizontal.png", 50, 180, associatedPlayer);
		az = new Climax_Zone("Horizontal.png", 250, 180, associatedPlayer);
		br1 = new Back_Row("Vertical.png", 450, 180, associatedPlayer);
		br2 = new Back_Row("Vertical.png", 650, 180, associatedPlayer);
		dz = new Deck_Zone("Vertical.png", 1050, 180, associatedPlayer);

		lz = new Level_Zone("Horizontal.png", 50, 440, associatedPlayer);
		cz = new Clock_Zone("Vertical.png", 250, 340, associatedPlayer);
		wr = new Waiting_Room("Vertical.png", 1050, 360, associatedPlayer);

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

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		if (selectedCard != null) {
			// move the card around with the mouse
			selectedCard.toCanvas().setLocation(e.getPoint());
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getClickCount() > 1) {
			return;
		}

		if (e.getButton() == MouseEvent.BUTTON1) {

			// if (selectedCard == null) {
			// // picking up a card from a zone
			// for (FieldElement fe : elements) {
			// // fe.mouseReleased(e);
			// if (!fe.isList() && fe.contains(e.getPoint())) {
			// selectedCard = fe.selectCard(e);
			// lastSelected = fe;
			// }
			// fe.mouseReleased(e);
			// System.out.println(selectedCard);
			// if (selectedCard != null) {
			// System.out.println("taking up  " + selectedCard.getCardName());
			// break;
			// }
			// }
			// } else {
			// // placing a card to a zone
			// for (FieldElement fe : elements) {
			// if (fe.contains(e.getPoint())) {
			// System.out.println("clicked on " + fe.toString() + "(" + e.getX()
			// + "," + e.getY() + ") placing " + selectedCard.getCardName());
			// Card tempCard = null;
			// if (!fe.isList()) {
			// tempCard = fe.selectCard(e);
			// lastSelected = fe;
			// if (tempCard != null)
			// System.out.println("picking up " + selectedCard.getCardName());
			// }
			// fe.setCard(selectedCard);
			// selectedCard = tempCard;
			// break;
			// }
			// }
			// if (selectedCard != null)
			// System.out.println("holding " + selectedCard.getCardName());
			// }

			if (lastSelected == null) {
				// choose the last selected zone
				for (FieldElement fe : elements) {
					if (fe.contains(e.getPoint())) {
						fe.mouseReleased(e);
						lastSelected = fe;
						System.out.println(lastSelected.toString());
						repaintElements();
						break;
					}
				}
			} else {
				System.out.println(lastSelected.toString());
				if (lastSelected.toString().equals("Front-Row")
				   || lastSelected.toString().equals("Back-Row")) {
					// swap
					System.out.println("SWAPPING PREP...");
					for (FieldElement fe : elements) {
						if (fe.contains(e.getPoint())
						   && (fe.toString().equals("Front-Row") || fe.toString()
						      .equals("Back-Row"))) {
							// || fe.toString().equals("Resolution Area"))) {
							fe.mouseReleased(e);
							Card card1 = fe.showCard();
							Card card2 = lastSelected.showCard();

							CardAssociation.State card1State = card1 != null ? card1
							   .getCurrentState() : null;
							CardAssociation.State card2State = card2 != null ? card2
							   .getCurrentState() : null;

							fe.setCard(card2);
							if (card2 != null)
								card2.setCurrentState(card2State);

							lastSelected.setCard(card1);
							if (card1 != null)
								card1.setCurrentState(card1State);

							repaintElements();
							break;
						}
					}
					lastSelected = null;
					System.out.println("SWAPPING DONE");
				} else if (lastSelected.toString().equals("Resolution Area")) {
					for (FieldElement fe : elements) {
						if (fe.contains(e.getPoint())
						   && (fe.toString().equals("Front-Row") || fe.toString()
						      .equals("Back-Row"))) {
							fe.mouseReleased(e);
							Card card1 = fe.showCard();
							Card card2 = lastSelected.showCard();
							if (card2 != null) {
								fe.setCard(card2);
								if (card1 != null)
									getWaitingRoom().setCard(card1);
							}
							Random_Zone randomSelect = (Random_Zone) lastSelected;
							randomSelect.removeCard();
							lastSelected = null;
							repaintElements();
							break;
						}
					}
				} else {
					for (FieldElement fe : elements) {
						if (fe.contains(e.getPoint())) {
							fe.mouseReleased(e);
							lastSelected = fe;
							repaintElements();
							break;
						}
					}
				}
			}

		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (selectedCard != null) {
				lastSelected.setCard(selectedCard);
			} else {

				// default action on each zone
				for (FieldElement fe : elements) {
					if (fe.contains(e.getPoint())) {
						fe.mouseReleased(e);
					}
				}
				lastSelected = null;
			}
		}

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
			/*
			 * if (lastSelected != null &&
			 * e.toString().equals(lastSelected.toString())) {
			 * 
			 * Card card = lastSelected.showCard();
			 * 
			 * Color curr = g2.getColor(); g2.setColor(Color.RED); Stroke oldStroke
			 * = g2.getStroke(); g2.setStroke(new BasicStroke(3));
			 * g2.drawRect((int)card.getCardBound().getX(),
			 * (int)card.getCardBound() .getY(),
			 * (int)card.getCardBound().getWidth(),
			 * (int)card.getCardBound().getHeight()); g2.setStroke(oldStroke);
			 * g2.setColor(curr); }
			 */
			e.paint(g2, selectedCard);
		}

		if (latestSelectedCard != null) {
			Color curr = g2.getColor();
			g2.setColor(Color.RED);
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(3));
			g2.drawRect((int) latestSelectedCard.getCardBound().getX(),
			   (int) latestSelectedCard.getCardBound().getY(),
			   (int) latestSelectedCard.getCardBound().getWidth(),
			   (int) latestSelectedCard.getCardBound().getHeight());
			g2.setStroke(oldStroke);
			g2.setColor(curr);
		}

		if (selectedCard != null) {
			// selectedCard.toCanvas().paint(g2);
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

	public Memory_Zone getMemoryZone() {
		return mz;
	}

	public boolean prepare(Deck d) {
		dz.loadDeck(d);
		return dz.getCount() == Deck.MAX_DECK_SIZE;
	}

	public Card getSelected() {
		Card temp = latestSelectedCard;

		if (temp == null) {
			System.out.println("MAINFIELD: no available card to display::" + temp);
		} else {
			System.out.println("MAINFIELD: displaying card info::" + temp);
		}

		// selectedCard = null;
		return temp;
	}

	public void setSelected(Card card) {
		latestSelectedCard = card;
	}

	public void repaintElements() {
		repaint();

		associatedPlayer.f.toFront();
		associatedPlayer.f.repaint();
	}
}
