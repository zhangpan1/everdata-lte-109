package com.eversec.lte.model.compound;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import static com.eversec.lte.utils.FormatUtils.*;

/**
 * 合成MR信息
 * 
 */
@SuppressWarnings("serial")
public class XdrCompoundSourceUEMR extends XdrCompoundSource {

	double longitude; // 8
	double latitude;// 8
	long enbId; // 4
	long cellId; // 4
	Date dateTime;// 8
	short mrType;// 1
	short phr;// 1
	int enbReceivedPower;// 2
	short ulSinr;// 1
	int servingFreq;// 2
	short servingRsrp;// 1
	short servingRsrq;// 1
	short neighborCellNumber;// 1
	List<XdrCompoundSourceUEMR.XdrNeighborCell> neighbors = new ArrayList<XdrCompoundSourceUEMR.XdrNeighborCell>();

	public XdrCompoundSourceUEMR() {
	}

	public XdrCompoundSourceUEMR(double longitude, double latitude, long enbId,
			long cellId, Date dateTime, short mrType, short phr,
			int enbReceivedPower, short ulSinr, int servingFreq,
			short servingRsrp, short servingRsrq, short neighborCellNumber,
			List<XdrNeighborCell> neighbors) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.enbId = enbId;
		this.cellId = cellId;
		this.dateTime = dateTime;
		this.mrType = mrType;
		this.phr = phr;
		this.enbReceivedPower = enbReceivedPower;
		this.ulSinr = ulSinr;
		this.servingFreq = servingFreq;
		this.servingRsrp = servingRsrp;
		this.servingRsrq = servingRsrq;
		this.neighborCellNumber = neighborCellNumber;
		this.neighbors = neighbors;
	}

	public static class XdrNeighborCell {
		int cellId;// 2
		int freq;// 2
		short rsrp;// 1
		short rsrq;// 1

		public XdrNeighborCell(int cellId, int freq, short rsrp, short rsrq) {
			super();
			this.cellId = cellId;
			this.freq = freq;
			this.rsrp = rsrp;
			this.rsrq = rsrq;
		}

		public int getCellId() {
			return cellId;
		}

		public void setCellId(int cellId) {
			this.cellId = cellId;
		}

		public int getFreq() {
			return freq;
		}

		public void setFreq(int freq) {
			this.freq = freq;
		}

		public short getRsrp() {
			return rsrp;
		}

		public void setRsrp(short rsrp) {
			this.rsrp = rsrp;
		}

		public short getRsrq() {
			return rsrq;
		}

		public void setRsrq(short rsrq) {
			this.rsrq = rsrq;
		}

		@Override
		public String toString() {
			return cellId + _DELIMITER_ + freq + _DELIMITER_ + rsrp
					+ _DELIMITER_ + rsrq;
		}

	}

	public List<XdrNeighborCell> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<XdrNeighborCell> neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public int getMemoryBytes() {
		return toString().getBytes().length
				+ common.toString().getBytes().length + 1;
	}

	@Override
	public String toString() {
		return longitude + "|" + latitude + "|" + enbId + "|" + cellId + "|"
				+ dateTime.getTime() + "|" + mrType + "|" + phr + "|"
				+ enbReceivedPower + "|" + ulSinr + "|" + servingFreq + "|"
				+ servingRsrp + "|" + servingRsrq + "|" + neighborCellNumber
				+ "|" + listToString(neighbors);
	}

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		setFormatLatLon(buffer, longitude);// 8
		setFormatLatLon(buffer, latitude);// 8
		buffer.putUnsignedInt(enbId); // 4
		buffer.putUnsignedInt(cellId); // 4
		buffer.putLong(dateTime.getTime());// 8
		buffer.putUnsigned(mrType);// 1
		buffer.putUnsigned(phr);// 1
		buffer.putUnsignedShort(enbReceivedPower);// 2
		buffer.putUnsigned(ulSinr);// 1
		buffer.putUnsignedShort(servingFreq);// 2
		buffer.putUnsigned(servingRsrp);// 1
		buffer.putUnsigned(servingRsrq);// 1
		buffer.putUnsigned(neighborCellNumber);// 1
		if (neighborCellNumber > 0 && neighborCellNumber < 255) {
			for (int i = 0; i < neighbors.size(); i++) {
				buffer.putUnsignedShort(neighbors.get(i).getCellId());
				buffer.putUnsignedShort(neighbors.get(i).getFreq());
				buffer.putUnsigned(neighbors.get(i).getRsrp());
				buffer.putUnsigned(neighbors.get(i).getRsrq());
			}
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {

		return (8 + 8 + 4 + 4 + 8 + 1 + 1 + 2 + 1 + 2 + 1 + 1 + 1)
				+ neighborCellNumber * 6;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
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

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public short getMrType() {
		return mrType;
	}

	public void setMrType(short mrType) {
		this.mrType = mrType;
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

}
