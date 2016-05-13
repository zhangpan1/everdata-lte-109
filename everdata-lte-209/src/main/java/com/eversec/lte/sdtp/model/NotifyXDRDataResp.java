package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

public class NotifyXDRDataResp extends SdtpResponse {
	/*
	 * 请求的返回结果： 1 代表成功 其它 代表失败
	 */
	byte result;// 1;
	
	public NotifyXDRDataResp(byte result) {
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
