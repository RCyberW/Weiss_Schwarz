package DeckBuilder;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import CardAssociation.Card;
import CardAssociation.Deck;

public class DropHandler extends TransferHandler implements Transferable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8664897212110157452L;

	private final DataFlavor flavors[] = { DataFlavor.imageFlavor };

	private Card thisCard;
	private Deck currentDeck;

	public DropHandler(Deck cDeck) {
		currentDeck = cDeck;
	}

	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}

	public boolean canImport(JComponent comp, DataFlavor flavor[]) {
		if (!(comp instanceof JLabel)) {
			return false;
		}
		for (int i = 0, n = flavor.length; i < n; i++) {
			for (int j = 0, m = flavors.length; j < m; j++) {
				if (flavor[i].equals(flavors[j])) {
					return true;
				}
			}
		}
		return false;
	}

	public Transferable createTransferable(JComponent comp) {
		return null;
	}

	public boolean importData(JComponent comp, Transferable t) {
		System.out.println("IMPORTING " + comp.getName());
		if (comp instanceof JLabel) {
			//Card label = (Card) comp;
			//System.out.println(label.getName());
			if (t.isDataFlavorSupported(flavors[0])) {
				try {

					Card cv2 = (Card) t.getTransferData(flavors[0]);
					//cv2.clone(label);
					/*label.setCardName(cv2.getCardName());
					label.setName(cv2.getName());

					System.out.println(cv2.getName() + ":" + cv2.getCardName());

					label.setImage(cv2.getImage());*/

					if(currentDeck != null) {
						currentDeck.addCard(cv2,false);
					}

					return true;
				} catch (UnsupportedFlavorException ignored) {
				} catch (IOException ignored) {
				}
			}
		}
		return false;
	}

	// Transferable
	public Object getTransferData(DataFlavor flavor) {
		if (isDataFlavorSupported(flavor)) {
			return thisCard;
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavors[0].equals(flavor);
	}

}
