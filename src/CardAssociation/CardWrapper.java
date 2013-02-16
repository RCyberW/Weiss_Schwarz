package CardAssociation;

import java.util.ArrayList;

public class CardWrapper {

	ArrayList<Card> similarCards;
	protected String cardName;
	protected int dupCount = 0;

	CardWrapper() {
		similarCards = new ArrayList<Card>();
	}

	public void resetCount() {
		dupCount = 0;
	}

	// used in Deck.java to decrement the number of copies of the card
	public void removeCount() {
		dupCount--;
	}

	// used in Deck.java to check how many copies of the card is there
	public int getCardCount() {
		return dupCount;
	}

	// used in Deck.java to increment the number of copies of the card
	public void addCount() {
		dupCount++;
	}

	public void setCount(int dupCount) {
		this.dupCount = dupCount;
	}

	// set the card name of the card
	public void setCardName(String name) {
		this.cardName = name;
	}

	// get the card name of the card
	public String getCardName() {
		return cardName;
	}

	public boolean addCard(Card c) {
		if (cardName.equals(c.getCardName())) {
			if (dupCount < Card.getMaxInDeck(c)) {
				dupCount++;
				similarCards.add(c.clone());
				return true;
			}			
		}
		return false;
	}
	
	public ArrayList<Card> getCards() {
		return similarCards;
	}

	public boolean containsCard(Card c) {
		if (cardName.equals(c.getCardName()))
			return true;
		else
			return false;
	}

}
