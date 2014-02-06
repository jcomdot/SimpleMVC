package com.jcomdot.simplemvc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static com.jcomdot.simplemvc.UserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static com.jcomdot.simplemvc.UserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:**/junit.xml")
//@ContextConfiguration(locations="classpath*:**/applicationContext.xml")
public class UserServiceTest {

	static class MockUserDao implements UserDao {

		private List<User> users;
		private List<User> updated = new ArrayList<User>();
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}

		public List<User> getUpdated() {
			return this.updated;
		}
		
		@Override
		public List<User> getAll() {
			return this.users;
		}

		@Override
		public void update(User user) {
			updated.add(user);
		}

		@Override
		public void add(User user) { throw new UnsupportedOperationException(); }
		@Override
		public User get(String id) { throw new UnsupportedOperationException(); }
		@Override
		public void deleteAll() { throw new UnsupportedOperationException(); }
		@Override
		public int getCount() { throw new UnsupportedOperationException(); }
	}

	static class MockMailSender implements MailSender {
		
		private List<String> requests = new ArrayList<String>();
		
		public List<String> getRequests() {
			return requests;
		}

		@Override
		public void send(SimpleMailMessage simpleMessage) throws MailException {
			requests.add(simpleMessage.getTo()[0]);
		}

		@Override
		public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		}

	}

	@Autowired UserService userService;
	@Autowired UserServiceImpl userServiceImpl;
	@Autowired UserDao userDao;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MailSender mailSender;
	List<User> users;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		users = Arrays.asList(
			new User("jhkim", "김준호", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "jsjang@outlook.com"),
			new User("dyshin", "신동엽", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "jcomdot@icloud.com"),
			new User("jsyoo", "유재석", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "jsjang@outlook.com"),
			new User("hdkang", "강호동", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "jcomdot@gmail.com"),
			new User("kglee", "이경규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "jsjang@outlook.com")
		);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertThat(this.userServiceImpl, is(notNullValue()));
	}
	
	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
//		userDao.deleteAll();
//		for (User user : this.users) this.userDao.add(user);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setUserLevelUpgradePolicy(this.userServiceImpl.getUserLevelUpgradePolicy());
		((UserLevelUpgradePolicyImpl)userServiceImpl.getUserLevelUpgradePolicy()).setUserDao(mockUserDao);
		userServiceImpl.setMailSender(mockMailSender);
		
//		this.userService.upgradeLevels();
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "dyshin", Level.SILVER);
		checkUserAndLevel(updated.get(1), "hdkang", Level.GOLD);
		
/*		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
*/	}

	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
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

	
	static class TestUserServiceException extends RuntimeException {
		private static final long serialVersionUID = 122233494726214926L;
	}

	static class TestUserService extends UserServiceImpl {
		
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}

		@Override
		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
	}

	@Test
	public void upgradeAllOrNothing() {
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setUserLevelUpgradePolicy(userServiceImpl.getUserLevelUpgradePolicy());
		testUserService.setMailSender(mailSender);
		
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);
		
		userDao.deleteAll();
		for (User user : users) userDao.add(user);
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected.");
		}
		catch(Exception e) {
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
}
