package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

public class NotifyCDRorTDRDataResp extends SdtpResponse {
	/*
	 * 请求的返回结果： 1 代表成功 其它 代表失败
	 */
	byte result;// 1;

	@Override
	public byte[] toByteArray() throws Exception {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.put(result);
		buffer.flip();
		return buffer.array();
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
