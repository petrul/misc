package pertinence.cognibox.huffman;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * the {@link BitSet} class does not keep track of the bit set length
 * properly so we do it here.
 * 
 * @author dadi
 */
public class Code {
	
	BitSet 	bitset;
	int 	length;
	
	/**
	 * the current position of a machine that may be trying to decode this string
	 */
	int cursor = 0;


	public Code() {
		this.bitset = new BitSet();
		this.length = 0;
	}
	
	
	public Code(int i) {
		int crtLen = 0;
		
		BitSet bs = new BitSet();
		int crtRest = i;
		while (crtRest > 0) {
			int crtBit = crtRest % 2;
			crtRest = crtRest / 2;
			bs.set(crtLen, crtBit == 1);
			crtLen++;
		}
		this.bitset = bs;
		this.length = crtLen;
		this.reverse();
	}

	
	public Code(String bitsetAsString) {
		this.length = bitsetAsString.length();
		BitSet bs = new BitSet();
		for (int i = 0; i < this.length; i++) {
			char c = bitsetAsString.charAt(i);
			assertTrue (c == '0' || c == '1');
			if (c == '1') bs.set(i);
		}
		this.bitset = bs;
	}
	
	
	public Code(BitSet bitset, int length) {
		super();
		this.bitset = bitset;
		this.length = length;
	}
	
	
	public void reverse() {
		BitSet bs = reverseBitset(this.bitset, this.length);
		this.bitset = bs;
	}
	
	
	public int nextBit() {
		if (this.cursor >= this.length)
			throw new IndexOutOfBoundsException("reached end of bitset : " + this.length);
		return this.bitset.get(this.cursor++) ? 1 : 0;
	}
	
	
	public Code toPrefixFree(int level) {
		return buildPrefixFree(this, level);
	}

	/**
	 * interprets the next bits of this code as a huffman code leading from its tree to one
	 * of its leaves.
	 */
	public Code nextHuffmanPrefixFree(HuffmanTree huffmanTree) {
		int from = this.cursor;
		huffmanTree.decode(this);
		int to = this.cursor;
		BitSet pfbs = this.bitset.get(from, to);
		return new Code(pfbs, to - from);
	}
	
	public Code nextPrefixFree(int level) {
		CodeAndLength result = fromPrefixFree(this.bitset, this.cursor, level);
		this.cursor += result.originalLength;
		return result.code;
	}
	
	
	public static CodeAndLength fromPrefixFree(Code c, int level) {
		return fromPrefixFree(c.getBitset(), 0, level);
	}
	
	
	public static CodeAndLength fromPrefixFree(BitSet bs, int level) {
		return fromPrefixFree(bs, 0, level);
	}
	
	
	public static CodeAndLength fromPrefixFree(BitSet bs, int fromPosition, int level) {
		// read the first sequence of ones
		int crtLength = 0;
		int crtBit = fromPosition;
		while (bs.get(crtBit)) {
			crtBit++; crtLength++; 
		}
		
		// now we're positioned on the final 0
		assertFalse(bs.get(crtBit));
		
		crtBit++;
		
		while (level-- > 0) {
			int newCrtLength = toInt(bs, crtBit, crtBit + crtLength);
			crtBit += crtLength; 
			crtLength = newCrtLength;
		}
		
		BitSet resultingBs = bs.get(crtBit, crtBit + crtLength);
		Code resultingCode = new Code(resultingBs, crtLength);
		crtBit += crtLength;
		return new CodeAndLength(resultingCode, crtBit - fromPosition);
	}
	
	
	public int toInt() {
		int result = toInt(this.bitset, 0, this.length);
		this.cursor = this.length;
		return result;
	}

	public int toInt(int from, int to) {
		int result = toInt(this.bitset, from, to);
		this.cursor = to;
		return result;
	}
	
	/**
	 * @return the part from the 'form' parameter, inclusive, until the 'to' parameter, exclusive 
	 */
	public static int toInt(BitSet bs, int from, int to) {
		int result = 0;
		
		for (int i = from; i < to; i++) {
			int crtBit = bs.get(i) ? 1 : 0;
			result = result << 1;
			result |= crtBit;
		}
		return result;
	}
	
	
	/**
	 * @return a prefix-free version of the code in argument
	 */
	public static Code buildPrefixFree(Code code, int level) {
		Code prefix = encodeLength(code.getLength(), level);
		return concatenate(prefix, code);
	}
	
	
	private static Code encodeLength(int length, int level) {
		if (level == 0) {
			BitSet bs = new BitSet();
			int i = 0;
			for (; i < length; i++) {
				bs.set(i);
			}
			bs.set(i, false);
			return new Code(bs, length + 1);
		} else {
			Code c = new Code(length);
			Code prefix = encodeLength(c.getLength(), level - 1);
			return concatenate(prefix, c);
		}
	}
	
	
	/**
	 * 
	 */
	public static Code concatenate(Code c1, Code c2) {
		BitSet bs = new BitSet();
		for (int i = 0; i < c1.getLength(); i++) {
			bs.set(i, c1.getBitset().get(i));
		}
		
		for (int i = 0; i < c2.getLength(); i++) {
			bs.set(c1.getLength() + i, c2.getBitset().get(i));
		}
		return new Code(bs, c1.getLength() + c2.getLength());
	}
	
	
	public void append(Code c) {
		Code resulting = concatenate(this, c);
		
		this.bitset = resulting.bitset;
		this.length = resulting.length;
		this.cursor = resulting.cursor;
	}
	
	public static BitSet reverseBitset(BitSet source, int len) {
		BitSet bs = new BitSet();
		for (int i = 0; i < len; i++) {
			bs.set(i, source.get(len - i - 1));
		}
		return bs;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(bitset.get(i) ? '1' : '0');
		}
		return sb.toString();
	}
	
	/* accessors */
	public BitSet getBitset() {
		return bitset;
	}

	public void setBitset(BitSet bitset) {
		this.bitset = bitset;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	/**
	 * adds the binary encoding of the given number in the given width (not prefix-free)
	 */
	public void addFixedWidthInt(int number, int width) {
		int crtNr = number;
		this.cursor += width - 1;
		
		for (int i = 0; i < width; i++) {
			int crtBit = crtNr % 2;
			crtNr = crtNr / 2;
			this.bitset.set(this.cursor--, crtBit == 1);
		}
		
		this.length += width;
		this.cursor += width + 1;
	}


	public void addPrefixFreeInt(int number, int level) {
		Code notPrefixFree = new Code(number);
		Code prefixFree = buildPrefixFree(notPrefixFree, level);
		this.append(prefixFree);
		this.cursor += prefixFree.length;
	}


	public void resetCursor() {
		this.cursor = 0;
	}

}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      

/**
 * 
 * when decoding a prefix-free sequence we may need the original length; the resulting
 * Code only contains the length of the decoded sequence; this structure is here in order to
 * remember the original length too which is needed for example in order to increase the cursor position 
 * by the right amount.
 */
class CodeAndLength {
	
	public CodeAndLength(Code code, int originalLength) {
		this.code = code;
		this.originalLength = originalLength;
	}
	
	Code code;
	int originalLength;
}