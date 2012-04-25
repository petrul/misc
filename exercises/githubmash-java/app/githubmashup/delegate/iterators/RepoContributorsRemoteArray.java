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

public class RepoContributorsRemoteArray extends RemotePagedJsonArray {
	
	public RepoContributorsRemoteArray(Repository repo) {
		super(Constants.BASE_URL + "repos/" + repo.owner + "/" + repo.name + "/contributors");
	}

	public List<String> asStringList() {
		ArrayList<String> result = new ArrayList<String>();
		for (JsonObject o : this) {
			result.add(o.get("login").getAsString());
		}
		return result;
	}

}
