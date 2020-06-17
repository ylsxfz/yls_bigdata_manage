package com.bigdata.yls_bigdata_hbase.hbase_phoenix.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * @Author yls
 * @Description  初始化客户端
 * @Date 2020/3/28 14:34
 **/
public class PhoenixJDBC_HbaseClient {
	public static void main(String[] args) {
		try {
	        // 加载数据库驱动
	        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");

	        /*
	         * 指定数据库地址,格式为 jdbc:phoenix:Zookeeper 地址
	         * 如果 HBase 采用 Standalone 模式或者伪集群模式搭建，则 HBase 默认使用内置的 Zookeeper，默认端口为 2181
	         */
	        Connection connection = DriverManager.getConnection("jdbc:phoenix:192.168.133.101:2181");

	        PreparedStatement statement = connection.prepareStatement("SELECT * FROM us_population");

	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            System.out.println(resultSet.getString("city") + " "
	                    + resultSet.getInt("population"));
	        }
	        statement.close();
	        connection.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
