package gest;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;
import static gest.Compass.*;


public class CompassTest {

	@Test
	public void testProgramTail() {
		
		assertEquals("R", Compass.programTail(NORTH_EAST, NORTH));
		assertEquals("RR", Compass.programTail(EAST, NORTH));
		assertEquals("IL", Compass.programTail(SOUTH_EAST, NORTH));
		assertEquals("I", Compass.programTail(SOUTH, NORTH));
		

		assertEquals("I", Compass.programTail(NORTH_EAST, SOUTH_WEST));
		assertEquals("IL", Compass.programTail(NORTH_EAST, WEST));
		
		assertEquals(0, Compass.cc(WEST, WEST));
		assertEquals(1, Compass.cc(WEST, EAST));
	}
	
	@Test
	public void testReflexivity() throws Exception {
		for (Compass src : Compass.values()) 
			for (Compass dest : Compass.values())
				assertEquals(cc(src, dest), cc(dest, src));
	}

	static Logger LOG = Logger.getLogger(CompassTest.class);
}
