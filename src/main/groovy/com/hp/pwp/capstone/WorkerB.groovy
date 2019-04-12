package com.hp.pwp.capstone
import com.surftools.BeanstalkClientImpl.ClientImpl
import com.surftools.BeanstalkClientImpl.JobImpl
import com.surftools.BeanstalkClient.BeanstalkException
import groovy.transform.Synchronized

class WorkerB{

	public static void main(String [] args) {
		BeanstalkClient beanstalk = new BeanstalkClient();  
		while(1){

			println "Before";
			String s = beanstalk.recieve_new_work();
		
			println "Recieved from workerManager: " + s;
						
			PDFFormat imposition = new PDFFormat();
			imposition.start(s);	

			sleep(1000);
			println "after sleep";
			beanstalk.send_to_work_manager(s);
		}
		return;
	}



}
