package com.eversec.lte.processor.decoder;

import static com.eversec.lte.utils.FormatUtils.TBCDFormat;
import static com.eversec.lte.utils.FormatUtils.getBytes;
import static com.eversec.lte.utils.FormatUtils.getDate;
import static com.eversec.lte.utils.FormatUtils.getIp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.constant.SdtpConstants.CompXDRType;
import com.eversec.lte.exception.UnkownProcessException;
import com.eversec.lte.model.compound.XdrCompoundCommon;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.model.compound.XdrCompoundSourceApp;
import com.eversec.lte.model.compound.XdrCompoundSourceApp.XdrApplicationCell;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.EpsBearer;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.XdrSingle;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR.XdrNeighborCell;
import com.eversec.lte.processor.data.StaticData;
import com.eversec.lte.processor.statistics.StaticUtil;

/**
 * 
 * @author bieremayi
 * 
 */
public class XdrCompBytesDecoder implements IDecoder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2628004729664471681L;

	@SuppressWarnings("unchecked")
	@Override
	public List<XdrCompoundSource> decode(byte[] load) {
		boolean first = true;
		List<XdrCompoundSource> cxdrs = new ArrayList<>();
		IoBuffer buffer = IoBuffer.wrap(load);
		while (buffer.hasRemaining()) {
			if (first) {
				first = false;
			} else {
				if (SdtpConfig.IS_APPLICATION_LAYER) {
					buffer.getUnsigned();
				}
			}
			XdrCompoundCommon common = null;
			try {
				common = getXdrCompoundCommon(buffer);
				int Interface = common.getXdrType();
				XdrCompoundSource cxdr = null;
				switch (Interface) {
				case CompXDRType.CXDR_APPLICATION:
					cxdr = processApplication(buffer, common);
					break;
				case CompXDRType.CXDR_SIGNALING:
					cxdr = processSignaling(buffer, common);
					break;
				case CompXDRType.CXDR_UEMR:
					cxdr = processUemr(buffer, common);
					break;
				case CompXDRType.CXDR_UNKNOWN:
					break;
				default:
					break;
				}
				if (cxdr != null) {
					cxdrs.add(cxdr);
					StaticData.SOURCE_COUNT.getAndIncrement();
					StaticUtil.statCompType(Interface);
					if (cxdr instanceof XdrCompoundSourceSignaling) {
						StaticUtil
								.statCompSignaling((XdrCompoundSourceSignaling) cxdr);
					}
				}
			} catch (Exception e) {
				throw new UnkownProcessException("xdr:"
						+ Hex.encodeHexString(buffer.array()), e);
			}
		}
		return cxdrs;
	}

	protected XdrCompoundSource processUemr(IoBuffer buffer,
			XdrCompoundCommon common) {
		double longitude = buffer.getDouble(); // 8
		double latitude = buffer.getDouble();// 8
		long enbId = buffer.getUnsignedInt(); // 4
		long cellId = buffer.getUnsignedInt(); // 4
		Date dateTime = getDate(buffer);// 8
		short mrType = buffer.getUnsigned();// 1
		short phr = buffer.getUnsigned();// 1
		int enbReceivedPower = buffer.getUnsignedShort();// 2
		short ulSinr = buffer.getUnsigned();// 1
		int servingFreq = buffer.getUnsignedShort();// 2
		short servingRsrp = buffer.getUnsigned();// 1
		short servingRsrq = buffer.getUnsigned();// 1
		short neighborCellNumber = buffer.getUnsigned();// 1
		List<XdrCompoundSourceUEMR.XdrNeighborCell> neighbors = new ArrayList<XdrCompoundSourceUEMR.XdrNeighborCell>();

		if (neighborCellNumber > 0 && neighborCellNumber < 0xff) {
			for (int i = 0; i < neighborCellNumber; i++) {
				int c_cellId = buffer.getUnsignedShort();// 2
				int freq = buffer.getUnsignedShort();// 2
				short rsrp = buffer.getUnsigned();// 1
				short rsrq = buffer.getUnsigned();// 1
				neighbors.add(new XdrNeighborCell(c_cellId, freq, rsrp, rsrq));
			}
		}
		XdrCompoundSourceUEMR uemr = new XdrCompoundSourceUEMR(longitude,
				latitude, enbId, cellId, dateTime, mrType, phr,
				enbReceivedPower, ulSinr, servingFreq, servingRsrp,
				servingRsrq, neighborCellNumber, neighbors);
		uemr.setCommon(common);
		return uemr;
	}

	protected XdrCompoundSource processSignaling(IoBuffer buffer,
			XdrCompoundCommon common) {
		short procedureType = buffer.getUnsigned(); // 1
		Date startTime = getDate(buffer); // 8
		Date endTime = getDate(buffer); // 8
		double startLongitude = buffer.getDouble(); // 8
		double startLatitude = buffer.getDouble(); // 8
		double endLongitude = buffer.getDouble(); // 8
		double endLatitude = buffer.getDouble(); // 8
		short procedureStatus = buffer.getUnsigned(); // 1
		int requestCause = buffer.getUnsignedShort(); // 2
		short failureInterface = buffer.getUnsigned(); // 1
		short failureProcedureType = buffer.getUnsigned();// 1
		int failureCause = buffer.getUnsignedShort(); // 2
		short keyword1 = buffer.getUnsigned(); // 1
		short keyword2 = buffer.getUnsigned(); // 1
		short keyword3 = buffer.getUnsigned(); // 1
		short keyword4 = buffer.getUnsigned(); // 1
		long enbId = buffer.getUnsignedInt(); // 4
		long cellId = buffer.getUnsignedInt(); // 4
		int mmeGroupId = buffer.getUnsignedShort(); // 2
		short mmeCode = buffer.getUnsigned(); // 1
		int tac = buffer.getUnsignedShort(); // 2
		String userIpv4 = getIp(getBytes(buffer, 4));// 4
		String userIpv6 = getIp(getBytes(buffer, 16));// 16
		long newEnbId = buffer.getUnsignedInt(); // 4
		long newCellId = buffer.getUnsignedInt(); // 4
		int newMmeGroupId = buffer.getUnsignedShort(); // 2 
		short newMmeCode = buffer.getUnsigned(); // 1
		int newTac = buffer.getUnsignedShort(); // 2  
		short epsBearerNumber = buffer.getUnsigned(); // 1
		List<XdrCompoundSourceSignaling.EpsBearer> bearers = new ArrayList<XdrCompoundSourceSignaling.EpsBearer>();
		if (epsBearerNumber > 0 && epsBearerNumber != 0xff) {
			for (int i = 0; i < epsBearerNumber; i++) {
				short bearerId = buffer.getUnsigned(); // 1
				short bearerType = buffer.getUnsigned(); // 1
				short bearerQCI = buffer.getUnsigned(); // 1
				short bearerStatus = buffer.getUnsigned(); // 1
				int bearRequestCause = buffer.getUnsignedShort(); // 2
				int bearFailureCause = buffer.getUnsignedShort(); // 2
				long bearerEnbGtpTeid = buffer.getUnsignedInt(); // 4
				long bearerSgwGtpTeid = buffer.getUnsignedInt(); // 4

				bearers.add(new EpsBearer(bearerId, bearerType, bearerQCI,
						bearerStatus, bearRequestCause, bearFailureCause,
						bearerEnbGtpTeid, bearerSgwGtpTeid));
			}
		}
		short xdrNumber = buffer.getUnsigned(); // 1
		List<XdrCompoundSourceSignaling.XdrSingle> singles = new ArrayList<XdrCompoundSourceSignaling.XdrSingle>();
		if (xdrNumber > 0 && xdrNumber != 0xff) {
			for (int i = 0; i < xdrNumber; i++) {
				short Interface = buffer.getUnsigned(); // 1
				byte[] xdrId = getBytes(buffer, 16); // 16
				short c_procedureType = buffer.getUnsigned(); // 1
				Date c_startTime = getDate(buffer); // 8
				Date c_endTime = getDate(buffer); // 8
				double startlongitude = buffer.getDouble(); // 8
				double startlatitude = buffer.getDouble(); // 8
				double endlongitude = buffer.getDouble(); // 8
				double endlatitude = buffer.getDouble(); // 8
				short status = buffer.getUnsigned(); // 1
				int reqCause = buffer.getUnsignedShort(); // 2
				int fauCause = buffer.getUnsignedShort(); // 2
				singles.add(new XdrSingle(Interface, xdrId, c_procedureType,
						c_startTime, c_endTime, startlongitude, startlatitude,
						endlongitude, endlatitude, status, reqCause, fauCause));
			}
		}

		XdrCompoundSourceSignaling signaling = new XdrCompoundSourceSignaling(
				procedureType, startTime, endTime, startLongitude,
				startLatitude, endLongitude, endLatitude, procedureStatus,
				requestCause, failureInterface, failureProcedureType,
				failureCause, keyword1, keyword2, keyword3, keyword4, enbId,
				cellId, mmeGroupId, mmeCode, tac, userIpv4, userIpv6, newEnbId,
				newCellId, newMmeGroupId, newMmeCode, newTac, epsBearerNumber,
				bearers, xdrNumber, singles);
		signaling.setCommon(common);
		return signaling;
	}

	protected XdrCompoundSource processApplication(IoBuffer buffer,
			XdrCompoundCommon common) {
		int appType = buffer.getUnsignedShort();// 2
		int appSubType = buffer.getUnsignedShort();// 2
		Date startTime = getDate(buffer);// 8
		Date endTime = getDate(buffer);// 8
		double startLongitude = buffer.getDouble();// 8
		double startLatitude = buffer.getDouble();// 8
		double endLongitude = buffer.getDouble();// 8
		double endLatitude = buffer.getDouble();// 8
		String userIPv4 = getIp(getBytes(buffer, 4));// 4
		String userIPv6 = getIp(getBytes(buffer, 16));// 16
		short xdrNumber = buffer.getUnsigned();// 1

		List<XdrCompoundSourceApp.XdrApplicationCell> cells = new ArrayList<XdrCompoundSourceApp.XdrApplicationCell>();
		if (xdrNumber > 0 && xdrNumber <= SdtpConstants.MAX_UNSIGNED_BYTE) {
			for (int i = 0; i < xdrNumber; i++) {
				byte[] xdrID = getBytes(buffer, 16);// 16
				Date c_startTime = getDate(buffer);// 8
				Date c_endTime = getDate(buffer);// 8
				double c_startLongitude = buffer.getDouble();// 8
				double c_startLatitude = buffer.getDouble();// 8
				double c_endLongitude = buffer.getDouble();// 8
				double c_endLatitude = buffer.getDouble(); // 8
				long enbID = buffer.getUnsignedInt();// 4
				long cellID = buffer.getUnsignedInt();// 4
				long enbGtpTeid = buffer.getUnsignedInt();// 4
				long sgwGtpTeid = buffer.getUnsignedInt();// 4
				long uLData = buffer.getUnsignedInt();// 4
				long dLData = buffer.getUnsignedInt();// 4

				cells.add(new XdrApplicationCell(xdrID, c_startTime, c_endTime,
						c_startLongitude, c_startLatitude, c_endLongitude,
						c_endLatitude, enbID, cellID, enbGtpTeid, sgwGtpTeid,
						uLData, dLData));
			}
		}
		XdrCompoundSourceApp app = new XdrCompoundSourceApp(appType,
				appSubType, startTime, endTime, startLongitude, startLatitude,
				endLongitude, endLatitude, userIPv4, userIPv6, xdrNumber, cells);
		app.setCommon(common);
		return app;
	}

	protected XdrCompoundCommon getXdrCompoundCommon(IoBuffer buffer) {
		int length = buffer.getUnsignedShort(); // 2
		String city = "";
		try {
			city = TBCDFormat(getBytes(buffer, 2));// 2
		} catch (Exception e) {
		}
		short rat = buffer.getUnsigned(); // 1
		short xdrType = buffer.getUnsigned(); // 1
		byte[] xdrId = new byte[16]; // 16
		buffer.get(xdrId);
		String imsi = "";
		try {
			imsi = TBCDFormat(getBytes(buffer, 8));// 8
		} catch (Exception e) {
		}
		String imei = "";
		try {
			imei = TBCDFormat(getBytes(buffer, 8));// 8
		} catch (Exception e) {
		}
		String msisdn = "";
		try {
			msisdn = TBCDFormat(getBytes(buffer, 16));// 16
		} catch (Exception e) {
		}
		return new XdrCompoundCommon(length, city, rat, xdrType, xdrId, imsi,
				imei, msisdn);
	}

	public static void main(String[] args) {
		System.out.println(0xff);
	}

}
