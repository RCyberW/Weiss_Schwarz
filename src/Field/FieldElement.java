package Field;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import CardAssociation.Card;
import Game.Player;

abstract class FieldElement extends Component implements MouseListener {

	/**
	 * Click instructions: MISSING - check top - check top place top/bottom -
	 * check top place waiting room - brainstorm - canceling
	 */
	/*
	 * CLOCK PHASE Right Click Back N/A Climax N/A Clock N/A Deck N/A Front N/A
	 * Hand Select the card to clock DONE Level N/A Memory N/A Resolution N/A
	 * Stock N/A Waiting Room N/A Left Click Back Check card Climax N/A Clock
	 * Check card Deck N/A Front Check card Hand N/A Level Check card Memory
	 * Check card Resolution N/A Stock N/A Waiting Room Check card
	 */
	/*
	 * MAIN PHASE Right Click Back Rest card for effect DONE Climax N/A Clock If
	 * there is a card selected from hand, shift Deck Draw a card Front Rest
	 * card for effect DONE Hand Play a card to the next available field spot
	 * Level N/A Memory N/A Resolution N/A Stock Pay for cost from top Waiting
	 * Room N/A Left Click Back Swap card with another zone Climax N/A Clock
	 * Search clock (able to select card or just look through waiting room) Deck
	 * Search deck (will warn opponent and shuffle) Front Swap card with another
	 * zone Hand N/A Level Search level (able to select card or just look
	 * through waiting room) Memory N/A Resolution N/A Stock Pay for cost from
	 * bottom Waiting Room Search waiting room (able to select card or just look
	 * through waiting room)
	 */
	/*
	 * CLIMAX PHASE Right Click Back Rest card for effect Climax N/A Clock N/A
	 * Deck N/A Front Rest card for effect Hand Play a climax to the climax zone
	 * Level N/A Memory N/A Resolution N/A Stock N/A Waiting Room N/A Left Click
	 * Back Check card Climax Check card Clock Check card Deck N/A Front Check
	 * card Hand N/A Level Check card Memory Check card Resolution Check card
	 * Stock N/A Waiting Room Check card
	 */
	/*
	 * BATTLE PHASE Right Click Back Toggle between modes Rest > Reverse > Stand
	 * Climax Remove climax and move to waiting room Clock N/A Deck Check
	 * trigger/Clock damage Front Toggle between modes Rest > Reverse > Stand
	 * Hand Play a card to the resolution(event) or waiting room(character)
	 * Level N/A Memory N/A Resolution N/A Stock Pay for cost from top Waiting
	 * Room N/A Left Click Back Swap card with another zone Climax N/A Clock
	 * Search clock (able to select card or just look through waiting room) Deck
	 * Search deck (will warn opponent and shuffle) Front Swap card with another
	 * zone Hand N/A Level Search level (able to select card or just look
	 * through waiting room) Memory N/A Resolution N/A Stock Pay for cost from
	 * bottom Waiting Room Search waiting room (able to select card or just look
	 * through waiting room)
	 */
	/*
	 * END PHASE Right Click Back N/A Climax N/A Clock N/A Deck N/A Front N/A
	 * Hand Drop a card to the waiting room Level N/A Memory N/A Resolution N/A
	 * Stock Pay for cost from top Waiting Room N/A Left Click Back Swap card
	 * with another zone Climax N/A Clock Search clock (able to select card or
	 * just look through waiting room) Deck Search deck (will warn opponent and
	 * shuffle) Front Swap card with another zone Hand N/A Level Search level
	 * (able to select card or just look through waiting room) Memory N/A
	 * Resolution N/A Stock Pay for cost from bottom Waiting Room Search waiting
	 * room (able to select card or just look through waiting room)
	 */
	private static final long serialVersionUID = 8801519680358307435L;

	protected BufferedImage bi;
	protected Rectangle rect;
	protected int w, h, x, y; // x, y coordinates of the displayed image and
								// height and width of the image
	public String zoneName;
	protected Player associatedPlayer;

