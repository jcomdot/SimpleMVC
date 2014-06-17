package com.jcomdot.simplemvc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.debug("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		// DI(Dependency Injection)
		/*
		 * ApplicationContext context = new
		 * GenericXmlApplicationContext("spring/context/applicationContext.xml"
		 * ); UserDao dao = context.getBean("userDao", UserDao.class);
		 * ((GenericXmlApplicationContext) context).close();
		 * 
		 * User user = new User("jsjang", "Joonsong Jang", "spring",
		 * Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "jsjang@outlook.com");
		 * 
		 * try { dao.add(user); logger.debug("등록 성공!!!"); } catch (Exception e)
		 * { e.printStackTrace(); }
		 * 
		 * User user2 = null; try { user2 = dao.get(user.getId());
		 * logger.debug("조회 성공!!!"); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		model.addAttribute("serverTime", formattedDate);
		// model.addAttribute("user", user2);

		return "home";
	}

	@RequestMapping(value = "/home")
	public String home2(@Value("#{systemProperties['os.name']}") String osName, Locale locale, Model model) {
		logger.debug("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("osName", osName);

		return "home2";
	}
}
