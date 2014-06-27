package springbook.learningtest.spring31.web;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


public class RequestConditionTest extends AbstractDispatcherServlet31Test {
	
	@Test
	public void patternRC() throws ServletException {
		setClasses(AppConfig.class, PatternController.class, ParamsController.class);
		buildDispatcherServlet();
		
		RequestMappingHandlerMapping rmhm = getBean(AppConfig.class).rmhm;
		for (Map.Entry<RequestMappingInfo, HandlerMethod> e : rmhm.getHandlerMethods().entrySet()) {
			System.out.println(e.getKey() + " => " + e.getValue());
		}
	}

	@Configuration
	static class AppConfig extends WebMvcConfigurationSupport {
		@Autowired RequestMappingHandlerMapping rmhm;
		
		@Override
		public RequestMappingHandlerMapping requestMappingHandlerMapping() {
			RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
			return rmhm;
		}
	}
	@Controller
	@RequestMapping({"/a", "/b"})
	static class PatternController {
		@RequestMapping({"/c", "/d"})
		public String hello() {
			return "hello.jsp";
		}
	}
	@Controller
	@RequestMapping(params={"p1", "p2"})
	static class ParamsController {
		@RequestMapping(params={"p3", "p4"})
		public String hello2() {
			return "hello.jsp";
		}
	}

}
