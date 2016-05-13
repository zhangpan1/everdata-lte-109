package com.eversec.lte.processor.decoder;

import static com.eversec.lte.constant.SdtpConstants.XDRInterface.CELL_MR;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.GNC;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S10;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S11;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S1MME;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S1U;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S5S8;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S6A;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.SGS;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UE_MR;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UU;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.X2;
import static com.eversec.lte.utils.FormatUtils.TBCDFormat;
import static com.eversec.lte.utils.FormatUtils.getBytes;
import static com.eversec.lte.utils.FormatUtils.getDate;
import static com.eversec.lte.utils.FormatUtils.getIp;
import static com.eversec.lte.utils.FormatUtils.getString;
import static com.eversec.lte.utils.FormatUtils.getStringSuffixUnkownChar;
import static com.eversec.lte.utils.FormatUtils.getStringSuffixZero;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants.XDRType;
import com.eversec.lte.exception.UnkownProcessException;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceCellMR;
import com.eversec.lte.model.single.XdrSingleSourceGnC;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceS5S8C;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUEMR.Neighbor;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.model.single.s1u.XdrSingleS1UApp;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppDNS;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppEmail;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppFtp;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppHttp;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppIM;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppMMS;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppP2P;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppRTSP;
import com.eversec.lte.model.single.s1u.XdrSingleS1UAppVOIP;
import com.eversec.lte.model.single.s1u.XdrSingleS1UBusinessCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UMobileCommon;
import com.eversec.lte.processor.data.StaticData;
import com.eversec.lte.processor.statistics.StaticUtil;
import com.eversec.lte.utils.FormatUtils;

/**
 * xdr解码
 * 单接口解码
 * @author bieremayi
 * 
 */
