package com.bigdata.yls_bigdata_hive.impl;

import com.bigdata.yls_bigdata_hive.dao.HiveJdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * -测试hive连接
 * @author yls
 * @Date 2019年10月12日
 *
 */
@Repository
public class HiveServiceImpl extends HiveJdbcBaseDaoImpl {
 
	/**
	 * 获取hive数据库数据信息
	 * @return
	 */
	public DataSource getDataSource() {
		DataSource dataSource = this.getJdbcTemplate().getDataSource();
		return dataSource;
	}
	
	/**
	 * 测试获取hive数据库数据信息
	 * @return
	 */
	public String test() {
		String sql = "SELECT sname from test limit 1";
//		String param = this.getJdbcTemplate().queryForObject(sql,String.class);
		String param = this.getJdbcTemplate().queryForObject(sql,String.class);
		return param;
	}
	
	public boolean uploadDataByHive(String sql ) throws SQLException {
//		this.getJdbcTemplate().execute(sql);
		this.getStatement().execute(sql);
		return true;
	}
	
}