package com.jcomdot.simplemvc;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserService {

	private UserDao userDao;
	private UserLevelUpgradePolicy userLevelUpgradePolicy;
	private PlatformTransactionManager transactionManager;
	private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public UserLevelUpgradePolicy getUserLevelUpgradePolicy() {
		return this.userLevelUpgradePolicy;
	}
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void upgradeLevels() throws Exception {

		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			List<User> users = this.userDao.getAll();
			
			for (User user : users) {
				if (canUpgradeLevel(user))	upgradeLevel(user);
			}
			this.transactionManager.commit(status);
		} catch (Exception e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}

	protected void upgradeLevel(User user) {
		userLevelUpgradePolicy.upgradeLevel(user);
		sendUpgradeEmail(user);
	}
	
	private boolean canUpgradeLevel(User user) {
		return userLevelUpgradePolicy.canUpgradeLevel(user);
	}
	
	private void sendUpgradeEmail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("always@jcomdot.com");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText(user.getName() + "님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");
		
		this.mailSender.send(mailMessage);
	}

	public void add(User user) {
		if (user.getLevel() == null)	user.setLevel(Level.BASIC);
		userDao.add(user);
	}
}
