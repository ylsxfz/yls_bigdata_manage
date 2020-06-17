package com.bigdata.yls_bigdata_hdfs.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.springframework.stereotype.Component;

import java.net.URI;


/**
 * @Author yls
 * @Description  hdfs的客户端初始化
 * @Date 2020/3/29 9:42
 **/
@Component
public class HdfsClient {

	public static FileSystem fs = null;

	public static YarnClient yarnClient= null;

	/**
	 * @Author yls
	 * @Description hdfs客户端的初始化方法
	 * @Date 2020/3/29 9:41
	 * @param url hdfs的url地址 例如：hdfs://192.168.1.1:9000
	 * @return void
	 **/
	public void init(String url) throws Exception {
		// 构造一个配置参数对象，设置一个参数：我们要访问的hdfs的URI
		// 从而FileSystem.get()方法就知道应该是去构造一个访问hdfs文件系统的客户端，以及hdfs的访问地址
		// new Configuration();的时候，它就会去加载jar包中的hdfs-default.xml
		// 然后再加载classpath下的hdfs-site.xml
		Configuration conf = new Configuration();
		// conf.set("fs.defaultFS", "hdfs://hdp-node01:9000");
		/**
		 * 参数优先级： 1、客户端代码中设置的值 2、classpath下的用户自定义配置文件 3、然后是服务器的默认配置
		 */
		// conf.set("dfs.replication", "3");

		// 获取一个hdfs的访问客户端，根据参数，这个实例应该是DistributedFileSystem的实例
		// fs = FileSystem.get(conf);

		// 如果这样去获取，那conf里面就可以不要配"fs.defaultFS"参数，而且，这个客户端的身份标识已经是hadoop用户
		fs = FileSystem.get(new URI(url), conf, "hadoop");
		
		
	}

	/**
	 * @Author yls
	 * @Description 初始化yarnClient
	 * @Date 2020/3/29 9:42
	 * @param yarnResAddress yarn的管理地址
	 * @return void
	 **/
	public void  initYarnClient(String yarnResAddress) {
		//yarn的相关配置
		Configuration yarnConf = new Configuration();
		yarnConf.set("yarn.resourcemanager.address",yarnResAddress );
		yarnClient = YarnClient.createYarnClient();
		yarnClient.init(yarnConf);
		yarnClient.start();
	}
	
	
}
