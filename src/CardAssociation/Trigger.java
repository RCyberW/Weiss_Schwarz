/**
 * @file Trigger.java
 * @author William Lam
 * @date Jan 6, 2012
 * @description 
 * 		Trigger.java is an enumeration of the different triggers
 */

package CardAssociation;

public enum Trigger {
	ALL("All"), NONE("None"), SOUL("Soul"), DUALSOUL("2 Soul"), SOULWIND("Soul Bounce"), SOULFLAME(
			"Soul Burn"), GATE("Gate"), TREASURE("Treasure"), BOOK("Book");

	String s;

	Trigger(String trigger) {
		s = trigger;
	}

	public String toString() {
		return s;
	}

	public static Trigger convertString(String s) {
		if (s.equals("0")) {
			return NONE;
		} else if (s.equals("1")) {
			return SOUL;
		} else if (s.equals("2")) {
			return DUALSOUL;
		} else if (s.equals("1W")) {
			return SOULWIND;
		} else if (s.equals("1F")) {
			return SOULFLAME;
		} else if (s.equals("D")) {
			return GATE;
		} else if (s.equals("G")) {
			return TREASURE;
		} else if (s.equals("B")) {
			return BOOK;
		} else {
			return null;
		}
	}
}
