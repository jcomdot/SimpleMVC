package com.jcomdot.simplemvc.jdk.proxy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import com.jcomdot.simplemvc.jdk.UppercaseHandler;

public class DynamicProxyTest {

	@Test
	public void simpleProxy() {
		Hello proxyHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] { Hello.class },
				new UppercaseHandler(new HelloTarget()));
		assertThat(proxyHello.sayHello("Tobbung"), is("HELLO TOBBUNG"));
		assertThat(proxyHello.sayHi("Tobbung"), is("HI TOBBUNG"));
		assertThat(proxyHello.sayThankYou("Tobbung"), is("THANK YOU TOBBUNG"));
	}
	
	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello proxiedHello = (Hello)pfBean.getObject();
		assertThat(proxiedHello.sayHello("Tobbung"), is("HELLO TOBBUNG"));
		assertThat(proxiedHello.sayHi("Tobbung"), is("HI TOBBUNG"));
		assertThat(proxiedHello.sayThankYou("Tobbung"), is("THANK YOU TOBBUNG"));
	}

	public class UppercaseAdvice implements MethodInterceptor {
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}
	}
	
	static interface Hello {
		String sayHello(String name);
		String sayHi(String name);
		String sayThankYou(String name);
	}
	
	static class HelloTarget implements Hello {
		public String sayHello(String name) { return "Hello " + name; }
		public String sayHi(String name) { return "Hi " + name; }
		public String sayThankYou(String name) { return "Thank You " + name; }
	}

	@Test
	public void pointcutAdvisor() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Tobbung"), is("HELLO TOBBUNG"));
		assertThat(proxiedHello.sayHi("Tobbung"), is("HI TOBBUNG"));
		assertThat(proxiedHello.sayThankYou("Tobbung"), is("Thank You Tobbung"));
	}
	
	@Test
	public void classNamedPointcutAdvisor() {
		// 포인트컷 준비
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			private static final long serialVersionUID = 1L;

			public ClassFilter getClassFilter() {
				return new ClassFilter() {
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		classMethodPointcut.setMappedName("sayH*");
		
		// 테스트
		checkAdviced(new HelloTarget(), classMethodPointcut, true);
		
		class HelloWorld extends HelloTarget {};
		checkAdviced(new HelloWorld(), classMethodPointcut, false);
		
		class HelloTobbung extends HelloTarget {};
		checkAdviced(new HelloTobbung(), classMethodPointcut, true);
	}
	
	private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		if (adviced) {
			assertThat(proxiedHello.sayHello("Tobbung"), is("HELLO TOBBUNG"));
			assertThat(proxiedHello.sayHi("Tobbung"), is("HI TOBBUNG"));
			assertThat(proxiedHello.sayThankYou("Tobbung"), is("Thank You Tobbung"));
		}
		else {
			assertThat(proxiedHello.sayHello("Tobbung"), is("Hello Tobbung"));
			assertThat(proxiedHello.sayHi("Tobbung"), is("Hi Tobbung"));
			assertThat(proxiedHello.sayThankYou("Tobbung"), is("Thank You Tobbung"));
		}
	}

}
