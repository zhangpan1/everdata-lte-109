package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

public class NotifyXDRDataReq extends SdtpRequest {
	byte[] load;
	
	int xdrType = -1;

	@Override
	public byte[] toByteArray() throws Exception {
		return toIoBuffer().array();
	}

	@Override
	public IoBuffer toIoBuffer() throws Exception {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.put(load);
		buffer.flip();
		return buffer;
	}

	public NotifyXDRDataReq(byte[] load) {
		super();
		this.load = load;
	}

	public byte[] getLoad() {
		return load;
	}

	public void setLoad(byte[] load) {
		this.load = load;
	}

	public int getXdrType() {
		return xdrType;
	}

	public void setXdrType(int xdrType) {
		this.xdrType = xdrType;
	}

	@Override
	public int getBodyLength() {
		return load.length + (xdrType > 0 ? 1 : 0);
	}

}
