package com.bigdata.yls_bigdata_hdfs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yls
 * @Description hadoop的基本配置文件
 * @Date 2020/3/29 9:43
 **/
@Configuration
public class HadoopConf {

    //Hadoop的RM地址
    @Value("${hadoop.yarn.resourcemanager.address}")
    private String yarnResourcemanagerAddress;

    //hdfs的具体地址
    @Value("${hadoop.hdfs.address}")
    private String hdfsAddress;


    public HadoopConf() {
    }

    public String getYarnResourcemanagerAddress() {
        return yarnResourcemanagerAddress;
    }

    public void setYarnResourcemanagerAddress(String yarnResourcemanagerAddress) {
        this.yarnResourcemanagerAddress = yarnResourcemanagerAddress;
    }

    public String getHdfsAddress() {
        return hdfsAddress;
    }

    public void setHdfsAddress(String hdfsAddress) {
        this.hdfsAddress = hdfsAddress;
    }

    @Override
    public String toString() {
        return "HadoopConf{" +
                "yarnResourcemanagerAddress='" + yarnResourcemanagerAddress + '\'' +
                ", hdfsAddress='" + hdfsAddress + '\'' +
                '}';
    }
}
