package pertinence.cognibox.years;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.junit.Test;

import pertinence.cognibox.huffman.HuffmanTree;

public class LitteralYearCodecTest {

	@Test
	public void testEncode() throws Exception {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("19", 100);
		map.put("17", 100);
		map.put("18", 100);
		map.put("10", 1000);
		
		HuffmanTree tree = new HuffmanTree(map);
//		tree.totallyUnportableSeeGraphvizRepresentation();
		
		LitteralYearCodec yenc = new LitteralYearCodec(tree);
		
		int yearToTest = 1976;
		String code = yenc.encode(yearToTest)[0];

		assertNotNull(code);
		assertEquals("P00.L7777777777777777777.L6666666666666666666.", code);
		assertTrue(code.contains("P"));
		LOG.debug(code);
		assertEquals(yearToTest, yenc.decode(code));

		{
			// no copy, no landmark, everything is stored as is with looooooong digits
			code = "";
			int yearWithNoCorrespondingCentury = 1345;
			Integer itsCentrury = yearWithNoCorrespondingCentury / 100;
			String itsCentruryToString = itsCentrury.toString();
			assertFalse(map.containsKey(itsCentruryToString));
			
			code = yenc.encode(yearWithNoCorrespondingCentury)[0];
			assertFalse(code.contains("P"));
			assertEquals("L1111111111111111111.L3333333333333333333.L4444444444444444444.L5555555555555555555.", code);
			LOG.info("code for " + yearWithNoCorrespondingCentury + " : " + code);
			assertEquals(yearWithNoCorrespondingCentury, yenc.decode(code));
		}
		
		{
			// copy with landmark
			int y = 1999;
			code = yenc.encode(y)[0];
			assertNotNull(code);
			assertTrue(code.contains("P"));
			assertTrue(code.contains("cc"));
			assertEquals("P00.cc", code);
			LOG.info(code);
			assertEquals(y, yenc.decode(code));
		}
		
		{
			// copy with no landmark
			int y = 1333;
			code = yenc.encode(y)[0];
			assertNotNull(code);
			assertFalse(code.contains("P"));
			assertTrue(code.contains("cc"));
			assertEquals("L1111111111111111111.L3333333333333333333.cc", code);
			LOG.debug(code);
			assertEquals(y, yenc.decode(code));
		}

	}


	@Test
	public void testPlay() throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		HuffmanTree tree = new HuffmanTree(map);
		LitteralYearCodec yenc = new LitteralYearCodec(tree);

		TreeSet<String> set = new TreeSet<String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int ldiff = o1.length() - o2.length();
				if (ldiff != 0)
					return ldiff;
				else
					return o1.compareTo(o2);
			}
		});

		for (int year = 1000; year < 2100; year++) {
			String code = yenc.encode(year)[0];
			assertNotNull(code);
			set.add(code);
		}

		int i = 0;
		for (String s : set) {
			LOG.debug(s);
			if (i++ > 120) break;
		}

	}
	
	static Logger LOG = Logger.getLogger(LitteralYearCodecTest.class);
}
