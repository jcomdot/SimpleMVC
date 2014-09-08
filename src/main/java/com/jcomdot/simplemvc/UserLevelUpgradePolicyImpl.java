package com.jcomdot.simplemvc;

import org.springframework.beans.factory.annotation.Autowired;

public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy {

	@Autowired private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
			case BASIC:		return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER:	return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD:		return false;
			default:		throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	@Override
	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

}
