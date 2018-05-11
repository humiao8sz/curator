package mytest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * ��LeaderLatch��ȣ� ͨ��LeaderSelectorListener���Զ��쵼Ȩ���п��ƣ� ���ʵ���ʱ���ͷ��쵼Ȩ������ÿ���ڵ㶼�п��ܻ���쵼Ȩ�� 
 * ��LeaderLatchһ������� ���ǵ���close�����������������ͷ��쵼Ȩ��
 */
public class LeaderLatch2
{
	private static final String PATH = "/leader";
	private static final int COUNT = 5;
	
	public static void main(String[] args) throws Exception
	{
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		List<MyLeaderSelectorListenerAdapter> list = new ArrayList<>();
		
		for (int i = 1; i <= COUNT; i++)
		{
			MyLeaderSelectorListenerAdapter example = new MyLeaderSelectorListenerAdapter(client, PATH, "Client #" + i);
			list.add(example);
		}
		
		TimeUnit.SECONDS.sleep(20);
		
		for (MyLeaderSelectorListenerAdapter listener : list)
		{
			listener.close();
		}
		
		client.close();
	}
}
class MyLeaderSelectorListenerAdapter extends LeaderSelectorListenerAdapter
{
	private final String name;
    private final LeaderSelector leaderSelector;
    
    public MyLeaderSelectorListenerAdapter(CuratorFramework client, String path, String name)
    {
    	this.name = name;
    	leaderSelector = new LeaderSelector(client, path, this);
    	
    	//��֤�ڴ�ʵ���ͷ��쵼Ȩ֮�󻹿��ܻ���쵼Ȩ��
    	leaderSelector.autoRequeue();
    	
    	leaderSelector.start();
    }
    
    public void close()
    {
    	leaderSelector.close();
    }
    
    /**
     * �������takeLeadership��������ķ���ȵȣ����Ҳ�Ҫ���أ��������ҪҪ��ʵ��һֱ��leader�Ļ����Լ�һ����ѭ����
     * һ���˷���ִ�����֮�󣬾ͻ�����ѡ��
     */
	public void takeLeadership(CuratorFramework client) throws Exception
	{
		System.out.println(name + " ��ѡΪleader");
		TimeUnit.SECONDS.sleep(2);
	}
}