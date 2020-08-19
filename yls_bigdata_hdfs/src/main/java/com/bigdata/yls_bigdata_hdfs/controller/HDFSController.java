package com.bigdata.yls_bigdata_hdfs.controller;

import com.bigdata.yls_bigdata_hdfs.config.HadoopConf;
import com.bigdata.yls_bigdata_hdfs.hdfs.HdfsClient;
import com.bigdata.yls_bigdata_hdfs.utils.HdfsClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试
 * @author yls
 *
 */
@RestController
public class HDFSController {

	@Autowired
	private HadoopConf hadoopConf;

	@Autowired
	private HdfsClient hdfsClient;


	@GetMapping("/hadoop_test")
	public String dataExport() {
		try{
			hdfsClient.init(hadoopConf.getHdfsAddress());
			System.out.println(hdfsClient.fs);
		}catch (Exception e){
			e.printStackTrace();
		}

		try {
			hdfsClient.initYarnClient(hadoopConf.getYarnResourcemanagerAddress());
			System.out.println("yarn初始化成功："+hdfsClient.yarnClient);
			System.out.println("yarn的所有队列："+hdfsClient.yarnClient.getAllQueues());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("yarn初始化失败："+e.getMessage());
		}
        try {

        	long start001 = System.currentTimeMillis();
        	List<String> list = new ArrayList<String>();
        	HdfsClientUtils.viewEndDirFromHdfsUrl("/", list);
        	list.forEach(value -> {
        		System.out.println(value);
        	});
        	//HdfsClientUtils.viewEndDirFromHdfsUrl("hdfs://192.168.133.101:9000/");
            long start002 = System.currentTimeMillis();
			System.out.println("耗时："+(start002-start001)+"ms");
        } catch (Exception e) {
			System.out.println("下载文件失败。"+e.getMessage());
            return "下载文件失败！";
        } finally {
        	
        }
		return "下载文件成功！";
    }
}
