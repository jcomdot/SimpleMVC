package com.jcomdot.simplemvc;

import java.util.List;

public class UserService {

	private UserDao userDao;
	private UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}
	
	public void upgradeLevels() {
		List<User> users = this.userDao.getAll();
		
		for (User user : users) {
			if (canUpgradeLevel(user))	upgradeLevel(user);
		}
	}

	private void upgradeLevel(User user) {
		userLevelUpgradePolicy.upgradeLevel(user);
	}
	
	private boolean canUpgradeLevel(User user) {
		return userLevelUpgradePolicy.canUpgradeLevel(user);
	}

	public void add(User user) {
		if (user.getLevel() == null)	user.setLevel(Level.BASIC);
		userDao.add(user);
	}
}
