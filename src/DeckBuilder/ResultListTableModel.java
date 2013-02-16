package DeckBuilder;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import CardAssociation.Card;

public class ResultListTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 3975544447965529587L;
	
	private String[] columnNames = { "ID", "Name", "Color", "Type", "Level",
			"Cost", "Soul", "Power" };	
	private ArrayList<Card> cardlist;
	
	public ResultListTableModel() {
	}
	
	public ResultListTableModel(ArrayList<Card> cardlist) {
		this.cardlist = cardlist;
	}
	
	public void setCardList(ArrayList<Card> cardlist) {
		this.cardlist = cardlist;
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return cardlist.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Card c = cardlist.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return c.getID();
		case 1:
			return c.getCardName();
		case 2:
			return c.getC();
		case 3:
			return c.getT();
		case 4:
			return c.getLevel();
		case 5:
			return c.getCost();
		case 6:
			return c.getSoul();
		case 7:
			return c.getPower();
		default:
			return null;
		}
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
}
