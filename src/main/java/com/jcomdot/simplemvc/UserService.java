package com.jcomdot.simplemvc;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {
	void add(User user);
	void update(User user);
	@Transactional(readOnly=true) User get(String id);
	@Transactional(readOnly=true) List<User> getAll();
	void deleteAll();
	
	void upgradeLevels();
}
