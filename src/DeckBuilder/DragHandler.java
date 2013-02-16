package DeckBuilder;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import CardAssociation.Card;

public class DragHandler extends TransferHandler implements Transferable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8664897212110157452L;

	private final DataFlavor flavors[] = { DataFlavor.imageFlavor };

	private Card thisCard;

	public DragHandler(Card cv2) {
		thisCard = cv2;
	}

	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}

	public boolean canImport(JComponent comp, DataFlavor flavor[]) {
		return false;
	}

	public Transferable createTransferable(JComponent comp) {

		if (comp instanceof JLabel) {
			JLabel label = (JLabel) comp;
			Icon icon = label.getIcon();
			if (icon instanceof ImageIcon) {
				return this;
			}
		}
		return null;
	}

	public boolean importData(JComponent comp, Transferable t) {
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
