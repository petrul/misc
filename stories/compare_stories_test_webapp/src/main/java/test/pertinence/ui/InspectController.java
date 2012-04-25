package test.pertinence.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import test.pertinence.dao.ResultDao;
import test.pertinence.dao.UserDao;
import test.pertinence.results.InsertionResult;
import test.pertinence.story.User;
import test.pertinence.story.insertion.InsertionStory;
import test.pertinence.story.repository.StoryRepository;

@Controller
public class InspectController {
	@Autowired
	UserDao userDao;
	
	@Autowired
	ResultDao comparisonDao;
	
	@Autowired
	StoryRepository storyRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	@ModelAttribute("inspection_list")
	Collection<InsertionResult> list(
			@RequestParam(required = false, value = "login") String login
			) {
		if (login == null || "".equals(login.trim()))
			return null;
		User user = this.userDao.loadUser(login);
		if (user == null)
			return null;

		List<InsertionResult> resList = this.comparisonDao.retrieveInsertionResultsForUser(user);
		
		// put back the actual story versions with text (only got ids from database)
		for (InsertionResult r : resList) {
			InsertionStory story = (InsertionStory) this.storyRepository.getStory(r.getStoryid());
			r.setStory(story);
			r.setOptionPresented1Text(story.getOptions().get(r.getOptionPresented1()));
			r.setOptionPresented2Text(story.getOptions().get(r.getOptionPresented2()));
		}
		LOG.info("inspecting results for user " + login);
		Collections.sort(resList, new Comparator<InsertionResult> ( ) {
			@Override
			public int compare(InsertionResult o1, InsertionResult o2) {
				return o1.getStoryid() - o2.getStoryid();
			}
		});
		return resList;
	}
	
	@ModelAttribute("user_list")
	public Collection<User> users() {
		List<User> allUsers = userDao.getAllUsers();
		return new TreeSet<User>(allUsers);
	}
	
	Logger LOG = Logger.getLogger(InspectController.class);
}
