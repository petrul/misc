package compressor;

public class RepetitionEncoder {

	/**
	 * given data x, find program p, so that M(p) = x
	 */
	String encode(String x) {
		
		StringBuilder result = new StringBuilder();
		int crtRunLength = 1;
		int crtIndex = 0;
		
		while (crtIndex < x.length() - 1) {
			char crtChar = x.charAt(crtIndex);
			
			while (crtIndex < (x.length() - 1) && x.charAt(++crtIndex) == crtChar)
				crtRunLength++;
			
			
			{ // flush
				if (crtRunLength > 2) {
					if (crtRunLength < 10) {
						result.append("*" + crtRunLength + crtChar);
					} else
						result.append("*(" + crtRunLength + ")" + crtChar);
				} else {
					for (int i = 0; i < crtRunLength; i++)
						result.append(crtChar);
				}
				crtRunLength = 0;

			}
			crtRunLength = 1;
		}
		result.append('.');
		return result.toString();
	}
	
	protected void flush() {
	}
}
