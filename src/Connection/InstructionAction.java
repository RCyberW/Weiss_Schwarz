package Connection;

public enum InstructionAction {
	// Alice "played card uuid1 to (zone)"
	// Alice "salvaged card uuid1 to (hand/deck)"
	// Alice "drew card uuid1 (hidden)"
	// Alice "searched for card uuid1 add to (zone)"
	// Alice "stocked card uuid1 (hidden/shown)"
	// Alice "send card uuid1 to (zone)"
	
	PLAY, // hand to stage
	SALVAGE, // waiting room to hand
	DRAW, // deck to hand
	SEARCH, // deck to hand
	STOCK, // deck to stock
	MOVE, // source to target
	BRAINSTORM, // deck to waiting room
	RECOLLECTION, // uh...nothing to nothing
	MEMORY, // field to memory
	ASSIST, // uh...just plays out
	BACKUP, // hand to waiting room
	BODYGUARD, // uh...another one of keywords
	ALARM, // nothing again!
	BOND, // waiting room to hand
	EXPERIENCE, // just there...
	ENCORE, // stock to waiting room
	DROP // hand to waiting room
}
