package com.eversec.lte.sdtp.file;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.single.XdrSingleSource;

public class SdtpFileFillOutputTools extends SdtpFileOutputTools {
	public String ScaDir() {
		return SdtpConfig.getFilledScaDir();
	}

	public String SigDir() {
		return SdtpConfig.getFilledSigDir();
	}

	public String S1uDir() {
		return SdtpConfig.getFilledS1uDir();
	}
	

	public static void output(XdrSingleSource xdr) {
		if(SdtpConfig.IS_OUTPUT_FILLED_XDR_2_FILE){
			SdtpFileOutputTools.getInstance().outputXdr(xdr);
		}

	}
}
