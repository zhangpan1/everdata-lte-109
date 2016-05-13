package com.eversec.lte.sdtp.file;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.single.XdrSingleSource;

public class SdtpFileSourceOutputTools extends SdtpFileOutputTools {

	public String ScaDir() {
		return SdtpConfig.getSourceScaDir();
	}

	public String SigDir() {
		return SdtpConfig.getSourceSigDir();
	}

	public String S1uDir() {
		return SdtpConfig.getSourceS1uDir();
	}
	

	public static void output(XdrSingleSource xdr) {
		if(SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE){
			SdtpFileOutputTools.getInstance().outputXdr(xdr);
		}
	}

}
