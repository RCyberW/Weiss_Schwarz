package gamePlay;

import java.util.ArrayList;

/**
 * @author Frank Chen
 * @version 0.1
 * @since 2014-01-01
 */
public enum Keyword {
	/* Field zone names */
	LIBRARY("library", "deck"),
	TRASH("trash", "grave", "waiting_room"),
	MEMORY("out_of_game", "memory", "backyard"),
	HAND("hand"),
	LIFE("life"),
	
	LAND("land, resource"), // Z/X and magic
	CHARGE("charge"), // Z/X
	CLIMAX("climax"), // W/S
	
	FRONT_ROW_1("front_1"),
	FRONT_ROW_2("front_2"),
	FRONT_ROW_3("front_3"),
	FRONT_ROW_4("front_4"),
	FRONT_ROW_5("front_5"),
	
	BACK_ROW_1("back_1"),
	BACK_ROW_2("back_2"),
	BACK_ROW_3("back_3"),
	BACK_ROW_4("back_4"),
	BACK_ROW_5("back_5"),
	
	/* Game phases */
	UNTAP_PHASE("untap_phase"),
	DRAW_PHASE("draw_phase"),
	RESOURCE_PHASE("resource_phase"), // Z/X
	IGNITION_PHASE("ignition_phase"), // Z/X
	MAIN_PHASE("main_phase"),
	CLIMAX_PHASE("climax_phase"), // W/S
	ATTACK_PHASE("attack_phase"), // W/S
	END_PHASE("end_phase"),
	
	
	/* Game actions */
	DECK_TOP("top_of_deck"),
	DECK_BOTTOM("bottom_of_deck"),
	DECK_SEARCH("search_deck"),
	DECK_SHUFFLE("shuffle_deck"),
	DECK_DRAW("draw"),
	
	TRASH_SEARCH("search_trash", "search_grave", "search_waiting_room"),
	TRASH_SALVAGE("return_from_waiting_room"),
	
	ATTACK("attack"),
	
	UNTAP("untap", "reboot", "stand"),
	TAP("rest", "tap"),
	RESOURCE("resource"), // Z/X
	IGNITION("ignition"), // Z/X
	COUNTER("counter"), 
	DISCARD("discard")
	
	/* Game specific terms */
	;
	
	private ArrayList<String> commonTerms;
	private String selectedTerm;
	
	/**
	 * @param keys the possible terms used across different card games
	 */
	Keyword(String ...keys ) {
		commonTerms = new ArrayList<String>();
		for (String key : keys) {
			commonTerms.add(key);
		}
	}

	/**
	 * @param term the selected terminology this game uses
	 */
	public void setTerm(String term) {
		if (commonTerms.contains(term)) {
			setSelectedTerm(term);
		}
	}
	
	/**
	 * @return the String representation of the keyword
	 */
	public String toString() {
		String terms = "";
		for (String term : commonTerms) {
			terms += term + " ";
		}
		return terms.trim().replace(" ", "/");
	}

	/**
	 * @return the selectedTerm
	 */
	public String getSelectedTerm() {
		return selectedTerm;
	}

	/**
	 * @param selectedTerm the selectedTerm to set
	 */
	public void setSelectedTerm(String selectedTerm) {
		this.selectedTerm = selectedTerm;
	}
}
