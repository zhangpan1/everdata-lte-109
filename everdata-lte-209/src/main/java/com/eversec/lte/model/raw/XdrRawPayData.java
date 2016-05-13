package com.eversec.lte.model.raw;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.model.XdrData;
import com.eversec.lte.utils.FormatUtils;

@SuppressWarnings("serial")
public class XdrRawPayData extends XdrData {
	short rat;// 1
	short Interface;// 1
	byte[] xdrID;// 16
	int num;// 2
	long lengthTotal;// 4
	List<XdrRawPayload> payloads = new ArrayList<XdrRawPayload>();

	public XdrRawPayData(short rat, short interface1, byte[] xdrID, int num,
			long lengthTotal, List<XdrRawPayload> payloads) {
		super();
		this.rat = rat;
		this.Interface = interface1;
		this.xdrID = xdrID;
		this.num = num;
		this.lengthTotal = lengthTotal;
		this.payloads = payloads;
	}

	public short getRat() {
		return rat;
	}

	public void setRat(short rat) {
		this.rat = rat;
	}

	public short getInterface() {
		return Interface;
	}

	public void setInterface(short interface1) {
		Interface = interface1;
	}

	public byte[] getXdrID() {
		return xdrID;
	}

	public void setXdrID(byte[] xdrID) {
		this.xdrID = xdrID;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getLengthTotal() {
		return lengthTotal;
	}

	public void setLengthTotal(long lengthTotal) {
		this.lengthTotal = lengthTotal;
	}

	public List<XdrRawPayload> getPayloads() {
		return payloads;
	}

	public void setPayloads(List<XdrRawPayload> payloads) {
		this.payloads = payloads;
	}

	@Override
	public String toString() {
		return "XdrRawPayData [rat=" + rat + ", Interface=" + Interface
				+ ", xdrID=" + Hex.encodeHexString(xdrID) + ", num=" + num
				+ ", lengthTotal=" + lengthTotal + ", payloads="
				+ FormatUtils.listToString(payloads) + "]";
	}

	@Override
	public boolean isXdrSingle() {
		return true;
	}

	@Override
	public String[] toStringArr() {
		return new String[] { toString() };
	}

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(rat);
		buffer.putUnsigned(Interface);
		buffer.put(xdrID);
		buffer.putUnsignedShort(num);
		// long lengthTotal = buffer.getUnsignedInt();207
		buffer.putUnsignedShort(lengthTotal);// 209
		for (XdrRawPayload payload : payloads) {
			buffer.put(payload.toBuffer());
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return (int) (1 + 1 + 16 + 2 + 2 + lengthTotal);
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

}
