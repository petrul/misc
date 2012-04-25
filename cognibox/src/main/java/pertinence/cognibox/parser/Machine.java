package pertinence.cognibox.parser;

import java.util.BitSet;
import java.util.Set;

import org.apache.log4j.Logger;

import static org.junit.Assert.*;

import pertinence.cognibox.huffman.Code;
import pertinence.cognibox.huffman.Data;
import pertinence.cognibox.huffman.HuffmanNode;
import pertinence.cognibox.huffman.HuffmanTree;

public class Machine {

	public final static int OP_PLUS 		= 0x2;
	public final static int OP_MINUS 	= 0x1;

	public final static int PREFIX_FREE_ENCODINGLEVEL = 1;

	HuffmanTree landmarksEncoded;

	
	public Machine(Data landmarks) {
		this.landmarksEncoded = new HuffmanTree(landmarks);
	}

	public Machine(HuffmanTree landmarks) {
		this.landmarksEncoded = landmarks;
	}
	
	/**
	 * decode: first two bits are operation code: 01 - minus 10 - plus 00 - noop
	 * (literal)
	 */
	public Code run(Code code) {

		BitSet bits = code.getBitset();
		assertTrue(bits.length() > 2); //

		int op = code.toInt(0, 2);

		int landmarkOrNot = code.nextBit();
		assertTrue(landmarkOrNot == 1); // this must be so!

		//Code firstArgIsAnIndexOfLandmark = code.nextHuffmanPrefixFree(this.landmarksEncoded);
		Integer whatTheLandmarkPointsTo = (Integer) this.landmarksEncoded.decode(code);
		Code secondArg = code.nextPrefixFree(PREFIX_FREE_ENCODINGLEVEL);

		if (op == OP_PLUS) {
			return new Code(whatTheLandmarkPointsTo + secondArg.toInt());
		} else
			return new Code(whatTheLandmarkPointsTo - secondArg.toInt());
	}


	/**
	 * given data x, find a program p (which should hopefully be shorter)
	 */
	public Code encode(Code code) {

		int year = code.toInt();
		Code minimalCode = null;
		
		Set<HuffmanNode> leaves = this.landmarksEncoded.getLeaves();
		
		for (HuffmanNode landmark : leaves) {
			
			Integer landmarkYear = (Integer) landmark.getLabel();
			Code crtCode;
			if (landmarkYear < year) {
				crtCode = buildCode(OP_PLUS, landmark, year - landmarkYear);
			} else
				crtCode = buildCode(OP_MINUS, landmark, landmarkYear - year);

			if (minimalCode == null || minimalCode.getLength() > crtCode.getLength())
				minimalCode = crtCode;
			
		}
		
		return minimalCode;
	}

	
	private Code buildCode(int opCode, HuffmanNode landmark, int diff) {
		Code code = new Code();
		code.addFixedWidthInt(opCode, 2);
		code.addFixedWidthInt(1, 1); // this is a landmark not a literal
		
		Code landmarkCode = this.landmarksEncoded.encode(landmark.getLabel());
		code.addFixedWidthInt(landmarkCode.toInt(), landmarkCode.getLength());
		
		code.addPrefixFreeInt(diff, PREFIX_FREE_ENCODINGLEVEL);
		
		return code;
	}

	
	static Logger LOG = Logger.getLogger(Machine.class);
}
