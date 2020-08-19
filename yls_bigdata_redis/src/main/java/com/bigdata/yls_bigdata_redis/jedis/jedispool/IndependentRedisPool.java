package com.bigdata.yls_bigdata_redis.jedis.jedispool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * reids工具类(单机版)
 * @author qiu peng
 *该类实现了一些redis常用的方法,只需掉用getClient()方法获取连接从而获取Jedis对象,
 *在使用Jedis对象后务必调用close()方法关闭连接
 */
@Component("redisPool")
public class IndependentRedisPool {
	
	private static final Logger logger=LoggerFactory.getLogger(IndependentRedisPool.class);
	
	private JedisPool pool;
	
	
	public void setPool(JedisPool pool) {
		this.pool=pool;
	}
	
	public JedisPool getPool() {
		return this.pool;
	}
	//******************************String_start*******************************************//
    public void set(String key,String value) {
       Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+value+"]");
    		jedis=pool.getResource();
    		jedis.set(key, value);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+value+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
	
    public void set(String key,String value,int time) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+value+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.setex(key, time, value);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+value+",time="+time+"]",e);
    	}finally {
    			if(jedis!=null) {if(jedis!=null) {jedis.close();}}
    	
    	}
    }
    
    public void set(byte[] key,byte[] value) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+value+"]");
    		jedis=pool.getResource();
    		jedis.set(key, value);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+value+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void set(byte[] key,byte[] value,int time) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加jedis数据,[key="+key+",value="+value+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.setex(key, time, value);
    	}catch(Exception e) {
    		logger.error("添加数redis据异常,[key="+key+",value="+value+",time="+time+"]",e);
    	}finally {
    		if(jedis!=null) {if(jedis!=null) {jedis.close();}}
    	}
    }
    
    public String  get(String key) {
    	Jedis jedis=null;
    	String value=null;
    	try {
    		logger.debug("获取redis数据,[key="+key+"]");
    		jedis=pool.getResource();
    		value=jedis.get(key);
    	}catch(Exception e) {
    		logger.error("获取redis数据异常,[key="+key+"]", e);
    	}finally {
    		if(jedis!=null) {if(jedis!=null) {jedis.close();}}
    	}
    	return value;
    }
    
    public byte[]  get(byte[] key) {
    	Jedis jedis=null;
    	byte[] value=null;
    	try {
    		logger.debug("获取redis数据,[key="+key+"]");
    		jedis=pool.getResource();
    		value=jedis.get(key);
    	}catch(Exception e) {
    		logger.error("获取redis数据异常,[key="+key+"]", e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    	return value;
    }
    
    public void del(String ...key) {
    	Jedis jedis=null;
    	try {
    		logger.debug("删除redis数据,[key="+key+"]");
    		jedis=pool.getResource();
    		jedis.del(key);
    	}catch(Exception e) {
    		logger.error("删除redis数据异常,[key="+key+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public void del(byte[] ...key) {
    	Jedis jedis=null;
    	try {
    		logger.debug("删除redis数据,[key="+key+"]");
    		jedis=pool.getResource();
    		jedis.del(key);
    	}catch(Exception e) {
    		logger.error("删除redis数据异常,[key="+key+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    //***********************************List_start*************************************************//
    public void lpush(String key,String ...values) {
    	Jedis jedis=null;
    	try {
    		logger.info("添加redis数据,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.lpush(key, values);
    		logger.info("添加数据队列长度:"+jedis.lrange(key, 0, -1));
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void lpush(String key,int time,String ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.lpush(key, values);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+",time="+time+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void lpush(byte[] key,byte[] ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.lpush(key, values);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void lpush(byte[] key,int time,byte[] ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.lpush(key, values);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+",time="+time+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void rpush(String key,String ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.rpush(key, values);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void rpush(String key,int time,String ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.rpush(key, values);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+",time="+time+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void rpush(byte[] key,byte[] ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.rpush(key, values);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void rpush(byte[] key,int time,byte[] ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.rpush(key, values);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+",time="+time+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public String lpop(String key) {
    	Jedis jedis=null;
    	String value=null;
    	try {
    		logger.debug("获取redis数据,[key="+key+"]");
    		jedis= pool.getResource();
    	    value = jedis.lpop(key);
    	}catch(Exception e) {
    		logger.error("获取redis数据异常,[key="+key+"]", e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value;
    }
    
    public byte[] lpop(byte[] key) {
    	Jedis jedis=null;
    	byte[] value=null;
    	try {
    		logger.debug("获取redis数据,[key="+key+"]");
    		jedis= pool.getResource();
    	    value = jedis.lpop(key);
    	}catch(Exception e) {
    		logger.error("获取redis数据异常,[key="+key+"]", e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value;
    }
    
    public String rpop(String key) {
    	Jedis jedis=null;
    	String value=null;
    	try {
    		logger.debug("获取redis数据,[key="+key+"]");
    		jedis= pool.getResource();
    	    value = jedis.rpop(key);
    	}catch(Exception e) {
    		logger.error("获取redis数据异常,[key="+key+"]", e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value;
    }
    
    public byte[] rpop(byte[] key) {
    	Jedis jedis=null;
    	byte[] value=null;
    	try {
    		logger.debug("获取redis数据,[key="+key+"]");
    		jedis= pool.getResource();
    	    value = jedis.rpop(key);
    	}catch(Exception e) {
    		logger.error("获取redis数据异常,[key="+key+"]", e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value;
    }
    
    public List<String> brpop(String key,int time) {
    	Jedis jedis=null;
    	List<String> value=null;
    	try {
    		logger.debug("获取redis数据,[key="+key+",time="+time+"]");
    		jedis= pool.getResource();
    	    value = jedis.brpop(time,key);
    	}catch(Exception e) {
    		logger.error("获取redis数据异常,[key="+key+",time="+time+"]", e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value;
    }
    
    public long lSize(String key) {
    	Jedis jedis=null;
    	List<String> value=null;
    	try {
    		logger.debug("获取List长度,[key="+key+"]");
    		jedis= pool.getResource();
    	    value = jedis.lrange(key, 0, -1);
    	}catch(Exception e) {
    		logger.error("获取List长度异常,[key="+key+"]", e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value.size();
    }
    /****************************************set_start*******************************************************/
    public void sSet(String key,String ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.sadd(key, values);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void sSet(byte[] key,byte[] ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.sadd(key, values);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void sSet(String key,int time,String ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.sadd(key, values);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public void sSet(byte[] key,int time,byte[] ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+values+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.sadd(key, values);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+values+",time="+time+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
    	}
    }
    
    public Long sSize(String key) {
    	Jedis jedis=null;
    	Long size=-1L;
    	try {
    		logger.debug("获取redis set集合元素个数,[key="+key+"]");
    		jedis=pool.getResource();
    		size=jedis.scard(key);
    	}catch(Exception e) {
    		logger.error("获取redis set集合元素个数异常,[key="+key+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return size;
    }
    
    public Long sSize(byte[] key) {
    	Jedis jedis=null;
    	Long size=-1L;
    	try {
    		logger.debug("获取redis set集合元素数量,[key="+key+"]");
    		jedis=pool.getResource();
    		size=jedis.scard(key);
    	}catch(Exception e) {
    		logger.error("获取redis set集合元素数量异常,[key="+key+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return size;
    }
    
    public String spop(String key) {
    	Jedis jedis=null;
    	String value=null;
    	try {
    		logger.debug("随机获取redis set集合元素,[key="+key+"]");
    		jedis=pool.getResource();
    		value=jedis.spop(key);
    	}catch(Exception e) {
    		logger.error("随机获取redis set集合元素异常,[key="+key+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value;
    }
    public byte[] spop(byte[] key) {
    	Jedis jedis=null;
    	byte[] value=null;
    	try {
    		logger.debug("随机获取redis set集合元素,[key="+key+"]");
    		jedis=pool.getResource();
    		value=jedis.spop(key);
    	}catch(Exception e) {
    		logger.error("随机获取redis set集合元素异常,[key="+key+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return value;
    }
    public Set<String> getSetAllData(String key){
    	Jedis jedis=null;
    	Set<String> data=null;
    	try {
    		logger.debug("获取redis set集合所有元素,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.smembers(key);
    	}catch(Exception e) {
    		logger.debug("获取redis set集合所有元素异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public Set<byte[]> getSetAllData(byte[] key){
    	Jedis jedis=null;
    	Set<byte[]> data=null;
    	try {
    		logger.debug("获取redis set集合所有元素,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.smembers(key);
    	}catch(Exception e) {
    		logger.debug("获取redis set集合所有元素异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public  void sRemove(String key,String ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("移除redis set集合元素,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.srem(key, values);
    	}catch(Exception e) {
    		logger.debug("移除redis set集合元素异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public  void sRemove(byte[] key,byte[] ...values) {
    	Jedis jedis=null;
    	try {
    		logger.debug("移除redis set集合元素,[key="+key+",value="+values+"]");
    		jedis=pool.getResource();
    		jedis.srem(key, values);
    	}catch(Exception e) {
    		logger.debug("移除redis set集合元素异常,[key="+key+",value="+values+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    /***************************************hash_start*******************************************************/
    public void hmset(String key,Map<String,String> hash) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+hash+"]");
    		jedis=pool.getResource();
    		jedis.hmset(key, hash);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+hash+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    public void hmset(byte[] key,Map<byte[],byte[]> hash) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+hash+"]");
    		jedis=pool.getResource();
    		jedis.hmset(key, hash);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+hash+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public void hmset(String key,Map<String,String> hash,int time) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+hash+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.hmset(key, hash);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+hash+",time="+time+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    public void hmset(byte[] key,Map<byte[],byte[]> hash,int time) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",value="+hash+",time="+time+"]");
    		jedis=pool.getResource();
    		jedis.hmset(key, hash);
    		jedis.expire(key, time);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",value="+hash+",time="+time+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public void hset(String key,String field,String value) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",field="+field+",value="+value+"]");
    		jedis=pool.getResource();
    		jedis.hset(key, field, value);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",field="+field+",value="+value+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public void hset(byte[] key,byte[] field,byte[] value) {
    	Jedis jedis=null;
    	try {
    		logger.debug("添加redis数据,[key="+key+",field="+field+",value="+value+"]");
    		jedis=pool.getResource();
    		jedis.hset(key, field, value);
    	}catch(Exception e) {
    		logger.error("添加redis数据异常,[key="+key+",field="+field+",value="+value+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public Map<String,String> hgetAll(String key){
    	Jedis jedis=null;
    	Map<String,String> data=null;
    	try {
    		logger.debug("获取redis hash集合所有元素,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.hgetAll(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合所有元素,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public Map<byte[],byte[]> hgetAll(byte[] key){
    	Jedis jedis=null;
    	Map<byte[],byte[]> data=null;
    	try {
    		logger.debug("获取redis hash集合所有元素,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.hgetAll(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合所有元素,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public List<String> hmget(String key,String ...fields) {
    	Jedis jedis=null;
    	List<String> data=null;
    	try {
    		logger.debug("获取redis hash集合元素,[key="+key+",fields="+fields+"]");
    		jedis=pool.getResource();
    		data=jedis.hmget(key, fields);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合元素异常,[key="+key+",fields="+fields+"]",e);
    	}finally {
			if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public List<byte[]> hmget(byte[] key,byte[] ...fields) {
    	Jedis jedis=null;
    	List<byte[]> data=null;
    	try {
    		logger.debug("获取redis hash集合元素,[key="+key+",fields="+fields+"]");
    		jedis=pool.getResource();
    		data=jedis.hmget(key, fields);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合元素异常,[key="+key+",fields="+fields+"]",e);
    	}finally {
			if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public Set<String> hkeys(String key){
    	Jedis jedis=null;
    	Set<String> data=null;
    	try {
    		logger.debug("获取redis hash集合所有field,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.hkeys(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合所有field异常,[key="+key+"]",e);
    	}finally {
			if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public Set<byte[]> hkeys(byte[] key){
    	Jedis jedis=null;
    	Set<byte[]> data=null;
    	try {
    		logger.debug("获取redis hash集合所有field,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.hkeys(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合所有field异常,[key="+key+"]",e);
    	}finally {
			if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public List<String> hvalues(String key){
    	Jedis jedis=null;
    	List<String> data=null;
    	try {
    		logger.debug("获取redis hash集合所有value,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.hvals(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合所有value异常,[key="+key+"]",e);
    	}finally {
			if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    public List<byte[]> hvalues(byte[] key){
    	Jedis jedis=null;
    	List<byte[]> data=null;
    	try {
    		logger.debug("获取redis hash集合所有value,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.hvals(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合所有value异常,[key="+key+"]",e);
    	}finally {
			if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public void  hdel(String key,String ...fields) {
    	Jedis jedis=null;
    	try {
    		logger.debug("删除redis hash集合元素,[key="+key+",field="+fields+"]");
    		jedis=pool.getResource();
    		jedis.hdel(key, fields);
    	}catch(Exception e) {
    		logger.error("删除redis hash集合元素异常,[key="+key+",field="+fields+"]",e);
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public void  hdel(byte[] key,byte[] ...fields) {
    	Jedis jedis=null;
    	try {
    		logger.debug("删除redis hash集合元素,[key="+key+",field="+fields+"]");
    		jedis=pool.getResource();
    		jedis.hdel(key, fields);
    	}catch(Exception e) {
    		logger.error("删除redis hash集合元素异常,[key="+key+",field="+fields+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    }
    
    public Long hsize(String key) {
    	Jedis jedis=null;
    	Long size=-1L;
    	try {
    		logger.debug("获取redis hash集合元素数量,[key="+key+"]");
    		jedis=pool.getResource();
    	    size = jedis.hlen(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合元素数量异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return size;
    }
    
    public Long hsize(byte[] key) {
    	Jedis jedis=null;
    	Long size=-1L;
    	try {
    		logger.debug("获取redis hash集合元素数量,[key="+key+"]");
    		jedis=pool.getResource();
    	    size = jedis.hlen(key);
    	}catch(Exception e) {
    		logger.error("获取redis hash集合元素数量异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return size;
    }
    
    public boolean hexists(String key,String field) {
    	Jedis jedis=null;
    	boolean flag=false;
    	try {
    		logger.debug("判断redis hash集合元素是否存在,[key="+key+"]");
    		jedis=pool.getResource();
    		flag = jedis.hexists(key, field);
    	}catch(Exception e) {
    		logger.error("判断redis hash集合元素是否存在异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return flag;
    }
    
    public boolean hexists(byte[] key,byte[] field) {
    	Jedis jedis=null;
    	boolean flag=false;
    	try {
    		logger.debug("判断redis hash集合元素是否存在,[key="+key+"]");
    		jedis=pool.getResource();
    		flag = jedis.hexists(key, field);
    	}catch(Exception e) {
    		logger.error("判断redis hash集合元素是否存在异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return flag;
    }
    
    /**************************************key操作*************************************************/
   
    /**警告:该方法需要谨慎使用,如果redis的key的数量过多使用该方法会导致,队列堵塞
     * redis死机等问题
     * 
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
    	Jedis jedis=null;
    	Set<String> data=null;
    	try {
    		logger.debug("查询key,[key="+key+"]");
    		jedis=pool.getResource();
    		data=jedis.keys(key);
    	}catch(Exception e) {
    		logger.debug("查询key异常,[key="+key+"]");
    	}finally {
			if(jedis!=null) {jedis.close();}
		}
    	return data;
    }
    
    public boolean exists(String key) {
    	Jedis jedis=null;
    	boolean flag=false;
    	try {
    		logger.debug("判断key是否存在,[key="+key+"]");
    		jedis=pool.getResource();
    		flag=jedis.exists(key);
    	}catch(Exception e) {
    		logger.error("判断key是否存在异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return flag;
    }
    
    public boolean exists(byte[] key) {
    	Jedis jedis=null;
    	boolean flag=false;
    	try {
    		logger.debug("判断key是否存在,[key="+key+"]");
    		jedis=pool.getResource();
    		flag=jedis.exists(key);
    	}catch(Exception e) {
    		logger.error("判断key是否存在异常,[key="+key+"]");
    	}finally {
    		if(jedis!=null) {jedis.close();}
		}
    	return flag;
    }
	/**获取客户端
	 * 
	 * @return jedis
	 */
	public Jedis getClient() {
		return pool.getResource();
	}
	
	/**释放连接
	 * 
	 * @param jedis
	 */
	public void close(Jedis jedis) {
		if(jedis!=null) {jedis.close();}
	}
	
}
