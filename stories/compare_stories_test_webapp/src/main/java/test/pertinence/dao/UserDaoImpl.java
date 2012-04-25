package test.pertinence.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import test.pertinence.story.User;

public class UserDaoImpl implements UserDao {
	SessionFactory sessionFactory;
	HibernateTemplate hibernateTemplate;
	
	@Override
	public User loadUser(String login) {
		try {
			User loadedUser = (User) this.hibernateTemplate.load(User.class, login);
			return loadedUser;
		} catch (ObjectRetrievalFailureException e) {
			return null;
		} catch (RuntimeException e) {
			throw e;
		}
	}
	@Override
	public void persistUser(User user) {
		this.hibernateTemplate.persist(user);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		List find = this.hibernateTemplate.find("from User");
		return find;

	}

	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(this.sessionFactory);
	}
}
