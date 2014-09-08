package springbook.learningtest.spring.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class AtAspectTest {
	
	@Test
	public void simple() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(HelloImpl.class, HelloWorld.class, HelloAspect.class);
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(ac);
		ac.refresh();
		
		Hello hello = ac.getBean(Hello.class);
		hello.makeHello("Spring");
		hello.makeHello("Spring", 2);
		hello.add(1, 2);
		
		HelloWorld helloWorld = ac.getBean(HelloWorld.class);
		helloWorld.hello();
		User u = new User(); u.id = 1; u.name = "Spring";
		helloWorld.printUser(u);
		
		HelloAspect aspect = ac.getBean(HelloAspect.class);
		System.out.println(aspect.resultCombine);
		System.out.println(aspect.resultReturnString);
		System.out.println(aspect.resultHelloClass);
		System.out.println(aspect.resultCommon);
	}

	@Aspect
	static class HelloAspect {
		List<String> resultCombine = new ArrayList<String>();
		List<String> resultReturnString = new ArrayList<String>();
		List<String> resultHelloClass = new ArrayList<String>();
		List<String> resultCommon = new ArrayList<String>();
		
		public void clear() {
			resultCombine.clear();
			resultReturnString.clear();
			resultHelloClass.clear();
			resultCommon.clear();
		}
		
		@Pointcut("execution(String *(..))") private void returnString() {}
		@Before("@target(an)")
		public void an(JoinPoint jp, Anno an) {
			System.out.println(an);
		}
	}

	@Target({ElementType.TYPE, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Anno { String value() default "a"; }

	interface Hello {
		int add(int a, int b);
		String makeHello(String name);
		String makeHello(String name, int repeat);
	}
	@Anno("@Anno:HelloImpl") static class HelloImpl implements Hello {

		@Override
		public int add(int a, int b) { return a+b; }
		public String Convert(int a) { return String.valueOf(a); }
		public int increase(int a) { return a++; }

		@Override
		public String makeHello(String name) { return "Hello " + name; }

		@Override
		public String makeHello(String name, int repeat) {
			String names = "";
			while (repeat > 0) { names += name; repeat--; }
			return names;
		}

	}

	@Anno("@Anno:HelloWorld") static class HelloWorld {
		public void hello() { System.out.println("Hello World"); }
		public void printUser(User u) { System.out.println(u); }
	}

	public static class User {
		int id; String name;
		public String toString() { return "User [id=" + id + ", name=" + name + "]"; }
	}

}
