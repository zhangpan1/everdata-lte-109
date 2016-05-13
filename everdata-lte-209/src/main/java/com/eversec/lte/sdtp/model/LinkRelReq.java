package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

/**
 * 
 * @author bieremayi
 * 
 */
public class LinkRelReq extends SdtpRequest {
	/*
	 * 连接释放的原因，各个值代表意义如下： 1: 用户正常释放。 2: 数据类型错误。 3: 超出机器处理能力。
	 */
	byte reason;// 1;
	
	public LinkRelReq(byte reason) {
		super();
		this.reason = reason;
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
		buffer.put(reason);
		buffer.flip();
		return buffer;
	}
	
	public byte getReason() {
		return reason;
	}

	public void setReason(byte reason) {
		this.reason = reason;
	}

	@Override
	public int getBodyLength() {
		return 1;
	}
}
