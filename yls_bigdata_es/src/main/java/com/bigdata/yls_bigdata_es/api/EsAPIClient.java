package com.bigdata.yls_bigdata_es.api;

import com.bigdata.yls_bigdata_es.api.config.EsAPIConfig;
import com.bigdata.yls_bigdata_es.api.model.User;
import com.bigdata.yls_bigdata_es.api.utils.EsAPIUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.http.ResponseEntity;

import java.net.UnknownHostException;

/**
 * @Author yls
 * @Description 测试
 * @Date 2020/3/28 14:23
 **/
public class EsAPIClient {
	public static void main(String[] args) {
		try {
			TransportClient transportClient = EsAPIConfig.init();
			System.out.println("获取es客户端："+transportClient);
			
			/**
			 * 添加数据
			 */
			User user = new User();
			user.setAddress("四川省广元市青川县");
			user.setAge("22");
			user.setUsername("小朋友，你猜我是谁");
			EsAPIUtils.transportClient = transportClient;
			
			String index = "yls_es_test";
			String type = "yls";
			String id = "NkyPlHABz0JmEgJwQmBe";
			ResponseEntity<String> insert = EsAPIUtils.insert(index, type, id, user);
			System.out.println("添加数据：");
			System.out.println(insert);
			System.out.println("*****************************");
			System.out.println();
			
			/**
			 * 查询数据
			 */
			ResponseEntity<String> searchById = EsAPIUtils.searchById(index, type, id);
			System.out.println("查询数据：");
			System.out.println(searchById);
			System.out.println("*****************************");
			System.out.println();
			

			/**
			 * 修改数据
			 */
			
			user.setAddress("上海市浦东新区");
			try {
				System.out.println("更新数据：");
				ResponseEntity<String> update = EsAPIUtils.update(index, type, id, user);
				System.out.println(update);
				System.out.println("*****************************");
				System.out.println();
			}  catch (Exception e) {
				e.printStackTrace();
			}
			
			/**
			 * 查询数据
			 */
			searchById = EsAPIUtils.searchById(index, type, id);
			System.out.println("查询数据：");
			System.out.println(searchById);
			System.out.println("*****************************");
			System.out.println();
			
			
			/**
			 * 删除数据
			 */
			ResponseEntity<String> delById = EsAPIUtils.delById(index, type, id);
			System.out.println("删除数据：");
			System.out.println(delById);
			System.out.println("*****************************");
			System.out.println();
			
			
			/**
			 * 查询数据
			 */
			searchById = EsAPIUtils.searchById(index, type, id);
			System.out.println("查询数据：");
			System.out.println(searchById);
			System.out.println("*****************************");
			System.out.println();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
