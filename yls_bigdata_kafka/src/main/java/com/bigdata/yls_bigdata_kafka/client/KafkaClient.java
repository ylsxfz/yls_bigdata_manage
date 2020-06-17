package com.bigdata.yls_bigdata_kafka.client;

/**
 * kafka分为两种客户端：
 * 	 1、springboot集成
 *   2、jdbc的方式连接
 *   
 *   
 * 创建topic（主题）
 * kafka-topics.sh --zookeeper node004:2181,node005:2181,node006:2181 --create --topic Hello-Kafka --partitions 1 --replication-factor 1
 *
 * 查看所有topic:
 * kafka-topics.sh --zookeeper node004:2181,node005:2181,node006:2181 --list
 *
 * 命令行消费者：
 * kafka-console-consumer.sh --bootstrap-server node004:9092,node005:9092,node006:9092 --topic Hello-Kafka --from-beginning
 * --group group1
 * 
 * 
 * @author yls
 *
 */
public class KafkaClient {

}
