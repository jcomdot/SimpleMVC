package springbook.learningtest.spring.hibernate;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

@Configuration
public class DataAccessConfig {
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.mariadb.jdbc.Driver.class);
		ds.setUrl("jdbc:mariadb://localhost/springbook");
		ds.setUsername("spring");
		ds.setPassword("book");
		return ds;
	}
	
	@Bean
	SessionFactory sessionFactory() {
		org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration().configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		return new LocalSessionFactoryBuilder(dataSource())
			.scanPackages("myproject.entity")
			.setProperty(SHOW_SQL, "hibernate.true")
			.setProperty(DIALECT, "org.hibernate.dialect.MySQL5Dialect")
			.setProperty(HBM2DDL_AUTO, "validate")
			.buildSessionFactory(serviceRegistry);
		
	}

}
