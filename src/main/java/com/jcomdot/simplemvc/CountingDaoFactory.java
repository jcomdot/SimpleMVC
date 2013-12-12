package com.jcomdot.simplemvc;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class CountingDaoFactory {
	
	@Bean
	public ActorDao actorDao() {
		ActorDao actorDao = new ActorDao();
//		actorDao.setConnectionMaker(connectionMaker());
		actorDao.setDataSource(dataSource());
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

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mariadb://localhost/sakila");
		dataSource.setUsername("root");
		dataSource.setPassword("fkfkvhxm");
//		dataSource.setDriverClass(org.postgresql.Driver.class);
//		dataSource.setUrl("jdbc:postgresql://localhost/pagila");
//		dataSource.setUsername("postgres");
//		dataSource.setPassword("fkfkvhxm");
		
		return dataSource;
	}
}
