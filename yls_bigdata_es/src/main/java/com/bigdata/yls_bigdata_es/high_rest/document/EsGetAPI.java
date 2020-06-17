package com.bigdata.yls_bigdata_es.high_rest.document;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.model.EsModel;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/4/23 15:41
 * @Description: ES-GetAPI
 * @Version 1.0
 */
public class EsGetAPI {
    //日志记录
    private final static Logger LOGGER = LoggerFactory.getLogger(EsGetAPI.class);

    //获取rest客户端
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    /**
     * 功能描述:
     * 〈主方法〉
     *
     * @author : yls
     * @date : 2020/4/26 21:01
     * @param args
     * @return : void
     */
    public static void main(String[] args) throws Exception {
        EsModel esModel = new EsModel("posts","doc","1");
        GetRequest getRequest = createGetRequest(esModel);
        //{"_index":"posts","_type":"doc","_id":"1","_version":4,"found":true,"_source":{"user":"kimchy","postDate":"2013-01-30","message":"trying out Elasticsearch"}}
        GetResponse getResponse = null;
        try {
           getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        }catch (ElasticsearchException e){
            e.printStackTrace();
            if (e.status() == RestStatus.NOT_FOUND) {
                System.err.println("not found");
            }
            if (e.status() == RestStatus.CONFLICT) {
                System.err.println("version error");
            }
        }

        pareResponse(getResponse);
        restHighLevelClient.close();

        //restHighLevelClient.getAsync(getRequest,RequestOptions.DEFAULT,ListenerUtils.getListener(GetResponse.class,restHighLevelClient));
    }


    /**
     * 功能描述:
     * 〈解析response〉
     *
     * @param getResponse get请求执行完的返回信息
     * @return : void
     * @author : yls
     * @date : 2020/4/23 15:35
     */
    private static void pareResponse(GetResponse getResponse) {
        String index = getResponse.getIndex();
        String type = getResponse.getType();
        String id = getResponse.getId();
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString();
            System.err.println(sourceAsString);
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            byte[] sourceAsBytes = getResponse.getSourceAsBytes();
            System.err.println(sourceAsBytes);
        } else {

        }
    }



    /**
     * 功能描述:
     * 〈es的getRequest的使用〉
     *
     * @author : yls
     * @date : 2020/4/23 15:51
     * @param esModel 索引，type，文档id
     * @return : org.elasticsearch.action.get.GetRequest
     */
    public static GetRequest createGetRequest(EsModel esModel){
        GetRequest getRequest = new GetRequest(esModel.getIndex(), esModel.getType(), esModel.getDocId());

        //过滤具体内容:{"_index":"posts","_type":"doc","_id":"1","_version":4,"found":true}
        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);

        //指定查询字段：
        //{"_index":"posts","_type":"doc","_id":"1","_version":4,"found":true,"_source":
        //{"postDate":"2013-01-30","message":"trying out Elasticsearch"}}
        String[] includes = new String[]{"message","*Date"};
        //过滤字段
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);
        //getRequest.storedFields("message");

        //路由值
        //getRequest.routing("routing");
        //父值
        //getRequest.parent("parent");
        //偏好值
        //getRequest.preference("preference");
        //实时标志
        //getRequest.realtime(false);
        //检索文档之前刷新
        //getRequest.refresh(true);
        //指定版本
        //getRequest.version(4);
        //版本类型
        //getRequest.versionType(VersionType.EXTERNAL);
        return getRequest;
    }




}
