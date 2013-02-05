/**
 * @file BuilderGUI.java
 * @author Jia Chen
 * @date 09/06/2011
 * @description 
 * 		BuilderGUI.java displays the deck building client. Allowing 
 * 		users to save and load decks as well as customize them
 */

/**
 * TODO:
 * drag and drop (not possible currently, need research)
 * print deck with card translation and image (research)
 * deck list does not display correct card when sorted
 */

package DeckBuilder;

import java.awt.*;
/*import java.awt.datatransfer.DataFlavor;
 import java.awt.datatransfer.Transferable;
 import java.awt.datatransfer.UnsupportedFlavorException;*/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
//import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import CardAssociation.*;

public class BuilderGUI extends JFrame {

	private static final long serialVersionUID = -6401235618421175285L;

	// private properties of BuilderGUI

	private Box searchBox;
	private Box listBox;
	private JMenuBar menu;
	private JPanel cardInfo;
	private Box deckList;

	// private JMenuItem newD;
	// private JMenuItem save;
	// private JMenuItem load;
	// private JMenuItem exit;

	private JButton newdb;
	private JButton saveb;
	private JButton loadb;
	private JButton exitb;

	private Deck currentDeck;
	private boolean changes;
	private Card selectedCard;

	private final int OFFSET = 35;
	private final int DECKPERLINE = 15;
	private final int RESULTPERLINE = 10;
	private final int MAXIMUMRESULTSHOWN = 500;

	private JFileChooser fc;
	private ArrayList<Card> completeList;
	private HashMap<String, Card> cardHolder;

	private ArrayList<Card> resultList;
	private File file;

	private static String datafile;

	/**
	 * Start the GUI client
	 */
	public BuilderGUI() {
		super("Weiss Schwarz Deck Builder");

		resultList = new ArrayList<Card>();

		searchBox = Box.createVerticalBox();
		listBox = Box.createVerticalBox();
		listBox.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Search Result"), null));
		listBox.setPreferredSize(new Dimension(500, 500));
		menu = new JMenuBar();
		cardInfo = new JPanel();
		deckList = Box.createHorizontalBox();
		deckList.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Deck List"), null));

		fc = new JFileChooser();

		currentDeck = new Deck();
		selectedCard = null;
		file = null;
		changes = false;

		deserializer();

		setSize(1000, 720);
		setResizable(false);
	}

	// ///////////////////////
	//
	// GUI Builders
	//
	// //////////////////////

	/**
	 * Building search box
	 */
	private void buildSearchBox() {
		Box row1 = Box.createHorizontalBox();
		Box row2 = Box.createHorizontalBox();
		Box row3 = Box.createHorizontalBox();
		final NumberFormat numberInput = NumberFormat.getInstance();
		numberInput.setParseIntegerOnly(true);

		final JTextField idSearch = new JTextField();
		final JTextField nameSearch = new JTextField();
		final JTextField traitSearch = new JTextField();
		// final JTextField typeSearch = new JTextField();
		// final JTextField colorSearch = new JTextField();
		final JTextField abilitySearch = new JTextField();
		final JTextField powerSearch = new JTextField();
		final JTextField costSearch = new JTextField();
		final JTextField levelSearch = new JTextField();
		final JTextField soulSearch = new JTextField();
		// final JTextField triggerSearch = new JTextField();

		CCode[] colorSelections = null;
		colorSelections = CCode.values();
		final JComboBox colorList = new JComboBox(colorSelections);
		colorList.setSelectedItem(null);

		CardAssociation.Type[] classifications = null;
		classifications = CardAssociation.Type.values();
		final JComboBox typeList = new JComboBox(classifications);
		typeList.setSelectedItem(null);

		Trigger[] triggerSelections = null;
		triggerSelections = Trigger.values();
		final JComboBox triggerList = new JComboBox(triggerSelections);
		triggerList.setSelectedItem(null);

		final KeyListener searchFieldListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					resultList.clear();

					/*
					 * System.out.println(idSearch.getText() +
					 * nameSearch.getText() + triggerSearch.getText() +
					 * powerSearch.getText() + costSearch.getText() +
					 * colorSearch.getText() + levelSearch.getText() +
					 * traitSearch.getText() + typeSearch.getText() +
					 * soulSearch.getText());
					 */

					String cardID = idSearch.getText();

					String name = nameSearch.getText();

					CCode sColor = (CCode) colorList.getSelectedItem();
					/*
					 * String color = colorSearch.getText(); if
					 * (color.equalsIgnoreCase("RED")) { sColor = CCode.RED; }
					 * else if (color.equalsIgnoreCase("BLUE")) { sColor =
					 * CCode.BLUE; } else if (color.equalsIgnoreCase("YELLOW"))
					 * { sColor = CCode.YELLOW; } else if
					 * (color.equalsIgnoreCase("GREEN")) { sColor = CCode.GREEN;
					 * } else { sColor = null; }
					 */

					CardAssociation.Type sType = (CardAssociation.Type) typeList
							.getSelectedItem();
					/*
					 * String type = typeSearch.getText(); if
					 * (type.equalsIgnoreCase("CHARACTER")) { sType =
					 * CardAssociation.Type.CHARACTER; } else if
					 * (type.equalsIgnoreCase("CLIMAX")) { sType =
					 * CardAssociation.Type.CLIMAX; } else if
					 * (type.equalsIgnoreCase("EVENT")) { sType =
					 * CardAssociation.Type.EVENT; } else { sType = null; }
					 */

					String level = levelSearch.getText();
					int sLevel;
					try {
						sLevel = numberInput.parse(level).intValue();
					} catch (ParseException e1) {
						sLevel = -1;
						// sLevel = (level.isEmpty()) ? -1 : 0;
					}

					String cost = costSearch.getText();
					int sCost;
					try {
						sCost = numberInput.parse(cost).intValue();
					} catch (ParseException e1) {
						sCost = -1;
						// sCost = (cost.isEmpty()) ? -1 : 0;
					}

					Trigger sTrigger = (Trigger) triggerList.getSelectedItem();
					/*
					 * String trigger = triggerSearch.getText(); sTrigger =
					 * Trigger.convertString(trigger);
					 */

					String power = powerSearch.getText();
					int sPower;
					try {
						sPower = numberInput.parse(power).intValue();
					} catch (ParseException e1) {
						sPower = -1;
						// sPower = (power.isEmpty()) ? -1 : 0;
					}
					String soul = soulSearch.getText();
					int sSoul;
					try {
						sSoul = numberInput.parse(soul).intValue();
					} catch (ParseException e1) {
						sSoul = -1;
						// sSoul = (soul.isEmpty()) ? -1 : 0;
					}
					String trait = traitSearch.getText();

					String sAbility = abilitySearch.getText();

					for (Card c : completeList) {
						if (c.meetsRequirement(cardID, name, sColor, sType,
								sLevel, sCost, sTrigger, sPower, sSoul, trait,
								sAbility))
							resultList.add(c);
					}

					refresh("search");
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

		};

