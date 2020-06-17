package com.bigdata.yls_bigdata_es.high_rest.model;

/**
 * @Auther: yls
 * @Date: 2020/4/23 15:46
 * @Description: Es实体
 * @Version 1.0
 */
public class EsModel {
    //索引
    private String index;
    //类型
    private String type;
    //文档id
    private String docId;

    public EsModel(){

    }

    public EsModel(String index, String type, String docId) {
        this.index = index;
        this.type = type;
        this.docId = docId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public String toString() {
        return "EsModel{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", docId='" + docId + '\'' +
                '}';
    }
}
