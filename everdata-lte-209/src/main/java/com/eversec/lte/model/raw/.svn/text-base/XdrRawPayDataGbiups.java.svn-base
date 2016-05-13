package com.eversec.lte.model.raw;

import org.apache.commons.codec.binary.Hex;

public class XdrRawPayDataGbiups extends XdrRawPayData {

	 

	private static final long serialVersionUID = 1L;

	byte[] cdrid;// 8
	int len;// Len 2
	short ver;// 1Ver 1
	short linkType;// 1LinkType 1
	short cardType;// 1CardType 1
	short cardID;// 1CardID 1
	long timeOffset;// 4TimeOffset 4
	long time;// 4Time 4
	long time2;// 4Time2 4
	int sequenceNum;// Sequence 2
	byte[] load1;

	public XdrRawPayDataGbiups(byte[] cdrid, int len, short ver,
			short linkType, short cardType, short cardID, long timeOffset,
			long time, long time2, int sequenceNum, byte[] load1) {
		super((short)0, (short)0, null, 0, 0, null);
		this.cdrid = cdrid;
		this.len = len;
		this.ver = ver;
		this.linkType = linkType;
		this.cardType = cardType;
		this.cardID = cardID;
		this.timeOffset = timeOffset;
		this.time = time;
		this.time2 = time2;
		this.sequenceNum = sequenceNum;
		this.load1 = load1;
	}

	@Override
	public String toString() {
		return "XdrRawPayDataGbiups [cdrid=" + Hex.encodeHexString(cdrid)
				+ ", len=" + len + ", ver=" + ver + ", linkType=" + linkType
				+ ", cardType=" + cardType + ", cardID=" + cardID
				+ ", timeOffset=" + timeOffset + ", time=" + time + ", time2="
				+ time2 + ", sequenceNum=" + sequenceNum + ", load1=" + "("
				+ load1.length + ")" + "]";
	}

}
