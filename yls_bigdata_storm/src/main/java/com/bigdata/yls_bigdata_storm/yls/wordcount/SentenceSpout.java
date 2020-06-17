package com.bigdata.yls_bigdata_storm.yls.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;

/**
 * 模拟数据源
 * 
 * @author yls
 *
 */
public class SentenceSpout extends BaseRichSpout {
	// 输出控制器
	private SpoutOutputCollector collector;

	private String[] sentences = { "my dog has fleas", "i like cold beverages", "the dog ate my homework",
			"don't have a cow man", "i don't think i like fleas" };

	private int index = 0;

	@Override
	public void nextTuple() {
		Values values = new Values(sentences[index]);
		System.out.println(sentences[index]);
		this.collector.emit(values);
		index++;
		if (index >= sentences.length) {
			index = 0;
		}
		Utils.sleep(1000);
	}

	@Override
	public void open(Map<String, Object> config, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));
	}

}
