package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.listToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

@SuppressWarnings("serial")
public class XdrSingleSourceX2 extends XdrSingleSource {
	short procedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short procedureStatus;// 1
	long sourceCellId;// 4
	long targetCellId;// 4
	long sourceEnbId;// 4
	long targetEnbId;// 4
	long mmeUeS1apId;// 4
	int mmeGroupID;// 2
	short mmeCode;// 1
	int requestCause;// 2
	int failureCause;// 2
	short epsBearerNumber;// 1
	List<XdrSingleSourceX2.Bearer> bearers = new ArrayList<XdrSingleSourceX2.Bearer>();

	@Override
	public String toString() {
		String bearStr = (bearers.size() == 0)?"":("|"+ listToString(bearers)) ;
		return procedureType + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + procedureStatus + "|"
				+ sourceCellId + "|" + targetCellId + "|" + sourceEnbId + "|"
				+ targetEnbId + "|" + mmeUeS1apId + "|" + mmeGroupID + "|"
				+ mmeCode + "|" + requestCause + "|" + failureCause + "|"
				+ epsBearerNumber + bearStr ;
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
		buffer.putUnsignedInt(sourceCellId);// 4
		buffer.putUnsignedInt(targetCellId);// 4
		buffer.putUnsignedInt(sourceEnbId);// 4
		buffer.putUnsignedInt(targetEnbId);// 4
		buffer.putUnsignedInt(mmeUeS1apId);// 4
		buffer.putUnsignedShort(mmeGroupID);// 2
		buffer.putUnsigned(mmeCode);// 1
		buffer.putUnsignedShort(requestCause);// 2
		buffer.putUnsignedShort(failureCause);// 2
		buffer.putUnsigned(epsBearerNumber);// 1
		if (epsBearerNumber > 0 && epsBearerNumber != 0xff) {
			for (int i = 0; i < epsBearerNumber; i++) {
				Bearer bearer = bearers.get(i);
				short bearerID = bearer.getBearerID();
				short bearerStatus = bearer.getBearerStatus();
				buffer.putUnsigned(bearerID);
				buffer.putUnsigned(bearerStatus);
			}
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		int len = 0;
		if (epsBearerNumber != 0xFF) {
			len = epsBearerNumber * 2;
		}
		return 1 + 8 + 8 + 1 + 4 + 4 + 4 + 4 + 4 + 2 + 1 + 2 + 2 + 1 + len;
	}

	public XdrSingleSourceX2(short procedureType, Date startTime, Date endTime,
			short procedureStatus, long sourceCellId, long targetCellId,
			long sourceEnbId, long targetEnbId, long mmeUeS1apId,
			int mmeGroupID, short mmeCode, int requestCause, int failureCause,
			short epsBearerNumber, List<Bearer> bearers) {
		super();
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procedureStatus = procedureStatus;
		this.sourceCellId = sourceCellId;
		this.targetCellId = targetCellId;
		this.sourceEnbId = sourceEnbId;
		this.targetEnbId = targetEnbId;
		this.mmeUeS1apId = mmeUeS1apId;
		this.mmeGroupID = mmeGroupID;
		this.mmeCode = mmeCode;
		this.requestCause = requestCause;
		this.failureCause = failureCause;
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

	public long getSourceCellId() {
		return sourceCellId;
	}

	public void setSourceCellId(long sourceCellId) {
		this.sourceCellId = sourceCellId;
	}

	public long getTargetCellId() {
		return targetCellId;
	}

	public void setTargetCellId(long targetCellId) {
		this.targetCellId = targetCellId;
	}

	public long getSourceEnbId() {
		return sourceEnbId;
	}

	public void setSourceEnbId(long sourceEnbId) {
		this.sourceEnbId = sourceEnbId;
	}

	public long getTargetEnbId() {
		return targetEnbId;
	}

	public void setTargetEnbId(long targetEnbId) {
		this.targetEnbId = targetEnbId;
	}

	public long getMmeUeS1apId() {
		return mmeUeS1apId;
	}

	public void setMmeUeS1apId(long mmeUeS1apId) {
		this.mmeUeS1apId = mmeUeS1apId;
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

	public short getEpsBearerNumber() {
		return epsBearerNumber;
	}

	public void setEpsBearerNumber(short epsBearerNumber) {
		this.epsBearerNumber = epsBearerNumber;
	}

	public static class Bearer {
		short bearerID;// 1
		short bearerStatus;// 1

		public Bearer(short bearerID, short bearerStatus) {
			this.bearerID = bearerID;
			this.bearerStatus = bearerStatus;
		}

		@Override
		public String toString() {
			return bearerID + _DELIMITER_ + bearerStatus;
		}

		public short getBearerID() {
			return bearerID;
		}

		public void setBearerID(short bearerID) {
			this.bearerID = bearerID;
		}

		public short getBearerStatus() {
			return bearerStatus;
		}

		public void setBearerStatus(short bearerStatus) {
			this.bearerStatus = bearerStatus;
		}
	}

	public List<XdrSingleSourceX2.Bearer> getBearers() {
		return bearers;
	}

	public void setBearers(List<XdrSingleSourceX2.Bearer> bearers) {
		this.bearers = bearers;
	}

}
