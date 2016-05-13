package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.getIp;

import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.model.XdrData;
import com.eversec.lte.utils.FormatUtils;

/**
 * 通用业务信息
 * 
 * @author bieremayi
 * 
 */
@SuppressWarnings("serial")
public class XdrSingleS1UBusinessCommon extends XdrData {

	short appTypeCode;// 1
	Date startTime;// 8
	Date endTime;// 8
	int protocolType;// 2
	int appType;// 2
	int appSubType;// 2
	short appContent;// 1
	short appStatus;// 1
	byte[] userIpv4;// 4
	byte[] userIpv6;// 16
	int userPort;// 2
	short l4protocal;// 1
	byte[] appServerIpIpv4;// 4
	byte[] appServerIpIpv6;// 16
	int appServerPort;// 2
	long ulData;// 4
	long dlData;// 4
	long ulIpPacket;// 4
	long dlIpPacket;// 4
	long ulDisOrderNum;// 上行tcp乱序报文数 4
	long dlDisOrderNum;// 下行tcp乱序报文数 4
	long ulRetransNum;// 上行tcp重传报文数 4
	long dlRetransNum;// 下行tcp重传报文数 4
	long tcpRespDelay;// tcp建链响应时延（ms） 4
	long tcpConfirmDeplay;// tcp建链确认时延（ms） 4
	long ulIpFragPackets;// 4
	long dlIpFragPackets;// 4
	long firstReqDelay;// tcp建链成功到第一条事务请求的时延（ms） 4
	long firstRespDelay;// 第一条事务请求到其第一个响应包时延（ms） 4
	long tcpWinSize;// 窗口大小 4
	long mssSize;// mss大小 4
	short tcpTestTimes;// tcp建链尝试次数 1
	short tcpState;// tcp连接状态指示 1
	short finish;// 会话是否结束标志 1

	public XdrSingleS1UBusinessCommon(short appTypeCode, Date startTime,
			Date endTime, int protocolType, int appType, int appSubType,
			short appContent, short appStatus, byte[] userIpv4,
			byte[] userIpv6, int userPort, short l4protocal,
			byte[] appServerIpIpv4, byte[] appServerIpIpv6, int appServerPort,
			long ulData, long dlData, long ulIpPacket, long dlIpPacket,
			long ulDisOrderNum, long dlDisOrderNum, long ulRetransNum,
			long dlRetransNum, long tcpRespDelay, long tcpConfirmDeplay,
			long ulIpFragPackets, long dlIpFragPackets, long firstReqDelay,
			long firstRespDelay, long tcpWinSize, long mssSize,
			short tcpTestTimes, short tcpState, short finish) {
		super();
		this.appTypeCode = appTypeCode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.protocolType = protocolType;
		this.appType = appType;
		this.appSubType = appSubType;
		this.appContent = appContent;
		this.appStatus = appStatus;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.userPort = userPort;
		this.l4protocal = l4protocal;
		this.appServerIpIpv4 = appServerIpIpv4;
		this.appServerIpIpv6 = appServerIpIpv6;
		this.appServerPort = appServerPort;
		this.ulData = ulData;
		this.dlData = dlData;
		this.ulIpPacket = ulIpPacket;
		this.dlIpPacket = dlIpPacket;
		this.ulDisOrderNum = ulDisOrderNum;
		this.dlDisOrderNum = dlDisOrderNum;
		this.ulRetransNum = ulRetransNum;
		this.dlRetransNum = dlRetransNum;
		this.tcpRespDelay = tcpRespDelay;
		this.tcpConfirmDeplay = tcpConfirmDeplay;
		this.ulIpFragPackets = ulIpFragPackets;
		this.dlIpFragPackets = dlIpFragPackets;
		this.firstReqDelay = firstReqDelay;
		this.firstRespDelay = firstRespDelay;
		this.tcpWinSize = tcpWinSize;
		this.mssSize = mssSize;
		this.tcpTestTimes = tcpTestTimes;
		this.tcpState = tcpState;
		this.finish = finish;
	}

