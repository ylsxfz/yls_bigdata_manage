package com.bigdata.yls_bigdata_hbase.hbase_phoenix.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;

@RestController
public class HbasePhoenixController {

	@Autowired
	private HbasePhoenixServiceImpl phoenixServiceImpl;
	
	@GetMapping("/hbase_phoenix_test")
	public String hbase_phoenix_test() {

		try {
			// HiveClientMgr.init(10);
			System.out.println("测试hbase_phoenix_test：");
			long start001 = System.currentTimeMillis();
			DataSource dataSource = phoenixServiceImpl.getDataSource();
			System.out.println(dataSource);

			List<USPopulation> queryAll = phoenixServiceImpl.queryAll();
			queryAll.forEach(value->{
				System.out.println(value);
			});
			
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
