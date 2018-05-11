package mytest;

import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class MyTest {
	private static String zookeeperConnectionString = "127.0.0.1:2181";
	private static String path = "/my/111";
	public static void main(String args[]) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		client.create().withMode(CreateMode.PERSISTENT).inBackground(new BackgroundCallback() {
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println(event.getPath()+"="+event.getType().name());
			}
		}, Executors.newFixedThreadPool(1)).forPath(path, "333".getBytes());
		client.getZookeeperClient().getZooKeeper().getSessionId();
/*		NodeCache cache = new NodeCache(client, path);
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
		//client.delete().forPath("/my");
		//client.delete().forPath("/test");
/*		for(String string : client.getChildren().forPath("/zookeeper")){
			//client.delete().forPath("/"+string);
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
/*		DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, path, 2);
		System.out.println("--------------------------wait");
		barrier.enter();
		System.out.println("--------------------------enter");
		barrier.leave();
		System.out.println("--------------------------leave");*/
		
		
		

		Thread.sleep(1000000L);
		//client.close();
	}
}
