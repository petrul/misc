package githubmashup.delegate.iterators;

import githubmashup.delegate.GithubApi;
import githubmashup.delegate.GithubApiDelegateImpl;
import githubmashup.delegate.GithubApiDelegateImpl;
import githubmashup.delegate.nextpage.HttpHeaderNextPagePolicy;
import githubmashup.delegate.nextpage.NextPagePolicy;

import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Use this to interate over Github api remote lists of JsonObjects
 * 
 * @author petru
 * 
 */
public class RemotePagedJsonArray implements Iterable<JsonObject> {
	
	final String 			xpath;
	final String 			firstPage;
	final GithubApi	github;
	final NextPagePolicy 	nextPagePolicy;
	final int 				maxResults;
	
	public RemotePagedJsonArray(String firstPage) {
		this(firstPage, Integer.MAX_VALUE);
	}
	
	public RemotePagedJsonArray(String firstPage, int maxResults) {
		// by default use Links http header for paging
		this(firstPage, null, maxResults);
	}
	
	public RemotePagedJsonArray(String firstPage, String xpath, int maxResults) {
		this(firstPage, new GithubApiDelegateImpl(), xpath, new HttpHeaderNextPagePolicy(), maxResults);
	}
	
	/**
	 * @param xpath if the result is not a JsonArray, the "xpath" to the interesting array
	 */
	public RemotePagedJsonArray(String firstPage, GithubApi github, String xpath, NextPagePolicy policy, int maxResults) {
		this.firstPage = firstPage;
		this.github = github;
		this.xpath = xpath;
		this.nextPagePolicy = policy;
		this.maxResults = maxResults;
	}

	@Override
	public Iterator<JsonObject> iterator() {
		return new GithubApiJsonObjIterator(firstPage, github, xpath, this.nextPagePolicy, this.maxResults);
	}


	
	
	static class GithubApiJsonObjIterator implements Iterator<JsonObject> {
		
		private GithubApi github;
		final private String xpath;
		final private NextPagePolicy nextPagePolicy;
		String crtUrl;
		JsonArray crtArray = null;
		int crtIndex;
		int pageCounter = 0;
		int overallCounter = 0;
		final int maxResults;
		

		public GithubApiJsonObjIterator(String firstPage, GithubApi github, String xpath, NextPagePolicy policy, int maxResults) {
			this.crtUrl = firstPage;
			this.github = github;
			this.xpath = xpath;
			this.nextPagePolicy = policy;
			this.maxResults = maxResults;
			loadNext();
		}

		private void loadNext() {
			this.pageCounter++;
			PageAndRelLinks pageAndRelLinks = this.github.getPageAndRelLinks(this.crtUrl);
			LOG.info("loaded page #" + pageCounter + " : [" + this.crtUrl + "]");
			
			this.crtUrl = this.nextPagePolicy.getNextPage(pageAndRelLinks);
			
			if (this.xpath != null) {
				// this is an object and we need to navigate a bit to get the array
				JsonObject root = new JsonParser().parse(pageAndRelLinks.content).getAsJsonObject();
				String[] parts = xpath.split("/");
				for(int i = 0; i < parts.length; i++) {
					if (i < parts.length - 1)
						root = root.getAsJsonObject(parts[i]);
					else
						this.crtArray = root.getAsJsonArray(parts[i]); // the last is an array
				}
				
			} else
				this.crtArray = new JsonParser().parse(pageAndRelLinks.content).getAsJsonArray();
			if (this.crtArray.size() == 0)
				this.crtUrl = null; // no more data, we should stop
			
			this.crtIndex = 0;
		}

		@Override
		public boolean hasNext() {
			if (this.overallCounter >= this.maxResults)
				return false;
			if (this.crtArray != null) {
				if (this.crtIndex < this.crtArray.size()) {
					return true;
				} else {
					// need to load next page
					if (this.crtUrl == null)
						return false;
					else {
						loadNext();
						return (this.crtUrl != null);
					}
				}
			} else {
				// no data
				return false;
			}
		}

		@Override
		public JsonObject next() {
			this.overallCounter ++;
			return this.crtArray.get(this.crtIndex++).getAsJsonObject();
		}

		@Override
		public void remove() {
			new RuntimeException("unimplemented");
		}
		static Logger LOG = Logger.getLogger(RemotePagedJsonArray.class);	
	}
	
}


