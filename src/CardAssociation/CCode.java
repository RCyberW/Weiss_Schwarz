/**
 * @file CCode.java
 * @author Jia Chen
 * @date Sept 06, 2011
 * @description 
 * 		CCode.java is an enumeration of the different card colors
 */

package CardAssociation;

public enum CCode {
	ALL(""), RED("Red"), BLUE("Blue"), YELLOW("Yellow"), GREEN("Green");
	
	String s;

	CCode(String color) {
        s = color;
    }

    public String toString() {
        return s;
    }
    
}
