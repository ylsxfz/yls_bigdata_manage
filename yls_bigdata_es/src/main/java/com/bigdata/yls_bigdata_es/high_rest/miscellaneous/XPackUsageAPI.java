package com.bigdata.yls_bigdata_es.high_rest.miscellaneous;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.document.ListenerUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.protocol.xpack.XPackUsageRequest;
import org.elasticsearch.protocol.xpack.XPackUsageResponse;

import java.io.IOException;
import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/5/3 12:31
 * @Description: 可以使用以下usage()方法检索有关X-Pack功能使用情况的详细信息：
 * @Version 1.0
 */
public class XPackUsageAPI {
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws IOException {
        XPackUsageRequest xPackUsageRequest = new XPackUsageRequest();

        //异步执行
        restHighLevelClient.xpack().usageAsync(xPackUsageRequest,
                RequestOptions.DEFAULT,
                ListenerUtils.getListener(XPackUsageResponse.class,restHighLevelClient));

        //同步执行
        XPackUsageResponse xPackUsageResponse = restHighLevelClient.xpack().usage(xPackUsageRequest, RequestOptions.DEFAULT);
        Map<String, Map<String, Object>> usages = xPackUsageResponse.getUsages();
        if (usages!=null){
           usages.forEach((key,value)->{
               System.err.println(key+"==>>:"+value);
               //System.out.println(value);
           });
        }

        restHighLevelClient.close();
    }
}
