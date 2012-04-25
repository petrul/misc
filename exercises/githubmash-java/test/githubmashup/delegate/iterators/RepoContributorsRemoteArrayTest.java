package githubmashup.delegate.iterators;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import githubmashup.model.Repository;
import junit.framework.TestCase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.JsonObject;

public class RepoContributorsRemoteArrayTest extends
		TestCase {

	
	@Test
	public void testNext() {
		RepoContributorsRemoteArray contributors = new RepoContributorsRemoteArray(new Repository("scala", "scala"));
		for (JsonObject s : contributors) {
			LOG.info(s);
		}
	}
	

	@Test
	public void testContributors() {
		// test inexistant 
		try {
			RepoContributorsRemoteArray contributors = new RepoContributorsRemoteArray(new Repository("octocat", "inexistentproject"));
			for (JsonObject o : contributors) {
				assertNotNull(o);
			}
			fail("FileNotFoundException exception expected on inexistant project");
		} catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof FileNotFoundException);
		}
		
		RepoContributorsRemoteArray collaborators = new RepoContributorsRemoteArray(new Repository("octocat", "Spoon-Knife"));
		int size = 0;
		
		Iterator<JsonObject> iterator = collaborators.iterator();
		while (iterator.hasNext()) {
			iterator.next() ; size++;
		}
		
		assertTrue(size > 1);
		LOG.info(collaborators);
	}
		
	
	
	static Logger LOG = Logger.getLogger(RepoContributorsRemoteArrayTest.class);

}
