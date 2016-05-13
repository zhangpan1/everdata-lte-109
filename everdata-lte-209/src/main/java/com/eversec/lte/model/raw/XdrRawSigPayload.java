package com.eversec.lte.model.raw;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

public class XdrRawSigPayload extends XdrRawPayload {
	int len;// 2
	// head1部分
	short ver;// 1
	short linkType;// 1
	short cardType;// 1
	short cardID;// 1
	long time;// 4
	long time2;// 4
	// head2部分 : IP无专用头
	byte[] load;

	public XdrRawSigPayload(int len, short ver, short linkType, short cardType,
			short cardID, long time, long time2, byte[] load) {
		super();
		this.len = len;
		this.ver = ver;
		this.linkType = linkType;
		this.cardType = cardType;
		this.cardID = cardID;
		this.time = time;
		this.time2 = time2;
		this.load = load;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public short getVer() {
		return ver;
	}

	public void setVer(short ver) {
		this.ver = ver;
	}

	public short getLinkType() {
		return linkType;
	}

	public void setLinkType(short linkType) {
		this.linkType = linkType;
	}

	public short getCardType() {
		return cardType;
	}

	public void setCardType(short cardType) {
		this.cardType = cardType;
	}

	public short getCardID() {
		return cardID;
	}

	public void setCardID(short cardID) {
		this.cardID = cardID;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime2() {
		return time2;
	}

	public void setTime2(long time2) {
		this.time2 = time2;
	}

	public byte[] getLoad() {
		return load;
	}

	public void setLoad(byte[] load) {
		this.load = load;
	}

	@Override
	public String toString() {
		return "XdrRawPayload [len=" + len + ", ver=" + ver + ", linkType="
				+ linkType + ", cardType=" + cardType + ", cardID=" + cardID
				+ ", time=" + time + ", time2=" + time2 + ", load="
				+ Hex.encodeHexString(load) + "]";
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buffer = IoBuffer.allocate(len + 2);
		buffer.putUnsignedShort(len);
		buffer.putUnsigned(ver);// 1
		buffer.putUnsigned(linkType);// 1
		buffer.putUnsigned(cardType);// 1
		buffer.putUnsigned(cardID);// 1
		buffer.putUnsignedInt(time);// 4
		buffer.putUnsignedInt(time2);// 4
		buffer.put(load);
		buffer.flip();
		return buffer;
	}

	@Override
	public int getTotalLength() {
		return len + 2;
	}
}
