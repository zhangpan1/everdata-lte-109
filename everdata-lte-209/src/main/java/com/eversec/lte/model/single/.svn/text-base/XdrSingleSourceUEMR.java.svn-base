package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.listToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

@SuppressWarnings("serial")
public class XdrSingleSourceUEMR extends XdrSingleSource {
	int mmeGroupId;//2
	short mmeCode;//1
	long mmeUeS1apId;// 4
	long enbID;// 4
	long cellID;// 4
	Date time;// 8
	short MRType;// 1
	short phr;// 1
	int enbReceivedPower;// 2
	short ulSinr;// 1
	int ta;// 2
	int aoa;// 2
	int servingFreq;// 2
	short servingRsrp;// 1
	short servingRsrq;// 1
	short neighborCellNumber;// 1

	List<XdrSingleSourceUEMR.Neighbor> neighbors = new ArrayList<XdrSingleSourceUEMR.Neighbor>();

	 

	public XdrSingleSourceUEMR(int mmeGroupId, short mmeCode, long mmeUeS1apId,
			long enbID, long cellID, Date time, short mRType, short phr,
			int enbReceivedPower, short ulSinr, int ta, int aoa,
			int servingFreq, short servingRsrp, short servingRsrq,
			short neighborCellNumber, List<Neighbor> neighbors) {
		this.mmeGroupId = mmeGroupId;
		this.mmeCode = mmeCode;
		this.mmeUeS1apId = mmeUeS1apId;
		this.enbID = enbID;
		this.cellID = cellID;
		this.time = time;
		this.MRType = mRType;
		this.phr = phr;
		this.enbReceivedPower = enbReceivedPower;
		this.ulSinr = ulSinr;
		this.ta = ta;
		this.aoa = aoa;
		this.servingFreq = servingFreq;
		this.servingRsrp = servingRsrp;
		this.servingRsrq = servingRsrq;
		this.neighborCellNumber = neighborCellNumber;
		this.neighbors = neighbors;
	}

	public long getMmeUeS1apId() {
		return mmeUeS1apId;
	}

	public void setMmeUeS1apId(long mmeUeS1apId) {
		this.mmeUeS1apId = mmeUeS1apId;
	}

	public long getEnbID() {
		return enbID;
	}

	public void setEnbID(long enbID) {
		this.enbID = enbID;
	}

	public long getCellID() {
		return cellID;
	}

