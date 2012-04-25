package test.pertinence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import test.pertinence.results.InsertionResult;

public class ResultDaoImplTest {

	ResultDaoImpl 	resultDao;
	DataSource		datasource;
	JdbcTemplate	jdbcTemplate;
	
	int storyIdThatDoesNotExist = 749876;
	
	public ResultDaoImplTest() {
		ClassPathXmlApplicationContext appCtxt 
		= new ClassPathXmlApplicationContext("story_compare.spring.xml");
		
		resultDao = (ResultDaoImpl) appCtxt.getBean("result-dao");
		datasource = (DataSource) appCtxt.getBean("datasource");
		this.jdbcTemplate = new JdbcTemplate(this.datasource);
		
	}
	
	@Test
	public void testGetOptionSampleWithFewestRespondentsForStoryId() {
		
		this.jdbcTemplate.update(
				"delete from InsertionResult where storyid = ?", 
					new Object[] {storyIdThatDoesNotExist});
		
		assertEquals(0, 
				this.jdbcTemplate.queryForInt(
						"select count(*) from InsertionResult res where res.storyid = ?", 
						new Object[] {storyIdThatDoesNotExist}));

		List<Integer> options = new ArrayList<Integer>();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		
		InsertionResult ir = new InsertionResult();
		ir.setStoryid(storyIdThatDoesNotExist);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		
		
		LOG.info(options);
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		
		LOG.info(options);
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		
		LOG.info(options);
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		
		LOG.info(options);
		assertTrue(options.get(0) == 1 && options.get(1) == 3);
		
		// insert two more 1,3
		
		ir.setId(0);
		this.resultDao.persistResult(ir);
		LOG.info(options);
	
		// now we shouldn't see 1,3 for a while
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		LOG.info(options);
		
		assertFalse(options.get(0) == 1 && options.get(1) == 3);
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		LOG.info(options);
		
		assertFalse(options.get(0) == 1 && options.get(1) == 3);
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		LOG.info(options);
		
		assertFalse(options.get(0) == 1 && options.get(1) == 3);
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		LOG.info(options);
		
		assertFalse(options.get(0) == 1 && options.get(1) == 3);
		
		
		// now we should see 1,3 again
		
		options.clear();
		options.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(storyIdThatDoesNotExist));
		ir.setId(0);
		ir.setOptionPresented1(options.get(0));
		ir.setOptionPresented2(options.get(1));
		this.resultDao.persistResult(ir);
		LOG.info(options);
		
		assertTrue(options.get(0) == 1 && options.get(1) == 3);
		
	}

	@Test
	public void justSee() {
		for (int i = 2; i < 40; i++) {
			try {
				Set<Integer> options = resultDao.getOptionSampleWithFewestRespondentsForStoryId(i);
				LOG.info("for story " + i + " the options with fewest respondents is " + options);
			} catch (Exception e) {
				// ignore
			}
		}
	}
	
	@After
	public void cleanUp() {
		this.jdbcTemplate.update(
				"delete from InsertionResult where storyid = ?", 
				new Object[] {storyIdThatDoesNotExist});
	}
	
	static Logger LOG = Logger.getLogger(ResultDaoImplTest.class);
}
