package com.jcomdot.simplemvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PConnectionMaker implements ConnectionMaker {

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/pagila", "postgres", "fkfkvhxm");

		return conn;
	}

}
