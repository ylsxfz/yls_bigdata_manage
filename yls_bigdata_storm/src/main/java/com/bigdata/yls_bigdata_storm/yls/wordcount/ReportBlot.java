package com.bigdata.yls_bigdata_storm.yls.wordcount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/**
 * 实现上报bolt
 * @author yls
 *
 */
public class ReportBlot extends BaseRichBolt{
	
	private HashMap<String, Long> counts = null;

	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		Long count = tuple.getLongByField("count");
		this.counts.put(word, count);
	}

	@Override
	public void prepare(Map<String, Object> config, TopologyContext context, OutputCollector collector) {
		this.counts = new HashMap<>();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//改bolt不输出
	}

	
	//引入cleanup,bolt在终止一个bolt之前调用的方法
	@Override
	public void cleanup() {
		System.out.println("---FINAL COUNTS---");
		ArrayList<String> keys = new ArrayList<>();
		keys.addAll(this.counts.keySet());
		Collections.sort(keys);
		keys.forEach(key->{
			System.out.println(key+":"+this.counts.get(key));
		});
		System.out.println("------");
	}
}
