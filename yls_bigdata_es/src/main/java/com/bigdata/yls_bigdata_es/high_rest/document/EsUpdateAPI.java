package com.bigdata.yls_bigdata_es.high_rest.document;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.model.EsModel;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/4/26 21:06
 * @Description: 更新相关的API
 * @Version 1.0
 */
public class EsUpdateAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsUpdateAPI.class);

    //获取rest客户端
    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();


    public static void main(String[] args) throws Exception {
        EsModel esModel = new EsModel("posts006", "doc", "1");
        UpdateRequest updateRequest = getUpdateRequest(esModel);


        //通过jsonString更新
        System.err.println("更新jsonString：");
        String jsonString = "{" +
                "\"postDate\":\"2018-01-01\","+
                "\"message\":\"daily update2\""+
                "}";
        updateRequest.doc(jsonString, XContentType.JSON);
        UpdateResponse updateResponse = executeUpdate(updateRequest);
        pareResponse(updateResponse);


        //通过jsonMap更新
        System.err.println("更新jsonMap：");
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("updated",new Date());
        jsonMap.put("reason","daily update3");
        updateRequest.doc(jsonMap);
        updateResponse = executeUpdate(updateRequest);
        pareResponse(updateResponse);

        //通过document更新
        System.err.println("更新docment builder:");
        XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
        jsonBuilder.startObject();
        {
            jsonBuilder.timeField("updated",new Date());
            jsonBuilder.field("reason","daily update004");
        }
        jsonBuilder.endObject();
        updateRequest.doc(jsonBuilder);
        updateResponse = executeUpdate(updateRequest);
        pareResponse(updateResponse);

        //直接更新doc
        System.err.println("更新doc：");
        updateRequest.doc("updated",new Date(),
                "reason","daily update005");
        updateResponse = executeUpdate(updateRequest);
        pareResponse(updateResponse);

        //直接更新doc,如果不存在，新增
        System.err.println("更新doc,如不存在，新增：");
        jsonString = "{" +
                "\"postDate\":\"2019-01-01\","+
                "\"message\":\"daily update0002\""+
                "}";
        updateRequest.upsert(jsonString,XContentType.JSON);
        updateResponse = executeUpdate(updateRequest);
        pareResponse(updateResponse);


        //通过script更新
        System.err.println("通过script更新：");
        UpdateRequest updateSciptRequest = getUpdateRequest(esModel);
        Script script = updateByScript();
        updateSciptRequest.script(script);
        UpdateResponse updateSciptResponse = executeUpdate(updateSciptRequest);

        //通过storeScript更新
        System.err.println("通过storeScript更新：");
        updateSciptRequest = getUpdateRequest(esModel);
        Script storeScript = updateByStoreScript();
        updateSciptRequest.script(storeScript);
        updateSciptResponse = executeUpdate(updateSciptRequest);
        restHighLevelClient.close();


        //异步执行
        //通过jsonString更新
//        System.err.println("更新jsonString：");
//        String jsonString = "{" +
//                "\"postDate\":\"2018-01-01\","+
//                "\"message\":\"daily update2\""+
//                "}";
//        updateRequest.doc(jsonString, XContentType.JSON);
//        restHighLevelClient.updateAsync(updateRequest,RequestOptions.DEFAULT,
//                ListenerUtils.getListener(UpdateResponse.class,restHighLevelClient));
    }





    public static UpdateResponse executeUpdate(UpdateRequest updateRequest){
        try{
            return restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
        }catch (ElasticsearchException e){
            // 如果不使用request.upsert方法，且request.scriptedUpsert(false);和request.docAsUpsert(false);都设置为false，则文档不存在时提示没有找到文档
            if (e.status() == RestStatus.CONFLICT){
                System.err.println(e.status());
                System.out.println("需要删除的文档版本与现在文档冲突!");
            }else if (e.status() == RestStatus.NOT_FOUND){
                System.err.println(e.status());
                System.out.println("文档不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new UpdateResponse();
    }


    public static  void pareResponse(UpdateResponse updateResponse){
        System.err.println(updateResponse);
        String index = updateResponse.getIndex();
        String type = updateResponse.getType();
        String id = updateResponse.getId();
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("文档创建成功!");
        }else if(updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            // 任何一个字段的更新，都算更新操作，即使只是日期字段的值变化
            System.out.println("文档更新成功!");
        }else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
            System.out.println("文档删除成功!");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
            // 如果request.detectNoop(true);中设置为false，则这个永远不会进入
            System.out.println("文档无变化!");
        }

        long version = updateResponse.getVersion();
        System.out.println("index:" + index + "; type:" + type + "; id:" + id + ",version:" + version);
        ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            System.out.println("未完全执行所有分片,总分片数为：" + shardInfo.getTotal() + ",执行的分片数为："+ shardInfo.getSuccessful());
        }
        // fetchSource 如果设置需要返回结果中包含内容了,如果没有设置返回内容，则result 等于null
        GetResult result = updateResponse.getGetResult();
        if(result == null) {
            System.out.println("无内容结果返回");
        }else if (result.isExists()) {
            // 此例中如果文档不存在，且这样设置：request.scriptedUpsert(true);、request.docAsUpsert(false);，则会创建一个空内容的文档，因为脚本中没有内容，而禁止doc创建新文档
            String sourceAsString = result.sourceAsString();
            System.out.println(sourceAsString);
            Map<String, Object> sourceAsMap = result.sourceAsMap();
        }
    }

    /**
     * 功能描述:
     * 〈创建更新请求
     *         如果request.docAsUpsert(true)和request.scriptedUpsert(true)都设置为true,且
     *         request.doc(jsonMap)被注释掉时，会报错如下：
     *         org.elasticsearch.action.ActionRequestValidationException:
     *         Validation Failed: 1: script or doc is missing;2: doc must be specified if doc_as_upsert is enabled;
     *         即如果开启动了doc_as_upsert方法，则必须使用doc方法传入需要更新的内容
     * 〉
     *
     * @author : yls
     * @date : 2020/5/2 7:45
     * @param esModel
     * @return : UpdateRequest
     */
    public static UpdateRequest getUpdateRequest(EsModel esModel){
        //更新的请求
        UpdateRequest updateRequest = new UpdateRequest(
                esModel.getIndex(),
                esModel.getType(),
                esModel.getDocId());

        //updateRequest.routing("routing");
        //updateRequest.parent("parent");
        // 等待主分片可用的超时时间
        //updateRequest.timeout(TimeValue.timeValueSeconds(1));
        //updateRequest.timeout("3s");
        //WAIT_UNTIL 一直保持请求连接中，直接当所做的更改对于搜索查询可见时的刷新发生后，
        //再将结果返回
        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        updateRequest.setRefreshPolicy("wait_for");
        //如果更新的过程中，文档被其它线程进行更新的话，会产生冲突，这个为设置更新失败后重试的次数
        updateRequest.retryOnConflict(3);
        // 是否将文档内容作为结果返回，默认是禁止的
        updateRequest.fetchSource(true);
        //updateRequest.version(2);
        // NO OPeration,空操作检查,默认情况为true，只有原来的source和新的source存在不同的字段情况下才会重建索引，如果一模一样是不会触发重建索引的，如果将detect_noop=false不管内容有没有变化都会重建索引，这一点可以通过version的值的变化来发现
        updateRequest.detectNoop(false);
        // true，表明如果文档不存在，则新更新的文档内容作为新的内容插入文档，
        // 这个和scriptedUpsert的区别是：更新文档的两种不同方式，有的使用doc方法更新有的使用脚本更新
        //updateRequest.scriptedUpsert(true);
        //updateRequest.docAsUpsert(true);
        // 设置在更新操作执行之前，要求活动状态的分片副本数；单机不要设置，否则会报错：超时
        updateRequest.waitForActiveShards(2);
        updateRequest.waitForActiveShards(ActiveShardCount.ALL);
        // 设置希望在返回结果中返回的字段值
        String[] includes = new String[]{"updated", "r*"};
        String[] excludes = Strings.EMPTY_ARRAY;
        updateRequest.fetchSource(new FetchSourceContext(false, includes, excludes));

//        String[] includes = Strings.EMPTY_ARRAY;
//        String[] excludes = new String[]{"updated"};
//        updateRequest.fetchSource(new FetchSourceContext(true, includes, excludes));
        return updateRequest;
    }


    /**
     * 功能描述:
     * 〈
     *  1、map作为对象提供参数。
     *  2、使用painless语句和map参数创建内联脚本
     *  〉
     *
     * @author : yls
     * @date : 2020/5/2 7:39
     * @return : org.elasticsearch.script.Script
     */
    public static Script updateByScript(){
        Map<String,Object> parameters = Collections.singletonMap("postDate","2020-08-08");
        return new Script(ScriptType.INLINE,"painless",
                "ctx._source.field001 += params.postDate",parameters);
    }


    /**
     * 功能描述:
     * 〈
     *  1、map作为对象提供参数。
     *  2、引用increment-field为painless语句的脚本
     * 〉
     *
     * @author : yls
     * @date : 2020/5/2 7:40
     * @return : org.elasticsearch.script.Script
     */
    public static Script updateByStoreScript(){
        Map<String,Object> parameters = Collections.singletonMap("field002","2022-09-09");
        return new Script(ScriptType.STORED,null,"increment-field",parameters);
    }



}
