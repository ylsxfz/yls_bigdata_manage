package com.bigdata.yls_bigdata_zookeeper.test;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

/**
 * @author : heibaiying
 * @description : curator客户端API基本使用
 */
public class BasicOperation {


    private static CuratorFramework client = null;
    private static final String zkServerPath = "192.168.133.101:2181";
    private static final String nodePath = "/yls_test";
    
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
     * 获取当前zookeeper客户端的的状态
     */
    
    public static void  getStatus() {
        CuratorFrameworkState state = client.getState();
        System.out.println("服务是否已经启动:" + (state == CuratorFrameworkState.STARTED));
    }


    /**
     * 创建节点(s)
     * public enum CreateMode {
     * 		// 永久节点
     *		PERSISTENT (0, false, false),
     *		//永久有序节点
     *		PERSISTENT_SEQUENTIAL (2, false, true),
	 *	    // 临时节点
	 *	    EPHEMERAL (1, true, false),
	 *	    // 临时有序节点
	 *	    EPHEMERAL_SEQUENTIAL (3, true, true);
	 *	    ....
	 *	}
     */
    
    public static void  createNodes(String nodeMsg) throws Exception {
        byte[] data = nodeMsg.getBytes();
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)      //节点类型
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(nodePath, data);
        
    }


    /**
     * 获取节点信息
     */
    
    public static void  getNode() throws Exception {
        Stat stat = new Stat();
        byte[] data = client.getData().storingStatIn(stat).forPath(nodePath);
        System.out.println("节点数据:" + new String(data));
        System.out.println("节点信息:" + stat.toString());
    }

    /**
     * 获取该节点的所有子节点
     */
    
    public static void  getChildrenNodes() throws Exception {
        List<String> childNodes = client.getChildren().forPath(nodePath);
        for (String s : childNodes) {
            System.out.println(s);
        }
    }


    /**
     * 更新节点
     */
    
    public static void  updateNode() throws Exception {
        byte[] newData = "defg".getBytes();
        client.setData().withVersion(2)     // 传入版本号，如果版本号错误则拒绝更新操作,并抛出BadVersion异常
                .forPath(nodePath, newData);
    }


    /**
     * 删除节点
     */
    
    public static void  deleteNodes() throws Exception {
        client.delete()
                .guaranteed()                // 如果删除失败，那么在会继续执行，直到成功
                .deletingChildrenIfNeeded()  // 如果有子节点，则递归删除
                .withVersion(0)              // 传入版本号，如果版本号错误则拒绝删除操作,并抛出BadVersion异常
                .forPath(nodePath);
    }


    /**
     * 判断节点是否存在
     * @return 
     */
    
    public static boolean  existNode() throws Exception {
        // 如果节点存在则返回其状态信息如果不存在则为null
        Stat stat = client.checkExists().forPath(nodePath);
        System.out.println("节点是否存在:" + !(stat == null));
		return !(stat == null);
    }


    /**
     * 使用usingWatcher注册的监听是一次性的,即监听只会触发一次，监听完毕后就销毁
     */
    
    public static void  DisposableWatch() throws Exception {
        client.getData().usingWatcher(new CuratorWatcher() {
            public void  process(WatchedEvent event) {
                System.out.println("节点" + event.getPath() + "发生了事件:" + event.getType());
            }
        }).forPath(nodePath);
        Thread.sleep(1000 * 1000);  //休眠以观察测试效果
    }


    public static void destroy() {
        if (client != null) {
            client.close();
        }
    }

    
    public static void main(String[] args) {
		prepare();
		
		getStatus();
		String nodeMsg = "abc";
		
		try {
			if (!existNode()) {
				createNodes(nodeMsg);
			}
			
			//updateNode();
			//getNode();
			
			//getChildrenNodes();
			
			//updateNode();
			
			//getNode();
			
			//getChildrenNodes();
			
			//deleteNodes();
			
			//existNode();
			//DisposableWatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
