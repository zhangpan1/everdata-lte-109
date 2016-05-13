package com.eversec.lte.model.single;

import static com.eversec.lte.utils.FormatUtils.listToString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import cern.colt.Arrays;

import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;
import com.eversec.lte.utils.FormatUtils;

@SuppressWarnings("serial")
public class XdrSingleSourceUu extends XdrSingleSource {
	short procedureType;// 1
	Date startTime;// 8
	Date endTime;// 8
	short keyword1;// 1
	short keyword2;// 1
	short procedureStatus;// 1
	byte[] plmnID;// 3
	long enbID;// 4
	long cellID;// 4
	int c_rnti;// 2
	long targetCellId;// 4
	long targetEnbId;// 4
	int targetC_rnti;// 2
	long mmeUeS1apId;// 4
	int mmeGroupID;// 2
	short mmeCode;// 1
	long m_tmsi;// 4
	short csfbIndication;// 1
	short redirectedNetwork; // 1
	short epsBearerNumber;// 1

	List<XdrSingleSourceUu.Bearer> bearers = new ArrayList<XdrSingleSourceUu.Bearer>();

	public XdrSingleSourceUu(short procedureType, Date startTime, Date endTime,
			short keyword1, short keyword2, short procedureStatus,
			byte[] plmnID, long enbID, long cellID, int c_rnti,
			long targetEnbId, long targetCellId, int targetC_rnti,
			long mmeUeS1apId, int mmeGroupID, short mmeCode, long m_tmsi,
			short csfbIndication, short redirectedNetwork,
			short epsBearerNumber, List<Bearer> bearers) {
		this.procedureType = procedureType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.procedureStatus = procedureStatus;
		this.plmnID = plmnID;
		this.enbID = enbID;
		this.cellID = cellID;
		this.c_rnti = c_rnti;
		this.targetEnbId = targetEnbId;
		this.targetCellId = targetCellId;
		this.targetC_rnti = targetC_rnti;
		this.mmeUeS1apId = mmeUeS1apId;
		this.mmeGroupID = mmeGroupID;
		this.mmeCode = mmeCode;
		this.m_tmsi = m_tmsi;
		this.csfbIndication = csfbIndication;
		this.redirectedNetwork = redirectedNetwork;
		this.epsBearerNumber = epsBearerNumber;
		this.bearers = bearers;
	}

	public static class Bearer {
		short bearerID;// 1
		short bearerStatus;// 1

		@Override
		public String toString() {
			return bearerID + _DELIMITER_ + bearerStatus;
		}

