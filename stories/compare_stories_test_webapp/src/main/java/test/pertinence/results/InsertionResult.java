package test.pertinence.results;

import java.util.Date;

import test.pertinence.story.Story;
import test.pertinence.story.User;

/**
 * encapsulates the result of a user choice :  
 * 
 * @author dadi
 *
 */
public class InsertionResult implements Comparable<InsertionResult> {
	long 			id = 0;
	Story 			story;
	int 			storyid;
	
	int 			optionPresented1;
	String 			optionPresented1Text;
	int 			optionPresented2;
	String 			optionPresented2Text;
	
	int 			chosenOption;
	User 			user;
	Date			date;
	
	String 			mainTextLeft;
	String 			mainTextRight;
	
	String 			comment;
	
	public InsertionResult() {
		this.date = new Date();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Story getStory() {
		return story;
	}
	
	public void setStory(Story story) {
		this.story = story;
		this.storyid = story.getId();
	}
	
	public int getStoryid() {
		return storyid;
	}
	
	public void setStoryid(int storyid) {
		this.storyid = storyid;
	}
	
	public int getChosenOption() {
		return chosenOption;
	}
	
	public void setChosenOption(int chosenOption) {
		this.chosenOption = chosenOption;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void touch() {
		this.date = new Date();
	}

	public String getMainTextLeft() {
		return mainTextLeft;
	}

	public void setMainTextLeft(String mainTextLeft) {
		this.mainTextLeft = mainTextLeft;
	}

	public String getMainTextRight() {
		return mainTextRight;
	}

	public void setMainTextRight(String mainTextRight) {
		this.mainTextRight = mainTextRight;
	}

	public int getOptionPresented1() {
		return optionPresented1;
	}

	public void setOptionPresented1(int optionPresented1Idx) {
		this.optionPresented1 = optionPresented1Idx;
	}

	public String getOptionPresented1Text() {
		return optionPresented1Text;
	}

	public void setOptionPresented1Text(String optionPresented1Text) {
		this.optionPresented1Text = optionPresented1Text;
	}

	public int getOptionPresented2() {
		return optionPresented2;
	}

	public void setOptionPresented2(int optionPresented2Idx) {
		this.optionPresented2 = optionPresented2Idx;
	}

	public String getOptionPresented2Text() {
		return optionPresented2Text;
	}

	public void setOptionPresented2Text(String optionPresented2Text) {
		this.optionPresented2Text = optionPresented2Text;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "InsertionResult : for story " + this.storyid 
		+ "(" + this.optionPresented1 + " vs " + this.optionPresented2 + ") choice was "
		+ this.chosenOption;
	}

	@Override
	public int compareTo(InsertionResult o) {
		return (int) (this.getId() - o.getId());
	}

}
