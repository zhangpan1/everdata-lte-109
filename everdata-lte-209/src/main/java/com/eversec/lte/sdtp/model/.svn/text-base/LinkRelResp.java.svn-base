package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

/**
 * 
 * @author bieremayi
 * 
 */
public class LinkRelResp extends SdtpResponse {
	/*
	 * 连接释放的完成状态 1：释放完成。 其它：释放失败。
	 */
	byte result;// 1;
	
	public LinkRelResp(byte result) {
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
