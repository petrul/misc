package githubmashup.delegate.iterators;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import githubmashup.delegate.Constants;
import githubmashup.delegate.GithubApiDelegateImpl;
import githubmashup.delegate.GithubApiDelegateImpl;
import githubmashup.delegate.nextpage.HttpHeaderNextPagePolicy;
import githubmashup.delegate.nextpage.NextPagePolicy;
import githubmashup.model.Repository;

public class RepoCommitsRemoteArray extends RemotePagedJsonArray {
	
	public RepoCommitsRemoteArray(Repository repo, int maxResults) {
		super(Constants.BASE_URL + "repos/" + repo.owner + "/" + repo.name + "/commits", maxResults);
	}

}
