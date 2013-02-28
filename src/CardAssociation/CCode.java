/**
 * @file CCode.java
 * @author Jia Chen
 * @date Sept 06, 2011
 * @description 
 * 		CCode.java is an enumeration of the different card colors
 */

package CardAssociation;

public enum CCode {
	ALL("All"), RED("Red"), BLUE("Blue"), YELLOW("Yellow"), GREEN("Green");
	
	String s;

	CCode(String trigger) {
        s = trigger;
    }

    public String toString() {
        return s;
    }
    
    public int getNumericCode() {
    	switch(this) {
	    	case ALL:
	    		return 0;
	    	case RED:
	    		return 1;
	    	case BLUE:
	    		return 2;
	    	case YELLOW:
	    		return 3;
	    	case GREEN:
	    		return 4;
	    	default:
    			return 255;
    	}
    }
    
}
