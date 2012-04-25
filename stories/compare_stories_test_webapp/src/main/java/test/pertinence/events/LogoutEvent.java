package test.pertinence.events;

import test.pertinence.story.User;

public class LogoutEvent extends Event {

	public LogoutEvent(User user) {
		super(user);
	}

}
