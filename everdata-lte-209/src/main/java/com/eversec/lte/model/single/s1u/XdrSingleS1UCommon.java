package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedTBCDBytes;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.model.XdrData;

/**
 * 公共信息
 * 
 * @author bieremayi
 * 
 */
@SuppressWarnings("serial")
public class XdrSingleS1UCommon extends XdrData {

	int length;// 2
	String city;// 2;TBCD
	short Interface;// 1
	byte[] xdrId;// 16

	public XdrSingleS1UCommon(int length, String city, short Interface,
			byte[] xdrId) {
		super();
		this.length = length;
		this.city = city;
		this.Interface = Interface;
		this.xdrId = xdrId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public short getInterface() {
		return Interface;
	}

	public void setInterface(short interface1) {
		Interface = interface1;
	}

	public byte[] getXdrId() {
		return xdrId;
	}

	public void setXdrId(byte[] xdrId) {
		this.xdrId = xdrId;
	}

	@Override
	public boolean isXdrSingle() {
		return true;
	}

	@Override
	public String toString() {
		return length + "|" + city + "|" + Interface + "|"
				+ Hex.encodeHexString(xdrId);
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
		buffer.putUnsignedShort(length);// 2
		buffer.put(getFixedTBCDBytes(city, 2, F));// 2
		buffer.putUnsigned(Interface);// 1
		buffer.put(xdrId);// 16
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 2 + 2 + 1 + 16;
	}

	@Override
	public int getMemoryBytes() {
		// TODO Auto-generated method stub
		return 0;
	}
}
