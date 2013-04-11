package DeckBuilder;

import javax.swing.table.DefaultTableCellRenderer;

public class NumberCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -7133902731298846071L;

	
	public void setValue(Object value) {
		if (value instanceof Integer) {
			Integer val = (Integer) value;
			if (val.intValue() < 0) {
				setText("-");
			}
			else {
				setText(val.toString());
			}
		}
	}
	/*
	@Override
	public Component getTableCellRendererComponent(JTable jTable, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
		if (c instanceof JLabel && value instanceof Number) {
			JLabel label = (JLabel) c;
			Number num = (Number) value;
			if (num.intValue() < 0) {
				label.setText("N/A");
			}
			else {
				label.setText(num.toString());
			}
		}
		return c;
	}
	*/
}
