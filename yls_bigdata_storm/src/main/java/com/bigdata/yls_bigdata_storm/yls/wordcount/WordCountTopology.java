package com.bigdata.yls_bigdata_storm.yls.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * 
 * 实现单词计数的可运行topology
 * @author yls
 *
 */
public class WordCountTopology {
	private static final String SENTENCE_SPOUT_ID = "sentence-spout";
	private static final String SPLIT_BOLT_ID = "split-bolt";
	private static final String COUNT_BOLT_ID = "count-bolt";
	private static final String REPORT_BOLT_ID = "report-bolt";
	private static final String TOPOLOGY_NAME = "word-count-topology";

	@SuppressWarnings("resource")
	public static void main(String[] args)throws Exception {
		//模拟数据源spout
		SentenceSpout sentenceSpout = new SentenceSpout();
		//单词分割bolt
		SplitSentenceBolt splitSentenceBolt = new SplitSentenceBolt();
		//统计单词bolt
		WordCountBolt wordCountBolt = new WordCountBolt();
		//计数上报bolt
		ReportBlot reportBlot = new ReportBlot();
		
		//流式处理
		TopologyBuilder builder = new TopologyBuilder();
		//设置数据源
		builder.setSpout(SENTENCE_SPOUT_ID, sentenceSpout);
		//开始分割单词
		//builder.setBolt(SPLIT_BOLT_ID, splitSentenceBolt).shuffleGrouping(SENTENCE_SPOUT_ID);
		//开始统计单词
		//builder.setBolt(COUNT_BOLT_ID, wordCountBolt).fieldsGrouping(SPLIT_BOLT_ID, new Fields("word"));
		//设置task和executor，提高并发
		//开始分割单词
		builder.setBolt(SPLIT_BOLT_ID, splitSentenceBolt,2).setNumTasks(4).shuffleGrouping(SENTENCE_SPOUT_ID);
		//开始统计单词
		builder.setBolt(COUNT_BOLT_ID, wordCountBolt, 4).fieldsGrouping(SPLIT_BOLT_ID, new Fields("word"));
		//开始上报
		builder.setBolt(REPORT_BOLT_ID, reportBlot).globalGrouping(COUNT_BOLT_ID);
		
		Config config = new Config();
		//给topology增加worker
		config.setNumWorkers(2);
		//本地模式：开发模式
		LocalCluster localCluster = new LocalCluster();
		//本地模式提交topology
		localCluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology()).wait(10000);
//		Utils.sleep(10000);
		localCluster.killTopology(TOPOLOGY_NAME);
		localCluster.shutdown();
	}
}
