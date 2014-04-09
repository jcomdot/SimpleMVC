package spring.context;

import javax.sql.DataSource;

import org.postgresql.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jcomdot.simplemvc.DummyMailSender;
import com.jcomdot.simplemvc.UserLevelUpgradePolicy;
import com.jcomdot.simplemvc.UserLevelUpgradePolicyImpl;
import com.jcomdot.simplemvc.UserService;
import com.jcomdot.simplemvc.UserServiceTest.TestUserService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="com.jcomdot.simplemvc")
@Import(SqlServiceContext.class)
public class AppContext {
	
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
	public UserLevelUpgradePolicy userLevelUpgradePolicy() {
		return new UserLevelUpgradePolicyImpl();
	}
	
	@Configuration
	@Profile("production")
	public static class ProductionAppContext {
		
		@Bean
		public MailSender mailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost("mail.jcomdot.com");
			return mailSender;
		}

	}
	
	@Configuration
	@Profile("test")
	public static class TestAppContext {
		
		@Bean
		public UserService testUserService() {
			return new TestUserService();
		}

		@Bean
		public MailSender mailSender() {
			return new DummyMailSender();
		}
		
	}
	
}
