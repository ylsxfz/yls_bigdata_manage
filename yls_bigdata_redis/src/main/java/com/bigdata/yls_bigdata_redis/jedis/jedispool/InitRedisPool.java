package com.bigdata.yls_bigdata_redis.jedis.jedispool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Component("initRedisPool")
public class InitRedisPool {

	private static final Logger logger=LoggerFactory.getLogger(InitRedisPool.class);
	
	/**
	 * redis IP
	 */
	@Value("#{propertyConfigurer.redisIp}")
	private String host;
	
	/**
	 * redis端口
	 */
	@Value("#{propertyConfigurer.redisPort}")
	private int port;
	
	/**
	 * 数据库编号
	 */
	@Value("#{propertyConfigurer.dataBaseNumber}")
	private int dataBaseNumber;
	
	/**
	 * 最大空闲连接数
	 */
	@Value("#{propertyConfigurer.redisMaxIdle}")
	private int maxIdle;
	
	/**
	 * 最大连接数
	 */
	@Value("#{propertyConfigurer.redisMaxTotal}")
	private int maxTotal;
	
	/**
	 * 最大等待时间
	 */
	@Value("#{propertyConfigurer.redisMaxWaitMillis}")
	private long maxWaitMillis;
	
	@Value("#{propertyConfigurer.redisPassword}")
	private String password;
	
	@Autowired
	private IndependentRedisPool redisPool;
	
	@PostConstruct
	private void initPool() {
		
		try {
			JedisPoolConfig conf=new JedisPoolConfig();
			conf.setMaxIdle(maxIdle);
			conf.setMaxTotal(maxTotal);
			conf.setMaxWaitMillis(maxWaitMillis);
		    redisPool.setPool(new JedisPool(conf, host,port,10000,password,dataBaseNumber));
		    if(redisPool.getPool()!=null) {
		    	logger.info("初始化单机版redis成功!");
		    }else {
		    	throw new Exception("初始化单机版redis异常,系统即将停止!!!!");
		    }
		}catch(Exception e) {
			logger.error("初始化单机版redis异常!",e);
			System.exit(-1);
			
		}
	}
}
