package com.bigdata.yls_bigdata_es.high_rest.cluster;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.document.ListenerUtils;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.cluster.health.ClusterShardHealth;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;

import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/5/3 13:18
 * @Description:
 * @Version 1.0
 */
public class ClusterHealthAPI {
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws Exception{
        ClusterHealthRequest healthRequest = new ClusterHealthRequest();
        //ClusterHealthRequest healthRequest = new ClusterHealthRequest("index1", "index2");
        //指定索引
        //healthRequest.indices("index1","index2");

//        //请求的超时时间为TimeValue。默认为30秒
//        healthRequest.timeout(TimeValue.timeValueSeconds(50));
//        //作为一个 String
//        healthRequest.timeout("50s");
//        //连接到主节点的超时TimeValue。默认与timeout
//        healthRequest.masterNodeTimeout(TimeValue.timeValueSeconds(20));
//        //作为一个 String
//        healthRequest.masterNodeTimeout("20s");
//        //状态等待（例如green，yellow或red）。接受一个ClusterHealthStatus值。
//        healthRequest.waitForStatus(ClusterHealthStatus.YELLOW);
//        //使用预定义方法
//        healthRequest.waitForYellowStatus();
//        //等待事件的优先级。接受一个Priority值。
//        healthRequest.waitForEvents(Priority.NORMAL);
//        //返回的健康信息的详细程度。接受一个ClusterHealthRequest.Level值。
//        healthRequest.level(ClusterHealthRequest.Level.SHARDS);
//        //等待0个重定位碎片。默认为false
//        healthRequest.waitForNoRelocatingShards(true);
//        //等待0个初始化分片。默认为false
//        healthRequest.waitForNoInitializingShards(true);
//        //等待N集群中的节点。默认为0
//        //使用>=N，<=N，>N和<N符号
//        //使用ge(N)，le(N)，gt(N)，lt(N)符号
//        healthRequest.waitForNodes("2");
//        healthRequest.waitForNodes(">=2");
//        healthRequest.waitForNodes("le(2)");
//        //等待所有分片在集群中处于活动状态
//        //等待分N片在集群中处于活动状态
//        healthRequest.waitForActiveShards(ActiveShardCount.ALL);
//        healthRequest.waitForActiveShards(1);
//        //非主节点可用于此请求。默认为false
//        healthRequest.local(true);

        ClusterHealthResponse healthResponse = restHighLevelClient
                .cluster()
                .health(healthRequest, RequestOptions.DEFAULT);
        System.err.println(healthResponse);
        restHighLevelClient.close();


        restHighLevelClient = EsHighLevelConfig.init();
        healthRequest = new ClusterHealthRequest();
        restHighLevelClient.cluster().healthAsync(healthRequest,
                RequestOptions.DEFAULT,
                ListenerUtils.getListener(ClusterHealthResponse.class,restHighLevelClient));

    }

    public static void pareResponse(ClusterHealthResponse response){
        //集群名称
        String clusterName = response.getClusterName();
        //群集状态（green，yellow或red）
        ClusterHealthStatus status = response.getStatus();
        //处理过程中请求是否超时
        boolean timedOut = response.isTimedOut();
        //请求的状态（OK或REQUEST_TIMEOUT）。其他错误将作为异常抛出
        RestStatus restStatus = response.status();
        //集群中的节点数
        int numberOfDataNodes = response.getNumberOfDataNodes();
        //集群中的数据节点数
        int numberOfNodes = response.getNumberOfNodes();
        //活动分片数
        int activeShards = response.getActiveShards();
        //主活动分片数
        int activePrimaryShards = response.getActivePrimaryShards();
        //分片数量
        int relocatingShards = response.getRelocatingShards();
        //初始化分片数
        int initializingShards = response.getInitializingShards();
        //未分配的碎片数
        int unassignedShards = response.getUnassignedShards();
        //当前正在延迟的未分配碎片数
        int delayedUnassignedShards = response.getDelayedUnassignedShards();
        //活动碎片百分比
        double activeShardsPercent = response.getActiveShardsPercent();
        //队列中所有任务的最大等待时间
        TimeValue taskMaxWaitingTime = response.getTaskMaxWaitingTime();
        //当前待处理的任务数
        int numberOfPendingTasks = response.getNumberOfPendingTasks();
        //当前正在进行的异步获取次数
        int numberOfInFlightFetch = response.getNumberOfInFlightFetch();
        //
        //有关集群中索引的详细信息
        Map<String, ClusterIndexHealth> indices = response.getIndices();
        //有关特定索引的详细信息
        ClusterIndexHealth index = indices.get("index");
        ClusterHealthStatus indexStatus = index.getStatus();
        int numberOfShards = index.getNumberOfShards();
        int numberOfReplicas = index.getNumberOfReplicas();
        int index_activeShards = index.getActiveShards();
        int index_activePrimaryShards = index.getActivePrimaryShards();
        int index_initializingShards = index.getInitializingShards();
        int index_relocatingShards = index.getRelocatingShards();
        int index_unassignedShards = index.getUnassignedShards();

        //有关特定分片的详细信息
        Map<Integer, ClusterShardHealth> shards = index.getShards();
        ClusterShardHealth shardHealth = shards.get(0);
        int shardId = shardHealth.getShardId();
        ClusterHealthStatus shardStatus = shardHealth.getStatus();
        int active = shardHealth.getActiveShards();
        int initializing = shardHealth.getInitializingShards();
        int unassigned = shardHealth.getUnassignedShards();
        int relocating = shardHealth.getRelocatingShards();
        boolean primaryActive = shardHealth.isPrimaryActive();
    }
}
