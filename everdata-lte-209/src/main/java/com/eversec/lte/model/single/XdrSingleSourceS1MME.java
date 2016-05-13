package com.eversec.lte.model.single;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

import static com.eversec.lte.utils.FormatUtils.F;
import static com.eversec.lte.utils.FormatUtils.getFixedBytes;
import static com.eversec.lte.utils.FormatUtils.getIp;
import static com.eversec.lte.utils.FormatUtils.listToString;

@SuppressWarnings("serial")
public class XdrSingleSourceS1MME extends XdrSingleSource {
	short procedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short procedureStatus;// 1
	int requestCause;// 2
	int failureCause;// 2
	short keyword1;// 1
	short keyword2;// 1
	short keyword3;// 1
	short keyword4;// 1
	long mmeUeS1apID;// 4
	int oldMmeGroupID;// 2
	short oldMmeCode;// 1
	long oldMTmsi;// 4
	int mmeGroupID;// 2
	short mmeCode;// 1
	long mTmsi;// 4
	long tmsi;// 4
	byte[] userIpv4;// 4
	byte[] userIpv6;// 16
	byte[] mmeIpAdd;// 16
	byte[] enbIpAdd;// 16
	int mmePort;// 2
	int enbPort;// 2
	int tac;// 2
	long cellID;// 4
	int otherTac;// 2
	long otherEci;// 4
	String apn;// 32
	short epsBearerNumber;// 1

	List<XdrSingleSourceS1MME.Bearer> bearers = new ArrayList<XdrSingleSourceS1MME.Bearer>();

