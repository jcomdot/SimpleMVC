package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class ConverterTest extends AbstractDispatcherServletTest {

	@Controller
	static class SearchController {
		@Autowired ConversionService conversionService;
		
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.setConversionService(conversionService);
		}
		
		@RequestMapping("/user/search")
		public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}

	static class LevelToStringConverter implements Converter<Level, String> {
		@Override
		public String convert(Level level) {
			return String.valueOf(level.intValue());
		}
	}

	static class StringToLevelConverter implements Converter<String, Level> {
		@Override
		public Level convert(String text) {
			return Level.valueOf(Integer.parseInt(text));
		}
	}

	@Test
	public void compositeGenericConversionService() throws ServletException, IOException {
		setRelativeLocations("conversionservice.xml");
		setClasses(SearchController.class);
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		assertModel("level", Level.BASIC);
	}

}
