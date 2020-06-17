package com.bigdata.yls_bigdata_kafka.jdbc;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * Kafka消费者和消费者组
 */
public class ConsumerJDBC {

	public static void main(String[] args) {
		String group = "group1";
		String topic = "fy-test-1";
		Properties props = new Properties();
		props.put("bootstrap.servers", "node004:9092,node005:9092,node006:9092");
		/* 指定分组ID */
		props.put("group.id", group);
		props.put("enable.auto.commit", true);
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

		// 订阅主题(s)
		consumer.subscribe(Collections.singletonList(topic));

		// 轮询获取数据
		//consumerGroup(consumer);
		
		/**
		 * 手动提交偏移量
		 */
		//同步提交
		//consumerSyn(consumer);
		
		//异步提交
		//consumerASyn(consumer);

		//同步加异步提交
		//consumerASynAndSyn(consumer);
		
		//提交特定的偏移量
		//consumerASynWithOffsets(consumer);
		
		//监听分区再均衡
		//rebalanceListener(topic, consumer);
		
		//退出轮询
		//consumerExit(consumer);
		
		//独立的消费者
		topic = "Kafka-Partitioner-Test-01";
		props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        KafkaConsumer<Integer, String> standaConsumer = new KafkaConsumer<>(props);
		standaloneConsumer(topic, standaConsumer);
	}

	/**
	 * 轮询获取数据
	 * 
	 * @param consumer
	 */
	private static void consumerGroup(KafkaConsumer<String, String> consumer) {
		try {
			while (true) {
				/* 轮询获取数据 */
				ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
				for (ConsumerRecord<String, String> record : records) {
					System.out.printf("topic = %s,partition = %d, key = %s, value = %s, offset = %d,\n", record.topic(),
							record.partition(), record.key(), record.value(), record.offset());
				}
			}
		} finally {
			consumer.close();
		}
	}
	
	/*********************************手动提交偏移量-start**********************************************/
	/**
	 * 同步提交
	 * @param consumer
	 */
	private static void consumerSyn(KafkaConsumer<String, String> consumer) {
		try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                }
                 /*同步提交*/
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
	}

	/**
	 * 异步提交
	 * @param consumer
	 */
	private static void consumerASyn(KafkaConsumer<String, String> consumer) {
		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
				for (ConsumerRecord<String, String> record : records) {
					System.out.println(record);
				}
				/* 异步提交并定义回调 */
				consumer.commitAsync(new OffsetCommitCallback() {
					@Override
					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
						if (exception != null) {
							System.out.println("错误处理");
							offsets.forEach((x, y) -> System.out.printf("topic = %s,partition = %d, offset = %s \n",
									x.topic(), x.partition(), y.offset()));
						}
					}
				});
			}
		} finally {
			consumer.close();
		}
	}
	
	/**
	 * 同步加异步提交
	 * @param consumer
	 */
	private static void consumerASynAndSyn(KafkaConsumer<String, String> consumer) {
		try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                }
                // 异步提交
                consumer.commitAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 因为即将要关闭消费者，所以要用同步提交保证提交成功
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
	}
	
	/**
	 * 提交特定的偏移量
	 * @param consumer
	 */
	private static void consumerASynWithOffsets(KafkaConsumer<String, String> consumer) {
		Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                    /*记录每个主题的每个分区的偏移量*/
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset()+1, "no metaData");
                    /*TopicPartition重写过hashCode和equals方法，所以能够保证同一主题和分区的实例不会被重复添加*/
                    offsets.put(topicPartition, offsetAndMetadata);
                }
                /*提交特定偏移量*/
                consumer.commitAsync(offsets, null);
            }
        } finally {
            consumer.close();
        }
	}
	/*********************************手动提交偏移量-end**********************************************/

	/**
	 * 监听分区再均衡
	 * @param topic
	 * @param consumer
	 */
	private static void rebalanceListener(String topic, KafkaConsumer<String, String> consumer) {
		Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        consumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {

            /*该方法会在消费者停止读取消息之后，再均衡开始之前就调用*/
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("再均衡即将触发");
                // 提交已经处理的偏移量
                consumer.commitSync(offsets);
            }

            /*该方法会在重新分配分区之后，消费者开始读取消息之前被调用*/
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

            }
        });

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1, "no metaData");
                    /*TopicPartition重写过hashCode和equals方法，所以能够保证同一主题和分区的实例不会被重复添加*/
                    offsets.put(topicPartition, offsetAndMetadata);
                }
                consumer.commitAsync(offsets, null);
            }
        } finally {
            consumer.close();
        }
	}
	
	/**
	 * 退出轮询
	 * @param consumer
	 */
	private static void consumerExit(KafkaConsumer<String, String> consumer) {
		/*调用wakeup优雅的退出*/
        final Thread mainThread = Thread.currentThread();
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                if ("exit".equals(sc.next())) {
                    consumer.wakeup();
                    try {
                        /*等待主线程完成提交偏移量、关闭消费者等操作*/
                        mainThread.join();
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic = %s,partition = %d, key = %s, value = %s, offset = %d,\n",
                            record.topic(), record.partition(), record.key(), record.value(), record.offset());
                }
            }
        } catch (WakeupException e) {
            //对于wakeup()调用引起的WakeupException异常可以不必处理
        } finally {
            consumer.close();
            System.out.println("consumer关闭");
        }
	}
	
	/**
	 * 独立的消费者
	 * @param topic
	 * @param consumer
	 */
	private static void standaloneConsumer(String topic, KafkaConsumer<Integer, String> consumer) {
		List<TopicPartition> partitions = new ArrayList<>();
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);

         /*可以指定读取哪些分区 如这里假设只读取主题的0分区*/
        for (PartitionInfo partition : partitionInfos) {
            if (partition.partition()==0){
                partitions.add(new TopicPartition(partition.topic(), partition.partition()));
            }
        }

        // 为消费者指定分区
        consumer.assign(partitions);


        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.printf("partition = %s, key = %d, value = %s\n",
                        record.partition(), record.key(), record.value());
            }
            consumer.commitSync();
        }
	}
}
