package com.bigdata.yls_bigdata_hbase.hbase_phoenix.integration;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class HbasePhoenixServiceImpl extends HbasePhoenixDaoImpl{

	
	/**
	 * @Author yls
	 * @Description 查询所有
	 * @Date 2020/3/28 14:33
	 * @return java.util.List<com.bigdata.yls_bigdata_hbase.hbase_phoenix.integration.USPopulation>
	 **/
	public List<USPopulation> queryAll(){
		String sql = "SELECT * from us_population";
		return this.getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper(USPopulation.class));
	}

	public DataSource getDataSource() {
		return this.getJdbcTemplate().getDataSource();
	}
}
