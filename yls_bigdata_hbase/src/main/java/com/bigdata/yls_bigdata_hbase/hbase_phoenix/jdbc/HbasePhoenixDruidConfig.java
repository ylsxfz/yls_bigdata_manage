package com.bigdata.yls_bigdata_hbase.hbase_phoenix.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
 
/**
 * @Author yls
 * @Description 配置hbase_phoenix数据源
 * @Date 2020/3/28 14:34
 **/
@Configuration
@EnableConfigurationProperties({DataSourceProperties.class})//将配置类注入到bean容器，使ConfigurationProperties注解类生效
public class HbasePhoenixDruidConfig {
 
	    private static Logger logger = LoggerFactory.getLogger(HbasePhoenixDruidConfig.class);
 
	    @Autowired
	    private DataSourceProperties dataSourceProperties;

//	    @Autowired
//	    private DataSourceCommonProperties dataSourceCommonProperties;
 
	    @Bean("phoenixHbaseDruDataSource") //新建bean实例
	    @Qualifier("phoenixHbaseDruDataSource")//标识
	    public DataSource dataSource(){
	        DruidDataSource datasource = new DruidDataSource();
 
	        //配置数据源属性
	        datasource.setUrl(dataSourceProperties.getPhoenix().get("url"));
	        datasource.setUsername(dataSourceProperties.getPhoenix().get("username"));
	        datasource.setPassword(dataSourceProperties.getPhoenix().get("password"));
	        datasource.setDriverClassName(dataSourceProperties.getPhoenix().get("driver-class-name"));
	        
	        //配置统一属性
//	        datasource.setInitialSize(dataSourceCommonProperties.getInitialSize());
//	        datasource.setMinIdle(dataSourceCommonProperties.getMinIdle());
//	        datasource.setMaxActive(dataSourceCommonProperties.getMaxActive());
//	        datasource.setMaxWait(dataSourceCommonProperties.getMaxWait());
//	        datasource.setTimeBetweenEvictionRunsMillis(dataSourceCommonProperties.getTimeBetweenEvictionRunsMillis());
//	        datasource.setMinEvictableIdleTimeMillis(dataSourceCommonProperties.getMinEvictableIdleTimeMillis());
//	        datasource.setValidationQuery(dataSourceCommonProperties.getValidationQuery());
//	        datasource.setTestWhileIdle(dataSourceCommonProperties.isTestWhileIdle());
//	        datasource.setTestOnBorrow(dataSourceCommonProperties.isTestOnBorrow());
//	        datasource.setTestOnReturn(dataSourceCommonProperties.isTestOnReturn());
//	        datasource.setPoolPreparedStatements(dataSourceCommonProperties.isPoolPreparedStatements());
//	        try {
//	            datasource.setFilters(dataSourceCommonProperties.getFilters());
//	        } catch (SQLException e) {
//	            logger.error("Druid configuration initialization filter error.", e);
//	        }
	        return datasource;
	    }
	    
}