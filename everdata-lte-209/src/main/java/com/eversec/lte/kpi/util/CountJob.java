package com.eversec.lte.kpi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eversec.lte.kpi.KafkaConsumerXdrSignalling;
import com.eversec.lte.kpi.KafkaConsumerXdrSignallingKPI;
import com.eversec.lte.kpi.KafkaConsumerXdrSignalling_S1C;

public class CountJob implements Job {

	SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public CountJob() {

	}

//	@Override
//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		for (KafkaConsumerXdrSignallingKPI k : KafkaConsumerXdrSignallingKPI.owner) {
//			k.showCount(DateFormat.format(new Date()));
//		}
//	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		for (KafkaConsumerXdrSignalling_S1C k : KafkaConsumerXdrSignalling_S1C.owner) {
			k.showCount(DateFormat.format(new Date()));
		}
	}

}