public class XdrSingleBytesDecoder implements IDecoder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7186365365048913820L;

	private long scaOffsetMills = SdtpConfig.getScaOffsetMills();

	public List<XdrSingleSource> decode(IoBuffer buffer) {
		boolean first = true;
		List<XdrSingleSource> xdrs = new ArrayList<>();
		while (buffer.hasRemaining()) {
			if (first) {
				first = false;
			} else {
				if (SdtpConfig.IS_APPLICATION_LAYER) {
					buffer.getUnsigned();
				}
			}
			XdrSingleCommon common = null;
			try {
				common = getXdrSingleCommon(buffer);
				int Interface = common.getInterface();
				XdrSingleSource xdr = null;
				switch (Interface) {
				case UU:
					xdr = processUu(buffer, common);
					break;
				case X2:
					xdr = processX2(buffer, common);
					break;
				case UE_MR:
					xdr = processUEMR(buffer, common);
					break;
				case CELL_MR:
					xdr = processCellMR(buffer, common);
					break;
				case S1MME:
					xdr = processS1MME(buffer, common);
					break;
				case S6A:
					xdr = processS6a(buffer, common);
					break;
				case S11:
					xdr = processS10S11(buffer, common);
					break;
				case S10:
					xdr = processS10S11(buffer, common);
					break;
				case SGS:
					xdr = processSGs(buffer, common);
					break;
				case S5S8:
					xdr = processS5S8C(buffer, common);
					break;
				case S1U:
					xdr = processS1U(buffer, common);
					break;
				case GNC:
					xdr = processGnc(buffer, common);
					break;
				default:
					break;
				}
				if (xdr != null) {
					xdrs.add(xdr);
					StaticData.SOURCE_COUNT.getAndIncrement();
					StaticUtil.statXdrType(Interface);
				}
			} catch (Exception e) {
				throw new UnkownProcessException("xdr : "
						+ Hex.encodeHexString(buffer.array()), e);
			}
		}
		return xdrs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<XdrSingleSource> decode(byte[] load) {
		return decode(IoBuffer.wrap(load));
	}

	/**
	 * 获取公共信息
	 * 
	 * @param buffer
	 * @return
	 */
	public XdrSingleCommon getXdrSingleCommon(IoBuffer buffer) {
		try {
			buffer.mark();
			int length = buffer.getUnsignedShort();
			String city = "";
			try {
				city = TBCDFormat(getBytes(buffer, 2));
			} catch (Exception e) {
			}
			short Interface = buffer.getUnsigned();
			byte[] xdrId = getBytes(buffer, 16);
			short rat = buffer.getUnsigned();
			String imsi = "";
			try {
				imsi = TBCDFormat(getBytes(buffer, 8));
			} catch (Exception e) {
			}
			String imei = "";
			try {
				imei = TBCDFormat(getBytes(buffer, 8));
			} catch (Exception e) {
			}
			String msisdn = "";
			try {
				msisdn = TBCDFormat(getBytes(buffer, 16));
			} catch (Exception e) {
			}
			return new XdrSingleCommon(length, city, Interface, xdrId, rat,
					imsi, imei, msisdn);
		} catch (Exception e) {
			throw new UnkownProcessException("single xdr : "
					+ Hex.encodeHexString(buffer.array()), e);
		}
	}

	/**
	 * Uu接口，通过MME-S1APID,MME-GROUPID,MME-CODE关联回填
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceUu processUu(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short keyword1 = buffer.getUnsigned();// 1
		short keyword2 = buffer.getUnsigned();// 1
		short procedureStatus = buffer.getUnsigned();// 1
		byte[] plmnID = getBytes(buffer, 3);// 3
		long enbID = buffer.getUnsignedInt();// 4
		long cellID = buffer.getUnsignedInt();// 4
		int c_rnti = buffer.getUnsignedShort();// 2
		long targetEnbId = buffer.getUnsignedInt();// 4
		long targetCellId = buffer.getUnsignedInt();// 4
		int targetC_rnti = buffer.getUnsignedShort();// 2
		long mmeUeS1apId = buffer.getUnsignedInt();// 4
		int mmeGroupID = buffer.getUnsignedShort();// 2
		short mmeCode = buffer.getUnsigned();// 1
		long m_tmsi = buffer.getUnsignedInt();// 4
		short csfbIndication = buffer.getUnsigned();// 1
		short redirectedNetwork = buffer.getUnsigned();// 1
		short epsBearerNumber = buffer.getUnsigned();// 1
		List<XdrSingleSourceUu.Bearer> bearers = new ArrayList<>();
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < epsBearerNumber; i++) {
				short bearerID = buffer.getUnsigned();// 1
				short bearerStatus = buffer.getUnsigned();// 1
				bearers.add(new XdrSingleSourceUu.Bearer(bearerID, bearerStatus));
			}
		}
		XdrSingleSourceUu data = new XdrSingleSourceUu(procedureType,
				startTime, endTime, keyword1, keyword2, procedureStatus,
				plmnID, enbID, cellID, c_rnti, targetEnbId, targetCellId,
				targetC_rnti, mmeUeS1apId, mmeGroupID, mmeCode, m_tmsi,
				csfbIndication, redirectedNetwork, epsBearerNumber, bearers);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime() + scaOffsetMills);
		data.setProduceEndTime(endTime.getTime() + scaOffsetMills);
		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		return data;
	}

	/**
	 * X2接口，通过MME-S1APID,MME-GROUPID,MME-CODE关联回填
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceX2 processX2(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short procedureStatus = buffer.getUnsigned();// 1
		long sourceCellId = buffer.getUnsignedInt();// 4
		long targetCellId = buffer.getUnsignedInt();// 4
		long sourceEnbId = buffer.getUnsignedInt();// 4
		long targetEnbId = buffer.getUnsignedInt();// 4
		long mmeUeS1apId = buffer.getUnsignedInt();// 4
		int mmeGroupID = buffer.getUnsignedShort();// 2
		short mmeCode = buffer.getUnsigned();// 1
		int requestCause = buffer.getUnsignedShort();// 2
		int failureCause = buffer.getUnsignedShort();// 2
		short epsBearerNumber = buffer.getUnsigned();// 1
		List<XdrSingleSourceX2.Bearer> bearers = new ArrayList<>();
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < epsBearerNumber; i++) {
				short bearerID = buffer.getUnsigned();// 1
				short bearerStatus = buffer.getUnsigned();// 1
				bearers.add(new XdrSingleSourceX2.Bearer(bearerID, bearerStatus));
			}
		}
		XdrSingleSourceX2 data = new XdrSingleSourceX2(procedureType,
				startTime, endTime, procedureStatus, sourceCellId,
				targetCellId, sourceEnbId, targetEnbId, mmeUeS1apId,
				mmeGroupID, mmeCode, requestCause, failureCause,
				epsBearerNumber, bearers);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime() + scaOffsetMills);
		data.setProduceEndTime(endTime.getTime() + scaOffsetMills);
		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		return data;
	}

	/**
	 * UE_MR接口，通过MME-S1APID,MME-GROUPID,MME-CODE关联回填
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceUEMR processUEMR(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		int mmeGroupId = buffer.getUnsignedShort();// 2
		short mmeCode = buffer.getUnsigned();// 1
		long mmeUeS1apId = buffer.getUnsignedInt();// 4
		long enbID = buffer.getUnsignedInt(); // 4
		long cellID = buffer.getUnsignedInt();// 4
		Date time = getDate(buffer);// 8
		short MRType = buffer.getUnsigned();// 1
		short phr = buffer.getUnsigned();// 1
		int enbReceivedPower = buffer.getUnsignedShort();// 2
		short ulSinr = buffer.getUnsigned();// 1
		int ta = buffer.getUnsignedShort();// 2
		int aoa = buffer.getUnsignedShort();// 2
		int servingFreq = buffer.getUnsignedShort();// 2
		short servingRsrp = buffer.getUnsigned();// 1
		short servingRsrq = buffer.getUnsigned();// 1
		short neighborCellNumber = buffer.getUnsigned();// 1
		List<XdrSingleSourceUEMR.Neighbor> neighbors = new ArrayList<>();
		if (neighborCellNumber != 0xFF) {
			for (int i = 0; i < neighborCellNumber; i++) {
				int neighborCellPci = buffer.getUnsignedShort();// 2
				int neighborFreq = buffer.getUnsignedShort();// 2
				short neighborRSRP = buffer.getUnsigned();// 1
				short neighborRSRQ = buffer.getUnsigned();// 1
				neighbors.add(new Neighbor(neighborCellPci, neighborFreq,
						neighborRSRP, neighborRSRQ));
			}
		}
		XdrSingleSourceUEMR data = new XdrSingleSourceUEMR(mmeGroupId, mmeCode,
				mmeUeS1apId, enbID, cellID, time, MRType, phr,
				enbReceivedPower, ulSinr, ta, aoa, servingFreq, servingRsrp,
				servingRsrq, neighborCellNumber, neighbors);
		data.setCommon(common);
		data.setProduceStartTime(time.getTime() + scaOffsetMills);
		data.setProduceEndTime(time.getTime() + scaOffsetMills);
		return data;
	}

	/**
	 * CELL_MR接口，不处理
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceCellMR processCellMR(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		long enbID = buffer.getUnsignedInt();// 4
		long cellID = buffer.getUnsignedInt();// 4
		Date time = getDate(buffer);// 8
		byte[] enbReceivedInterfere = getBytes(buffer, 20);// 20

		byte[] ulPacketLoss = getBytes(buffer, 9);// 9
		byte[] dlpacketLoss = getBytes(buffer, 9);// 9
		XdrSingleSourceCellMR data = new XdrSingleSourceCellMR(enbID, cellID,
				time, enbReceivedInterfere, ulPacketLoss, dlpacketLoss);
		data.setCommon(common);
		data.setProduceStartTime(time.getTime());
		data.setProduceEndTime(time.getTime());
		return data;
	}

	/**
	 * S1-MME接口
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceS1MME processS1MME(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short procedureStatus = buffer.getUnsigned();// 1
		int requestCause = buffer.getUnsignedShort();// 2
		int failureCause = buffer.getUnsignedShort();// 2
		short keyword1 = buffer.getUnsigned();// 1
		short keyword2 = buffer.getUnsigned();// 1
		short keyword3 = buffer.getUnsigned();// 1
		short keyword4 = buffer.getUnsigned();// 1
		long mmeUeS1apID = buffer.getUnsignedInt();// 4
		int oldMmeGroupID = buffer.getUnsignedShort();// 2
		short oldMmeCode = buffer.getUnsigned();// 1
		long oldMTmsi = buffer.getUnsignedInt();// 4
		int mmeGroupID = buffer.getUnsignedShort();// 2
		short mmeCode = buffer.getUnsigned();// 1
		long mTmsi = buffer.getUnsignedInt();// 4
		long tmsi = buffer.getUnsignedInt();// 4
		byte[] userIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));// 4
		byte[] userIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		byte[] mmeIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		byte[] enbIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		int mmePort = buffer.getUnsignedShort();// 2
		int enbPort = buffer.getUnsignedShort();// 2
		int tac = buffer.getUnsignedShort();// 2
		long cellID = buffer.getUnsignedInt();// 4
		int otherTac = buffer.getUnsignedShort();// 2
		long otherEci = buffer.getUnsignedInt();// 4
		String apn = getStringSuffixZero(buffer, 32);// 32
		short epsBearerNumber = buffer.getUnsigned();// 1
		List<XdrSingleSourceS1MME.Bearer> bearers = new ArrayList<>();
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < epsBearerNumber; i++) {
				short bearerID = buffer.getUnsigned();// 1
				short bearerType = buffer.getUnsigned();// 1
				short bearerQCI = buffer.getUnsigned();// 1
				short bearerStatus = buffer.getUnsigned();// 1
				int bearerRequestCause = buffer.getUnsignedShort();// 2
				int bearerFailureCause = buffer.getUnsignedShort();// 2
				long bearerEnbGtpTeid = buffer.getUnsignedInt();// 4
				long bearerSGWGtpTeid = buffer.getUnsignedInt();// 4
				bearers.add(new XdrSingleSourceS1MME.Bearer(bearerID,
						bearerType, bearerQCI, bearerStatus,
						bearerRequestCause, bearerFailureCause,
						bearerEnbGtpTeid, bearerSGWGtpTeid));
			}
		}
		XdrSingleSourceS1MME data = new XdrSingleSourceS1MME(procedureType,
				startTime, endTime, procedureStatus, requestCause,
				failureCause, keyword1, keyword2, keyword3, keyword4,
				mmeUeS1apID, oldMmeGroupID, oldMmeCode, oldMTmsi, mmeGroupID,
				mmeCode, mTmsi, tmsi, userIpv4, userIpv6, mmeIpAdd, enbIpAdd,
				mmePort, enbPort, tac, cellID, otherTac, otherEci, apn,
				epsBearerNumber, bearers);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime());
		data.setProduceEndTime(endTime.getTime());
		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		byte[] low8 = new byte[8];
		System.arraycopy(common.getXdrId(), 8, low8, 0, 8);
		data.setLowID(Hex.encodeHexString(low8));
		return data;
	}

	/**
	 * S6a接口，通过IMSI回填
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceS6a processS6a(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short procedureStatus = buffer.getUnsigned();// 1
		int cause = buffer.getUnsignedShort();// 2
		byte[] userIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));// 4
		byte[] userIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		byte[] mmeAddress = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		byte[] hssAddress = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		int mmePort = buffer.getUnsignedShort();// 2
		int hssPort = buffer.getUnsignedShort();// 2
		String originRealm = getStringSuffixZero(buffer, 44);// 44
		String destinationRealm = getStringSuffixZero(buffer, 44);// 44
		String originHost = getStringSuffixZero(buffer, 64);// 64
		String destinationHost = getStringSuffixZero(buffer, 64);// 64
		long applicationId = buffer.getUnsignedInt();// 4
		short subscriberStatus = buffer.getUnsigned();// 1
		short accessRestrictionData = buffer.getUnsigned();// 1
		XdrSingleSourceS6a data = new XdrSingleSourceS6a(procedureType,
				startTime, endTime, procedureStatus, cause, userIpv4, userIpv6,
				mmeAddress, hssAddress, mmePort, hssPort, originRealm,
				destinationRealm, originHost, destinationHost, applicationId,
				subscriberStatus, accessRestrictionData);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime());
		data.setProduceEndTime(endTime.getTime());
		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		return data;
	}

	/**
	 * S10,S11接口,通过userIpv4_sgwteid与imei，imsi，msisdn回填
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceS10S11 processS10S11(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short procedureStatus = buffer.getUnsigned();// 1
		int failureCause = buffer.getUnsignedShort();// 2
		int reqCause = buffer.getUnsignedShort();// 2
		byte[] userIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));// 4
		byte[] userIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		byte[] mmeAddress = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		byte[] sgwOrOldMmeAddress = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		int mmePort = buffer.getUnsignedShort();// 2
		int sgwOrOldMmePort = buffer.getUnsignedShort();// 2
		long mmeControlTeid = buffer.getUnsignedInt();// 4
		long oldMmeOrSgwControlTeid = buffer.getUnsignedInt();// 4
		String apn = FormatUtils.getStringSuffixUnkownChar(buffer, 32);// 32  getStringSuffixZero
		short epsBearerNumber = buffer.getUnsigned();// 1
		List<XdrSingleSourceS10S11.Bearer> bearers = new ArrayList<XdrSingleSourceS10S11.Bearer>();
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < epsBearerNumber; i++) {
				short bearerID = buffer.getUnsigned();// 1
				short bearerType = buffer.getUnsigned();// 1
				short bearerQci = buffer.getUnsigned();// 1
				short bearerStatus = buffer.getUnsigned();// 1
				long bearerEnbGtpTeid = buffer.getUnsignedInt();// 4
				long bearerSGWGtpTeid = buffer.getUnsignedInt();// 4
				bearers.add(new XdrSingleSourceS10S11.Bearer(bearerID,
						bearerType, bearerQci, bearerStatus, bearerEnbGtpTeid,
						bearerSGWGtpTeid));
			}
		}
		XdrSingleSourceS10S11 data = new XdrSingleSourceS10S11(procedureType,
				startTime, endTime, procedureStatus, failureCause, reqCause,
				userIpv4, userIpv6, mmeAddress, sgwOrOldMmeAddress, mmePort,
				sgwOrOldMmePort, mmeControlTeid, oldMmeOrSgwControlTeid, apn,
				epsBearerNumber, bearers);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime());
		data.setProduceEndTime(endTime.getTime());
		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		return data;
	}

	/**
	 * SGs接口，通过IMSI回填
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceSGs processSGs(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short procedureStatus = buffer.getUnsigned();// 1
		short sgsCause = buffer.getUnsigned();// 1
		short rejectCause = buffer.getUnsigned();// 1
		short cpCause = buffer.getUnsigned();// 1
		short rpCause = buffer.getUnsigned();// 1
		byte[] userIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));
		byte[] userIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] mmeIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] mscServerIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		int mmePort = buffer.getUnsignedShort();// 2
		int mscServerPort = buffer.getUnsignedShort();// 2
		short serviceIndicator = buffer.getUnsigned();// 1
		String mmeName = FormatUtils
				.getStringRepZeroWithDot(getStringSuffixZero(buffer, 55));
		long tmsi = buffer.getUnsignedInt();// 4
		int newLac = buffer.getUnsignedShort();// 2
		int oldLac = buffer.getUnsignedShort();// 2
		int tac = buffer.getUnsignedShort();// 2
		long cellId = buffer.getUnsignedInt();// 4
		String callingId = "";
		try {
			byte[] values = getBytes(buffer, 24);
			if( ! FormatUtils.isAllF(values)){
				callingId = TBCDFormat(values);// 24
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		short vlrNameLength = buffer.getUnsigned();// 1
		String vlrName = FormatUtils
				.getStringRepZeroWithDot(getStringSuffixZero(buffer,
						vlrNameLength));
		XdrSingleSourceSGs data = new XdrSingleSourceSGs(procedureType,
				startTime, endTime, procedureStatus, sgsCause, rejectCause,
				cpCause, rpCause, userIpv4, userIpv6, mmeIpAdd, mscServerIpAdd,
				mmePort, mscServerPort, serviceIndicator, mmeName, tmsi,
				newLac, oldLac, tac, cellId, callingId, vlrNameLength, vlrName);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime());
		data.setProduceEndTime(endTime.getTime());
		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		return data;
	}

	/**
	 * S5/S8-C接口，暂不回填处理
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceS5S8C processS5S8C(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short procedureStatus = buffer.getUnsigned();// 1
		int failureCause = buffer.getUnsignedShort();// 2
		byte[] userIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));// 4
		byte[] userIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] sgwIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] pgwIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		int sgwPort = buffer.getUnsignedShort();// 2
		int pgwPort = buffer.getUnsignedShort();// 2
		long sgwControlTeid = buffer.getUnsignedInt();// 4
		long pgwControlTeid = buffer.getUnsignedInt();// 4
		byte[] indicationFlags = getBytes(buffer, 7);
		short uliLength = buffer.getUnsigned();// 1
		String uli = getString(buffer, uliLength);
		short epsBearerNumber = buffer.getUnsigned();// 1
		List<XdrSingleSourceS5S8C.Bearer> bearers = new ArrayList<XdrSingleSourceS5S8C.Bearer>();
		if (epsBearerNumber != 0xFF) {
			for (int i = 0; i < epsBearerNumber; i++) {
				short bearerID = buffer.getUnsigned();// 1
				short bearerType = buffer.getUnsigned();// 1
				short bearerQci = buffer.getUnsigned();// 1
				short bearerStatus = buffer.getUnsigned();// 1
				long bearerSgwGtpTeid = buffer.getUnsignedInt();// 4
				long bearerPgwGtpTeid = buffer.getUnsignedInt();// 4
				bearers.add(new XdrSingleSourceS5S8C.Bearer(bearerID,
						bearerType, bearerQci, bearerStatus, bearerSgwGtpTeid,
						bearerPgwGtpTeid));
			}
		}
		XdrSingleSourceS5S8C data = new XdrSingleSourceS5S8C(procedureType,
				startTime, endTime, procedureStatus, failureCause, userIpv4,
				userIpv6, sgwIpAdd, pgwIpAdd, sgwPort, pgwPort, sgwControlTeid,
				pgwControlTeid, indicationFlags, uliLength, uli,
				epsBearerNumber, bearers);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime());
		data.setProduceEndTime(endTime.getTime());
		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		return data;
	}

	/**
	 * S1U接口
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceS1U processS1U(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short machineIpAddType = buffer.getUnsigned();// 1
		byte[] sgwOrGgsnIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		byte[] enbOrSgsnIpAdd = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		int sgwOrGgsnPort = buffer.getUnsignedShort();// 2
		int enbOrSgsnPort = buffer.getUnsignedShort();// 2
		long enbOrSgsnGtpTeid = buffer.getUnsignedInt();// 4
		long sgwOrGgsnGtpTeid = buffer.getUnsignedInt();// 4
		int tac = buffer.getUnsignedShort();// 2
		long cellId = buffer.getUnsignedInt();// 4
		String apn = FormatUtils.getStringSuffixF(getBytes(buffer, 32));// 32
		XdrSingleS1UMobileCommon mobileCommon = new XdrSingleS1UMobileCommon(
				common.getRat(), common.getImsi(), common.getImei(),
				common.getMsisdn(), machineIpAddType, sgwOrGgsnIpAdd,
				enbOrSgsnIpAdd, sgwOrGgsnPort, enbOrSgsnPort, enbOrSgsnGtpTeid,
				sgwOrGgsnGtpTeid, tac, cellId, apn);
		short appTypeCode = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		int protocolType = buffer.getUnsignedShort();// 2
		int appType = buffer.getUnsignedShort();// 2
		int appSubType = buffer.getUnsignedShort();// 2
		short appContent = buffer.getUnsigned();// 1
		short appStatus = buffer.getUnsigned();// 1
		byte[] userIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));// 4
		byte[] userIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		int userPort = buffer.getUnsignedShort();// 2
		short l4protocal = buffer.getUnsigned();// 1
		byte[] appServerIpIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));// 4
		byte[] appServerIpIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));// 16
		int appServerPort = buffer.getUnsignedShort();// 2
		long ulData = buffer.getUnsignedInt();// 4
		long dlData = buffer.getUnsignedInt();// 4
		long ulIpPacket = buffer.getUnsignedInt();// 4
		long dlIpPacket = buffer.getUnsignedInt();// 4
		long ulDisOrderNum = buffer.getUnsignedInt();// 4
		long dlDisOrderNum = buffer.getUnsignedInt();// 4
		long ulRetransNum = buffer.getUnsignedInt();// 4
		long dlRetransNum = buffer.getUnsignedInt();// 4
		long tcpRespDelay = buffer.getUnsignedInt();// 4
		long tcpConfirmDeplay = buffer.getUnsignedInt();// 4
		long ulIpFragPackets = buffer.getUnsignedInt();// 4
		long dlIpFragPackets = buffer.getUnsignedInt();// 4
		long firstReqDelay = buffer.getUnsignedInt();// 4
		long firstRespDelay = buffer.getUnsignedInt();// 4
		long tcpWinSize = buffer.getUnsignedInt();// 4
		long mssSize = buffer.getUnsignedInt();// 4
		short tcpTestTimes = buffer.getUnsigned();// 1
		short tcpState = buffer.getUnsigned();// 1
		short finish = buffer.getUnsigned();// 1
		XdrSingleS1UBusinessCommon businessCommon = new XdrSingleS1UBusinessCommon(
				appTypeCode, startTime, endTime, protocolType, appType,
				appSubType, appContent, appStatus, userIpv4, userIpv6,
				userPort, l4protocal, appServerIpIpv4, appServerIpIpv6,
				appServerPort, ulData, dlData, ulIpPacket, dlIpPacket,
				ulDisOrderNum, dlDisOrderNum, ulRetransNum, dlRetransNum,
				tcpRespDelay, tcpConfirmDeplay, ulIpFragPackets,
				dlIpFragPackets, firstReqDelay, firstRespDelay, tcpWinSize,
				mssSize, tcpTestTimes, tcpState, finish);
		XdrSingleS1UApp app = processXdrSingleS1UApp(appTypeCode, buffer);
		XdrSingleSourceS1U data = new XdrSingleSourceS1U(
				new XdrSingleS1UCommon(common.getLength(), common.getCity(),
						common.getInterface(), common.getXdrId()),
				mobileCommon, businessCommon, app);
		data.setCommon(common);
		data.setProduceStartTime(startTime.getTime());
		data.setProduceEndTime(endTime.getTime());
		return data;
	}

	/**
	 * 处理s1u特定业务
	 * 
	 * @param appTypeCode
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processXdrSingleS1UApp(short appTypeCode,
			IoBuffer buffer) {
		XdrSingleS1UApp app = null;
		switch (appTypeCode) {
		case XDRType.HTTP:
			app = processHttp(buffer);
			break;
		case XDRType.DNS:
			app = processDns(buffer);
			break;
		case XDRType.EMAIL:
			app = processEmail(buffer);
			break;
		case XDRType.FTP:
			app = processFtp(buffer);
			break;
		case XDRType.IM:
			app = processIM(buffer);
			break;
		case XDRType.GN_SIGNALING:
			break;
		case XDRType.S11_SIGNALING:
			break;
		case XDRType.MMS:
			app = processMss(buffer);
			break;
		case XDRType.P2P:
			app = processP2P(buffer);
			break;
		case XDRType.RTSP:
			app = processRtsp(buffer);
			break;
		case XDRType.VOIP:
			app = processVoIp(buffer);
			break;
		default:
			app = null;
			break;
		}

		if (appTypeCode == XDRType.XDR_BUSINESS || app != null) {
			StaticUtil.statS1uAppType(appTypeCode);
		}
		return app;
	}

	/**
	 * s1u voip
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processVoIp(IoBuffer buffer) {
		short dircet = buffer.getUnsigned();
		String callNum =  FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;//getString(buffer, 32).trim();
		String calledNum =  FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;//getString(buffer, 32).trim();
		short callType = buffer.getUnsigned();
		int flow = buffer.getUnsignedShort();
		short hangCase = buffer.getUnsigned();
		short msgType = buffer.getUnsigned();
		return new XdrSingleS1UAppVOIP(dircet, callNum, calledNum, callType,
				flow, hangCase, msgType);
	}

	/**
	 * s1u rtsp
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processRtsp(IoBuffer buffer) {
		String url =  FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128).trim();
		String userAgent =  FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128).trim();
		String ip =  FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128).trim();
		int userStartPort = buffer.getUnsignedShort();
		int userEndPort = buffer.getUnsignedShort();
		int serverStartPort = buffer.getUnsignedShort();
		int serverEndPort = buffer.getUnsignedShort();
		int vidoNum = buffer.getUnsignedShort();
		int audioNum = buffer.getUnsignedShort();
		long delay = buffer.getUnsignedInt();
		return new XdrSingleS1UAppRTSP(url, userAgent, ip, userStartPort,
				userEndPort, serverStartPort, serverEndPort, vidoNum, audioNum,
				delay);
	}

	/**
	 * s1u p2p
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processP2P(IoBuffer buffer) {
		long size = buffer.getUnsignedInt();
		byte[] p2pId = getBytes(buffer, 16);
		String tracker = getString(buffer, 128);
		return new XdrSingleS1UAppP2P(size, p2pId, tracker);
	}

	/**
	 * s1u IM
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processIM(IoBuffer buffer) {
		String account =  FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;// getString(buffer, 32).trim();
		String version =  FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;//getString(buffer, 32).trim();
		String type = FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;// getString(buffer, 32).trim();
		short actionType = buffer.getUnsigned();
		return new XdrSingleS1UAppIM(account, version, type, actionType);
	}

	/**
	 * s1u ftp
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processFtp(IoBuffer buffer) {
		short ftpState = buffer.getUnsigned();
		String login = FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;//getString(buffer, 32).trim();
		String curDir = FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128).trim();
		short trasferMode = buffer.getUnsigned();
		short trasferDirect = buffer.getUnsigned();
		String fileName =  FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128);
		int localPort = buffer.getUnsignedShort();
		int remotePort = buffer.getUnsignedShort();
		long fileSize = buffer.getUnsignedInt();
		long respDelay = buffer.getUnsignedInt();
		long transferDuration = buffer.getUnsignedInt();
		return new XdrSingleS1UAppFtp(ftpState, login, curDir, trasferMode,
				trasferDirect, fileName, localPort, remotePort, fileSize,
				respDelay, transferDuration);
	}

	/**
	 * s1u email
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processEmail(IoBuffer buffer) {
		int type = buffer.getUnsignedShort();
		int state = buffer.getUnsignedShort();
		String username = FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;//getString(buffer, 32).trim();
		String sendInfo = FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128).trim();
		long length = buffer.getUnsignedInt();
		String smtpHost = FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128).trim();
		String recAccount = FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//getString(buffer, 128).trim();
		String headerInfo =FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;// getString(buffer, 128).trim();
		short linkType = buffer.getUnsigned();
		return new XdrSingleS1UAppEmail(type, state, username, sendInfo,
				length, smtpHost, recAccount, headerInfo, linkType);
	}

	/**
	 * s1u http
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrSingleS1UApp processHttp(IoBuffer buffer) {
		short ver = buffer.getUnsigned();
		int type = buffer.getUnsignedShort();
		int transState = buffer.getUnsignedShort();
		long firstPkgDelay = buffer.getUnsignedInt();
		long lastPkgDelay = buffer.getUnsignedInt();
		long lastAckDelay = buffer.getUnsignedInt();
		String host = getString(buffer, 64).trim();
		String uri = getString(buffer, 512).trim();
		String xOnlineHost = getString(buffer, 128).trim();
		String userAgent = getString(buffer, 256).trim();
		String httpContentType = getString(buffer, 128).trim();
		String referUri = getString(buffer, 128).trim();
		String cookie = getString(buffer, 256).trim();
		long contentLength = buffer.getUnsignedInt();
		short targetAction = buffer.getUnsigned();
		short wtpBreakType = buffer.getUnsigned();
		short wtpBreakCause = buffer.getUnsigned();
		String title = getString(buffer, 256).trim();
		String keyWord = getString(buffer, 256).trim();
		short businessActionType = buffer.getUnsigned();
		short businessFinishType = buffer.getUnsigned();
		long businessDelay = buffer.getUnsignedInt();
		short browser = buffer.getUnsigned();
		short protalType = buffer.getUnsigned();
		return new XdrSingleS1UAppHttp(ver, type, transState, firstPkgDelay,
				lastPkgDelay, lastAckDelay, host, uri, xOnlineHost, userAgent,
				httpContentType, referUri, cookie, contentLength, targetAction,
				wtpBreakType, wtpBreakCause, title, keyWord,
				businessActionType, businessFinishType, businessDelay, browser,
				protalType);
	}

	protected XdrSingleS1UApp processDns(IoBuffer buffer) {
		String dns = getString(buffer, 64).trim();
		String ip = getString(buffer, 15);
		short dnsResult = buffer.getUnsigned();
		short dnsTimes = buffer.getUnsigned();
		short dnsRespTimes = buffer.getUnsigned();
		short grantContentNum = buffer.getUnsigned();
		short additionContentNum = buffer.getUnsigned();
		return new XdrSingleS1UAppDNS(dns, ip, dnsResult, dnsTimes,
				dnsRespTimes, grantContentNum, additionContentNum);
	}

	protected XdrSingleS1UApp processMss(IoBuffer buffer) {
		short transType = buffer.getUnsigned();
		short successFlag = buffer.getUnsigned();
		short httpOrWap = buffer.getUnsigned();
		int httpWapCode = buffer.getUnsignedShort();
		int mmseRspStatus = buffer.getUnsignedShort();
		String mmsSendAddr =FormatUtils.getStringSuffixUnkownChar(buffer, 128) ;//128 getString(buffer, 128).trim();
		String mmsMsgId =FormatUtils.getStringSuffixUnkownChar(buffer, 64) ;//64 getString(buffer, 64).trim();
		String mmsTransactionId =FormatUtils.getStringSuffixUnkownChar(buffer, 32) ;//32 getString(buffer, 32).trim();
		String mmsRetriveAddr =FormatUtils.getStringSuffixUnkownChar(buffer, 256) ;//256 getString(buffer, 256).trim();
		int mmsRetriveAddrNum = buffer.getUnsignedShort();
		String mmsCcBccAddr =FormatUtils.getStringSuffixUnkownChar(buffer, 256) ;//256 getString(buffer, 256).trim();
		int mmsCcBccAddrNum = buffer.getUnsignedShort();
		String mmsSubject =FormatUtils.getStringSuffixUnkownChar(buffer, 256) ;//256 getString(buffer, 256).trim();
		long mmsDataSize = buffer.getUnsignedInt();
		String mmscAddr = FormatUtils.getStringSuffixUnkownChar(buffer, 64) ;//64 getString(buffer, 64);
		String host = FormatUtils.getStringSuffixUnkownChar(buffer, 64) ;//64 getString(buffer, 64);
		String referUri = FormatUtils.getStringSuffixUnkownChar(buffer, 128); ;//128 getString(buffer, 128);
		String xOnlineHost =  FormatUtils.getStringSuffixUnkownChar(buffer, 128);//128	getString(buffer, 128);
		return new XdrSingleS1UAppMMS(transType, successFlag, httpOrWap,
				httpWapCode, mmseRspStatus, mmsSendAddr, mmsMsgId,
				mmsTransactionId, mmsRetriveAddr, mmsRetriveAddrNum,
				mmsCcBccAddr, mmsCcBccAddrNum, mmsSubject, mmsDataSize,
				mmscAddr, host, referUri, xOnlineHost);
	}

	/**
	 * Gn-c接口
	 * 
	 * @param buffer
	 * @param common
	 * @return
	 * @throws DecoderException
	 */
	protected XdrSingleSourceGnC processGnc(IoBuffer buffer,
			XdrSingleCommon common) throws DecoderException {
		short procedureType = buffer.getUnsigned();// 1
		short subProcedureType = buffer.getUnsigned();// 1
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		short procedureStatus = buffer.getUnsigned();// 1
		byte[] userIpv4 = getBytes(buffer, 4);//getIp(getBytes(buffer, 4));
		byte[] userIpv6 = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] mmeAddress = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] oldMmeAddress = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] sgsnCIP = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		byte[] sgsnUIP = getBytes(buffer, 16);//getIp(getBytes(buffer, 16));
		int mmePort = buffer.getUnsignedShort();// 2
		int oldMmePort = buffer.getUnsignedShort();// 2
		int sgsnCPort = buffer.getUnsignedShort();// 2
		int sgsnUPort = buffer.getUnsignedShort();// 2
		long sgsnControlTeid = buffer.getUnsignedInt();// 4
		long sgsnDataTeid = buffer.getUnsignedInt();// 4
		long oldMmeControlTeid = buffer.getUnsignedInt();// 4
		long pTmsi = buffer.getUnsignedInt();// 4
		int lac = buffer.getUnsignedShort();// 2
		int rac = buffer.getUnsignedShort();// 2
		int tac = buffer.getUnsignedShort();// 2
		long cellId = buffer.getUnsignedInt();// 4
		int cause = buffer.getUnsignedShort();// 2
		int keyword = buffer.getUnsignedShort();// 2
		String apn = getString(buffer, 32);// 32
		XdrSingleSourceGnC data = new XdrSingleSourceGnC(procedureType,
				subProcedureType, startTime, endTime, procedureStatus,
				userIpv4, userIpv6, mmeAddress, oldMmeAddress, sgsnCIP,
				sgsnUIP, mmePort, oldMmePort, sgsnCPort, sgsnUPort,
				sgsnControlTeid, sgsnDataTeid, oldMmeControlTeid, pTmsi, lac,
				rac, tac, cellId, cause, keyword, apn);
		data.setCommon(common);

		StaticUtil.statXdrProcedureType(common.getInterface() + "-"
				+ procedureType);
		return data;
	}

	public static void main(String[] args) throws DecoderException {
		XdrSingleBytesDecoder singleXdr = new XdrSingleBytesDecoder();
		String dst = "016620f30b5b42403362393037323835ffffffffff0664008080611874f46805912097897300683146671023f5ffffffffffffffffff01ffffffffffffffffffffffff6456ce35ffffffffffffffffffffffff6456f535084b084b889a92020520dfa233d00038f101434d4e45542e6d6e633030302e6d63633436302e67707273ffffffffffffffff650000014b112613d10000014b1126143800040012001aff020ac51087ffffffffffffffffffffffffffffffff4d1e01dac90403ffffffffffffffffffffffffffffffff00350000004800000088000000010000000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ffff6e6f7274682d616d65726963612e706f6f6c2e6e74702e6f7267000000000000000000000000000000000000000000000000000000000000000000000000000037342e3230372e3234322e373100000001040000";
		byte[] load = Hex.decodeHex(dst.toCharArray());
		singleXdr.decode(load);
	}

}
