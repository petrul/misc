package test.pertinence.events;

import test.pertinence.story.User;

public class TestStartedEvent extends Event {

	public TestStartedEvent(User user) {
		super(user);
	}

}
