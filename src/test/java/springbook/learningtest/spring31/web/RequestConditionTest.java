package springbook.learningtest.spring31.web;

import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

public class RequestConditionTest extends AbstractDispatcherServlet31Test {

	@RequestMapping({"/s1", "/s2"})
	static interface Super {
		public String hello();
	}

	@Test
	public void patternRC() throws ServletException {
		setClasses(AppConfig.class, PatternController.class,
				ParamsController.class, HeadersController.class,
				ConsumesController.class, ProducesController.class,
				RequestMethodController.class, Quiz4.class, Quiz5.class,
				Quiz6.class, Quiz7.class);
		buildDispatcherServlet();

		RequestMappingHandlerMapping rmhm = getBean(AppConfig.class).rmhm;
		for (Map.Entry<RequestMappingInfo, HandlerMethod> e : rmhm
				.getHandlerMethods().entrySet()) {
			System.out.println(e.getKey() + " => " + e.getValue());
		}
	}

	@Configuration
	static class AppConfig extends WebMvcConfigurationSupport {
		@Autowired
		RequestMappingHandlerMapping rmhm;

		@Override
		public RequestMappingHandlerMapping requestMappingHandlerMapping(
				ContentNegotiationManager contentNegotiationManager, FormattingConversionService conversionService,
				ResourceUrlProvider resourceUrlProvider) {
			// TODO Auto-generated method stub
			return super.requestMappingHandlerMapping(contentNegotiationManager, conversionService, resourceUrlProvider);
		}
	}

	@Controller
	@RequestMapping({ "/a", "/b" })
	static class PatternController {
		@RequestMapping({ "/c", "/d" })
		public String hello() {
			return "hello.jsp";
		}
	}

	@Controller
	@RequestMapping(params = { "p1", "p2" })
	static class ParamsController {
		@RequestMapping(params = { "p3", "p4" })
		public String hello2() {
			return "hello.jsp";
		}
	}

	@Controller
	@RequestMapping(headers = { "h1", "h2" })
	static class HeadersController {
		@RequestMapping(value = "/f", headers = { "h3", "h4" })
		public String hello3() {
			return "hello.jsp";
		}
	}

	@Controller
	@RequestMapping
	static class ConsumesController {
		@RequestMapping(value = "/g", consumes = { "c3/text", "c4/json" })
		public String hello4() {
			return "hello.jsp";
		}
	}

	@Controller
	@RequestMapping(produces = { "r1/text", "r2/text" })
	static class ProducesController {
		@RequestMapping(value = "/g", produces = { "r3/text", "r4/json" })
		public String hello5() {
			return "hello.jsp";
		}
	}

	@Controller
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	static class RequestMethodController {
		@RequestMapping(method = { RequestMethod.PUT, RequestMethod.DELETE })
		public String hello6() {
			return "hello.jsp";
		}
	}

	@Controller
	@RequestMapping(headers = { "a", "Content-Type=application/json",
			"Content-Type=multipart/form-data" })
	public static class Quiz4 implements Super {
		@Override
		@RequestMapping(headers = { "c", "d" })
		public String hello() { return "hello.jsp"; }
	}

	@Controller
	@RequestMapping(headers = { "a", "b" })
	public static class Quiz5 {
		@RequestMapping(headers = { "c", "d", "Content-Type=application/json" }, consumes = "multipart/form-data")
		public void hello() {
		}
	}

	@Controller
	@RequestMapping(consumes = { "application/xml",
			"application/x-www-form-urlencoded" })
	public static class Quiz6 {
		@RequestMapping(consumes = { "multipart/form-data", "application/json" })
		public void hello() {
		}
	}
	
	@Controller
	public static class Quiz7 implements Super {
		@Override
		@RequestMapping(consumes = { "multipart/form-data", "application/json" })
		public String hello() { return "hello.jsp"; }
	}
}
