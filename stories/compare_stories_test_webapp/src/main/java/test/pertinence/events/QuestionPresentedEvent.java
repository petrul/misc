package test.pertinence.events;

import test.pertinence.results.InsertionResult;
import test.pertinence.story.User;

/**
 * Triggered when a user gets presented a question (not yet answered) 
 * @author dadi
 */
public class QuestionPresentedEvent extends Event {

	InsertionResult response;
	

	public InsertionResult getResponse() {
		return response;
	}


	public void setResponse(InsertionResult response) {
		this.response = response;
	}


	public QuestionPresentedEvent(User user) {
		super(user);
	}

}
