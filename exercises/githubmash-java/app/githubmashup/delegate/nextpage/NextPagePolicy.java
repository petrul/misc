package githubmashup.delegate.nextpage;

import githubmashup.delegate.iterators.PageAndRelLinks;

/**
 * paging is not the same accross Github api versions: as of v3 you need to inspect http headers for a Link param
 * whereas in v2 you need to just pass a parameter called "start_page" which you increase until no more data.
 * 
 * @author petru
 *
 */
public interface NextPagePolicy {

	String getNextPage(PageAndRelLinks pageAndRelLinks);

}
