package com.jcomdot.simplemvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MConnectionMaker implements ConnectionMaker {

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("org.mariadb.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/sakila", "root", "fkfkvhxm");

		return conn;
	}

}
