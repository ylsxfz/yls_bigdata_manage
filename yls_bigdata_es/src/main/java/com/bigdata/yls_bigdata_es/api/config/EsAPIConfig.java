package com.bigdata.yls_bigdata_es.api.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author yls
 * @Description es的相关配置文件
 * @Date 2020/3/28 14:17
 **/
public class EsAPIConfig {
    public static TransportClient init() throws UnknownHostException {
    	// 1. 创建配置文件
        Settings settings = Settings.builder()
                // 添加配置属性， 集群名
                .put("cluster.name", "yls-es-cluster")
                // 创建
                .build();
        // 2. 利用配置文件创建ES客户端
        TransportClient transportClient = new PreBuiltTransportClient(settings);
        // 配置TCP端口，这里注意新版的API由InetSocketTransportAddress改成了TransportAddress
        TransportAddress nodeAddress01 = new TransportAddress(InetAddress.getByName("192.168.133.104"), 9300);
        TransportAddress nodeAddress02 = new TransportAddress(InetAddress.getByName("192.168.133.105"), 9300);
        transportClient.addTransportAddress(nodeAddress01);
        transportClient.addTransportAddress(nodeAddress02);
        return transportClient;
    }
}
