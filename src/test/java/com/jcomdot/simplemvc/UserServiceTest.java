package com.jcomdot.simplemvc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static com.jcomdot.simplemvc.UserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static com.jcomdot.simplemvc.UserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

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
			new User("jhkim", "김준호", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("dyshin", "신동엽", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("jsyoo", "유재석", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
			new User("hdkang", "강호동", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User("kglee", "이경규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
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
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = this.userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}
		else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
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
