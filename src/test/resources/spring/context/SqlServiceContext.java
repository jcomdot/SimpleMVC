package spring.context;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.jcomdot.simplemvc.user.sqlservice.OxmSqlService;
import com.jcomdot.simplemvc.user.sqlservice.SqlRegistry;
import com.jcomdot.simplemvc.user.sqlservice.SqlService;
import com.jcomdot.simplemvc.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

@Configuration
public class SqlServiceContext {
	
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		Resource resource 
			= new DefaultResourceLoader().getResource("classpath:com/jcomdot/simplemvc/sqlmap.xml");
		sqlService.setSqlmap(resource);
		return sqlService;
	}
	
	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
			.setName("embeddedDatabase")
			.setType(HSQL)
			.addScript("classpath:com/jcomdot/simplemvc/user/sqlservice/updatable/sqlRegistrySchema.sql")
			.build();
	}
	
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}
	
	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.jcomdot.simplemvc.user.sqlservice.jaxb");
		return marshaller;
	}

}
