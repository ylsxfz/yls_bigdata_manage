package com.bigdata.yls_bigdata_es.high_rest.document;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.model.EsModel;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: yls
 * @Date: 2020/4/26 14:31
 * @Description: ES-ExistsAPI
 * @Version 1.0
 */
public class EsExistsAPI {
    //日志记录
    private final static Logger LOGGER = LoggerFactory.getLogger(EsExistsAPI.class);

    //获取rest客户端
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    /**
     * 功能描述:
     * 〈主方法〉
     *
     * @author : yls
     * @date : 2020/4/26 21:00
     * @param args
     * @return : void
     */
    public static void main(String[] args) throws Exception {
        EsModel esModel = new EsModel("posts006", "doc", "1");
        //同步执行
        //boolean exists = restHighLevelClient.exists(createGetRequest(esModel), RequestOptions.DEFAULT);
        //System.err.println("是否存在："+exists);

        //同步请求需要关闭客户端
        //restHighLevelClient.close();

        //同步执行
        restHighLevelClient.existsAsync(createGetRequest(esModel),RequestOptions.DEFAULT,
                ListenerUtils.getListener(Boolean.class,restHighLevelClient));
    }


    /**
     * 功能描述:
     * 〈创建getRequest〉
     *
     * @author : yls
     * @date : 2020/4/26 14:35
     * @param esModel 索引，type，文档id
     * @return : org.elasticsearch.action.get.GetRequest
     */
    public static GetRequest createGetRequest(EsModel esModel){
        GetRequest getRequest = new GetRequest(esModel.getIndex(), esModel.getType(), esModel.getDocId());
        //禁止获取存储的字段和"_source",请求的内容会减轻一些
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        return getRequest;
    }
}