		/*
		 * final JFormattedTextField powerSearch = new JFormattedTextField(
		 * numberInput); final JFormattedTextField costSearch = new
		 * JFormattedTextField( numberInput); final JFormattedTextField
		 * levelSearch = new JFormattedTextField( numberInput); final
		 * JFormattedTextField soulSearch = new JFormattedTextField(
		 * numberInput);
		 */

		JButton submitButton = new JButton("Submit");
		submitButton.setPreferredSize(new Dimension(100, 25));
		JButton clearButton = new JButton("Clear");
		submitButton.setPreferredSize(new Dimension(100, 25));

		// nameSearch.setMaximumSize(new Dimension(255, 20));

		JLabel idLabel = new JLabel("Card ID");
		JLabel nameLabel = new JLabel("Card Name");
		JLabel triggerLabel = new JLabel("Trigger");
		JLabel powerLabel = new JLabel("Power");
		JLabel costLabel = new JLabel("Cost");
		JLabel colorLabel = new JLabel("Color");
		JLabel levelLabel = new JLabel("Level");
		JLabel traitLabel = new JLabel("Trait");
		JLabel typeLabel = new JLabel("Type");
		JLabel soulLabel = new JLabel("Soul");
		JLabel abilityLabel = new JLabel("Ability");

		idLabel.setLabelFor(idSearch);
		nameLabel.setLabelFor(nameSearch);
		// triggerLabel.setLabelFor(triggerSearch);
		powerLabel.setLabelFor(powerSearch);
		costLabel.setLabelFor(costSearch);
		// colorLabel.setLabelFor(colorSearch);
		levelLabel.setLabelFor(levelSearch);
		traitLabel.setLabelFor(traitSearch);
		// typeLabel.setLabelFor(typeSearch);
		soulLabel.setLabelFor(soulSearch);
		abilityLabel.setLabelFor(abilitySearch);

		// searchBox.setLayout(new GridLayout(0, 2));

		idSearch.addKeyListener(searchFieldListener);
		nameSearch.addKeyListener(searchFieldListener);
		traitSearch.addKeyListener(searchFieldListener);
		// typeSearch.addKeyListener(searchFieldListener);
		// colorSearch.addKeyListener(searchFieldListener);
		abilitySearch.addKeyListener(searchFieldListener);
		powerSearch.addKeyListener(searchFieldListener);
		costSearch.addKeyListener(searchFieldListener);
		levelSearch.addKeyListener(searchFieldListener);
		soulSearch.addKeyListener(searchFieldListener);
		// triggerSearch.addKeyListener(searchFieldListener);

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				resultList.clear();

				/*
				 * System.out.println(idSearch.getText() + nameSearch.getText()
				 * + triggerSearch.getText() + powerSearch.getText() +
				 * costSearch.getText() + colorSearch.getText() +
				 * levelSearch.getText() + traitSearch.getText() +
				 * typeSearch.getText() + soulSearch.getText());
				 */

				String cardID = idSearch.getText();

				String name = nameSearch.getText();

				CCode sColor = (CCode) colorList.getSelectedItem();
				/*
				 * String color = colorSearch.getText(); if
				 * (color.equalsIgnoreCase("RED")) { sColor = CCode.RED; } else
				 * if (color.equalsIgnoreCase("BLUE")) { sColor = CCode.BLUE; }
				 * else if (color.equalsIgnoreCase("YELLOW")) { sColor =
				 * CCode.YELLOW; } else if (color.equalsIgnoreCase("GREEN")) {
				 * sColor = CCode.GREEN; } else { sColor = null; }
				 */

				CardAssociation.Type sType = (CardAssociation.Type) typeList
						.getSelectedItem();
				/*
				 * String type = typeSearch.getText(); if
				 * (type.equalsIgnoreCase("CHARACTER")) { sType =
				 * CardAssociation.Type.CHARACTER; } else if
				 * (type.equalsIgnoreCase("CLIMAX")) { sType =
				 * CardAssociation.Type.CLIMAX; } else if
				 * (type.equalsIgnoreCase("EVENT")) { sType =
				 * CardAssociation.Type.EVENT; } else { sType = null; }
				 */

