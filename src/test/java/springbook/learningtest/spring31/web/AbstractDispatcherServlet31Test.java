package springbook.learningtest.spring31.web;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.learningtest.spring.web.ConfigurableDispatcherServlet;

public abstract class AbstractDispatcherServlet31Test extends AbstractDispatcherServletTest {

	@Override
	protected ConfigurableDispatcherServlet createDispatcherServlet() {
		return new ConfigurableDispatcherServlet() {
			private static final long serialVersionUID = 7409906105259164196L;

			@Override
			protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
				AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
				wac.register(this.classes);
				wac.setServletContext(getServletContext());
				wac.setServletConfig(getServletConfig());
				wac.refresh();
				return wac;
			}
		};
	}

	@Override
	public AnnotationConfigWebApplicationContext getContext() {
		return (AnnotationConfigWebApplicationContext) super.getContext();
	}

}
