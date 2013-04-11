/**
 * @file Importer.java
 * @author Jia Chen
 * @date Sept 06, 2011
 * @description 
 * 		Importer.java is used for parsing in csv files and images 
 * 		into object representations of the cards
 */

package DeckBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

import CardAssociation.*;

public class Importer {

	private static ArrayList<Card> setCards = new ArrayList<Card>();
	private static HashMap<String, Card> allCards = new HashMap<String, Card>();
	private Writer nameDict;
	private Scanner scanner;

	public Importer() {
		try {
			nameDict = new BufferedWriter(new FileWriter(new File(
					"nameDict.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// parsing the given file to fill setCards and allCards
	/*
	 * public void scan(File file) {
	 * 
	 * try { Scanner scanner = new Scanner(file); scanner.nextLine();
	 * 
	 * while (scanner.hasNext()) { Scanner lineScan = new
	 * Scanner(scanner.nextLine()) .useDelimiter(";");
	 * 
	 * String cardId = lineScan.next(); String rarity = lineScan.next(); String
	 * name = lineScan.next(); String color = lineScan.next(); String side =
	 * lineScan.next(); String cardType = lineScan.next(); int level =
	 * lineScan.nextInt(); int cost = lineScan.nextInt(); int power =
	 * lineScan.nextInt(); int soul = lineScan.nextInt(); String trait1 =
	 * lineScan.next(); String trait2 = lineScan.next(); String trigger =
	 * lineScan.next();
	 * 
	 * Card card = new Card(cardId, name); card.setCost(cost); //
	 * card.setDamage(damage) card.setLevel(level); card.setPower(power);
	 * card.setSoul(soul); card.setTrait1(trait1); card.setTrait2(trait2);
	 * 
	 * System.out.println(trigger);
	 * card.setTrigger(Trigger.convertString(trigger));
	 * 
	 * if (color.equalsIgnoreCase("red")) { card.setC(CCode.RED); } else if
	 * (color.equalsIgnoreCase("blue")) { card.setC(CCode.BLUE); } else if
	 * (color.equalsIgnoreCase("yellow")) { card.setC(CCode.YELLOW); } else if
	 * (color.equalsIgnoreCase("green")) { card.setC(CCode.GREEN); }
	 * 
	 * if (cardType.equalsIgnoreCase("character")) { card.setT(Type.CHARACTER);
	 * } else if (cardType.equalsIgnoreCase("event")) { card.setT(Type.EVENT); }
	 * else if (cardType.equalsIgnoreCase("climax")) { card.setT(Type.CLIMAX); }
	 * 
	 * String textFlavor = lineScan.next(); String cardEffect = lineScan.next();
	 * card.setFlavorText(textFlavor); card.addEffect(cardEffect);
	 * 
	 * String imageName = lineScan.next();
	 * 
	 * card.setImage(new File("WS-AB150px/" + imageName));
	 * 
	 * Card c = allCards.put(name, card); if (c == null) { // new card
	 * setCards.add(card); } else { card.setID(cardId); //allCards.put(name,
	 * card); }
	 * 
	 * // System.out.println(trigger); }
	 * 
	 * } catch (FileNotFoundException e) { System.out.println("File Not Found");
	 * }
	 * 
	 * for (int i = 0; i < setCards.size(); i++) { //
	 * System.out.println(setCards.get(i).toString()); }
	 * 
	 * serializingLists(); }
	 */

	public void scanRaw(File file) {
		try {
			scanner = new Scanner(file);
			// System.out.println(file.exists() + " : " + file.getName());
			int lineDex = 0;
			boolean isStart = false;
			Card newCard = new Card();
			String fileName = file.getName().replace(".txt", "");
			String temp = "";
			boolean effectStart = true;

			while (scanner.hasNext()) {

				String str = scanner.nextLine().trim();
				if (str.isEmpty() && isStart) {
					// System.out.println("<START OF CARD>");
					isStart = false;
					lineDex = 0;
					newCard = new Card();
				} else if (str.equals("==========================")) {
					// System.out.println("<END OF CARD>");
					isStart = true;
					// newCard.setImage(new File("FieldImages/Vertical.png"));
					if (!new File("src" + newCard.getImageResource()).exists()) {
						// if (newCard.getImage().exists()) {
						// Card c = allCards.put(newCard.getCardName(),
						// newCard);
						newCard.setImageResource("/resources/FieldImages/cardBack-s.jpg");	
					}
					System.out.println(newCard.getImageResource());
					Card c = allCards.put(newCard.getID(), newCard);
					if (c == null) {
						setCards.add(newCard);

					} else {
						// c.setID(newCard.getID());
						// allCards.put(newCard.getCardName(), c);
						allCards.put(newCard.getID(), c);
						// if (!c.getImage().exists()) {
						// setCards.remove(c);
						// setCards.add(newCard);
						// }
					}

				} else if (str.isEmpty() && !isStart) {
				} else {

					switch (lineDex) {
					case 0: // card name
						str = rename(str);
						newCard.setCardName(str);
						str = "Title: " + str;
						break;
					case 1: // card id
						str = str.replace("  ", " ");
						newCard.setID(str);
						newCard.setImageResource("/resources/FieldImages/"
								+ fileName + "/"
								+ str.replace(" ", "_").replace("/", "-")
								+ ".jpg");
						/*
						 * newCard.setImage(new
						 * File("src/resources/FieldImages/" + fileName + "/" +
						 * str.replace(" ", "_").replace("/", "-") + ".jpg"));
						 */
						str = "ID: " + str;
						break;
					case 2: // card classification
						temp = str.substring("Type: ".length());
						if (temp.equalsIgnoreCase("climax")) {
							newCard.setT(Type.CLIMAX);
						} else if (temp.equalsIgnoreCase("event")) {
							newCard.setT(Type.EVENT);
						} else if (temp.equalsIgnoreCase("character")) {
							newCard.setT(Type.CHARACTER);
						}
						break;
					case 3:
						temp = str.substring("Level: ".length());
						if (!temp.equalsIgnoreCase("N/A"))
							newCard.setLevel(Integer.parseInt(temp));
						break;
					case 4:
						temp = str.substring("Color: ".length());
						if (temp.equalsIgnoreCase("blue")) {
							newCard.setC(CCode.BLUE);
						} else if (temp.equalsIgnoreCase("red")) {
							newCard.setC(CCode.RED);
						} else if (temp.equalsIgnoreCase("yellow")) {
							newCard.setC(CCode.YELLOW);
						} else if (temp.equalsIgnoreCase("green")) {
							newCard.setC(CCode.GREEN);
						}
						break;
					case 5:
						temp = str.substring("Cost: ".length());
						if (!temp.equalsIgnoreCase("N/A"))
							newCard.setCost(Integer.parseInt(temp));
						break;
					case 6:
						try {
							temp = str.substring("Trigger: ".length());
							if (!temp.equalsIgnoreCase(""))
								newCard.setTrigger(Trigger.convertString(temp));
						} catch (StringIndexOutOfBoundsException e) {
							newCard.setTrigger(Trigger.NONE);
						}
						break;
					case 7:
						temp = str.substring("Power: ".length());
						if (!temp.equalsIgnoreCase("N/A"))
							newCard.setPower(Integer.parseInt(temp));
						break;
					case 8:
						temp = str.substring("Soul: ".length());
						if (!temp.equalsIgnoreCase("N/A"))
							newCard.setSoul(Integer.parseInt(temp));
						break;
					case 9:
						str = str.replace("::", ":");
						int breaker = -1;
						if (str.contains("_"))
							breaker = str.indexOf("_");
						str = str.replace("_", " ");

						if (breaker > -1) {
							newCard.setTrait1(str.substring(0, breaker - 1)
									.trim());
							newCard.setTrait2(str.substring(breaker).trim());
						} else {
							newCard.setTrait1(str.substring(0, str.length() - 1));
							newCard.setTrait2("N/A");
						}
						break;
					case 10:
						effectStart = true;
						break;
					default:
						if (str.equalsIgnoreCase("Flavor Text:")) {
							effectStart = false;

						} else if (effectStart) {
							newCard.addEffect(str);
						} else {
							newCard.setFlavorText(str);
						}
						break;
					}
					// System.out.println(lineDex + " : " + str);

					lineDex++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String rename(String str) throws IOException {
		String resultString = str.charAt(0) + "";
		String testResultString = str.charAt(0) + "";
		String regex1 = "[^a-zA-Z]*[a-zA-Z0-9\\p{Punct}\\s]*[^a-zA-Z0-9\\p{Punct}[\\s]]+[^a-zA-Z\\s0-9\"]+$";
		String regex2 = "[^a-zA-Z]+[a-zA-Z0-9\\p{Punct}\\s]*[^a-zA-Z0-9[\\s]\"]+$";
		String regex3 = "[a-zA-Z0-9\\p{Punct}\\s]+$";

		if (Pattern.matches(regex1, str) || Pattern.matches(regex2, str)
				|| Pattern.matches(regex3, str) || str.contains("Rock Cannon")) {
			return str;
		}

		do {
			str = str.replace("  ", " ");
		} while (str.contains("  "));

		for (int i = 1; i < str.length(); i++) {

			testResultString += str.charAt(i);
			if (Pattern.matches(regex1, testResultString))
				resultString = testResultString;
		}
		resultString = resultString.trim();
		if (!resultString.equals(str)) {
			nameDict.write("[" + str + "  =>  " + resultString + "]\n");
		}
		return resultString;
	}

	private void close() {
		try {
			nameDict.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// serialize the content into "CardData"
	protected void serializingLists() {

		FileOutputStream fileOutput;
		ObjectOutputStream objectOutput;
		try {
			fileOutput = new FileOutputStream("src/resources/CardDatav2");
			objectOutput = new ObjectOutputStream(fileOutput);

			objectOutput.writeObject(setCards);
			objectOutput.writeObject(allCards);

			objectOutput.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void loadfromTxt() {
		Importer importer = new Importer();
		File file = new File("Data");
		if (file.isDirectory()) {
			String[] fileSet = file.list();
			for (int i = 0; i < fileSet.length; i++) {
				if (fileSet[i].endsWith(".txt")) {
					System.out.println(fileSet[i]);
					importer.scanRaw(new File("Data/" + fileSet[i]));
				}
			}
		}
		System.out.println("# of cards = " + setCards.size());
		System.out.println("# of cards in binder = " + allCards.size());
		Collections.sort(setCards);
		System.out.println(System.getProperty("os.name"));
		importer.serializingLists();
		importer.close();
	}
	
	public static void loadFromSQLiteDB() throws SQLiteException {
		Importer importer = new Importer();
		SQLiteConnection db = new SQLiteConnection(new File("CardData.sqlite"));
		db.open();
		
		HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
		

		SQLiteStatement st = db.prepare("SELECT * from cardtable");
		if (st.step()) {
			for (int i = 0; i < st.columnCount(); ++i) {
				fieldMap.put(st.getColumnName(i), i);
			}
		}
		st.dispose();
		
//		private String id;
//		private String pID;
//		private String cardName;
//		private String cardName_e;
//		private int dupCount = 0;
//		private ArrayList<String> effects;
//		private ArrayList<String> effects_e;
//		private int power;
//		private Trigger trigger;
//		private int level;
//		private int cost;
//		private int soul;
//		private Type t;
//		private CCode c;
//		private String trait1;
//		private String trait2;
//		private String trait1_e;
//		private String trait2_e;
//		private String flavorText;
//		private String flavorText_e;
		
		st = db.prepare("SELECT * from cardtable");
		while (st.step()) {
			Card c = new Card();
			
			c.setCardName(st.columnString(fieldMap.get("name")));
			c.setCardName_e(st.columnString(fieldMap.get("name_e")));
			
			Scanner s = new Scanner(st.columnString(fieldMap.get("rule")));
			while (s.hasNextLine()) {
				c.addEffect(s.nextLine());
			}
			
			s = new Scanner(st.columnString(fieldMap.get("rule_e")));
			while (s.hasNextLine()) {
				c.addEffect_e(s.nextLine());
			}
			
			String temp = st.columnString(fieldMap.get("type"));
			if (temp.equalsIgnoreCase("climax")) {
				c.setT(Type.CLIMAX);
			} else if (temp.equalsIgnoreCase("event")) {
				c.setT(Type.EVENT);
			} else if (temp.equalsIgnoreCase("character")) {
				c.setT(Type.CHARACTER);
			}
			
			c.setTrigger(Trigger.convertString(st.columnString(fieldMap.get("trigger"))));

			if (c.getT() == Type.CLIMAX || c.getT() == Type.EVENT) {
				assert(st.columnString(fieldMap.get("power")).equals("N/A"));
				assert(st.columnString(fieldMap.get("soul")).equals("N/A"));
				c.setPower(-1);
				c.setSoul(-1);
			}
			else {
				c.setPower(st.columnInt(fieldMap.get("power")));
				c.setSoul(st.columnInt(fieldMap.get("soul")));
			}
			
			if (c.getT() == Type.CLIMAX) {
				assert(st.columnString(fieldMap.get("level")).equals("N/A"));
				assert(st.columnString(fieldMap.get("cost")).equals("N/A"));
				c.setLevel(-1);
				c.setCost(-1);
			}
			else {
				c.setLevel(st.columnInt(fieldMap.get("level")));
				c.setCost(st.columnInt(fieldMap.get("cost")));
			}
			
			temp = st.columnString(fieldMap.get("color"));
			if (temp.equalsIgnoreCase("blue")) {
				c.setC(CCode.BLUE);
			} else if (temp.equalsIgnoreCase("red")) {
				c.setC(CCode.RED);
			} else if (temp.equalsIgnoreCase("yellow")) {
				c.setC(CCode.YELLOW);
			} else if (temp.equalsIgnoreCase("green")) {
				c.setC(CCode.GREEN);
			}
			
			c.setTrait1(st.columnString(fieldMap.get("trait1")));
			c.setTrait1_e(st.columnString(fieldMap.get("trait1_e")));
			c.setTrait2(st.columnString(fieldMap.get("trait2")));
			c.setTrait2_e(st.columnString(fieldMap.get("trait2_e")));
			
			c.setFlavorText(st.columnString(fieldMap.get("flavor")));
			c.setFlavorText_e(st.columnString(fieldMap.get("flavor_e")));
			
			String setid = st.columnString(fieldMap.get("setid"));
			String imagefilename = st.columnString(fieldMap.get("imagefilename"));
			c.setImageResource("/resources/FieldImages/" + setid.split("/")[1] + "/" + imagefilename);
			System.out.println(c.getImageResource());
			
			String cid = st.columnString(fieldMap.get("cardid"));
			c.setID((c.isAlternateArt() ? cid + "_alt" : cid));
			
			if (!new File("src" + c.getImageResource()).exists()) {
				// if (newCard.getImage().exists()) {
				// Card c = allCards.put(newCard.getCardName(),
				// newCard);
				c.setImageResource("/resources/FieldImages/cardBack-s.jpg");	
			}
			System.out.println(c.getImageResource());
			Card cc = allCards.put(c.getID(), c);
			if (cc == null) {
				setCards.add(c);

			} else {
				// c.setID(newCard.getID());
				// allCards.put(newCard.getCardName(), c);
				allCards.put(c.getID(), cc);
				// if (!c.getImage().exists()) {
				// setCards.remove(c);
				// setCards.add(newCard);
				// }
			}
		}
		System.out.println("# of cards = " + setCards.size());
		System.out.println("# of cards in binder = " + allCards.size());
		Collections.sort(setCards);
		System.out.println(System.getProperty("os.name"));
		importer.serializingLists();
		importer.close();
//		for (String key : fieldMap.keySet()) {
//			System.out.println(key + " " + fieldMap.get(key));
//		}

	}

	public static void main(String[] args) throws SQLiteException {
		//loadFromTxt();
		loadFromSQLiteDB();

		
		
	}
}
