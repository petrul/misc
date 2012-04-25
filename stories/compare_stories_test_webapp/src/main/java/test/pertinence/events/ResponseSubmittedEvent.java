package test.pertinence.events;

import test.pertinence.results.InsertionResult;
import test.pertinence.story.User;

/**
 * recorded when user answers a question
 * @author dadi
 */
public class ResponseSubmittedEvent extends Event {

	InsertionResult 	response;
	String 				comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ResponseSubmittedEvent(User user) {
		super(user);
	}
	
	public InsertionResult getResponse() {
		return response;
	}

	public void setResponse(InsertionResult response) {
		this.response = response;
	}

}