	/**
	 * A basic FieldElement constructor
	 * 
	 * @param imageFileName
	 *            relative URL of the background image
	 * @param xa
	 *            x-axis coordinate on the field
	 * @param ya
	 *            y-axis coordinate on the field
	 */
	public FieldElement(String imageFileName, int xa, int ya, String zone,
			Player player) {
		// InputStream imageSrc =
		// getClass().getResource("/resources/FieldImages/" + imageFileName);
		addMouseListener(this);
		setAssociatedPlayer(player);
		zoneName = zone;

		try {
			System.out.println(zone
					+ "   "
					+ getClass().getResource(
							"/resources/FieldImages/" + imageFileName));
			BufferedImage before = ImageIO.read(getClass().getResource(
					"/resources/FieldImages/" + imageFileName));

			x = (int) (xa * Game.Game.gameScale);
			y = (int) ((ya + Game.Game.translatedY) * Game.Game.gameScale);

			int wid = before.getWidth();
			int hit = before.getHeight();
			bi = new BufferedImage(wid, hit, BufferedImage.TYPE_INT_ARGB);

			AffineTransform at = new AffineTransform();
			at.scale(Game.Game.gameScale, Game.Game.gameScale);
			AffineTransformOp scaleOp = new AffineTransformOp(at,
					AffineTransformOp.TYPE_BILINEAR);
			bi = scaleOp.filter(before, null);

			w = (int) (bi.getWidth(null));
			h = (int) (bi.getHeight(null));
			/*
			 * rect = new Rectangle((int) (x), (int) (y +
			 * Game.Game.translatedY), (int) (w * Game.Game.gameScale), (int) (h
			 * * Game.Game.gameScale));
			 */
			rect = new Rectangle((int) (x), (int) (y), (int) (w), (int) (h));
		} catch (IOException e) {
			System.out.println("Image could not be read field");
			System.exit(1);
		}
	}

	/**
	 * @return the Dimension of the FieldElement
	 */
	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}

	/**
	 * * check if the zone contains cards
	 * 
	 * @return
	 */
	public abstract boolean containCards();

	public boolean contains(int x, int y) {
		return rect.contains(x, y);
	}

	/**
	 * @param g
	 *            draw the FieldElement
	 */
	/*
	 * public void paint(Graphics g, Card topCard) {
	 * 
	 * // System.out.println(zoneName + " contains card = " + containCards());
	 * 
	 * if (containCards()) { paint(g, topCard); } else { g.drawImage(bi, x, y,
	 * null); } g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
	 * g.setColor(Color.BLUE);
	 * 
	 * g.drawString(zoneName, x + 10, y + 20); }
	 */

	public void paintOption(Graphics g) {
		System.out.println("Climax drawing option");
		g.drawString("option 1", x, y - 10);
		g.setColor(Color.LIGHT_GRAY);
		// g.drawRect(this.x, y - 10, 150, 20);
		g.fillRect(x, y - 10, 150, 20);
		g.setColor(Color.BLUE);
	}

	/**
	 * draw the cards in the zone
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public abstract void paint(Graphics g, Card c);

	// public abstract void addCard(Card c);
	//
	// public abstract ArrayList<Card> showCards();
	//
	// public abstract Card removeCard(Card c);

	/*
	 * public boolean snapToZone(Rectangle r) {
	 * 
	 * if (r.intersects(rect)) { r.setLocation(x, y); return true; }
	 * 
	 * return false; }
	 */

	public boolean snapToZone(Card c) {

		if (c.getCardBound().intersects(rect)) {
			c.getCardBound().setLocation(x, y);
			System.out.println("Intersect " + zoneName);
			return true;
		}

		return false;
	}

	@Override
	public abstract void mouseReleased(MouseEvent e);

	@Override
	public void mouseEntered(MouseEvent e) {
		if (rect.contains(e.getX(), e.getY()))
			System.out.println("FieldElement mouseEntered " + zoneName);
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	public abstract Card selectCard(MouseEvent e);

	public abstract void setCard(Card selectedCard);

	public abstract Card showCard();

	public String toString() {
		if (rect != null)
			System.out.println("hitbox (" + rect.x + " ~ " + rect.getWidth()
					+ ", " + rect.y + " ~ " + rect.getHeight() + ")");
		return zoneName;
	}

	public void setAssociatedPlayer(Player associatedPlayer) {
		this.associatedPlayer = associatedPlayer;
	}

	public Player getAssociatedPlayer() {
		return associatedPlayer;
	}

	public boolean isList() {
		return false;
	}
}
