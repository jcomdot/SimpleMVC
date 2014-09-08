package springbook.learningtest.spring31.web;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.learningtest.spring31.ioc.BeanDefinitionUtils;

public class MvcArchitectureTest extends AbstractDispatcherServletTest {

	@Configuration
	public static class JobWebConfig extends WebMvcConfigurationSupport {

		@Override
		protected void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(handlerInterceptor());
		}

		@Bean HandlerInterceptor handlerInterceptor() {
			return new AuditInterceptor();
		}

	}
	static class AuditInterceptor extends HandlerInterceptorAdapter {
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			HandlerMethod hm = (HandlerMethod)handler;
			if (hm.getMethodAnnotation(Audit.class) != null) {
				saveAuditInfo(request, response, handler);
			}
			return super.preHandle(request, response, handler);
		}

		private void saveAuditInfo(HttpServletRequest request, HttpServletResponse response, Object handler) {
			System.out.println(request.getRequestURI());
		}
	}
	@Controller
	static class JobController {
		@RequestMapping("/hello")
		public String hello() { return "hello.jsp"; }
		
		@RequestMapping("/specialjob")
		@Audit
		public String specialjob() { return "specialjob.jsp"; }
	}
	@Retention(RetentionPolicy.RUNTIME)
	@interface Audit {}
	@Test
	public void handlerMethodInterceptor() throws ServletException, IOException {
		setClasses(JobWebConfig.class, JobController.class);
		runService("/hello");
		runService("/specialjob");
	}

	@Controller
	static class SimpleController {
		@RequestMapping("/hello")
		public String hello() {
			return "hello.jsp";
		}
	}
	@Test
	public void defaultStrategies() throws ServletException {
		setRelativeLocations("servlet.xml");
		buildDispatcherServlet();
		BeanDefinitionUtils.printBeanDefinitions(getContext());
	}

}
