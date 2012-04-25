package compressor;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;

public class RepetitionEncoderTest {

	@Test
	public void testEncode() {
		RepetitionEncoder enc = new RepetitionEncoder();
		LOG.info(enc.encode("HHHHHHHHHHHHHHHHHHHHHHHTTTHTTHHHHTTTT"));
		assertEquals("*(23)H*3THTT*4H*4T.", enc.encode("HHHHHHHHHHHHHHHHHHHHHHHTTTHTTHHHHTTTT"));
	}

	static Logger LOG = Logger.getLogger(RepetitionEncoderTest.class);
}
