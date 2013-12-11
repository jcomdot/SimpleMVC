package com.jcomdot.simplemvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
	
	@Bean
	public ActorDao actorDao() {
		ActorDao actorDao = new ActorDao();
		actorDao.setConnectionMaker(connectionMaker());
		return actorDao;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new MConnectionMaker();
	}
}
