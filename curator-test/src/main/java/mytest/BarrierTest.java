package mytest;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.testng.Assert;

public class BarrierTest {
	private static String zookeeperConnectionString = "127.0.0.1:2181";
	private static String path = "/my/path6";

	public static void main(String args[]) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		PathChildrenCache cache = new PathChildrenCache(client, "/my", true);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				if(event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED){
					System.out.println("--------------------------------------" + event.getData());
				}
			}
		});
		cache.start();

		client.create().forPath("/my/three", "three".getBytes());
		client.create().forPath("/my/four", "four".getBytes());
		Thread.sleep(1000000L);
		// client.close();
	}
}