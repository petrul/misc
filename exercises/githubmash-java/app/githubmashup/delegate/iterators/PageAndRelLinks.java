package githubmashup.delegate.iterators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The contents of a page AND its relative links: next, previous and whatever else 
 * @author petru
 *
 */

public class PageAndRelLinks {
	
	public String content;
	
	public Map<String, String> links = new HashMap<String, String>();
	
	public void addLink(String rel, String link) {
		this.links.put(rel, link);
	}

	@Override
	public String toString() {
		return "PageAndRelLinks [content=" + content + ", links=" + links + "]";
	}
	
	public String getNextPageUrl() {
		return this.links.get("next");
	}

}
