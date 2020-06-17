package com.bigdata.yls_bigdata_hive.controller;

import com.bigdata.yls_bigdata_hive.impl.HiveServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;


/**
 * 测试
 * @author yls
 *
 */
@RestController
public class HiveController {


	@Autowired
	private HiveServiceImpl hiveServiceImpl;

	@GetMapping("/hive_test")
	public String dataExport() {
		try {
			// HiveClientMgr.init(10);
			long start001 = System.currentTimeMillis();
			DataSource dataSource = hiveServiceImpl.getDataSource();
			System.out.println(dataSource);
			String sql = "drop table yls_practice.toss";
			hiveServiceImpl.uploadDataByHive(sql );
			long start002 = System.currentTimeMillis();
			System.out.println("耗时：" + (start002 - start001) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		} finally {

		}
		return "数据导出成功！";
	}
}
