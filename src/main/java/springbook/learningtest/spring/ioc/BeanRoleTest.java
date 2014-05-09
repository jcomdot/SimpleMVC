package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class BeanRoleTest {

	@Test
	public void testSimpleConfig() {
		GenericApplicationContext context = new GenericXmlApplicationContext(BeanRoleTest.class, "beanrole.xml");
		BeanDefinitionUtils.printBeanDefinitions(context);
		
		SimpleConfig sc = context.getBean(SimpleConfig.class);
		assertThat(sc.hello, is(notNullValue()));
		sc.hello.sayHello();
		context.close();
	}

}
