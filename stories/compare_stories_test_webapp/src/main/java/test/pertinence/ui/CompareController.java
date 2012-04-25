package test.pertinence.ui;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import test.pertinence.dao.EventDao;
import test.pertinence.dao.ResultDao;
import test.pertinence.events.ResponseSubmittedEvent;
import test.pertinence.results.ComparisonResult;
import test.pertinence.story.StoryVersion;
import test.pertinence.story.TwoStories;
import test.pertinence.story.User;
import test.pertinence.story.repository.StoryRepository;

@Controller
@SessionAttributes({"comparisonResult"})
public class CompareController {
	
	@Autowired
	StoryRepository storyRepository;
	
	@Autowired
	ResultDao		comparisonResultDao;
	
	@Autowired
	EventDao				eventDao;

	public void setStoryRepository(StoryRepository storyRepository) {
		this.storyRepository = storyRepository;
	}

	@RequestMapping(method=RequestMethod.GET)
	public void view() {
		return;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ModelAttribute
	public ModelAndView show(HttpSession session) {

		User user = (User) session.getAttribute("user");
		if (user == null)
			return new ModelAndView("redirect:/login/logout.html");

		TwoStories twoRandomVersions = storyRepository.getTwoRandomVersions();
		StoryVersion storyLeft = twoRandomVersions.getLeftStory();
		StoryVersion storyRight = twoRandomVersions.getRightStory();

		ComparisonResult bean = new ComparisonResult();
		
		bean.setStoryLeft(storyLeft);
		bean.setStoryRight(storyRight);
		
		bean.setUser(user);
		
		this.comparisonResultDao.persistComparisonResult(bean);
//		{
//			QuestionPresentedEvent questionPresentedEvent = new QuestionPresentedEvent(user);
//			questionPresentedEvent.setResponse(bean);
//			this.eventDao.persistEvent(questionPresentedEvent);
//		}
		
		bean.setComparisonValue(50);
		
		LOG.info("comparing " + storyLeft + " to " + storyRight + " ...");
		return new ModelAndView().addObject(bean);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String submit(@ModelAttribute ComparisonResult comparisonResult, SessionStatus ss, HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		if (user == null)
			return "redirect:/login/logout.html";
		
		
		comparisonResult.touch();
		this.comparisonResultDao.updateResult(comparisonResult);

		{
			ResponseSubmittedEvent event = new ResponseSubmittedEvent(user);
			//event.setResponse(comparisonResult);
			this.eventDao.persistEvent(event);
		}
		
		LOG.info("comparison submitted :" + comparisonResult);
		ss.setComplete();
		return "redirect:show.html";
	}
	


    static Logger LOG = Logger.getLogger(CompareController.class);
}
