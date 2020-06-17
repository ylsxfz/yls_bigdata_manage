package com.bigdata.yls_bigdata_zookeeper.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZookeeperController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperController.class);
	
	@GetMapping("/test/zookeeper_test")
	public String zookeeper() {
        try {
        	long start001 = System.currentTimeMillis();
        	
        	long start002 = System.currentTimeMillis();
            LOGGER.info("耗时："+(start002-start001)+"ms");
        } catch (Exception e) {
        	e.printStackTrace();
            LOGGER.error("失败。"+e.getMessage());
            return "失败！";
        } finally {
        }
		return "成功！";
    
	}
}
