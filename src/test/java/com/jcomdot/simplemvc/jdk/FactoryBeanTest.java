package com.jcomdot.simplemvc.jdk;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:**/FactoryBeanTest-context.xml")
public class FactoryBeanTest {
	@Autowired
	ApplicationContext context;

	@Test
	public void getMessageFromFactoryBean() {
		Object message = context.getBean("message");
		assertThat(message, is(instanceOf(Message.class)));
		assertThat(((Message)message).getText(), is("Factory Bean"));
	}
	
	@Test
	public void getFactoryBean() throws Exception {
		Object factory = context.getBean("&message");
		assertThat(factory, is(instanceOf(MessageFactoryBean.class)));
	}

}
