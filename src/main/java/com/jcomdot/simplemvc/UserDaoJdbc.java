package com.jcomdot.simplemvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;
	private Map<String, String> sqlMap;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	private RowMapper<User> rowMapper = 
		new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setLevel(Level.valueOf(rs.getShort("level")));
				user.setLogin(rs.getInt("login"));
				user.setRecommend(rs.getInt("recommend"));
				user.setEmail(rs.getString("email"));
				return user;
			}
		};

	@Override
	public void add(User user) {
		this.jdbcTemplate.update(this.sqlMap.get("add"),
				user.id, user.getName(), user.getPassword(), user.getEmail(),
				user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update(this.sqlMap.get("update"), 
				user.getName(), user.getPassword(), user.getEmail(),
				user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
	}

	@Override
	public User get(String id) {
		return this.jdbcTemplate.queryForObject(this.sqlMap.get("get"), new Object[] {id}, this.rowMapper);
	}

	@Override
	public List<User> getAll() {
		return this.jdbcTemplate.query(this.sqlMap.get("getAll"), this.rowMapper);
	}

	@Override
	public void deleteAll() {
		this.jdbcTemplate.update(this.sqlMap.get("deleteAll"));
	}

	@Override
	public int getCount() {
		return this.jdbcTemplate.queryForObject(this.sqlMap.get("getCount"), Integer.class);
	}

}
