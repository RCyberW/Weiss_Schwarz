package DeckBuilder;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class LinkedTableRowSorter<M extends TableModel> extends TableRowSorter<M> {
	private BuilderGUI gui;
	public LinkedTableRowSorter(M model, BuilderGUI gui) {
		super(model);
		this.gui = gui;
	}
	
	/*
	@Override
	public void sort() {
		super.sort();
	}
	*/
	
	@Override
	public void toggleSortOrder(int column) {
		super.toggleSortOrder(column);
		gui.refresh("deckThumbs");
	}
}
