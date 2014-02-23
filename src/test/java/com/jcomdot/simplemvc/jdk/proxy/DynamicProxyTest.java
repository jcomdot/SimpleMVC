package com.jcomdot.simplemvc.jdk.proxy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

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

}
