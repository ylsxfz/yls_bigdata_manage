package com.bigdata.yls_bigdata_zookeeper.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * @author : heibaiying
 * @description : 使用curator操作Zookeeper ACL
 */
public class AclOperation {


    private static CuratorFramework client = null;
    private static final String zkServerPath = "192.168.133.101:2181";
    private static final String nodePath = "/yls_test_02";
    
    public static void prepare() {
        // 重试策略
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .build();  //指定命名空间后，client的所有路径操作都会以/workspace开头
        client.start();
    }



    /**
     * 新建节点并赋予权限
     */
    public static void createNodesWithAcl() throws Exception {
        List<ACL> aclList = new ArrayList<>();
        // 对密码进行加密
        String digest1 = DigestAuthenticationProvider.generateDigest("heibai:123456");
        String digest2 = DigestAuthenticationProvider.generateDigest("ying:123456");
        Id user01 = new Id("digest", digest1);
        Id user02 = new Id("digest", digest2);
        // 指定所有权限
        aclList.add(new ACL(Perms.ALL, user01));
        // 如果想要指定权限的组合，中间需要使用 | ,这里的|代表的是位运算中的 按位或
        aclList.add(new ACL(Perms.DELETE | Perms.CREATE, user02));

        // 创建节点
        byte[] data = "abc".getBytes();
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(aclList)
                .forPath(nodePath, data);
    }


    /**
     * 给已有节点设置权限,注意这会删除所有原来节点上已有的权限设置
     */
    public static void setAcl() throws Exception {
        String digest = DigestAuthenticationProvider.generateDigest("admin:admin");
        Id user = new Id("digest", digest);
        client.setACL()
                .withACL(Collections.singletonList(new ACL(Perms.READ | Perms.DELETE, user)))
                .forPath(nodePath);
    }


    /**
     * 获取权限
     */
    public static void getAcl() throws Exception {
        List<ACL> aclList = client.getACL().forPath(nodePath);
        ACL acl = aclList.get(0);
        System.out.println(acl.getId().getId() + "是否有删读权限:" + (acl.getPerms() == (Perms.READ | Perms.DELETE)));
    }


    public static void destroy() {
        if (client != null) {
            client.close();
        }
    }

    
    public static void main(String[] args) {
		prepare();
		try {
			createNodesWithAcl();
			getAcl();
			setAcl();
			getAcl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
