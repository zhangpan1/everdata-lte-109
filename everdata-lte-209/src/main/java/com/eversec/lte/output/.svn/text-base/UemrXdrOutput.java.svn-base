package com.eversec.lte.output;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.processor.data.QueueData;

/**
 * 
 * @author bieremayi
 * 
 */
public class UemrXdrOutput {
	public static void output(XdrSingleSourceUEMR uemr) {
		if (SdtpConfig.IS_COMPOUND_UEMR) {
			try {
				QueueData.UEMR_QUEUE.put(uemr);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
