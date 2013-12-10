package com.jcomdot.simplemvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {
	
	@Bean
	public ActorDao actorDao() {
		ActorDao actorDao = new ActorDao();
		actorDao.setConnectionMaker(connectionMaker());
		return actorDao;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker());
	}

	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new PConnectionMaker();
	}
}
