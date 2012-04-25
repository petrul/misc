package test.pertinence.ui;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import test.pertinence.dao.EventDao;
import test.pertinence.dao.UserDao;
import test.pertinence.events.FinalCommentEvent;
import test.pertinence.events.LoginEvent;
import test.pertinence.events.LogoutEvent;
import test.pertinence.events.TestEndedEvent;
import test.pertinence.events.TestStartedEvent;
import test.pertinence.story.User;
import test.pertinence.story.insertion.InsertionStory;
import test.pertinence.story.repository.StoryRepository;

@Controller
public class LoginController {

	@Autowired
	UserDao	userDao;
	
	@Autowired
	EventDao	eventDao;
	
	@Autowired
	StoryRepository storyRepository;
	
	@Autowired
	MailSender mailSender;
	
	@Autowired
    SimpleMailMessage templateMessage;
	
	@RequestMapping(method=RequestMethod.GET)
	@ModelAttribute("user")
	public ModelAndView show(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null)
			return new ModelAndView("redirect:/insert/show.html");
		return new ModelAndView().addObject(new User()); 
	}

	@RequestMapping(method=RequestMethod.POST)
	public String submit(
			@ModelAttribute("user") User user, 
			HttpSession session, 
			HttpServletRequest req) 
	{
		String login = user.getLogin();
		if (login == null || login.equals(""))
			login = "anonymous";
		
		String newLogin = login;
		
		int counter = 0;
		while (true) {
			counter++;
			User existingUser = this.userDao.loadUser(newLogin);
			if (existingUser == null) {
				LOG.debug("new login [" + newLogin + "]");
				break;
			} else {
				LOG.debug("login " + newLogin + " already used");
			}

			newLogin = login + "_" + counter;
		}
		
		user.setLogin(newLogin);
		user.setHostIp(req.getRemoteAddr());
		//user.setResolvedHostName(req.getRemoteHost());
		LOG.info("user [" + user + "] logged in.");
		
		this.userDao.persistUser(user);
		session.setAttribute("user", user);
		
		{
			LoginEvent loginEvent = new LoginEvent(user);
			TestStartedEvent testStartedEvent = new TestStartedEvent(user);
			this.eventDao.persistEvent(loginEvent);
			this.eventDao.persistEvent(testStartedEvent);
		}
		
		ArrayList<InsertionStory> sample = this.storyRepository.getCompleteSampleForUser();
		session.setAttribute("sample", sample);
		session.setAttribute("counter", 0);
		session.setAttribute("sample-iterator", sample.iterator());
		
		return "redirect:/insert/show.html";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void thankyou(HttpSession session) {
		User user = (User) session.getAttribute("user");
		TestEndedEvent testEndedEvent = new TestEndedEvent(user);
		this.eventDao.persistEvent(testEndedEvent);
	}

	@RequestMapping(method=RequestMethod.GET)
	public void goodbye() {
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String submitFinalComment(
			@RequestParam(required=false) String finalComment,
			HttpSession session
		)
	{
			User user = (User) session.getAttribute("user");
			FinalCommentEvent finalCommentEvent = new FinalCommentEvent(user);
			finalCommentEvent.setComment(finalComment);
			this.eventDao.persistEvent(finalCommentEvent);
			
			this.logout(session);
			
			{
				SimpleMailMessage mail = new SimpleMailMessage(this.templateMessage);
				mail.setSubject("Test histoires : a test was completed");
				StringBuilder txt = new StringBuilder();
				txt.append("user : " + user + "\n");
				txt.append("final comment : [" + finalComment + "]\n");
				mail.setText(txt.toString());
				try {
					this.mailSender.send(mail);
				} catch (Exception e) {
					LOG.warn("error while sending mail", e);
				}
			}
			
			return "redirect:goodbye.html";
	}

	
	@RequestMapping(method=RequestMethod.GET)
	public String logout(HttpSession session) {
		
		LOG.info("user [" + session.getAttribute("user") + "] logged out.");

		User user = (User) session.getAttribute("user");
		LogoutEvent logoutEvent = new LogoutEvent(user);
		this.eventDao.persistEvent(logoutEvent);
		
		session.removeAttribute("user");
		session.removeAttribute("sample");
		session.removeAttribute("counter");
		session.removeAttribute("progress");
		
		return "redirect:goodbye.html";
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	static Logger LOG = Logger.getLogger(LoginController.class);
}
