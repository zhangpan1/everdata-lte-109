package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.getIp;
import static com.eversec.lte.utils.FormatUtils.listToString;
import static com.eversec.lte.utils.FormatUtils.setString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

@SuppressWarnings("serial")
public class XdrSingleSourceS5S8C extends XdrSingleSource {
	short procedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short procedureStatus;// 1
	int failureCause;// 2
	byte[] userIpv4;// 4
	byte[] userIpv6;// 16
	byte[] sgwIpAdd;// 16
	byte[] pgwIpAdd;// 16
	int sgwPort;// 2
	int pgwPort;// 2
	long sgwControlTeid;// 4
	long pgwControlTeid;// 4
	byte[] indicationFlags;// 7
	short uliLength;// 1
	String uli;
	short epsBearerNumber;// 1

	List<XdrSingleSourceS5S8C.Bearer> bearers = new ArrayList<XdrSingleSourceS5S8C.Bearer>();

	public XdrSingleSourceS5S8C(short procedureType, Date startTime,
			Date endTime, short procedureStatus, int failureCause,
			byte[] userIpv4, byte[] userIpv6, byte[] sgwIpAdd, byte[] pgwIpAdd,
			int sgwPort, int pgwPort, long sgwControlTeid, long pgwControlTeid,
			byte[] indicationFlags, short uliLength, String uli,
			short epsBearerNumber, List<Bearer> bearers) {
		super();
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procedureStatus = procedureStatus;
		this.failureCause = failureCause;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.sgwIpAdd = sgwIpAdd;
		this.pgwIpAdd = pgwIpAdd;
		this.sgwPort = sgwPort;
		this.pgwPort = pgwPort;
		this.sgwControlTeid = sgwControlTeid;
		this.pgwControlTeid = pgwControlTeid;
		this.indicationFlags = indicationFlags;
		this.uliLength = uliLength;
		this.uli = uli;
		this.epsBearerNumber = epsBearerNumber;
		this.bearers = bearers;
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

	public byte[] getSgwIpAdd() {
		return sgwIpAdd;
	}

	public void setSgwIpAdd(byte[] sgwIpAdd) {
		this.sgwIpAdd = sgwIpAdd;
	}

	public byte[] getPgwIpAdd() {
		return pgwIpAdd;
	}

	public void setPgwIpAdd(byte[] pgwIpAdd) {
		this.pgwIpAdd = pgwIpAdd;
	}

	public int getSgwPort() {
		return sgwPort;
	}

	public void setSgwPort(int sgwPort) {
		this.sgwPort = sgwPort;
	}

	public int getPgwPort() {
		return pgwPort;
	}

	public void setPgwPort(int pgwPort) {
		this.pgwPort = pgwPort;
	}

	public long getSgwControlTeid() {
		return sgwControlTeid;
	}

	public void setSgwControlTeid(long sgwControlTeid) {
		this.sgwControlTeid = sgwControlTeid;
	}

	public long getPgwControlTeid() {
		return pgwControlTeid;
	}

	public void setPgwControlTeid(long pgwControlTeid) {
		this.pgwControlTeid = pgwControlTeid;
	}

	public byte[] getIndicationFlags() {
		return indicationFlags;
	}

	public void setIndicationFlags(byte[] indicationFlags) {
		this.indicationFlags = indicationFlags;
	}

	public short getUliLength() {
		return uliLength;
	}

	public void setUliLength(short uliLength) {
		this.uliLength = uliLength;
	}

	public String getUli() {
		return uli;
	}

	public void setUli(String uli) {
		this.uli = uli;
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
		short bearerQci;// 1
		short bearerStatus;// 1
		long bearerSgwGtpTeid;// 4
		long bearerPgwGtpTeid;// 4

		@Override
		public String toString() {
			return bearerID + _DELIMITER_ + bearerType + _DELIMITER_ + bearerQci + _DELIMITER_
					+ bearerStatus + _DELIMITER_ + bearerSgwGtpTeid + _DELIMITER_
					+ bearerPgwGtpTeid;
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

		public long getBearerSgwGtpTeid() {
			return bearerSgwGtpTeid;
		}

		public void setBearerSgwGtpTeid(long bearerSgwGtpTeid) {
			this.bearerSgwGtpTeid = bearerSgwGtpTeid;
		}

		public long getBearerPgwGtpTeid() {
			return bearerPgwGtpTeid;
		}

		public void setBearerPgwGtpTeid(long bearerPgwGtpTeid) {
			this.bearerPgwGtpTeid = bearerPgwGtpTeid;
		}

		public Bearer(short bearerID, short bearerType, short bearerQci,
				short bearerStatus, long bearerSgwGtpTeid, long bearerPgwGtpTeid) {
			super();
			this.bearerID = bearerID;
			this.bearerType = bearerType;
			this.bearerQci = bearerQci;
			this.bearerStatus = bearerStatus;
			this.bearerSgwGtpTeid = bearerSgwGtpTeid;
			this.bearerPgwGtpTeid = bearerPgwGtpTeid;
		}
	}

	public List<XdrSingleSourceS5S8C.Bearer> getBearers() {
		return bearers;
	}

	public void setBearers(List<XdrSingleSourceS5S8C.Bearer> bearers) {
		this.bearers = bearers;
	}

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
		buffer.put(userIpv4);//getIp(userIpv4, 4));// 4);// 4
		buffer.put(userIpv6);//getIp(userIpv6, 16));// 16);
		buffer.put(sgwIpAdd);//getIp(sgwIpAdd, 16));// 16);
		buffer.put(pgwIpAdd);//getIp(pgwIpAdd, 16));// 16);
		buffer.putUnsignedShort(sgwPort);// 2
		buffer.putUnsignedShort(pgwPort);// 2
		buffer.putUnsignedInt(sgwControlTeid);// 4
		buffer.putUnsignedInt(pgwControlTeid);// 4
		buffer.put( indicationFlags );// 7
		buffer.putUnsigned(uliLength);// 1
		setString(buffer, uli, uliLength);
		buffer.putUnsigned(epsBearerNumber); // 1
		if (epsBearerNumber > 0 && epsBearerNumber != 0xff) {
			for (int i = 0; i < bearers.size(); i++) {
				Bearer bearer = bearers.get(i);
				buffer.putUnsigned(bearer.getBearerID());
				buffer.putUnsigned(bearer.getBearerType());
				buffer.putUnsigned(bearer.getBearerQci());
				buffer.putUnsigned(bearer.getBearerStatus());
				buffer.putUnsignedInt(bearer.getBearerSgwGtpTeid());
				buffer.putUnsignedInt(bearer.getBearerPgwGtpTeid());
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
		return 1 + 8 + 8 + 1 + 2 + 4 + 16 + 16 + 16 + 2 + 2 + 4 + 4 + 7 + 1
				+ uliLength + 1 + len;

	}

	@Override
	public String toString() {
		String bearStr = (bearers.size() == 0)?"":("|"+ listToString(bearers)) ;
		return procedureType + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + procedureStatus + "|"
				+ failureCause + "|" + FormatUtils.getIp(userIpv4) + "|" + FormatUtils.getIp(userIpv6) + "|"
				+ FormatUtils.getIp(sgwIpAdd) + "|" + FormatUtils.getIp(pgwIpAdd) + "|" + sgwPort + "|" + pgwPort
				+ "|" + sgwControlTeid + "|" + pgwControlTeid + "|"
				+ Hex.encodeHexString(indicationFlags ) + "|" + uliLength + "|" + uli + "|"
				+ epsBearerNumber + bearStr;
	}
}
