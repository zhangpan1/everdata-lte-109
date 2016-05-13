package com.eversec.lte.sdtp.model;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

import static com.eversec.lte.utils.FormatUtils.getFixedBytes;

public class LinkAuthReq extends SdtpRequest {
	String loginID;// 12;
	String digest;// 64;
	long timestamp;// 4;
	int rand;// 2;

	@Override
	public int getBodyLength() {
		return 82;
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
		buffer.put(getFixedBytes(loginID, 12));
		buffer.put(Hex.decodeHex(digest.toCharArray()));
		buffer.putUnsignedInt(timestamp);
		buffer.putUnsignedShort(rand);
		buffer.flip();
		return buffer;
	}
	
	public LinkAuthReq(String loginID, String digest, long timestamp, int rand) {
		super();
		this.loginID = loginID;
		this.digest = digest;
		this.timestamp = timestamp;
		this.rand = rand;
	}

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getRand() {
		return rand;
	}

	public void setRand(int rand) {
		this.rand = rand;
	}

}
