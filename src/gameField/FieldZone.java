package gameField;

public enum FieldZone {
	FRONT1("FRONT1"), 
	FRONT2("FRONT2"), 
	FRONT3("FRONT3"), 
	BACK1("BACK1"), 
	BACK2("BACK2"), 
	MEMORY("MEMORY"), 
	WAITING("WAITING"), 
	CLOCK("CLOCK"), 
	LEVEL("LEVEL"), 
	STOCK("STOCK"), 
	RESOLUTION("RESOLUTION"), 
	DECK("DECK"), 
	CLIMAX("CLIMAX"), 
	HAND("HAND");
	
	String s;

	FieldZone(String zone) {
		s = zone;
	}

	public String toString() {
		return s;
	}
}
