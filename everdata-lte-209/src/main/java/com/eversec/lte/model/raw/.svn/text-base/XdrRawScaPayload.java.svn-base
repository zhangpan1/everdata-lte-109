package com.eversec.lte.model.raw;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

/**
 * 软件采集包头，区分硬采集
 * 
 * @author lirongzhi
 * 
 */
public class XdrRawScaPayload extends XdrRawPayload {
	int len;// 2
	// head1部分
	short ver;// 1
	short cardType;// 1
	long time;// 4
	long time2;// 4
	short netElement;// 1
	short Interface;// 1
	short direction;// 1
	// head2部分 +load部分:

	byte[] load;

	public XdrRawScaPayload(int len, short ver, short cardType, long time,
			long time2, short netElement, short interface1, short direction,
			byte[] load) {
		this.len = len;
		this.ver = ver;
		this.cardType = cardType;
		this.time = time;
		this.time2 = time2;
		this.netElement = netElement;
		Interface = interface1;
		this.direction = direction;
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

	public short getCardType() {
		return cardType;
	}

	public void setCardType(short cardType) {
		this.cardType = cardType;
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

	public short getNetElement() {
		return netElement;
	}

	public void setNetElement(short netElement) {
		this.netElement = netElement;
	}

	public short getInterface() {
		return Interface;
	}

	public void setInterface(short interface1) {
		Interface = interface1;
	}

	public short getDirection() {
		return direction;
	}

	public void setDirection(short direction) {
		this.direction = direction;
	}

	public byte[] getLoad() {
		return load;
	}

	public void setLoad(byte[] load) {
		this.load = load;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buffer = IoBuffer.allocate(len+2);

		buffer.putUnsignedShort(len);// 2
		// head1部分
		buffer.putUnsigned(ver);// 1
		buffer.putUnsigned(cardType);// 1
		buffer.putUnsignedInt(time);// 4
		buffer.putUnsignedInt(time2);// 4
		buffer.putUnsigned(netElement);// 1
		buffer.putUnsigned(Interface);// 1
		buffer.putUnsigned(direction);// 1
		// head2部分 +load部分:
		buffer.put(load);
		buffer.flip();
		return buffer;
	}

	@Override
	public int getTotalLength() {
		return len+2;
	}
}
