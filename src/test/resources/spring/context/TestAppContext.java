package spring.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import com.jcomdot.simplemvc.DummyMailSender;
import com.jcomdot.simplemvc.UserLevelUpgradePolicy;
import com.jcomdot.simplemvc.UserLevelUpgradePolicyImpl;
import com.jcomdot.simplemvc.UserService;
import com.jcomdot.simplemvc.UserServiceTest.TestUserService;

@Configuration
public class TestAppContext {
	
	@Bean
	public UserService testUserService() {
		return new TestUserService();
	}

	@Bean
	public UserLevelUpgradePolicy userLevelUpgradePolicy() {
		return new UserLevelUpgradePolicyImpl();
	}

	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
	
}
