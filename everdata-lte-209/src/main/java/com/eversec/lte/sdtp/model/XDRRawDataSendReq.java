package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

public class XDRRawDataSendReq extends SdtpRequest {
	byte[] load;
	
	@Override
	public byte[] toByteArray(){
		return toIoBuffer().array();
	}

	@Override
	public IoBuffer toIoBuffer() {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.put(load);
		buffer.flip();
		return buffer;
	}

	public XDRRawDataSendReq(byte[] load) {
		super();
		this.load = load;
	}

	public byte[] getLoad() {
		return load;
	}

	public void setLoad(byte[] load) {
		this.load = load;
	}

	@Override
	public int getBodyLength() {
		return load.length;
	}

}
