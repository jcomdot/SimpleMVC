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

	public void pointcutMatches(String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args)
			throws Exception {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);
		
		assertThat(pointcut.getClassFilter().matches(clazz)
				&& pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null), is(expected));
	}
	
	public void targetClassPointcutMatches(String expression, Boolean... expected) throws Exception {
		pointcutMatches(expression, expected[0], Target.class, "hello");
		pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
		pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
		pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
		pointcutMatches(expression, expected[4], Target.class, "method");
		pointcutMatches(expression, expected[5], Bean.class, "method");
	}
	
	@Test
	public void pointcut() throws Exception {
		targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
		targetClassPointcutMatches("execution(* hello(..))", true, true, false, false, false, false);
		targetClassPointcutMatches("execution(* hello())", true, false, false, false, false, false);
		targetClassPointcutMatches("execution(* hello(String))", false, true, false, false, false, false);
		targetClassPointcutMatches("execution(* meth*(..))", false, false, false, false, true, true);
		targetClassPointcutMatches("execution(* *(int,int))", false, false, true, true, false, false);
		targetClassPointcutMatches("execution(* *())", true, false, false, false, true, true);
		targetClassPointcutMatches("execution(* com.jcomdot.simplemvc.spring.pointcut.Target.*(..))", true, true, true, true, true, false);
		targetClassPointcutMatches("execution(* com.jcomdot.simplemvc.spring.pointcut.*.*(..))", true, true, true, true, true, true);
		targetClassPointcutMatches("execution(* com.jcomdot.simplemvc.spring.pointcut..*.*(..))", true, true, true, true, true, true);
		targetClassPointcutMatches("execution(* com.jcomdot..*.*(..))", true, true, true, true, true, true);
		targetClassPointcutMatches("execution(* kr.co..*.*(..))", false, false, false, false, false, false);
		targetClassPointcutMatches("execution(* *..Target.*(..))", true, true, true, true, true, false);
		targetClassPointcutMatches("execution(* *..Tar*.*(..))", true, true, true, true, true, false);
		targetClassPointcutMatches("execution(* *..*get.*(..))", true, true, true, true, true, false);
		targetClassPointcutMatches("execution(* *..B*.*(..))", false, false, false, false, false, true);
		targetClassPointcutMatches("execution(* *..TargetInterface.*(..))", true, true, true, true, false, false);
		targetClassPointcutMatches("execution(* *(..) throws Runtime*)", false, false, false, true, false, true);
		targetClassPointcutMatches("execution(int *(..))", false, false, true, true, false, false);
		targetClassPointcutMatches("execution(void *(..))", true, true, false, false, true, true);
	}
}
