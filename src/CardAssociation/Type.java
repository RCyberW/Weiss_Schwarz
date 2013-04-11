/**
 * @file State.java
 * @author Jia Chen
 * @date Sept 06, 2011
 * @description 
 * 		Type.java is an enumeration of the different card type
 */

package CardAssociation;

public enum Type {
	ALL(""), CLIMAX("Climax"), CHARACTER("Character"), EVENT("Event");

	String s;

	Type(String type) {
		s = type;
	}

	public String toString() {
		return s;
	}
	
	public int getNumericCode() {
    	switch(this) {
	    	case ALL:
	    		return 0;
	    	case CLIMAX:
	    		return 1;
	    	case CHARACTER:
	    		return 2;
	    	case EVENT:
	    		return 3;
	    	default:
    			return 255;
    	}
    }
}
