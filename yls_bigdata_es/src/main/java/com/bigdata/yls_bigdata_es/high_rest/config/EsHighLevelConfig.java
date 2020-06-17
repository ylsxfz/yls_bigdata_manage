package com.bigdata.yls_bigdata_es.high_rest.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @Auther: yls
 * @Date: 2020/4/23 13:10
 * @Description: es-high-level-client
 * @Version 1.0
 */
public class EsHighLevelConfig {
    public static RestHighLevelClient init(){
        // 如果有多个从节点可以持续在内部new多个HttpHost，参数1是ip,参数2是HTTP端口，参数3是通信协议
        RestClientBuilder clientBuilder = RestClient.builder(
                new HttpHost("192.168.133.104", 9200, "http")
                //new HttpHost("192.168.3.7", 9200, "http")
        );

        // 添加其他配置，都是可选，比如设置请求头，每个请求都会带上这个请求头
        Header[] defaultHeaders = {new BasicHeader("Content-Type", ContentType.APPLICATION_JSON.toString())};
        clientBuilder.setDefaultHeaders(defaultHeaders);

        return  new RestHighLevelClient(clientBuilder);
    }
}
