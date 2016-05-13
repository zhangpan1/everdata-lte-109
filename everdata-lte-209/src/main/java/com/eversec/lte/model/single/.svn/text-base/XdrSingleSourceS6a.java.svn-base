package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedBytes;
import static com.eversec.lte.utils.FormatUtils.getIp;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

@SuppressWarnings("serial")
public class XdrSingleSourceS6a extends XdrSingleSource {
	short procedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short procedureStatus;// 1
	int cause;// 2
	byte[] userIpv4;// 4
	byte[] userIpv6;// 16
	byte[] mmeAddress; // 16
	byte[] hssAddress; // 16
	int mmePort;// 2
	int hssPort;// 2
	String originRealm;// 44
	String destinationRealm;// 44
	String originHost;// 64
	String destinationHost;// 64
	long applicationId;// 4
	short subscriberStatus;// 1
	short accessRestrictionData;// 1

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
		buffer.putUnsignedShort(cause);// 2
		buffer.put(userIpv4);//getIp(userIpv4, 4));// 4
		buffer.put(userIpv6);//getIp(userIpv6, 16));// 16
		buffer.put(mmeAddress);//getIp(mmeAddress, 16));// 16
		buffer.put(hssAddress);//getIp(hssAddress, 16));// 16
		buffer.putUnsignedShort(mmePort);// 2
		buffer.putUnsignedShort(hssPort);// 2
		buffer.put(getFixedBytes(originRealm, 44, FormatUtils.ZERO));// 44
		buffer.put(getFixedBytes(destinationRealm, 44, FormatUtils.ZERO));// 44
		buffer.put(getFixedBytes(originHost, 64, FormatUtils.ZERO));// 64
		buffer.put(getFixedBytes(destinationHost, 64, FormatUtils.ZERO));// 64
		buffer.putUnsignedInt(applicationId);// 4
		buffer.putUnsigned(subscriberStatus);// 1
		buffer.putUnsigned(accessRestrictionData);// 1
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 8 + 8 + 1 + 2 + 4 + 16 + 16 + 16 + 2 + 2 + 44 + 44 + 64 + 64
				+ 4 + 1 + 1;
	}

	 

	public XdrSingleSourceS6a(short procedureType, Date startTime,
			Date endTime, short procedureStatus, int cause, byte[] userIpv4,
			byte[] userIpv6, byte[] mmeAddress, byte[] hssAddress, int mmePort,
			int hssPort, String originRealm, String destinationRealm,
			String originHost, String destinationHost, long applicationId,
			short subscriberStatus, short accessRestrictionData) {
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procedureStatus = procedureStatus;
		this.cause = cause;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.mmeAddress = mmeAddress;
		this.hssAddress = hssAddress;
		this.mmePort = mmePort;
		this.hssPort = hssPort;
		this.originRealm = originRealm;
		this.destinationRealm = destinationRealm;
		this.originHost = originHost;
		this.destinationHost = destinationHost;
		this.applicationId = applicationId;
		this.subscriberStatus = subscriberStatus;
		this.accessRestrictionData = accessRestrictionData;
	}

	public String getOriginRealm() {
		return originRealm;
	}

	public void setOriginRealm(String originRealm) {
		this.originRealm = originRealm;
	}

	public String getDestinationRealm() {
		return destinationRealm;
	}

	public void setDestinationRealm(String destinationRealm) {
		this.destinationRealm = destinationRealm;
	}

	public String getOriginHost() {
		return originHost;
	}

	public void setOriginHost(String originHost) {
		this.originHost = originHost;
	}

	public String getDestinationHost() {
		return destinationHost;
	}

	public void setDestinationHost(String destinationHost) {
		this.destinationHost = destinationHost;
	}

	public long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
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

	public int getCause() {
		return cause;
	}

	public void setCause(int cause) {
		this.cause = cause;
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

	public byte[] getHssAddress() {
		return hssAddress;
	}

	public void setHssAddress(byte[] hssAddress) {
		this.hssAddress = hssAddress;
	}

	public int getMmePort() {
		return mmePort;
	}

	public void setMmePort(int mmePort) {
		this.mmePort = mmePort;
	}

	public int getHssPort() {
		return hssPort;
	}

	public void setHssPort(int hssPort) {
		this.hssPort = hssPort;
	}

	public short getSubscriberStatus() {
		return subscriberStatus;
	}

	public void setSubscriberStatus(short subscriberStatus) {
		this.subscriberStatus = subscriberStatus;
	}

	public short getAccessRestrictionData() {
		return accessRestrictionData;
	}

	public void setAccessRestrictionData(short accessRestrictionData) {
		this.accessRestrictionData = accessRestrictionData;
	}

	@Override
	public String toString() {
		return  procedureType + "|" + startTime.getTime() + "|" + endTime.getTime() + "|"
				+ procedureStatus + "|" + cause + "|" + FormatUtils.getIp(userIpv4) + "|"
				+ FormatUtils.getIp(userIpv6) + "|" + FormatUtils.getIp(mmeAddress) + "|" + FormatUtils.getIp(hssAddress) + "|"
				+ mmePort + "|" + hssPort + "|" + originRealm
				+ "|" + destinationRealm + "|"
				+ originHost + "|"
				+ destinationHost + "|" + applicationId + "|"
				+ subscriberStatus + "|" + accessRestrictionData  ;
	}
	
	

}
