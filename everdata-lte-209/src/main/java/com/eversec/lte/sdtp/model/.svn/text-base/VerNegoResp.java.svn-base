package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

/**
 * 
 * @author bieremayi
 * 
 */
public class VerNegoResp extends SdtpResponse {
	/*
	 * 
	 * 返回原因值。 协议协商的结果，各个值代表意义如下： 1: 版本协商通过。 2: 版本过高。 3: 版本过低。
	 */
	byte result;// 1;
	
	public VerNegoResp(byte result) {
		super();
		this.result = result;
	}

	@Override
	public byte[] toByteArray() throws Exception {
		return toIoBuffer().array();
	}
	
	@Override
	public IoBuffer toIoBuffer() throws Exception {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.put(result);
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	
	
	
}
