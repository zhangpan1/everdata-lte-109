package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedTBCDBytes;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.model.XdrData;

@SuppressWarnings("serial")
public class XdrSingleCommon extends XdrData {
	int length;// 2
	String city;// 2;TBCD
	short Interface;// 1
	byte[] xdrId;// 16
	short rat;// 1
	String imsi;// 8;TBCD
	String imei;// 8;TBCD
	String msisdn;// 16;TBCD

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
		buffer.putUnsigned(rat);// 1
		buffer.put(getFixedTBCDBytes(imsi, 8, F));// 8
		buffer.put(getFixedTBCDBytes(imei, 8, F));// 8
		buffer.put(getFixedTBCDBytes(msisdn, 16, F));// 16
		buffer.flip();
		return buffer;
	}

	public XdrSingleCommon() {
	}

	public XdrSingleCommon(int length, String city, short Interface,
			byte[] xdrId, short rat, String imsi, String imei, String msisdn) {
		super();
		this.length = length;
		this.city = city;
		this.Interface = Interface;
		this.xdrId = xdrId;
		this.rat = rat;
		this.imsi = imsi;
		this.imei = imei;
		this.msisdn = msisdn;
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

	public short getRat() {
		return rat;
	}

	public void setRat(short rat) {
		this.rat = rat;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public boolean isXdrSingle() {
		return true;
	}

	@Override
	public String toString() {
		return length + "|" + city + "|" + Interface + "|"
				+ Hex.encodeHexString(xdrId) + "|" + rat + "|" + imsi + "|"
				+ imei + "|" + msisdn;
	}

	@Override
	public String[] toStringArr() {
		return new String[] { toString() };
	}

	@Override
	public int getMemoryBytes() {
		return toString().getBytes().length;
	}

	@Override
	public int getBodyLength() {
		// 54
		return 2 + 2 + 1 + 16 + 1 + 8 + 8 + 16;
	}

}
