package springbook.learningtest.spring.web.controllers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class SimpleControllerTest extends AbstractDispatcherServletTest {

	@Test
	public void helloControllerUnitTest() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "Spring");
		Map<String, Object> model = new HashMap<String, Object>();
		
		new HelloController().control(params, model);
		
		assertThat((String)model.get("message"), is("Hello Spring"));
	}
	
	public class HelloController extends SimpleController {
		public HelloController() {
			this.setRequiredParams(new String[] { "name" });
			this.setViewName("/WEB-INF/view/hello.jsp");
		}

		@Override
		public void control(Map<String, String> params,
				Map<String, Object> model) throws Exception {
			model.put("message", "Hello " + params.get("name"));
		}
	}

	static abstract class SimpleController implements Controller {
		private String[] requiredParams;
		private String viewName;

		public void setRequiredParams(String[] requiredParams) {
			this.requiredParams = requiredParams;
		}

		public void setViewName(String viewName) {
			this.viewName = viewName;
		}

		@Override
		public ModelAndView handleRequest(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			if (viewName == null)
				throw new IllegalStateException();

			Map<String, String> params = new HashMap<String, String>();
			for (String param : requiredParams) {
				String value = request.getParameter(param);
				if (value == null)
					throw new IllegalStateException();
				params.put(param, value);
			}

			Map<String, Object> model = new HashMap<String, Object>();

			this.control(params, model);

			return new ModelAndView(this.viewName, model);
		}

		public abstract void control(Map<String, String> params,
				Map<String, Object> model) throws Exception;
	}
}
