package com.jcomdot.simplespring;

import java.sql.*;

public class ActorDao {
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/pagila", "postgres", "fkfkvhxm");
		return conn;
	}
	
	public void add(Actor actor) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		
		PreparedStatement ps = conn.prepareStatement("insert into actor(first_name, last_name, last_update) values(?, ?, localtimestamp)");
		ps.setString(1, actor.getFirstName());
		ps.setString(2, actor.getLastName());
		
		ps.executeUpdate();
		
		ps.close();
		conn.close();
		
	}
	
	public int getLastIdx() throws ClassNotFoundException, SQLException {
		int idx = -1;
		
		Connection conn = getConnection();
		
		PreparedStatement ps = conn.prepareStatement("select last_value from actor_actor_id_seq");
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			idx = rs.getInt("last_value");
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return idx;
	}
	
	public Actor get(int id) throws ClassNotFoundException, SQLException {
		
		Actor actor = null;
		
		Connection conn = getConnection();
		
		PreparedStatement ps = conn.prepareStatement("select * from actor where actor_id = ?");
		ps.setInt(1, id);
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			actor = new Actor();
			actor.setActorId(rs.getInt("actor_id"));
			actor.setFirstName(rs.getString("first_name"));
			actor.setLastName(rs.getString("last_name"));
			actor.setLastUpdate(rs.getTimestamp("last_update"));
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return actor;
	}

}
