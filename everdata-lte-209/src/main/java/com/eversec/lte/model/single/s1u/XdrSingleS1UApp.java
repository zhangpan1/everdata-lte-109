package com.eversec.lte.model.single.s1u;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.XdrData;

public abstract class XdrSingleS1UApp extends XdrData {
	
	public static String _S1U_DELIMITER_ =  SdtpConfig.getS1uAppDelimiter();
	/**
	 * 
	 */
	private static final long serialVersionUID = 6378373153537290256L;

	public  byte[] toByteArray(){
		return toIobuffer().array();
	}
	
	@Override
	public boolean isXdrSingle() {
		return false;
	}
	
	@Override
	public String[] toStringArr() {
		return new String[]{toString()};
	}

	public abstract IoBuffer toIobuffer();

	public abstract int getBodyLength();
}
