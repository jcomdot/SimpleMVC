package com.jcomdot.simplemvc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HomeControllerTest {

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
	public void testHome() {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, new Locale("ko", "KR"));
		
		String formattedDate = dateFormat.format(date);
		System.out.println(formattedDate);

		// DI(Dependency Injection)
//		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		ApplicationContext context = new GenericXmlApplicationContext("spring/context/applicationContext.xml");
		ActorDao dao = context.getBean("actorDao", ActorDao.class);
//		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
//		context.close();
		((GenericXmlApplicationContext) context).close();
		
		// DL(Dependency Lookup)
//		ActorDao dao = new ActorDao();
		
		Actor actor = new Actor();
		actor.setFirstName("Tobbung");
		actor.setLastName("Jang");

		try {
			dao.add(actor);
//			logger.debug("등록 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Actor actor2;
		try {
			int lastIdx = dao.getLastIdx();
			actor2 = dao.get(lastIdx);
			System.out.println(actor2.firstName + " " + actor2.lastName);
//			logger.debug("조회 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			dao.deleteAddedRecords();
			assertThat(dao.getCount(), is(200));
//			logger.debug("조회 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		fail("Not yet implemented");
	}

}
