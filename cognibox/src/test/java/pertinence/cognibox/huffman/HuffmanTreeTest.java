package pertinence.cognibox.huffman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

public class HuffmanTreeTest {

	
	@Test
	public void testHuffmanTree() {
		String text = "o that this too too solid flesh would melt thaw and resolve itself into a dew";
		
		HashMap<Character, Integer> map = this.stringToFreqMap(text);
		int nrUniqueLetters = map.keySet().size();
		
		Data data = new Data(map);
		HuffmanTree tree = new HuffmanTree(data);
		
		int countLeaves = 0;
		for (HuffmanNode node : tree.getAllNodes()) {
			if (node.getLeftChild() == null && node.getRightChild() == null) countLeaves++;
		}
		assertEquals(nrUniqueLetters, countLeaves);
		
		ArrayList<Code> allCodes = new ArrayList<Code>();  
		for (Character c : map.keySet()) {
			Code code = tree.encode(c);
			assertNotNull(code);
			allCodes.add(code);
		}
		
		/* assert prefix-free-ness */
		for (Code code : allCodes) {
			String crtCode = code.toString();
			for (Code codeToCompare : allCodes) {
				String crtCompare = codeToCompare.toString();
				if (crtCode.equals(crtCompare)) continue;
				
				assertFalse(crtCode.startsWith(crtCompare));
				assertFalse(crtCompare.startsWith(crtCode));
			}
		}
	}

	
	private HashMap<Character, Integer> stringToFreqMap(String s) {
		HashMap<Character, Integer> result = new HashMap<Character, Integer>();
		for (int i = 0; i < s.length(); i++) {
			Character charAt = s.charAt(i);
			if (result.containsKey(charAt)) {
				int x = result.get(charAt) + 1;
				result.put(charAt, x);
			} else {
				result.put(charAt, 1);
			}
		}
		return result;
	}


	@Test
	public void testPlay() throws Exception {
		
		Map<String, Integer> initialMap = this.csvToMap();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String s : initialMap.keySet()) {
			int i = Integer.parseInt(s);
			if (i % 100 == 0) // only centuries
				map.put(s, initialMap.get(s));
		}
		
		HuffmanTree tree = new HuffmanTree(new Data(map));
		
		/* uncomment next line if you want to see the result */
//		tree.totallyUnportableSeeGraphvizRepresentation();
		
		for (Object o : map.keySet()) 
			LOG.info("" + o + " => " + tree.encode(o));
	}
	
	
	@Test
	public void testOneLeafOnlyWorks() throws Exception {
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("a", 100);
		HuffmanTree tree = new HuffmanTree(data);
		Code code = tree.encode("a");
//		LOG.info(code);
		assertNotNull(code);
		assertTrue(code.getLength() > 0);
		
	}
	
	protected Map<String, Integer> csvToMap() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		try {
			List<?> lines = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("years.csv"));
			for (Object line : lines) {
				String s = (String) line;
				String[] twoParts = s.split(";");
				String p1 = twoParts[0];
				int p2 = Integer.parseInt(twoParts[1]);
				map.put(p1, p2);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	
	
	static Logger LOG = Logger.getLogger(HuffmanTreeTest.class);
}
