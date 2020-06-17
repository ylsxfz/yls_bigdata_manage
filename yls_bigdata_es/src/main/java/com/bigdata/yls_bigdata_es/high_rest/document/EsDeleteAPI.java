package com.bigdata.yls_bigdata_es.high_rest.document;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.model.EsModel;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: yls
 * @Date: 2020/4/26 15:20
 * @Description:
 * @Version 1.0
 */
public class EsDeleteAPI {
    //日志记录
    private final static Logger LOGGER = LoggerFactory.getLogger(EsDeleteAPI.class);

    //获取rest客户端
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    /**
     * 功能描述:
     * 〈主方法〉
     * @author : yls
     * @date : 2020/4/26 20:59
     * @param args
     * @return : void
     */
    public static void main(String[] args) throws Exception{
        EsModel esModel = new EsModel("posts006", "doc", "1");
        DeleteRequest deleteRequest = getDeleteRequest(esModel);

        try{
            //同步执行
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            pareResponse(deleteResponse);
            restHighLevelClient.close();
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                System.err.println("not_found");
            }
        }catch (ElasticsearchException e) {

            if (e.status() == RestStatus.CONFLICT) {
                System.err.println("version error");
            }
        }

        //异步执行
        //restHighLevelClient.deleteAsync(deleteRequest, RequestOptions.DEFAULT,
               // ListenerUtils.getListener(DeleteResponse.class,restHighLevelClient));
    }


    /**
     * 功能描述:
     * 〈解析response〉
     *
     * @author : yls
     * @date : 2020/4/26 20:58
     * @param deleteResponse 删除数据的response
     * @return : void
     */
    public static void pareResponse(DeleteResponse deleteResponse){
        String index = deleteResponse.getIndex();
        String type = deleteResponse.getType();
        String id = deleteResponse.getId();
        long version = deleteResponse.getVersion();
        LOGGER.info("index:"+index+",type:"+type+",id"+id+",version"+version);
        System.err.println("index:"+index+",type:"+type+",id"+id+",version"+version);
        ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                String reason = failure.reason();
            }
        }
    }

    /**
     * 功能描述:
     * 〈删除request〉
     *
     * @author : yls
     * @date : 2020/4/26 20:59
     * @param esModel es请求的model
     * @return : org.elasticsearch.action.delete.DeleteRequest
     */
    public static DeleteRequest  getDeleteRequest(EsModel esModel){
        //删除request
        DeleteRequest deleteRequest = new DeleteRequest(esModel.getIndex(), esModel.getType(), esModel.getDocId());

        deleteRequest.routing("routing");
        deleteRequest.parent("parent");
        //超时时间
        deleteRequest.timeout("2m");
        deleteRequest.timeout(TimeValue.timeValueMinutes(2));
        //刷新策略
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        deleteRequest.setRefreshPolicy("wait_for");
        //版本
        //deleteRequest.version(2);
        //deleteRequest.versionType(VersionType.EXTERNAL);
        return deleteRequest;
    }
}
