package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedBytes;
import static com.eversec.lte.utils.FormatUtils.listToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

@SuppressWarnings("serial")
public class XdrSingleSourceS10S11 extends XdrSingleSource {
	short procedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short procedureStatus;// 1
	int failureCause;// 2
	int reqCause;// 2
	byte[] userIpv4;// 4
	byte[] userIpv6;// 16
	byte[] mmeAddress;// 16
	byte[] sgwOrOldMmeAddress;// 16
	int mmePort;// 2
	int sgwOrOldMmePort;// 2
	long mmeControlTeid;// 4
	long oldMmeOrSgwControlTeid;// 4
	String apn;//32
	short epsBearerNumber;// 1
	List<XdrSingleSourceS10S11.Bearer> bearers = new ArrayList<XdrSingleSourceS10S11.Bearer>();

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
		buffer.putUnsignedShort(failureCause);// 2
		buffer.putUnsignedShort(reqCause);// 2
		buffer.put(userIpv4);//FormatUtils.getIp(userIpv4, 4));// 4
		buffer.put(userIpv6);//FormatUtils.getIp(userIpv6, 16));// 16
		buffer.put(mmeAddress);//FormatUtils.getIp(mmeAddress, 16));// 16
		buffer.put(sgwOrOldMmeAddress);//FormatUtils.getIp(sgwOrOldMmeAddress, 16));// 16
		buffer.putUnsignedShort(mmePort);// 2
		buffer.putUnsignedShort(sgwOrOldMmePort);// 2
		buffer.putUnsignedInt(mmeControlTeid);// 4
		buffer.putUnsignedInt(oldMmeOrSgwControlTeid);// 4
		buffer.put(getFixedBytes(apn, 32, FormatUtils.ZERO));// 32
		buffer.putUnsigned(epsBearerNumber);// 1
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < bearers.size(); i++) {
				buffer.putUnsigned(bearers.get(i).getBearerID());// 1
				buffer.putUnsigned(bearers.get(i).getBearerType());// 1
				buffer.putUnsigned(bearers.get(i).getBearerQci());// 1
				buffer.putUnsigned(bearers.get(i).getBearerStatus());// 1
				buffer.putUnsignedInt(bearers.get(i).getBearerEnbGtpTeid());// 4
				buffer.putUnsignedInt(bearers.get(i).getBearerSGWGtpTeid());// 4
			}
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		int len = 0;
		if (epsBearerNumber != 0xFF) {
			len = epsBearerNumber * 12;
		}
		return 1 + 8 + 8 + 1 + 2 + 2 + 4 + 16 + 16 + 16 + 2 + 2 + 4 + 4 +32+ 1
				+ len;
	}

	public static class Bearer {
		short bearerID;// 1
		short bearerType;// 1
		short bearerQci;// 1
		short bearerStatus;// 1
		long bearerEnbGtpTeid;// 4
		long bearerSGWGtpTeid;// 4

		public Bearer() {
		}

		public Bearer(short bearerID, short bearerType, short bearerQci,
				short bearerStatus, long bearerEnbGtpTeid, long bearerSGWGtpTeid) {
			super();
			this.bearerID = bearerID;
			this.bearerType = bearerType;
			this.bearerQci = bearerQci;
			this.bearerStatus = bearerStatus;
			this.bearerEnbGtpTeid = bearerEnbGtpTeid;
			this.bearerSGWGtpTeid = bearerSGWGtpTeid;
		}

		@Override
		public String toString() {
			return bearerID + _DELIMITER_ + bearerType + _DELIMITER_
					+ bearerQci + _DELIMITER_ + bearerStatus + _DELIMITER_
					+ bearerEnbGtpTeid + _DELIMITER_ + bearerSGWGtpTeid;
		}

		public short getBearerID() {
			return bearerID;
		}

		public void setBearerID(short bearerID) {
			this.bearerID = bearerID;
		}

		public short getBearerType() {
			return bearerType;
		}

		public void setBearerType(short bearerType) {
			this.bearerType = bearerType;
		}

		public short getBearerQci() {
			return bearerQci;
		}

		public void setBearerQci(short bearerQci) {
			this.bearerQci = bearerQci;
		}

		public short getBearerStatus() {
			return bearerStatus;
		}

		public void setBearerStatus(short bearerStatus) {
			this.bearerStatus = bearerStatus;
		}

		public long getBearerEnbGtpTeid() {
			return bearerEnbGtpTeid;
		}

		public void setBearerEnbGtpTeid(long bearerEnbGtpTeid) {
			this.bearerEnbGtpTeid = bearerEnbGtpTeid;
		}

		public long getBearerSGWGtpTeid() {
			return bearerSGWGtpTeid;
		}

		public void setBearerSGWGtpTeid(long bearerSGWGtpTeid) {
			this.bearerSGWGtpTeid = bearerSGWGtpTeid;
		}
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

	public int getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(int failureCause) {
		this.failureCause = failureCause;
	}

	public int getReqCause() {
		return reqCause;
	}

	public void setReqCause(int reqCause) {
		this.reqCause = reqCause;
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

	public byte[] getSgwOrOldMmeAddress() {
		return sgwOrOldMmeAddress;
	}

	public void setSgwOrOldMmeAddress(byte[] sgwOrOldMmeAddress) {
		this.sgwOrOldMmeAddress = sgwOrOldMmeAddress;
	}

	public int getMmePort() {
		return mmePort;
	}

	public void setMmePort(int mmePort) {
		this.mmePort = mmePort;
	}

	public int getSgwOrOldMmePort() {
		return sgwOrOldMmePort;
	}

	public void setSgwOrOldMmePort(int sgwOrOldMmePort) {
		this.sgwOrOldMmePort = sgwOrOldMmePort;
	}

	public long getMmeControlTeid() {
		return mmeControlTeid;
	}

	public void setMmeControlTeid(long mmeControlTeid) {
		this.mmeControlTeid = mmeControlTeid;
	}

	public long getOldMmeOrSgwControlTeid() {
		return oldMmeOrSgwControlTeid;
	}

	public void setOldMmeOrSgwControlTeid(long oldMmeOrSgwControlTeid) {
		this.oldMmeOrSgwControlTeid = oldMmeOrSgwControlTeid;
	}

	public short getEpsBearerNumber() {
		return epsBearerNumber;
	}

	public void setEpsBearerNumber(short epsBearerNumber) {
		this.epsBearerNumber = epsBearerNumber;
	}

	public List<XdrSingleSourceS10S11.Bearer> getBearers() {
		return bearers;
	}

	public void setBearers(List<XdrSingleSourceS10S11.Bearer> bearers) {
		this.bearers = bearers;
	}

	 
	 

	public XdrSingleSourceS10S11(short procedureType, Date startTime,
			Date endTime, short procedureStatus, int failureCause,
			int reqCause, byte[] userIpv4, byte[] userIpv6, byte[] mmeAddress,
			byte[] sgwOrOldMmeAddress, int mmePort, int sgwOrOldMmePort,
			long mmeControlTeid, long oldMmeOrSgwControlTeid, String apn,
			short epsBearerNumber, List<Bearer> bearers) {
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procedureStatus = procedureStatus;
		this.failureCause = failureCause;
		this.reqCause = reqCause;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.mmeAddress = mmeAddress;
		this.sgwOrOldMmeAddress = sgwOrOldMmeAddress;
		this.mmePort = mmePort;
		this.sgwOrOldMmePort = sgwOrOldMmePort;
		this.mmeControlTeid = mmeControlTeid;
		this.oldMmeOrSgwControlTeid = oldMmeOrSgwControlTeid;
		this.apn = apn;
		this.epsBearerNumber = epsBearerNumber;
		this.bearers = bearers;
	}

	@Override
	public String toString() {
		String bearStr = (bearers.size() == 0)?"":("|"+ listToString(bearers)) ;
		return procedureType + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + procedureStatus + "|"
				+ failureCause + "|" + reqCause + "|" + FormatUtils.getIp(userIpv4) + "|"
				+ FormatUtils.getIp(userIpv6) + "|" + FormatUtils.getIp(mmeAddress) + "|" + FormatUtils.getIp(sgwOrOldMmeAddress) + "|"
				+ mmePort + "|" + sgwOrOldMmePort + "|" + mmeControlTeid + "|"
				+ oldMmeOrSgwControlTeid + "|" + (apn == null ? "" : apn.trim()) + "|"+ epsBearerNumber +
				bearStr;
	}

}
