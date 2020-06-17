package com.bigdata.yls_bigdata_kafka.jdbc.partitioners;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区器
 */
public class CustomPartitioner implements Partitioner {

    private int passLine;

    @Override
    public void configure(Map<String, ?> configs) {
    	//从生产配置中获取分数线
        passLine = Integer.valueOf(configs.get("pass.line").toString());
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    	//key为分数，当分数大于分数线时候，分配到1分区，否则分配到0分区
        return Integer.valueOf(key.toString()) >= passLine ? 1 : 0;
    }

    @Override
    public void close() {
        System.out.println("分区器关闭");
    }


}
