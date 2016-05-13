package com.eversec.lte.sdtp.file;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.constant.SdtpConstants.CompXDRType;
import com.eversec.lte.constant.SdtpConstants.XDRInterface;
import com.eversec.lte.model.compound.XdrCompoundCommon;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.model.compound.XdrCompoundSourceApp;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;
import com.eversec.lte.model.compound.XdrCompoundSourceApp.XdrApplicationCell;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.EpsBearer;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.XdrSingle;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR.XdrNeighborCell;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSourceCellMR;
import com.eversec.lte.model.single.XdrSingleSourceGnC;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceS5S8C;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.model.single.XdrSingleSourceUEMR.Neighbor;
import com.eversec.lte.model.single.XdrSingleSourceX2.Bearer;
import com.eversec.lte.model.single.s1u.XdrSingleS1UApp;
import com.eversec.lte.model.single.s1u.XdrSingleS1UBusinessCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UMobileCommon;
import com.eversec.lte.utils.FormatUtils;

public class BaseTest {
	
	public static void main(String[] args) {
		XdrSingleSourceS1MME s1mme = createS1MME();
		ExamineFileOutputTools.output(s1mme);
	}

	private static byte[] createBytes(int len) {
		byte[] dst = new byte[len];
		for (int i = 0; i < dst.length; i++) {
			dst[i] = 1;
		}
		return dst;
	}

	private static XdrSingleCommon createSingleCommon(int Interface) {
		int length = 100;// 2
		String city = "010";// 2;TBCD
		// short Interface;// 1
		byte[] xdrId = createBytes(16);// 16
		short rat = 1;// 1
		String imsi = "12345678";// 8;TBCD
		String imei = "12345678";// 8;TBCD
		String msisdn = "1234567890123456";// 16;TBCD
		return new XdrSingleCommon(length, city, (short) Interface, xdrId, rat,
				imsi, imei, msisdn);
	}

