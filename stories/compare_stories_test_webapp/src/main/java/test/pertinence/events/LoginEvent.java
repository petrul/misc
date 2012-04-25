package test.pertinence.events;

import test.pertinence.story.User;

public class LoginEvent extends Event {

	public LoginEvent(User user) {
		super(user);
	}

}
