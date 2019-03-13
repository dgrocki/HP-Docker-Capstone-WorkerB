package com.hp.pwp.capstone
import com.surftools.BeanstalkClientImpl.ClientImpl
import com.surftools.BeanstalkClientImpl.JobImpl
import com.surftools.BeanstalkClient.BeanstalkException

class WorkerB{

	public static void main(String [] args) {
		BeanstalkClient beanstalk = new BeanstalkClient();  
		while(1){

			String s = beanstalk.recieve_new_work();
		
			println "Recieved from workerManager: " + s;
						
			PDFFormat imposition = new PDFFormat(s);
			imposition.start();	

			beanstalk.send_to_work_manager(s);
			
		}
		return;
	}

}
