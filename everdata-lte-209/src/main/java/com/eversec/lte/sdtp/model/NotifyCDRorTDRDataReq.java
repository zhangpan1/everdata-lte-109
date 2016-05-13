package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

public class NotifyCDRorTDRDataReq extends SdtpRequest {
	int CDRType;// 2;
	long CDRID;// 8;
	byte[] load;
	
	@Override
	public byte[] toByteArray() throws Exception {
		return toIoBuffer().array();
	}

	@Override
	public IoBuffer toIoBuffer() throws Exception {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.putUnsignedShort(CDRType);
		buffer.putLong(CDRID);
		buffer.put(load);
		buffer.flip();
		return buffer;
	}

	public NotifyCDRorTDRDataReq(int CDRType, long CDRID, byte[] load) {
		super();
		this.CDRType = CDRType;
		this.CDRID = CDRID;
		this.load = load;
	}

	public int getCDRType() {
		return CDRType;
	}

	public void setCDRType(int cDRType) {
		CDRType = cDRType;
	}

	public long getCDRID() {
		return CDRID;
	}

	public void setCDRID(long cDRID) {
		CDRID = cDRID;
	}

	public byte[] getLoad() {
		return load;
	}

	public void setLoad(byte[] load) {
		this.load = load;
	}

	@Override
	public int getBodyLength() {
		return 10+load.length;
	}

}
