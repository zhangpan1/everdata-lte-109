package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

public class XdrSingleS1UAppP2P extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5767502993884925474L;
	long size; // 4 文件大小
	byte[] p2pId;// 16 P2P标示
	String tracker;// 128 Tracker

	public XdrSingleS1UAppP2P(long size, byte[] p2pId, String tracker) {
		super();
		this.size = size;
		this.p2pId = p2pId;
		this.tracker = tracker;
	}

	@Override
	public String toString() {
		return size + _S1U_DELIMITER_ + Hex.encodeHexString(p2pId) + _S1U_DELIMITER_ + tracker;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsignedInt(size);
		buffer.put(p2pId);//16
		setString(buffer, tracker, 128);
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 4 + 16 + 128;
	}

}
