package com.bigdata.yls_bigdata_es.rest.controller;

import com.bigdata.yls_bigdata_es.api.model.User;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


public class EsRestUtils {
	public static RestClient restClient;


	/**
	 * @Author yls
	 * @Description 同步执行
	 * @Date 2020/3/28 14:24
	 * @return org.springframework.http.ResponseEntity<java.lang.String>
	 **/
	public static ResponseEntity<String> getEsInfo() throws IOException {
		// 构造HTTP请求，第一个参数是请求方法，第二个参数是服务器的端点，host默认是http://localhost:9200
		Request request = new Request("GET", "/");
		// // 设置其他一些参数比如美化json
		// request.addParameter("pretty", "true");

		// // 设置请求体
		// request.setEntity(new NStringEntity("{\"json\":\"text\"}",
		// ContentType.APPLICATION_JSON));

		// // 还可以将其设置为String，默认为ContentType为application/json
		// request.setJsonEntity("{\"json\":\"text\"}");

		/*
		 * performRequest是同步的，将阻塞调用线程并在请求成功时返回Response，如果失败则抛出异常 内部属性可以取出来通过下面的方法
		 */
		Response response = restClient.performRequest(request);
		// // 获取请求行
		// RequestLine requestLine = response.getRequestLine();
		// // 获取host
		// HttpHost host = response.getHost();
		// // 获取状态码
		// int statusCode = response.getStatusLine().getStatusCode();
		// // 获取响应头
		// Header[] headers = response.getHeaders();
		// 获取响应体
		String responseBody = EntityUtils.toString(response.getEntity());
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	/**
	 * @Author yls
	 * @Description 异步执行HTTP请求
	 * @Date 2020/3/28 14:24
	 * @return org.springframework.http.ResponseEntity<java.lang.String>
	 **/
	public static ResponseEntity<String> asynchronous() {
		Request request = new Request("GET", "/");
		restClient.performRequestAsync(request, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				System.out.println("异步执行HTTP请求并成功");
			}

			@Override
			public void onFailure(Exception exception) {
				System.out.println("异步执行HTTP请求并失败");
			}
		});
		return null;
	}
	
	 /**
	  * @Author yls
	  * @Description 并行异步执行HTTP请求
	  * @Date 2020/3/28 14:24
	  * @param documents
	  * @return void
	  **/
    public void parallAsyn(User[] documents) {
//        final CountDownLatch latch = new CountDownLatch(documents.length);
//        for (int i = 0; i < documents.length; i++) {
//            Request request = new Request("PUT", "/posts/doc/" + i);
//            //let's assume that the documents are stored in an HttpEntity array
//            request.setEntity(documents[i]);
//            client.performRequestAsync(
//                    request,
//                    new ResponseListener() {
//                        @Override
//                        public void onSuccess(Response response) {
//
//                            latch.countDown();
//                        }
//
//                        @Override
//                        public void onFailure(Exception exception) {
//
//                            latch.countDown();
//                        }
//                    }
//            );
//        }
//        latch.await();
    }

    /**
     * @Author yls
     * @Description 添加ES对象, Book的ID就是ES中存储的document的ID，所以最好不要为空，自定义生成的ID太浮夸
     * @Date 2020/3/28 14:25
     * @param index 索引
     * @param type 类型
     * @param id id主键
     * @param user 实体同户
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     **/
    public static ResponseEntity<String> add(String index,String type,String id,User user) throws IOException, JSONException {
        // 构造HTTP请求，第一个参数是请求方法，第二个参数是服务器的端点，host默认是http://localhost:9200，
        // endpoint直接指定为index/type的形式
        Request request = new Request("POST", new StringBuilder("/"+index+"/"+type+"/").
                append(user.getId()).toString());
        // 设置其他一些参数比如美化json
        request.addParameter("pretty", "true");
        JSONObject jsonObject = new JSONObject(user.toString());
        System.out.println(jsonObject.toString());
        // 设置请求体并指定ContentType，如果不指定默认为APPLICATION_JSON
        request.setEntity(new NStringEntity(jsonObject.toString(),"UTF-8"));
        
        // 发送HTTP请求
        Response response = restClient.performRequest(request);

        // 获取响应体, id: AWXvzZYWXWr3RnGSLyhH
        String responseBody = EntityUtils.toString(response.getEntity());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @Author yls
     * @Description 根据id获取ES对象
     * @Date 2020/3/28 14:25
     * @param index 索引
     * @param type 类型
     * @param id id主键
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     **/
    public static ResponseEntity<String> getUserById(String index,String type,String id) {
        Request request = new Request("GET", new StringBuilder("/"+index+"/"+type+"/").
                append(id).toString());
        // 添加json返回优化
        request.addParameter("pretty", "true");
        Response response = null;
        String responseBody = null;
        try {
            // 执行HHTP请求
            response = restClient.performRequest(request);
            responseBody = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return new ResponseEntity<>("can not found the book by your id", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @Author yls
     * @Description 根据id更新Book
     * @Date 2020/3/28 14:25
     * @param index 索引
     * @param type 类型
     * @param id id主键
     * @param user 实体用户
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     **/
    public static ResponseEntity<String> updateUser(String index,String type,String id, User user) throws IOException, JSONException {
        // 构造HTTP请求
        Request request = new Request("POST", new StringBuilder("/"+index+"/"+type+"/").
                append(id).append("/_update").toString());
        request.addParameter("pretty", "true");

        // 将数据丢进去，这里一定要外包一层“doc”，否则内部不能识别
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("doc", new JSONObject(user.toString()));
        request.setEntity(new NStringEntity(jsonObject.toString(),"UTF-8"));

        // 执行HTTP请求
        Response response = restClient.performRequest(request);

        // 获取返回的内容
        String responseBody = EntityUtils.toString(response.getEntity());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @Author yls
     * @Description 使用脚本更新Book
     * @Date 2020/3/28 14:26
     * @param index 索引
     * @param type 类型
     * @param id id主键
     * @param user 实体对象
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     **/
    public static ResponseEntity<String> updateUser2(String index,String type,String id, User user) throws IOException, JSONException {
        // 构造HTTP请求
        Request request = new Request("POST", new StringBuilder("/"+index+"/"+type+"/").
                append(id).append("/_update").toString());
        request.addParameter("pretty", "true");

        JSONObject jsonObject = new JSONObject();
        // 创建脚本语言，如果是字符变量，必须加单引号
        StringBuilder op1 = new StringBuilder("ctx._source.username=").append("'" + user.getUsername() + "'");
        jsonObject.put("script", op1);

        request.setEntity(new NStringEntity(jsonObject.toString(), "UTF_8"));
        
        // 执行HTTP请求
        Response response = restClient.performRequest(request);

        // 获取返回的内容
        String responseBody = EntityUtils.toString(response.getEntity());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    /**
     * @Author yls
     * @Description 删除数据
     * @Date 2020/3/28 14:26
     * @param index 索引
     * @param type 类型
     * @param id id主键
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     **/
    public static ResponseEntity<String> deleteById(String index,String type,String id) throws IOException {
        Request request = new Request("DELETE", new StringBuilder("/"+index+"/"+type+"/").append(id).toString());
        request.addParameter("pretty", "true");
        // 执行HTTP请求
        Response response = restClient.performRequest(request);
        // 获取结果
        String responseBody = EntityUtils.toString(response.getEntity());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
