/**
 * @file Deck.java
 * @author Jia Chen
 * @date Sept 05, 2011
 * @description 
 * 		Deck.java is an object representation of the deck used in
 * 		this application.
 */

package CardAssociation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Deck {

	// deck properties
	private String deckName;
	final int MAX_DECK_SIZE = 50;
	// private int cardsRemain = 0;

	// deck statistics
	private int numLv3;
	private int numLv2;
	private int numLv1;
	private int numLv0;
	private int numClimax;
	private int numEvent;
	private int numSoulTrigger;

	private int numYellow;
	private int numGreen;
	private int numRed;
	private int numBlue;

	private int dmgCount;

	private ArrayList<Card> cards;
	private ArrayList<Card> unique;
	private ArrayList<CardWrapper> cardWrapper;
	private JFrame frame;

	public ArrayList<Card> shuffledCards;

	// create a deck
	public Deck() {
		setDeckName("Untitled");
		cards = new ArrayList<Card>();
		unique = new ArrayList<Card>();
		shuffledCards = new ArrayList<Card>();
		cardWrapper = new ArrayList<CardWrapper>();
		frame = new JFrame();
	}

	// add cards to a deck
		public boolean addCard(Card referenceCard, boolean verbose) {
			System.out.println("adding in Deck");

			boolean toAdd = true;
			int x = 0;
			for (; x < cardWrapper.size(); x++) {
				if (cardWrapper.get(x).containsCard(referenceCard)) {
					toAdd = cardWrapper.get(x).addCard(referenceCard);
					break;
				}
			}

			if (x == cardWrapper.size()) {
				CardWrapper newWrapper = new CardWrapper();
				newWrapper.setCardName(referenceCard.getCardName());
				cardWrapper.add(newWrapper);
				toAdd = cardWrapper.get(x).addCard(referenceCard);
			}

			// check if the deck has max number of cards
			if (cards.size() < MAX_DECK_SIZE) {
				// if not, check to see if there are 4 copies of a card
				if (cards.contains(referenceCard)) {
					// if not, then add
					// System.err.println(card.getName());
					if (referenceCard.getCardCount() < Card.getMaxInDeck(referenceCard) 
							&& toAdd) {
						referenceCard.addCount();
						for (int i = 0; i < cards.size(); i++) {
							Card tempCard = cards.get(i);
							if (referenceCard.compareTo(tempCard) == 0) {
								tempCard.setCount(referenceCard.getCardCount());
							}
						}

						Card card = referenceCard.clone();
						card.setCardName(card.getCardName()
								+ referenceCard.getCardCount());
						onlineUpdateStatistics(card, true);
						cards.add(card);
						shuffledCards.add(card);
						// System.err.println(card.toString());
					} else if (verbose){
						System.out.println(referenceCard.getCardName()
								+ " has maximum copies");
						// Warn the user that there are 4 copies existing
						JOptionPane.showMessageDialog(
								frame,
								"There are already the maximum copies (" + 
										Card.getMaxInDeck(referenceCard) +") of "
										+ referenceCard.getCardName() + " in the deck",
										"Max Copies", JOptionPane.WARNING_MESSAGE);
					}
				} else if (toAdd) {
					// card does not exist, add
					referenceCard.resetCount();
					referenceCard.addCount();
					Card card = referenceCard.clone();
					onlineUpdateStatistics(card, true);
					cards.add(card);
					shuffledCards.add(card);
					unique.add(referenceCard);
					Collections.sort(unique);
				} else if (verbose) {
					System.out.println(referenceCard.getCardName()
							+ " has maximum copies");
					// Warn the user that there are 4 copies existing
					JOptionPane.showMessageDialog(
							frame,
							"There are already the maximum copies (" + 
									Card.getMaxInDeck(referenceCard) +") of "
									+ referenceCard.getCardName() + " in the deck",
									"Max Copies", JOptionPane.WARNING_MESSAGE);
				}
			} else if (verbose) {
				System.out.println("FULL DECK");
				// Warn the user that it is a full deck
				JOptionPane.showMessageDialog(frame,
						"This deck already contain 50 cards!", "Full Deck",
						JOptionPane.WARNING_MESSAGE);
			}
			return toAdd;
		}


	// remove a card form the deck
	public boolean removeCard(Card card) {
		// check to see if the card exists in the deck
		if (cards.contains(card)) {

			int x = 0;
			for (; x < cardWrapper.size(); x++) {
				if (cardWrapper.get(x).containsCard(card)) {
					cardWrapper.get(x).removeCount();
				}
			}

			onlineUpdateStatistics(card, false);
			cards.remove(card);
			card.removeCount();
			for (int i = 0; i < cards.size(); i++) {
				Card tempCard = cards.get(i);
				if (card.compareTo(tempCard) == 0) {
					tempCard.setCount(card.getCardCount());
				}
				if (i < unique.size()) {
					if (unique.get(i).compareTo(card) == 0) {
						unique.get(i).setCount(card.getCardCount());
					}
				}
			}
			System.out.println(card.getCardName() + " has "
					+ card.getCardCount() + " copies");
			if (card.getCardCount() == 0) {
				unique.remove(card);
			}
			return true;
		}
		return false;
	}

	// get the deck list
	public ArrayList<Card> getCards() {
		return cards;
	}

	public ArrayList<Card> getUnique() {
		return unique;
	}

	// public void setCardsRemain(int cardsRemain) {
	// this.cardsRemain = cardsRemain;
	// }
	//
	// public int getCardsRemain() {
	// return cardsRemain;
	// }

	// set the deck name
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}

	// get the deck name
	public String getDeckName() {
		return deckName;
	}

	// save the deck
	public void save(File file) {
		try {
			FileOutputStream fileOutput;
			ObjectOutputStream objectOutput;
			fileOutput = new FileOutputStream(file.getAbsolutePath());
			objectOutput = new ObjectOutputStream(fileOutput);

			objectOutput.writeObject(cards);
			objectOutput.writeObject(shuffledCards);

			objectOutput.close();

			// FileWriter fstream = new FileWriter(file.getParent() + "/"
			// + file.getName());
			// BufferedWriter out = new BufferedWriter(fstream);
			// for (int i = 0; i < cards.size(); i++) {
			// Card c = cards.get(i);
			// out.write(c.toString() + "\n");
			// }
			// out.close();
		} catch (IOException e) {
			System.out.println("File does not exist");
		}
	}

	public void saveRaw(File file) {
		try {
			OutputStreamWriter fw = new OutputStreamWriter(
					new FileOutputStream(file.getAbsolutePath()), "UTF-8");
			// FileWriter fw = new FileWriter(file.getAbsolutePath());
			for (Card c : unique) {
				for (int i = 0; i < c.getCardCount(); ++i)
					fw.write(c.getID() + "\n");
			}
			fw.close();
		} catch (IOException e) {
			System.out.println("File does not exist");
		}
	}

	public void loadRaw(File file, HashMap<String, Card> dictionary) {
		try {
			InputStreamReader fr = new InputStreamReader(new FileInputStream(
					file.getAbsolutePath()), "UTF-8");
			// FileReader fr = new FileReader(file.getAbsolutePath());
			Scanner s = new Scanner(fr);

			java.util.Iterator<Card> valueList = dictionary.values().iterator();

			while (s.hasNextLine()) {
				String line = s.nextLine();

				// line = line.replace(" ", "");
				String pID = line.charAt(0) + "";

				for (int i = 1; i < line.length(); i++) {
					if ((Character.isLetter(line.charAt(i)) && Character
							.isDigit(line.charAt(i - 1)))
							|| (Character.isSpaceChar(line.charAt(i)) && Character
									.isDigit(line.charAt(i - 1)))) {
						break;
					} else {
						pID += line.charAt(i);
					}
				}

				System.out.println(pID);
				Card c = dictionary.get(line);
				if (c == null) {
					while (valueList.hasNext()) {
						Card temp = valueList.next();
						if (temp.meetsRequirement(pID, "", null, null, -1, -1,
								null, -1, -1, "", "")) {
							c = temp;
						}
					}
				}

				if (c != null)
					addCard(c, false);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// load the deck
	@SuppressWarnings("unchecked")
	public void load(File file, HashMap<String, Card> dictionary) {

		FileInputStream fileInput;
		ObjectInputStream objectInput;

		try {
			fileInput = new FileInputStream(file.getAbsolutePath());
			objectInput = new ObjectInputStream(fileInput);
			cards = (ArrayList<Card>) objectInput.readObject();
			offlineUpdateStatistics();
			shuffledCards = (ArrayList<Card>) objectInput.readObject();
			System.out.println("card count: " + shuffledCards.size());
			objectInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// try {
		// Scanner scan = new Scanner(file);
		// cards.clear();
		// shuffledCards.clear();
		// while (scan.hasNext()) {
		// Scanner lineScan = new Scanner(scan.nextLine())
		// .useDelimiter(":");
		// String id = lineScan.next();
		// String name = lineScan.next();
		// addCard(dictionary.get(id));
		// System.out.println("card count: " + shuffledCards.size());
		// }
		// } catch (FileNotFoundException e) {
		// System.out.println("File does not exist");
		// }
	}

	// get a shuffled deck for playing
	public ArrayList<Card> getPlayingDeck() {
		Collections.shuffle(shuffledCards);
		return shuffledCards;
	}

	// Update stats
	private void onlineUpdateStatistics(Card c, boolean inc) {
		int offset;
		if (inc)
			offset = 1;
		else
			offset = -1;
		switch (c.getLevel()) {
		case 3:
			numLv3 += offset;
			break;
		case 2:
			numLv2 += offset;
			break;
		case 1:
			numLv1 += offset;
			break;
		case 0:
			if (c.getT() != Type.CLIMAX)
				numLv0 += offset;
			break;
		default:
		}

		switch (c.getC()) {
		case YELLOW:
			numYellow += offset;
			break;
		case GREEN:
			numGreen += offset;
			break;
		case RED:
			numRed += offset;
			break;
		case BLUE:
			numBlue += offset;
			break;
		default:
		}

		switch (c.getT()) {
		case CLIMAX:
			numClimax += offset;
			break;
		case EVENT:
			numEvent += offset;
			break;
		default:
		}

		switch (c.getTrigger()) {
		case DUALSOUL:
			numSoulTrigger += offset;
		case SOUL:
		case SOULWIND:
		case SOULFLAME:
			numSoulTrigger += offset;
			break;
		default:
		}

		dmgCount += c.getSoul();
	}

	private void offlineUpdateStatistics() {
		numLv3 = 0;
		numLv2 = 0;
		numLv1 = 0;
		numLv0 = 0;
		numClimax = 0;
		numEvent = 0;
		numSoulTrigger = 0;
		dmgCount = 0;
		for (Card c : cards) {
			onlineUpdateStatistics(c, true);
		}
	}

	// Statistics methods
	public int getNumCards() {
		return cards.size();
	}

	public int getNumLevel3() {
		return numLv3;
	}

	public int getNumLevel2() {
		return numLv2;
	}

	public int getNumLevel1() {
		return numLv1;
	}

	public int getNumLevel0() {
		return numLv0;
	}

	public int getNumClimax() {
		return numClimax;
	}

	public int getNumEvent() {
		return numEvent;
	}

	public int getNumSoul() {
		return numSoulTrigger;
	}

	public int getNumYellow() {
		return numYellow;
	}

	public int getNumGreen() {
		return numGreen;
	}

	public int getNumRed() {
		return numRed;
	}

	public int getNumBlue() {
		return numBlue;
	}

	public int getDamage() {
		return dmgCount;
	}

	public void printStatistics() {
		System.out.println("# cards:" + getNumCards());
		System.out.println("# LV3:" + getNumLevel3());
		System.out.println("# LV2:" + getNumLevel2());
		System.out.println("# LV1:" + getNumLevel1());
		System.out.println("# LV0:" + getNumLevel0());
		System.out.println("# Climax:" + getNumClimax());
		System.out.println("# Event:" + getNumClimax());
		System.out.println("# Soul Triggers:" + getNumSoul());
	}
}
