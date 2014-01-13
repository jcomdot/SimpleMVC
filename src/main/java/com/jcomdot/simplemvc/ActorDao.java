package com.jcomdot.simplemvc;

import java.util.List;

public interface ActorDao {
	void add(Actor actor);
	void addWithId(Actor actor);
	int getLastIdx();
	Actor get(int id);
	List<Actor> getAll();
	void deleteAddedRecords();
	int getCount();
}
