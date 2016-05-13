package com.eversec.lte.processor.compound;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.utils.FormatUtils;

/**
 * 
 * @author bieremayi
 * 
 */
public class XdrIDGenerater {

	private static AtomicLong SEQ;

	static {
		SEQ = new AtomicLong(SdtpConfig.getSeqInitialValue());
	}

	public static byte[] getXdrID() {
		byte[] rets = new byte[16];
		byte[] timeBytes = FormatUtils.longToBytes(System.currentTimeMillis());
		byte[] seqBytes = FormatUtils.longToBytes(SEQ.incrementAndGet());
		System.arraycopy(timeBytes, 0, rets, 0, 8);
		System.arraycopy(seqBytes, 0, rets, 8, 8);
		return rets;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(XdrIDGenerater.getXdrID()));
	}

}
