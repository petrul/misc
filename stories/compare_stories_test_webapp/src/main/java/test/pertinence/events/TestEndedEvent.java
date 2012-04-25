package test.pertinence.events;

import test.pertinence.story.User;

public class TestEndedEvent extends Event {

	public TestEndedEvent(User user) {
		super(user);
	}

}
