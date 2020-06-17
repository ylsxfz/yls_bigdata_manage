package com.bigdata.yls_bigdata_redis.lettuce;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;

/**
 * @Author yls
 * @Description redis客户端Lettuce
 * @Date 2020/3/29 19:25
 **/
@Configuration
@EnableCaching  //启用缓存
public class LettuceCacheConfig extends CachingConfigurerSupport{
	
	/**
	 * @Author yls
	 * @Description 自定义缓存key的生成策略
	 * @Date 2020/3/29 19:25
	 * @return org.springframework.cache.interceptor.KeyGenerator
	 **/
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}
	
	/**
	 * @Author yls
	 * @Description 缓存配置管理器
	 * @Date 2020/3/29 19:25
	 * @param factory
	 * @return org.springframework.cache.CacheManager
	 **/
	@Bean
	public CacheManager cacheManager(LettuceConnectionFactory factory) {
		//以锁写入的方式创建RedisCacheWriter对象
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(factory);
		//创建默认的缓存配置对象
		RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfig);
		return redisCacheManager;
	}
	
	/**
	 * @Description 获取缓存操作助手对象
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory factory){
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL,Visibility.ANY);
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		
		
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
		
	}
	
}
