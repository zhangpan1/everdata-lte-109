package com.eversec.lte.sdtp.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

/**
 * 
 * @author bieremayi
 * 
 */
public class LinkDataCheckResp extends SdtpResponse {
	long sendflag;// 4;
	byte result;// 1;
	long sendDataInfo;// 4;
	long recDataInfo;// 4;
	
	public LinkDataCheckResp(long sendflag, byte result, long sendDataInfo,
			long recDataInfo) {
		super();
		this.sendflag = sendflag;
		this.result = result;
		this.sendDataInfo = sendDataInfo;
		this.recDataInfo = recDataInfo;
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
		buffer.putUnsignedInt(sendflag);
		buffer.put(result);
		buffer.putUnsignedInt(sendDataInfo);
		buffer.putUnsignedInt(recDataInfo);
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 13;
	}

	public long getSendflag() {
		return sendflag;
	}

	public void setSendflag(long sendflag) {
		this.sendflag = sendflag;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public long getSendDataInfo() {
		return sendDataInfo;
	}

	public void setSendDataInfo(long sendDataInfo) {
		this.sendDataInfo = sendDataInfo;
	}

	public long getRecDataInfo() {
		return recDataInfo;
	}

	public void setRecDataInfo(long recDataInfo) {
		this.recDataInfo = recDataInfo;
	}

	
	
	
}
