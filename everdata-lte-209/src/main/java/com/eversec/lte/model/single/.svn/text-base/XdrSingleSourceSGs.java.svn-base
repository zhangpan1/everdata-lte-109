package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedBytes;
import static com.eversec.lte.utils.FormatUtils.getFixedTBCDBytes;
import static com.eversec.lte.utils.FormatUtils.getIp;
import static com.eversec.lte.utils.FormatUtils.setString;

import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

@SuppressWarnings("serial")
public class XdrSingleSourceSGs extends XdrSingleSource {
	short procedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short procedureStatus;// 1
	short sgsCause;// 1
	short rejectCause;// 1
	short cpCause;// 1
	short rpCause;// 1
	byte[] userIpv4;// 4
	byte[] userIpv6;// 16
	byte[] mmeIpAdd;// 16
	byte[] mscServerIpAdd;// 16
	int mmePort;// 2
	int mscServerPort;// 2
	short serviceIndicator;// 1
	String mmeName;// 55
	long tmsi;// 4
	int newLac;// 2
	int oldLac;// 2
	int tac;// 2
	long cellId;// 4
	String callingId;// 24
	short vlrNameLength;// 1
	String vlrName;// 可变长

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(procedureType);// 1
		buffer.putLong(startTime.getTime());// 8
		buffer.putLong(endTime.getTime());// 8
		buffer.putUnsigned(procedureStatus);// 1
		buffer.putUnsigned(sgsCause);// 1
		buffer.putUnsigned(rejectCause);// 1
		buffer.putUnsigned(cpCause);// 1
		buffer.putUnsigned(rpCause);// 1
		buffer.put(userIpv4);//getIp(userIpv4, 4));// 4
		buffer.put(userIpv6);//getIp(userIpv6, 16)); // 16
		buffer.put(mmeIpAdd);//getIp(mmeIpAdd, 16));// 16);
		buffer.put(mscServerIpAdd);//getIp(mscServerIpAdd, 16));// 16);
		buffer.putUnsignedShort(mmePort);// 2
		buffer.putUnsignedShort(mscServerPort);// 2
		buffer.putUnsigned(serviceIndicator);// 1
		setString(buffer, mmeName, 55);
		buffer.putUnsignedInt(tmsi);// 4
		buffer.putUnsignedShort(newLac); // 2
		buffer.putUnsignedShort(oldLac); // 2
		buffer.putUnsignedShort(tac); // 2
		buffer.putUnsignedInt(cellId);// 4
		buffer.put(getFixedTBCDBytes(callingId, 24, F));// 24
		buffer.putUnsigned(vlrNameLength);// 1
		setString(buffer, vlrName, vlrNameLength);
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 8 + 8 + 1 + 1 + 1 + 1 + 1 + 4 + 16 + 16 + 16 + 2 + 2 + 1
				+ 55 + 4 + 2 + 2 + 2 + 4 + 24 + 1 + vlrNameLength;
	}

	public XdrSingleSourceSGs(short procedureType, Date startTime,
			Date endTime, short procedureStatus, short sgsCause,
			short rejectCause, short cpCause, short rpCause, byte[] userIpv4,
			byte[] userIpv6, byte[] mmeIpAdd, byte[] mscServerIpAdd,
			int mmePort, int mscServerPort, short serviceIndicator,
			String mmeName, long tmsi, int newLac, int oldLac, int tac,
			long cellId, String callingId, short vlrNameLength, String vlrName) {
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procedureStatus = procedureStatus;
		this.sgsCause = sgsCause;
		this.rejectCause = rejectCause;
		this.cpCause = cpCause;
		this.rpCause = rpCause;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.mmeIpAdd = mmeIpAdd;
		this.mscServerIpAdd = mscServerIpAdd;
		this.mmePort = mmePort;
		this.mscServerPort = mscServerPort;
		this.serviceIndicator = serviceIndicator;
		this.mmeName = mmeName;
		this.tmsi = tmsi;
		this.newLac = newLac;
		this.oldLac = oldLac;
		this.tac = tac;
		this.cellId = cellId;
		this.callingId = callingId;
		this.vlrNameLength = vlrNameLength;
		this.vlrName = vlrName;
	}

	public short getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(short procedureType) {
		this.procedureType = procedureType;
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

	public short getSgsCause() {
		return sgsCause;
	}

	public void setSgsCause(short sgsCause) {
		this.sgsCause = sgsCause;
	}

	public short getRejectCause() {
		return rejectCause;
	}

	public void setRejectCause(short rejectCause) {
		this.rejectCause = rejectCause;
	}

	public short getCpCause() {
		return cpCause;
	}

	public void setCpCause(short cpCause) {
		this.cpCause = cpCause;
	}

	public short getRpCause() {
		return rpCause;
	}

	public void setRpCause(short rpCause) {
		this.rpCause = rpCause;
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

	public byte[] getMmeIpAdd() {
		return mmeIpAdd;
	}

	public void setMmeIpAdd(byte[] mmeIpAdd) {
		this.mmeIpAdd = mmeIpAdd;
	}

	public byte[] getMscServerIpAdd() {
		return mscServerIpAdd;
	}

	public void setMscServerIpAdd(byte[] mscServerIpAdd) {
		this.mscServerIpAdd = mscServerIpAdd;
	}

	public int getMmePort() {
		return mmePort;
	}

	public void setMmePort(int mmePort) {
		this.mmePort = mmePort;
	}

	public int getMscServerPort() {
		return mscServerPort;
	}

	public void setMscServerPort(int mscServerPort) {
		this.mscServerPort = mscServerPort;
	}

	public short getServiceIndicator() {
		return serviceIndicator;
	}

	public void setServiceIndicator(short serviceIndicator) {
		this.serviceIndicator = serviceIndicator;
	}

	public String getMmeName() {
		return mmeName;
	}

	public void setMmeName(String mmeName) {
		this.mmeName = mmeName;
	}

	public long getTmsi() {
		return tmsi;
	}

	public void setTmsi(long tmsi) {
		this.tmsi = tmsi;
	}

	public int getNewLac() {
		return newLac;
	}

	public void setNewLac(int newLac) {
		this.newLac = newLac;
	}

	public int getOldLac() {
		return oldLac;
	}

	public void setOldLac(int oldLac) {
		this.oldLac = oldLac;
	}

	public int getTac() {
		return tac;
	}

	public void setTac(int tac) {
		this.tac = tac;
	}

	public String getCallingId() {
		return callingId;
	}

	public void setCallingId(String callingId) {
		this.callingId = callingId;
	}

	public long getCellId() {
		return cellId;
	}

	public void setCellId(long cellId) {
		this.cellId = cellId;
	}

	public short getVlrNameLength() {
		return vlrNameLength;
	}

	public void setVlrNameLength(short vlrNameLength) {
		this.vlrNameLength = vlrNameLength;
	}

	public String getVlrName() {
		return vlrName;
	}

	public void setVlrName(String vlrName) {
		this.vlrName = vlrName;
	}

	@Override
	public String toString() {
		return procedureType + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + procedureStatus + "|" + sgsCause
				+ "|" + rejectCause + "|" + cpCause + "|" + rpCause + "|"
				+ FormatUtils.getIp(userIpv4) + "|" + FormatUtils.getIp(userIpv6) + "|" + FormatUtils.getIp(mmeIpAdd) + "|"
				+ FormatUtils.getIp(mscServerIpAdd) + "|" + mmePort + "|" + mscServerPort + "|"
				+ serviceIndicator + "|" + mmeName + "|" + tmsi + "|" + newLac
				+ "|" + oldLac + "|" + tac + "|" + cellId + "|" + callingId
				+ "|" + vlrNameLength + "|" + vlrName;
	}

}
