package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class MessageConverterTest extends AbstractDispatcherServletTest {

	@Test
	public void getTest() throws ServletException, IOException {
		setRelativeLocations("messageConverter.xml");
		setClasses(JsonController.class);
		initRequest("/user/checkloginid/ceojang");
		runService();
		System.out.println(getContentAsString());
	}
	
	@Controller
	static class JsonController {
		
		@RequestMapping(value="user/checkloginid/{loginId}", method=RequestMethod.GET)
		@ResponseBody
		public Result checklogin(@PathVariable String loginId) {
			Result result = new Result();
			result.setDuplicated(true);
			result.setAvailableId(loginId + (int)(Math.random()*1000));
			return result;
		}

	}

	static class Result {
		boolean duplicated;
		String availableId;
		
		public boolean isDuplicated() { return duplicated; }
		public void setDuplicated(boolean duplicated) { this.duplicated = duplicated; }
		public String getAvailableId() { return availableId; }
		public void setAvailableId(String availableId) { this.availableId = availableId; }
	}

}
