package test.pertinence.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import test.pertinence.dao.EventDao;
import test.pertinence.dao.ResultDao;
import test.pertinence.events.QuestionPresentedEvent;
import test.pertinence.events.ResponseSubmittedEvent;
import test.pertinence.results.InsertionResult;
import test.pertinence.story.User;
import test.pertinence.story.insertion.InsertionStory;
import test.pertinence.story.repository.StoryRepository;

/**
 * Controlls page succession for the test with option insertions 
 * @author dadi
 */

@Controller
@SessionAttributes("insertionResult")
public class InsertController {
	
	@Autowired
	EventDao eventDao;
	
	@Autowired
	ResultDao resultDao;
	
	@Autowired
	MailSender mailSender;
	
	@Autowired
    SimpleMailMessage templateMessage;
	
	@Autowired
	StoryRepository storyRepository;

	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView show(HttpSession session, SessionStatus ss, ModelMap mm) {
		
		User user = (User) session.getAttribute("user");
		InsertionResult result = new InsertionResult();
		InsertionStory crtStory;
		{
			Iterator<InsertionStory> it =  (Iterator<InsertionStory>) session.getAttribute("sample-iterator");
			if (user == null || it == null || ! it.hasNext())
				return new ModelAndView("redirect:/login/thankyou.html");
			
			 crtStory = it.next();
			
			result.setStory(crtStory);
		}
		
		result.setUser(user);

		{
			Integer cnt = (Integer)session.getAttribute("counter");
			Collection<?> sample = (Collection<?>) session.getAttribute("sample");
			cnt++;
			session.setAttribute("counter", cnt);
			session.setAttribute("progress", "" + cnt + "/" + sample.size());
		}
		
		try {
			String mainText = crtStory.getMain();
			Integer pos = crtStory.getInsertPosition();
			String leftSide = mainText.substring(0, pos);
			String rightSide = mainText.substring(pos);
			result.setMainTextLeft(leftSide);
			result.setMainTextRight(rightSide);
			
			Integer firstOption, secondOption;
			
			List<Integer> randomOptionSample = new ArrayList<Integer>();
			randomOptionSample.addAll(resultDao.getOptionSampleWithFewestRespondentsForStoryId(crtStory.getId()));
			Collections.shuffle(randomOptionSample);
			Iterator<Integer> _tmpid = randomOptionSample.iterator();
			firstOption = _tmpid.next();
			secondOption = _tmpid.next();
			
			result.setOptionPresented1(firstOption);
			result.setOptionPresented1Text(crtStory.getOptions().get(firstOption));
			result.setOptionPresented2(secondOption);
			result.setOptionPresented2Text(crtStory.getOptions().get(secondOption));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		this.resultDao.persistResult(result);
		{
			QuestionPresentedEvent questionPresentedEvent = new QuestionPresentedEvent(user);
			questionPresentedEvent.setResponse(result);
			this.eventDao.persistEvent(questionPresentedEvent);
		}
		
		LOG.info("presenting " + crtStory + " to user " + user + "...");
		return new ModelAndView().addObject(result);
	}


	@RequestMapping(method=RequestMethod.POST)
	public String submit(@ModelAttribute InsertionResult result, SessionStatus ss, HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		if (user == null)
			return "redirect:/login/logout.html";
		
		result.touch();
		this.resultDao.updateResult(result);

		{
			ResponseSubmittedEvent event = new ResponseSubmittedEvent(user);
			event.setResponse(result);
			event.setComment(result.getComment());
			this.eventDao.persistEvent(event);
		}
		
		if (result.getComment() != null && "" != result.getComment().trim()) {
			SimpleMailMessage mail = new SimpleMailMessage(this.templateMessage);
			mail.setSubject("Test histoires : a question was answered with a comment");
			StringBuilder txt = new StringBuilder();
			txt.append("user       : " + user + "\n");
			txt.append("response   : " + result.toString() + "\n");
			txt.append("comment    : " + result.getComment() + "\n");
			txt.append("---------------------------- \n");
			
			InsertionStory story = (InsertionStory) this.storyRepository.getStory(result.getStoryid());
			
			txt.append("story text : " + story.getMain() + "\n");
			txt.append("option " + result.getOptionPresented1() + " : " + story.getOptions().get(result.getOptionPresented1()) + "\n");
			txt.append("option " + result.getOptionPresented2() + " : " + story.getOptions().get(result.getOptionPresented2()) + "\n");
			txt.append("chosen option was : " + story.getOptions().get(result.getChosenOption()) + "\n");
			
			mail.setText(txt.toString());
			try {
				this.mailSender.send(mail);
			} catch (Exception e) {
				LOG.warn("error while sending mail", e);
			}
		}
		
		LOG.info("comparison submitted :" + result);
		ss.setComplete();
		return "redirect:show.html";
	}
	
	static Logger LOG = Logger.getLogger(InsertController.class);
}
