package mytest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * ��������LeaderLatch: leaderLatch.start();
 * һ�������� LeaderLatch�������ʹ����ͬlatch path������LeaderLatch���棬Ȼ�������ѡ������һ����Ϊleader�� 
 * �������ʱ�鿴һ��������ʵ���Ƿ���leader:
 */
public class LeaderLatch1
{
	private static final String PATH = "/leader";
	private static final int COUNT = 5;

	public static void main(String[] args) throws Exception
	{
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();

		List<LeaderLatch> list = new ArrayList<>();

		for (int i = 1; i <= COUNT; i++)
		{
			LeaderLatch example = new LeaderLatch(client, PATH, "Client #" + i);
			example.start();
			list.add(example);
		}

		TimeUnit.SECONDS.sleep(2);

		LeaderLatch leader = null;
		for (LeaderLatch ll : list)
		{
			if (ll.hasLeadership())
			{
				leader = ll;
			}
			System.out.println(ll.getId() + "\t" + ll.hasLeadership());
		}
		;

		TimeUnit.SECONDS.sleep(2);
		
		list.remove(leader);
		leader.close();
		
		TimeUnit.SECONDS.sleep(2);

		System.out.println("========================");
		
		for (LeaderLatch ll : list)
		{
			System.out.println(ll.getId() + "\t" + ll.hasLeadership());
		}

		for (LeaderLatch ll : list)
		{
			ll.close();
		}

		client.close();
	}
}
