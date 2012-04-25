package githubmashup.delegate.iterators;

import static org.junit.Assert.*;
import githubmashup.delegate.GithubApiDelegateImplTest;
import githubmashup.model.Commit;
import githubmashup.model.Repository;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.JsonObject;

public class RepoCommitsRemoteArrayTest extends TestCase {

	@Test
	public void testCommits() {
		RepoCommitsRemoteArray array = new RepoCommitsRemoteArray(new Repository("timburks", "nu"), 20 );
		int counter = 0;
		for (JsonObject o : array) {
			counter ++;
			assertNotNull(o.get("committer"));
		}
		assertEquals(20, counter);
	}

	static Logger LOG = Logger.getLogger(RepoCommitsRemoteArrayTest.class);
}
