package com.hp.pwp.capstone
import com.surftools.BeanstalkClientImpl.ClientImpl
import com.surftools.BeanstalkClientImpl.JobImpl
import com.surftools.BeanstalkClient.BeanstalkException

class WorkerB{

	public static void main(String [] args) {
		BeanstalkClient beanstalk = new BeanstalkClient();		
		while(1){
			beanstalk.recieve_new_work();
		}
		return;
	}

}
