package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

/**
 * 
 * @author bieremayi
 * 
 */
public class LinkDataCheckReq extends SdtpRequest {
	long sendflag;// 4;
	long sendDataInfo;// 4;

	@Override
	public byte[] toByteArray() throws Exception {
		return toIoBuffer().array();
	}

	@Override
	public IoBuffer toIoBuffer() throws Exception {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH
				+ getBodyLength());
		buffer.put(SdtpHeader.toByteArray(header));
		buffer.putUnsignedInt(sendflag);
		buffer.putUnsignedInt(sendDataInfo);
		buffer.flip();
		return buffer;
	}
	
	public LinkDataCheckReq(long sendflag, long sendDataInfo) {
		super();
		this.sendflag = sendflag;
		this.sendDataInfo = sendDataInfo;
	}

	public long getSendflag() {
		return sendflag;
	}

	public void setSendflag(long sendflag) {
		this.sendflag = sendflag;
	}

	public long getSendDataInfo() {
		return sendDataInfo;
	}

	public void setSendDataInfo(long sendDataInfo) {
		this.sendDataInfo = sendDataInfo;
	}

	@Override
	public int getBodyLength() {
		return 8;
	}

}
