package com.jcomdot.simplemvc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:**/junit.xml")
public class JUnitTest {

	@Autowired ApplicationContext context;
	
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
	static ApplicationContext contextObject = null;
	
	@Test public void test1() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat((contextObject == null) || (contextObject == this.context), is(true));
		contextObject = this.context;
	}

	@Test public void test2() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}

	@Test public void test3() {
		assertThat(testObjects, not(hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject, either(is(nullValue(ApplicationContext.class))).or(is(this.context)));
		contextObject = this.context;
	}
}
