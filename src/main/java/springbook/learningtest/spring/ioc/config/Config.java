package springbook.learningtest.spring.ioc.config;

import org.springframework.context.annotation.*;

import springbook.learningtest.spring.ioc.bean.*;

@Configuration
public class Config {

	@Bean public Hello hello(Printer printer) {
		Hello hello = new Hello();
		hello.setPrinter(printer);
		return hello;
	}
	
	@Bean public Printer printer() {
		return new StringPrinter();
	}
	
}
