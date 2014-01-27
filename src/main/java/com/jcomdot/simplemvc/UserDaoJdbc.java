package com.jcomdot.simplemvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
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
		this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend, email) values(?, ?, ?, ?, ?, ?, ?)",
				user.id, user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? " + 
			"where id = ?", user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
			user.getRecommend(), user.getEmail(), user.getId());
	}

	@Override
	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] {id}, this.rowMapper);
	}

	@Override
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users", this.rowMapper);
	}

	@Override
	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	@Override
	public int getCount() {
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

}
