package com.jcomdot.simplemvc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:**/applicationContext.xml")
public class HomeControllerTest {

	@Autowired private ActorDao dao;
	@Autowired private DataSource dataSource;
	private Actor actor1;
	private Actor actor2;
	private Actor actor3;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		this.actor1 = new Actor("호동", "강");
		this.actor2 = new Actor("재석", "유");
		this.actor3 = new Actor("경규", "이");

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=EmptyResultDataAccessException.class)
	public void getActorFailure() throws SQLException {

		this.dao.deleteAddedRecords();
		assertThat(this.dao.getCount(), is(200));
		
		this.dao.get(201);

	}
	
	@Test
	public void getAll() {
		this.dao.deleteAddedRecords();
		
		this.dao.add(this.actor1);
		List<Actor> actors1 = this.dao.getAll();
		assertThat(actors1.size(), is(201));
		this.checkSameActor(this.actor1, actors1.get(0));
		
		this.dao.add(this.actor2);
		List<Actor> actors2 = this.dao.getAll();
		assertThat(actors2.size(), is(202));
		this.checkSameActor(this.actor1, actors2.get(1));
		this.checkSameActor(this.actor2, actors2.get(0));
		
		this.dao.add(this.actor3);
		List<Actor> actors3 = this.dao.getAll();
		assertThat(actors3.size(), is(203));
		this.checkSameActor(this.actor1, actors3.get(2));
		this.checkSameActor(this.actor2, actors3.get(1));
		this.checkSameActor(this.actor3, actors3.get(0));
		
		this.dao.deleteAddedRecords();
	}
	
	private void checkSameActor(Actor actor1, Actor actor2) {
		assertThat(actor1.getFirstName(), is(actor2.getFirstName()));
		assertThat(actor1.getLastName(), is(actor2.getLastName()));
	}
	
	@Test
	public void count() {

		try {
			this.dao.deleteAddedRecords();
			assertThat(this.dao.getCount(), is(200));
			
			this.dao.add(this.actor1);
			this.actor1.setActorId(this.dao.getLastIdx());
			this.dao.add(actor2);
			this.actor2.setActorId(this.dao.getLastIdx());
			this.dao.add(actor3);
			this.actor3.setActorId(this.dao.getLastIdx());
			assertThat(this.dao.getCount(), is(203));
			
			Actor actorGet1 = this.dao.get(this.actor1.getActorId());
			assertThat(actorGet1.getFirstName() , is(this.actor1.getFirstName()));
			assertThat(actorGet1.getLastName() , is(this.actor1.getLastName()));
			Actor actorGet2 = this.dao.get(this.actor2.getActorId());
			assertThat(actorGet2.getFirstName() , is(this.actor2.getFirstName()));
			assertThat(actorGet2.getLastName() , is(this.actor2.getLastName()));
			Actor actorGet3 = this.dao.get(this.actor3.getActorId());
			assertThat(actorGet3.getFirstName() , is(this.actor3.getFirstName()));
			assertThat(actorGet3.getLastName() , is(this.actor3.getLastName()));
			
			this.dao.deleteAddedRecords();
			assertThat(this.dao.getCount(), is(200));
			this.dao.add(actor3);
			assertThat(this.dao.getCount(), is(201));
			this.dao.deleteAddedRecords();
			assertThat(this.dao.getCount(), is(200));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test(expected=DataAccessException.class)
	public void duplicateKey() {
		Actor actor = new Actor("Watson", "John");
		actor.setActorId(200);
		this.dao.addWithId(actor);
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void duplicateKeyWithoutExceptionExpected() {
		Actor actor = new Actor("Sherlock", "Holmes");
		actor.setActorId(200);
		this.dao.addWithId(actor);
	}
	
	@Test
	public void sqlExeptionTranslate() {
		Actor actor = new Actor("Conan", "Doyle");
		actor.setActorId(200);
		try {
			this.dao.addWithId(actor);
		} catch (DuplicateKeyException e) {
			SQLException sqlEx = (SQLException)e.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			assertThat(set.translate(null, null, sqlEx), is(instanceOf(DuplicateKeyException.class)));
		}
	}
	
	@Test
	public void testHome() {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, new Locale("ko", "KR"));
		
		String formattedDate = dateFormat.format(date);
		System.out.println(formattedDate);

		Actor actor = new Actor();
		actor.setFirstName("Tobbung");
		actor.setLastName("Jang");

		try {
			this.dao.add(actor);
			assertThat(this.dao.getCount(), is(201));
//			logger.debug("등록 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Actor actor2;
		try {
			int lastIdx = this.dao.getLastIdx();
			actor2 = this.dao.get(lastIdx);
			System.out.println(actor2.firstName + " " + actor2.lastName);
//			logger.debug("조회 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.dao.deleteAddedRecords();
			assertThat(this.dao.getCount(), is(200));
//			logger.debug("조회 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		fail("Not yet implemented");
	}

}
