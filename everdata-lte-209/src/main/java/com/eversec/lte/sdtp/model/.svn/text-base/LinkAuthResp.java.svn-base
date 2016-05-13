package com.eversec.lte.sdtp.model;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

public class LinkAuthResp extends SdtpResponse {
	byte result;// 1;
	String digest;// 64;
	
	public LinkAuthResp(byte result, String digest) {
		super();
		this.result = result;
		this.digest = digest;
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
		buffer.put(result);
		buffer.put(Hex.decodeHex(digest.toCharArray()));
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 65;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

}
