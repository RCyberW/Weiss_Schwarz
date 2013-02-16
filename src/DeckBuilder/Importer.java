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

import CardAssociation.*;

public class Importer {

	private static ArrayList<Card> setCards = new ArrayList<Card>();
	private static HashMap<String, Card> allCards = new HashMap<String, Card>();
	private Writer nameDict;

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
			Scanner scanner = new Scanner(file);
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
					if (new File("src"+newCard.getImageResource()).exists()) {
//					if (newCard.getImage().exists()) {
						//Card c = allCards.put(newCard.getCardName(), newCard);
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
					}

				} else if (str.isEmpty() && !isStart) {
				} else {

					switch (lineDex) {
						case 0 : //card name
							str = rename(str);
							newCard.setCardName(str);
							str = "Title: " + str;
							break;
						case 1 : //card id
							str = str.replace("  ", " ");
							newCard.setID(str);
							newCard.setImageResource("/resources/FieldImages/" + fileName
									+ "/"
									+ str.replace(" ", "_").replace("/", "-")
									+ ".jpg");
							/*
							newCard.setImage(new File("src/resources/FieldImages/" + fileName
									+ "/"
									+ str.replace(" ", "_").replace("/", "-")
									+ ".jpg"));
									*/
							str = "ID: " + str;
							break;
						case 2 : // card classification
							temp = str.substring("Type: ".length());
							if (temp.equalsIgnoreCase("climax")) {
								newCard.setT(Type.CLIMAX);
							} else if (temp.equalsIgnoreCase("event")) {
								newCard.setT(Type.EVENT);
							} else if (temp.equalsIgnoreCase("character")) {
								newCard.setT(Type.CHARACTER);
							}
							break;
						case 3 :
							temp = str.substring("Level: ".length());
							if (!temp.equalsIgnoreCase("N/A"))
								newCard.setLevel(Integer.parseInt(temp));
							break;
						case 4 :
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
						case 5 :
							temp = str.substring("Cost: ".length());
							if (!temp.equalsIgnoreCase("N/A"))
								newCard.setCost(Integer.parseInt(temp));
							break;
						case 6 :
							try {
								temp = str.substring("Trigger: ".length());
								if (!temp.equalsIgnoreCase(""))
									newCard.setTrigger(Trigger
											.convertString(temp));
							} catch (StringIndexOutOfBoundsException e) {
								newCard.setTrigger(Trigger.NONE);
							}
							break;
						case 7 :
							temp = str.substring("Power: ".length());
							if (!temp.equalsIgnoreCase("N/A"))
								newCard.setPower(Integer.parseInt(temp));
							break;
						case 8 :
							temp = str.substring("Soul: ".length());
							if (!temp.equalsIgnoreCase("N/A"))
								newCard.setSoul(Integer.parseInt(temp));
							break;
						case 9 :
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
								newCard.setTrait1(str.substring(0,
										str.length() - 1));
								newCard.setTrait2("N/A");
							}
							break;
						case 10 :
							effectStart = true;
							break;
						default :
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
		//String regex1 = "[^a-zA-Z]*[a-zA-Z\\-\\s\\'\\&0-9\"\\.\\!\\?]*[^a-zA-Z]*[^a-zA-Z\\s\\'\\&0-9\"\\!\\?]$";
		String regex1 = "[^a-zA-Z]*[a-zA-Z0-9\\p{Punct}\\s]*[^a-zA-Z0-9\\p{Punct}[\\s]]+[^a-zA-Z\\s0-9\"]+$";
		String regex2 = "[^a-zA-Z]+[a-zA-Z0-9\\p{Punct}\\s]*[^a-zA-Z0-9[\\s]\"]+$";
		String regex3 = "[a-zA-Z0-9\\p{Punct}\\s]+$";

		if (Pattern.matches(regex1, str) || Pattern.matches(regex2, str) || Pattern.matches(regex3, str)
				|| str.contains("Rock Cannon")) {
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

	// public ArrayList<Card> getList() {
	// return setCards;
	// }
	//
	// public static Card findCard(String id) {
	// return allCards.get(id);
	// }

	public static void main(String[] args) {
		Importer importer = new Importer();

		// importer.scan(new File("test/ws-AB-W11-withImageName.csv"));

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

		// convert name keys to card id keys
		/*String[] keys = allCards.keySet().toArray(new String[allCards.size()]);
		for (String k : keys) {
			Card c = allCards.get(k);
			Card tempC = allCards.put(c.getID(), c);
			if (tempC != null) {
				System.out.printf("ORIGINAL: ID = %s name = %s\n",
						tempC.getID(), tempC.getCardName());
				System.out.printf("REPEAT  : ID = %s name = %s\n", c.getID(),
						c.getCardName());
			}
			allCards.remove(k);
		}
		System.out.println(allCards.size());

		Iterator<Card> cardIte = allCards.values().iterator();

		while (cardIte.hasNext()) {
			Card card = (Card) cardIte.next();
			setCards.add(card);
		}*/
		System.out.println("# of cards = " + setCards.size());
		System.out.println("# of cards in binder = " + allCards.size());
		Collections.sort(setCards);

		/*
		 * String testStr = "Ã§Â¸ÂºÃ¯Â¿Â½Ã¢â€“Â½Ã§Â¸ÂºÃ¥ï¿½Â¥Ã¯Â½Â¤Ã¯Â½Â¢Ã§Â¸ÂºÃ¯Â½Â§Ã©Å¡â€¢Ã¤Â¹ï¿½Ã¢â€”â€ Ã¨Å“Ë†Ã§â€�Â»Ã¥â€¹Â¹The Scene To Be Seen In A Dream Someday";
		 * System.out.println("testStr = " + testStr); for (int i = 1; i <
		 * testStr.length(); i++) { boolean isCurrentAlph =
		 * ((int)(testStr.charAt(i)) <= 127); boolean isPrevNAlph =
		 * ((int)(testStr.charAt(i - 1)) > 127); boolean testBool =
		 * isCurrentAlph && isPrevNAlph; System.out.println(testBool + " curr("
		 * + isCurrentAlph + ") = " + testStr.charAt(i) + " prev(" + isPrevNAlph
		 * + ") = " + testStr.charAt(i - 1)); }
		 */

		importer.serializingLists();
		importer.close();
		// try {
		// Writer errReport = new BufferedWriter(new FileWriter(new File(
		// "errReport.txt")));
		// Scanner scan1 = new Scanner(new File("nameDict1.txt"));
		// Scanner scan2 = new Scanner(new File("nameDict2.txt"));
		// String str1 = "";
		// String str2 = "";
		// boolean equal = true;
		// while (scan1.hasNext() && scan2.hasNext()) {
		// System.out.println("Process...");
		// if (equal)
		// str1 = scan1.nextLine();
		// str2 = scan2.nextLine();
		// equal = str1.equals(str2);
		// if (!equal) {
		// errReport.write(str2 + "\n");
		// }
		// }
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
	}
}
