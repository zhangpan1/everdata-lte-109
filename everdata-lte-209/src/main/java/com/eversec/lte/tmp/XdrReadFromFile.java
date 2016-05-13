//package com.eversec.lte.tmp;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.io.FileUtils;
//
//import com.eversec.lte.model.single.XdrSingleCommon;
//import com.eversec.lte.model.single.XdrSingleSourceCellMR;
//import com.eversec.lte.model.single.XdrSingleSourceGnC;
//import com.eversec.lte.model.single.XdrSingleSourceS10S11;
//import com.eversec.lte.model.single.XdrSingleSourceS1MME;
//import com.eversec.lte.model.single.XdrSingleSourceS5S8C;
//import com.eversec.lte.model.single.XdrSingleSourceS6a;
//import com.eversec.lte.model.single.XdrSingleSourceSGs;
//import com.eversec.lte.model.single.XdrSingleSourceUEMR;
//import com.eversec.lte.model.single.XdrSingleSourceUu;
//import com.eversec.lte.model.single.XdrSingleSourceX2;
//import com.eversec.lte.utils.FormatUtils;
//
//public class XdrReadFromFile {
//
//	public static interface XdrParser<T> {
//		public T parse(String line);
//	}
//
//	public static <T> List<T> readXdrFromFile(String dir, XdrParser<T> p) {
//		try {
//			List<T> result = new ArrayList<>();
//			Collection<File> fs = FileUtils
//					.listFiles(new File(dir), null, true);
//			for (File f : fs) {
//				List<String> lines = FileUtils.readLines(f);
//				for (String line : lines) {
//					result.add(p.parse(line));
//				}
//			}
//			return result;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static List<XdrSingleSourceCellMR> readCellMrFromFile(String dir)
//			throws Exception {
//		return readXdrFromFile(dir, new XdrParser<XdrSingleSourceCellMR>() {
//			@Override
//			public XdrSingleSourceCellMR parse(String line) {
//				String[] arr = line.split("\\|");
//				int index = 9;
//				XdrSingleCommon common = getXdrSingleCommon(arr);
//				long enbID = plong(arr[index++]);// 4
//				long cellID = plong(arr[index++]);// 4
//				Date time = pdate(arr[index++]);// 8
//				byte[] enbReceivedInterfere = pbytes(arr[index++], 20);// 20
//				byte[] ulPacketLoss = pbytes(arr[index++], 9);// 9
//				byte[] dlpacketLoss = pbytes(arr[index++], 9);// 9
//				XdrSingleSourceCellMR xdr = new XdrSingleSourceCellMR(enbID,
//						cellID, time, enbReceivedInterfere, ulPacketLoss,
//						dlpacketLoss);
//				xdr.setCommon(common);
//				return xdr;
//			}
//		});
//	}
//
//	public static List<XdrSingleSourceGnC> readGncFromFile(String dir)
//			throws Exception {
//		return null;
//	}
//
//	public static List<XdrSingleSourceS10S11> readS11FromFile(String dir)
//			throws Exception {
//		return readXdrFromFile(dir, new XdrParser<XdrSingleSourceS10S11>() {
//			@Override
//			public XdrSingleSourceS10S11 parse(String line) {
//				String[] arr = line.split("\\|");
//				int index = 9;
//				XdrSingleCommon common = getXdrSingleCommon(arr);
//				short procedureType = pshort(arr[index++]);// 1
//				Date startTime = pdate(arr[index++]);// 8
//				Date endTime = pdate(arr[index++]);// 8
//				short procedureStatus = pshort(arr[index++]);// 1
//				int failureCause = pint(arr[index++]);// 2
//				int reqCause = pint(arr[index++]);// 2
//				String userIpv4 = arr[index++];// 4
//				String userIpv6 = arr[index++];// 16
//				String mmeAddress = arr[index++];// 16
//				String sgwOrOldMmeAddress = arr[index++];// 16
//				int mmePort = pint(arr[index++]);// 2
//				int sgwOrOldMmePort = pint(arr[index++]);// 2
//				long mmeControlTeid = plong(arr[index++]);// 4
//				long oldMmeOrSgwControlTeid = plong(arr[index++]);// 4
//				String apn = arr[index++];// 32 getStringSuffixZero
//				short epsBearerNumber = pshort(arr[index++]);// 1
//				List<XdrSingleSourceS10S11.Bearer> bearers = new ArrayList<XdrSingleSourceS10S11.Bearer>();
//				XdrSingleSourceS10S11 xdr = new XdrSingleSourceS10S11(
//						procedureType, startTime, endTime, procedureStatus,
//						failureCause, reqCause, userIpv4, userIpv6, mmeAddress,
//						sgwOrOldMmeAddress, mmePort, sgwOrOldMmePort,
//						mmeControlTeid, oldMmeOrSgwControlTeid, apn,
//						epsBearerNumber, bearers);
//				xdr.setCommon(common);
//				return xdr;
//			}
//		});
//	}
//
//	public static List<XdrSingleSourceS1MME> readS1MMEFromFile(String dir)
//			throws Exception {
//		return readXdrFromFile(dir, new XdrParser<XdrSingleSourceS1MME>() {
//			@Override
//			public XdrSingleSourceS1MME parse(String line) {
//				String[] arr = line.split("\\|");
//				int index = 9;
//				XdrSingleCommon common = getXdrSingleCommon(arr);
//				short procedureType = pshort(arr[index++]);// 1
//				Date startTime = pdate(arr[index++]);// 8
//				Date endTime = pdate(arr[index++]);// 8
//				short procedureStatus = pshort(arr[index++]);// 1
//				int requestCause = pint(arr[index++]);// 2
//				int failureCause = pint(arr[index++]);// 2
//				short keyword1 = pshort(arr[index++]);// 1
//				short keyword2 = pshort(arr[index++]);// 1
//				short keyword3 = pshort(arr[index++]);// 1
//				short keyword4 = pshort(arr[index++]);// 1
//				long mmeUeS1apID = plong(arr[index++]);// 4
//				int oldMmeGroupID = pint(arr[index++]);// 2
//				short oldMmeCode = pshort(arr[index++]);// 1
//				long oldMTmsi = plong(arr[index++]);// 4
//				int mmeGroupID = pint(arr[index++]);// 2
//				short mmeCode = pshort(arr[index++]);// 1
//				long mTmsi = plong(arr[index++]); // 4
//				long tmsi = plong(arr[index++]); // 4
//				byte[] userIpv4 = FormatUtils.getIp(arr[index++], 4);// 4
//				byte[] userIpv6 = FormatUtils.getIp(arr[index++], 16);// 16
//				byte[] mmeIpAdd = FormatUtils.getIp(arr[index++], 16);// 16
//				byte[] enbIpAdd = FormatUtils.getIp(arr[index++], 16);// 16
//				int mmePort = pint(arr[index++]);// 2
//				int enbPort = pint(arr[index++]);// 2
//				int tac = pint(arr[index++]);// 2
//				long cellID = plong(arr[index++]);
//				;// 4
//				int otherTac = pint(arr[index++]);// 2
//				long otherEci = plong(arr[index++]); // 4
//				String apn = arr[index++];// 32
//				short epsBearerNumber = pshort(arr[index++]);// 1
//				List<XdrSingleSourceS1MME.Bearer> bearers = new ArrayList<>();
//				XdrSingleSourceS1MME xdr = new XdrSingleSourceS1MME(
//						procedureType, startTime, endTime, procedureStatus,
//						requestCause, failureCause, keyword1, keyword2,
//						keyword3, keyword4, mmeUeS1apID, oldMmeGroupID,
//						oldMmeCode, oldMTmsi, mmeGroupID, mmeCode, mTmsi, tmsi,
//						userIpv4, userIpv6, mmeIpAdd, enbIpAdd, mmePort,
//						enbPort, tac, cellID, otherTac, otherEci, apn,
//						epsBearerNumber, bearers);
//				xdr.setCommon(common);
//				return xdr;
//			}
//		});
//	}
//
//	public static List<XdrSingleSourceS5S8C> readS5S8FromFile(String dir)
//			throws Exception {
//		return null;
//	}
//
//	public static List<XdrSingleSourceS6a> readS6AFromFile(String dir)
//			throws Exception {
//		return readXdrFromFile(dir, new XdrParser<XdrSingleSourceS6a>() {
//			@Override
//			public XdrSingleSourceS6a parse(String line) {
//				String[] arr = line.split("\\|");
//				int index = 9;
//				XdrSingleCommon common = getXdrSingleCommon(arr);
//				short procedureType = pshort(arr[index++]);// 1
//				Date startTime = pdate(arr[index++]);// 8
//				Date endTime = pdate(arr[index++]);// 8
//				short procedureStatus = pshort(arr[index++]);// 1
//				int cause = pint(arr[index++]);// 2
//				String userIpv4 = arr[index++];// 4
//				String userIpv6 = arr[index++];// 16
//				String mmeAddress = arr[index++];// 16
//				String hssAddress = arr[index++];// 16
//				int mmePort = pint(arr[index++]);// 2
//				int hssPort = pint(arr[index++]);// 2
//				String originRealm = arr[index++];// 44
//				String destinationRealm = arr[index++];// 44
//				String originHost = arr[index++];// 64
//				String destinationHost = arr[index++];// 64
//				long applicationId = plong(arr[index++]);// 4
//				short subscriberStatus = pshort(arr[index++]);// 1
//				short accessRestrictionData = pshort(arr[index++]);// 1
//				XdrSingleSourceS6a xdr = new XdrSingleSourceS6a(procedureType,
//						startTime, endTime, procedureStatus, cause, userIpv4,
//						userIpv6, mmeAddress, hssAddress, mmePort, hssPort,
//						originRealm, destinationRealm, originHost,
//						destinationHost, applicationId, subscriberStatus,
//						accessRestrictionData);
//				xdr.setCommon(common);
//				return xdr;
//			}
//		});
//	}
//
//	public static List<XdrSingleSourceSGs> readSGSFromFile(String dir)
//			throws Exception {
//		return readXdrFromFile(dir, new XdrParser<XdrSingleSourceSGs>() {
//			@Override
//			public XdrSingleSourceSGs parse(String line) {
//				String[] arr = line.split("\\|");
//				int index = 9;
//				XdrSingleCommon common = getXdrSingleCommon(arr);
//				short procedureType = pshort(arr[index++]);// 1
//				Date startTime = pdate(arr[index++]);// 8
//				Date endTime = pdate(arr[index++]);// 8
//				short procedureStatus = pshort(arr[index++]);// 1
//				short sgsCause = pshort(arr[index++]);// 1
//				short rejectCause = pshort(arr[index++]);// 1
//				short cpCause = pshort(arr[index++]);// 1
//				short rpCause = pshort(arr[index++]);// 1
//				String userIpv4 = arr[index++];
//				String userIpv6 = arr[index++];
//				String mmeIpAdd = arr[index++];
//				String mscServerIpAdd = arr[index++];
//				int mmePort = pint(arr[index++]);// 2
//				int mscServerPort = pint(arr[index++]);// 2
//				short serviceIndicator = pshort(arr[index++]);// 1
//				String mmeName = arr[index++];
//				long tmsi = plong(arr[index++]);// 4
//				int newLac = pint(arr[index++]);// 2
//				int oldLac = pint(arr[index++]);// 2
//				int tac = pint(arr[index++]);// 2
//				long cellId = plong(arr[index++]);// 4
//				String callingId = arr[index++];
//				short vlrNameLength = pshort(arr[index++]);// 1
//				String vlrName = arr[index++];
//				XdrSingleSourceSGs xdr = new XdrSingleSourceSGs(procedureType,
//						startTime, endTime, procedureStatus, sgsCause,
//						rejectCause, cpCause, rpCause, userIpv4, userIpv6,
//						mmeIpAdd, mscServerIpAdd, mmePort, mscServerPort,
//						serviceIndicator, mmeName, tmsi, newLac, oldLac, tac,
//						cellId, callingId, vlrNameLength, vlrName);
//				xdr.setCommon(common);
//				return xdr;
//			}
//		});
//	}
//
//	public static List<XdrSingleSourceUEMR> readUEMRFromFile(String dir)
//			throws Exception {
//		return null;
//	}
//
//	public static List<XdrSingleSourceUu> readUUFromFile(String dir)
//			throws Exception {
//		return null;
//	}
//
//	public static List<XdrSingleSourceX2> readX2FromFile(String dir)
//			throws Exception {
//		return null;
//	}
//
//	public static XdrSingleCommon getXdrSingleCommon(String[] arr) {
//		int index = 0;
//		int length = pint(arr[index++]);
//		String city = "";
//		try {
//			city = arr[index++];
//		} catch (Exception e) {
//		}
//		short Interface = pshort(arr[index++]);
//		byte[] xdrId = new byte[0];
//		try {
//			xdrId = Hex.decodeHex(arr[index++].toCharArray());
//		} catch (DecoderException e) {
//		}
//		short rat = pshort(arr[index++]);
//		String imsi = arr[index++];
//		String imei = arr[index++];
//		String msisdn = arr[index++];
//		return new XdrSingleCommon(length, city, Interface, xdrId, rat, imsi,
//				imei, msisdn);
//	}
//
//	public static int pint(String value) {
//		if (null == value || value.length() == 0) {
//			return 0;
//		}
//		return Integer.parseInt(value);
//	}
//
//	public static short pshort(String value) {
//		if (null == value || value.length() == 0) {
//			return 0;
//		}
//		return Short.parseShort(value);
//	}
//
//	public static long plong(String value) {
//		if (null == value || value.length() == 0) {
//			return 0;
//		}
//		return Long.parseLong(value);
//	}
//
//	public static Date pdate(String value) {
//		if (null == value || value.length() == 0) {
//			return null;
//		}
//		return new Date(Long.parseLong(value));
//	}
//
//	public static byte[] pbytes(String value, int len) {
//		try {
//			return Hex.decodeHex(value.toCharArray());
//		} catch (DecoderException e) {
//			e.printStackTrace();
//		}
//		return new byte[len];
//	}
//}
