package com.bigdata.yls_bigdata_es.high_rest.miscellaneous;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.document.ListenerUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.protocol.xpack.XPackInfoRequest;
import org.elasticsearch.protocol.xpack.XPackInfoResponse;

import java.io.IOException;
import java.util.EnumSet;

/**
 * @Auther: yls
 * @Date: 2020/5/3 12:24
 * @Description: 可以使用以下xPackInfo()方法检索有关已安装的X-Pack功能的常规信息：
 * @Version 1.0
 */
public class XPackInfoAPI {
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws IOException {
        XPackInfoRequest xPackInfoRequest = new XPackInfoRequest();
        //启用详细模式。默认值为，false但true将返回更多信息
        xPackInfoRequest.setVerbose(true);
        //设置要检索的信息类别。默认设置为不返回任何信息，这对于检查是否已安装X-Pack很有帮助，而其他信息则不多
        xPackInfoRequest.setCategories(EnumSet.of(
                XPackInfoRequest.Category.BUILD,
                XPackInfoRequest.Category.LICENSE,
                XPackInfoRequest.Category.FEATURES
        ));


        //异步执行
        restHighLevelClient.xpack().infoAsync(xPackInfoRequest,
                RequestOptions.DEFAULT,
                ListenerUtils.getListener(XPackInfoResponse.class,restHighLevelClient));


        System.err.println("同步执行");
        XPackInfoResponse info = restHighLevelClient.xpack().info(xPackInfoRequest, RequestOptions.DEFAULT);
        System.err.println(info);
        restHighLevelClient.close();
    }
}
