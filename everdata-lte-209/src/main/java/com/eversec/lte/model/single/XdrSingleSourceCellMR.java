package com.eversec.lte.model.single;

import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

@SuppressWarnings("serial")
public class XdrSingleSourceCellMR extends XdrSingleSource {
	long enbID;//4
	long cellID;// 4
	Date time;// 8
	byte[] enbReceivedInterfere;// 20
	byte[] ulPacketLoss;// 9
	byte[] dlpacketLoss;// 9

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsignedInt(enbID);
		buffer.putUnsignedInt(cellID);
		buffer.putLong(time.getTime());
		buffer.put(enbReceivedInterfere); 
		buffer.put(ulPacketLoss);
		buffer.put(dlpacketLoss);
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 4 + 4 + 8 + 20 + 9 + 9;
	}

	public XdrSingleSourceCellMR(long enbID, long cellID, Date time,
			byte[] enbReceivedInterfere, byte[] ulPacketLoss,
			byte[] dlpacketLoss) {
		super();
		this.enbID = enbID;
		this.cellID = cellID;
		this.time = time;
		this.enbReceivedInterfere = enbReceivedInterfere;
		this.ulPacketLoss = ulPacketLoss;
		this.dlpacketLoss = dlpacketLoss;
	}

	public XdrSingleSourceCellMR() {
		super();
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

	public byte[] getEnbReceivedInterfere() {
		return enbReceivedInterfere;
	}

	public void setEnbReceivedInterfere(byte[] enbReceivedInterfere) {
		this.enbReceivedInterfere = enbReceivedInterfere;
	}

	public byte[] getUlPacketLoss() {
		return ulPacketLoss;
	}

	public void setUlPacketLoss(byte[] ulPacketLoss) {
		this.ulPacketLoss = ulPacketLoss;
	}

	public byte[] getDlpacketLoss() {
		return dlpacketLoss;
	}

	public void setDlpacketLoss(byte[] dlpacketLoss) {
		this.dlpacketLoss = dlpacketLoss;
	}

	@Override
	public String toString() {
		return  enbID  + "|" + cellID + "|" + time.getTime()
				+ "|" + Hex.encodeHexString(enbReceivedInterfere) + "|"
				+ Hex.encodeHexString(ulPacketLoss) + "|"
				+ Hex.encodeHexString(dlpacketLoss);
	}
}
