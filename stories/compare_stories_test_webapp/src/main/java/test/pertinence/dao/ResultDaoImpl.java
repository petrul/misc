package test.pertinence.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import test.pertinence.results.ComparisonResult;
import test.pertinence.results.InsertionResult;
import test.pertinence.story.User;

public class ResultDaoImpl implements ResultDao {
	SessionFactory sessionFactory;
	HibernateTemplate hibernateTemplate;

	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(this.sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComparisonResult> retrieveComparisonResultsForUser(User user) {
		List find = this.hibernateTemplate.find("from ComparisonResult cr where cr.user.login = ?", user.getLogin());
		return find;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InsertionResult> retrieveInsertionResultsForUser(User user) {
		List find = this.hibernateTemplate.find("from InsertionResult r where r.user.login = ?", user.getLogin());
		return find;
	}

	
	@Override
	public void updateResult(ComparisonResult comparison) {
		this.hibernateTemplate.saveOrUpdate(comparison);
	}
	
	@Override
	public void updateResult(InsertionResult result) {
		this.hibernateTemplate.saveOrUpdate(result);
	}

	/* (non-Javadoc)
	 * @see test.pertinence.dao.ComparisonResultDao#insertNewComparisonResult(test.pertinence.story.ComparisonResult)
	 */
	public void persistComparisonResult(ComparisonResult comparison) {
		this.hibernateTemplate.persist(comparison);
	}
	
	public void persistResult(InsertionResult result) {
		this.hibernateTemplate.persist(result);
	}

	@Override
	public Set<Integer> getOptionSampleWithFewestRespondentsForStoryId(Integer storyId) {
		
		TreeMap<Integer, Integer[]> sorter = new TreeMap<Integer, Integer[]>();
		
		String query = 
			" from InsertionResult res where res.storyid = :storyid and  "
			+ "( "
			+ "		(res.optionPresented1 = :opt1 and res.optionPresented2 = :opt2) "
			+ " or "
			+ "		(res.optionPresented1 = :opt2 and res.optionPresented2 = :opt1) "
			+ ")"
		;
		
		Integer[] options = {1,2};
		List<?> res = this.hibernateTemplate.findByNamedParam(
				query, 
				new String[]  {"storyid", "opt1", "opt2"}, 
				new Object[] { storyId, options[0], options[1] } 
		);
		Integer comp_12 = res.size();
		sorter.put(comp_12, options);
		
		options = new Integer[] {2,3};
		res = this.hibernateTemplate.findByNamedParam(
				query, 
				new String[]  {"storyid", "opt1", "opt2"}, 
				new Object[] { storyId, options[0], options[1] } 
		);
		int comp_23 = res.size();
		sorter.put(comp_23, options);

		options = new Integer[] {1,3};
		res = this.hibernateTemplate.findByNamedParam(
				query, 
				new String[]  {"storyid", "opt1", "opt2"}, 
				new Object[] { storyId, options[0], options[1] } 
		);
		int comp_13 = res.size();
		sorter.put(comp_13, options);

		Integer leastSize = sorter.keySet().iterator().next();
		Integer[] integers = sorter.get(leastSize);
		HashSet<Integer> resp = new HashSet<Integer>();
		resp.add(integers[0]); resp.add(integers[1]);
		
		return resp;
	}

	

}