		public Bearer(short bearerID, short bearerStatus) {
			super();
			this.bearerID = bearerID;
			this.bearerStatus = bearerStatus;
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

	public short getProcedureStatus() {
		return procedureStatus;
	}

	public void setProcedureStatus(short procedureStatus) {
		this.procedureStatus = procedureStatus;
	}

	public byte[] getPlmnID() {
		return plmnID;
	}

	public void setPlmnID(byte[] plmnID) {
		this.plmnID = plmnID;
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

	public int getC_rnti() {
		return c_rnti;
	}

	public void setC_rnti(int c_rnti) {
		this.c_rnti = c_rnti;
	}

	public long getTargetEnbId() {
		return targetEnbId;
	}

	public void setTargetEnbId(long targetEnbId) {
		this.targetEnbId = targetEnbId;
	}

	public long getTargetCellId() {
		return targetCellId;
	}

	public void setTargetCellId(long targetCellId) {
		this.targetCellId = targetCellId;
	}

	public int getTargetC_rnti() {
		return targetC_rnti;
	}

	public void setTargetC_rnti(int targetC_rnti) {
		this.targetC_rnti = targetC_rnti;
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

	public long getM_tmsi() {
		return m_tmsi;
	}

	public void setM_tmsi(long m_tmsi) {
		this.m_tmsi = m_tmsi;
	}

	public short getCsfbIndication() {
		return csfbIndication;
	}

	public void setCsfbIndication(short csfbIndication) {
		this.csfbIndication = csfbIndication;
	}

	public short getRedirectedNetwork() {
		return redirectedNetwork;
	}

	public void setRedirectedNetwork(short redirectedNetwork) {
		this.redirectedNetwork = redirectedNetwork;
	}

	public short getEpsBearerNumber() {
		return epsBearerNumber;
	}

	public void setEpsBearerNumber(short epsBearerNumber) {
		this.epsBearerNumber = epsBearerNumber;
	}

	public List<XdrSingleSourceUu.Bearer> getBearers() {
		return bearers;
	}

	public void setBearers(List<XdrSingleSourceUu.Bearer> bearers) {
		this.bearers = bearers;
	}

	@Override
	public String toString() {
		String bearStr = (bearers.size() == 0)?"":("|"+ listToString(bearers)) ;
		return procedureType + "|" + startTime.getTime() + "|"
				+ endTime.getTime() + "|" + keyword1 + "|" + keyword2 + "|"
				+ procedureStatus + "|" + Hex.encodeHexString(plmnID) + "|"
				+ enbID + "|" + cellID + "|" + c_rnti + "|" + targetEnbId + "|"
				+ targetCellId + "|" + targetC_rnti + "|" + mmeUeS1apId + "|"
				+ mmeGroupID + "|" + mmeCode + "|" + m_tmsi + "|"
				+ csfbIndication + "|" + redirectedNetwork + "|"
				+ epsBearerNumber + bearStr;
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
		buffer.putUnsigned(keyword1);// 1
		buffer.putUnsigned(keyword2);// 1
		buffer.putUnsigned(procedureStatus);// 1
		if (plmnID.length != 3) {
			plmnID = FormatUtils.createAllFBytes(3);
		}
		buffer.put(plmnID);// 3
		buffer.putUnsignedInt(enbID);// 4
		buffer.putUnsignedInt(cellID);// 4
		buffer.putUnsignedShort(c_rnti);// 2
		buffer.putUnsignedInt(targetEnbId);// 4
		buffer.putUnsignedInt(targetCellId);// 4
		buffer.putUnsignedShort(targetC_rnti);// 2
		buffer.putUnsignedInt(mmeUeS1apId);// 4
		buffer.putUnsignedShort(mmeGroupID);// 2
		buffer.putUnsigned(mmeCode);// 1
		buffer.putUnsignedInt(m_tmsi);// 4
		buffer.putUnsigned(csfbIndication);// 1
		buffer.putUnsigned(redirectedNetwork);// 1
		buffer.putUnsigned(epsBearerNumber);// 1
		if (epsBearerNumber > 0 && epsBearerNumber != 0xff) {
			for (int i = 0; i < bearers.size(); i++) {
				Bearer bearer = bearers.get(i);
				short bearerID = bearer.getBearerID();
				short bearerStatus = bearer.getBearerStatus();
				buffer.putUnsigned(bearerID);// 1
				buffer.putUnsigned(bearerStatus);// 1
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
		return 1 + 8 + 8 + 1 + 1 +1 + 3 + 4 + 4 + 2 + 4 + 4 + 2 + 4 + 2 + 1 + 4
				+ 1 + 1 +1 + len;
	}

//	public static long hexToLong(String xdrId) {
//		try {
//			String b = xdrId;// "000000000000000054d444c0cc627f18";
//			b = b.substring(16);
//			// System.out.println(b);
//			byte[] bb = Hex.decodeHex(b.toCharArray());
//			BigInteger bigInteger2 = new BigInteger(bb);
//			// System.out.println(bigInteger2);
//			return bigInteger2.longValue();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0L;
//		}
//	}
//
//	public static String longToHex(long num) {
//		BigInteger bigInteger = new BigInteger(num + "");
//		return "0000000000000000"
//				+ String.valueOf(Hex.encodeHex(bigInteger.toByteArray()));
//	}
//
//	public static List<XdrSingleSourceUu> getUuXdrList(String file)
//			throws IOException, NumberFormatException, DecoderException {
//		List<XdrSingleSourceUu> rets = new ArrayList<>();
//		BufferedReader reader = new BufferedReader(new FileReader(
//				new File(file)));
//		String line = null;
//		while ((line = reader.readLine()) != null) {
//			String[] arrs = line.split("\\|", 100);
//			int length = Integer.valueOf(arrs[0]);
//			String city = arrs[1];
//			short Interface1 = Short.valueOf(arrs[2]);
//			byte[] xdrId = Hex.decodeHex(longToHex(Long.valueOf(arrs[3]))
//					.toCharArray());
//			short rat = Short.valueOf(arrs[4]);
//			String imsi = arrs[5];
//			String imei = arrs[6];
//			String msisdn = arrs[7];
//			XdrSingleCommon common = new XdrSingleCommon(length, city,
//					Interface1, xdrId, rat, imsi, imei, msisdn);
//			short procedureType = Short.valueOf(arrs[8]);// 1
//			Date startTime = new Date(Long.valueOf(arrs[9]));// 8
//			Date endTime = new Date(Long.valueOf(arrs[10]));// 8
//			short keyword1 = Short.valueOf(arrs[11]);// 1
//			short procedureStatus = Short.valueOf(arrs[12]);// 1
//			byte[] plmnID = new BigInteger(arrs[13]).toByteArray();// 3（2.0.7）
//			long cellID = Long.valueOf(arrs[14]);// 3
//			byte[] enbID = new BigInteger(arrs[15]).toByteArray();// 4（2.0.7）
//			int c_rnti = Integer.valueOf(arrs[16]);// 2
//			byte[] targetEnbId = new BigInteger(arrs[17]).toByteArray();// 3（2.0.7）
//			long targetCellId = Long.valueOf(arrs[18]);// 4
//			int targetC_rnti = Integer.valueOf(arrs[19]);// 2
//			long mmeUeS1apId = Long.valueOf(arrs[20]);// 4
//			int mmeGroupID = Integer.valueOf(arrs[21]);// 2
//			short mmeCode = Short.valueOf(arrs[22]);// 1
//			long m_tmsi = Long.valueOf(arrs[23]);// 4
//			short csfbIndication = Short.valueOf(arrs[24]);// 1
//			short epsBearerNumber = Short.valueOf(arrs[25]);// 1
//			List<XdrSingleSourceUu.Bearer> bearers = new ArrayList<>();
//			if (arrs.length == 27) {
//				String bearerStr = arrs[26];
//				String[] bears = bearerStr.split(",");
//				for (int i = 0; i < bears.length; i += 2) {
//					XdrSingleSourceUu.Bearer b = new XdrSingleSourceUu.Bearer(
//							Short.valueOf(bears[i]),
//							Short.valueOf(bears[i + 1]));
//					bearers.add(b);
//				}
//			}
//			XdrSingleSourceUu uu = new XdrSingleSourceUu(procedureType,
//					startTime, endTime, keyword1, procedureStatus, plmnID,
//					enbID, cellID, c_rnti, targetEnbId, targetCellId,
//					targetC_rnti, mmeUeS1apId, mmeGroupID, mmeCode, m_tmsi,
//					csfbIndication, epsBearerNumber, bearers);
//			uu.setCommon(common);
//			rets.add(uu);
//		}
//		reader.close();
//		return rets;
//	}
//
//	public static void main(String[] args) throws IOException,
//			NumberFormatException, DecoderException {
//		List<XdrSingleSourceUu> list = getUuXdrList("D://371_0003_UU_20150206122400_00000.csv");
//		for (XdrSingleSourceUu uu : list) {
//			System.out.println(uu);
//			System.out.println(Arrays.toString(uu.toByteArray()));
//
//			IoBuffer buffer = IoBuffer.allocate(uu.getCommon().getBodyLength()
//					+ uu.getBodyLength());
//			buffer.put(uu.getCommon().toByteArray());
//			buffer.put(uu.toByteArray());
//			buffer.flip();
//			new XdrSingleBytesDecoder().decode(buffer);
//		}
//	}
}
