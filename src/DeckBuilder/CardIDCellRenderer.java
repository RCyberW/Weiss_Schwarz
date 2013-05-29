package DeckBuilder;

import javax.swing.table.DefaultTableCellRenderer;

public class CardIDCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -149259411077133021L;

	public void setValue(Object value) {
		if (value instanceof String) {
			String val = (String) value;
			val = val.replace("_alt", "").replace("_sign", "");
			setText(val);
		}
	}
}
