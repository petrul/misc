package githubmashup.delegate.nextpage;

import githubmashup.delegate.iterators.PageAndRelLinks;

/**
 * adds a start_page url param with increasing values in order to page, proper for v2 api
 * @author petru
 *
 */
public class UrlParamNextPagePolicy implements NextPagePolicy {

	int counter = 1;
	final String baseUrl;
	
	public UrlParamNextPagePolicy(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	@Override
	public String getNextPage(PageAndRelLinks pageAndRelLinks) {
		return this.baseUrl + "?start_page=" + this.counter++;
	}

}
