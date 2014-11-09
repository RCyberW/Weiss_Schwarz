package builderDeck;

import javax.swing.RowFilter;

import deckComponents.Card;

public class SearchRowFilter extends RowFilter<ResultListTableModel, Integer> {
  @Override
  public boolean include(Entry<? extends ResultListTableModel, ? extends Integer> entry) {
    ResultListTableModel resultCardListModel = (ResultListTableModel) entry.getModel();
    Card card = resultCardListModel.getCard(entry.getIdentifier());
    return card.isMatching();
  }
}
