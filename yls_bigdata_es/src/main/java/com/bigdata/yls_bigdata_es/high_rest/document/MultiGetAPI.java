package com.bigdata.yls_bigdata_es.high_rest.document;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/5/3 10:36
 * @Description: multiGetAPI get 在单个http请求中并行执行多个请求。
 * @Version 1.0
 */
public class MultiGetAPI {

    private final static Logger LOGGER = LoggerFactory.getLogger(MultiGetAPI.class);

    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws Exception {
        MultiGetRequest multiGetRequest = getMultiGetRequest();

        //异步执行
//        restHighLevelClient.mgetAsync(multiGetRequest,RequestOptions.DEFAULT,
//                ListenerUtils.getListener(MultiGetItemResponse.class,restHighLevelClient));


        //同步执行并解析结果集
        MultiGetResponse mget = restHighLevelClient.mget(multiGetRequest, RequestOptions.DEFAULT);

        //解析结果集
        MultiGetItemResponse[] responses = mget.getResponses();
        for (MultiGetItemResponse multiGetItemResponse:
             responses) {
            //getFailure 返回null，因为没有失败。
            MultiGetResponse.Failure failure = multiGetItemResponse.getFailure();
            System.err.println(failure.getFailure());
            GetResponse response = multiGetItemResponse.getResponse();

            String index = multiGetItemResponse.getIndex();
            String type = multiGetItemResponse.getType();
            String id = multiGetItemResponse.getId();
            if (response!=null && response.isExists()){
                System.err.println(response);
                long version = response.getVersion();
                //将该文档检索为 String
                String sourceAsString = response.getSourceAsString();
                System.out.println(sourceAsString);

                //将该文档检索为 Map<String, Object>
                Map<String, Object> sourceAsMap = response.getSourceAsMap();
                if (sourceAsMap!=null && !sourceAsMap.isEmpty()){
                    sourceAsMap.forEach((k,v)->{
                        System.err.println("key:"+k+";value:"+v);
                    });
                }

                //将该文档检索为 byte[]
                byte[] sourceAsBytes = response.getSourceAsBytes();
                System.err.println(sourceAsBytes);
            }

        }

        restHighLevelClient.close();


    }


    /**
     * 功能描述:
     * 〈批量查询〉
     *
     * @author : yls
     * @date : 2020/5/3 11:47
     * @return : org.elasticsearch.action.get.MultiGetRequest
     */
    public static MultiGetRequest getMultiGetRequest(){
        MultiGetRequest multiGetRequest = new MultiGetRequest();

        multiGetRequest.add(new MultiGetRequest.Item(
                "posts006",
                "doc",
                "1"
        ));

        //过滤文档内容
        multiGetRequest.add(new MultiGetRequest.Item(
           "posts006",
           "doc",
           "2"
        ).fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));



        //过滤部分字段
        String[] includes = new String[]{"foo","*r"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        multiGetRequest.add(new MultiGetRequest.Item(
                "posts007",
                "doc",
                "3"
        ).fetchSourceContext(fetchSourceContext));


        String[] includes02 = Strings.EMPTY_ARRAY;
        String[] excludes02 = new String[]{"foo","*r*"};
        FetchSourceContext fetchSourceContext02 = new FetchSourceContext(true, includes02, excludes02);
        multiGetRequest.add(new MultiGetRequest.Item(
                "posts006",
                "doc",
                "6"
        ).fetchSourceContext(fetchSourceContext02)
//        .parent("some_parent")
//        .routing("some_routing")
        .versionType(VersionType.EXTERNAL)
        .version(1L)
        );

        //偏好值
        multiGetRequest.preference("some_preference");
        //将实时标志设置为false（true默认情况下）
        multiGetRequest.realtime();
        //检索文档之前刷新（false默认情况下）
        multiGetRequest.refresh(true);

        return multiGetRequest;
    }
}
