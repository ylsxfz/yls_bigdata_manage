package com.bigdata.yls_bigdata_hive.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

/**
* -注入hive数据源
 * @author yls
 * @Date 2019年10月12日
*
*/
@Repository
public class HiveJdbcBaseDaoImpl {
	
	private JdbcTemplate jdbcTemplate;

	private Statement statement;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public Statement getStatement() {
		return statement;
	}

	@Autowired
	public void setJdbcTemplate(@Qualifier("hiveDruidDataSource") DataSource dataSource) throws SQLException {
		this.statement = dataSource.getConnection().createStatement();
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
}