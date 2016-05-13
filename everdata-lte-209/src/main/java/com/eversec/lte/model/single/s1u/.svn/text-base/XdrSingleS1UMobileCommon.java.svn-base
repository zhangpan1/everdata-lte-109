package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedBytes;
import static com.eversec.lte.utils.FormatUtils.getFixedTBCDBytes;
import static com.eversec.lte.utils.FormatUtils.getIp;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.model.XdrData;
import com.eversec.lte.utils.FormatUtils;

/**
 * 移动网通用信息
 * 
 * @author bieremayi
 * 
 */
@SuppressWarnings("serial")
public class XdrSingleS1UMobileCommon extends XdrData {

	short rat;// 1
	String imsi;// 8;TBCD
	String imei;// 8;TBCD
	String msisdn;// 16;TBCD
	short machineIpAddType;// 1
	byte[] sgwOrGgsnIpAdd;// 16
	byte[] enbOrSgsnIpAdd;// 16
	int sgwOrGgsnPort;// 2
	int enbOrSgsnPort;// 2
	long enbOrSgsnGtpTeid;// 4
	long sgwOrGgsnGtpTeid;// 4
	int tac;// 2
	long cellId;// 4
	String apn;// 32

	public XdrSingleS1UMobileCommon(short rat, String imsi, String imei,
			String msisdn, short machineIpAddType, byte[] sgwOrGgsnIpAdd,
			byte[] enbOrSgsnIpAdd, int sgwOrGgsnPort, int enbOrSgsnPort,
			long enbOrSgsnGtpTeid, long sgwOrGgsnGtpTeid, int tac, long cellId,
			String apn) {
		super();
		this.rat = rat;
		this.imsi = imsi;
		this.imei = imei;
		this.msisdn = msisdn;
		this.machineIpAddType = machineIpAddType;
		this.sgwOrGgsnIpAdd = sgwOrGgsnIpAdd;
		this.enbOrSgsnIpAdd = enbOrSgsnIpAdd;
		this.sgwOrGgsnPort = sgwOrGgsnPort;
		this.enbOrSgsnPort = enbOrSgsnPort;
		this.enbOrSgsnGtpTeid = enbOrSgsnGtpTeid;
		this.sgwOrGgsnGtpTeid = sgwOrGgsnGtpTeid;
		this.tac = tac;
		this.cellId = cellId;
		this.apn = apn;
	}

	@Override
	public String toString() {
		return rat + "|" + imsi + "|" + imei + "|" + msisdn + "|"
				+ machineIpAddType + "|" + FormatUtils.getIp(sgwOrGgsnIpAdd) + "|"
				+  FormatUtils.getIp(enbOrSgsnIpAdd) + "|" + sgwOrGgsnPort + "|" + enbOrSgsnPort
				+ "|" + enbOrSgsnGtpTeid + "|" + sgwOrGgsnGtpTeid + "|" + tac
				+ "|" + cellId + "|" + apn;
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

	public short getMachineIpAddType() {
		return machineIpAddType;
	}

	public void setMachineIpAddType(short machineIpAddType) {
		this.machineIpAddType = machineIpAddType;
	}

	public byte[] getSgwOrGgsnIpAdd() {
		return sgwOrGgsnIpAdd;
	}

	public void setSgwOrGgsnIpAdd(byte[] sgwOrGgsnIpAdd) {
		this.sgwOrGgsnIpAdd = sgwOrGgsnIpAdd;
	}

	public byte[] getEnbOrSgsnIpAdd() {
		return enbOrSgsnIpAdd;
	}

	public void setEnbOrSgsnIpAdd(byte[] enbOrSgsnIpAdd) {
		this.enbOrSgsnIpAdd = enbOrSgsnIpAdd;
	}

	public int getSgwOrGgsnPort() {
		return sgwOrGgsnPort;
	}

	public void setSgwOrGgsnPort(int sgwOrGgsnPort) {
		this.sgwOrGgsnPort = sgwOrGgsnPort;
	}

	public int getEnbOrSgsnPort() {
		return enbOrSgsnPort;
	}

	public void setEnbOrSgsnPort(int enbOrSgsnPort) {
		this.enbOrSgsnPort = enbOrSgsnPort;
	}

	public long getEnbOrSgsnGtpTeid() {
		return enbOrSgsnGtpTeid;
	}

	public void setEnbOrSgsnGtpTeid(long enbOrSgsnGtpTeid) {
		this.enbOrSgsnGtpTeid = enbOrSgsnGtpTeid;
	}

	public long getSgwOrGgsnGtpTeid() {
		return sgwOrGgsnGtpTeid;
	}

	public void setSgwOrGgsnGtpTeid(long sgwOrGgsnGtpTeid) {
		this.sgwOrGgsnGtpTeid = sgwOrGgsnGtpTeid;
	}

	public int getTac() {
		return tac;
	}

	public void setTac(int tac) {
		this.tac = tac;
	}

	public long getCellId() {
		return cellId;
	}

	public void setCellId(long cellId) {
		this.cellId = cellId;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	@Override
	public String[] toStringArr() {
		return new String[] { toString() };
	}

	@Override
	public boolean isXdrSingle() {
		return true;
	}

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(rat);// 1
		buffer.put(getFixedTBCDBytes(imsi, 8, F)); // 8
		buffer.put(getFixedTBCDBytes(imei, 8, F)); // 8
		buffer.put(getFixedTBCDBytes(msisdn, 16, F)); // 16
		buffer.putUnsigned(machineIpAddType); // 1
		buffer.put(sgwOrGgsnIpAdd);//getIp(sgwOrGgsnIpAdd, 16));// 16
		buffer.put(enbOrSgsnIpAdd);//getIp(enbOrSgsnIpAdd, 16));// 16
		buffer.putUnsignedShort(sgwOrGgsnPort);// 2
		buffer.putUnsignedShort(enbOrSgsnPort);// 2
		buffer.putUnsignedInt(enbOrSgsnGtpTeid);// 4
		buffer.putUnsignedInt(sgwOrGgsnGtpTeid);// 4
		buffer.putUnsignedShort(tac);// 2
		buffer.putUnsignedInt(cellId);// 4
		buffer.put(getFixedBytes(apn, 32, F));// 32
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 8 + 8 + 16 + 1 + 16 + 16 + 2 + 2 + 4 + 4 + 2 + 4 + 32;
	}

	@Override
	public int getMemoryBytes() {
		// TODO Auto-generated method stub
		return 0;
	}
}
