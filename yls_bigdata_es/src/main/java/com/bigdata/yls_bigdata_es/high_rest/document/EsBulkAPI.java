package com.bigdata.yls_bigdata_es.high_rest.document;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @Auther: yls
 * @Date: 2020/5/2 9:15
 * @Description: 批量处理请求API
 * @Version 1.0
 */
public class EsBulkAPI {

    private final static Logger LOGGER = LoggerFactory.getLogger(EsBulkAPI.class);

    private  static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();


    public static void main(String[] args) throws Exception {
//        //同步执行
//        BulkRequest bulkRequest = getBulkRequest();
//        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//        pareResponse(bulkResponse);
//        restHighLevelClient.close();
//
//
//
//        //异步请求
//        restHighLevelClient.bulkAsync(bulkRequest,RequestOptions.DEFAULT,
//                ListenerUtils.getListener(BulkResponse.class,restHighLevelClient));

        //执行器
        try {
            getBulkProcessor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 功能描述:
     * 〈批量操作的执行器〉
     *
     * @author : yls
     * @date : 2020/5/2 10:19
     * @param
     * @return : void
     */
    private static void getBulkProcessor() throws Exception {
        BulkProcessor.Listener bulkProcessorListener = getBulkProcessorListener();
        BiConsumer<BulkRequest, ActionListener<BulkResponse>> biConsumer =
            (bulkRequest,listener) -> restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT,listener);
//        BulkProcessor bulkProcessor  = BulkProcessor.builder(biConsumer, bulkProcessorListener).build();

        BulkProcessor.Builder builder = BulkProcessor.builder(biConsumer, bulkProcessorListener);
        //根据当前添加的操作数设置何时刷新新的批量请求（默认为1000，使用-1禁用它）
        builder.setBulkActions(2);
        //根据当前添加的操作大小设置何时刷新新的批量请求（默认为5Mb，使用-1禁用它）
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        //设置允许执行的并发请求数（默认为1，使用0仅允许执行单个请求）
        builder.setConcurrentRequests(2);
        //设置刷新间隔，BulkRequest如果间隔过去，则刷新所有未决（默认值未设置）
//        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        builder.setFlushInterval(TimeValue.timeValueMinutes(5L));
        //设置一个恒定的退避策略，该策略最初等待1秒，然后重试3次。
        // 请参阅BackoffPolicy.noBackoff()，BackoffPolicy.constantBackoff()
        // 以及BackoffPolicy.exponentialBackoff() 更多选项
        builder.setBackoffPolicy(BackoffPolicy
                .constantBackoff(TimeValue.timeValueSeconds(1L),3));

        //创建bulkProcessor
        BulkProcessor bulkProcessor = builder.build();
        IndexRequest one = new IndexRequest("posts006", "doc", "1")
                .source(XContentType.JSON, "field", "foo");
        DeleteRequest two = new DeleteRequest("posts006", "doc", "3");
        UpdateRequest three = new UpdateRequest("posts006", "doc", "2")
                .doc(XContentType.JSON, "other", "test");
        IndexRequest four = new IndexRequest("posts006", "doc", "4")
                .source(XContentType.JSON, "field", "baz");

        bulkProcessor.add(one);
        bulkProcessor.add(two);
        bulkProcessor.add(three);
        bulkProcessor.add(four);

        //将所有请求添加到后BulkProcessor，需要使用两种可用的关闭方法之一关闭其实例。
        //该awaitClose()方法可用于等待所有请求都已处理或经过指定的等待时间：
        //true如果所有批量请求都已完成，并且所有批量请求都已完成false之前经过了等待时间，则该方法返回
        boolean b = bulkProcessor.awaitClose(10L, TimeUnit.MINUTES);
        System.out.println(b);

        //该close()方法可用于立即关闭BulkProcessor：
        //两种方法在关闭处理器之前都会刷新添加到处理器的请求，并且还禁止将任何新请求添加到处理器。
        bulkProcessor.close();
        restHighLevelClient.close();
    }


    /**
     * 功能描述:
     * 〈解析Bulkresponse〉
     *
     * @author : yls
     * @date : 2020/5/2 9:48
     * @param bulkResponse
     * @return : void
     */
    public static void pareResponse(BulkResponse bulkResponse){
        System.err.println("bulkResponse.hasFailures()："+bulkResponse.hasFailures());
        if (bulkResponse.hasFailures()){

        }

        for (BulkItemResponse bulkItemResponse:
             bulkResponse) {
            if (bulkItemResponse.isFailed()){
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                System.err.println("failure；"+failure);
            }
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE){
                IndexResponse indexResponse = (IndexResponse) itemResponse;
                System.err.println("indexResponse："+indexResponse);
            }else if(bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE){
                UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                System.err.println("updateResponse:"+updateResponse);
            }else if(bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE){
                DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                System.err.println("deleteResponse:"+deleteResponse);
            }

        }
    }

