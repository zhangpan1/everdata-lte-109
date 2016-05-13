package com.eversec.lte.model.compound;

import static com.eversec.lte.utils.FormatUtils.setFormatLatLon;
import static com.eversec.lte.utils.FormatUtils.getIp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.utils.FormatUtils;

/**
 * 合成业务XDR
 */
@SuppressWarnings("serial")
public class XdrCompoundSourceApp extends XdrCompoundSource {
	int appType;// 2
	int appSubType;// 2
	Date startTime;// 8
	Date endTime;// 8
	double startLongitude;// 8
	double startLatitude;// 8
	double endLongitude;// 8
	double endLatitude;// 8
	String userIPv4;// 4
	String userIPv6;// 16
	short xdrNumber;// 1（默认0）

	List<XdrCompoundSourceApp.XdrApplicationCell> cells = new ArrayList<XdrCompoundSourceApp.XdrApplicationCell>();

	public XdrCompoundSourceApp() {
		super();
	}

	public XdrCompoundSourceApp(int appType, int appSubType, Date startTime,
			Date endTime, double startLongitude, double startLatitude,
			double endLongitude, double endLatitude, String userIPv4,
			String userIPv6, short xdrNumber, List<XdrApplicationCell> cells) {
		super();
		this.appType = appType;
		this.appSubType = appSubType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startLongitude = startLongitude;
		this.startLatitude = startLatitude;
		this.endLongitude = endLongitude;
		this.endLatitude = endLatitude;
		this.userIPv4 = userIPv4;
		this.userIPv6 = userIPv6;
		this.xdrNumber = xdrNumber;
		this.cells = cells;
	}

	public List<XdrApplicationCell> getCells() {
		return cells;
	}

	public void setCells(List<XdrApplicationCell> cells) {
		this.cells = cells;
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
		buffer.putUnsignedShort(appType);// 2
		buffer.putUnsignedShort(appSubType);// 2
		buffer.putLong(startTime.getTime());// 8
		buffer.putLong(endTime.getTime());// 8
		setFormatLatLon(buffer, startLongitude);// 8
		setFormatLatLon(buffer, startLatitude);// 8
		setFormatLatLon(buffer, endLongitude);// 8
		setFormatLatLon(buffer, endLatitude);// 8
		buffer.put(getIp(userIPv4, 4));// 4
		buffer.put(getIp(userIPv6, 16));// 16
		buffer.putUnsigned(xdrNumber); // 1
		if (xdrNumber > 0 && xdrNumber <= SdtpConstants.MAX_UNSIGNED_BYTE) {
			for (int i = 0; i < cells.size(); i++) {
				buffer.put(cells.get(i).toIobuffer());
			}
		}
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 2 + 2 + 8 + 8 + 8 + 8 + 8 + 8 + 4 + 16 + 1 + xdrNumber
				* XdrApplicationCell.getBodyLength();
	}

	@Override
	public String toString() {
		return appType + "|" + appSubType + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + startLongitude + "|"
				+ startLatitude + "|" + endLongitude + "|" + endLatitude + "|"
				+ userIPv4 + "|" + userIPv6 + "|" + xdrNumber + "|"
				+ FormatUtils.listToString(cells);
	}

	public static class XdrApplicationCell {
		byte[] xdrID;// 16
		Date startTime;// 8
		Date endTime;// 8
		double startLongitude;// 8
		double startLatitude;// 8
		double endLongitude;// 8
		double endLatitude;// 8
		long enbID;// 4
		long cellID;// 4
		long enbGtpTeid;// 4
		long sgwGtpTeid;// 4
		long uLData;// 4
		long dLData;// 4

		public XdrApplicationCell() {
		}

		public XdrApplicationCell(byte[] xdrID, Date startTime, Date endTime,
				double startLongitude, double startLatitude,
				double endLongitude, double endLatitude, long enbID,
				long cellID, long enbGtpTeid, long sgwGtpTeid, long uLData,
				long dLData) {
			super();
			this.xdrID = xdrID;
			this.startTime = startTime;
			this.endTime = endTime;
			this.startLongitude = startLongitude;
			this.startLatitude = startLatitude;
			this.endLongitude = endLongitude;
			this.endLatitude = endLatitude;
			this.enbID = enbID;
			this.cellID = cellID;
			this.enbGtpTeid = enbGtpTeid;
			this.sgwGtpTeid = sgwGtpTeid;
			this.uLData = uLData;
			this.dLData = dLData;
		}

		@Override
		public String toString() {
			return Hex.encodeHexString(xdrID) + _DELIMITER_
					+ startTime.getTime() + _DELIMITER_ + endTime.getTime()
					+ _DELIMITER_ + startLongitude + _DELIMITER_
					+ startLatitude + _DELIMITER_ + endLongitude + _DELIMITER_
					+ endLatitude + _DELIMITER_ + enbID + _DELIMITER_ + cellID
					+ _DELIMITER_ + enbGtpTeid + _DELIMITER_ + sgwGtpTeid
					+ _DELIMITER_ + uLData + _DELIMITER_ + dLData;
		}

		public IoBuffer toIobuffer() {
			IoBuffer buffer = IoBuffer.allocate(getBodyLength());
			buffer.put(xdrID);// 16
			buffer.putLong(startTime.getTime());// 8
			buffer.putLong(endTime.getTime());// 8
			setFormatLatLon(buffer, startLongitude);// 8
			setFormatLatLon(buffer, startLatitude);// 8
			setFormatLatLon(buffer, endLongitude);// 8
			setFormatLatLon(buffer, endLatitude);// 8
			buffer.putUnsignedInt(enbID);// 4
			buffer.putUnsignedInt(cellID);// 4
			buffer.putUnsignedInt(enbGtpTeid);// 4
			buffer.putUnsignedInt(sgwGtpTeid);// 4
			buffer.putUnsignedInt(uLData);// 4
			buffer.putUnsignedInt(dLData);// 4
			buffer.flip();
			return buffer;
		}

		public static int getBodyLength() {
			return 16 + 8 + 8 + 8 + 8 + 8 + 8 + 4 + 4 + 4 + 4 + 4 + 4;
		}

		public byte[] getXdrID() {
			return xdrID;
		}

		public void setXdrID(byte[] xdrID) {
			this.xdrID = xdrID;
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

		public long getEnbGtpTeid() {
			return enbGtpTeid;
		}

		public void setEnbGtpTeid(long enbGtpTeid) {
			this.enbGtpTeid = enbGtpTeid;
		}

		public long getSgwGtpTeid() {
			return sgwGtpTeid;
		}

		public void setSgwGtpTeid(long sgwGtpTeid) {
			this.sgwGtpTeid = sgwGtpTeid;
		}

		public long getuLData() {
			return uLData;
		}

		public void setuLData(long uLData) {
			this.uLData = uLData;
		}

		public long getdLData() {
			return dLData;
		}

		public void setdLData(long dLData) {
			this.dLData = dLData;
		}

	}
}
