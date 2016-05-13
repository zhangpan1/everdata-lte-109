package com.eversec.lte.sdtp.model;


import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;

/**
 * 
 * @author bieremayi
 * 
 */
public class VerNegoReq extends SdtpRequest {
	byte version;// 1;协议的主版本号
	byte subVersion;// 1;协议的子版本号
	
	@Override
	public int getBodyLength() {
		return 2;
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
		buffer.put(version);
		buffer.put(subVersion);
		buffer.flip();
		return buffer;
	}

	public VerNegoReq(byte version, byte subVersion) {
		super();
		this.version = version;
		this.subVersion = subVersion;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getSubVersion() {
		return subVersion;
	}

	public void setSubVersion(byte subVersion) {
		this.subVersion = subVersion;
	}

}
