package com.eversec.lte.processor.task;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.lang3.StringUtils;

import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.output.CompXdrFileOutput;
import com.eversec.lte.processor.compound.UemrXdrCompound;
import com.eversec.lte.sdtp.tokafka.SdtpToKafkaOutputTools;
import com.eversec.lte.sdtp.tosdtp.SdtpToSdtpOutputTools;

/**
 * 合成uemr，并输出
 * 
 * @author bieremayi
 * 
 */
public class UemrComponudOutputTask extends
		AbstractOutputTask<XdrSingleSourceUEMR> {

	public UemrComponudOutputTask(ArrayBlockingQueue<XdrSingleSourceUEMR> queue) {
		super(queue);
	}

	@Override
	public void run() {
		while (true) {
			try {
				XdrSingleSourceUEMR uemr = queue.take();
				if (StringUtils.isNotBlank(uemr.getCommon().getImsi())) {
					XdrCompoundSourceUEMR compound = UemrXdrCompound
							.compoundUEMR(uemr);
					CompXdrFileOutput.output(compound);
//					CompXdrSdtpOutput.output(compound);
					SdtpToSdtpOutputTools.output(compound);
//					CustomXdrKafkaOutput.output(compound);
					SdtpToKafkaOutputTools.output(compound);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
