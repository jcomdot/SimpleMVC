package com.jcomdot.simplemvc;

public class DaoFactory {
	
	public ActorDao actorDao() {
		return new ActorDao(connectionMaker());
	}
	
	private ConnectionMaker connectionMaker() {
		return new PConnectionMaker();
	}
}