				String level = levelSearch.getText();
				int sLevel;
				try {
					sLevel = numberInput.parse(level).intValue();
				} catch (ParseException e1) {
					sLevel = -1;
					// sLevel = (level.isEmpty()) ? -1 : 0;
				}

				String cost = costSearch.getText();
				int sCost;
				try {
					sCost = numberInput.parse(cost).intValue();
				} catch (ParseException e1) {
					sCost = -1;
					// sCost = (cost.isEmpty()) ? -1 : 0;
				}

				Trigger sTrigger = (Trigger) triggerList.getSelectedItem();
				/*
				 * String trigger = triggerSearch.getText(); sTrigger =
				 * Trigger.convertString(trigger);
				 */

				String power = powerSearch.getText();
				int sPower;
				try {
					sPower = numberInput.parse(power).intValue();
				} catch (ParseException e1) {
					sPower = -1;
					// sPower = (power.isEmpty()) ? -1 : 0;
				}
				String soul = soulSearch.getText();
				int sSoul;
				try {
					sSoul = numberInput.parse(soul).intValue();
				} catch (ParseException e1) {
					sSoul = -1;
					// sSoul = (soul.isEmpty()) ? -1 : 0;
				}
				String trait = traitSearch.getText();

				String sAbility = abilitySearch.getText();

				for (Card c : completeList) {
					if (c.meetsRequirement(cardID, name, sColor, sType, sLevel,
							sCost, sTrigger, sPower, sSoul, trait, sAbility))
						resultList.add(c);
				}

				refresh("search");
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				idSearch.setText("");
				nameSearch.setText("");
				traitSearch.setText("");
				// typeSearch.setText("");
				// colorSearch.setText("");
				abilitySearch.setText("");
				// triggerSearch.setText("");
				powerSearch.setText("");
				costSearch.setText("");
				soulSearch.setText("");
				levelSearch.setText("");

