package com.eversec.lte.model.compound;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedTBCDBytes;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 合成XDR公共信息
 * 
 */
@SuppressWarnings("serial")
public class XdrCompoundCommon extends XdrCompoundSource {
	int length; // 2
	String city; // 2
	short rat; // 1
	short xdrType; // 1
	byte[] xdrId = new byte[16]; // 16
	String imsi;// 8
	String imei;// 8
	String msisdn;// 16

	public XdrCompoundCommon() {
	}

	public XdrCompoundCommon(int length, String city, short rat, short xdrType,
			byte[] xdrId, String imsi, String imei, String msisdn) {
		super();
		this.length = length;
		this.city = city;
		this.rat = rat;
		this.xdrType = xdrType;
		this.xdrId = xdrId;
		this.imsi = imsi;
		this.imei = imei;
		this.msisdn = msisdn;
	}

	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsignedShort(length);// 2
		buffer.put(getFixedTBCDBytes(city, 2, F));// 2
		buffer.put((byte) rat);// 1
		buffer.put((byte) xdrType);// 1
		buffer.put(xdrId);// 16
		buffer.put(getFixedTBCDBytes(imsi, 8, F));// 8
		buffer.put(getFixedTBCDBytes(imei, 8, F));// 8
		buffer.put(getFixedTBCDBytes(msisdn, 16, F));// 16
		buffer.flip();
		return buffer;
	}

	@Override
	public String toString() {
		return length + "|" + city + "|" + rat + "|" + xdrType + "|"
				+ Hex.encodeHexString(xdrId) + "|" + imsi + "|" + imei + "|"
				+ msisdn;
	}

	@Override
	public int getBodyLength() {
		return 2 + 2 + 1 + 1 + 16 + 8 + 8 + 16;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public boolean isXdrSingle() {
		return false;
	}

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
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

	public short getRat() {
		return rat;
	}

	public void setRat(short rat) {
		this.rat = rat;
	}

	public int getXdrType() {
		return xdrType;
	}

	public void setXdrType(short xdrType) {
		this.xdrType = xdrType;
	}

	public byte[] getXdrId() {
		return xdrId;
	}

	public void setXdrId(byte[] xdrId) {
		this.xdrId = xdrId;
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

}
