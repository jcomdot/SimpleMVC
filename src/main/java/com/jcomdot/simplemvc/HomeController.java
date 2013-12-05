package com.jcomdot.simplemvc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
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
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);

		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		ActorDao dao = context.getBean("actorDao", ActorDao.class);
		Actor actor = new Actor();
		actor.setFirstName("토뿡");
		actor.setLastName("장");

		try {
			dao.add(actor);
			logger.info("등록 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Actor actor2 = null;
		try {
			int lastIdx = dao.getLastIdx();
			actor2 = dao.get(lastIdx);
			logger.info("조회 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("actor", actor2);
		
		return "home";
	}
	
}
