package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedBytes;
import static com.eversec.lte.utils.FormatUtils.getIp;

import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

@SuppressWarnings("serial")
public class XdrSingleSourceGnC extends XdrSingleSource {
	short procedureType;// 1
	short subProcedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short procedureStatus;// 1
	byte[] userIpv4;// 4
	byte[] userIpv6;// 16
	byte[] mmeAddress;// 16
	byte[] oldMmeAddress;// 16
	byte[] sgsnCIP;// 16
	byte[] sgsnUIP;// 16
	int mmePort;// 2
	int oldMmePort;// 2
	int sgsnCPort;// 2
	int sgsnUPort;// 2
	long sgsnControlTeid;// 4
	long sgsnDataTeid;// 4
	long oldMmeControlTeid;// 4
	long pTmsi;// 4
	int lac;// 2
	int rac;// 2
	int tac;// 2
	long cellId;// 4
	int cause;// 2
	int keyword;// 2
	String apn;// 32

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(procedureType);// 1
		buffer.putUnsigned(subProcedureType);// 1
		buffer.putLong(startTime.getTime());// 8
		buffer.putLong(endTime.getTime());// 8
		buffer.putUnsigned(procedureStatus);// 1
		buffer.put(userIpv4);//getIp(userIpv4, 4));// 4
		buffer.put(userIpv6);//getIp(userIpv6, 16));// 16
		buffer.put(mmeAddress);//getIp(mmeAddress, 16));// 16
		buffer.put(oldMmeAddress);//getIp(oldMmeAddress, 16));// 16
		buffer.put(sgsnCIP);//getIp(sgsnCIP, 16));// 16
		buffer.put(sgsnUIP);//getIp(sgsnUIP, 16));// 16
		buffer.putUnsignedShort(mmePort);// 2
		buffer.putUnsignedShort(oldMmePort);// 2
		buffer.putUnsignedShort(sgsnCPort);// 2
		buffer.putUnsignedShort(sgsnUPort);// 2
		buffer.putUnsignedInt(sgsnControlTeid);// 4
		buffer.putUnsignedInt(sgsnDataTeid);// 4
		buffer.putUnsignedInt(oldMmeControlTeid);// 4
		buffer.putUnsignedInt(pTmsi);// 4
		buffer.putUnsignedShort(lac);// 2
		buffer.putUnsignedShort(rac);// 2
		buffer.putUnsignedShort(tac);// 2
		buffer.putUnsignedInt(cellId);// 4
		buffer.putUnsignedShort(cause);// 2
		buffer.putUnsignedShort(keyword);// 2
		buffer.put(getFixedBytes(apn, 32, FormatUtils.ZERO));// 32
		buffer.flip();
		return buffer;
	}
	
	public XdrSingleSourceGnC(short procedureType, short subProcedureType,
			Date startTime, Date endTime, short procedureStatus,
			byte[] userIpv4, byte[] userIpv6, byte[] mmeAddress,
			byte[] oldMmeAddress, byte[] sgsnCIP, byte[] sgsnUIP, int mmePort,
			int oldMmePort, int sgsnCPort, int sgsnUPort, long sgsnControlTeid,
			long sgsnDataTeid, long oldMmeControlTeid, long pTmsi, int lac,
			int rac, int tac, long cellId, int cause, int keyword, String apn) {
		super();
		this.procedureType = procedureType;
		this.subProcedureType = subProcedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procedureStatus = procedureStatus;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.mmeAddress = mmeAddress;
		this.oldMmeAddress = oldMmeAddress;
		this.sgsnCIP = sgsnCIP;
		this.sgsnUIP = sgsnUIP;
		this.mmePort = mmePort;
		this.oldMmePort = oldMmePort;
		this.sgsnCPort = sgsnCPort;
		this.sgsnUPort = sgsnUPort;
		this.sgsnControlTeid = sgsnControlTeid;
		this.sgsnDataTeid = sgsnDataTeid;
		this.oldMmeControlTeid = oldMmeControlTeid;
		this.pTmsi = pTmsi;
		this.lac = lac;
		this.rac = rac;
		this.tac = tac;
		this.cellId = cellId;
		this.cause = cause;
		this.keyword = keyword;
		this.apn = apn;
	}

	@Override
	public int getBodyLength() {
		return 1 + 1 + 8 + 8 + 1 + 4 + 16 + 16 + 16 + 16 + 16 + 2 + 2 + 2 + 2
				+ 4 + 4 + 4 + 4 + 2 + 2 + 2 + 4 + 2 + 2 + 32;
	}

	@Override
	public String toString() {
		return procedureType + "|" + subProcedureType + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + procedureStatus + "|" + FormatUtils.getIp(userIpv4) + "|"
				+ FormatUtils.getIp(userIpv6) + "|" + FormatUtils.getIp(mmeAddress) + "|" + FormatUtils.getIp(oldMmeAddress) + "|"
				+ FormatUtils.getIp(sgsnCIP) + "|" + FormatUtils.getIp(sgsnUIP) + "|" + mmePort + "|" + oldMmePort
				+ "|" + sgsnCPort + "|" + sgsnUPort + "|" + sgsnControlTeid
				+ "|" + sgsnDataTeid + "|" + oldMmeControlTeid + "|" + pTmsi
				+ "|" + lac + "|" + rac + "|" + tac + "|" + cellId + "|"
				+ cause + "|" + keyword + "|" + apn;
	}

	public short getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(short procedureType) {
		this.procedureType = procedureType;
	}

	public short getSubProcedureType() {
		return subProcedureType;
	}

	public void setSubProcedureType(short subProcedureType) {
		this.subProcedureType = subProcedureType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public short getProcedureStatus() {
		return procedureStatus;
	}

	public void setProcedureStatus(short procedureStatus) {
		this.procedureStatus = procedureStatus;
	}

	public byte[] getUserIpv4() {
		return userIpv4;
	}

	public void setUserIpv4(byte[] userIpv4) {
		this.userIpv4 = userIpv4;
	}

	public byte[] getUserIpv6() {
		return userIpv6;
	}

	public void setUserIpv6(byte[] userIpv6) {
		this.userIpv6 = userIpv6;
	}

	public byte[] getMmeAddress() {
		return mmeAddress;
	}

	public void setMmeAddress(byte[] mmeAddress) {
		this.mmeAddress = mmeAddress;
	}

	public byte[] getOldMmeAddress() {
		return oldMmeAddress;
	}

	public void setOldMmeAddress(byte[] oldMmeAddress) {
		this.oldMmeAddress = oldMmeAddress;
	}

	public byte[] getSgsnCIP() {
		return sgsnCIP;
	}

	public void setSgsnCIP(byte[] sgsnCIP) {
		this.sgsnCIP = sgsnCIP;
	}

	public byte[] getSgsnUIP() {
		return sgsnUIP;
	}

	public void setSgsnUIP(byte[] sgsnUIP) {
		this.sgsnUIP = sgsnUIP;
	}

	public int getMmePort() {
		return mmePort;
	}

	public void setMmePort(int mmePort) {
		this.mmePort = mmePort;
	}

	public int getOldMmePort() {
		return oldMmePort;
	}

	public void setOldMmePort(int oldMmePort) {
		this.oldMmePort = oldMmePort;
	}

	public int getSgsnCPort() {
		return sgsnCPort;
	}

	public void setSgsnCPort(int sgsnCPort) {
		this.sgsnCPort = sgsnCPort;
	}

	public int getSgsnUPort() {
		return sgsnUPort;
	}

	public void setSgsnUPort(int sgsnUPort) {
		this.sgsnUPort = sgsnUPort;
	}

	public long getSgsnControlTeid() {
		return sgsnControlTeid;
	}

	public void setSgsnControlTeid(long sgsnControlTeid) {
		this.sgsnControlTeid = sgsnControlTeid;
	}

	public long getSgsnDataTeid() {
		return sgsnDataTeid;
	}

	public void setSgsnDataTeid(long sgsnDataTeid) {
		this.sgsnDataTeid = sgsnDataTeid;
	}

	public long getOldMmeControlTeid() {
		return oldMmeControlTeid;
	}

	public void setOldMmeControlTeid(long oldMmeControlTeid) {
		this.oldMmeControlTeid = oldMmeControlTeid;
	}

	public long getpTmsi() {
		return pTmsi;
	}

	public void setpTmsi(long pTmsi) {
		this.pTmsi = pTmsi;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getRac() {
		return rac;
	}

	public void setRac(int rac) {
		this.rac = rac;
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

	public int getCause() {
		return cause;
	}

	public void setCause(int cause) {
		this.cause = cause;
	}

	public int getKeyword() {
		return keyword;
	}

	public void setKeyword(int keyword) {
		this.keyword = keyword;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}
}