    /**
     * 功能描述:
     * 〈bulkRequest批量处理请求〉
     *
     * @author : yls
     * @date : 2020/5/2 9:16
     * @param
     * @return : org.elasticsearch.action.bulk.BulkRequest
     */
    public static BulkRequest getBulkRequest(){
        BulkRequest bulkRequest = new BulkRequest();

        bulkRequest.add(new IndexRequest("posts006","doc","1")
                .source(XContentType.JSON,"field","foo"));
        bulkRequest.add(new IndexRequest("posts006","doc","2")
                .source(XContentType.JSON,"field","foo"));
        bulkRequest.add(new IndexRequest("posts006","doc","3")
                .source(XContentType.JSON,"field","foo"));
        bulkRequest.add(new DeleteRequest("posts006", "doc", "3"));
        bulkRequest.add(new UpdateRequest("posts006", "doc", "2")
                .doc(XContentType.JSON,"other", "test"));
        bulkRequest.add(new IndexRequest("posts006", "doc", "4")
                .source(XContentType.JSON,"field", "baz"));

        // 等待主分片可用的超时时间
        bulkRequest.timeout("2m");
        bulkRequest.timeout(TimeValue.timeValueMinutes(2));

        //WAIT_UNTIL 一直保持请求连接中，直接当所做的更改对于搜索查询可见时的刷新发生后，
        //再将结果返回
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        bulkRequest.setRefreshPolicy("wait_for");

        // 设置在更新操作执行之前，要求活动状态的分片副本数；单机不要设置，否则会报错：超时
        bulkRequest.waitForActiveShards(2);
        bulkRequest.waitForActiveShards(ActiveShardCount.ALL);

        return bulkRequest;
    }


    /**
     * 功能描述:
     * 〈
     *  在BulkProcessor通过提供一个工具类，允许它们被添加到所述处理器透明地执行索引/更新/删除操作简化了批量API的使用。
     *  为了执行请求，BulkProcessor需要以下组件：
     *  RestHighLevelClient
     *      该客户端用于执行BulkRequest 和检索BulkResponse
     *  BulkProcessor.Listener
     *      在每次BulkRequest执行之前或之后或BulkRequest失败 时调用此侦听器
     *      然后该BulkProcessor.builder方法可用于构建新的BulkProcessor
     *
     *  1、创建 BulkProcessor.Listener
     *  2、每次执行一次之前都会调用此方法 BulkRequest
     *  3、每次执行 BulkRequest
     *  4、BulkRequest失败时调用此方法
     *  5、BulkProcessor通过build()从中调用方法来创建BulkProcessor.Builder。该RestHighLevelClient.bulkAsync() 方法将用于执行BulkRequest后台操作。
     * 〉
     *
     * @author : yls
     * @date : 2020/5/2 9:50
     * @param
     * @return : void
     */
    public static BulkProcessor.Listener getBulkProcessorListener(){
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {

            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                System.err.println("beforeBulk:"+request);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                System.err.println("executionId:"+executionId);
                System.err.println("afterBulk-request:"+request);
                System.err.println("afterBulk-response:"+response);
                pareResponse(response);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                System.err.println("executionId:"+executionId);
                System.err.println("afterBulk-request:"+request);
                System.err.println("afterBulk-failure:"+failure);
            }
        };

        return listener;
    }



    /**
     * 功能描述:
     * 〈异步执行监听器〉
     *
     * @author : yls
     * @date : 2020/5/2 10:03
     * @param
     * @return : org.elasticsearch.action.ActionListener
     */
    public static  ActionListener getListener(){
        return new ActionListener<BulkResponse>(){
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                System.err.println("listener-bulkResponse:"+bulkResponse);
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("listener-e:"+e.getMessage().toString());
            }
        };
    }
}
