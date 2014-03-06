package com.jcomdot.simplemvc;

import java.util.List;

public interface UserService {
	void add(User user);
	void update(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	
	void upgradeLevels();
}