	String lowID;// xdr低8位HEX字符串（额外添加字段用于判断s1mme主流程）

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
		buffer.putUnsignedShort(requestCause);// 2
		buffer.putUnsignedShort(failureCause);// 2
		buffer.putUnsigned(keyword1);// 1
		buffer.putUnsigned(keyword2);// 1
		buffer.putUnsigned(keyword3);// 1
		buffer.putUnsigned(keyword4);// 1
		buffer.putUnsignedInt(mmeUeS1apID);// 4
		buffer.putUnsignedShort(oldMmeGroupID);// 2
		buffer.putUnsigned(oldMmeCode);// 1
		buffer.putUnsignedInt(oldMTmsi);// 4
		buffer.putUnsignedShort(mmeGroupID);// 2
		buffer.putUnsigned(mmeCode);// 1
		buffer.putUnsignedInt(mTmsi);// 4
		buffer.putUnsignedInt(tmsi);// 4
		buffer.put(userIpv4);//getIp(userIpv4, 4));// 4
		buffer.put(userIpv6);//getIp(userIpv6, 16));// 16
		buffer.put(mmeIpAdd);//getIp(mmeIpAdd, 16));// 16
		buffer.put(enbIpAdd);//getIp(enbIpAdd, 16));// 16
		buffer.putUnsignedShort(mmePort);// 2
		buffer.putUnsignedShort(enbPort);// 2
		buffer.putUnsignedShort(tac);// 2
		buffer.putUnsignedInt(cellID);// 4
		buffer.putUnsignedShort(otherTac);// 2
		buffer.putUnsignedInt(otherEci);// 4
		buffer.put(getFixedBytes(apn, 32, FormatUtils.ZERO));// 32
		buffer.putUnsigned(epsBearerNumber);// 1
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < bearers.size(); i++) {
				buffer.putUnsigned(bearers.get(i).getBearerID());
				buffer.putUnsigned(bearers.get(i).getBearerType());
				buffer.putUnsigned(bearers.get(i).getBearerQCI());
				buffer.putUnsigned(bearers.get(i).getBearerStatus());
				buffer.putUnsignedShort(bearers.get(i).getRequestCause());
				buffer.putUnsignedShort(bearers.get(i).getFailureCause());
				buffer.putUnsignedInt(bearers.get(i).getBearerEnbGtpTeid());
				buffer.putUnsignedInt(bearers.get(i).getBearerSGWGtpTeid());
			}
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		int len = 0;
		if (epsBearerNumber != 0xFF) {
			len = epsBearerNumber * 16;
		}
		return 1 + 8 + 8 + 1 + 2 + 2 +  1 +1+1 +1  + 4 + 2 + 1 + 4 + 2 + 1+ 4 + 4 + 4 + 16 + 16 + 16 + 2
				+ 2 + 2 + 4 + 2 + 4 + 32 + 1 + len;
	}

	 

	public XdrSingleSourceS1MME(short procedureType, Date startTime,
			Date endTime, short procedureStatus, int requestCause,
			int failureCause, short keyword1, short keyword2, short keyword3,
			short keyword4, long mmeUeS1apID, int oldMmeGroupID,
			short oldMmeCode, long oldMTmsi, int mmeGroupID, short mmeCode,
			long mTmsi, long tmsi, byte[] userIpv4, byte[] userIpv6,
			byte[] mmeIpAdd, byte[] enbIpAdd, int mmePort, int enbPort,
			int tac, long cellID, int otherTac, long otherEci, String apn,
			short epsBearerNumber, List<Bearer> bearers) {
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procedureStatus = procedureStatus;
		this.requestCause = requestCause;
		this.failureCause = failureCause;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.keyword4 = keyword4;
		this.mmeUeS1apID = mmeUeS1apID;
		this.oldMmeGroupID = oldMmeGroupID;
		this.oldMmeCode = oldMmeCode;
		this.oldMTmsi = oldMTmsi;
		this.mmeGroupID = mmeGroupID;
		this.mmeCode = mmeCode;
		this.mTmsi = mTmsi;
		this.tmsi = tmsi;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.mmeIpAdd = mmeIpAdd;
		this.enbIpAdd = enbIpAdd;
		this.mmePort = mmePort;
		this.enbPort = enbPort;
		this.tac = tac;
		this.cellID = cellID;
		this.otherTac = otherTac;
		this.otherEci = otherEci;
		this.apn = apn;
		this.epsBearerNumber = epsBearerNumber;
		this.bearers = bearers;
	}

	public short getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(short keyword2) {
		this.keyword2 = keyword2;
	}

	public short getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(short keyword3) {
		this.keyword3 = keyword3;
	}

	public short getKeyword4() {
		return keyword4;
	}

	public void setKeyword4(short keyword4) {
		this.keyword4 = keyword4;
	}

	public int getOldMmeGroupID() {
		return oldMmeGroupID;
	}

	public void setOldMmeGroupID(int oldMmeGroupID) {
		this.oldMmeGroupID = oldMmeGroupID;
	}

	public short getOldMmeCode() {
		return oldMmeCode;
	}

	public void setOldMmeCode(short oldMmeCode) {
		this.oldMmeCode = oldMmeCode;
	}

	public long getOldMTmsi() {
		return oldMTmsi;
	}

	public void setOldMTmsi(long oldMTmsi) {
		this.oldMTmsi = oldMTmsi;
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

	public short getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(short keyword1) {
		this.keyword1 = keyword1;
	}

	public long getMmeUeS1apID() {
		return mmeUeS1apID;
	}

	public void setMmeUeS1apID(long mmeUeS1apID) {
		this.mmeUeS1apID = mmeUeS1apID;
	}

	public int getMmeGroupID() {
		return mmeGroupID;
	}

	public void setMmeGroupID(int mmeGroupID) {
		this.mmeGroupID = mmeGroupID;
	}

	public short getMmeCode() {
		return mmeCode;
	}

	public void setMmeCode(short mmeCode) {
		this.mmeCode = mmeCode;
	}

	public long getmTmsi() {
		return mTmsi;
	}

	public void setmTmsi(long mTmsi) {
		this.mTmsi = mTmsi;
	}

	public long getTmsi() {
		return tmsi;
	}

	public void setTmsi(long tmsi) {
		this.tmsi = tmsi;
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

	public byte[] getEnbIpAdd() {
		return enbIpAdd;
	}

	public void setEnbIpAdd(byte[] enbIpAdd) {
		this.enbIpAdd = enbIpAdd;
	}

	public int getMmePort() {
		return mmePort;
	}

	public void setMmePort(int mmePort) {
		this.mmePort = mmePort;
	}

	public int getEnbPort() {
		return enbPort;
	}

	public void setEnbPort(int enbPort) {
		this.enbPort = enbPort;
	}

	public int getTac() {
		return tac;
	}

	public void setTac(int tac) {
		this.tac = tac;
	}

	public long getCellID() {
		return cellID;
	}

	public void setCellID(long cellID) {
		this.cellID = cellID;
	}

	public int getOtherTac() {
		return otherTac;
	}

	public void setOtherTac(int otherTac) {
		this.otherTac = otherTac;
	}

	public long getOtherEci() {
		return otherEci;
	}

	public void setOtherEci(long otherEci) {
		this.otherEci = otherEci;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public short getEpsBearerNumber() {
		return epsBearerNumber;
	}

	public void setEpsBearerNumber(short epsBearerNumber) {
		this.epsBearerNumber = epsBearerNumber;
	}

	public static class Bearer {
		short bearerID;// 1
		short bearerType;// 1
		short bearerQCI;// 1
		short bearerStatus;// 1
		int requestCause;//2
		int failureCause;//2
		long bearerEnbGtpTeid;// 4
		long bearerSGWGtpTeid;// 4

		

		public Bearer(short bearerID, short bearerType, short bearerQCI,
				short bearerStatus, int requestCause, int failureCause,
				long bearerEnbGtpTeid, long bearerSGWGtpTeid) {
			this.bearerID = bearerID;
			this.bearerType = bearerType;
			this.bearerQCI = bearerQCI;
			this.bearerStatus = bearerStatus;
			this.requestCause = requestCause;
			this.failureCause = failureCause;
			this.bearerEnbGtpTeid = bearerEnbGtpTeid;
			this.bearerSGWGtpTeid = bearerSGWGtpTeid;
		}

		@Override
		public String toString() {
			return bearerID + _DELIMITER_ + bearerType + _DELIMITER_
					+ bearerQCI + _DELIMITER_ + bearerStatus + _DELIMITER_
					+ requestCause + _DELIMITER_ + failureCause + _DELIMITER_
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

		public short getBearerQCI() {
			return bearerQCI;
		}

		public void setBearerQCI(short bearerQCI) {
			this.bearerQCI = bearerQCI;
		}

		public short getBearerStatus() {
			return bearerStatus;
		}

		public void setBearerStatus(short bearerStatus) {
			this.bearerStatus = bearerStatus;
		}

		public int getRequestCause() {
			return requestCause;
		}

		public void setRequestCause(int requestCause) {
			this.requestCause = requestCause;
		}

		public int getFailureCause() {
			return failureCause;
		}

		public void setFailureCause(int failureCause) {
			this.failureCause = failureCause;
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

	public List<XdrSingleSourceS1MME.Bearer> getBearers() {
		return bearers;
	}

	public void setBearers(List<XdrSingleSourceS1MME.Bearer> bearers) {
		this.bearers = bearers;
	}
	
	public int getRequestCause() {
		return requestCause;
	}
	public void setRequestCause(int requestCause) {
		this.requestCause = requestCause;
	}
	public int getFailureCause() {
		return failureCause;
	}
	public void setFailureCause(int failureCause) {
		this.failureCause = failureCause;
	} 

	@Override
	public String toString() {
		String bearStr = (bearers.size() == 0)?"":("|"+ listToString(bearers)) ;
		return  procedureType + "|" + startTime.getTime() + "|" + endTime.getTime() + "|"
				+ procedureStatus + "|" + requestCause + "|" + failureCause
				+ "|" + keyword1 + "|" + keyword2 + "|" + keyword3 + "|"
				+ keyword4 + "|" + mmeUeS1apID + "|" + oldMmeGroupID + "|"
				+ oldMmeCode + "|" + oldMTmsi + "|" + mmeGroupID + "|"
				+ mmeCode + "|" + mTmsi + "|" + tmsi + "|" + FormatUtils.getIp(userIpv4) + "|"
				+ FormatUtils.getIp(userIpv6) + "|" + FormatUtils.getIp(mmeIpAdd) + "|" + FormatUtils.getIp(enbIpAdd) + "|" + mmePort
				+ "|" + enbPort + "|" + tac + "|" + cellID + "|" + otherTac
				+ "|" + otherEci + "|" + (apn == null ? "" : apn.trim()) + "|" + epsBearerNumber +
				bearStr ;
	}

	public String getLowID() {
		return lowID;
	}

	public void setLowID(String lowID) {
		this.lowID = lowID;
	}

}
