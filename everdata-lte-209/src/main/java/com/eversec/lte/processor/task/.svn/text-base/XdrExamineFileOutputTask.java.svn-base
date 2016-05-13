package com.eversec.lte.processor.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;
import com.eversec.lte.sdtp.file.ExamineFileOutputTools;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;

/**
 * 
 * 
 */
public class XdrExamineFileOutputTask extends AbstractOutputTask<NotifyXDRDataReq> {

	public XdrExamineFileOutputTask(ArrayBlockingQueue<NotifyXDRDataReq> queue) {
		super(queue);
	}

	@Override
	public void run() {
		XdrSingleBytesDecoder xdrDecoder = new XdrSingleBytesDecoder();
		while (true) {
			Collection<NotifyXDRDataReq> coll = new ArrayList<>();
			int count = queue.drainTo(coll, drainMaxElements);
			
			if (count > 0) {
				try {
					for (NotifyXDRDataReq data : coll) {
						List<XdrSingleSource> xdrs = xdrDecoder.decode(data.getLoad());
						for (XdrSingleSource xdr : xdrs) {
//						OriginalXdrFileOutput.output(xdr);
							if(SdtpConfig.IS_USE_NEW_EXAMINE){
								ExamineFileOutputTools.output(xdr);
							}else{
								
//								ExamineXdrFileOutput.output(new IOutputSingleAdapter(xdr));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
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
