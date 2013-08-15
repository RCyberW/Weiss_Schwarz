package Field;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
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
	
	public static boolean debug = false;
	private static final long serialVersionUID = -2417240192973578906L;

	private BufferedImage dplaymat;
	private BufferedImage aplaymat;
	// Graphics2D bg2d;
	// BufferedImage bi;
	// Graphics2D big;
	private int w, h;

	private Card selectedCard;
	private Card latestSelectedCard;
	private FieldElement lastSelected;
	private Player associatedPlayer;

	// Field Elements
	private ArrayList<FieldElement> elements;
	// Defender's Side
	private Clock_Zone dcz;
	private Stock_Zone dsz;
	private Front_Row dfr1;
	private Front_Row dfr2;
	private Front_Row dfr3;
	private Back_Row dbr1;
	private Back_Row dbr2;
	private Waiting_Room dwr;
	private Deck_Zone ddz;
	private Memory_Zone dmz;
	private Level_Zone dlz;
	private Climax_Zone daz;
	private Random_Zone drz;

	// Attacker's Side
	private Clock_Zone acz;
	private Stock_Zone asz;
	private Front_Row afr1;
	private Front_Row afr2;
	private Front_Row afr3;
	private Back_Row abr1;
	private Back_Row abr2;
	private Waiting_Room awr;
	private Deck_Zone adz;
	private Memory_Zone amz;
	private Level_Zone alz;
	private Climax_Zone aaz;
	private Random_Zone arz;

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
			dplaymat = scaleOp.filter(before, null);
			aplaymat = scaleOp.filter(before, null);
			w = (int) (dplaymat.getWidth() / Game.Game.gameScale);
			h = (int) (dplaymat.getHeight() / Game.Game.gameScale);

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

		createElements(h);

	}

	private void createElements(int offset) {

		elements = new ArrayList<FieldElement>();

		dfr1 = new Front_Row("Vertical.png", 350, 0, associatedPlayer, offset);
		dfr2 = new Front_Row("Vertical.png", 550, 0, associatedPlayer, offset);
		dfr3 = new Front_Row("Vertical.png", 750, 0, associatedPlayer, offset);
		dmz = new Memory_Zone("Horizontal.png", 1000, 0, associatedPlayer, offset);
		drz = new Random_Zone("Vertical.png", 50, 0, associatedPlayer, offset);

		dsz = new Stock_Zone("Horizontal.png", 50, 180, associatedPlayer, offset);
		daz = new Climax_Zone("Horizontal.png", 250, 180, associatedPlayer,
		   offset);
		dbr1 = new Back_Row("Vertical.png", 450, 180, associatedPlayer, offset);
		dbr2 = new Back_Row("Vertical.png", 650, 180, associatedPlayer, offset);
		ddz = new Deck_Zone("Vertical.png", 1050, 180, associatedPlayer, offset);

		dlz = new Level_Zone("Horizontal.png", 50, 440, associatedPlayer, offset);
		dcz = new Clock_Zone("Vertical.png", 250, 400, associatedPlayer, offset);
		dwr = new Waiting_Room("Vertical.png", 1050, 400, associatedPlayer,
		   offset);

		elements.add(dcz);
		elements.add(dsz);
		elements.add(dfr1);
		elements.add(dfr2);
		elements.add(dfr3);
		elements.add(dwr);
		elements.add(ddz);
		elements.add(dbr1);
		elements.add(dbr2);
		elements.add(dmz);
		elements.add(dlz);
		elements.add(daz);
		elements.add(drz);

		offset = 0;

		afr1 = new Front_Row("Vertical.png", 350, 440, associatedPlayer, offset);
		afr2 = new Front_Row("Vertical.png", 550, 440, associatedPlayer, offset);
		afr3 = new Front_Row("Vertical.png", 750, 440, associatedPlayer, offset);
		amz = new Memory_Zone("Horizontal.png", 50, 440, associatedPlayer,
		   offset);
		arz = new Random_Zone("Vertical.png", 1040, 440, associatedPlayer, offset);

		asz = new Stock_Zone("Horizontal.png", 1000, 260, associatedPlayer, offset);
		aaz = new Climax_Zone("Horizontal.png", 800, 260, associatedPlayer,
		   offset);
		abr1 = new Back_Row("Vertical.png", 450, 260, associatedPlayer, offset);
		abr2 = new Back_Row("Vertical.png", 650, 260, associatedPlayer, offset);
		adz = new Deck_Zone("Vertical.png", 50, 260, associatedPlayer, offset);

		alz = new Level_Zone("Horizontal.png", 1000, 0, associatedPlayer, offset);
		acz = new Clock_Zone("Vertical.png", 800, 0, associatedPlayer, offset);
		awr = new Waiting_Room("Vertical.png", 50, 0, associatedPlayer, offset);

		elements.add(acz);
		elements.add(asz);
		elements.add(afr1);
		elements.add(afr2);
		elements.add(afr3);
		elements.add(awr);
		elements.add(adz);
		elements.add(abr1);
		elements.add(abr2);
		elements.add(amz);
		elements.add(alz);
		elements.add(aaz);
		elements.add(arz);
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
									getDefenderWaitingRoom().setCard(card1);
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
		g2.drawImage(aplaymat, 0, 0, null);
		g2.drawImage(dplaymat, 0, (int) (h * Game.Game.gameScale), null);
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

	// Get defender field elements
	public Front_Row getDefenderFrontRow1() {
		return dfr1;
	}

	public Front_Row getDefenderFrontRow2() {
		return dfr2;
	}

	public Front_Row getDefenderFrontRow3() {
		return dfr3;
	}

	public Back_Row getDefenderBackRow1() {
		return dbr1;
	}

	public Back_Row getDefenderBackRow2() {
		return dbr2;
	}

	public Deck_Zone getDefenderDeckZone() {
		return ddz;
	}

	public Level_Zone getDefenderLevelZone() {
		return dlz;
	}

	public Clock_Zone getDefenderClockZone() {
		return dcz;
	}

	public Waiting_Room getDefenderWaitingRoom() {
		return dwr;
	}

	public Climax_Zone getDefenderClimaxZone() {
		return daz;
	}

	public Random_Zone getDefenderRandomZone() {
		return drz;
	}

	public Stock_Zone getDefenderStockZone() {
		return dsz;
	}

	public Memory_Zone getDefenderMemoryZone() {
		return dmz;
	}

	// Set and get attacker field elements
	public Front_Row getAttackerFrontRow1() {
		return afr1;
	}

	public Front_Row getAttackerFrontRow2() {
		return afr2;
	}

	public Front_Row getAttackerFrontRow3() {
		return afr3;
	}

	public Back_Row getAttackerBackRow1() {
		return abr1;
	}

	public Back_Row getAttackerBackRow2() {
		return abr2;
	}

	public Deck_Zone getAttackerDeckZone() {
		return adz;
	}

	public Level_Zone getAttackerLevelZone() {
		return alz;
	}

	public Clock_Zone getAttackerClockZone() {
		return acz;
	}

	public Waiting_Room getAttackerWaitingRoom() {
		return awr;
	}

	public Climax_Zone getAttackerClimaxZone() {
		return aaz;
	}

	public Random_Zone getAttackerRandomZone() {
		return arz;
	}

	public Stock_Zone getAttackerStockZone() {
		return asz;
	}

	public Memory_Zone getAttackerMemoryZone() {
		return amz;
	}
	
	public boolean prepare(Deck d) {
		ddz.loadDeck(d);
		return ddz.getCount() == Deck.MAX_DECK_SIZE;
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
