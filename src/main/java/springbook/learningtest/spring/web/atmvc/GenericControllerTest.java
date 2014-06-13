package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class GenericControllerTest extends AbstractDispatcherServletTest {

	@Test
	public void gencon() throws ServletException, IOException {
		setClasses(UserController.class);
		runService("/user/add").assertViewName("user/add");
		runService("/user/update").assertViewName("user/update");
		runService("/user/view").assertViewName("user/view");
		runService("/user/delete").assertViewName("user/delete");
		runService("/user/list").assertViewName("user/list");
	}
	
	static abstract class GenericController<T, P, S> {
		S service;
		@RequestMapping("/add") public void add(T entity) {  }
		@RequestMapping("/update") public void update(T entity) {  }
		@RequestMapping("/view") public T view(P id) { return (T)null; }
		@RequestMapping("/delete") public void delete(P id) {  }
		@RequestMapping("/list") public List<T> list() { return (List<T>)null; }
	}
	@RequestMapping("/user")
	static class UserController extends GenericController<User, Integer, UserService> {}
	static class User { int id; String name; }
	static class UserService {}

}
