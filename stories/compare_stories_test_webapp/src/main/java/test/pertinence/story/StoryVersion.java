package test.pertinence.story;

import java.io.Serializable;

public class StoryVersion implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	int absoluteId = 0; // for db
	int 	id;
	Story	story;
	String 	text;
	
	public StoryVersion(Story s, int versionid) {
		this.setStory(s);
		this.setId(versionid);
	}
	
	public int getAbsoluteId() {
		return absoluteId;
	}

	public void setAbsoluteId(int absoluteId) {
		this.absoluteId = absoluteId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "ver. #" + this.story.getId() + "/" + this.getId();
	}
}
