package com.bigdata.yls_bigdata_es.high_rest.document;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Auther: yls
 * @Date: 2020/4/23 16:13
 * @Description:
 * @Version 1.0
 */
public class ListenerUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ListenerUtils.class);

    /**
     * 功能描述:
     * 〈异步请求监听器〉
     *
     * @author : yls
     * @date : 2020/4/23 16:14
     * @param t 泛型
     * @return : org.elasticsearch.action.ActionListener
     */
    public static <T> ActionListener getListener(T t, RestHighLevelClient restHighLevelClient){
        return new ActionListener<T>(){

            @Override
            public void onResponse(T t) {
                System.err.println("response:"+t);
                try {
                    restHighLevelClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("异步提交ES异常："+e.getMessage());
            }
        };
    }
}
