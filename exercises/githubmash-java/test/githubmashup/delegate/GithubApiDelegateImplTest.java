package githubmashup.delegate;

import static org.junit.Assert.*;

import githubmashup.delegate.iterators.PageAndRelLinks;
import githubmashup.model.Commit;
import githubmashup.model.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import play.Play;
import play.cache.Cache;
import play.test.UnitTest;

public class GithubApiDelegateImplTest extends TestCase {
	
	GithubApi github = new GithubApiDelegateImpl();


	@Test
	public void testGetPageAndRelLinks() {
		PageAndRelLinks pageAndRelLinks = github.getPageAndRelLinks("https://api.github.com/repos/timburks/nu/commits");

		assertNotNull(pageAndRelLinks.content);
		assertNotNull(pageAndRelLinks.links);
		String firstNext = pageAndRelLinks.links.get("next");
		assertNotNull(firstNext);
		LOG.info(firstNext);
		
		// get next page
		pageAndRelLinks = github.getPageAndRelLinks(firstNext);
		assertNotNull(pageAndRelLinks.content);
		assertNotNull(pageAndRelLinks.links);
		String secondNext = pageAndRelLinks.links.get("next");
		assertNotNull(secondNext);
		assertFalse(firstNext.equals(secondNext));
		LOG.info(secondNext);
	}
	
	static Logger LOG = Logger.getLogger(GithubApiDelegateImplTest.class);
}
