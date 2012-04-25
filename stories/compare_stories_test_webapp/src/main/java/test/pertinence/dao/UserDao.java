package test.pertinence.dao;

import java.util.List;

import test.pertinence.story.User;

public interface UserDao {
	User loadUser(String login);
	void persistUser(User user);
	List<User> getAllUsers();
}
