package com.eversec.lte.processor.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Hex;

import com.eversec.lte.model.raw.XdrRawPayData;
import com.eversec.lte.model.raw.XdrRawPayload;
import com.eversec.lte.output.OriginalRawFileOutput;
import com.eversec.lte.processor.decoder.RawBytesDecoder;
import com.eversec.lte.processor.decoder.RawGbiupsBytesDecoder;
import com.eversec.lte.processor.statistics.RawStat;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;

/**
 * 
 * 
 * 
 */
public class RawGbiupsDataFileOutputTask extends
		AbstractOutputTask<XDRRawDataSendReq> {

	public RawGbiupsDataFileOutputTask(ArrayBlockingQueue<XDRRawDataSendReq> queue) {
		super(queue);
	}

	@Override
	public void run() {
		RawBytesDecoder decoder = new RawGbiupsBytesDecoder();
		while (true) {
			try {
				Collection<XDRRawDataSendReq> coll = new ArrayList<>();
				int count = queue.drainTo(coll, drainMaxElements);
				if (count > 0) {
					for (XDRRawDataSendReq rawData : coll) {
						List<XdrRawPayData> payDatas = decoder.decode(
								rawData.getLoad(), false);
						for (XdrRawPayData payData : payDatas) {
							OriginalRawFileOutput.output(payData.toString());
						}

					}

				} else {
					try {
						TimeUnit.MILLISECONDS.sleep(drainTaskSleepMills);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
