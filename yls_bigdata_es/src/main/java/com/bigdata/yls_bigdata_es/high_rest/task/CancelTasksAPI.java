package com.bigdata.yls_bigdata_es.high_rest.task;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.document.ListenerUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.TaskOperationFailure;
import org.elasticsearch.action.admin.cluster.node.tasks.cancel.CancelTasksRequest;
import org.elasticsearch.action.admin.cluster.node.tasks.cancel.CancelTasksResponse;
import org.elasticsearch.action.admin.cluster.node.tasks.list.TaskGroup;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.tasks.TaskId;
import org.elasticsearch.tasks.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/5/3 13:57
 * @Description:
 * @Version 1.0
 */
public class CancelTasksAPI {
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws Exception{
        CancelTasksRequest cancelTasksRequest = new CancelTasksRequest();
        cancelTasksRequest.setActions("cluster:*");
        cancelTasksRequest.setNodes("node004","node005");
        cancelTasksRequest.setParentTaskId(new TaskId("parentTaskId",42));

        CancelTasksResponse cancelResponse = restHighLevelClient.tasks().cancel(cancelTasksRequest, RequestOptions.DEFAULT);

        System.err.println(cancelResponse);

        //取消任务清单
        List<TaskInfo> tasks = cancelResponse.getTasks();
        //按节点分组的已取消任务的列表
        Map<String, List<TaskInfo>> perNodeTasks = cancelResponse.getPerNodeTasks();
        //由父任务分组的已取消任务的列表
        List<TaskGroup> taskGroups = cancelResponse.getTaskGroups();
        //节点故障列表
        List<ElasticsearchException> nodeFailures = cancelResponse.getNodeFailures();
        //任务取消失败列表
        List<TaskOperationFailure> taskFailures = cancelResponse.getTaskFailures();
        restHighLevelClient.close();


        restHighLevelClient = EsHighLevelConfig.init();
        restHighLevelClient.tasks().cancelAsync(cancelTasksRequest,
                RequestOptions.DEFAULT,
                ListenerUtils.getListener(CancelTasksResponse.class,restHighLevelClient));
    }
}
