package com.jcomdot.simplemvc;

import java.sql.*;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

public class ActorDao {

//	private ConnectionMaker connectionMaker;
	private DataSource dataSource;
	private JdbcContext jdbcContext;
	
//	public void setConnectionMaker(ConnectionMaker connectionMaker) {
//		this.connectionMaker = connectionMaker;
//	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}

	public void add(final Actor actor) throws ClassNotFoundException, SQLException {
		
		this.jdbcContext.workWithStatementStrategy(
			new StatementStrategy() {
				public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps 
						= conn.prepareStatement("insert into actor(first_name, last_name, last_update) values(?, ?, localtimestamp)");
					ps.setString(1, actor.getFirstName());
					ps.setString(2, actor.getLastName());
					
					return ps;
				}
			}
		);
	}
	
	public int getLastIdx() throws ClassNotFoundException, SQLException {
		int idx = -1;
		
		Connection conn = this.dataSource.getConnection();
		
		PreparedStatement ps = conn.prepareStatement("select max(actor_id) as last_value from actor");
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			idx = rs.getInt("last_value");
		}
		
		rs.close();
		ps.close();
		conn.close();
		
		return idx;
	}
	
	public Actor get(int id) throws SQLException {
		
		Actor actor = null;
		
		Connection conn = this.dataSource.getConnection();
		
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
		
		if (actor == null) throw new EmptyResultDataAccessException(1);
		
		return actor;
	}
	
	public void deleteAddedRecords() throws SQLException {

		this.jdbcContext.workWithStatementStrategy(
			new StatementStrategy() {
				public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
					return conn.prepareStatement("delete from actor where actor_id > 200");
				}
			}
		);
		
	}
	
	public void resetCount() throws SQLException {

		Connection conn = this.dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT setval('public.actor_actor_id_seq', 200, true)");
		ps.executeQuery();
		
		ps.close();
		conn.close();
	}
	
	public int getCount() throws SQLException {
		
		int ret = -1;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.dataSource.getConnection();
			ps = conn.prepareStatement("select count(*) from actor");
			
			rs = ps.executeQuery();
			if (rs.next()) {
				ret = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
		
		return ret;
	}

}