	@Override
	public String toString() {
		return appTypeCode + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + protocolType + "|" + appType + "|"
				+ appSubType + "|" + appContent + "|" + appStatus + "|"
				+ FormatUtils.getIp(userIpv4) + "|" + FormatUtils.getIp(userIpv6) + "|" + userPort + "|" + l4protocal
				+ "|" + FormatUtils.getIp(appServerIpIpv4) + "|" + FormatUtils.getIp(appServerIpIpv6) + "|"
				+ appServerPort + "|" + ulData + "|" + dlData + "|"
				+ ulIpPacket + "|" + dlIpPacket + "|" + ulDisOrderNum + "|"
				+ dlDisOrderNum + "|" + ulRetransNum + "|" + dlRetransNum + "|"
				+ tcpRespDelay + "|" + tcpConfirmDeplay + "|" + ulIpFragPackets
				+ "|" + dlIpFragPackets + "|" + firstReqDelay + "|"
				+ firstRespDelay + "|" + tcpWinSize + "|" + mssSize + "|"
				+ tcpTestTimes + "|" + tcpState + "|" + finish;
	}

	public short getAppTypeCode() {
		return appTypeCode;
	}

	public void setAppTypeCode(short appTypeCode) {
		this.appTypeCode = appTypeCode;
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

	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public int getAppSubType() {
		return appSubType;
	}

	public void setAppSubType(int appSubType) {
		this.appSubType = appSubType;
	}

	public short getAppContent() {
		return appContent;
	}

	public void setAppContent(short appContent) {
		this.appContent = appContent;
	}

	public short getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(short appStatus) {
		this.appStatus = appStatus;
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

	public int getUserPort() {
		return userPort;
	}

	public void setUserPort(int userPort) {
		this.userPort = userPort;
	}

	public short getL4protocal() {
		return l4protocal;
	}

	public void setL4protocal(short l4protocal) {
		this.l4protocal = l4protocal;
	}

	public byte[] getAppServerIpIpv4() {
		return appServerIpIpv4;
	}

	public void setAppServerIpIpv4(byte[] appServerIpIpv4) {
		this.appServerIpIpv4 = appServerIpIpv4;
	}

	public byte[] getAppServerIpIpv6() {
		return appServerIpIpv6;
	}

	public void setAppServerIpIpv6(byte[] appServerIpIpv6) {
		this.appServerIpIpv6 = appServerIpIpv6;
	}

	public int getAppServerPort() {
		return appServerPort;
	}

	public void setAppServerPort(int appServerPort) {
		this.appServerPort = appServerPort;
	}

	public long getUlData() {
		return ulData;
	}

	public void setUlData(long ulData) {
		this.ulData = ulData;
	}

	public long getDlData() {
		return dlData;
	}

	public void setDlData(long dlData) {
		this.dlData = dlData;
	}

	public long getUlIpPacket() {
		return ulIpPacket;
	}

	public void setUlIpPacket(long ulIpPacket) {
		this.ulIpPacket = ulIpPacket;
	}

	public long getDlIpPacket() {
		return dlIpPacket;
	}

	public void setDlIpPacket(long dlIpPacket) {
		this.dlIpPacket = dlIpPacket;
	}

	public long getUlDisOrderNum() {
		return ulDisOrderNum;
	}

	public void setUlDisOrderNum(long ulDisOrderNum) {
		this.ulDisOrderNum = ulDisOrderNum;
	}

	public long getDlDisOrderNum() {
		return dlDisOrderNum;
	}

	public void setDlDisOrderNum(long dlDisOrderNum) {
		this.dlDisOrderNum = dlDisOrderNum;
	}

	public long getUlRetransNum() {
		return ulRetransNum;
	}

	public void setUlRetransNum(long ulRetransNum) {
		this.ulRetransNum = ulRetransNum;
	}

	public long getDlRetransNum() {
		return dlRetransNum;
	}

	public void setDlRetransNum(long dlRetransNum) {
		this.dlRetransNum = dlRetransNum;
	}

	public long getTcpRespDelay() {
		return tcpRespDelay;
	}

	public void setTcpRespDelay(long tcpRespDelay) {
		this.tcpRespDelay = tcpRespDelay;
	}

	public long getTcpConfirmDeplay() {
		return tcpConfirmDeplay;
	}

	public void setTcpConfirmDeplay(long tcpConfirmDeplay) {
		this.tcpConfirmDeplay = tcpConfirmDeplay;
	}

	public long getUlIpFragPackets() {
		return ulIpFragPackets;
	}

	public void setUlIpFragPackets(long ulIpFragPackets) {
		this.ulIpFragPackets = ulIpFragPackets;
	}

	public long getDlIpFragPackets() {
		return dlIpFragPackets;
	}

	public void setDlIpFragPackets(long dlIpFragPackets) {
		this.dlIpFragPackets = dlIpFragPackets;
	}

	public long getFirstReqDelay() {
		return firstReqDelay;
	}

	public void setFirstReqDelay(long firstReqDelay) {
		this.firstReqDelay = firstReqDelay;
	}

	public long getFirstRespDelay() {
		return firstRespDelay;
	}

	public void setFirstRespDelay(long firstRespDelay) {
		this.firstRespDelay = firstRespDelay;
	}

	public long getTcpWinSize() {
		return tcpWinSize;
	}

	public void setTcpWinSize(long tcpWinSize) {
		this.tcpWinSize = tcpWinSize;
	}

	public long getMssSize() {
		return mssSize;
	}

	public void setMssSize(long mssSize) {
		this.mssSize = mssSize;
	}

	public short getTcpTestTimes() {
		return tcpTestTimes;
	}

	public void setTcpTestTimes(short tcpTestTimes) {
		this.tcpTestTimes = tcpTestTimes;
	}

	public short getTcpState() {
		return tcpState;
	}

	public void setTcpState(short tcpState) {
		this.tcpState = tcpState;
	}

	public short getFinish() {
		return finish;
	}

	public void setFinish(short finish) {
		this.finish = finish;
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

		buffer.putUnsigned(appTypeCode);// 1
		buffer.putLong(startTime.getTime());// 8
		buffer.putLong(endTime.getTime());// 8
		buffer.putUnsignedShort(protocolType);// 2
		buffer.putUnsignedShort(appType);// 2
		buffer.putUnsignedShort(appSubType);// 2
		buffer.putUnsigned(appContent);// 1
		buffer.putUnsigned(appStatus);// 1
		buffer.put(userIpv4);//getIp(userIpv4, 4));// 4
		buffer.put(userIpv6);//getIp(userIpv6, 16));// 16
		buffer.putUnsignedShort(userPort);// 2
		buffer.putUnsigned(l4protocal);// 1
		buffer.put(appServerIpIpv4);//getIp(appServerIpIpv4, 4));// 4
		buffer.put(appServerIpIpv6);//getIp(appServerIpIpv6, 16));// 16
		buffer.putUnsignedShort(appServerPort);// 2
		buffer.putUnsignedInt(ulData);// 4
		buffer.putUnsignedInt(dlData);// 4
		buffer.putUnsignedInt(ulIpPacket);// 4
		buffer.putUnsignedInt(dlIpPacket);// 4
		buffer.putUnsignedInt(ulDisOrderNum);// 4
		buffer.putUnsignedInt(dlDisOrderNum);// 4
		buffer.putUnsignedInt(ulRetransNum);// 4
		buffer.putUnsignedInt(dlRetransNum);// 4
		buffer.putUnsignedInt(tcpRespDelay);// 4
		buffer.putUnsignedInt(tcpConfirmDeplay);// 4
		buffer.putUnsignedInt(ulIpFragPackets);// 4
		buffer.putUnsignedInt(dlIpFragPackets);// 4
		buffer.putUnsignedInt(firstReqDelay);// 4
		buffer.putUnsignedInt(firstRespDelay);// 4
		buffer.putUnsignedInt(tcpWinSize);// 4
		buffer.putUnsignedInt(mssSize);// 4
		buffer.putUnsigned(tcpTestTimes);// 1
		buffer.putUnsigned(tcpState);// 1
		buffer.putUnsigned(finish);// 1
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 8 + 8 + 2 + 2 + 2 + 1 + 1 + 4 + 16 + 2 + 1 + 4 + 16 + 2 + 4
				+ 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 1
				+ 1 + 1;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

}
