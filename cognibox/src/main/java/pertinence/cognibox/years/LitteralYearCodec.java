package pertinence.cognibox.years;

import java.util.HashMap;
import java.util.Map;

import pertinence.cognibox.huffman.Code;
import pertinence.cognibox.huffman.HuffmanTree;

public class LitteralYearCodec implements YearCodec {
	
	private HuffmanTree landmarks;
	Map<Character, String> digitCodes; 
	
	public LitteralYearCodec(HuffmanTree landmarks) {
		this.landmarks = landmarks;
		this.init();
	}
	
	
	protected void init() {
		this.digitCodes = new HashMap<Character, String>();
		this.digitCodes.put('0', "0000000000000000000");
		this.digitCodes.put('1', "1111111111111111111");
		this.digitCodes.put('2', "2222222222222222222");
		this.digitCodes.put('3', "3333333333333333333");
		this.digitCodes.put('4', "4444444444444444444");
		this.digitCodes.put('5', "5555555555555555555");
		this.digitCodes.put('6', "6666666666666666666");
		this.digitCodes.put('7', "7777777777777777777");
		this.digitCodes.put('8', "8888888888888888888");
		this.digitCodes.put('9', "9999999999999999999");
	}


	/**
	 * 
	 */
	public String[] encode(int year) {
		StringBuilder sb = new StringBuilder();
		String yearAsString = new Integer(year).toString();
		int crtIndex = 0;
		Character lastChar = null;
		
		try {
			String firstTwoChars = yearAsString.substring(0, 2);
			String centuryCode = this.landmarks.encode(firstTwoChars).toString();
			sb.append("P").append(centuryCode).append(".");
			crtIndex += 2;
			lastChar = firstTwoChars.charAt(1);
		} catch (IllegalArgumentException e) {
			// no landmark for this century
		}
		
		for (int i = crtIndex; i < yearAsString.length(); i++) {
			char charAt = yearAsString.charAt(i);
			if (lastChar != null && charAt == lastChar) {
				sb.append('c');
				continue;
			}
			String longerCode = this.digitCodes.get(charAt);
			sb.append('L').append(longerCode).append('.');
			lastChar = charAt;
		}
		String[] res = new String[1];
		res[0] = sb.toString();
		return res;
	}


	/**
	 * 
	 */
	public int decode(String code) {
		int idx = 0;
		int codeLength = code.length();
		StringBuilder result = new StringBuilder();
		StringAndIndex si = new StringAndIndex(code, idx);
		
		while (idx < codeLength) {
			
			readCurrentToken(si, result);
			idx = si.idx;
		}
		
		return Integer.parseInt(result.toString());
	}


	private void readCurrentToken(StringAndIndex str_idx, StringBuilder result) {
		
		//int idx = str_idx.idx;
		String code = str_idx.str;
		char firstChar = code.charAt(str_idx.idx++);
		
		char crtDigit;
		
		switch (firstChar) {
		case 'L':
			crtDigit = readLooooongDigit(str_idx);
			result.append(crtDigit);
			break;

		case 'P':
			String seq = readSequence(str_idx);
			String decodedString = (String) this.landmarks.decode(new Code(seq));
			str_idx.lastChar = decodedString.charAt(decodedString.length() - 1);
			result.append(decodedString);
			break;
			
		case 'c':
			crtDigit = str_idx.lastChar;
			result.append(crtDigit);
			break;
			
		default:
			throw new IllegalStateException("problem parsing at position " + str_idx.idx);
		}
	}


	private String readSequence(StringAndIndex strIdx) {
		StringBuilder sb = new StringBuilder();
		String str = strIdx.str;
		char crtChar;
		while ((crtChar = str.charAt(strIdx.idx++)) != '.') {
			sb.append(crtChar);
		}
		return sb.toString();
	}


	private char readLooooongDigit(StringAndIndex strIdx) {
		String str = strIdx.str;
		int idx = strIdx.idx;
		
		char c_result = str.charAt(idx);
		while (str.charAt(idx++) != '.') { /* skip to point */ }
		
		strIdx.idx = idx;
		strIdx.lastChar = c_result;
		return c_result;
	}
	
}


class StringAndIndex {
	
	public StringAndIndex(String str, int idx) {
		this.str = str;
		this.idx = idx;
	}
	
	Character lastChar = null; 
	String str;
	int idx;
}