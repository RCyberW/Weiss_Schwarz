package DeckBuilder;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import CardAssociation.*;

public class DeckListTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -3458988850341773210L;
	
	private String[] columnNames = { "#", "ID", "Name", "Color", "Type", "L", "C",
			"Trigger", "S", "Power" };
	private Class<?>[] types = {Integer.class, 
			String.class, 
			String.class, 
			CCode.class, 
			Type.class,
			Integer.class,
			Integer.class,
			Trigger.class,
			Integer.class,
			Integer.class};
	private ArrayList<Card> decklist;
	
	public DeckListTableModel() {
	}
	
	public DeckListTableModel(ArrayList<Card> decklist) {
		this.decklist = decklist;
	}	
	
	
	public void setDeckList(ArrayList<Card> decklist) {
		this.decklist = decklist;
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return decklist.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Card c = decklist.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return c.getCardCount();
		case 1:
			return c.getID();
		case 2:
			return c.getCardName();
		case 3:
			return c.getC();
		case 4:
			return c.getT();
		case 5:
			return c.getLevel();
		case 6:
			return c.getCost();
		case 7:
			return c.getTrigger();
		case 8:
			return c.getSoul();
		case 9:
			return c.getPower();
		default:
			return null;
		}
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return types[columnIndex];
	}

}
