package com.eversec.lte.sdtp.model;

import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

import cern.colt.Arrays;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.utils.SdtpUtils;

@SuppressWarnings("serial")
public class SdtpHeader implements Serializable {

	int totalLength;// 2;消息总长度(含消息头及消息体)
	int messageType;// 2;消息类型
	long sequenceId;// 4;交互的流水号，顺序累加，步长为1，循环使用（一个交互的一对请求和应答消息的流水号必须相同）
	short totalContents;// 1;消息体中的事件数量（最多40条）若考虑实时性要求，可每次只填一个事件

	public SdtpHeader() {
	}

	public SdtpHeader(int totalLength, int messageType, long sequenceId,
			short totalContents) {
		this.totalLength = totalLength;
		this.messageType = messageType;
		this.sequenceId = sequenceId;
		this.totalContents = totalContents;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public long getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(long sequenceId) {
		this.sequenceId = sequenceId;
	}

	public short getTotalContents() {
		return totalContents;
	}

	public void setTotalContents(short totalContents) {
		this.totalContents = totalContents;
	}

	@Override
	public String toString() {
		return "SdtpHeader [toatlLength=" + totalLength + ", messageType="
				+ messageType + ", sequenceId=" + sequenceId
				+ ", totalContents=" + totalContents + " ]";
	}

	public static byte[] toByteArray(SdtpHeader header) {
		return toIoBuffer(header).array();
	}

	public static IoBuffer toIoBuffer(SdtpHeader header) {
		IoBuffer buffer = IoBuffer.allocate(SdtpConstants.SDTP_HEADER_LENGTH);
		buffer.putUnsignedShort(header.getTotalLength());
		buffer.putUnsignedShort(header.getMessageType());
		buffer.putUnsignedInt(header.getSequenceId());
		buffer.putUnsigned(header.getTotalContents());
		buffer.flip();
		return buffer;
	}
	
	public static void main(String[] args) {
		SdtpHeader header = new SdtpHeader(128000, 1, 1, (short)1);
		byte[] dst = toByteArray(header);
		System.out.println(Arrays.toString(dst));
		header = SdtpUtils.getHeader(IoBuffer.wrap(dst));
		System.out.println(header);
		
	}
}
