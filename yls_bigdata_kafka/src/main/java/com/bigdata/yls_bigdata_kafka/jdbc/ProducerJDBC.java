package com.bigdata.yls_bigdata_kafka.jdbc;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
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
 */
public class ProducerJDBC {
    public static void main(String[] args) {
    	String topicName = "fy-test-1";
        Properties props = new Properties();
        props.put("bootstrap.servers", "node004:9092,node005:9092,node006:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        /*创建生产者*/
        Producer<String, String> producer = new KafkaProducer<>(props);
        //生产者发送消息
        //sedMsg(topicName, producer);
        
        //同步发送消息
        //sedBySynchronous(topicName, producer);
        
        //异步发送消息
        //sedByAsynchronous(topicName, producer);
        
        //自定义分区器
        //props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        //customPartitioner(topicName, props);
        
        //针对消费者的 ==》独立的消费者
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        Producer<Integer, String> producerByInt = new KafkaProducer<>(props);
        topicName = "Kafka-Partitioner-Test-01";
        sedMsgByIntKey(topicName, producerByInt);
    }


    /**
     * 生产者发送消息
     * @param topicName
     * @param producer
     */
	private static void sedMsg(String topicName, Producer<String, String> producer) {
        for (int i = 100; i < 200; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, "hello" + i, "world" + i);
            /* 发送消息*/
            producer.send(record);
        }

        /*关闭生产者*/
        producer.close();
	}
	
	private static void sedMsgByIntKey(String topicName, Producer<Integer, String> producer) {
        for (int i = 100; i < 200; i++) {
            ProducerRecord<Integer, String> record = new ProducerRecord<>(topicName, i, "world" + i);
            /* 发送消息*/
            producer.send(record);
        }

        /*关闭生产者*/
        producer.close();
	}
    
	/**
	 * 同步发送消息
	 * @param topicName
	 * @param producer
	 */
	private static void sedBySynchronous(String topicName, Producer<String, String> producer) {
		for (int i = 0; i < 10; i++) {
            try {
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, "k" + i, "world" + i);
                /*同步发送消息*/
                RecordMetadata metadata = producer.send(record).get();
                System.out.printf("topic=%s, partition=%d, offset=%s \n",
                        metadata.topic(), metadata.partition(), metadata.offset());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        /*关闭生产者*/
        producer.close();
	}
    
    /**
     * 异步发送消息
     * @param topicName
     * @param producer
     */
	private static void sedByAsynchronous(String topicName, Producer<String, String> producer) {
		for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, "k" + i, "world" + i);
            /*异步发送消息，并监听回调*/
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        System.out.println("进行异常处理");
                    } else {
                        System.out.printf("topic=%s, partition=%d, offset=%s \n",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    }
                }
            });
        }

        /*关闭生产者*/
        producer.close();
	}
	
	/**
	 * 自定义分区器
	 * @param topicName
	 * @param props
	 */
	 private static void customPartitioner (String topicName, Properties props) {
			/*传递自定义分区器*/
	        props.put("partitioner.class", "com.spring.bigdata.cluster.kafka.producer.partitioners.CustomPartitioner");
	        /*传递分区器所需的参数*/
	        props.put("pass.line",6);

	        Producer<Integer, String> producer = new KafkaProducer<>(props);

	        for (int i = 0; i <= 10; i++) {
	            String score = "score:" + i;
	            ProducerRecord<Integer, String> record = new ProducerRecord<>(topicName, i, score);
	            /*异步发送消息*/
	            producer.send(record, (metadata, exception) ->
	                    System.out.printf("%s, partition=%d, \n", score, metadata.partition()));
	        }

	        producer.close();
		}
}