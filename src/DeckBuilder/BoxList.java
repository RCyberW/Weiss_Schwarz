package DeckBuilder;

import java.util.ArrayList;

import javax.swing.Box;

import CardAssociation.Card;

public class BoxList extends Box{

	public BoxList(int axis) {
		super(axis);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 38093076853990442L;
	
	
	public void updateList(ArrayList<Card> list) {
		
		
		
		super.repaint();
	}

}
