package com.jcomdot.simplemvc;

import static com.jcomdot.simplemvc.UserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static com.jcomdot.simplemvc.UserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spring.context.AppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes=AppContext.class)
@Transactional
@TransactionConfiguration(defaultRollback=false)
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

	@Autowired ApplicationContext context;
	@Autowired UserService userService;
	@Autowired UserService testUserService;
	@Autowired UserLevelUpgradePolicy userLevelUpgradePolicy;
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
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);
		((UserLevelUpgradePolicyImpl)userServiceImpl.getUserLevelUpgradePolicy()).setUserDao(mockUserDao);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "dyshin", Level.SILVER);
		checkUserAndLevel(updated.get(1), "hdkang", Level.GOLD);
		
	}
	
	@Test
	public void mockUpgradeLevels() throws Exception {
		
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		userServiceImpl.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);
		((UserLevelUpgradePolicyImpl)userServiceImpl.getUserLevelUpgradePolicy()).setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDao, times(2)).update(Matchers.any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
		
	}

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

	public static class TestUserService extends UserServiceImpl {
		
		private String id = "hdkang";
		
		@Override
		protected void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}

		@Override @Transactional(readOnly=true)
		public List<User> getAll() {
			for (User user : super.getAll()) {
				super.update(user);
			}
			return null;
		}
		
	}
	
	@Test(expected=UncategorizedSQLException.class)
	@Transactional(propagation=Propagation.NEVER)
	public void readOnlyTransactionAttribute() {
		testUserService.getAll();
	}
	
	@Test
	@Rollback
	public void transactionSync() {
		userService.deleteAll();
		userService.add(users.get(0));
		userService.add(users.get(1));
	}

	@Test
	@Transactional(propagation=Propagation.NEVER)
	public void upgradeAllOrNothing() throws Exception {
		userDao.deleteAll();
		for (User user : users) userDao.add(user);
		
		try {
			this.testUserService.upgradeLevels();
			fail("TestUserServiceException expected.");
		}
		catch(Exception e) {
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	@Test
	public void advisorAutoProxyCreator() {
		assertThat(testUserService, instanceOf(java.lang.reflect.Proxy.class));
	}

	@Autowired DefaultListableBeanFactory bf;
	
	@Test
	public void beans() {
		System.out.println("===Beans list(Start)================================================================");
		for (String n : bf.getBeanDefinitionNames()) {
			System.out.println(n + " \t " + bf.getBean(n).getClass().getName());
		}
		System.out.println("===Beans list(End)==================================================================");
	}
	
}
