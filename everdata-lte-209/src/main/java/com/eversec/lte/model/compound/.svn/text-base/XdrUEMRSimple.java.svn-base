package com.eversec.lte.model.compound;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedTBCDBytes;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.model.XdrData;

public class XdrUEMRSimple extends XdrData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -591361526670029607L;
	private String imsi;// 8
	private double longitude; // 8
	private double latitude;// 8
	private long time;// 8

	public XdrUEMRSimple(String imsi, double longitude, double latitude,
			long time) {
		this.imsi = imsi;
		this.longitude = longitude;
		this.latitude = latitude;
		this.time = time;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String[] toStringArr() {
		return new String[] { toString() };
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

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.put(getFixedTBCDBytes(imsi, 8, F));
		buffer.putDouble(longitude); // 8
		buffer.putDouble(latitude);// 8
		buffer.putLong(time);// 8
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 8 + 8 + 8 + 8;
	}

}
