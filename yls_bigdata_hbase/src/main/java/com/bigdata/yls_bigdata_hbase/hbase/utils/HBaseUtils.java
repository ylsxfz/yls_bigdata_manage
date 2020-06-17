package com.bigdata.yls_bigdata_hbase.hbase.utils;

import com.bigdata.yls_bigdata_hbase.hbase.HbaseClient;
import javafx.util.Pair;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HBaseUtils {
	private static Connection connection = HbaseClient.connection;

	/**
	 * @Author yls
	 * @Description 创建 HBase 表
	 * @Date 2020/3/28 14:28
     * @param tableName      表名
     * @param columnFamilies 列族的数组
	 * @return
     */
    public static boolean createTable(String tableName, List<String> columnFamilies) {
        try {
            HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
            if (admin.tableExists(TableName.valueOf(tableName))) {
                return false;
            }
            TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
            columnFamilies.forEach(columnFamily -> {
                ColumnFamilyDescriptorBuilder cfDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
                cfDescriptorBuilder.setMaxVersions(1);
                ColumnFamilyDescriptor familyDescriptor = cfDescriptorBuilder.build();
                tableDescriptor.setColumnFamily(familyDescriptor);
            });
            admin.createTable(tableDescriptor.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * @Author yls
     * @Description 删除 hBase 表
     * @Date 2020/3/28 14:28
     * @param tableName 表名
     * @return boolean
     **/
    public static boolean deleteTable(String tableName) {
        try {
            HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
            TableName name = TableName.valueOf(tableName);
            // 删除表前需要先禁用表
            admin.disableTableAsync(name);
            admin.deleteTableAsync(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @Author yls
     * @Description 插入数据
     * @Date 2020/3/28 14:29
     * @param tableName     表名
     * @param rowKey       唯一标识
     * @param columnFamilyName 列族名
     * @param qualifier    列标识
     * @param value       数据
     * @return boolean
     **/
    public static boolean putRow(String tableName, String rowKey, String columnFamilyName, String qualifier,
                                 String value) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * @Author yls
     * @Description 插入数据
     * @Date 2020/3/28 14:29
     * @param tableName  表名
     * @param rowKey   唯一标识
     * @param columnFamilyName 列族名
     * @param pairList   列标识和值的集合
     * @return boolean
     **/
    public static boolean putRow(String tableName, String rowKey, String columnFamilyName, List<Pair<String, String>> pairList) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            pairList.forEach(pair -> put.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(pair.getKey()), Bytes.toBytes(pair.getValue())));
            table.put(put);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * @Author yls
     * @Date 2020/3/28 14:31
     * @Description 根据rowKey获取指定行的数据
     * @param tableName 表名
     * @param rowKey    唯一标识
     * @return org.apache.hadoop.hbase.client.Result
     **/
    public static Result getRow(String tableName, String rowKey) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            return table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Author yls
     * @Date 2020/3/28 14:31
     * @Description 获取指定行指定列(cell)的最新版本的数据
     * @param tableName    表名
     * @param rowKey       唯一标识
     * @param columnFamily 列族
     * @param qualifier    列标识
     * @return java.lang.String
     **/
    public static String getCell(String tableName, String rowKey, String columnFamily, String qualifier) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            if (!get.isCheckExistenceOnly()) {
                get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
                Result result = table.get(get);
                byte[] resultValue = result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
                return Bytes.toString(resultValue);
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Author yls
     * @Date 2020/3/28 14:31
     * @Description 检索全表
     * @param tableName 表名
     * @return org.apache.hadoop.hbase.client.ResultScanner
     **/
    public static ResultScanner getScanner(String tableName) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Author yls
     * @Description 检索表中指定数据
     * @param tableName  表名
     * @param filterList 过滤器
     * @return org.apache.hadoop.hbase.client.ResultScanner
     **/
    public static ResultScanner getScanner(String tableName, FilterList filterList) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setFilter(filterList);
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author yls
     * @Date 2020/3/28 14:31
     * @Description 检索表中指定数据
     * @param tableName   表名
     * @param startRowKey 起始RowKey
     * @param endRowKey   终止RowKey
     * @param filterList  过滤器
     * @return org.apache.hadoop.hbase.client.ResultScanner
     **/
    public static ResultScanner getScanner(String tableName, String startRowKey, String endRowKey,
                                           FilterList filterList) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStopRow(Bytes.toBytes(endRowKey));
            scan.setFilter(filterList);
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author yls
     * @Date 2020/3/28 14:32
     * @Description 删除指定行记录
     * @param tableName 表名
     * @param rowKey    唯一标识
     * @return boolean
     **/
    public static boolean deleteRow(String tableName, String rowKey) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * @Author yls
     * @Description 删除指定行指定列
     * @param tableName  表名
     * @param rowKey     唯一标识
     * @param familyName 列族
     * @param qualifier  列标识
     * @return boolean
     **/
    public static boolean deleteColumn(String tableName, String rowKey, String familyName,
                                          String qualifier) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier));
            table.delete(delete);
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}