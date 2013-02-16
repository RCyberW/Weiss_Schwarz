package DeckBuilder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import CardAssociation.CCode;
import CardAssociation.Card;
import CardAssociation.Trigger;
import CardAssociation.Type;

public class TestMain {

	private static ArrayList<Card> setCards = new ArrayList<Card>();
	private static ArrayList<Card> deckCards = new ArrayList<Card>();
	private static boolean debug = false;

	public void scanRaw(File file) {
		try {
			Scanner scanner = new Scanner(file);
			if (debug)
				System.out.println(file.exists() + " : " + file.getName());
			int lineDex = 0;
			boolean isStart = false;
			// scanner.nextLine();
			Card newCard = new Card();
			String fileName = file.getName().replace(".txt", "");
			// String index = "";
			String temp = "";
			boolean effectStart = true;

			while (scanner.hasNext()) {

				String str = scanner.nextLine().trim();
				// Scanner lineScan = new Scanner(str);
				if (str.isEmpty() && isStart) {
					if (debug)
						System.out.println("<START OF CARD>");
					isStart = false;
					lineDex = 0;
					newCard = new Card();
				} else if (str.equals("==========================")) {
					if (debug)
						System.out.println("<END OF CARD>");
					isStart = true;
					if (newCard.getImage() != null) {
						setCards.add(newCard);
					}

				} else if (str.isEmpty() && !isStart) {
				} else {

					switch (lineDex) {
					case 0:
						int i;
						for (i = 1; i < str.length(); i++) {
							if (Character.isLowerCase(str.charAt(i))
									|| Character.isUpperCase(str.charAt(i))
									|| str.charAt(i) == '"') {
								break;
							}
						}
						if (i < str.length()) {
							str = str.replace(str.charAt(i) + "",
									" " + str.charAt(i));
						}
						str = str.replace("\t", " ");
						str = str.replace("  ", " ");
						// index = str;
						newCard.setCardName(str);
						str = "Title: " + str;
						break;
					case 1:
						str = str.replace("  ", " ");
						newCard.setID(str);
						File imageFile = new File("FieldImages/" + fileName
								+ "/" + str.replace(" ", "_").replace("/", "-")
								+ ".jpg");
						if (debug)
							System.out.println(imageFile.exists() + ":"
									+ imageFile.getAbsolutePath());
						if (imageFile.exists())
							newCard.setImage(imageFile);
						str = "ID: " + str;
						break;
					case 2:
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

	public static void main(String[] args) {

		JFrame frame = new JFrame("test window");
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}
		});

		TestMain tmain = new TestMain();

		tmain.scanRaw(new File("Data/GC-S16.txt"));

		Box boxH1 = Box.createVerticalBox();
		Box boxV1 = Box.createHorizontalBox();

		for (int i = 0; i < setCards.size(); i++) {
			Card cv2 = setCards.get(i);
			if (i > 0 && i % 10 == 0) {
				boxH1.add(boxV1);
				boxV1 = Box.createHorizontalBox();
			}
			if (cv2 != null) {
				if (debug)
					System.out.println(cv2.getCardName());
				//cv2.addMouseListener(cv2);
				//cv2.setTransferHandler(new DragHandler(cv2));
				//boxV1.add(cv2);
			}
		}

		Box boxH2 = Box.createVerticalBox();
		Box boxV2 = Box.createHorizontalBox();

		for (int i = 0; i < 50; i++) {
			Card cv2 = new Card(i + "", "test" + i);
			deckCards.add(cv2);
			if (i > 0 && i % 10 == 0) {
				boxH2.add(boxV2);
				boxV2 = Box.createHorizontalBox();
			}
			if (cv2 != null) {
				if (debug)
					System.out.println(cv2.getCardName());
				//cv2.addMouseListener(cv2);
				//cv2.setTransferHandler(new DropHandler(null));
				//boxV2.add(cv2);
			}
		}
		
		boxH2.add(boxV2);

		JButton listButton = new JButton("LIST");
		listButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < deckCards.size(); i++) {
					System.out.println(deckCards.get(i).getCardName());
				}
				
			}

		});

		boxH2.add(listButton);
		
		frame.add(boxH1, BorderLayout.WEST);
		frame.add(boxH2, BorderLayout.EAST);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
