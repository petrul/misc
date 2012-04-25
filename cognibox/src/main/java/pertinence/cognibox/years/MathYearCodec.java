package pertinence.cognibox.years;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import pertinence.cognibox.huffman.Code;
import pertinence.cognibox.huffman.HuffmanNode;
import pertinence.cognibox.huffman.HuffmanTree;

/**
 * Codec for mathematical (number-wise, as opposed to litteral {@link LitteralYearCodec}) encoding of years.
 *  
 * @author dadi
 * @see LitteralYearCodec
 */
public class MathYearCodec implements YearCodec {

	HuffmanTree landmarks;
	
	int 		effortBarrier;
	
	/**
	 * 
	 * @param landmarks
	 *            a set of landmarks, aka concepts, that can be used in order to
	 *            better encode data
	 * @param effortBarrier
	 *            when multiple encodings are possible, this sets the number of
	 *            extrabits, relative to the minimum encoding, that are allowed,
	 *            when looking for alternative encodings. For example, if zero,
	 *            only minimum encodings are returned; if 1, then minimum
	 *            encondings and almost-minimum (with 1 extra bit) may be
	 *            returned; and so on.
	 */
	public MathYearCodec(HuffmanTree landmarks, int effortBarrier) {
		this.landmarks = landmarks;
		
		if (this.landmarks.getData().size() < 1)
			throw new IllegalArgumentException("must provide at least one landmark in the huffman tree");

		this.effortBarrier = effortBarrier;
	}
	

	@Override
	public String[] encode(int year) {
		// find the closest landmark
		Set<HuffmanNode> leaves = this.landmarks.getLeaves();
		
		Set<LandmarkAndDistance> yearsOrderedByDistance = new TreeSet<LandmarkAndDistance>();
		
		for (HuffmanNode node: leaves) {
			Integer remarkable_year = (Integer) node.getLabel();
			int dist = year - remarkable_year;
			yearsOrderedByDistance.add(new LandmarkAndDistance(remarkable_year, dist));
		}
		
		List<String> encodings = new ArrayList<String>();
		for (LandmarkAndDistance ld : yearsOrderedByDistance) {
			if (Math.abs(ld.distance) > this.effortBarrier)
				break;
			
			// if we're here, we have a candidate for encoding
			StringBuilder code = new StringBuilder();
			code.append('P').append(this.landmarks.encode(ld.year).toString()).append('.');
			char charToAdd = ld.distance > 0 ? '+' : '-' ; // 'increase' or 'decrease'
			for (int i = 0 ; i < Math.abs(ld.distance); i++) {
				code.append(charToAdd);
			}
//			code.append('.');
			encodings.add(code.toString());
		}
		
		return (String[]) encodings.toArray(new String[encodings.size()]);
	}

	
	/**
	 * 
	 */
	@Override
	public int decode(String code) {
		int idx = 0;
		int codeLength = code.length();
		ValueBasket si = new ValueBasket(code, idx);
		
		while (idx < codeLength) {
			readCurrentToken(si);
			idx = si.idx;
		}
		
		return si.decodedYear;
	}


	private void readCurrentToken(ValueBasket vars) {

		String code = vars.str;
		char firstChar = code.charAt(vars.idx++);

		switch (firstChar) {
		case 'P':
			String seq = readSequence(vars);
			Integer decodedInt = (Integer) this.landmarks.decode(new Code(seq));
			vars.decodedYear = decodedInt;
			break;

		case '+':
			vars.decodedYear++;
			break;

		case '-':
			vars.decodedYear--;
			break;

		default:
			throw new IllegalStateException("problem parsing at position " + vars.idx + ", unexpected character '" + firstChar + "'");
		}
	}


	private String readSequence(ValueBasket strIdx) {
		StringBuilder sb = new StringBuilder();
		String str = strIdx.str;
		char crtChar;
		while ((crtChar = str.charAt(strIdx.idx++)) != '.') {
			sb.append(crtChar);
		}
		return sb.toString();
	}

	
	static Logger LOG = Logger.getLogger(MathYearCodec.class);
}


/**
 * 
 */
class LandmarkAndDistance implements Comparable<LandmarkAndDistance> {
	
	int year;
	int distance;
	
	public LandmarkAndDistance(int year, int distance) {
		super();
		this.year = year;
		this.distance = distance;
	}
	
	@Override
	public int compareTo(LandmarkAndDistance that) {
		int this_dist_abs = Math.abs(this.distance);
		int that_dist_abs = Math.abs(that.distance);
		
		int diff = this_dist_abs - that_dist_abs;
		if (diff != 0)
			return diff;
		else
			return this.year - that.year;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}


/**
 * 
 */
class ValueBasket {
	
	public ValueBasket(String str, int idx) {
		this.str = str;
		this.idx = idx;
	}
	
	String str;
	int idx;
	int decodedYear = 0;
}
