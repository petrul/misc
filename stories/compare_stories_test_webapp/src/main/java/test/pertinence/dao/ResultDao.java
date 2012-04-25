package test.pertinence.dao;

import java.util.List;
import java.util.Set;

import test.pertinence.results.ComparisonResult;
import test.pertinence.results.InsertionResult;
import test.pertinence.story.User;

public interface ResultDao {

	public abstract void persistComparisonResult(ComparisonResult comparison);
	public void persistResult(InsertionResult result);
	
	public abstract void updateResult(ComparisonResult comparison);
	public abstract void updateResult(InsertionResult result);
	public abstract List<ComparisonResult> retrieveComparisonResultsForUser(User user);
	public abstract List<InsertionResult> retrieveInsertionResultsForUser(User user);
	
	/**
	 * given a story id, returns two options to compare that have the fewest answers up to
	 * now. Use this instead of randomly choosing options to present in order to balance 
	 * answers. 
	 */
	public abstract Set<Integer> getOptionSampleWithFewestRespondentsForStoryId(Integer storyId);
}