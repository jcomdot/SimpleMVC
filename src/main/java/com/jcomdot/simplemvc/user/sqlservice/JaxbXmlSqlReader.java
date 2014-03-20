package com.jcomdot.simplemvc.user.sqlservice;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.jcomdot.simplemvc.UserDao;
import com.jcomdot.simplemvc.user.sqlservice.jaxb.SqlType;
import com.jcomdot.simplemvc.user.sqlservice.jaxb.Sqlmap;

public class JaxbXmlSqlReader implements SqlReader {

	public String sqlmapFile;
	
	public void setSqlmapFile(String sqlmapFile) { this.sqlmapFile = sqlmapFile; }
	
	@Override
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			
			for (SqlType sql : sqlmap.getSql()) {
				sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

}
