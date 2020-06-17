package com.bigdata.yls_bigdata_es.rest;

import com.bigdata.yls_bigdata_es.api.model.User;
import com.bigdata.yls_bigdata_es.rest.config.EsRestConfig;
import com.bigdata.yls_bigdata_es.rest.controller.EsRestUtils;
import org.elasticsearch.client.RestClient;
import org.springframework.http.ResponseEntity;

/**
 * @Author yls
 * @Description 测试数据
 * @Date 2020/3/28 14:26
 * @return
 **/
public class EsRestClient {
	public static void main(String[] args) {
		try {
			RestClient restClient = EsRestConfig.init();
			EsRestUtils.restClient = restClient;
			
			ResponseEntity<String> esInfo = EsRestUtils.getEsInfo();
			System.out.println(esInfo);
			
			//ResponseEntity<String> asynchronous = EsRestUtils.asynchronous();
			//System.out.println(asynchronous);
			
			//Caused by: org.elasticsearch.client."error" : "Content-Type header [text/plain; charset=ISO-8859-1] is not supported",
			
			
			/**
			 * 添加数据
			 */
			User user = new User();
			user.setAddress("四川省成都市");
			user.setAge("25");
			user.setUsername("你猜我是谁");
			user.setId("2");
			String index = "yls_es_test";
			String type = "yls";
			String id = user.getId();
			ResponseEntity<String> add = EsRestUtils.add(index, type, id, user);
			System.out.println(add);
			
			ResponseEntity<String> searchById = EsRestUtils.getUserById(index, type, id);
			System.out.println(searchById);
			
			user.setAge("66");
			ResponseEntity<String> updateUser = EsRestUtils.updateUser(index, type, id, user);
			System.out.println(updateUser);
			searchById = EsRestUtils.getUserById(index, type, id);
			System.out.println(searchById);
			
			
			user.setUsername("一个有趣的傻子");
			ResponseEntity<String> updateUser2 = EsRestUtils.updateUser2(index, type, id, user);
			System.out.println(updateUser2);
			searchById = EsRestUtils.getUserById(index, type, id);
			System.out.println(searchById);
			
			ResponseEntity<String> deleteById = EsRestUtils.deleteById(index, type, id);
			System.out.println(deleteById);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
