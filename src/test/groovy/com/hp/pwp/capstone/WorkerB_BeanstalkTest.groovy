package com.hp.pwp.capstone
import org.junit.Test
import com.surftools.BeanstalkClientImpl.ClientImpl
import com.surftools.BeanstalkClientImpl.JobImpl
import com.surftools.BeanstalkClient.Job
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

public class BeanstalkTest extends GroovyTestCase {
  @Test
  void testRecieveWork() {
  	def mock = new MockFor(ClientImpl)
	JobImpl job = new JobImpl(1);
	job.setData((byte [])"new_job");

	mock.demand.watch{"to_worker_b"};
	mock.demand.reserve{ job }
	mock.demand.delete{1};
	mock.use{
		BeanstalkClient b = new BeanstalkClient();
		String s = b.recieve_new_work();
		println s;
		assert( s == "new_job")
	}
	mock.expect.verify();
  }
@Test
  void testSendWork() {
  	def mock = new MockFor(ClientImpl)
	String json = "riak";
	long priority = 0;
	int delaySeconds = 0;
	int timeToRun = 10;
	byte[] data = json.getBytes();
	
	mock.demand.with{
		put{a, b,c, d -> return};
	}
//	mock.demand.put{true};
	mock.use{
		BeanstalkClient b = new BeanstalkClient();
		b.sendWork("riak");
	}
	mock.expect.verify()
  }
  @Test
  void testUseTube() {
  	def mock = new MockFor(ClientImpl)
//	String json = "hello"
//	long priority = 0;
//	int delaySeconds = 0;
//	int timeToRun = 10;
//	byte[] data = json.getBytes();
	
//	mock.demand.put{priority, delaySeconds, timeToRun, data};
	mock.demand.useTube{"riak"}
	mock.use{
		BeanstalkClient b = new BeanstalkClient();
		b.useTube("riak");
	}
	mock.expect.verify()
  }
}



