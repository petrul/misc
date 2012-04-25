package githubmashup.delegate.nextpage;

import githubmashup.delegate.iterators.PageAndRelLinks;

/**
 * look as the http header Links in order to establish the next page
 * @author petru
 *
 */
public class HttpHeaderNextPagePolicy implements NextPagePolicy {

	@Override
	public String getNextPage(PageAndRelLinks pageAndRelLinks) {
		return pageAndRelLinks.getNextPageUrl();
	}

}