	public void setCellID(long cellID) {
		this.cellID = cellID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public short getMRType() {
		return MRType;
	}

	public void setMRType(short mRType) {
		MRType = mRType;
	}

	public short getPhr() {
		return phr;
	}

	public void setPhr(short phr) {
		this.phr = phr;
	}

	public int getEnbReceivedPower() {
		return enbReceivedPower;
	}

	public void setEnbReceivedPower(int enbReceivedPower) {
		this.enbReceivedPower = enbReceivedPower;
	}

	public short getUlSinr() {
		return ulSinr;
	}

	public void setUlSinr(short ulSinr) {
		this.ulSinr = ulSinr;
	}

	public int getTa() {
		return ta;
	}

	public void setTa(int ta) {
		this.ta = ta;
	}

	public int getAoa() {
		return aoa;
	}

	public void setAoa(int aoa) {
		this.aoa = aoa;
	}

	public int getServingFreq() {
		return servingFreq;
	}

	public void setServingFreq(int servingFreq) {
		this.servingFreq = servingFreq;
	}

	public short getServingRsrp() {
		return servingRsrp;
	}

	public void setServingRsrp(short servingRsrp) {
		this.servingRsrp = servingRsrp;
	}

	public short getServingRsrq() {
		return servingRsrq;
	}

	public void setServingRsrq(short servingRsrq) {
		this.servingRsrq = servingRsrq;
	}

	public short getNeighborCellNumber() {
		return neighborCellNumber;
	}

	public void setNeighborCellNumber(short neighborCellNumber) {
		this.neighborCellNumber = neighborCellNumber;
	}

	public static class Neighbor {
		int neighborCellPci;// 2
		int neighborFreq;// 2
		short neighborRSRP;// 1
		short neighborRSRQ;// 1

		@Override
		public String toString() {
			return neighborCellPci + _DELIMITER_ + neighborFreq + _DELIMITER_ + neighborRSRP
					+ _DELIMITER_ + neighborRSRQ;
		}

		public Neighbor(int neighborCellPci, int neighborFreq,
				short neighborRSRP, short neighborRSRQ) {
			super();
			this.neighborCellPci = neighborCellPci;
			this.neighborFreq = neighborFreq;
			this.neighborRSRP = neighborRSRP;
			this.neighborRSRQ = neighborRSRQ;
		}

		public int getNeighborCellPci() {
			return neighborCellPci;
		}

		public void setNeighborCellPci(int neighborCellPci) {
			this.neighborCellPci = neighborCellPci;
		}

		public int getNeighborFreq() {
			return neighborFreq;
		}

		public void setNeighborFreq(int neighborFreq) {
			this.neighborFreq = neighborFreq;
		}

		public short getNeighborRSRP() {
			return neighborRSRP;
		}

		public void setNeighborRSRP(short neighborRSRP) {
			this.neighborRSRP = neighborRSRP;
		}

		public short getNeighborRSRQ() {
			return neighborRSRQ;
		}

		public void setNeighborRSRQ(short neighborRSRQ) {
			this.neighborRSRQ = neighborRSRQ;
		}

	}

	public List<XdrSingleSourceUEMR.Neighbor> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<XdrSingleSourceUEMR.Neighbor> neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public String toString() {
		String neighborStr = (neighbors.size() == 0)?"":("|"+ listToString(neighbors)) ;
		return   mmeGroupId + "|" + mmeCode + "|" +  mmeUeS1apId + "|"
				+  enbID  + "|" + cellID + "|" + time.getTime() + "|" + MRType
				+ "|" + phr + "|" + enbReceivedPower + "|" + ulSinr + "|" + ta
				+ "|" + aoa + "|" + servingFreq + "|" + servingRsrp + "|"
				+ servingRsrq + "|" + neighborCellNumber + neighborStr;
	}

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength()) ;
		buffer.putUnsignedShort(mmeGroupId);// 2
		buffer.putUnsigned(mmeCode);//1
		buffer.putUnsignedInt(mmeUeS1apId);// 4
		buffer.putUnsignedInt( enbID );// 4
		buffer.putUnsignedInt(cellID);// 4
		buffer.putLong(time.getTime());// 8
		buffer.putUnsigned(MRType);// 1
		buffer.putUnsigned(phr);// 1
		buffer.putUnsignedShort(enbReceivedPower);// 2
		buffer.putUnsigned(ulSinr);// 1
		buffer.putUnsignedShort(ta);// 2
		buffer.putUnsignedShort(aoa);// 2
		buffer.putUnsignedShort(servingFreq);// 2
		buffer.putUnsigned(servingRsrp);// 1
		buffer.putUnsigned(servingRsrq);// 1
		buffer.putUnsigned(neighborCellNumber);// 1
		if (neighborCellNumber > 0 && neighborCellNumber != 0xff) {
			for (int i = 0; i < neighborCellNumber; i++) {
				Neighbor neighbor = neighbors.get(i);

				int neighborCellPci = neighbor.getNeighborCellPci();// 2
				int neighborFreq = neighbor.getNeighborFreq();// 2
				short neighborRSRP = neighbor.getNeighborRSRP();// 1
				short neighborRSRQ = neighbor.getNeighborRSRQ();// 1

				buffer.putUnsignedShort(neighborCellPci);
				buffer.putUnsignedShort(neighborFreq);
				buffer.putUnsigned(neighborRSRP);
				buffer.putUnsigned(neighborRSRQ);

			}
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		int len = 0;
		if (neighborCellNumber != 0xFF) {
			len = neighborCellNumber * 6;
		}
		return 2+1+ 4 + 4 + 4 + 8 + 1 + 1 + 2 + 1 + 2 + 2 + 2 + 1 + 1 + 1 + len;
	}
}
