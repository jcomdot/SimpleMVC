package springbook.learningtest.spring.web.atmvc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.propertyeditors.CharsetEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class BindingTest extends AbstractDispatcherServletTest {

	@Test
	public void charsetEditor() {
		CharsetEditor charsetEditor = new CharsetEditor();
		charsetEditor.setAsText("UTF-8");
		assertThat(charsetEditor.getValue(), is(instanceOf(Charset.class)));
		assertThat((Charset)charsetEditor.getValue(), is(Charset.forName("UTF-8")));
	}
	
	@Test
	public void levelPropertyEditor() {
		LevelPropertyEditor levelEditor = new LevelPropertyEditor();
		
		levelEditor.setAsText("3");
		assertThat((Level)levelEditor.getValue(), is(Level.GOLD));
		
		levelEditor.setValue(Level.BASIC);
		assertThat(levelEditor.getAsText(), is("1"));
	}
	static class LevelPropertyEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			return String.valueOf(((Level)this.getValue()).intValue());
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
		}
	}

	@Test
	public void levelTypeParameter() throws ServletException, IOException {
		setClasses(SearchController.class);
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		assertModel("level", Level.BASIC);
	}
	@Controller
	static class SearchController {
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		}
		
		@RequestMapping("/user/search")
		public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}

	@Test
	public void webBindingInitializer() throws ServletException, IOException {
		setClasses(SearchController2.class, ConfigForWebBindingInitializer.class);
		initRequest("/user/search").addParameter("level", "2");
		runService();
		assertModel("level", Level.SILVER);
	}
	@Controller
	static class SearchController2 {
		@RequestMapping("/user/search")
		public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}
	@Configuration
	@EnableWebMvc
	static class ConfigForWebBindingInitializer {
		@Bean
		public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
			return new RequestMappingHandlerAdapter() {{
				setWebBindingInitializer(webBindingInitializer());
			}};
		}
		
		@Bean
		public WebBindingInitializer webBindingInitializer() {
			return new WebBindingInitializer() {
				@Override
				public void initBinder(WebDataBinder binder, WebRequest request) {
					binder.registerCustomEditor(Level.class, new LevelPropertyEditor());
				}
			};
		}
	}

	@Test
	public void dataBinder() {
		WebDataBinder dataBinder = new WebDataBinder(null);
		dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		assertThat(dataBinder.convertIfNecessary("1", Level.class), is(Level.BASIC));
	}
	
	@Test
	public void namedPropertyEditor() throws ServletException, IOException {
		setClasses(MemberController.class);
		initRequest("/add.do").addParameter("id", "1000").addParameter("age", "1000");
		runService();
		System.out.println(((Member)getModelAndView().getModel().get("member")).age);
	}

	@Controller
	static class MemberController {
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.registerCustomEditor(int.class, "age", new MinMaxPropertyEditor(0, 200));
		}
		@RequestMapping("/add")
		public void add(@ModelAttribute Member member) { }
	}

	static class Member {
		int id, age;
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public int getAge() { return age; }
		public void setAge(int age) { this.age = age; }
	}

	static class MinMaxPropertyEditor extends PropertyEditorSupport {
		
		int min, max;
		
		public MinMaxPropertyEditor(int min, int max) {
			this.min = min; this.max = max;
		}

		@Override
		public String getAsText() {
			return String.valueOf((Integer)this.getValue());
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			Integer val = Integer.parseInt(text);
			if (val < min) val = min;
			else if (val > max) val = max;
			setValue(val);
		}

	}

}
