package com.jcomdot.simplemvc.spring.pointcut;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class PointcutExpressionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void showMethodSigniture() throws Exception {
		System.out.println(Target.class.getMethod("minus", int.class, int.class));
		System.out.println(Target.class.getMethod("plus", int.class, int.class));
		System.out.println(Bean.class.getMethod("method"));
	}
	
	@Test
	public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public int " + "com.jcomdot.simplemvc.spring.pointcut.Target.minus(int,int) " + 
				"throws java.lang.RuntimeException)");
		
		// Target.minus()
		assertThat(pointcut.getClassFilter().matches(Target.class) &&
				pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null),
				is(true));
		
		// Target.plus()
		assertThat(pointcut.getClassFilter().matches(Target.class) && 
				pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null),
				is(false));
		
		// Bean.method()
		assertThat(pointcut.getClassFilter().matches(Bean.class) &&
				pointcut.getMethodMatcher().matches(Bean.class.getMethod("method"), null), is(false));
	}

}
