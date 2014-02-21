package com.jcomdot.simplemvc.jdk;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DynamicProxyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Tobbung"), is("Hello Tobbung"));
		assertThat(hello.sayHi("Tobbung"), is("Hi Tobbung"));
		assertThat(hello.sayThankYou("Tobbung"), is("Thank You Tobbung"));
	}
	
	@Test
	public void helloUpperProxy() {
		Hello proxyHello = new HelloUppercase(new HelloTarget());
		assertThat(proxyHello.sayHello("Tobbung"), is("HELLO TOBBUNG"));
		assertThat(proxyHello.sayHi("Tobbung"), is("HI TOBBUNG"));
		assertThat(proxyHello.sayThankYou("Tobbung"), is("THANK YOU TOBBUNG"));
	}

	@Test
	public void helloUpperDynamicProxy() {
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] { Hello.class },
				new UppercaseHandler(new HelloTarget()));
		assertThat(proxiedHello.sayHello("Tobbung"), is("HELLO TOBBUNG"));
		assertThat(proxiedHello.sayHi("Tobbung"), is("HI TOBBUNG"));
		assertThat(proxiedHello.sayThankYou("Tobbung"), is("THANK YOU TOBBUNG"));
	}
}
