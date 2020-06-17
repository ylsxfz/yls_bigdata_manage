package com.bigdata.yls_bigdata_es.api.utils;

import com.bigdata.yls_bigdata_es.api.model.User;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class EsAPIUtils {
	public static TransportClient transportClient;

	/**
	 * @Author yls
	 * @Description
	 * @Date 2020/3/28 14:23
	 * @param index 索引
	 * @param type 类型
	 * @param id id主键
	 * @param user 用户
	 * @return org.springframework.http.ResponseEntity<java.lang.String>
	 **/
	public static ResponseEntity<String> insert(String index, String type, String id, User user) {
		try {
			// 利用内置的json文档生成工具自动序列化放进去的属性
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("address", user.getAddress())
					.field("age", user.getAge()).field("username", user.getUsername()).endObject();
			// 查看序列化出来的JSON文档
			System.out.println(Strings.toString(builder));
			// 第一个参数是索引Index，第二个参数是类型Type(这个概念会在将来删除)，第三个参数是id，（都可缺省,ES自动生成）
			IndexResponse response = transportClient.prepareIndex(index, type).setSource(builder).get();
			return new ResponseEntity<>(response.getId(), HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	/**
	 * @Author yls
	 * @Description 根据id查询数据
	 * @Date 2020/3/28 14:22
	 * @param index 索引
	 * @param type 类型
	 * @param id id主键
	 * @return org.springframework.http.ResponseEntity<java.lang.String>
	 **/
	public static ResponseEntity<String> searchById(String index, String type, String id) {
		GetResponse response = transportClient.prepareGet(index, type, id).get();
		if (response.getSource()==null) {
			return new ResponseEntity<>("未查到数据！", HttpStatus.OK);
		}
		return new ResponseEntity<>(response.getSource().toString(), HttpStatus.OK);
	}

	/**
	 * @Author yls
	 * @Description 根据id删除数据
	 * @Date 2020/3/28 14:22
	 * @param index 索引
	 * @param type 类型
	 * @param id id主键
	 * @return org.springframework.http.ResponseEntity<java.lang.String>
	 **/
	public static ResponseEntity<String> delById(String index, String type, String id) {
		DeleteResponse deleteResponse = transportClient.prepareDelete(index, type, id).get();
		return new ResponseEntity<>(deleteResponse.getResult().toString(), HttpStatus.OK);
	}

	/**
	 * @Author yls
	 * @Description 更新数据
	 * @Date 2020/3/28 14:21
	 * @param index 索引
	 * @param type 类型
	 * @param id id
	 * @param user 对应的实体用户
	 * @return org.springframework.http.ResponseEntity<java.lang.String>
	 **/
	public static ResponseEntity<String> update(String index, String type, String id, User user)
			throws IOException, ExecutionException, InterruptedException {
		// 指定更新文档的所在索引、类型和id
		UpdateRequest updateRequest = new UpdateRequest(index, type, id);
		// 创建更新的文档
		XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
		if (StringUtils.isNotBlank(user.getUsername())) {
			xContentBuilder.field("username", user.getUsername());
		}
		if (StringUtils.isNotBlank(user.getAddress())) {
			xContentBuilder.field("address", user.getAddress());
		}
		xContentBuilder.endObject();
		updateRequest.doc(xContentBuilder);
		// 执行更新操作并获取执行结果
		UpdateResponse response = transportClient.update(updateRequest).get();
		return new ResponseEntity<>(response.getResult().toString(), HttpStatus.OK);
	}
}
