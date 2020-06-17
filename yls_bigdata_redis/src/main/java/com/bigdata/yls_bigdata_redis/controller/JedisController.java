package com.bigdata.yls_bigdata_redis.controller;

import com.bigdata.yls_bigdata_redis.jedis.JedisClientTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author yls
 * @Description 测试
 * @Date 2020/3/29 19:23
 **/
@RestController
public class JedisController {

	@GetMapping("/redis_jedis_test")
	public String dataExport() {
		try {
			long start001 = System.currentTimeMillis();
			JedisClientTemplate.setToRedis("yls", "springBoot整合redis。");
			System.out.println(JedisClientTemplate.getRedis("yls"));
			long start002 = System.currentTimeMillis();
			System.out.println("耗时：" + (start002 - start001) + "ms");
		} catch (Exception e) {
			System.out.println(e);
		} finally {

		}
		return "数据导出成功！";
	}
}
