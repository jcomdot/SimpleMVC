package com.jcomdot.simplemvc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring.context.AppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes=AppContext.class)
public class UserDaoTest {

	@Autowired private UserDao dao;
	@Autowired private DataSource dataSource;
	private User user1;
	private User user2;
	private User user3;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		this.user1 = new User("jhwatson", "John", "Watson", Level.BASIC, 1, 0, "jhwatson@jcomdot.com");
		this.user2 = new User("shholmes", "Sherlock", "Holmes", Level.SILVER, 55, 10, "shholmes@jcomdot.com");
		this.user3 = new User("acdoyle", "Conan", "Doyle", Level.GOLD, 100, 40, "acdoyle@jcomdot.com");

	}

	@After
	public void tearDown() throws Exception {
		this.dao.deleteAll();
	}

	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {

		this.dao.deleteAll();
		assertThat(this.dao.getCount(), is(0));
		
		this.dao.get(this.user1.getId());
	}
	
	@Test
	public void addAndGet() {

		this.dao.deleteAll();
		this.dao.add(this.user1);
		this.dao.add(this.user2);
		
		User userget1 = this.dao.get(this.user1.getId());
		this.checkSameUser(userget1, this.user1);
		
		User userget2 = this.dao.get(this.user2.getId());
		this.checkSameUser(userget2, this.user2);
	}
	
	@Test
	public void update() {
		
		this.dao.deleteAll();
		
		this.dao.add(this.user1);
		
		this.user1.setName("Mary Morstan");
		this.user1.setPassword("james");
		this.user1.setLevel(Level.GOLD);
		this.user1.setLogin(1000);
		this.user1.setRecommend(999);
		this.dao.update(this.user1);
		
		User user1update = this.dao.get(this.user1.getId());
		this.checkSameUser(user1update, this.user1);
	}
	
	@Test
	public void getAll() {
		this.dao.deleteAll();
		
		this.dao.add(this.user1);
		List<User> users1 = this.dao.getAll();
		assertThat(users1.size(), is(1));
		this.checkSameUser(this.user1, users1.get(0));
		
		this.dao.add(this.user2);
		List<User> users2 = this.dao.getAll();
		assertThat(users2.size(), is(2));
		this.checkSameUser(this.user1, users2.get(0));
		this.checkSameUser(this.user2, users2.get(1));
		
		this.dao.add(this.user3);
		List<User> users3 = this.dao.getAll();
		assertThat(users3.size(), is(3));
		this.checkSameUser(this.user1, users3.get(0));
		this.checkSameUser(this.user2, users3.get(1));
		this.checkSameUser(this.user3, users3.get(2));
	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
	
	@Test
	public void count() {

		try {
			this.dao.deleteAll();
			assertThat(this.dao.getCount(), is(0));
			
			this.dao.add(this.user1);
			this.dao.add(this.user2);
			this.dao.add(this.user3);
			assertThat(this.dao.getCount(), is(3));
			
			User actorGet1 = this.dao.get(this.user1.getId());
			assertThat(actorGet1.getName() , is(this.user1.getName()));
			assertThat(actorGet1.getPassword() , is(this.user1.getPassword()));
			User actorGet2 = this.dao.get(this.user2.getId());
			assertThat(actorGet2.getName() , is(this.user2.getName()));
			assertThat(actorGet2.getPassword() , is(this.user2.getPassword()));
			User actorGet3 = this.dao.get(this.user3.getId());
			assertThat(actorGet3.getName() , is(this.user3.getName()));
			assertThat(actorGet3.getPassword() , is(this.user3.getPassword()));
			
			this.dao.deleteAll();
			assertThat(this.dao.getCount(), is(0));
			this.dao.add(user3);
			assertThat(this.dao.getCount(), is(1));
			this.dao.deleteAll();
			assertThat(this.dao.getCount(), is(0));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test(expected=DataAccessException.class)
	public void duplicateKey() {
		this.dao.add(this.user1);
		User user = new User("jhwatson", "Watson", "John", Level.BASIC, 1, 0, "jhwatson@jcomdot.com");
		this.dao.add(user);
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void duplicateKeyWithoutExceptionExpected() {
		this.dao.add(this.user2);
		User user = new User("shholmes", "Sherlock", "Holmes", Level.SILVER, 55, 10, "shholmes@jcomdot.com");
		this.dao.add(user);
	}
	
	@Test
	public void sqlExeptionTranslate() {
		User user = new User("acdoyle", "Conan", "Doyle", Level.GOLD, 100, 40, "acdoyle@jcomdot.com");
		try {
			this.dao.add(user);
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

		User user = new User("jsjang", "Joonsong Jang", "test", Level.GOLD, 120, 50, "jsjang@jcomdot.com");

		try {
			this.dao.deleteAll();
			this.dao.add(user);
			assertThat(this.dao.getCount(), is(1));
//			logger.debug("등록 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		User user2;
		try {
			user2 = this.dao.get(user.getId());
			System.out.println(user2.getId() + ":" + user2.getName() + "(" + user2.getPassword() + ")");
//			logger.debug("조회 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.dao.deleteAll();
			assertThat(this.dao.getCount(), is(0));
//			logger.debug("조회 성공!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		fail("Not yet implemented");
	}

}
