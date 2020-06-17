package com.bigdata.yls_bigdata_es.high_rest.cluster;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.document.ListenerUtils;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;

/**
 * @Auther: yls
 * @Date: 2020/5/3 13:09
 * @Description:
 * @Version 1.0
 */
public class ClusterGetAPI {

    private static RestHighLevelClient restHighLevelClient = EsHighLevelConfig.init();

    public static void main(String[] args) throws Exception{
        ClusterGetSettingsRequest getSettingsRequest = new ClusterGetSettingsRequest();
        getSettingsRequest.includeDefaults(true);
        getSettingsRequest.local(true);
        getSettingsRequest.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        getSettingsRequest.masterNodeTimeout("1m");
        ClusterGetSettingsResponse response = restHighLevelClient.cluster().getSettings(getSettingsRequest,
                RequestOptions.DEFAULT);
        System.err.println(response);

        Settings persistentSettings = response.getPersistentSettings();
        Settings transientSettings = response.getTransientSettings();
        Settings defaultSettings = response.getDefaultSettings();
        String settingValue = response.getSetting("cluster.routing.allocation.enable");
        System.err.println(persistentSettings);
        System.err.println(transientSettings);
        System.err.println(defaultSettings);
        System.err.println(settingValue);

        restHighLevelClient.close();


        restHighLevelClient = EsHighLevelConfig.init();
        ClusterGetSettingsRequest request = new ClusterGetSettingsRequest();
        restHighLevelClient.cluster().getSettingsAsync(
                request,
                RequestOptions.DEFAULT,
                ListenerUtils.getListener(ClusterGetSettingsResponse.class,restHighLevelClient)
        );
    }
}
