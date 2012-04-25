package test.pertinence.story.repository;

import java.util.ArrayList;

import test.pertinence.story.Story;
import test.pertinence.story.StoryVersion;
import test.pertinence.story.TwoStories;
import test.pertinence.story.insertion.InsertionStory;

public interface StoryRepository {
	
	Story getStory(int storyId);
	
	StoryVersion getVersion(int storyId, int versionId);
	
	StoryVersion getRandomVersion();
	
	public TwoStories getTwoRandomVersions();
	
	/**
	 * @return a list of stories to compare
	 */
	ArrayList<InsertionStory> getCompleteSampleForUser();
}
