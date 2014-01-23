package com.jcomdot.simplemvc;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class UserService {

	private DataSource dataSource;
	private UserDao userDao;
	private UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public UserLevelUpgradePolicy getUserLevelUpgradePolicy() {
		return this.userLevelUpgradePolicy;
	}
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}
	
	public void upgradeLevels() throws Exception {
		
		TransactionSynchronizationManager.initSynchronization();
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try {
			List<User> users = this.userDao.getAll();
			
			for (User user : users) {
				if (canUpgradeLevel(user))	upgradeLevel(user);
			}
		} catch (Exception e) {
			c.rollback();
			throw e;
		} finally {
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}

	protected void upgradeLevel(User user) {
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
