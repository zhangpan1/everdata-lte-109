package com.eversec.lte.processor.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.kafka.producer.KafkaByteProducer;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;

/**
 * 
 * @author bieremayi
 * 
 */
public class RawDataKafkaOutputTask extends
		AbstractOutputTask<XDRRawDataSendReq> {

	private final String topic = "raw";

	public RawDataKafkaOutputTask(ArrayBlockingQueue<XDRRawDataSendReq> queue) {
		super(queue);
	}

	@Override
	public void run() {
		while (true) {
			KafkaByteProducer producer = new KafkaByteProducer(
					SdtpConfig.getKafkaRawBrokerList());
			Collection<XDRRawDataSendReq> coll = new ArrayList<>();
			try {
				while (true) {
					int count = queue.drainTo(coll, drainMaxElements);
					if (count > 0) {
						for (XDRRawDataSendReq data : coll) {
							producer.sendBytes(topic, data.toByteArray());
						}
					} else {
						try {
							TimeUnit.MILLISECONDS.sleep(drainTaskSleepMills);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					coll.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				producer.close();
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
