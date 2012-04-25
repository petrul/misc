package gest;

public enum Compass {
	
	NORTH(1, "N"),
	
	NORTH_EAST(2, "NE"),
	
	EAST(3, "E"),
	
	SOUTH_EAST(4, "SE"),
	
	SOUTH(5, "S"),
	
	SOUTH_WEST(6, "SW"),
	
	WEST(7, "W"),
	
	NORTH_WEST(8, "NW");
	
	protected int intCode;
	
	protected String stringCode;
	
	private Compass(int intCode, String stringCode) {
		this.intCode = intCode;
		this.stringCode = stringCode;
	}

	/*
	 * Program tails to append to 'NORTH' in order to get all other directions N, NE, E,..., W, NW
	 * Conditional complexity is the size of this tail. E.g. CC(NE, N) = 2 = len('RR') 
	 */
	static String[] PROGRAM_TAILS_FROM_NORTH = { "", "R", "RR", "IL", "I", "IR", "LL", "L" };
	static int PROGRAM_TAILS_FROM_NORTH_LENGTH = PROGRAM_TAILS_FROM_NORTH.length;
	
	/**
	 * cc(y|x) is the conditional complexity of producing y given x.
	 */
	public static int cc(Compass dest, Compass source) {
		return programTail(dest, source).length();
	}

	public static String programTail(Compass dest, Compass source) {
		int index = dest.intCode - source.intCode;
		if (index < 0) index += PROGRAM_TAILS_FROM_NORTH_LENGTH;
		return PROGRAM_TAILS_FROM_NORTH[index];
	}

	public int length() {
		return this.stringCode.length();
	}
	
//	static ALL = {NORTH, NORTH_EAST, EAST, SOUTH_EAST, 
//		SOUTH, SOUTH_WEST, WEST, NORTH_WEST }; 
//	}
//	public static Compass[] all() {
		
}
