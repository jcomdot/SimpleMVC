package com.jcomdot.simplemvc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:**/applicationContext.xml")
public class UserServiceTest {

	@Autowired UserService userService;
	@Autowired UserDao userDao;
	List<User> users;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		users = Arrays.asList(
			new User("jhkim", "김준호", "p1", Level.BASIC, 49, 0),
			new User("dyshin", "신동엽", "p2", Level.BASIC, 50, 0),
			new User("jsyoo", "유재석", "p3", Level.SILVER, 60, 29),
			new User("hdkang", "강호동", "p4", Level.SILVER, 60, 30),
			new User("kglee", "이경규", "p5", Level.GOLD, 100, 100)
		);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() {
		this.userDao.deleteAll();
		for (User user : this.users)
			this.userDao.add(user);
		
		this.userService.upgradeLevels();
		
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	}

	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = this.userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
	
	@Test
	public void add() {
		this.userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}
	
}
