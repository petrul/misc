package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import githubmashup.delegate.GithubApi;
import githubmashup.delegate.iterators.RepoCommitsRemoteArray;
import githubmashup.delegate.iterators.RepoContributorsRemoteArray;
import githubmashup.model.Repository;
import play.mvc.Controller;
import scripts.similarity.PlayWithUsersWatchingRepos;

public class repo extends Controller {
	@Inject
	static GithubApi githubApi;
	
	public static void embeddableDetails() {
		String ownerr = params.get("owner");
		String repo = params.get("repo");
		try {
			RepoContributorsRemoteArray collaborators = new RepoContributorsRemoteArray(new Repository(ownerr, repo));
			//List<String> collaborators = array.asStringList();
			render(ownerr, repo, collaborators);
		} catch (RuntimeException e) {
			LOG.info(e,e);
			error(e);
		}
	}
	
	public static void details(String ownerr, String repo) {
		RepoCommitsRemoteArray array = new RepoCommitsRemoteArray(new Repository(ownerr, repo), 100);
		
		HashMap<String, Integer> contributionsPieData = new HashMap<String, Integer>();
		TreeMap<String, Integer> commitTimeline = new TreeMap<String, Integer>();
		
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'");
		SimpleDateFormat year_month_Formatter = new SimpleDateFormat("yyyy/MM");
		
		for (JsonObject o: array) {
			try {
				// data for the pie
				String commiter = o.getAsJsonObject("committer").get("login").getAsString();
				if (contributionsPieData.containsKey(commiter)) {
					contributionsPieData.put(commiter, contributionsPieData.get(commiter) + 1);
				} else
					contributionsPieData.put(commiter, 1);
				
				
				// data for the timeline
				
				String strdate = o.getAsJsonObject("commit").getAsJsonObject("committer").get("date").getAsString();
				Date date = format.parse(strdate);
				String targetStrFormat = year_month_Formatter.format(date);
				
				if (commitTimeline.containsKey(targetStrFormat)) 
					commitTimeline.put(targetStrFormat, commitTimeline.get(targetStrFormat) + 1);
				else
					commitTimeline.put(targetStrFormat, 1);
				
			} catch (ClassCastException e) {
				// sometimes the committer is null and I get "play.exceptions.JavaExecutionException: com.google.gson.JsonNull cannot be cast to com.google.gson.JsonObject" , don't know why
				continue;
			} catch (ParseException e) {
				LOG.warn(e.getMessage());
				continue;
			}
		}
		LOG.info(commitTimeline);
		
		// fill in missing days
		
		render(ownerr, repo, contributionsPieData, commitTimeline);
	}

	public static void seeAlso(String ownerr, String repo) {
		HashMap<Repository, java.lang.Double> seealso = PlayWithUsersWatchingRepos.see_also(ownerr, repo);
//		HashMap<Repository, java.lang.Double> seealso = new HashMap<Repository, java.lang.Double>();
//		seealso.put(new Repository("gigi", "grandioso"), 1.0);
//		seealso.put(new Repository("hoho", "lala"), 1.0);
		render(seealso);
	}
	
	static Logger LOG = Logger.getLogger(repo.class);
}
