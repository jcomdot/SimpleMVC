package com.jcomdot.simplemvc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping(value = "/checkLoginId")
	public String checkLogin1(Locale locale, Model model) {
		return "msgConv";
	}
	@RequestMapping(value = "/checkLoginId/{loginId}", method=RequestMethod.GET)
	@ResponseBody
	public Result checkLogin2(@PathVariable String loginId, Locale locale) {
		Result result = new Result();
		result.setDuplicated(true);
		result.setAvailableId(loginId + (int)(Math.random()*1000));
		
		logger.debug("Welcome home! The client locale is {}.", locale);

//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
//		String formattedDate = dateFormat.format(date);

		return result;
	}
	@RequestMapping(value = "/register", method=RequestMethod.POST)
	@ResponseBody
	public Result register(@RequestBody User user) {
		Result result = new Result();
		result.setDuplicated(true);
		result.setAvailableId(user.getName() + (int)(Math.random()*1000));
		
		return result;
	}
	static class Result {
		boolean duplicated;
		String availableId;
		
		public boolean isDuplicated() { return duplicated; }
		public void setDuplicated(boolean duplicated) { this.duplicated = duplicated; }
		public String getAvailableId() { return availableId; }
		public void setAvailableId(String availableId) { this.availableId = availableId; }
	}
	static class User {
		int loginid;
		String name;
		
		public int getLoginid() { return loginid; }
		public void setLoginid(int loginid) { this.loginid = loginid; }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
	}

}
