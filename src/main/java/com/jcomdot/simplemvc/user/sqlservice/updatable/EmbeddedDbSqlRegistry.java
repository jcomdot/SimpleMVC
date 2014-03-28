package com.jcomdot.simplemvc.user.sqlservice.updatable;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jcomdot.simplemvc.user.sqlservice.SqlNotFoundException;
import com.jcomdot.simplemvc.user.sqlservice.SqlUpdateFailureException;
import com.jcomdot.simplemvc.user.sqlservice.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

	JdbcTemplate jdbc;
	
	public void setDataSource(DataSource dataSource) {
		jdbc = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void registerSql(String key, String sql) {
		jdbc.update("insert into sqlmap(key_, sql_) values(?,?)", key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		// TODO Auto-generated method stub
		try {
			return jdbc.queryForObject("select sql_ from sqlmap where key_ = ?", String.class, key);
		} catch (EmptyResultDataAccessException e) {
			throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다", e);
		}
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		int affected = jdbc.update("update sqlmap set sql_ = ? where key_ = ?", sql, key);
		if (affected == 0) {
			throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다");
		}
	}

	@Override
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
			updateSql(entry.getKey(), entry.getValue());
		}

	}

}
