package githubmashup.delegate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import githubmashup.delegate.iterators.PageAndRelLinks;

public interface GithubApi {

	/**
	 * GETs the contents of a page and parses its Links header 
	 */
	public abstract PageAndRelLinks getPageAndRelLinks(String surl);

	public JsonObject getJsonObject(String url);
}