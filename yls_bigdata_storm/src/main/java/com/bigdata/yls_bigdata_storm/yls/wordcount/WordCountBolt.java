package com.bigdata.yls_bigdata_storm.yls.wordcount;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
/**
 * 实现单词统计
 * @author yls
 *
 */
public class WordCountBolt extends BaseRichBolt{

	private OutputCollector collector;
	
	private HashMap<String, Long> counts = null;
	
	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		Long count = this.counts.get(word);
		if (count==null) {
			count =0L;
		}
		count++;
		this.counts.put(word, count);
		this.collector.emit(new Values(word,count));
	}

	@Override
	public void prepare(Map<String, Object> config, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.counts = new HashMap<>();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word","count"));
	}

}
