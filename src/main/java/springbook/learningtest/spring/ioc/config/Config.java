package springbook.learningtest.spring.ioc.config;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import springbook.learningtest.spring.ioc.bean.*;

@Configuration
public class Config {

	@Resource Properties systemProperties;
	@Value("#{systemProperties['os.name']}") String osName;
	@Value("#{systemEnvironment['Path']}") String path;
	
	@Bean public Hello hello(Printer printer) {
		Hello hello = new Hello();
		hello.setPrinter(printer);
		return hello;
	}
	
	@Bean public Printer printer() {
		return new StringPrinter();
	}
	
	@Bean public String systemInfo() {
		return "[" + this.osName + "]" + this.path;
	}
}
