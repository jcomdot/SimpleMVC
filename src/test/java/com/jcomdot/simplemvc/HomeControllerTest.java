package com.jcomdot.simplemvc;

import java.sql.SQLException;
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
import org.springframework.dao.EmptyResultDataAccessException;

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

	@Test(expected=EmptyResultDataAccessException.class)
	public void getActorFailure() throws SQLException {

		ApplicationContext context = new GenericXmlApplicationContext("spring/context/applicationContext.xml");
		ActorDao dao = context.getBean("actorDao", ActorDao.class);
		((GenericXmlApplicationContext) context).close();
		
		dao.deleteAddedRecords();
		assertThat(dao.getCount(), is(200));
		
		dao.get(201);

	}
	
	@Test
	public void count() {

		ApplicationContext context = new GenericXmlApplicationContext("spring/context/applicationContext.xml");
		ActorDao dao = context.getBean("actorDao", ActorDao.class);
		((GenericXmlApplicationContext) context).close();

		Actor actor1 = new Actor("호동", "강");
		Actor actor2 = new Actor("재석", "유");
		Actor actor3 = new Actor("경규", "이");

		try {
			dao.deleteAddedRecords();
			assertThat(dao.getCount(), is(200));
			
			dao.add(actor1);
			actor1.setActorId(dao.getLastIdx());
			dao.add(actor2);
			actor2.setActorId(dao.getLastIdx());
			assertThat(dao.getCount(), is(202));
			
			Actor actorGet1 = dao.get(actor1.getActorId());
			assertThat(actorGet1.getFirstName() , is(actor1.getFirstName()));
			assertThat(actorGet1.getLastName() , is(actor1.getLastName()));
			Actor actorGet2 = dao.get(actor2.getActorId());
			assertThat(actorGet2.getFirstName() , is(actor2.getFirstName()));
			assertThat(actorGet2.getLastName() , is(actor2.getLastName()));
			
			dao.deleteAddedRecords();
			assertThat(dao.getCount(), is(200));
			dao.add(actor3);
			assertThat(dao.getCount(), is(201));
			dao.deleteAddedRecords();
			assertThat(dao.getCount(), is(200));
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			assertThat(dao.getCount(), is(201));
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
