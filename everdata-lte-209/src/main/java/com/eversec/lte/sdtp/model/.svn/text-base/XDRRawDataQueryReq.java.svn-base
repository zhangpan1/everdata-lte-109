package com.eversec.lte.sdtp.model;

import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

public class XDRRawDataQueryReq extends SdtpRequest {
	short Interface;//1
	byte[] xdrID;//16
	Date startTime;//8

	public XDRRawDataQueryReq(short interface1, byte[] xdrID, Date startTime) {
		super();
		Interface = interface1;
		this.xdrID = xdrID;
		this.startTime = startTime;
	}

	@Override
	public byte[] toByteArray() {
		return toIoBuffer().array();
	}

	@Override
	public IoBuffer toIoBuffer() {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.putUnsigned(Interface);
		buffer.put( xdrID );
		buffer.putLong(startTime.getTime());
		buffer.flip();
		return buffer;
	}

	public short getInterface() {
		return Interface;
	}

	public void setInterface(short interface1) {
		Interface = interface1;
	}

	public byte[] getXdrID() {
		return xdrID;
	}

	public void setXdrID(byte[] xdrID) {
		this.xdrID = xdrID;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Override
	public int getBodyLength() {
		return 1 + 16 + 8;
	}

}
