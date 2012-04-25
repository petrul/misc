package pertinence.cognibox.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import pertinence.cognibox.huffman.Code;
import pertinence.cognibox.huffman.Data;
import pertinence.cognibox.huffman.HuffmanTree;


public class MachineTest {
	
	@Test
	public void testRun() throws Exception {
		
		Map<String, Integer> initialMap = this.csvToMap();
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (String s : initialMap.keySet()) {
			int i = Integer.parseInt(s);
			if (i % 100 == 0) // only centuries
				map.put(Integer.parseInt(s), initialMap.get(s));
		}
		
		HuffmanTree tree = new HuffmanTree(new Data(map));
		Machine m = new Machine(tree);
		
		Code code = new Code();
		code.addFixedWidthInt(Machine.OP_PLUS, 2);
		code.addFixedWidthInt(1, 1);
		
		int year2000code = tree.encode(2000).toInt();
		code.append(new Code(year2000code));
		//code.addPrefixFreeInt(year2000code, Machine.PREFIX_FREE_ENCODINGLEVEL);
		code.addPrefixFreeInt(7, Machine.PREFIX_FREE_ENCODINGLEVEL);
		
		LOG.info("program : " + code);
		
		Code result = m.run(code);
		assertEquals(2000 + 7, result.toInt());
		LOG.info(result.toInt());
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
	
	
	@Test
	public void testEncode() throws Exception {
		Map<String, Integer> initialMap = this.csvToMap();
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (String s : initialMap.keySet()) {
			int i = Integer.parseInt(s);
			if (i % 100 == 0) // only centuries
				map.put(Integer.parseInt(s), initialMap.get(s));
		}
		
		Machine m = new Machine(new Data(map));
		
		int year = 1978;
		Code program = m.encode(new Code(year));
		assertEquals(year, m.run(program).toInt());
		
		for (int i = 1100; i < 2200; i++) {
			program = m.encode(new Code(i));
			System.out.println("" + i +"; " + program.getLength());
		}
	}
	
	
	@Test @Ignore
	public void testDoLandmarksMinimizeAverageCode() throws Exception {
		
		int n = 10 * 1000;
		
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		
		int nrLandmarks = 10; // number of landmarks
		
		int interLandmarkGap = n / nrLandmarks;
		
		for (int i = 0; i < nrLandmarks; i++) {
			Integer landmark = i * interLandmarkGap;
			data.put(landmark, 100);
		}
		
		HuffmanTree tree = new HuffmanTree(data);
		Machine machine = new Machine(tree);
		
		long lengthSum = 0;
		for (int i = 0; i < n; i++) {
			Code encoded = machine.encode(new Code(i));
			int length = encoded.getLength();
			lengthSum += length;
		}
		
		long avgLength = lengthSum / n;
		
		LOG.info("average code length for " + nrLandmarks + " landmarks = " + avgLength);
	}
	

	protected void innerTestLandmarksEffectOnFewNumbers(int nrLandmarks) {
		int n = 100;
		
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		
		int interLandmarkGap = n / nrLandmarks;
		
		for (int i = 0; i < nrLandmarks; i++) {
			Integer landmark = i * interLandmarkGap;
			data.put(landmark, 100);
		}
		
		HuffmanTree tree = new HuffmanTree(data);
		Machine machine = new Machine(tree);
		
		long lengthSum = 0;
		for (int i = 0; i < n; i++) {
			Code encoded = machine.encode(new Code(i));
			int length = encoded.getLength();
			lengthSum += length;
		}
		
		long avgLength = lengthSum / n;
		
		LOG.info("average code length for " + nrLandmarks + " landmarks = " + avgLength);

	}
	
	
	@Test
	public void testLandmarksEffectOnFewNumbers() throws Exception {
		for (int i = 1; i < 10; i++) {
			//int nrLandmarks = 1; // number of landmarks
			innerTestLandmarksEffectOnFewNumbers(i);
		}
	}
	
	static Logger LOG = Logger.getLogger(MachineTest.class);
}
