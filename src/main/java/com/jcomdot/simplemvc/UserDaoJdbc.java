package com.jcomdot.simplemvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jcomdot.simplemvc.user.sqlservice.SqlService;

@Repository
public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;
	@Autowired private SqlService sqlService;
	
	@Autowired
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
		this.jdbcTemplate.update(this.sqlService.getSql("userAdd"),
				user.id, user.getName(), user.getPassword(), user.getEmail(),
				user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"), 
				user.getName(), user.getPassword(), user.getEmail(),
				user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
	}

	@Override
	public User get(String id) {
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), new Object[] {id}, this.rowMapper);
	}

	@Override
	public List<User> getAll() {
		return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.rowMapper);
	}

	@Override
	public void deleteAll() {
		this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
	}

	@Override
	public int getCount() {
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
	}

}
