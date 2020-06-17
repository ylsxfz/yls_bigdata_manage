package com.bigdata.yls_bigdata_hbase.hbase_phoenix.jdbc;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
 
/**
 * @Author yls
 * @Description 统一属性控制类，获取配置文件属性
 * @Date 2020/3/28 14:33
 **/
@ConfigurationProperties(prefix = DataSourceProperties.DS, ignoreUnknownFields = false)
public class DataSourceProperties {
	final static String DS = "spring.datasource";
	
	private Map<String,String> hive;
	
	private Map<String,String> phoenix;
	
	private Map<String, String> mysqlMain;
	
	private Map<String,String> commonConfig;

	public Map<String, String> getHive() {
		return hive;
	}

	public void setHive(Map<String, String> hive) {
		this.hive = hive;
	}

	public Map<String, String> getCommonConfig() {
		return commonConfig;
	}

	public void setCommonConfig(Map<String, String> commonConfig) {
		this.commonConfig = commonConfig;
	}

	public static String getDs() {
		return DS;
	}

	public Map<String, String> getMysqlMain() {
		return mysqlMain;
	}

	public void setMysqlMain(Map<String, String> mysqlMain) {
		this.mysqlMain = mysqlMain;
	}
	
	
	public void setPhoenix(Map<String, String> phoenix) {
		this.phoenix = phoenix;
	}
	
	public Map<String, String> getPhoenix() {
		return phoenix;
	}
	
}