	public static XdrSingleSourceCellMR createCellMR() {
		long enbID = 1;// 4
		long cellID = 1;// 4
		Date time = new Date();// 8
		byte[] enbReceivedInterfere = createBytes(20);// 20
		byte[] ulPacketLoss = createBytes(9);// 9
		byte[] dlpacketLoss = createBytes(9);// 9
		XdrSingleSourceCellMR result = new XdrSingleSourceCellMR(enbID, cellID,
				time, enbReceivedInterfere, ulPacketLoss, dlpacketLoss);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.CELL_MR));
		return result;
	}

	public static XdrSingleSourceGnC createGnC() {
		return null;
	}

	public static XdrSingleSourceS10S11 createS10S11() {
		short procedureType = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		short procedureStatus = 1;// 1
		int failureCause = 1;// 2
		int reqCause = 1;// 2
		byte[] userIpv4 = FormatUtils.getIp("127.0.0.1",4);// 4
		byte[] userIpv6 =  FormatUtils.getIp("0:0:0:0:0:0:0:1",16);// 16
		byte[] mmeAddress =FormatUtils.getIp("0:0:0:0:0:0:0:1",16);// 16
		byte[] sgwOrOldMmeAddress = FormatUtils.getIp("0:0:0:0:0:0:0:1",16);// 16
		int mmePort = 1;// 2
		int sgwOrOldMmePort = 1;// 2
		long mmeControlTeid = 1;// 4
		long oldMmeOrSgwControlTeid = 1;// 4
		String apn = "123213";// 32
		short epsBearerNumber = 2;// 1
		List<XdrSingleSourceS10S11.Bearer> bearers = new ArrayList<XdrSingleSourceS10S11.Bearer>();
		for (int i = 0; i < epsBearerNumber; i++) {
			short bearerID = 1;// 1
			short bearerType = 2;// 1
			short bearerQci = 3;// 1
			short bearerStatus = 4;// 1
			long bearerEnbGtpTeid = 5;// 4
			long bearerSGWGtpTeid = 6;// 4
			bearers.add(new XdrSingleSourceS10S11.Bearer(bearerID, bearerType,
					bearerQci, bearerStatus, bearerEnbGtpTeid, bearerSGWGtpTeid));
		}
		XdrSingleSourceS10S11 result = new XdrSingleSourceS10S11(procedureType,
				startTime, endTime, procedureStatus, failureCause, reqCause,
				userIpv4, userIpv6, mmeAddress, sgwOrOldMmeAddress, mmePort,
				sgwOrOldMmePort, mmeControlTeid, oldMmeOrSgwControlTeid, apn,
				epsBearerNumber, bearers);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.S11));
		return result;
	}

	private static byte[] createIp4() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static byte[] createIp6() {
		// TODO Auto-generated method stub
		return null;
	}

	public static XdrSingleSourceS1MME createS1MME() {

		short procedureType = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		short procedureStatus = 1;// 1
		int requestCause = 1;// 2
		int failureCause = 1;// 2
		short keyword1 = 1;// 1
		short keyword2 = 1;// 1
		short keyword3 = 1;// 1
		short keyword4 = 1;// 1
		long mmeUeS1apID = 1;// 4
		int oldMmeGroupID = 1;// 2
		short oldMmeCode = 1;// 1
		long oldMTmsi = 1;// 4
		int mmeGroupID = 1;// 2
		short mmeCode = 1;// 1
		long mTmsi = 1;// 4
		long tmsi = 1;// 4
		byte[] userIpv4 = FormatUtils.getIp("127.0.0.1",4);// 4
		byte[] userIpv6 = FormatUtils.getIp("0:0:0:0:0:0:0:1",16);// 16
		byte[] mmeIpAdd = FormatUtils.getIp("0:0:0:0:0:0:0:1",16);// 16
		byte[] enbIpAdd = FormatUtils.getIp("0:0:0:0:0:0:0:1",16);// 16
		int mmePort = 1;// 2
		int enbPort = 1;// 2
		int tac = 1;// 2
		long cellID = 1;// 4
		int otherTac = 1;// 2
		long otherEci = 1;// 4
		String apn = "hello world!";// 32
		short epsBearerNumber = 2;// 1

		List<XdrSingleSourceS1MME.Bearer> bearers = new ArrayList<XdrSingleSourceS1MME.Bearer>();
		for (int i = 0; i < epsBearerNumber; i++) {
			short bearerID = 1;// 1
			short bearerType = 1;// 1
			short bearerQCI = 1;// 1
			short bearerStatus = 1;// 1
			// int requestCause = 1;// 2
			// int failureCause = 1;// 2
			long bearerEnbGtpTeid = 1;// 4
			long bearerSGWGtpTeid = 1;// 4
			bearers.add(new XdrSingleSourceS1MME.Bearer(bearerID, bearerType,
					bearerQCI, bearerStatus, requestCause, failureCause,
					bearerEnbGtpTeid, bearerSGWGtpTeid));
		}
		XdrSingleSourceS1MME result = new XdrSingleSourceS1MME(procedureType,
				startTime, endTime, procedureStatus, requestCause,
				failureCause, keyword1, keyword2, keyword3, keyword4,
				mmeUeS1apID, oldMmeGroupID, oldMmeCode, oldMTmsi, mmeGroupID,
				mmeCode, mTmsi, tmsi, userIpv4, userIpv6, mmeIpAdd, enbIpAdd,
				mmePort, enbPort, tac, cellID, otherTac, otherEci, apn,
				epsBearerNumber, bearers);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.S1MME));
		return result;
	}

	public static XdrSingleSourceS1U createS1U() {
		XdrSingleS1UCommon s1uCommon = createS1uCommon();
		XdrSingleS1UMobileCommon mobileCommon = createS1uMobileCommon();
		XdrSingleS1UBusinessCommon businessCommon = createsS1uBusinessCommon();
		XdrSingleS1UApp app = null;
		return new XdrSingleSourceS1U(s1uCommon, mobileCommon, businessCommon,
				app);
	}

	private static XdrSingleS1UCommon createS1uCommon() {
		int length = 10;// 2
		String city = "024";// 2;TBCD
		short Interface = XDRInterface.S1U;// 1
		byte[] xdrId = createBytes(16);// 16
		return new XdrSingleS1UCommon(length, city, Interface, xdrId);
	}

	private static XdrSingleS1UMobileCommon createS1uMobileCommon() {
		short rat = 1;// 1
		String imsi = "12345678";// 8;TBCD
		String imei = "12345678";// 8;TBCD
		String msisdn = "1234567890123456";// 16;TBCD
		short machineIpAddType = 1;// 1
		byte[] sgwOrGgsnIpAdd =  FormatUtils.getIp("0.0.0.0",16);// 4,16
		byte[] enbOrSgsnIpAdd =  FormatUtils.getIp("0.0.0.0",16);// 4,16
		int sgwOrGgsnPort = 1;// 2
		int enbOrSgsnPort = 1;// 2
		long enbOrSgsnGtpTeid = 2;// 4
		long sgwOrGgsnGtpTeid = 2;// 4
		int tac = 2;// 2
		long cellId = 2;// 4
		String apn = "12345678901234567890123456789012";// 32
		return new XdrSingleS1UMobileCommon(rat, imsi, imei, msisdn,
				machineIpAddType, sgwOrGgsnIpAdd, enbOrSgsnIpAdd,
				sgwOrGgsnPort, enbOrSgsnPort, enbOrSgsnGtpTeid,
				sgwOrGgsnGtpTeid, tac, cellId, apn);
	}

	private static XdrSingleS1UBusinessCommon createsS1uBusinessCommon() {
		short appTypeCode = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		int protocolType = 1;// 2
		int appType = 1;// 2
		int appSubType = 1;// 2
		short appContent = 1;// 1
		short appStatus = 1;// 1
		byte[] userIpv4 =  FormatUtils.getIp("1.1.1.1",4);// 4
		byte[] userIpv6 =  FormatUtils.getIp("1.1.1.1",16);// 16
		int userPort = 1;// 2
		short l4protocal = 1;// 1
		byte[] appServerIpIpv4 =  FormatUtils.getIp("1.1.1.1",4);// 4
		byte[] appServerIpIpv6 =  FormatUtils.getIp("1.1.1.1",16);// 16
		int appServerPort = 1;// 2
		long ulData = 1;// 4
		long dlData = 1;// 4
		long ulIpPacket = 1;// 4
		long dlIpPacket = 1;// 4
		long ulDisOrderNum = 1;// 上行tcp乱序报文数 4
		long dlDisOrderNum = 1;// 下行tcp乱序报文数 4
		long ulRetransNum = 1;// 上行tcp重传报文数 4
		long dlRetransNum = 1;// 下行tcp重传报文数 4
		long tcpRespDelay = 1;// tcp建链响应时延（ms） 4
		long tcpConfirmDeplay = 1;// tcp建链确认时延（ms） 4
		long ulIpFragPackets = 1;// 4
		long dlIpFragPackets = 1;// 4
		long firstReqDelay = 1;// tcp建链成功到第一条事务请求的时延（ms） 4
		long firstRespDelay = 1;// 第一条事务请求到其第一个响应包时延（ms） 4
		long tcpWinSize = 1;// 窗口大小 4
		long mssSize = 1;// mss大小 4
		short tcpTestTimes = 1;// tcp建链尝试次数 1
		short tcpState = 1;// tcp连接状态指示 1
		short finish = 1;// 会话是否结束标志 1
		return new XdrSingleS1UBusinessCommon(appTypeCode, startTime, endTime,
				protocolType, appType, appSubType, appContent, appStatus,
				userIpv4, userIpv6, userPort, l4protocal, appServerIpIpv4,
				appServerIpIpv6, appServerPort, ulData, dlData, ulIpPacket,
				dlIpPacket, ulDisOrderNum, dlDisOrderNum, ulRetransNum,
				dlRetransNum, tcpRespDelay, tcpConfirmDeplay, ulIpFragPackets,
				dlIpFragPackets, firstReqDelay, firstRespDelay, tcpWinSize,
				mssSize, tcpTestTimes, tcpState, finish);
	}

	public static XdrSingleSourceS5S8C createS5S8C() {
		short procedureType = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		short procedureStatus = 1;// 1
		int failureCause = 1;// 2
		byte[] userIpv4 =  FormatUtils.getIp("127.0.0.1",4);// 4
		byte[] userIpv6 =  FormatUtils.getIp("127.0.0.1",16);// 16
		byte[] sgwIpAdd =  FormatUtils.getIp("127.0.0.1",16);// 16
		byte[] pgwIpAdd =  FormatUtils.getIp("127.0.0.1",16);// 16
		int sgwPort = 100;// 2
		int pgwPort = 200;// 2
		long sgwControlTeid = 300;// 4
		long pgwControlTeid = 400;// 4
		byte[] indicationFlags = createBytes(7);// 7
		short uliLength = (short) "http://www.baidu.com".getBytes().length;// 1
		String uli = "http://www.baidu.com";
		short epsBearerNumber = 1;// 1

		List<XdrSingleSourceS5S8C.Bearer> bearers = new ArrayList<XdrSingleSourceS5S8C.Bearer>();
		for (int i = 0; i < epsBearerNumber; i++) {
			short bearerID = 1;// 1
			short bearerType = 1;// 1
			short bearerQci = 1;// 1
			short bearerStatus = 1;// 1
			long bearerSgwGtpTeid = 1;// 4
			long bearerPgwGtpTeid = 1;// 4

			bearers.add(new XdrSingleSourceS5S8C.Bearer(bearerID, bearerType,
					bearerQci, bearerStatus, bearerSgwGtpTeid, bearerPgwGtpTeid));
		}
		XdrSingleSourceS5S8C result = new XdrSingleSourceS5S8C(procedureType,
				startTime, endTime, procedureStatus, failureCause, userIpv4,
				userIpv6, sgwIpAdd, pgwIpAdd, sgwPort, pgwPort, sgwControlTeid,
				pgwControlTeid, indicationFlags, uliLength, uli,
				epsBearerNumber, bearers);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.S5S8));
		return result;
	}

	public static XdrSingleSourceS6a createS6a() {
		short procedureType = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		short procedureStatus = 1;// 1
		int cause = 1;// 2
		byte[] userIpv4 = FormatUtils.getIp("127.0.0.1",4);// 4
		byte[] userIpv6 =  FormatUtils.getIp("127.0.0.1",16);// 16
		byte[] mmeAddress =  FormatUtils.getIp("127.0.0.1",16); // 16
		byte[] hssAddress =  FormatUtils.getIp("127.0.0.1",16); // 16
		int mmePort = 100;// 2
		int hssPort = 200;// 2
		String originRealm = "12345678";// 44
		String destinationRealm = "12345678";// 44
		String originHost = "12345678";// 64
		String destinationHost = "12345678";// 64
		long applicationId = 1;// 4
		short subscriberStatus = 1;// 1
		short accessRestrictionData = 2;// 1
		XdrSingleSourceS6a result = new XdrSingleSourceS6a(procedureType,
				startTime, endTime, procedureStatus, cause, userIpv4, userIpv6,
				mmeAddress, hssAddress, mmePort, hssPort, originRealm,
				destinationRealm, originHost, destinationHost, applicationId,
				subscriberStatus, accessRestrictionData);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.S6A));
		return result;
	}

	public static XdrSingleSourceSGs createSGs() {
		short procedureType = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		short procedureStatus = 1;// 1
		short sgsCause = 1;// 1
		short rejectCause = 1;// 1
		short cpCause = 1;// 1
		short rpCause = 1;// 1
		byte[] userIpv4 =  FormatUtils.getIp("127.0.0.1",4);// 4
		byte[] userIpv6 =  FormatUtils.getIp("127.0.0.1",16);// 16
		byte[] mmeIpAdd =  FormatUtils.getIp("127.0.0.1",16);// 16
		byte[] mscServerIpAdd =  FormatUtils.getIp("127.0.0.1",16);// 16
		int mmePort = 1;// 2
		int mscServerPort = 1;// 2
		short serviceIndicator = 1;// 1
		String mmeName = "mme";// 55
		long tmsi = 1;// 4
		int newLac = 1;// 2
		int oldLac = 1;// 2
		int tac = 1;// 2
		long cellId = 1;// 4
		String callingId = "1234567890123456";// 24
		short vlrNameLength = (short) "abc".getBytes().length;// 1
		String vlrName = "abc";// 可变长
		XdrSingleSourceSGs result = new XdrSingleSourceSGs(procedureType,
				startTime, endTime, procedureStatus, sgsCause, rejectCause,
				cpCause, rpCause, userIpv4, userIpv6, mmeIpAdd, mscServerIpAdd,
				mmePort, mscServerPort, serviceIndicator, mmeName, tmsi,
				newLac, oldLac, tac, cellId, callingId, vlrNameLength, vlrName);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.SGS));
		return result;
	}

	public static XdrSingleSourceUEMR createUEMR() {
		int mmeGroupId = 1;// 2
		short mmeCode = 1;// 1
		long mmeUeS1apId = 1;// 4
		long enbID = 1;// 4
		long cellID = 1;// 4
		Date time = new Date();// 8
		short mRType = 1;// 1
		short phr = 1;// 1
		int enbReceivedPower = 1;// 2
		short ulSinr = 1;// 1
		int ta = 1;// 2
		int aoa = 1;// 2
		int servingFreq = 1;// 2
		short servingRsrp = 1;// 1
		short servingRsrq = 1;// 1
		short neighborCellNumber = 2;// 1
		List<XdrSingleSourceUEMR.Neighbor> neighbors = new ArrayList<XdrSingleSourceUEMR.Neighbor>();
		for (int i = 0; i < neighborCellNumber; i++) {
			short neighborCellPci = 1;// 1（2.0.7）
			int neighborFreq = 2;// 2
			short neighborRSRP = 1;// 1
			short neighborRSRQ = 1;// 1
			neighbors.add(new Neighbor(neighborCellPci, neighborFreq,
					neighborRSRP, neighborRSRQ));
		}

		XdrSingleSourceUEMR result = new XdrSingleSourceUEMR(mmeGroupId,
				mmeCode, mmeUeS1apId, enbID, cellID, time, mRType, phr,
				enbReceivedPower, ulSinr, ta, aoa, servingFreq, servingRsrp,
				servingRsrq, neighborCellNumber, neighbors);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.UE_MR));
		return result;
	}

	public static XdrSingleSourceUu createUu() {
		short procedureType = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		short keyword1 = 1;// 1
		short keyword2 = 1;// 1
		short procedureStatus = 1;// 1
		byte[] plmnID = createBytes(3);// 3
		long enbID = 1;// 4
		long cellID = 1;// 4
		int c_rnti = 1;// 2
		long targetCellId = 1;// 4
		long targetEnbId = 1;// 4
		int targetC_rnti = 1;// 2
		long mmeUeS1apId = 1;// 4
		int mmeGroupID = 1;// 2
		short mmeCode = 1;// 1
		long m_tmsi = 1;// 4
		short csfbIndication = 1;// 1
		short redirectedNetwork = 1; // 1
		short epsBearerNumber = 1;// 1
		List<XdrSingleSourceUu.Bearer> bearers = new ArrayList<XdrSingleSourceUu.Bearer>();
		for (int i = 0; i < epsBearerNumber; i++) {
			short bearerID = 1;// 1
			short bearerStatus = 1;// 1
			bearers.add(new XdrSingleSourceUu.Bearer(bearerID, bearerStatus));
		}
		XdrSingleSourceUu result = new XdrSingleSourceUu(procedureType,
				startTime, endTime, keyword1, keyword2, procedureStatus,
				plmnID, enbID, cellID, c_rnti, targetEnbId, targetCellId,
				targetC_rnti, mmeUeS1apId, mmeGroupID, mmeCode, m_tmsi,
				csfbIndication, redirectedNetwork, epsBearerNumber, bearers);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.UU));
		return result;
	}

	public static XdrSingleSourceX2 createX2() {
		short procedureType = 1;// 1
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		short procedureStatus = 1;// 1
		long sourceCellId = 1;// 4
		long targetCellId = 1;// 4
		long sourceEnbId = 1;// 4
		long targetEnbId = 1;// 4
		long mmeUeS1apId = 1;// 4
		int mmeGroupID = 1;// 2
		short mmeCode = 1;// 1
		int requestCause = 1;// 2
		int failureCause = 1;// 2
		short epsBearerNumber = 1;// 1
		List<XdrSingleSourceX2.Bearer> bearers = new ArrayList<XdrSingleSourceX2.Bearer>();
		for (int i = 0; i < epsBearerNumber; i++) {
			short bearerID = 1;// 1
			short bearerStatus = 1;// 1
			bearers.add(new Bearer(bearerID, bearerStatus));
		}

		XdrSingleSourceX2 result = new XdrSingleSourceX2(procedureType,
				startTime, endTime, procedureStatus, sourceCellId,
				targetCellId, sourceEnbId, targetEnbId, mmeUeS1apId,
				mmeGroupID, mmeCode, requestCause, failureCause,
				epsBearerNumber, bearers);
		result.setCommon(createSingleCommon(SdtpConstants.XDRInterface.X2));
		return result;
	}

	protected static XdrCompoundSource createCompSignle() {
		short procedureType = 1; // 1
		Date startTime = new Date(); // 8
		Date endTime = new Date(); // 8
		double startLongitude = 1; // 8
		double startLatitude = 1; // 8
		double endLongitude = 1; // 8
		double endLatitude = 1; // 8
		short procedureStatus = 1; // 1
		int requestCause = 1;// 2
		short failureInterface = 1; // 1
		short failureProcedureType = 1;// 1
		int failureCause = 1; // 2
		short keyword1 = 1; // 1
		short keyword2 = 1; // 1
		short keyword3 = 1; // 1
		short keyword4 = 1; // 1
		long enbId = 1; // 4
		long cellId = 1; // 4
		int mmeGroupId = 1; // 2
		short mmeCode = 1; // 1
		int tac = 1; // 2
		String userIpv4 = "127.0.0.1";// 4
		String userIpv6 = "127.0.0.1";// 16
		long newEnbId = 1; // 4
		long newCellId = 1; // 4
		int newMmeGroupId = 1; // 2
		short newMmeCode = 1; // 1
		int newTac = 1; // 2
		short epsBearerNumber = 2; // 1
		List<XdrCompoundSourceSignaling.EpsBearer> bearers = new ArrayList<XdrCompoundSourceSignaling.EpsBearer>();
		for (int i = 0; i < epsBearerNumber; i++) {
			short bearerId = 1; // 1
			short bearerType = 1; // 1
			short bearerQCI = 1; // 1
			short bearerStatus = 1; // 1
			int bearRequestCause = 1;// 2
			int bearFailureCause = 2;// 2
			long bearerEnbGtpTeid = 1; // 4
			long bearerSgwGtpTeid = 1; // 4
			bearers.add(new EpsBearer(bearerId, bearerType, bearerQCI,
					bearerStatus, bearRequestCause, bearRequestCause,
					bearerEnbGtpTeid, bearerSgwGtpTeid));
		}
		short xdrNumber = 2; // 1
		List<XdrCompoundSourceSignaling.XdrSingle> singles = new ArrayList<XdrCompoundSourceSignaling.XdrSingle>();
		for (int i = 0; i < xdrNumber; i++) {
			short c_Interface = 1; // 1
			byte[] xdrId = createBytes(16); // 16
			short c_procedureType = 1; // 1
			Date c_startTime = new Date(); // 8
			Date c_endTime = new Date(); // 8
			double startlongitude = 1; // 8
			double startlatitude = 1; // 8
			double endlongitude = 1; // 8
			double endlatitude = 1; // 8
			short status = 1; // 1
			short cause = 1; // 1
			singles.add(new XdrSingle(c_Interface, xdrId, c_procedureType,
					c_startTime, c_endTime, startlongitude, startlatitude,
					endlongitude, endlatitude, status, cause, cause));
		}
		XdrCompoundSourceSignaling sign = new XdrCompoundSourceSignaling(
				procedureType, startTime, endTime, startLongitude,
				startLatitude, endLongitude, endLatitude, procedureStatus,
				requestCause, failureInterface, failureProcedureType,
				failureCause, keyword1, keyword2, keyword3, keyword4, enbId,
				cellId, mmeGroupId, mmeCode, tac, userIpv4, userIpv6, newEnbId,
				newCellId, newMmeGroupId, newMmeCode, newTac, epsBearerNumber,
				bearers, xdrNumber, singles);

		sign.setCommon(createCompCommon(CompXDRType.CXDR_SIGNALING));

		return sign;
	}

	protected static XdrCompoundSource createCompApp() {
		int appType = 1;// 2
		int appSubType = 1;// 2
		Date startTime = new Date();// 8
		Date endTime = new Date();// 8
		double startLongitude = 1;// 8
		double startLatitude = 1;// 8
		double endLongitude = 1;// 8
		double endLatitude = 1;// 8
		String userIPv4 = "127.0.0.1";// 4
		String userIPv6 = "127.0.0.1";// 16
		short xdrNumber = 1;// 1

		List<XdrCompoundSourceApp.XdrApplicationCell> cells = new ArrayList<XdrCompoundSourceApp.XdrApplicationCell>();
		for (int i = 0; i < xdrNumber; i++) {
			byte[] xdrID = createBytes(16);// 16
			Date c_startTime = new Date();// 8
			Date c_endTime = new Date();// 8
			double c_startLongitude = 1;// 8
			double c_startLatitude = 1;// 8
			double c_endLongitude = 1;// 8
			double c_endLatitude = 1;// 8
			int enbID = 1;// 2
			long cellID = 1;// 4
			long enbGtpTeid = 1;// 4
			long sgwGtpTeid = 1;// 4
			long uLData = 1;// 4
			long dLData = 1;// 4
			cells.add(new XdrApplicationCell(xdrID, c_startTime, c_endTime,
					c_startLongitude, c_startLatitude, c_endLongitude,
					c_endLatitude, enbID, cellID, enbGtpTeid, sgwGtpTeid,
					uLData, dLData));
		}
		XdrCompoundSourceApp app = new XdrCompoundSourceApp(appType,
				appSubType, startTime, endTime, startLongitude, startLatitude,
				endLongitude, endLatitude, userIPv4, userIPv6, xdrNumber, cells);
		app.setCommon(createCompCommon(CompXDRType.CXDR_APPLICATION));
		return app;
	}

	protected static XdrCompoundSource createCompUEMR() {
		double longitude = 1; // 8
		double latitude = 1;// 8
		long enbId = 1; // 4
		long cellId = 1; // 4
		Date dateTime = new Date();// 8
		short mrType = 1;// 1
		short phr = 1;// 1
		int enbReceivedPower = 1;// 2
		short ulSinr = 1;// 1
		int servingFreq = 1;// 2
		short servingRsrp = 1;// 1
		short servingRsrq = 1;// 1
		short neighborCellNumber = 1;// 1
		List<XdrCompoundSourceUEMR.XdrNeighborCell> neighbors = new ArrayList<XdrCompoundSourceUEMR.XdrNeighborCell>();
		for (int i = 0; i < neighborCellNumber; i++) {
			short c_cellId = 1;// 2
			int freq = 1;// 2
			short rsrp = 1;// 1
			short rsrq = 1;// 1
			neighbors.add(new XdrNeighborCell(c_cellId, freq, rsrp, rsrq));
		}
		XdrCompoundSourceUEMR uemr = new XdrCompoundSourceUEMR(longitude,
				latitude, enbId, cellId, dateTime, mrType, phr,
				enbReceivedPower, ulSinr, servingFreq, servingRsrp,
				servingRsrq, neighborCellNumber, neighbors);

		uemr.setCommon(createCompCommon(CompXDRType.CXDR_UEMR));
		return uemr;
	}

	private static XdrCompoundCommon createCompCommon(int xdrType1) {
		int length = 1; // 2
		String city = "010"; // 2
		short rat = 1; // 1
		short xdrType = (short) xdrType1; // 1
		byte[] xdrId = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, }; // 16
		String imsi = "12345678";// 8
		String imei = "12345678";// 8
		String msisdn = "1234567890123456";// 16
		return new XdrCompoundCommon(length, city, rat, xdrType, xdrId, imsi,
				imei, msisdn);
	}
}
