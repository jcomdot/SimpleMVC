package spring.context;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.*;

import javax.sql.DataSource;

import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jcomdot.simplemvc.*;
import com.jcomdot.simplemvc.UserServiceTest.TestUserService;
import com.jcomdot.simplemvc.user.sqlservice.*;
import com.jcomdot.simplemvc.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="com.jcomdot.simplemvc")
public class TestApplicationContext {
	
	@Autowired UserDao userDao;
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:postgresql://localhost/testdb");
		dataSource.setUsername("spring");
		dataSource.setPassword("test");
		
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		return tm;
	}

	@Bean
	public UserService testUserService() {
		TestUserService testService = new TestUserService();
		testService.setUserDao(this.userDao);
		testService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
		testService.setMailSender(mailSender());
		return testService;
	}

	@Bean
	public UserLevelUpgradePolicy userLevelUpgradePolicy() {
		UserLevelUpgradePolicyImpl userLevelUpgradePolicy = new UserLevelUpgradePolicyImpl();
		userLevelUpgradePolicy.setUserDao(this.userDao);
		return userLevelUpgradePolicy;
	}

	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
	
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		Resource resource 
			= new DefaultResourceLoader().getResource("classpath:com/jcomdot/simplemvc/sqlmap.xml");
		System.out.println(resource.toString());
		sqlService.setSqlmap(resource);
		return sqlService;
	}
	
	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
			.setName("embeddedDatabase")
			.setType(HSQL)
			.addScript("classpath:com/jcomdot/simplemvc/user/sqlservice/updatable/sqlRegistrySchema.sql")
			.build();
	}
	
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}
	
	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.jcomdot.simplemvc.user.sqlservice.jaxb");
		return marshaller;
	}

}
