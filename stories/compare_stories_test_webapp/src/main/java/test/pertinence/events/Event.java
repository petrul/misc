package test.pertinence.events;

import java.util.Date;

import test.pertinence.story.User;

public abstract class Event {
	long	id = 0;
	Date	date;
	User	user;

	public Event(User user) {
		this.date = new Date();
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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


}
