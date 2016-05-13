package com.eversec.lte.model.compound;

import static com.eversec.lte.utils.FormatUtils.setFormatLatLon;
import static com.eversec.lte.utils.FormatUtils.getIp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

/**
 * 合成信令
 */
@SuppressWarnings("serial")
public class XdrCompoundSourceSignaling extends XdrCompoundSource {
	short procedureType; // 1
	Date startTime; // 8
	Date endTime; // 8
	double startLongitude; // 8
	double startLatitude; // 8
	double endLongitude; // 8
	double endLatitude; // 8
	short procedureStatus; // 1
	int requestCause;// 2
	short failureInterface; // 1
	short failureProcedureType;// 1
	int failureCause; // 2
	short keyword1; // 1
	short keyword2; // 1
	short keyword3; // 1
	short keyword4; // 1
	long enbId; // 4
	long cellId; // 4
	int mmeGroupId; // 2
	short mmeCode; // 1
	int tac; // 2
	String userIpv4;// 4
	String userIpv6;// 16
	long newEnbId; // 4
	long newCellId; // 4
	int newMmeGroupId; // 2
	short newMmeCode; // 1
	int newTac; // 2
	short epsBearerNumber; // 1
	List<XdrCompoundSourceSignaling.EpsBearer> bearers = new ArrayList<XdrCompoundSourceSignaling.EpsBearer>();
	short xdrNumber; // 1
	List<XdrCompoundSourceSignaling.XdrSingle> singles = new ArrayList<XdrCompoundSourceSignaling.XdrSingle>();

	public XdrCompoundSourceSignaling() {
	}

	public static Comparator<XdrSingle> XDR_SINGLE_COMPARATOR = new Comparator<XdrSingle>() {
		@Override
		public int compare(XdrSingle o1, XdrSingle o2) {
			return (int) (o1.getStartTime().getTime() - o2.getStartTime()
					.getTime());
		}
	};

	public XdrCompoundSourceSignaling(short procedureType, Date startTime,
			Date endTime, double startLongitude, double startLatitude,
			double endLongitude, double endLatitude, short procedureStatus,
			int requestCause, short failureInterface,
			short failureProcedureType, int failureCause, short keyword1,
			short keyword2, short keyword3, short keyword4, long enbId,
			long cellId, int mmeGroupId, short mmeCode, int tac,
			String userIpv4, String userIpv6, long newEnbId, long newCellId,
			int newMmeGroupId, short newMmeCode, int newTac,
			short epsBearerNumber, List<EpsBearer> bearers, short xdrNumber,
			List<XdrSingle> singles) {
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startLongitude = startLongitude;
		this.startLatitude = startLatitude;
		this.endLongitude = endLongitude;
		this.endLatitude = endLatitude;
		this.procedureStatus = procedureStatus;
		this.requestCause = requestCause;
		this.failureInterface = failureInterface;
		this.failureProcedureType = failureProcedureType;
		this.failureCause = failureCause;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.keyword4 = keyword4;
		this.enbId = enbId;
		this.cellId = cellId;
		this.mmeGroupId = mmeGroupId;
		this.mmeCode = mmeCode;
		this.tac = tac;
		this.userIpv4 = userIpv4;
		this.userIpv6 = userIpv6;
		this.newEnbId = newEnbId;
		this.newCellId = newCellId;
		this.newMmeGroupId = newMmeGroupId;
		this.newMmeCode = newMmeCode;
		this.newTac = newTac;
		this.epsBearerNumber = epsBearerNumber;
		this.bearers = bearers;
		this.xdrNumber = xdrNumber;
		this.singles = singles;
	}

	@Override
	public String toString() {
		return procedureType + "|" + startTime.getTime() + "|" + endTime.getTime() + "|"
				+ startLongitude + "|" + startLatitude + "|" + endLongitude
				+ "|" + endLatitude + "|" + procedureStatus + "|"
				+ requestCause + "|" + failureInterface + "|"
				+ failureProcedureType + "|" + failureCause + "|" + keyword1
				+ "|" + keyword2 + "|" + keyword3 + "|" + keyword4 + "|"
				+ enbId + "|" + cellId + "|" + mmeGroupId + "|" + mmeCode + "|"
				+ tac + "|" + userIpv4 + "|" + userIpv6 + "|" + newEnbId + "|"
				+ newCellId + "|" + newMmeGroupId + "|" + newMmeCode + "|"
				+ newTac + "|" + epsBearerNumber + "|"
				+ FormatUtils.listToString(bearers) + "|" + xdrNumber + "|"
				+ FormatUtils.listToString(singles);
	}

