package com.bigdata.yls_bigdata_es.high_rest.miscellaneous;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @Auther: yls
 * @Date: 2020/5/3 12:21
 * @Description:
 * @Version 1.0
 */
public class PingAPI {
    public static RestHighLevelClient restHighLevelClient =  EsHighLevelConfig.init();
    public static void main(String[] args) throws IOException {
        boolean ping = restHighLevelClient.ping(RequestOptions.DEFAULT);
        System.err.println(ping);
        restHighLevelClient.close();
    }
}
