package springbook.learningtest.spring31.enable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class ConfigurerTest {
	@Test
	public void configurer() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		Hello h = ac.getBean(Hello.class);
		h.sayHello();
		assertThat(h.getHello(), is("Hello Tobbung"));
		ac.close();
	}
	
	@Configuration
	@EnableHello
	public static class AppConfig implements HelloConfigurer {
		@Override
		public void configName(Hello hello) {
			hello.setName("Tobbung");
		}
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Import(HelloConfig.class)
	public @interface EnableHello {
	}

	public interface HelloConfigurer {
		void configName(Hello hello);
	}
	
	@Configuration
	public static class HelloConfig {
		@Autowired(required=false) HelloConfigurer helloConfigurer;
		@Bean
		Hello hello() {
			Hello h = new Hello();
			h.setName("Tobbung");
			return h;
		}
	}
}