				colorList.setSelectedItem(null);
				typeList.setSelectedItem(null);
				triggerList.setSelectedItem(null);
			}
		});

		row1.add(Box.createHorizontalStrut(5));
		row1.add(idLabel);
		row1.add(Box.createHorizontalStrut(5));
		row1.add(idSearch);
		row1.add(Box.createHorizontalStrut(5));
		row1.add(nameLabel);
		row1.add(Box.createHorizontalStrut(5));
		row1.add(nameSearch);
		row1.add(Box.createHorizontalStrut(5));
		row1.add(colorLabel);
		row1.add(Box.createHorizontalStrut(5));
		// row1.add(colorSearch);
		row1.add(colorList);
		row1.add(Box.createHorizontalStrut(5));
		row1.add(typeLabel);
		row1.add(Box.createHorizontalStrut(5));
		// row1.add(typeSearch);
		row1.add(typeList);
		row1.add(Box.createHorizontalStrut(5));
		row1.add(submitButton);
		row1.add(Box.createHorizontalStrut(5));

		row2.add(Box.createHorizontalStrut(5));
		row2.add(triggerLabel);
		row2.add(Box.createHorizontalStrut(5));
		// row2.add(triggerSearch);
		row2.add(triggerList);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(powerLabel);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(powerSearch);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(costLabel);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(costSearch);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(soulLabel);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(soulSearch);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(levelLabel);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(levelSearch);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(traitLabel);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(traitSearch);
		row2.add(Box.createHorizontalStrut(5));
		row2.add(clearButton);
		row2.add(Box.createHorizontalStrut(5));

		row3.add(Box.createHorizontalStrut(5));
		row3.add(abilityLabel);
		row3.add(Box.createHorizontalStrut(5));
		row3.add(abilitySearch);
		row3.add(Box.createHorizontalStrut(5));

		row1.setPreferredSize(new Dimension(800, 20));
		row1.setMaximumSize(new Dimension(800, 20));
		row2.setPreferredSize(new Dimension(800, 20));
		row2.setMaximumSize(new Dimension(800, 20));
		row3.setPreferredSize(new Dimension(800, 20));
		row3.setMaximumSize(new Dimension(800, 20));

		searchBox.add(row1);
		searchBox.add(Box.createVerticalStrut(5));
		searchBox.add(row2);
		searchBox.add(Box.createVerticalStrut(5));
		searchBox.add(row3);
		searchBox.setPreferredSize(new Dimension(800, 80));
		searchBox.setMaximumSize(new Dimension(800, 80));
	}

	/**
	 * Building the menu bar
	 */
	/*
	 * private void buildMenu() {
	 * 
	 * JMenu firstTab = new JMenu("File");
	 * 
	 * newD = new JMenuItem("New"); save = new JMenuItem("Save"); load = new
	 * JMenuItem("Load"); exit = new JMenuItem("Exit");
	 * 
	 * newD.addActionListener(new ActionListener() {
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { if (changes) {
	 * saveOption(); } file = null; currentDeck = new Deck();
	 * 
	 * resultList = (ArrayList<Card>) completeList.clone();
	 * 
	 * refresh("new"); } });
	 * 
	 * save.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { changes = false;
	 * action(e); } });
	 * 
	 * load.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { currentDeck = new
	 * Deck(); action(e); changes = false; } });
	 * 
	 * exit.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { saveOption();
	 * System.exit(1); } });
	 * 
	 * firstTab.add(newD); firstTab.add(save); firstTab.add(load);
	 * firstTab.add(exit);
	 * 
	 * menu.add(firstTab);
	 * 
	 * }
	 */

	/**
	 * Building the selection pane
	 * 
	 * @return Box populated with the option buttons
	 */
	private Box buildOption() {

		Box box = Box.createHorizontalBox();

		newdb = new JButton("New");
		saveb = new JButton("Save");
		loadb = new JButton("Load");
		exitb = new JButton("Exit");

		newdb.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (changes) {
					saveOption();
				}
				file = null;
				currentDeck = new Deck();

				resultList = (ArrayList<Card>) completeList.clone();

				refresh("new");
			}
		});

		saveb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changes = false;
				action(e);
			}
		});

		loadb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentDeck = new Deck();
				action(e);
				changes = false;
			}
		});

		exitb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveOption();
				System.exit(1);
			}
		});

		box.add(newdb);
		box.add(saveb);
		box.add(loadb);
		box.add(exitb);

		return box;
	}

	/**
	 * Building the card information pane
	 * 
	 * @param c
	 *            Display the information of the selected Card c
	 */
	private void buildCardInfo(Card c) {
		Box splitter = Box.createHorizontalBox();
		// Box splitter2 = Box.createVerticalBox();
		// int widthM = getPreferredSize().width / 2;
		int widthM = getWidth() / 2 - OFFSET;

		if (getPreferredSize().width / 2 - OFFSET > widthM)
			widthM = getPreferredSize().width / 2 - OFFSET;
		else
			widthM = getWidth() / 2 - OFFSET;

		splitter.setPreferredSize(new Dimension(widthM, 300));
		splitter.setMaximumSize(new Dimension(widthM, 300));
		// splitter2.setPreferredSize(new Dimension(widthM, 300));
		// splitter2.setMaximumSize(new Dimension(widthM, 300));

		if (c == null) {
		} else {
			splitter.add(c.displayImage(
					(int) (splitter.getPreferredSize().width * 0.22),
					splitter.getPreferredSize().height));
			splitter.add(c.getInfoPane(
					(int) (splitter.getPreferredSize().width * 0.78),
					splitter.getPreferredSize().height));
			// splitter2.add(splitter);

		}

		JEditorPane link = new JEditorPane(
				"text/html",
				"Special thanks to <a href = 'http://littleakiba.com/tcg/weiss-schwarz'>littleakiba</a> for the translations.");
		link.setEditable(false);
		link.setOpaque(false);
		link.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent hle) {
				if (HyperlinkEvent.EventType.ACTIVATED.equals(hle
						.getEventType())) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI(
								"http://littleakiba.com/tcg/weiss-schwarz"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});

		Box splitter3 = Box.createVerticalBox();

		splitter3.add(splitter);
		splitter3.add(link);
		splitter3.add(buildOption());

		cardInfo.add(splitter3);

		// System.out.println("splitter has components: " +
		// splitter.getComponentCount());
	}

	/**
	 * Building search result list
	 * 
	 * @return JScrollPane populated with information of the cards in the result
	 *         list
	 */
	private JScrollPane buildResultList() {
		String[] columnNames = { "ID", "Name", "Color", "Type", "Level",
				"Cost", "Soul", "Power" };

		final ArrayList<Card> allCards = resultList;

		Object[][] cardData = new Object[allCards.size()][columnNames.length];

		// Input display data into the table
		for (int i = 0; i < allCards.size(); i++) {
			Card card = allCards.get(i);
			cardData[i][0] = card.getID();
			cardData[i][1] = card.getCardName();
			cardData[i][2] = card.getC();
			cardData[i][3] = card.getT();
			cardData[i][4] = card.getLevel();
			cardData[i][5] = card.getCost();
			cardData[i][6] = card.getSoul();
			cardData[i][7] = card.getPower();
		}

		final JTable displayTable = new JTable(cardData, columnNames) {

			private static final long serialVersionUID = 3570425890676389430L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};

		// Allocate column widths
		int widthM = getPreferredSize().width / 2 - OFFSET;
		if (getWidth() / 2 - OFFSET > widthM)
			widthM = getWidth() / 2 - OFFSET;
		else
			widthM = getPreferredSize().width / 2 - OFFSET;

		displayTable.setPreferredScrollableViewportSize(new Dimension(widthM,
				70));
		displayTable.setFillsViewportHeight(true);
		displayTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		displayTable.setRowSorter(new TableRowSorter<TableModel>(displayTable
				.getModel()));

		// Handles right click selection
		displayTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Left mouse click
				if (SwingUtilities.isLeftMouseButton(e)) {
					// Do something
				}
				// Right mouse click
				else if (SwingUtilities.isRightMouseButton(e)) {
					// Get the coordinates of the mouse click
					Point p = e.getPoint();

					// Get the row index that contains that coordinate
					int rowNumber = displayTable.rowAtPoint(p);

					// Get the ListSelectionModel of the JTable
					ListSelectionModel model = displayTable.getSelectionModel();

					// Set the selected interval of rows. Using the "rowNumber"
					// variable for the beginning and end selects only that one
					// row.
					model.setSelectionInterval(rowNumber, rowNumber);

					int row = displayTable.getSelectedRow();
					if (row > -1) {
						selectedCard = cardHolder.get(displayTable.getValueAt(
								row, 0));
						// selectedCard = allCards.get(row);
					}
					refresh("listBox");
				}

			}
		});

		// Handles click action
		displayTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int row = displayTable.getSelectedRow();
				if (row > -1) {
					selectedCard = cardHolder.get(displayTable.getValueAt(row,
							0));
					// selectedCard = allCards.get(row);
				}
				if (e.getClickCount() == 1
						&& e.getButton() == MouseEvent.BUTTON1) {
					refresh("listBox");
				} /*
				 * else if ((e.getClickCount() == 2 && e.getButton() ==
				 * MouseEvent.BUTTON1) || (e.getClickCount() == 1 &&
				 * e.getButton() == MouseEvent.BUTTON3) && row > -1) {
				 * 
				 * currentDeck.addCard(selectedCard); refresh("listBox"); }
				 */
			}

			public void mouseReleased(MouseEvent e) {
				int row = displayTable.getSelectedRow();
				if (row > -1) {
					selectedCard = cardHolder.get(displayTable.getValueAt(row,
							0));
					// selectedCard = allCards.get(row);
				}
				if ((e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
						|| (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
						&& row > -1) {
					currentDeck.addCard(selectedCard);
					refresh("listBox");
				}
			}
		});

		// Handles keyboard inputs
		displayTable.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int row = displayTable.getSelectedRow();
				if (row > -1) {
					selectedCard = cardHolder.get(displayTable.getValueAt(row,
							0));
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					currentDeck.addCard(selectedCard);
				}
				refresh("listBox");
			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

		});

		TableColumn indCol = displayTable.getColumnModel().getColumn(0);
		indCol.setPreferredWidth(110);
		TableColumn namCol = displayTable.getColumnModel().getColumn(1);
		namCol.setPreferredWidth(widthM - 110 - 55 - 60 - 35 - 35 - 35 - 50);
		TableColumn colCol = displayTable.getColumnModel().getColumn(2);
		colCol.setPreferredWidth(55);
		TableColumn typCol = displayTable.getColumnModel().getColumn(3);
		typCol.setPreferredWidth(60);
		TableColumn lvlCol = displayTable.getColumnModel().getColumn(4);
		lvlCol.setPreferredWidth(35);
		TableColumn cstCol = displayTable.getColumnModel().getColumn(5);
		cstCol.setPreferredWidth(35);
		TableColumn solCol = displayTable.getColumnModel().getColumn(6);
		solCol.setPreferredWidth(35);
		TableColumn powCol = displayTable.getColumnModel().getColumn(7);
		powCol.setPreferredWidth(50);

		JScrollPane listPane = new JScrollPane(displayTable);

		return listPane;
	}

	/**
	 * Building search result thumbnail
	 * 
	 * @param listPane
	 *            The ResultList JScrollPane
	 * @return JScrollPane populated with thumbnail of the cards in the result
	 *         list
	 */
	private JScrollPane buildResultThumbPane(JScrollPane listPane) {
		JPanel panel = new JPanel();
		Box box = Box.createHorizontalBox();
		Box vbox = Box.createVerticalBox();
		vbox.setAlignmentX(Box.LEFT_ALIGNMENT);

		if (resultList.size() > MAXIMUMRESULTSHOWN) {

		} else {

			for (int i = 0; i < resultList.size(); i++) {
				if (i % RESULTPERLINE == 0 || i >= resultList.size()) {
					if (i > 0) {
						vbox.add(box);
					}
					box = Box.createHorizontalBox();
					box.setAlignmentX(Box.LEFT_ALIGNMENT);
				}
				final Card thisCard = resultList.get(i);
				JLabel tempLab = thisCard.initiateImage();
				MouseListener listener = new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						// JComponent comp = (JComponent) e.getSource();
						// TransferHandler handler = comp.getTransferHandler();
						// handler.exportAsDrag(comp, e, TransferHandler.COPY);

						selectedCard = thisCard;
						System.out.println("selected "
								+ selectedCard.getCardName());
						refresh("listBox2");

						if ((e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
								|| (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)) {
							selectedCard = thisCard;
							currentDeck.addCard(selectedCard);
							refresh("listBox");
						}
					}

					public void mouseReleased(MouseEvent e) {
						System.out.println("WILL BE ADDED TO DECK");
					}
				};

				// thisCard.setTransferHandler(new DragHandler(thisCard));
				tempLab.addMouseListener(listener);
				box.add(tempLab);

			}
			if ((resultList.size()) % RESULTPERLINE != 0) {
				vbox.add(box);
			} else if (vbox.getComponentCount() < 1) {
				vbox.add(box);
			}
			panel.add(vbox);
		}
		JScrollPane jsp = new JScrollPane(panel);
		jsp.setPreferredSize(new Dimension(listPane.getPreferredSize()));

		return jsp;
	}

	/**
	 * Building the search result viewer area
	 * 
	 * @return JTabbedPane with different tabs for thumbnail and list views of
	 *         the result list
	 */
	private JTabbedPane buildResultArea() {

		JScrollPane listResulPane = buildResultList();
		JScrollPane listThumbPane = buildResultThumbPane(listResulPane);

		JTabbedPane jtb = new JTabbedPane();
		jtb.add("View 1", listResulPane);
		jtb.add("View 2", listThumbPane);

		return jtb;
	}

	/**
	 * Building the deck list pane
	 * 
	 * @return JScrollPane populated with information of the cards in the deck
	 */
	private JScrollPane buildDeckList() {

		String[] columnNames = { "#", "ID", "Name", "Color", "Type", "L", "C",
				"Trigger", "S", "Power" };

		final ArrayList<Card> cards = currentDeck.getUnique();

		Object[][] cardData = new Object[cards.size()][columnNames.length];

		// Input card data into the display table
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			cardData[i][0] = card.getCardCount();
			cardData[i][1] = card.getID();
			cardData[i][2] = card.getCardName();
			cardData[i][3] = card.getC();
			cardData[i][4] = card.getT();
			cardData[i][5] = card.getLevel();
			cardData[i][6] = card.getCost();
			cardData[i][7] = card.getTrigger();
			cardData[i][8] = card.getSoul();
			cardData[i][9] = card.getPower();
		}

		final JTable displayTable = new JTable(cardData, columnNames) {

			private static final long serialVersionUID = 3570425890676389430L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};

		displayTable
				.setPreferredScrollableViewportSize(new Dimension(600, 175));
		displayTable.setFillsViewportHeight(true);
		displayTable.setRowSorter(new TableRowSorter<TableModel>(displayTable
				.getModel()));

		// Handles right click selection
		displayTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Left mouse click
				if (SwingUtilities.isLeftMouseButton(e)) {
					// Do something
				}
				// Right mouse click
				else if (SwingUtilities.isRightMouseButton(e)) {
					// get the coordinates of the mouse click
					Point p = e.getPoint();

					// get the row index that contains that coordinate
					int rowNumber = displayTable.rowAtPoint(p);

					// Get the ListSelectionModel of the JTable
					ListSelectionModel model = displayTable.getSelectionModel();

					// Set the selected interval of rows. Using the "rowNumber"
					// variable for the beginning and end selects only that one
					// row.
					model.setSelectionInterval(rowNumber, rowNumber);

					int row = displayTable.getSelectedRow();
					if (row > -1) {
						selectedCard = cardHolder.get(displayTable.getValueAt(
								row, 0));
						// selectedCard = allCards.get(row);
					}
					refresh("deckList");
				}

			}
		});

		// Handles click action
		displayTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int row = displayTable.getSelectedRow();
				if (row > -1)
					selectedCard = cardHolder.get(displayTable.getValueAt(row,
							1));
				if (e.getClickCount() == 1
						&& e.getButton() == MouseEvent.BUTTON1) {
					refresh("deckList");
				} /*
				 * else if ((e.getClickCount() == 2 && e.getButton() ==
				 * MouseEvent.BUTTON1) || (e.getClickCount() == 1 &&
				 * e.getButton() == MouseEvent.BUTTON3) && row > -1) {
				 * currentDeck.removeCard(selectedCard); refresh("deckList2"); }
				 */
			}

			public void mouseReleased(MouseEvent e) {
				int row = displayTable.getSelectedRow();
				if (row > -1)
					selectedCard = cardHolder.get(displayTable.getValueAt(row,
							1));
				if ((e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
						|| (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
						&& row > -1) {
					currentDeck.removeCard(selectedCard);
					refresh("deckList2");
				}
			}
		});

		displayTable.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent f) {
			}

			@Override
			public void focusLost(FocusEvent f) {
			}
		});

		// Keyboard controls to operate the builder
		displayTable.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

			}

			/*
			 * Key Explanations DELETE remove a card - remove a card = add a
			 * card + add a card ENTER add a card
			 * 
			 * @see
			 * java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				int row = displayTable.getSelectedRow();
				if (row > -1)
					selectedCard = cardHolder.get(displayTable.getValueAt(row,
							1));
				refresh("deckList");
				if (e.getKeyCode() == KeyEvent.VK_DELETE
						|| e.getKeyCode() == KeyEvent.VK_MINUS) {
					currentDeck.removeCard(selectedCard);
					refresh("deckList2");
				} else if (e.getKeyCode() == KeyEvent.VK_EQUALS
						|| e.getKeyCode() == KeyEvent.VK_ADD
						|| e.getKeyCode() == KeyEvent.VK_ENTER) {
					currentDeck.addCard(selectedCard);
					refresh("deckList2");
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});

		// allocating size for columns
		int widthM = getPreferredSize().width;

		if (getWidth() / 2 - OFFSET > widthM)
			widthM = getWidth();
		else
			widthM = getPreferredSize().width;

		int cntW = 20;
		int indW = 130;
		int colW = 75;
		int typW = 80;
		int lvlW = 20;
		int cstW = 20;
		int trgW = 100;
		int solW = 20;
		int powW = 60;

		int usedSpace = cntW + indW + colW + typW + lvlW + cstW + trgW + solW
				+ powW;

		TableColumn cntCol = displayTable.getColumnModel().getColumn(0);
		cntCol.setPreferredWidth(cntW);
		TableColumn indCol = displayTable.getColumnModel().getColumn(1);
		indCol.setPreferredWidth(indW);
		TableColumn namCol = displayTable.getColumnModel().getColumn(2);
		namCol.setPreferredWidth(widthM - usedSpace);
		TableColumn colCol = displayTable.getColumnModel().getColumn(3);
		colCol.setPreferredWidth(colW);
		TableColumn typCol = displayTable.getColumnModel().getColumn(4);
		typCol.setPreferredWidth(typW);
		TableColumn lvlCol = displayTable.getColumnModel().getColumn(5);
		lvlCol.setPreferredWidth(lvlW);
		TableColumn cstCol = displayTable.getColumnModel().getColumn(6);
		cstCol.setPreferredWidth(cstW);
		TableColumn trgCol = displayTable.getColumnModel().getColumn(7);
		trgCol.setPreferredWidth(trgW);
		TableColumn solCol = displayTable.getColumnModel().getColumn(8);
		solCol.setPreferredWidth(solW);
		TableColumn powCol = displayTable.getColumnModel().getColumn(9);
		powCol.setPreferredWidth(powW);

		JScrollPane listPane = new JScrollPane(displayTable);

		return listPane;
	}

	/**
	 * Building deck statistic analyzer
	 * 
	 * @return Box with the statistical information of the deck displayed
	 */
	private Box buildStatsZone() {
		Box leftPanel = Box.createVerticalBox();
		Box analyzerBox = Box.createHorizontalBox();

		Box newVert = Box.createVerticalBox();
		newVert.add(new JLabel("Card count: "));
		newVert.add(new JLabel("Climax: "));
		newVert.add(new JLabel("Level 0: "));
		newVert.add(new JLabel("Level 1: "));
		newVert.add(new JLabel("Level 2: "));
		newVert.add(new JLabel("Level 3: "));
		newVert.add(new JLabel("Soul Triggers: "));
		Box newVert2 = Box.createVerticalBox();
		newVert2.add(new JLabel(String.valueOf(currentDeck.getCards().size())));
		newVert2.add(new JLabel(String.valueOf(currentDeck.getNumClimax())));
		newVert2.add(new JLabel(String.valueOf(currentDeck.getNumLevel0())));
		newVert2.add(new JLabel(String.valueOf(currentDeck.getNumLevel1())));
		newVert2.add(new JLabel(String.valueOf(currentDeck.getNumLevel2())));
		newVert2.add(new JLabel(String.valueOf(currentDeck.getNumLevel3())));
		newVert2.add(new JLabel(String.valueOf(currentDeck.getNumSoul())));
		newVert2.setPreferredSize(new Dimension(20, 100));

		Box newVert3 = Box.createVerticalBox();
		newVert3.add(new JLabel("Event: "));
		newVert3.add(new JLabel("Yellow: "));
		newVert3.add(new JLabel("Green: "));
		newVert3.add(new JLabel("Red: "));
		newVert3.add(new JLabel("Blue: "));
		newVert3.add(new JLabel("------"));
		newVert3.add(new JLabel("Damage: "));
		Box newVert4 = Box.createVerticalBox();
		newVert4.add(new JLabel(String.valueOf(currentDeck.getNumEvent())));
		newVert4.add(new JLabel(String.valueOf(currentDeck.getNumYellow())));
		newVert4.add(new JLabel(String.valueOf(currentDeck.getNumGreen())));
		newVert4.add(new JLabel(String.valueOf(currentDeck.getNumRed())));
		newVert4.add(new JLabel(String.valueOf(currentDeck.getNumBlue())));
		newVert4.add(new JLabel("------"));
		newVert4.add(new JLabel(String.valueOf(currentDeck.getDamage())));
		newVert4.setPreferredSize(new Dimension(20, 100));

		analyzerBox.add(newVert);
		// deckList.add(Box.createHorizontalGlue());
		analyzerBox.add(newVert2);
		analyzerBox.add(Box.createHorizontalStrut(10));
		analyzerBox.add(newVert3);
		// deckList.add(Box.createHorizontalGlue());
		analyzerBox.add(newVert4);
		// analyzerBox.add(Box.createHorizontalStrut(5));

		JButton clearDeck = new JButton("Clear Deck");
		clearDeck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (changes) {
					saveOption();
				}
				file = null;
				currentDeck = new Deck();

				refresh("deckList2");
			}
		});

		leftPanel.add(analyzerBox);
		leftPanel.add(clearDeck);
		leftPanel.setAlignmentX(CENTER_ALIGNMENT);
		return leftPanel;
	}

	/**
	 * Building deck thumbnail
	 * 
	 * @param deckListPane
	 *            The ResultList JScrollPane
	 * @return JScrollPane populated with thumbnail of the cards in the deck
	 */
	private JScrollPane buildDeckThumbPane(JScrollPane deckListPane) {

		JPanel panel = new JPanel();
		Box box = Box.createHorizontalBox();
		Box vbox = Box.createVerticalBox();
		vbox.setAlignmentX(Box.LEFT_ALIGNMENT);

		// DropHandler dh = new DropHandler(currentDeck);

		for (int i = 0; i < 50; i++) {
			if (i % DECKPERLINE == 0 || i >= 50) {
				if (i > 0) {
					vbox.add(box);
				}
				box = Box.createHorizontalBox();
				box.setAlignmentX(Box.LEFT_ALIGNMENT);
			}
			if (i < currentDeck.getCards().size()) {
				final Card thisCard = currentDeck.getCards().get(i);
				JLabel tempLab = thisCard.initiateImage();
				MouseListener listener = new MouseAdapter() {
					public void mousePressed(MouseEvent e) {

						/*JComponent comp = (JComponent) e.getSource();
						TransferHandler handler = comp.getTransferHandler();
						handler.exportAsDrag(comp, e, TransferHandler.COPY);*/

						selectedCard = thisCard;
						System.out.println(thisCard.getCardName() + " has "
								+ thisCard.getCardCount() + " copies");
						refresh("deckList");

						if ((e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
								|| (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)) {
							currentDeck.removeCard(selectedCard);
							refresh("deckList2");
						}
					}
				};

				// thisCard.setTransferHandler(dh);
				tempLab.addMouseListener(listener);
				Box newBox = Box.createHorizontalBox();
				newBox.add(tempLab);
				box.add(newBox);
			} else {
				/*
				 * Card cv2 = new Card(i + "", "test" + i);
				 * System.out.println(cv2.getCardName()); cv2.initiateImage();
				 * cv2.addMouseListener(cv2); cv2.setTransferHandler(dh);
				 * box.add(cv2);
				 */
			}

		}
		if ((resultList.size() - 1) % DECKPERLINE != 0)
			vbox.add(box);
		panel.add(vbox);

		JScrollPane jsp = new JScrollPane(panel);
		jsp.setPreferredSize(new Dimension(deckListPane.getPreferredSize()));
		/*
		 * jsp.addMouseMotionListener(new MouseMotionListener(){
		 * 
		 * @Override public void mouseDragged(MouseEvent arg0) {
		 * System.out.println("DRAG HAPPENED"); }
		 * 
		 * @Override public void mouseMoved(MouseEvent arg0) {
		 * System.out.println("MOVE HAPPENED"); }
		 * 
		 * });
		 */

		return jsp;
	}

	/**
	 * Building deck viewer area
	 * 
	 * @return JTabbedPane with different tabs for thumbnail and list views of
	 *         the deck
	 */
	private JTabbedPane buildDeckArea() {
		JTabbedPane jtb = new JTabbedPane();
		/*
		 * jtb.addMouseMotionListener(new MouseMotionListener(){
		 * 
		 * @Override public void mouseDragged(MouseEvent e) {
		 * System.out.println("JTB DRAG"); }
		 * 
		 * @Override public void mouseMoved(MouseEvent e) {
		 * 
		 * }
		 * 
		 * });
		 */

		JScrollPane deckListPane = buildDeckList();
		JScrollPane deckThumPane = buildDeckThumbPane(deckListPane);

		jtb.addTab("View 1", deckListPane);
		jtb.addTab("View 2", deckThumPane);

		return jtb;
	}

	// ///////////////////////
	//
	// Public Methods
	//
	// //////////////////////

	/**
	 * Initialize the client
	 */
	public void init() {
		buildUI();
	}

	/**
	 * Action events that are necessary
	 * 
	 * @param e
	 *            ActionEvent e
	 */
	public void action(ActionEvent e) {
		fc.setCurrentDirectory(new File("Deck"));

		if (e.getSource() == loadb) {
			saveOption();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				currentDeck.loadRaw(file, cardHolder);
				refresh("load");
			}
		} else if (e.getSource() == saveb) {

			File directoryMaker = new File("Deck");
			if (!directoryMaker.exists())
				directoryMaker.mkdir();
			fc.setCurrentDirectory(directoryMaker);
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// if (file == null)
				file = fc.getSelectedFile();
				if (file.exists()) {
					// System.err.println(file.getCardName());
				} else {

					file = new File(directoryMaker, file.getName());
					System.out.println(file.getName() + " was created");
				}
				currentDeck.saveRaw(file);
			}
		}
	}

	/**
	 * Refresh view
	 * 
	 * @param source
	 *            The string representation of the source called refresh
	 */
	public void refresh(String source) {
		// getContentPane().removeAll();
		// validate();

		if (source.equalsIgnoreCase("load")
				|| source.equalsIgnoreCase("listBox")
				|| source.equalsIgnoreCase("deckList")
				|| source.equalsIgnoreCase("search")
				|| source.equalsIgnoreCase("new")
				|| source.equalsIgnoreCase("listBox2")) {
			cardInfo.removeAll();
			cardInfo.validate();
			buildCardInfo(selectedCard);
		}

		if (source.equalsIgnoreCase("search") || source.equalsIgnoreCase("new")) {
			listBox.getComponent(0).setVisible(false);
			listBox.removeAll();
			listBox.validate();
			listBox.add(new JLabel("Card count: " + resultList.size()));
			listBox.add(buildResultArea());
		}

		if (source.equalsIgnoreCase("load")) {
			changes = true;
		}

		if (source.equalsIgnoreCase("listBox")
				|| source.equalsIgnoreCase("deckList2")
				|| source.equalsIgnoreCase("load")
				|| source.equalsIgnoreCase("new")) {
			deckList.removeAll();
			deckList.validate();
			deckList.add(buildStatsZone());
			deckList.add(buildDeckArea());
			// changes = true;
		}

		// BorderLayout layout = new BorderLayout();
		// getContentPane().setLayout(layout);
		//
		// add(BorderLayout.NORTH, searchBox);
		// add(BorderLayout.WEST, cardInfo);
		// add(BorderLayout.EAST, listBox);
		// add(BorderLayout.SOUTH, deckList);
		// setJMenuBar(menu);

		// pack();
		// System.out.println(getWidth() + " * " + getHeight());

		setVisible(true);
	}

	/**
	 * Prompt for save
	 */
	private void saveOption() {
		if (changes) {
			if (file == null) {
				int returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
				}
			}

			if (file != null) {
				currentDeck.saveRaw(file);
				currentDeck.save(file);
			}
		}
	}

	/**
	 * Build the internal elements of the UI
	 */
	public void buildUI() {
		buildSearchBox();
		// buildMenu();
		buildCardInfo(selectedCard);
		listBox.add(new JLabel("Card count: " + resultList.size()));
		listBox.add(buildResultArea());
		deckList.add(buildStatsZone());
		deckList.add(buildDeckArea());

		setJMenuBar(menu);

		BorderLayout layout = new BorderLayout();
		getContentPane().setLayout(layout);

		add(BorderLayout.NORTH, searchBox);
		add(BorderLayout.WEST, cardInfo);
		add(BorderLayout.EAST, listBox);
		add(BorderLayout.SOUTH, deckList);
	}

	/**
	 * used by Player.java to load a selected deck
	 * 
	 * @param selectedDeck
	 *            Name of the selected deck
	 */
	public void loadDefaultDeck(String selectedDeck) {
		currentDeck.load(new File("Deck/" + selectedDeck), cardHolder);
		refresh("load");
	}

	/**
	 * De-serialize the data given
	 */
	@SuppressWarnings("unchecked")
	private void deserializer() {

		FileInputStream fileInput;
		ObjectInputStream objectInput;

		try {
			fileInput = new FileInputStream(datafile);
			objectInput = new ObjectInputStream(fileInput);

			completeList = (ArrayList<Card>) objectInput.readObject();
			resultList = (ArrayList<Card>) completeList.clone();
			cardHolder = (HashMap<String, Card>) objectInput.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	// main
	public static void main(String[] args) {
		if (args.length == 0)
			datafile = "CardDatav2";
		else
			datafile = args[0];
		BuilderGUI builderGui = new BuilderGUI();
		builderGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		builderGui.init();

		// builderGui.pack();
		builderGui.setVisible(true);
	}

}
