package com.bigdata.yls_bigdata_es.high_rest.miscellaneous;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import org.elasticsearch.Build;
import org.elasticsearch.Version;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.ClusterName;

import java.io.IOException;

/**
 * @Auther: yls
 * @Date: 2020/5/3 12:17
 * @Description:
 * @Version 1.0
 */
public class InfoAPI {
    public static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws IOException {
        MainResponse mainResponse = restHighLevelClient.info(RequestOptions.DEFAULT);
        ClusterName clusterName = mainResponse.getClusterName();
        Build build = mainResponse.getBuild();
        String nodeName = mainResponse.getNodeName();
        Version version = mainResponse.getVersion();

        System.err.println(clusterName+":"+nodeName+":"+build+":"+version);
        restHighLevelClient.close();
    }
}
