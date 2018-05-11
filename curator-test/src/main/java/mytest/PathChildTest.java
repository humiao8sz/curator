package mytest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class PathChildTest {
	private static String zookeeperConnectionString = "127.0.0.1:2181";
	private static String path = "/my/path6";
	public static void main(String args[]) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		
		
        final DistributedBarrier barrier = new DistributedBarrier(client, "/barrier");
        barrier.setBarrier();

        ExecutorService               service = Executors.newSingleThreadExecutor();
        service.submit
        (
            new Callable<Object>()
            {
                @Override
                public Object call() throws Exception
                {
                    Thread.sleep(20000L);
                    barrier.removeBarrier();
                    return null;
                }
            }
        );
        barrier.waitOnBarrier();
        System.out.println("--------------------------------------");

		Thread.sleep(1000000L);
		//client.close();
	}
}