	@Override
	public int getMemoryBytes() {
		return toString().getBytes().length
				+ common.toString().getBytes().length + 1;
	}

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(procedureType); // 1
		buffer.putLong(startTime.getTime()); // 8
		buffer.putLong(endTime.getTime()); // 8
		setFormatLatLon(buffer, startLongitude);// 8
		setFormatLatLon(buffer, startLatitude);// 8
		setFormatLatLon(buffer, endLongitude);// 8
		setFormatLatLon(buffer, endLatitude);// 8
		buffer.putUnsigned(procedureStatus); // 1
		buffer.putUnsignedShort(requestCause); // 2
		buffer.putUnsigned(failureInterface); // 1
		buffer.putUnsigned(failureProcedureType); // 1
		buffer.putUnsignedShort(failureCause); // 2
		buffer.putUnsigned(keyword1); // 1
		buffer.putUnsigned(keyword2); // 1
		buffer.putUnsigned(keyword3); // 1
		buffer.putUnsigned(keyword4); // 1
		buffer.putUnsignedInt(enbId); // 4
		buffer.putUnsignedInt(cellId); // 4
		buffer.putUnsignedShort(mmeGroupId); // 2
		buffer.putUnsigned(mmeCode); // 1
		buffer.putUnsignedShort(tac); // 2
		buffer.put(getIp(userIpv4, 4));// 4
		buffer.put(getIp(userIpv6, 16));// 16
		buffer.putUnsignedInt(newEnbId); // 4
		buffer.putUnsignedInt(newCellId); // 4
		buffer.putUnsignedShort(newMmeGroupId); // 2
		buffer.putUnsigned(newMmeCode); // 1
		buffer.putUnsignedShort(newTac); // 2
		buffer.putUnsigned(epsBearerNumber); // 1
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < bearers.size(); i++) {
				buffer.putUnsigned(bearers.get(i).getBearerId());
				buffer.putUnsigned(bearers.get(i).getBearerType());
				buffer.putUnsigned(bearers.get(i).getBearerQCI());
				buffer.putUnsigned(bearers.get(i).getBearerStatus());
				buffer.putUnsignedShort(bearers.get(i).getBearRequestCause());
				buffer.putUnsignedShort(bearers.get(i).getBearFailureCause());
				buffer.putUnsignedInt(bearers.get(i).getBearerEnbGtpTeid());
				buffer.putUnsignedInt(bearers.get(i).getBearerSgwGtpTeid());
			}
		}
		buffer.putUnsigned(xdrNumber); // 1
		for (int i = 0; i < singles.size(); i++) {
			buffer.put(singles.get(i).toIobuffer());
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return (1 + 8 + 8 + 8 + 8 + 8 + 8 + 1 + 2 + 1 + 1+ 2 + 1+ 1+ 1+ 1 + 4 + 4 + 2 + 1 + 2
				+ 4 + 16 + 4 + 4 + 2 + 1 + 2 + 1)
				+ epsBearerNumber
				* EpsBearer.getBodyLength()
				+ 1
				+ xdrNumber
				* XdrSingle.getBodyLength();
	}

	public static class EpsBearer {
		short bearerId; // 1
		short bearerType; // 1
		short bearerQCI; // 1
		short bearerStatus; // 1
		int bearRequestCause;// 2
		int bearFailureCause;// 2
		long bearerEnbGtpTeid; // 4
		long bearerSgwGtpTeid; // 4

		public EpsBearer(short bearerId, short bearerType, short bearerQCI,
				short bearerStatus, int bearRequestCause, int bearFailureCause,
				long bearerEnbGtpTeid, long bearerSgwGtpTeid) {
			this.bearerId = bearerId;
			this.bearerType = bearerType;
			this.bearerQCI = bearerQCI;
			this.bearerStatus = bearerStatus;
			this.bearRequestCause = bearRequestCause;
			this.bearFailureCause = bearFailureCause;
			this.bearerEnbGtpTeid = bearerEnbGtpTeid;
			this.bearerSgwGtpTeid = bearerSgwGtpTeid;
		}

		public short getBearerId() {
			return bearerId;
		}

		public void setBearerId(short bearerId) {
			this.bearerId = bearerId;
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

		public long getBearerEnbGtpTeid() {
			return bearerEnbGtpTeid;
		}

		public void setBearerEnbGtpTeid(long bearerEnbGtpTeid) {
			this.bearerEnbGtpTeid = bearerEnbGtpTeid;
		}

		public long getBearerSgwGtpTeid() {
			return bearerSgwGtpTeid;
		}

		public void setBearerSgwGtpTeid(long bearerSgwGtpTeid) {
			this.bearerSgwGtpTeid = bearerSgwGtpTeid;
		}

		public int getBearRequestCause() {
			return bearRequestCause;
		}

		public void setBearRequestCause(int bearRequestCause) {
			this.bearRequestCause = bearRequestCause;
		}

		public int getBearFailureCause() {
			return bearFailureCause;
		}

		public void setBearFailureCause(int bearFailureCause) {
			this.bearFailureCause = bearFailureCause;
		}

		public static int getBodyLength() {
			return 1 + 1 + 1 + 1 + 2 + 2 + 4 + 4;
		}

		@Override
		public String toString() {
			return bearerId + _DELIMITER_ + bearerType + _DELIMITER_
					+ bearerQCI + _DELIMITER_ + bearerStatus + _DELIMITER_
					+ bearRequestCause + _DELIMITER_ + bearFailureCause
					+ _DELIMITER_ + bearerEnbGtpTeid + _DELIMITER_
					+ bearerSgwGtpTeid;
		}
	}

	/**
	 * 单接口信息信息
	 * 
	 * @author bieremayi
	 * 
	 */
	public static class XdrSingle implements Serializable {
		short Interface; // 1
		byte[] xdrId; // 16
		short procedureType; // 1
		Date startTime; // 8
		Date endTime; // 8
		double startlongitude; // 8
		double startlatitude; // 8
		double endlongitude; // 8
		double endlatitude; // 8
		short status; // 1
		int requestCause; // 2
		int failureCause; // 2

		public XdrSingle() {
			super();
		}

		public XdrSingle(short interface1, byte[] xdrId, short procedureType,
				Date startTime, Date endTime, double startlongitude,
				double startlatitude, double endlongitude, double endlatitude,
				short status, int requestCause, int failureCause) {
			Interface = interface1;
			this.xdrId = xdrId;
			this.procedureType = procedureType;
			this.startTime = startTime;
			this.endTime = endTime;
			this.startlongitude = startlongitude;
			this.startlatitude = startlatitude;
			this.endlongitude = endlongitude;
			this.endlatitude = endlatitude;
			this.status = status;
			this.requestCause = requestCause;
			this.failureCause = failureCause;
		}

		public short getInterface() {
			return Interface;
		}

		public void setInterface(short interface1) {
			Interface = interface1;
		}

		public byte[] getXdrId() {
			return xdrId;
		}

		public void setXdrId(byte[] xdrId) {
			this.xdrId = xdrId;
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

		public double getStartlongitude() {
			return startlongitude;
		}

		public void setStartlongitude(double startlongitude) {
			this.startlongitude = startlongitude;
		}

		public double getStartlatitude() {
			return startlatitude;
		}

		public void setStartlatitude(double startlatitude) {
			this.startlatitude = startlatitude;
		}

		public double getEndlongitude() {
			return endlongitude;
		}

		public void setEndlongitude(double endlongitude) {
			this.endlongitude = endlongitude;
		}

		public double getEndlatitude() {
			return endlatitude;
		}

		public void setEndlatitude(double endlatitude) {
			this.endlatitude = endlatitude;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
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

		public IoBuffer toIobuffer() {
			IoBuffer buffer = IoBuffer.allocate(getBodyLength());
			buffer.putUnsigned(Interface);// 1
			buffer.put(xdrId);// 16
			buffer.putUnsigned(procedureType); // 1
			buffer.putLong(startTime.getTime()); // 8
			buffer.putLong(endTime.getTime()); // 8
			setFormatLatLon(buffer, startlongitude);// 8
			setFormatLatLon(buffer, startlatitude);// 8
			setFormatLatLon(buffer, endlongitude);// 8
			setFormatLatLon(buffer, endlatitude);// 8
			buffer.putUnsigned(status);// 1
			buffer.putUnsignedShort(requestCause);//2
			buffer.putUnsignedShort(failureCause);//2
			buffer.flip();
			return buffer;
		}

		public static int getBodyLength() {
			return 1 + 16 + 1 + 8 + 8 + 8 + 8 + 8 + 8 + 1 + 2 +2;
		}

		@Override
		public String toString() {
			return Interface + _DELIMITER_ + Hex.encodeHexString(xdrId)
					+ _DELIMITER_ + procedureType + _DELIMITER_
					+ startTime.getTime() + _DELIMITER_ + endTime.getTime()
					+ _DELIMITER_ + startlongitude + _DELIMITER_
					+ startlatitude + _DELIMITER_ + endlongitude + _DELIMITER_
					+ endlatitude + _DELIMITER_ + status + _DELIMITER_ + requestCause+ _DELIMITER_ + failureCause;
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

	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public short getProcedureStatus() {
		return procedureStatus;
	}

	public void setProcedureStatus(short procedureStatus) {
		this.procedureStatus = procedureStatus;
	}

	public short getFailureInterface() {
		return failureInterface;
	}

	public void setFailureInterface(short failureInterface) {
		this.failureInterface = failureInterface;
	}

	public int getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(int failureCause) {
		this.failureCause = failureCause;
	}

	public int getRequestCause() {
		return requestCause;
	}

	public void setRequestCause(int requestCause) {
		this.requestCause = requestCause;
	}

	public short getFailureProcedureType() {
		return failureProcedureType;
	}

	public void setFailureProcedureType(short failureProcedureType) {
		this.failureProcedureType = failureProcedureType;
	}

	public short getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(short keyword1) {
		this.keyword1 = keyword1;
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

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	public long getCellId() {
		return cellId;
	}

	public void setCellId(long cellId) {
		this.cellId = cellId;
	}

	public int getMmeGroupId() {
		return mmeGroupId;
	}

	public void setMmeGroupId(int mmeGroupId) {
		this.mmeGroupId = mmeGroupId;
	}

	public short getMmeCode() {
		return mmeCode;
	}

	public void setMmeCode(short mmeCode) {
		this.mmeCode = mmeCode;
	}

	public int getTac() {
		return tac;
	}

	public void setTac(int tac) {
		this.tac = tac;
	}

	public String getUserIpv4() {
		return userIpv4;
	}

	public void setUserIpv4(String userIpv4) {
		this.userIpv4 = userIpv4;
	}

	public String getUserIpv6() {
		return userIpv6;
	}

	public void setUserIpv6(String userIpv6) {
		this.userIpv6 = userIpv6;
	}

	public long getNewEnbId() {
		return newEnbId;
	}

	public void setNewEnbId(long newEnbId) {
		this.newEnbId = newEnbId;
	}

	public long getNewCellId() {
		return newCellId;
	}

	public void setNewCellId(long newCellId) {
		this.newCellId = newCellId;
	}

	public int getNewMmeGroupId() {
		return newMmeGroupId;
	}

	public void setNewMmeGroupId(int newMmeGroupId) {
		this.newMmeGroupId = newMmeGroupId;
	}

	public short getNewMmeCode() {
		return newMmeCode;
	}

	public void setNewMmeCode(short newMmeCode) {
		this.newMmeCode = newMmeCode;
	}

	public int getNewTac() {
		return newTac;
	}

	public void setNewTac(int newTac) {
		this.newTac = newTac;
	}

	public short getEpsBearerNumber() {
		return epsBearerNumber;
	}

	public void setEpsBearerNumber(short epsBearerNumber) {
		this.epsBearerNumber = epsBearerNumber;
	}

	public List<XdrCompoundSourceSignaling.EpsBearer> getBearers() {
		return bearers;
	}

	public void setBearers(List<XdrCompoundSourceSignaling.EpsBearer> bearers) {
		this.bearers = bearers;
	}

	public short getXdrNumber() {
		return xdrNumber;
	}

	public void setXdrNumber(short xdrNumber) {
		this.xdrNumber = xdrNumber;
	}

	public List<XdrCompoundSourceSignaling.XdrSingle> getSingles() {
		return singles;
	}

	public void setSingles(List<XdrCompoundSourceSignaling.XdrSingle> singles) {
		this.singles = singles;
	}

}
