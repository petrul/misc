package pertinence.cognibox.years;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;

import pertinence.cognibox.huffman.HuffmanTree;

public class MathYearCodecTest {

	@Test
	public void testEncode() throws Exception {
		
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		data.put(1789, 100);
		data.put(1778, 100);
		data.put(1776, 1000);
		
		HuffmanTree tree = new HuffmanTree(data);
		MathYearCodec codec = new MathYearCodec(tree, 100);
		
		int yearToEncode = 1777;
		String[] codes = codec.encode(yearToEncode);
		assertNotNull(codes);
		assertEquals(3, codes.length);
		
		for (String c : codes) {
			LOG.info(c);
			assertEquals(yearToEncode, codec.decode(c));
		}
	}


	@Test
	public void testEncode2() throws Exception {
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		data.put(1770, 100);

		HuffmanTree tree = new HuffmanTree(data);
		MathYearCodec codec = new MathYearCodec(tree, 100);
		
		int yearToEncode = 1777;
		String[] codes = codec.encode(yearToEncode);
		assertNotNull(codes);
		assertTrue(codes.length > 0);
		LOG.info(ToStringBuilder.reflectionToString(codes));
		
		for (String c : codes) {
			LOG.info(c);
			assertEquals(yearToEncode, codec.decode(c));
		}

	}	
	
	static Logger LOG = Logger.getLogger(MathYearCodecTest.class);
}
