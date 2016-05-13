package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 
 * @author bieremayi
 * 
 */
public abstract class SdtpResponse {

	protected SdtpHeader header;

	public abstract byte[] toByteArray() throws Exception;
	
	public abstract IoBuffer toIoBuffer() throws Exception;

	public abstract int getBodyLength();

	public SdtpHeader getHeader() {
		return header;
	}

	public void setHeader(SdtpHeader header) {
		this.header = header;
	}

}
