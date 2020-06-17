package com.bigdata.yls_bigdata_redis.controller;

import com.bigdata.yls_bigdata_redis.lettuce.LettuceClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author yls
 * @Description 测试
 * @Date 2020/3/29 19:23
 **/
@RestController
public class LettuceController {

	@Autowired
	private LettuceClientService lettuceClientService;

	@GetMapping("/redis_lettuce_test")
	public String lettuceClientTest() {
		String key = "redis-lettuce-01";
		boolean set = lettuceClientService.set(key, "hello world!");
		System.out.println("添加状态："+set);
		System.out.println("添加后查询："+lettuceClientService.get(key));
		lettuceClientService.remove(key);
		System.out.println("删除后查询："+lettuceClientService.get(key));
		return "测试成功";
	}
}
