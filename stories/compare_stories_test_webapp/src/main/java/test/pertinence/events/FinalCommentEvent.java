package test.pertinence.events;

import test.pertinence.story.User;

public class FinalCommentEvent extends Event {

	String comment;
	
	public FinalCommentEvent(User user) {
		super(user);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
