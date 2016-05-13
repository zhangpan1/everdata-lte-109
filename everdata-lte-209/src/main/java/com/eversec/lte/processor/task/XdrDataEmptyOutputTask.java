package com.eversec.lte.processor.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;

/**
 * 
 * 
 */
public class XdrDataEmptyOutputTask extends AbstractOutputTask<NotifyXDRDataReq> {

	public XdrDataEmptyOutputTask(ArrayBlockingQueue<NotifyXDRDataReq> queue) {
		super(queue);
	}

	@Override
	public void run() {
		XdrSingleBytesDecoder xdrDecoder = new XdrSingleBytesDecoder();
		while (true) {
			Collection<NotifyXDRDataReq> coll = new ArrayList<>();
			int count = queue.drainTo(coll, drainMaxElements);
			if (count > 0) {
				for (NotifyXDRDataReq data : coll) {
					xdrDecoder.decode(data.getLoad());
				}
			} else {
				try {
					TimeUnit.MILLISECONDS.sleep(drainTaskSleepMills);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
