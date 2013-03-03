package Game;

public enum Phase {

	STAND_PHASE("Stand Phase"), 
	DRAW_PHASE("Draw Phase"), 
	CLOCK_PHASE("Clock Phase"), 
	MAIN_PHASE("Main Phase"), 
	CLIMAX_PHASE("Climax Phase"), 
	ATTACK_PHASE("Attack Phase"), 
	END_PHASE("End Phase");

	String s;

	Phase(String phase) {
		s = phase;
	}

	public String toString() {
		return s;
	}
}
