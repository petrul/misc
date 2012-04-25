package githubmashup.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import play.cache.Cache;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import githubmashup.delegate.iterators.PageAndRelLinks;
import githubmashup.model.Commit;
import githubmashup.model.Repository;
import githubmashup.model.User;

public class GithubApiDelegateImpl implements GithubApi {

	JsonParser parser = new JsonParser();

	/* (non-Javadoc)
	 * @see githubmashup.delegate.GithubApi#getPageAndRelLinks(java.lang.String)
	 */
	@Override
	public PageAndRelLinks getPageAndRelLinks(String surl) {
		try {
			URL url = new URL(surl);
			URLConnection connection = url.openConnection();
			String field = connection.getHeaderField("Link");
			PageAndRelLinks result = new PageAndRelLinks();
			
			if (field != null) {
				String[] links = field.split(",");
				for (String s: links) {
					String[] parts = s.split(";");
					
					// this should be something like <https://api.github.com/repos/timburks/nu/commits?last_sha=6b869cdc2fd2c3fba86d688c39e630dba0df7d54&top=master>
					String link = parts[0].trim();
					int begin = 0;
					if (link.charAt(0) == '<') begin++;
					int end = link.length();
					if (link.charAt(end - 1) == '>') end--;
					link = link.substring(begin, end);
					
	
					// this should be something like  rel="next"
					String rel = parts[1].trim();
					rel = rel.substring(5, rel.length() - 1);
					
					result.links.put(rel, link);
				}
			}			
			
			InputStream is = connection.getInputStream();
			result.content = IOUtils.toString(is);
			return result;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
//	public List<String> getRepoContributors(Repository repo) {
//		assertValidRepo(repo);
//		
//			List<String> users = new ArrayList<String>();
//			
//			String url = BASE_URL + "repos/" + repo.owner + "/" + repo.name + "/contributors";
//			JsonArray arr = cacheOrGet(url);
//			
//			for (int i = 0; i < arr.size(); i++) {
//				
//				JsonObject obj = arr.get(i).getAsJsonObject();
//				users.add(obj.get("login").getAsString());
//				
//			}
//			return users;
//		
//	}

	/**
	 * get a json object, not an array
	 */
	public JsonObject getJsonObject(String url) {
		try {
//			String contents = (String)Cache.get(url);
//			if (contents == null) { 
				InputStream stream = new URL(url).openStream();
				String contents = IOUtils.toString(stream);
//				Cache.set(url, contents);
//			}
			
			JsonObject arr = parser.parse(contents).getAsJsonObject();

			return arr;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

//	@Override
//	public List<Commit> getRepoCommits(Repository repo, int maxSize) {
//			assertValidRepo(repo);
//			List<Commit> list = new ArrayList<Commit>();
//			
//			String url = BASE_URL + "repos/" + repo.owner + "/" + repo.name + "/commits";
//			JsonArray arr = cacheOrGet(url);
//			
//			for (int i = 0; i < arr.size(); i++) {
//				
//				JsonObject obj = arr.get(i).getAsJsonObject().get("commit").getAsJsonObject();
//				LOG.info(obj);
//				
//			}
//			
//			return list;
//	}

//	private void assertValidRepo(Repository repo) {
//		Assert.assertNotNull(repo.owner);
//		Assert.assertNotNull(repo.name);		
//	}

	static Logger LOG = Logger.getLogger(GithubApiDelegateImpl.class);


}
