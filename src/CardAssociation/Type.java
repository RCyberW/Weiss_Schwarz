/**
 * @file State.java
 * @author Jia Chen
 * @date Sept 06, 2011
 * @description 
 * 		Type.java is an enumeration of the different card type
 */

package CardAssociation;

public enum Type {
	ALL("All"), CLIMAX("Climax"), CHARACTER("Character"), EVENT("Event");

	String s;

	Type(String type) {
		s = type;
	}

	public String toString() {
		return s;
	}
}
