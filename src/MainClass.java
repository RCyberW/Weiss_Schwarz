import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {
		System.out.println("Hello World");
		try {
			Scanner scan1 = new Scanner(new File("nameDict1.txt"));
			Scanner scan2 = new Scanner(new File("nameDict2.txt"));

			while (scan1.hasNext() && scan2.hasNext()) {
				System.out.println("Process...");
				String str1 = scan1.nextLine();
				String str2 = scan2.nextLine();
				do {
					if (!str1.equals(str2)) {
						System.out.println(str2);
					}
					str2 = scan2.nextLine();
				} while (!str1.equals(str2) && scan2.hasNext());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}