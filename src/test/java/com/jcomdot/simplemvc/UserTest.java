package com.jcomdot.simplemvc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	User user;
	
	@Before
	public void setUp() throws Exception {
		user = new User();
	}

	@Test
	public void upgradeLevel() {
		Level[] levels = Level.values();
		for (Level level : levels) {
			if (level.nextLevel() == null) continue;
			user.setLevel(level);
			user.upgradeLevel();
			assertThat(user.getLevel(), is(level.nextLevel()));
		}
	}

	@Test(expected=IllegalStateException.class)
	public void cannotupgradeLevel() {
		Level[] levels = Level.values();
		for (Level level : levels) {
			if (level.nextLevel() != null) continue;
			user.setLevel(level);
			user.upgradeLevel();
		}
	}
}
