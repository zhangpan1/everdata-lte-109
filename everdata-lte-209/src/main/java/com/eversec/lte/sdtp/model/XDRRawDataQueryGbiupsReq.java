package com.eversec.lte.sdtp.model;

import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

/**
 * 扩展
 * @author lirongzhi
 *
 */
public class XDRRawDataQueryGbiupsReq extends XDRRawDataQueryReq {
	Date endTime;// 8

	public XDRRawDataQueryGbiupsReq(short interface1, byte[] xdrID,
			Date startTime, Date endTime) {
		super(interface1, xdrID, startTime);
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}

	@Override
	public IoBuffer toIoBuffer() {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.putUnsigned(Interface);
		buffer.put(xdrID);
		buffer.putLong(startTime.getTime());
		buffer.putLong(endTime.getTime());
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 8 + 8 + 8;
	}

}
