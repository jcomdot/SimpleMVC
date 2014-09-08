package springbook.learningtest.spring.ioc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import springbook.learningtest.spring.ioc.bean.*;
import springbook.learningtest.spring.ioc.config.Config;

public class ApplicationContextTest {
	
	String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void registerBean() {
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerSingleton("hello1", Hello.class);
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		ac.registerBeanDefinition("hello2", helloDef);
		
		Hello hello1 = ac.getBean("hello1", Hello.class);
		assertThat(hello1, is(notNullValue()));
		
		Hello hello2 = ac.getBean("hello2", Hello.class);
		assertThat(hello2.sayHello(), is("Hello Spring"));
		assertThat(hello1, is(not(hello2)));
		assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
		
		ac.close();
	}
	
	@Test
	public void registerBeanWithDependency() {
		StaticApplicationContext ac = new StaticApplicationContext();
		
		ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
		
		ac.registerBeanDefinition("hello", helloDef);
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
		
		ac.close();
	}
	
	@Test
	public void genericXmlApplicationContext() {
		GenericApplicationContext ac = new GenericXmlApplicationContext("springbook/learningtest/spring/ioc/genericApplicationContext.xml");
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
		
		ac.close();
	}
	
	@Test
	public void contextHierarchy() {
		ApplicationContext parent = new GenericXmlApplicationContext(basePath + "parentContext.xml");
		GenericApplicationContext child = new GenericApplicationContext(parent);
		
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
		reader.loadBeanDefinitions(basePath + "childContext.xml");
		child.refresh();
		
		Printer printer = child.getBean("printer", Printer.class);
		assertThat(printer, is(notNullValue()));
		
		Hello hello = child.getBean("hello", Hello.class);
		assertThat(hello, is(notNullValue()));
		
		hello.print();
		assertThat(printer.toString(), is("Hello Child"));
	}
	
	@Test
	public void simpleBeanScanning() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("springbook.learningtest.spring.ioc.bean");
		AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class);
		
		assertThat(hello, is(notNullValue()));
		((AnnotationConfigApplicationContext)ctx).close();
	}
	
	@Test
	public void configurationBean() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
		AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class);
		assertThat(hello, is(notNullValue()));
		
		AnnotatedHelloConfig config = ctx.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
		assertThat(config, is(notNullValue()));
		
		assertThat(config.annotatedHello(), is(sameInstance(hello)));
		
		((AnnotationConfigApplicationContext)ctx).close();
	}
	
	private static class BeanA {
		@Autowired BeanB beanB;
	}
	private static class BeanB {
	}
	
	@Test
	public void simpleAtAutowired() {
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(BeanA.class, BeanB.class);
		BeanA beanA = ctx.getBean(BeanA.class);
		assertThat(beanA.beanB, is(notNullValue()));
		ctx.close();
	}
	
	@Test
	public void simpleConfig() {
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
		Config config = ctx.getBean(Config.class);
		Printer printer = ctx.getBean(Printer.class);
		assertThat(config.printer(), is(sameInstance(printer)));
		ctx.close();
	}
	
	@Test
	public void systemPropEnv() {
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
		Config config = ctx.getBean(Config.class);
		System.out.println(config.systemInfo());
		ctx.close();
	}
	
	@Test
	public void singletonScope() {
		AbstractApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
		Set<SingletonBean> beans = new HashSet<SingletonBean>();
		
		beans.add(ac.getBean(SingletonBean.class));
		beans.add(ac.getBean(SingletonBean.class));
		assertThat(beans.size(), is(1));
		
		beans.add(ac.getBean(SingletonClientBean.class).bean1);
		beans.add(ac.getBean(SingletonClientBean.class).bean2);
		assertThat(beans.size(), is(1));
		
		ac.close();
	}
	
	static class SingletonBean {}
	static class SingletonClientBean {
		@Autowired SingletonBean bean1;
		@Autowired SingletonBean bean2;
	}
	
	@Test
	public void prototypeScope() {
		AbstractApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);
		Set<PrototypeBean> beans = new HashSet<PrototypeBean>();
		
		beans.add(ac.getBean(PrototypeBean.class));
		assertThat(beans.size(), is(1));
		beans.add(ac.getBean(PrototypeBean.class));
		assertThat(beans.size(), is(2));
		beans.add(ac.getBean(PrototypeClientBean.class).bean1);
		assertThat(beans.size(), is(3));
		beans.add(ac.getBean(PrototypeClientBean.class).bean2);
		assertThat(beans.size(), is(4));
		
		ac.close();
	}
	
	@Scope("prototype")
	static class PrototypeBean {}
	static class PrototypeClientBean {
		@Autowired PrototypeBean bean1;
		@Autowired PrototypeBean bean2;
	}

}
