package springbook.learningtest.spring31.enable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class EnableHelloTest {

	@Test
	public void enable() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		Hello h = ac.getBean(Hello.class);
		h.sayHello();
		assertThat(h.getHello(), is("Hello Tobbung"));
		ac.close();
	}
	
	@Configuration
	@EnableHello
	public static class AppConfig {
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Import(HelloConfig.class)
	public @interface EnableHello {
	}
	
	@Configuration
	public static class HelloConfig {
		@Bean
		Hello hello() {
			Hello h = new Hello();
			h.setName("Tobbung");
			return h;
		}
	}
}
