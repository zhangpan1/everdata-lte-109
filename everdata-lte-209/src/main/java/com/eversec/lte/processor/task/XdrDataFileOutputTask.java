package com.eversec.lte.processor.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;
import com.eversec.lte.sdtp.file.SdtpFileGroupingSourceOutputTools;
import com.eversec.lte.sdtp.file.SdtpFileSourceOutputTools;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;

/**
 * 
 * @author bieremayi
 * 
 */
public class XdrDataFileOutputTask extends AbstractOutputTask<NotifyXDRDataReq> {

	public XdrDataFileOutputTask(ArrayBlockingQueue<NotifyXDRDataReq> queue) {
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
					List<XdrSingleSource> xdrs = xdrDecoder.decode(data.getLoad());
					for (XdrSingleSource xdr : xdrs) {
//						OriginalXdrFileOutput.output(xdr);
						if(SdtpConfig.IS_USE_GROUPING_OUTPUT){
							SdtpFileGroupingSourceOutputTools.output(xdr);

						}else{
							
							SdtpFileSourceOutputTools.output(xdr);
						}
					}
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
