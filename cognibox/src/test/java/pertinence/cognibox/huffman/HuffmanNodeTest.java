package pertinence.cognibox.huffman;

import static org.junit.Assert.*;

import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.junit.Test;

public class HuffmanNodeTest {

	@Test
	public void testCompareTo() {
			TreeSet<HuffmanNode> set = new TreeSet<HuffmanNode>();
			set.add(new HuffmanNode("hi", 5));
			set.add(new HuffmanNode("adrian", 5));
			set.add(new HuffmanNode(7, 5));
			set.add(new HuffmanNode(5));
			set.add(new HuffmanNode(6));
			set.add(new HuffmanNode("Adrian", 6));
			
			assertEquals(6, set.size());
			
			for (HuffmanNode node : set)
				LOG.info(node);
	}

	static Logger LOG = Logger.getLogger(HuffmanNodeTest.class);
}
