package pertinence.cognibox.mains;

import org.apache.log4j.Logger;

public class Play implements Runnable {
	
	public static void main(String[] args) {
		new Play().run();
	}

	public void run() {
//		Integer best_landmark = null;
//		int minimum_code_length_sum = Integer.MAX_VALUE
//
//		year_range = 1000..2100
//		for (candidate_landmark in year_range) {
//			
//			litt_landmarks = [:]
//			litt_hufftree = new HuffmanTree(litt_landmarks)
//			LitteralYearCodec litt_codec = new LitteralYearCodec(litt_hufftree)
//
//			long code_length_sum = 0
//			for (year in year_range) {
//				litt_codes = litt_codec.encode(year)
//				//math_codes = math_codec.encode(year)
//				
//				for (code: litt_codes) {
//					code_length_sum += code.length()
//				}
//			}
//			
//			if (minimum_code_length_sum < code_length_sum) {
//				minimum_code_length_sum = code_length_sum
//				best_landmark = candidate_landmark
//			}
//			
//			println "$year : $litt_codes / $math_codes"
			
	}
	
	static Logger LOG = Logger.getLogger(Play.class);
}
