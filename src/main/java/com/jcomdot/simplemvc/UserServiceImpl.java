package com.jcomdot.simplemvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

//@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired private UserDao userDao;
	@Autowired private UserLevelUpgradePolicy userLevelUpgradePolicy;
	@Autowired private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public UserLevelUpgradePolicy getUserLevelUpgradePolicy() {
		return this.userLevelUpgradePolicy;
	}
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void upgradeLevels() {

		List<User> users = this.userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user))	upgradeLevel(user);
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

	@Override
	public void add(User user) {
		if (user.getLevel() == null)	user.setLevel(Level.BASIC);
		userDao.add(user);
	}

	@Override
	public void update(User user) { userDao.update(user); }

	@Override
	public User get(String id) { return userDao.get(id); }

	@Override
	public List<User> getAll() { return userDao.getAll(); }

	@Override
	public void deleteAll() { userDao.deleteAll(); }
}
