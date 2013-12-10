package com.jcomdot.simplemvc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.debug("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);

		// DI(Dependency Injection)
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		ActorDao dao = context.getBean("actorDao", ActorDao.class);
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		context.close();
		
		// DL(Dependency Lookup)
//		ActorDao dao = new ActorDao();
		
		Actor actor = new Actor();
		actor.setFirstName("Tobbung");
		actor.setLastName("Jang");

		try {
			dao.add(actor);
			logger.debug("등록 성공!!!({})", ccm.getCounter());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Actor actor2 = null;
		try {
			int lastIdx = dao.getLastIdx();
			actor2 = dao.get(lastIdx);
			logger.debug("조회 성공!!!({})", ccm.getCounter());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("actor", actor2);
		
		return "home";
	}
	
}
