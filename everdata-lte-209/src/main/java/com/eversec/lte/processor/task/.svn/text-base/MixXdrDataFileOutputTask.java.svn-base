package com.eversec.lte.processor.task;

import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_EXAMINE_XDR_2_FILE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.output.CompXdrFileOutput;
import com.eversec.lte.processor.decoder.XdrAppBytesDecoder;
import com.eversec.lte.sdtp.file.ExamineFileOutputTools;
import com.eversec.lte.sdtp.file.SdtpFileFillOutputTools;
import com.eversec.lte.sdtp.file.SdtpFileGroupingFillOutputTools;
import com.eversec.lte.sdtp.file.SdtpFileGroupingSourceOutputTools;
import com.eversec.lte.sdtp.file.SdtpFileSourceOutputTools;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.vo.AppMessage;

/**
 * 模拟应用层输出文件任务(包括合成xdr,单接口xdr)
 * 
 * @author bieremayi
 * 
 */
public class MixXdrDataFileOutputTask extends
		AbstractOutputTask<NotifyXDRDataReq> {

	public MixXdrDataFileOutputTask(ArrayBlockingQueue<NotifyXDRDataReq> queue) {
		super(queue);
	}

	@Override
	public void run() {
		XdrAppBytesDecoder appDecoder = new XdrAppBytesDecoder();
		Collection<NotifyXDRDataReq> coll = new ArrayList<>();
		while (true) {
			int count = queue.drainTo(coll, drainMaxElements);
			if (count > 0) {
				for (NotifyXDRDataReq req : coll) {
					try {
						AppMessage appMsg = appDecoder.decode(req.getLoad());
						List<XdrSingleSource> xdrs = appMsg.getXdrs();
						if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_FILE) {
							for (XdrSingleSource xdr : xdrs) {
								if(SdtpConfig.IS_USE_GROUPING_OUTPUT){
									SdtpFileGroupingFillOutputTools.output(xdr);
								}else{
									SdtpFileFillOutputTools.output(xdr);
								}
							}
						} else if (SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
							for (XdrSingleSource xdr : xdrs) {
								if(SdtpConfig.IS_USE_GROUPING_OUTPUT){
									SdtpFileGroupingSourceOutputTools.output(xdr);

								}else{
									SdtpFileSourceOutputTools.output(xdr);
									
								}
							}
						}

						if (IS_OUTPUT_EXAMINE_XDR_2_FILE) {
							for (XdrSingleSource xdr : xdrs) {
								if (SdtpConfig.IS_USE_NEW_EXAMINE) {
									ExamineFileOutputTools.output(xdr);
								} else {

									// ExamineXdrFileOutput.output(new
									// IOutputSingleAdapter(xdr));
								}
							}
						}
						List<XdrCompoundSource> cxdrs = appMsg.getCxdrs();
						if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_FILE) {
							for (XdrCompoundSource cxdr : cxdrs) {
								CompXdrFileOutput.output(cxdr);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				coll.clear();
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
