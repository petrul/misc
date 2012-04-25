package gest;

import static org.junit.Assert.*;


import org.apache.log4j.Logger;
import org.junit.Test;
import static gest.Compass.*;

public class GestureTest {

	@Test
	public void testDistanceFrom() {
		Gesture src = new Gesture(NORTH, SOUTH_WEST);
		Gesture dest = new Gesture(EAST, SOUTH);
		
		assertEquals(dest.distanceFrom(src),
				cc(EAST, NORTH) + cc(SOUTH, SOUTH_WEST)
		);
	}
	

	
	@Test
	public void test2() throws Exception {
		
	}
	
	static Logger LOG = Logger.getLogger(GestureTest.class);
}
