package com.bigdata.yls_bigdata_redis.jedis;


import redis.clients.jedis.JedisCluster;

public class JedisClientTemplate {

	private static JedisCluster jedisCluster = JedisClusterClient.jedisCluster;
	
	//@Autowired
	//private JedisClusterClient jedisClusterClient;
	
	/**
	 * @Description 添加值
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setToRedis(String key, Object value) {
		try {
			//JedisCluster jedisCluster = jedisClusterClient.getJedisCluster();
			String str = jedisCluster.set(key, String.valueOf(value));
			if ("OK".equals(str)) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * @Description 获取值
	 * @param key
	 * @return
	 */
	public static Object getRedis(String key) {
		String str = null;
		try {
			//JedisCluster jedisCluster = jedisClusterClient.getJedisCluster();
			str = jedisCluster.get(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}

}
