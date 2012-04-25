package pertinence.cognibox.years;

/**
 * 
 */
public interface YearCodec {
	
	String[] encode(int year);
	
	int decode(String code);
	
}