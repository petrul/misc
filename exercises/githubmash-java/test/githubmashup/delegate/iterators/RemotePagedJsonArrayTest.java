package githubmashup.delegate.iterators;

import static org.junit.Assert.*;

import githubmashup.delegate.GithubApiDelegateImpl;
import githubmashup.delegate.GithubApiDelegateImpl;
import githubmashup.delegate.GithubApiDelegateImplTest;
import githubmashup.delegate.iterators.RemotePagedJsonArray;
import githubmashup.delegate.nextpage.UrlParamNextPagePolicy;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Array;
import org.junit.Test;

import com.google.gson.JsonObject;

public class RemotePagedJsonArrayTest {
	
//	GithubApiDelegate github = new GithubApiDelegateImpl();
	
	/**
	 * this is on the v3 api
	 */
	@Test
	public void testIteratorOnRepoCommits() {
		RemotePagedJsonArray src = new RemotePagedJsonArray("https://api.github.com/repos/timburks/nu/commits");	
		int counter = 0;
		for (JsonObject j : src) { // this is the coolness of it
			counter++;
			assertNotNull(j.get("committer"));
		}
		
		assertTrue(counter > 100); // there should be lots of commits
	}
	
	
	/**
	 * this is on the v2 api; there are no links
	 */
	@Test
	public void testIteratorOnRepoSearch() {
		String url = "https://github.com/api/v2/json/repos/search/scala";
		RemotePagedJsonArray src = new RemotePagedJsonArray(
				url, 
				new GithubApiDelegateImpl(), 
				"repositories",
				new UrlParamNextPagePolicy(url),
				20);	
		int counter = 0;
		for (JsonObject j : src) {
			counter++;
			assertNotNull(j.get("type"));// one of the properties of a repo
		}
		assertEquals(20, counter);
	}
	static Logger LOG = Logger.getLogger(GithubApiDelegateImplTest.class);	
}
