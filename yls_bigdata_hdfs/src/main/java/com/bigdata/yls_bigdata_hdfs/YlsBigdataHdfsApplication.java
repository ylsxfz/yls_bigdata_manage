package com.bigdata.yls_bigdata_hdfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Author yls
 * @Description 关于hdfs的一些简单操作
 * @Date 2020/3/29 19:08
 **/
@SpringBootApplication
@ServletComponentScan //自动扫描
public class YlsBigdataHdfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YlsBigdataHdfsApplication.class, args);
    }

}
