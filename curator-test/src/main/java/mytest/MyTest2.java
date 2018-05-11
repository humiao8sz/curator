package mytest;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class MyTest2 {
	private static String zookeeperConnectionString = "127.0.0.1:2181";
	private static String path = "/my/path3";
	public static void main(String args[]) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
/*		client.create().withMode(CreateMode.EPHEMERAL).forPath(path, "111".getBytes());
		
		NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener
        (
            new NodeCacheListener()
            {
                @Override
                public void nodeChanged() throws Exception
                {	
                    System.out.println("Node Change!!!");
                }
            }
        );
        cache.start(true);

        client.delete().forPath(path);
        client.create().forPath(path, "111".getBytes());
*/      
/*		for(String string : client.getChildren().forPath("/")){
			System.out.println(string);
		}*/
/*		InterProcessMutex lock = new InterProcessMutex(client, path);
		lock.acquire();
		for(int i =0;i<50;i++){
			Thread.sleep(500L);
			System.out.println(i);
		}
		lock.release();
*/		
/*		DistributedAtomicInteger d = new DistributedAtomicInteger(client, path, retryPolicy);
		while(true){
			Thread.sleep(1000L);
			AtomicValue<Integer> v = d.increment();
			System.out.println(v.postValue());
		}
*/		
/*		DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, path, 5);
		System.out.println("--------------------------wait");
		barrier.enter();
		System.out.println("--------------------------enter");
		Thread.sleep(10000L);
		barrier.leave();
		System.out.println("--------------------------leave");
		*/
		
		for(String string : client.getChildren().forPath("/my")){
			System.out.println(string);
		}
		
		Thread.sleep(1000000L);
		//client.close();
	}
}
