package test.pertinence.dao;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import test.pertinence.events.Event;

public class EventDaoImpl implements EventDao {
	
	SessionFactory sessionFactory;
	HibernateTemplate hibernateTemplate;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(this.sessionFactory);
	}

	@Override
	public void persistEvent(Event event) {
		this.hibernateTemplate.persist(event);
	}


}
