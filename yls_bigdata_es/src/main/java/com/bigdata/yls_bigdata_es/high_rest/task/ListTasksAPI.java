package com.bigdata.yls_bigdata_es.high_rest.task;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.TaskOperationFailure;
import org.elasticsearch.action.admin.cluster.node.tasks.list.ListTasksRequest;
import org.elasticsearch.action.admin.cluster.node.tasks.list.ListTasksResponse;
import org.elasticsearch.action.admin.cluster.node.tasks.list.TaskGroup;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.tasks.TaskId;
import org.elasticsearch.tasks.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/5/3 13:39
 * @Description:
 * @Version 1.0
 */
public class ListTasksAPI {
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws Exception{
        ListTasksRequest tasksRequest = new ListTasksRequest();

        //仅请求与群集相关的任务
        //请求在节点nodeId1和nodeId2上运行的所有任务
        //仅请求执行特定任务的孩子
        tasksRequest.setActions("cluster:*");
        tasksRequest.setNodes("node004","node005");
        tasksRequest.setParentTask(new TaskId("parentTaskId",42));

        //如果信息包含详细信息，可能会降低生成数据的速度。默认为false
        tasksRequest.setDetailed(true);

        //该请求是否应等待所有找到的任务完成。默认为false
        //请求的超时时间为TimeValue。仅当setWaitForCompletion是时适用true。默认为30秒
        //超时为 String
        tasksRequest.setWaitForCompletion(true);
        tasksRequest.setTimeout(TimeValue.timeValueSeconds(50));
        tasksRequest.setTimeout("50s");

        ListTasksResponse tasksResponse = restHighLevelClient.tasks().list(tasksRequest, RequestOptions.DEFAULT);

        List<TaskInfo> tasks = tasksResponse.getTasks();
        tasks.forEach(taskInfo -> {
            System.err.println("taskInfo==>>:"+taskInfo);
        });

        Map<String, List<TaskInfo>> perNodeTasks = tasksResponse.getPerNodeTasks();
        perNodeTasks.forEach((key,value)->{
            System.err.println(key+":"+value);
        });


        List<TaskGroup> taskGroups = tasksResponse.getTaskGroups();
        taskGroups.forEach(taskGroup -> {
            System.err.println("taskGroup==>>:"+taskGroup);
        });


        List<ElasticsearchException> nodeFailures = tasksResponse.getNodeFailures();
        nodeFailures.forEach(nodeFailure ->{
            System.err.println("nodeFailure:"+nodeFailure);
        });

        List<TaskOperationFailure> taskFailures = tasksResponse.getTaskFailures();
        taskFailures.forEach(taskOperationFailure -> {
            System.err.println("taskOperationFailure:"+taskOperationFailure);
        });

        restHighLevelClient.close();

//        restHighLevelClient = EsHighLevelConfig.init();
//        restHighLevelClient.tasks().listAsync(tasksRequest,
//                RequestOptions.DEFAULT,
//                ListenerUtils.getListener(ListTasksResponse.class,restHighLevelClient));
    }
}
