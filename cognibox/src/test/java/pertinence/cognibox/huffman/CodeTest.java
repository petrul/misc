package pertinence.cognibox.huffman;

import static org.junit.Assert.*;

import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Test;

import pertinence.cognibox.parser.Machine;


public class CodeTest {

	@Test
	public void testCodeInt() {
		Code code = new Code(3000);
		assertEquals("101110111000", code.toString());
		code = new Code(2);
		assertEquals("10", code.toString());
		
		int cnst = 15333;
		code = new Code(cnst);
		assertEquals(cnst, code.toInt());
		
		for (int i = 0; i < 100; i++) {
			int rnd = Math.abs(new Random().nextInt());
			Code c = new Code(rnd);
			assertEquals(rnd, c.toInt());
		}
	}

	
	@Test
	public void testConcatenate() throws Exception {
		Code c1 = new Code("01");
		Code c2 = new Code("10");
		Code thetwo = Code.concatenate(c1, c2);
		assertEquals(c1.getLength() + c2.getLength(), thetwo.getLength());
		assertEquals("0110", thetwo.toString());
	}
	
	
	@Test
	public void testPrefixFree() throws Exception {
		int someInt = 10000;
		Code c1 = new Code(someInt);
		LOG.debug(c1.toPrefixFree(1));
		Code pf = c1.toPrefixFree(2);
		CodeAndLength code = Code.fromPrefixFree(pf, 2);
		assertEquals(someInt, code.code.toInt());
		assertEquals(25, code.originalLength);
		
		
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			int randomInt = Math.abs(rnd.nextInt());
			Code c = new Code(randomInt);
			Code cpf = c.toPrefixFree(2);
			Code c_decoded = Code.fromPrefixFree(cpf, 2).code;
			assertEquals(c_decoded.toInt(), randomInt);
			assertEquals(c.toInt(), randomInt);
		}
	}

	
	@Test
	public void testPrefixFreeEncodingAndDecodings() throws Exception {
		Code code = new Code();
		code.addFixedWidthInt(Machine.OP_PLUS, 2);
		assertEquals(2, code.toInt());
		code.addFixedWidthInt(Machine.OP_PLUS, 2); // now we should have 1010b = ten in decimal
		assertEquals(8 + 2, code.toInt());
		code.addFixedWidthInt(Machine.OP_PLUS, 3); // now we should have 10 10 010 = 82 in decimal
		assertEquals(64 + 16 + 2, code.toInt());
		
		code = new Code();
		code.addPrefixFreeInt(5, Machine.PREFIX_FREE_ENCODINGLEVEL);
		code.resetCursor();
		
		Code rc = code.nextPrefixFree(Machine.PREFIX_FREE_ENCODINGLEVEL);
		LOG.debug(code);
		assertEquals(5, rc.toInt());
		
		code.addPrefixFreeInt(7, Machine.PREFIX_FREE_ENCODINGLEVEL);
		LOG.debug(code);
		
		code.resetCursor();
		assertEquals(5, code.nextPrefixFree(Machine.PREFIX_FREE_ENCODINGLEVEL).toInt());
		assertEquals(7, code.nextPrefixFree(Machine.PREFIX_FREE_ENCODINGLEVEL).toInt());
		
	}

	
	@Test
	public void testAppending() throws Exception {
		Code code = new Code();
		code.addFixedWidthInt(2, 2);
		code.addPrefixFreeInt(5, 1);
		assertEquals("1011011101", code.toString());
		
		code = new Code();
		code.addFixedWidthInt(2, 2);
		code.addFixedWidthInt(1, 1);
		assertEquals("101", code.toString());
	}
	
	
	static Logger LOG = Logger.getLogger(CodeTest.class);
}
