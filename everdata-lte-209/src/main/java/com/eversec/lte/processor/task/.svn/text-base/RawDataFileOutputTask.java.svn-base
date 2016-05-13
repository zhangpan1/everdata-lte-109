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
import com.eversec.lte.processor.statistics.RawStat;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;

/**
 * 
 * @author bieremayi
 * 
 */
public class RawDataFileOutputTask extends
		AbstractOutputTask<XDRRawDataSendReq> {

	public RawDataFileOutputTask(ArrayBlockingQueue<XDRRawDataSendReq> queue) {
		super(queue);
	}

	@Override
	public void run() {
		RawBytesDecoder decoder = new RawBytesDecoder();
		while (true) {
			try {
				Collection<XDRRawDataSendReq> coll = new ArrayList<>();
				int count = queue.drainTo(coll, drainMaxElements);
				if (count > 0) {
					for (XDRRawDataSendReq rawData : coll) {
						List<XdrRawPayData> payDatas = decoder.decode(
								rawData.getLoad(), false);
						for (XdrRawPayData payData : payDatas) {
							short Interface = payData.getInterface();
							byte[] xdrId = payData.getXdrID();
							List<XdrRawPayload> payloads = payData
									.getPayloads();
							StringBuilder sb = new StringBuilder();
							for (XdrRawPayload payload : payloads) {
								sb.append(payload.getTime());
								sb.append(",");
							}
							if (sb.length() > 0) {
								sb.deleteCharAt(sb.length() - 1);
							}
							OriginalRawFileOutput.output(Interface + "",
									Hex.encodeHexString(xdrId), sb.toString());
							RawStat.statRaw(xdrId);
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
