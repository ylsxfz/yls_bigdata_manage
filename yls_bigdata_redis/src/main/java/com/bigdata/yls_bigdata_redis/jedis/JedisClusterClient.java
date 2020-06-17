package com.bigdata.yls_bigdata_redis.jedis;

import com.bigdata.yls_bigdata_redis.conf.RedisProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

//@Configuration
@Component
public class JedisClusterClient {
    //@Autowired
   // private RedisProperties redisProperties;
	
	public static JedisCluster jedisCluster = null;
    
    /**
     * @Description jedis集群模式
     * @return
     */
//    public JedisCluster getJedisCluster(){
//        String [] serverArray=redisProperties.getClusterNodes().split(",");
//        Set<HostAndPort> nodes=new HashSet<>();
//
//        for (String ipPort:serverArray){
//            String [] ipPortPair=ipPort.split(":");
//            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));
//
//        }
//        return  new JedisCluster(nodes,redisProperties.getCommandTimeout());
//    }
	
	/**
	 * @Description 在项目启动的时候初始化
	 * @param redisProperties
	 */
	 public void  init(RedisProperties redisProperties){
	        String [] serverArray=redisProperties.getClusterNodes().split(",");
	        Set<HostAndPort> nodes=new HashSet<>();

	        for (String ipPort:serverArray){
	            String [] ipPortPair=ipPort.split(":");
	            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));

	        }
	        jedisCluster = new JedisCluster(nodes,redisProperties.getCommandTimeout());
	    }

}
