package com.bigdata.yls_bigdata_hbase.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * @Author yls
 * @Description 连接客户端
 * @Date 2020/3/28 14:32
 * @return
 **/
@Component
public class HbaseClient {
	 public static Connection connection;
	 public void init(String clientPort,String zookeeperQuorum) {
	     Configuration configuration = HBaseConfiguration.create();
	     configuration.set("hbase.zookeeper.property.clientPort", clientPort);
	     // 如果是集群 则主机名用逗号分隔
	     configuration.set("hbase.zookeeper.quorum",zookeeperQuorum);
	        try {
	            connection = ConnectionFactory.createConnection(configuration);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
}
