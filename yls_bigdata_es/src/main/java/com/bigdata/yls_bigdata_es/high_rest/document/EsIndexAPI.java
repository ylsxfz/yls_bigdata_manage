package com.bigdata.yls_bigdata_es.high_rest.document;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.model.EsModel;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Auther: yls
 * @Date: 2020/4/23 13:36
 * @Description: ES-IndexAPI
 * @Version 1.0
 */
public class EsIndexAPI {

    //日志记录
    private final static Logger LOGGER = LoggerFactory.getLogger(EsIndexAPI.class);

    //获取rest客户端
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    /**
     * 功能描述:
     * 〈inde相关的方法〉
     *
     * @param args
     * @return : void
     * @author : yls
     * @date : 2020/4/23 15:39
     */
    public static void main(String[] args) throws Exception {
        EsModel esModel = new EsModel("mail", "info", "1");

        //获取indexrequest
        IndexRequest indexRequest = createIndex(esModel);
        //同步执行，会返回response
        IndexResponse indexResponse = getIndexResponse(indexRequest);

        pareResponse(indexResponse);

        //同步请求需要关闭客户端
        restHighLevelClient.close();

        //异步请求不用关闭客户端
        //restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT,
                //ListenerUtils.getListener(IndexResponse.class,restHighLevelClient));
    }


    /**
     * 功能描述:
     * 〈创建index的request和respon〉
     *
     * @param esModel 索引，type，文档id
     * @return : void
     * @author : yls
     * @date : 2020/4/23 13:55
     */
    public static IndexRequest createIndex(EsModel esModel) throws Exception {
        IndexRequest indexRequest = new IndexRequest(
                esModel.getIndex(),
                esModel.getType(),
                esModel.getDocId()
        );

        //利用map插入数据
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("user", "kimchy");
//        jsonMap.put("postDate", new Date());
//        jsonMap.put("message", "trying out Elasticsearch");
//        indexRequest.source(jsonMap);

        //利用builder插入数据
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.field("user", "kimchy");
//            builder.timeField("postDate", new Date());
//            builder.field("message", "trying out Elasticsearch");
//        }
//        builder.endObject();
//        indexRequest.source(jsonMap);


        //直接链式编程
//       indexRequest = new IndexRequest("posts", "doc", "1")
//                .source("user", "kimchy",
//                        "postDate", new Date(),
//                        "message", "trying out Elasticsearch");

        //利用字符串
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";

        jsonString = "{"+"\"txtContent\":\"测试邮件撒大声地阿萨德看冷风机拉水电费阿斯兰快递费静安寺吗，" +
                "sad风口浪尖拉时代峻峰这是一张图片这是一个地图位置这是一个视频\""+"}";
        indexRequest.source(jsonString, XContentType.JSON);
        //设置路由
        //indexRequest.routing("routeing");
        //超时时间
        //indexRequest.timeout(TimeValue.timeValueSeconds(1));
        //indexRequest.timeout("1s");
        //刷新策略
        //indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //indexRequest.setRefreshPolicy("wait_for");
        //设置版本
        //indexRequest.version(1);
        //版本类型
        //indexRequest.versionType(VersionType.EXTERNAL);
        //设置类型：创建create/更新update
        //indexRequest.opType(DocWriteRequest.OpType.CREATE);
        //索引文档之前要执行的摄取管道的名称
        //indexRequest.setPipeline("pipeline");
        return indexRequest;
    }


    /**
     * 功能描述:
     * 〈解析response〉
     *
     * @param indexResponse index请求执行完的返回信息
     * @return : void
     * @author : yls
     * @date : 2020/4/23 15:35
     */
    private static void pareResponse(IndexResponse indexResponse) {
        //执行请求后返回的相关信息
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        //版本
        long version = indexResponse.getVersion();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            //新建
            LOGGER.info("index===》create");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            //修改
            LOGGER.info("index===》update");
        }

        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            //有分片查询失败
            LOGGER.info("index-search-shard===》failure");
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                String reason = failure.reason();
                //有分片查询失败
                LOGGER.info("index-search-shard===》failure" + reason);
            }
        }
        LOGGER.info(indexResponse.toString());
    }

    /**
     * 功能描述:
     * 〈同步执行indexRequest〉
     *
     * @param indexRequest index请求
     * @return : org.elasticsearch.action.index.IndexResponse
     * @author : yls
     * @date : 2020/4/23 15:34
     */
    private static IndexResponse getIndexResponse(IndexRequest indexRequest) throws IOException {
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            e.printStackTrace();
            if (e.status() == RestStatus.CONFLICT) {
                LOGGER.info("es查询失败：" + e.status());
            }
        }
        return indexResponse;
    }

}
