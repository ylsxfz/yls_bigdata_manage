package com.bigdata.yls_bigdata_es.high_rest.cluster;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.document.ListenerUtils;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.routing.allocation.decider.EnableAllocationDecider;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.indices.recovery.RecoverySettings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yls
 * @Date: 2020/5/3 12:48
 * @Description: 群集更新设置API允许更新群集范围的设置。
 * @Version 1.0
 */
public class ClusterUpdateAPI {

    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args)throws Exception {
        ClusterUpdateSettingsRequest updateSettingsRequest = new ClusterUpdateSettingsRequest();

        updateSettingsRequest.timeout(TimeValue.timeValueMinutes(2));
        updateSettingsRequest.timeout("2m");

        updateSettingsRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        updateSettingsRequest.masterNodeTimeout("1m");

        String transientSettingKey =
                RecoverySettings.INDICES_RECOVERY_MAX_BYTES_PER_SEC_SETTING.getKey();
        int transientSettingValue = 10;
        Settings transientSettings =
                Settings.builder()
                        .put(transientSettingKey, transientSettingValue, ByteSizeUnit.BYTES)
                        .build();

        String persistentSettingKey =
                EnableAllocationDecider.CLUSTER_ROUTING_ALLOCATION_ENABLE_SETTING.getKey();
        String persistentSettingValue =
                EnableAllocationDecider.Allocation.NONE.name();
        Settings persistentSettings =
                Settings.builder()
                        .put(persistentSettingKey, persistentSettingValue)
                        .build();

        updateSettingsRequest.transientSettings(transientSettings);
        updateSettingsRequest.persistentSettings(persistentSettings);
        run(updateSettingsRequest);

        //通过builder修改
        updateSettingsRequest = new ClusterUpdateSettingsRequest();
        Settings.Builder transientSettingsBuilder =
                Settings.builder()
                        .put(transientSettingKey, transientSettingValue, ByteSizeUnit.BYTES);
        updateSettingsRequest.transientSettings(transientSettingsBuilder);
        run(updateSettingsRequest);

        //通过string修改
        updateSettingsRequest = new ClusterUpdateSettingsRequest();
        updateSettingsRequest.transientSettings(
                "{\"indices.recovery.max_bytes_per_sec\": \"10b\"}"
                , XContentType.JSON);
        run(updateSettingsRequest);


        //通过map修改
        updateSettingsRequest = new ClusterUpdateSettingsRequest();
        Map<String, Object> map = new HashMap<>();
        map.put(transientSettingKey
                , transientSettingValue + ByteSizeUnit.BYTES.getSuffix());
        updateSettingsRequest.transientSettings(map);
        run(updateSettingsRequest);
        restHighLevelClient.close();

        //通过map异步修改
        updateSettingsRequest = new ClusterUpdateSettingsRequest();
        updateSettingsRequest.transientSettings(map);
        restHighLevelClient = EsHighLevelConfig.init();
        restHighLevelClient.cluster()
                .putSettingsAsync(updateSettingsRequest,
                        RequestOptions.DEFAULT,
                        ListenerUtils.getListener(ClusterUpdateSettingsResponse.class,restHighLevelClient));
    }

    public static void run(ClusterUpdateSettingsRequest request) throws IOException {
        ClusterUpdateSettingsResponse updateSettingsResponse = restHighLevelClient.cluster().putSettings(request, RequestOptions.DEFAULT);
        Settings persistentSettings = updateSettingsResponse.getPersistentSettings();
        Settings transientSettings = updateSettingsResponse.getTransientSettings();
        boolean acknowledged = updateSettingsResponse.isAcknowledged();
        System.err.println();
        System.err.println("************************************");
        System.err.println(acknowledged);
        System.err.println(persistentSettings);
        System.err.println(transientSettings);
        System.err.println("************************************");
    }
}
