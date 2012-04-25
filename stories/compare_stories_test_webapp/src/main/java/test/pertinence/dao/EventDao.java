package test.pertinence.dao;

import test.pertinence.events.Event;

public interface EventDao {
	void persistEvent(Event event);
}
