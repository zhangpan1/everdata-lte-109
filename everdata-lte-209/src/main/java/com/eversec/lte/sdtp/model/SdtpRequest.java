package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * @author bieremayi
 * 
 */
public abstract class SdtpRequest {
	
	protected SdtpHeader header;

	public abstract byte[] toByteArray() throws Exception;
	
	public abstract IoBuffer toIoBuffer() throws Exception;
	
	public abstract int getBodyLength();
	
	protected IoSession session;
	
	protected long systemMillis;
	
	
	public SdtpHeader getHeader() {
		return header;
	}

	public void setHeader(SdtpHeader header) {
		this.header = header;
	}
	
	public IoSession getSession() {
		return session;
	}
	
	public void setSession(IoSession session) {
		this.session = session;
	}
	
	public long getSystemMillis() {
		return systemMillis;
	}
	
	public void setSystemMillis(long systemMillis) {
		this.systemMillis = systemMillis;
	}

}
