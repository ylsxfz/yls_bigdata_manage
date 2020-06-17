package com.bigdata.yls_bigdata_redis.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yls
 * @Description 读取redis的节点和超时时间等配置
 * @Date 2020/3/29 19:22
 **/
@Configuration
@ConfigurationProperties(prefix = "redis.cache")
public class RedisProperties {
    private int expireSeconds;
    //集群节点
    private String clusterNodes;
    //超时时间
    private int commandTimeout;
    

	public int getExpireSeconds() {
		return expireSeconds;
	}


	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}


	public String getClusterNodes() {
		return clusterNodes;
	}


	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}


	public int getCommandTimeout() {
		return commandTimeout;
	}


	public void setCommandTimeout(int commandTimeout) {
		this.commandTimeout = commandTimeout;
	}


    
}
