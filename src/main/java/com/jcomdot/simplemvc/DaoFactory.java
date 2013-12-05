package com.jcomdot.simplemvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
	
	@Bean
	public ActorDao actorDao() {
		return new ActorDao(connectionMaker());
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new PConnectionMaker();
	}
}
