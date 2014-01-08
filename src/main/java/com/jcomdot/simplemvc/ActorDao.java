package com.jcomdot.simplemvc;

import java.sql.*;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ActorDao {

	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private RowMapper<Actor> rowMapper = 
		new RowMapper<Actor>() {
			public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
				Actor actor = new Actor();
				actor.setActorId(rs.getInt("actor_id"));
				actor.setFirstName(rs.getString("first_name"));
				actor.setLastName(rs.getString("last_name"));
				actor.setLastUpdate(rs.getTimestamp("last_update"));
				return actor;
			}
		};
	
	public void add(final Actor actor) throws DuplicateActorIdException {
		try {
			this.jdbcTemplate.update("insert into actor(first_name, last_name, last_update) values(?, ?, localtimestamp)",
				actor.getFirstName(), actor.getLastName());
		} catch (DataAccessException e) {
			if (((SQLException)e.getCause()).getErrorCode() == 1062)
				throw new DuplicateActorIdException(e);
			else
				throw new RuntimeException(e);
		}
	}
	
	public int getLastIdx() {
		return this.jdbcTemplate.queryForObject("select max(actor_id) as last_value from actor", Integer.class);
	}
	
	public Actor get(int id) {
		return this.jdbcTemplate.queryForObject("select * from actor where actor_id = ?", new Object[] {id}, this.rowMapper);
	}
	
	public List<Actor> getAll() {
		return this.jdbcTemplate.query("select * from actor order by actor_id desc", this.rowMapper);
				
	}
	
	public void deleteAddedRecords() {
		this.jdbcTemplate.update("delete from actor where actor_id > 200");
		this.resetCount();
	}
	
	public void resetCount() {
		this.jdbcTemplate.execute("select setval('public.actor_actor_id_seq', 200, true)");
	}
	
	public int getCount() {
		return this.jdbcTemplate.queryForObject("select count(*) from actor", Integer.class);
	}

}
