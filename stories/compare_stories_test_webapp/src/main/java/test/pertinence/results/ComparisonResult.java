package test.pertinence.results;

import java.util.Date;

import test.pertinence.story.StoryVersion;
import test.pertinence.story.User;

public class ComparisonResult {

	long 			id = 0; // for db
	int 			comparisonValue = -1;
	User			user;
	transient StoryVersion 	storyLeft;
	transient StoryVersion	storyRight;
	Date			date;

	int leftStoryId;
	int leftStoryVersionId;
	int rightStoryId;
	int rightStoryVersionId;

	public ComparisonResult() {
		this.touch();
	}
	
	public void touch() {
		this.date = new Date();
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public StoryVersion getStoryLeft() {
		return storyLeft;
	}
	public void setStoryLeft(StoryVersion storyLeft) {
		this.storyLeft = storyLeft;
		this.leftStoryId = storyLeft.getStory().getId();
		this.leftStoryVersionId = storyLeft.getId();
	}
	public StoryVersion getStoryRight() {
		return storyRight;
	}
	public void setStoryRight(StoryVersion storyRight) {
		this.storyRight = storyRight;
		this.rightStoryId = storyRight.getStory().getId();
		this.rightStoryVersionId = storyRight.getId();
	}
	
	public int getComparisonValue() {
		return comparisonValue;
	}
	public void setComparisonValue(int comparisonValue) {
		this.comparisonValue = comparisonValue;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getLeftStoryId() {
		return leftStoryId;
	}

	public int getLeftStoryVersionId() {
		return leftStoryVersionId;
	}

	public int getRightStoryId() {
		return rightStoryId;
	}

	public int getRightStoryVersionId() {
		return rightStoryVersionId;
	}

	public void setLeftStoryId(int leftStoryId) {
		this.leftStoryId = leftStoryId;
	}

	public void setLeftStoryVersionId(int leftStoryVersionId) {
		this.leftStoryVersionId = leftStoryVersionId;
	}

	public void setRightStoryId(int rightStoryId) {
		this.rightStoryId = rightStoryId;
	}

	public void setRightStoryVersionId(int rightStoryVersionId) {
		this.rightStoryVersionId = rightStoryVersionId;
	}

	
	@Override
	public String toString() {
		return "[" + this.storyLeft + "] vs [" + storyRight + "] : " + this.comparisonValue + " by [" + this.user + "] at [" + this.date + "]";
	}
}
