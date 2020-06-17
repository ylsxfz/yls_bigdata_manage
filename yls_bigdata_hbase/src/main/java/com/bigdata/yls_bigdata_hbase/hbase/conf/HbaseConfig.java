package com.bigdata.yls_bigdata_hbase.hbase.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yls
 * @Description hbase的配置
 * @Date 2020/3/28 14:27
 **/
@Configuration
public class HbaseConfig {
	@Value("${hbase.zookeeper.quorum}")
    private String zookeeperQuorum;
 
    @Value("${hbase.zookeeper.property.clientPort}")
    private String clientPort;
    
    
    public void setClientPort(String clientPort) {
		this.clientPort = clientPort;
	}
    
    public void setZookeeperQuorum(String zookeeperQuorum) {
		this.zookeeperQuorum = zookeeperQuorum;
	}
    
    public String getClientPort() {
		return clientPort;
	}
    
    public String getZookeeperQuorum() {
		return zookeeperQuorum;
	}

	@Override
	public String toString() {
		return "HbaseConfig [zookeeperQuorum=" + zookeeperQuorum + ", clientPort=" + clientPort + "]";
	}
    
}
