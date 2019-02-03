package com.hp.pwp.capstone
import com.surftools.BeanstalkClientImpl.ClientImpl
import com.surftools.BeanstalkClientImpl.JobImpl
import com.surftools.BeanstalkClient.BeanstalkException

class WorkerB{
	private BeanstalkClient beanstalk = new BeanstalkClient();		
	//pull a new job off the new_work queue
	public recieve_new_work(){
		beanstalk.watchTube("new_work");
			
		JobImpl job = beanstalk.recieveWork();
		
		String s = new String(job.data);
		println s;
		
		beanstalk.deleteWork(job.jobId);
			
		return;
	}
	//put a new job on the to_workerB queue
	public send_to_workerB(){}
	//pull a job off of the riak queue
	public send_to_riak(){
		beanstalk.watchTube("riak");
		JobImpl job = beanstalk.recieveWork();
		
		String s = new String(job.data);
		println s;
		
		beanstalk.deleteWork(job.jobId);
			
		return;

	}
	//put a job on the status queue
	public send_status(){}


	public static void main(String [] args) {
		WorkerB wm = new WorkerB();

		//for testing only, only need a beanstalk client in the class
		BeanstalkClient beanstalk = new BeanstalkClient();		
	
		beanstalk.useTube("new_work");
		String input = "New work from the outside world";	
		beanstalk.sendWork(input);
		beanstalk.useTube("riak");
		input = "More stuff for Riak";	
		beanstalk.sendWork(input);
		beanstalk.useTube("new_work");
		input = "New work number 2 from the outside world";	
		beanstalk.sendWork(input);
		beanstalk.useTube("riak");
		input = "Time to put something in Riak from Worker B";	
		beanstalk.sendWork(input);
		
		
		wm.recieve_new_work();
		wm.send_to_riak();
		wm.recieve_new_work();
		wm.send_to_riak();
		return;
	}

}
