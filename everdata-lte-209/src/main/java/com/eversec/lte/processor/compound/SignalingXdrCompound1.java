//package com.eversec.lte.processor.compound;
//
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT;
//import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.ATTACH;
//import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.CSFB;
//import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.S1_HANDOVER;
//import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.SERVICE_REQUEST;
//import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.SMS;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S11;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S1MME;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S6A;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.SGS;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UU;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.X2;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.apache.commons.lang3.StringUtils;
//
//import scala.Tuple2;
//
//import com.eversec.lte.constant.SdtpConstants;
//import com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType;
//import com.eversec.lte.constant.SdtpConstants.CompXDRType;
//import com.eversec.lte.model.compound.XdrCompoundCommon;
//import com.eversec.lte.model.compound.XdrCompoundSourceSignaling;
//import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.EpsBearer;
//import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.XdrSingle;
//import com.eversec.lte.model.single.XdrSingleCommon;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS10S11;
//import com.eversec.lte.model.single.XdrSingleSourceS1MME;
//import com.eversec.lte.model.single.XdrSingleSourceS6a;
//import com.eversec.lte.model.single.XdrSingleSourceSGs;
//import com.eversec.lte.model.single.XdrSingleSourceUu;
//import com.eversec.lte.model.single.XdrSingleSourceX2;
//import com.eversec.lte.processor.decoder.XdrCustomBytesDecoder;
//import com.eversec.lte.vo.compound.CompInfo;
//import com.eversec.lte.vo.compound.CompMessage;
//import com.eversec.lte.vo.compound.CompMessage.MrInfo;
//import com.eversec.lte.vo.compound.CompStatus;
//
///**
// * 信令面合成XDR工具类
// * 
// * @author bieremayi
// */
//public class SignalingXdrCompound1 extends Compounder {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -4235657083884450159L;
//
//	private XdrCustomBytesDecoder xdrCustomBytesDecoder = new XdrCustomBytesDecoder();
//
//	@Override
//	public Iterable<CompInfo> call(Tuple2<String, Iterable<byte[]>> t)
//			throws Exception {
//		TreeMap<Long, MrInfo> mrInfos = new TreeMap<>();
//		List<XdrSingleSource> xdrs = new ArrayList<>();
//		for (byte[] load : t._2) {
//			CompMessage compMessage = xdrCustomBytesDecoder.decode(load);
//			mrInfos.putAll(compMessage.getMrInfos());
//			xdrs.addAll(compMessage.getXdrs());
//		}
//		// 1.xdr按照开始时间排序
//		Collections.sort(xdrs, XdrSingleSource.XDR_SINGLE_SOURCE_COMPARATOR);
//		// 2.信令流程开始结束index集合
//		List<IndexPair> indexPairs = getIndexPairs(xdrs);
//		// 3.合成信令XDR
//		CompInfo compInfo = compoundSignalingXdr(xdrs, mrInfos, indexPairs);
//		return Arrays.asList(compInfo);
//	}
//
//	public CompInfo compoundSignalingXdr(List<XdrSingleSource> xdrs,
//			TreeMap<Long, MrInfo> mrInfos, List<IndexPair> indexPairs) {
//		CompInfo compInfo = new CompInfo();
//		int lastIndex = 0;
//		// 合成信令XDR公共信息
//		String city = null;
//		short rat = MAX_UNSIGNED_BYTE;
//		short xdrType = CompXDRType.CXDR_SIGNALING;
//		byte[] xdrId = XdrIDGenerater.getXdrID();
//		String imsi = null;
//		String imei = null;
//		String msisdn = null;
//		// 根据流程开始结束信息输出合成信令XDR
//		for (IndexPair indexPair : indexPairs) {
//			long failureStartTime = Long.MIN_VALUE;// 失败接口开始时间（记录最晚失败开始时间）
//			long failureEndTime = Long.MAX_VALUE;// 失败接口结束时间（记录最早失败结束时间）
//			// 通用信令信息格式
//			short procedureType = (short) indexPair.procedureType;
//			short procedureStatus = (short) indexPair.procedureEndStatus;
//			int mainProcedureType = SdtpConstants.S1MME_PROCEDURE_MAP
//					.get(Integer.valueOf(procedureType));// 合成流程对应的s1mme主流程
//			short failureInterface = MAX_UNSIGNED_BYTE;// 失败接口，失败原因获取规则：1.取所有失败接口中结束时间最靠前（小）的接口填充，如果多个失败xdr结束时间相同，则取开始时间最靠后（大）的xdr填充
//			int failureCause = MAX_UNSIGNED_SHORT;
//			short keyword = MAX_UNSIGNED_BYTE;
//			long enbId = MAX_UNSIGNED_INT;
//			long cellId = MAX_UNSIGNED_INT;
//			int mmeGroupId = MAX_UNSIGNED_SHORT;
//			short mmeCode = MAX_UNSIGNED_BYTE;
//			int tac = MAX_UNSIGNED_SHORT;
//			String userIpv4 = null;
//			String userIpv6 = null;
//			long newEnbId = MAX_UNSIGNED_INT;
//			long newCellId = MAX_UNSIGNED_INT;
//			int newMmeGroupId = MAX_UNSIGNED_SHORT;
//			short newMmeCode = MAX_UNSIGNED_BYTE;
//			int newTac = MAX_UNSIGNED_SHORT;
//			short epsBearerNumber = 0;
//			List<EpsBearer> bearers = new ArrayList<>();
//			short xdrNumber = 0;
//			List<XdrSingle> singles = new ArrayList<>();
//			// Set<Short> bearerIDs = new HashSet<>();
//			boolean first = true;
//			// 生成单接口xdrs
//			for (int i = indexPair.startIndex; i <= indexPair.endIndex; i++) {
//				lastIndex = Math.max(lastIndex, indexPair.endIndex);
//				XdrSingleSource xdr = xdrs.get(i);
//				XdrSingleCommon singleCommon = xdr.getCommon();
//				xdrNumber++;// xdr数量
//				if (first) {
//					city = singleCommon.getCity();
//					rat = singleCommon.getRat();
//					imsi = singleCommon.getImsi();
//					imei = singleCommon.getImei();
//					msisdn = singleCommon.getMsisdn();
//					first = false;
//				}
//				if (StringUtils.isBlank(imei)) {
//					imei = singleCommon.getImei();
//				}
//				if (StringUtils.isBlank(msisdn)) {
//					msisdn = singleCommon.getMsisdn();
//				}
//				int Interface = xdr.getCommon().getInterface();
//				double startlongitude = 0;
//				double startlatitude = 0;
//				double endlongitude = 0;
//				double endlatitude = 0;
//				long produceStartTime = xdr.getProduceStartTime();
//				Entry<Long, MrInfo> startEntry = mrInfos
//						.ceilingEntry(produceStartTime);
//				if (startEntry != null) {
//					startlongitude = startEntry.getValue().getLongitude();
//					startlatitude = startEntry.getValue().getLatitude();
//				}
//				long produceEndTime = xdr.getProduceEndTime();
//				Entry<Long, MrInfo> endEntry = mrInfos
//						.ceilingEntry(produceEndTime);
//				if (endEntry != null) {
//					endlongitude = endEntry.getValue().getLongitude();
//					endlatitude = endEntry.getValue().getLatitude();
//				}
//				short cause = MAX_UNSIGNED_BYTE;
//				XdrSingle single = null;
//				switch (Interface) {
//				case UU:
//					XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
//					if (procedureStatus != 0) {
//						int uuProcedureStatus = uu.getProcedureStatus();
//						if (uuProcedureStatus == 1 || uuProcedureStatus == 255) {
//							long startTime = uu.getStartTime().getTime();
//							long endTime = uu.getEndTime().getTime();
//							if (endTime < failureEndTime) {
//								failureStartTime = startTime;
//								failureEndTime = endTime;
//								failureInterface = (short) Interface;
//								// failureCause
//							} else if (endTime == failureEndTime) {
//								if (startTime > failureStartTime) {
//									failureStartTime = startTime;
//									failureEndTime = endTime;
//									failureInterface = (short) Interface;
//									// failureCause
//								}
//							}
//						}
//					}
//					if (enbId == MAX_UNSIGNED_INT) {
//						enbId = new BigInteger(uu.getEnbID()).longValue();// 最靠前的uu获取
//					}
//					if (procedureType == CompSignalingProcedureType.S1_HANDOVER) {
//						enbId = MAX_UNSIGNED_INT;
//						if (uu.getProcedureType() == 3
//								&& newEnbId == MAX_UNSIGNED_INT) {
//							newEnbId = new BigInteger(uu.getEnbID())
//									.longValue();
//						}
//					}
//					single = new XdrSingle((short) UU, singleCommon.getXdrId(),
//							uu.getProcedureType(), uu.getStartTime(),
//							uu.getEndTime(), startlongitude, startlatitude,
//							endlongitude, endlatitude, uu.getProcedureStatus(),
//							cause);
//					singles.add(single);
//					break;
//				case X2:
//					XdrSingleSourceX2 x2 = (XdrSingleSourceX2) xdr;
//					if (procedureStatus != 0) {
//						int x2ProcedureStatus = x2.getProcedureStatus();// 流程状态
//						if (x2ProcedureStatus == 1 || x2ProcedureStatus == 255) {
//							long startTime = x2.getStartTime().getTime();
//							long endTime = x2.getEndTime().getTime();
//							if (endTime < failureEndTime) {
//								failureStartTime = startTime;
//								failureEndTime = endTime;
//								failureInterface = (short) Interface;
//								failureCause = x2.getFailureCause();
//							} else if (endTime == failureEndTime) {
//								if (startTime > failureStartTime) {
//									failureStartTime = startTime;
//									failureEndTime = endTime;
//									failureInterface = (short) Interface;
//									failureCause = x2.getFailureCause();
//								}
//							}
//						}
//					}
//					cause = checkCause((short) x2.getFailureCause());
//					single = new XdrSingle((short) X2, singleCommon.getXdrId(),
//							x2.getProcedureType(), x2.getStartTime(),
//							x2.getEndTime(), startlongitude, startlatitude,
//							endlongitude, endlatitude, x2.getProcedureStatus(),
//							cause);
//					singles.add(single);
//					break;
//				case S1MME:
//					XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
//					int s1mmeProcedureType = s1mme.getProcedureType();// 当前s1mme流程类型
//					if (procedureStatus != 0) {
//						int s1mmeProcedureStatus = s1mme.getProcedureStatus();// 流程状态
//						if (s1mmeProcedureStatus == 1
//								|| s1mmeProcedureStatus == 255) {
//							long startTime = s1mme.getStartTime().getTime();
//							long endTime = s1mme.getEndTime().getTime();
//							if (endTime < failureEndTime) {
//								failureStartTime = startTime;
//								failureEndTime = endTime;
//								failureInterface = (short) Interface;
//								failureCause = s1mme.getCause();
//							} else if (endTime == failureEndTime) {
//								if (startTime > failureStartTime) {
//									failureStartTime = startTime;
//									failureEndTime = endTime;
//									failureInterface = (short) Interface;
//									failureCause = s1mme.getCause();
//								}
//							}
//						}
//					}
//					// 根据s1mme主流程设置合成信息
//					if (s1mmeProcedureType == mainProcedureType) {
//						keyword = s1mme.getKeyword1();
//						cellId = s1mme.getCellID();
//						mmeGroupId = s1mme.getMmeGroupID();
//						mmeCode = s1mme.getMmeCode();
//						tac = s1mme.getTac();
//						userIpv4 = s1mme.getUserIpv4();
//						userIpv6 = s1mme.getUserIpv6();
//						for (com.eversec.lte.model.single.XdrSingleSourceS1MME.Bearer bearer : s1mme
//								.getBearers()) {
//							epsBearerNumber++;
//							EpsBearer epsBearer = new EpsBearer(
//									bearer.getBearerID(),
//									bearer.getBearerType(),
//									bearer.getBearerQCI(),
//									bearer.getBearerStatus(),
//									bearer.getBearerEnbGtpTeid(),
//									bearer.getBearerSGWGtpTeid());
//							bearers.add(epsBearer);
//						}
//					}
//					// 特殊流程取值
//					if (procedureType == CompSignalingProcedureType.S1_HANDOVER) {// S1切换流程特殊取值
//						if (s1mmeProcedureType == 15) {
//							newCellId = s1mme.getOtherEci();
//						} else if (s1mmeProcedureType == 16) {
//							newMmeGroupId = s1mme.getMmeGroupID();
//							newMmeCode = s1mme.getMmeCode();
//						}
//					} else if (procedureType == CompSignalingProcedureType.TAU) {// TAU流程newtac取值
//						if (s1mmeProcedureType == 5) {
//							newTac = s1mme.getTac();
//						}
//					} else if (procedureType == CompSignalingProcedureType.CSFB) {// CSFB流程keyword取值
//						if (singles.size() > 0) {
//							int csfbStartInterface = singles.get(0)
//									.getInterface();
//							if (csfbStartInterface == SGS) {
//								keyword = 1;
//							} else if (csfbStartInterface == UU) {
//								keyword = 0;
//							}
//						}
//					} else if (procedureType == CompSignalingProcedureType.SMS) {// SMS流程keyword取值
//						if (singles.size() > 0) {
//							int smsStartInterface = singles.get(0)
//									.getInterface();
//							if (smsStartInterface == S1MME) {
//								keyword = 1;
//							} else if (smsStartInterface == SGS) {
//								keyword = 0;
//							}
//						} else if (xdrNumber == 1) {// 以S1MME接口开始且合成流程为SMS则设置keyword为1
//							keyword = 1;
//						}
//					}
//					cause = checkCause((short) s1mme.getCause());
//					single = new XdrSingle((short) S1MME,
//							singleCommon.getXdrId(), s1mme.getProcedureType(),
//							s1mme.getStartTime(), s1mme.getEndTime(),
//							startlongitude, startlatitude, endlongitude,
//							endlatitude, s1mme.getProcedureStatus(), cause);
//					singles.add(single);
//					break;
//				case S6A:
//					XdrSingleSourceS6a s6a = (XdrSingleSourceS6a) xdr;
//					if (procedureStatus != 0) {
//						int s6aProcedureStatus = s6a.getProcedureStatus();// 流程状态
//						if (s6aProcedureStatus == 1
//								|| s6aProcedureStatus == 255) {
//							long startTime = s6a.getStartTime().getTime();
//							long endTime = s6a.getEndTime().getTime();
//							if (endTime < failureEndTime) {
//								failureStartTime = startTime;
//								failureEndTime = endTime;
//								failureInterface = (short) Interface;
//								failureCause = s6a.getCause();
//							} else if (endTime == failureEndTime) {
//								if (startTime > failureStartTime) {
//									failureStartTime = startTime;
//									failureEndTime = endTime;
//									failureInterface = (short) Interface;
//									failureCause = s6a.getCause();
//								}
//							}
//						}
//					}
//					cause = checkCause((short) s6a.getCause());
//					single = new XdrSingle((short) S6A,
//							singleCommon.getXdrId(), s6a.getProcedureType(),
//							s6a.getStartTime(), s6a.getEndTime(),
//							startlongitude, startlatitude, endlongitude,
//							endlatitude, s6a.getProcedureStatus(), cause);
//					singles.add(single);
//					break;
//				case S11:
//					XdrSingleSourceS10S11 s11 = (XdrSingleSourceS10S11) xdr;
//					if (procedureStatus != 0) {
//						int s11ProcedureStatus = s11.getProcedureStatus();// 流程状态
//						if (s11ProcedureStatus == 1
//								|| s11ProcedureStatus == 255) {
//							long startTime = s11.getStartTime().getTime();
//							long endTime = s11.getEndTime().getTime();
//							if (endTime < failureEndTime) {
//								failureStartTime = startTime;
//								failureEndTime = endTime;
//								failureInterface = (short) Interface;
//								failureCause = s11.getFailureCause();
//							} else if (endTime == failureEndTime) {
//								if (startTime > failureStartTime) {
//									failureStartTime = startTime;
//									failureEndTime = endTime;
//									failureInterface = (short) Interface;
//									failureCause = s11.getFailureCause();
//								}
//							}
//						}
//					}
//					cause = checkCause((short) s11.getFailureCause());
//					single = new XdrSingle((short) S11,
//							singleCommon.getXdrId(), s11.getProcedureType(),
//							s11.getStartTime(), s11.getEndTime(),
//							startlongitude, startlatitude, endlongitude,
//							endlatitude, s11.getProcedureStatus(), cause);
//					singles.add(single);
//					break;
//				case SGS:
//					XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
//					// 设置faliureInterface,failureCause
//					if (procedureStatus != 0) {
//						int sgsProcedureStatus = sgs.getProcedureStatus();// 流程状态
//						if (sgsProcedureStatus == 1
//								|| sgsProcedureStatus == 255) {
//							long startTime = sgs.getStartTime().getTime();
//							long endTime = sgs.getEndTime().getTime();
//							if (endTime < failureEndTime) {
//								failureStartTime = startTime;
//								failureEndTime = endTime;
//								failureInterface = (short) Interface;
//								if (sgs.getSgsCause() != MAX_UNSIGNED_BYTE) {
//									failureCause = sgs.getSgsCause();
//								} else if (sgs.getRejectCause() != MAX_UNSIGNED_BYTE) {
//									failureCause = sgs.getRejectCause();
//								}
//							} else if (endTime == failureEndTime) {
//								if (startTime > failureStartTime) {
//									failureStartTime = startTime;
//									failureEndTime = endTime;
//									failureInterface = (short) Interface;
//									if (sgs.getSgsCause() != MAX_UNSIGNED_BYTE) {
//										failureCause = sgs.getSgsCause();
//									} else if (sgs.getRejectCause() != MAX_UNSIGNED_BYTE) {
//										failureCause = sgs.getRejectCause();
//									}
//								}
//							}
//						}
//					}
//					cause = checkCause((short) sgs.getSgsCause());
//					// 特殊流程keyword取值
//					if (xdrNumber == 1) {// 以SGS接口开始且合成流程为CSFB则设置keyword为1
//						if (procedureType == CompSignalingProcedureType.CSFB) {
//							keyword = 1;
//						}
//					}
//					single = new XdrSingle((short) SGS,
//							singleCommon.getXdrId(), sgs.getProcedureType(),
//							sgs.getStartTime(), sgs.getEndTime(),
//							startlongitude, startlatitude, endlongitude,
//							endlatitude, sgs.getProcedureStatus(), cause);
//					singles.add(single);
//					break;
//				default:
//					break;
//				}
//			}
//			if (xdrNumber > 0 && xdrNumber <= 255) {
//				// 设置合成信息XDR时间、经纬度信息
//				Date startTime = null;
//				Date endTime = null;
//				double startLongitude = 0;
//				double startLatitude = 0;
//				double endLongitude = 0;
//				double endLatitude = 0;
//				XdrSingle startXdr = singles.get(0);
//				XdrSingle endXdr = singles.get(indexPair.procedureEndIndex
//						- indexPair.startIndex);
//				startTime = startXdr.getStartTime();
//				endTime = endXdr.getEndTime();
//				startLongitude = startXdr.getStartlongitude();
//				startLatitude = startXdr.getStartlatitude();
//				endLongitude = endXdr.getEndlongitude();
//				endLatitude = endXdr.getEndlatitude();
//				XdrCompoundSourceSignaling compSignalingXdr = new XdrCompoundSourceSignaling(
//						procedureType, startTime, endTime, startLongitude,
//						startLatitude, endLongitude, endLatitude,
//						procedureStatus, failureInterface, failureCause,
//						keyword, enbId, cellId, mmeGroupId, mmeCode, tac,
//						userIpv4, userIpv6, newEnbId, newCellId, newMmeGroupId,
//						newMmeCode, newTac, epsBearerNumber, bearers,
//						xdrNumber, singles);
//				Collections.sort(singles,
//						XdrCompoundSourceSignaling.XDR_SINGLE_COMPARATOR);// 重新按照开始时间排序
//				XdrCompoundCommon common = new XdrCompoundCommon(0, city, rat,
//						xdrType, xdrId, imsi, imei, msisdn);
//				common.setLength(common.getBodyLength()
//						+ compSignalingXdr.getBodyLength());// 重设xdr length
//				compSignalingXdr.setCommon(common);
//				compInfo.getCxdrs().add(compSignalingXdr);
//			}
//		}
//		// 将未成功合成的xdr保存到kafka(topic : signal-cxdr-with-uemr-tryagain)
//		if (indexPairs.size() > 0) {
//			lastIndex++;
//		}
//		for (int i = lastIndex; i < xdrs.size(); i++) {
//			compInfo.getXdrs().add(xdrs.get(i));
//		}
//		return compInfo;
//	}
//
//	/**
//	 * 单接口xdr中cause为2个字节，合成xdr单接口为一个字节，需要判断是否溢出
//	 * 
//	 * @param cause
//	 * @return
//	 */
//	private short checkCause(short cause) {
//		if (cause < 0 || cause > MAX_UNSIGNED_BYTE) {
//			cause = MAX_UNSIGNED_BYTE;
//		}
//		return cause;
//	}
//
//	public static class IndexPair {
//		int startIndex;
//		int endIndex;
//		int procedureType;
//		int procedureEndIndex;
//		int procedureEndStatus;
//
//		public IndexPair(int startIndex, int endIndex, int procedureType,
//				int procedureEndIndex, int procedureEndStatus) {
//			super();
//			this.startIndex = startIndex;
//			this.endIndex = endIndex;
//			this.procedureType = procedureType;
//			this.procedureEndIndex = procedureEndIndex;
//			this.procedureEndStatus = procedureEndStatus;
//		}
//	}
//
//	/**
//	 * 合成所需有效信息
//	 * 
//	 * @author bieremayi
//	 * 
//	 */
//	public static class CompUsefulInfo {
//		int procedureType = MAX_UNSIGNED_BYTE;
//		int procedureStatus = MAX_UNSIGNED_BYTE;
//		long procedureStartTime = -1;
//		long procedureEndTime = -1;
//		int Interface = -1;
//		short keyword = MAX_UNSIGNED_BYTE;
//		short serviceIndicator = MAX_UNSIGNED_BYTE;
//
//		public CompUsefulInfo(int procedureType, int procedureStatus,
//				long procedureEndTime, int Interface, short keyword,
//				long procedureStartTime) {
//			super();
//			this.procedureType = procedureType;
//			this.procedureStatus = procedureStatus;
//			this.procedureEndTime = procedureEndTime;
//			this.Interface = Interface;
//			this.keyword = keyword;
//			this.procedureStartTime = procedureStartTime;
//		}
//
//		public int getProcedureType() {
//			return procedureType;
//		}
//
//		public void setProcedureType(int procedureType) {
//			this.procedureType = procedureType;
//		}
//
//		public int getProcedureStatus() {
//			return procedureStatus;
//		}
//
//		public void setProcedureStatus(int procedureStatus) {
//			this.procedureStatus = procedureStatus;
//		}
//
//		public long getProcedureEndTime() {
//			return procedureEndTime;
//		}
//
//		public void setProcedureEndTime(long procedureEndTime) {
//			this.procedureEndTime = procedureEndTime;
//		}
//
//		public long getProcedureStartTime() {
//			return procedureStartTime;
//		}
//
//		public void setProcedureStartTime(long procedureStartTime) {
//			this.procedureStartTime = procedureStartTime;
//		}
//
//		public int getInterface() {
//			return Interface;
//		}
//
//		public void setInterface(int interface1) {
//			Interface = interface1;
//		}
//
//		public short getKeyword() {
//			return keyword;
//		}
//
//		public void setKeyword(short keyword) {
//			this.keyword = keyword;
//		}
//
//		public short getServiceIndicator() {
//			return serviceIndicator;
//		}
//
//		public void setServiceIndicator(short serviceIndicator) {
//			this.serviceIndicator = serviceIndicator;
//		}
//	}
//
//	/**
//	 * 获取合成信令所需的信息
//	 * 
//	 * @param xdr
//	 * @return
//	 */
//	private CompUsefulInfo getCompUsefulInfo(XdrSingleSource xdr) {
//		XdrSingleCommon common = xdr.getCommon();
//		int Interface = common.getInterface();
//		int procedureType = MAX_UNSIGNED_BYTE;
//		int procedureStatus = MAX_UNSIGNED_BYTE;
//		long procedureStartTime = -1;
//		long procedureEndTime = -1;
//		short keyword = MAX_UNSIGNED_BYTE;
//		short serviceIndicator = MAX_UNSIGNED_BYTE;
//		switch (Interface) {
//		case UU:
//			XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
//			procedureType = uu.getProcedureType();
//			procedureStatus = uu.getProcedureStatus();
//			procedureEndTime = uu.getProduceEndTime();
//			keyword = uu.getKeyword1();
//			procedureStartTime = uu.getProduceStartTime();
//			break;
//		case X2:
//			XdrSingleSourceX2 x2 = (XdrSingleSourceX2) xdr;
//			procedureType = x2.getProcedureType();
//			procedureStatus = x2.getProcedureStatus();
//			procedureEndTime = x2.getProduceEndTime();
//			procedureStartTime = x2.getProduceStartTime();
//			break;
//		case S1MME:
//			XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
//			procedureType = s1mme.getProcedureType();
//			procedureStatus = s1mme.getProcedureStatus();
//			procedureEndTime = s1mme.getProduceEndTime();
//			keyword = s1mme.getKeyword1();
//			procedureStartTime = s1mme.getProduceStartTime();
//			break;
//		case S6A:
//			XdrSingleSourceS6a s6a = (XdrSingleSourceS6a) xdr;
//			procedureType = s6a.getProcedureType();
//			procedureStatus = s6a.getProcedureStatus();
//			procedureEndTime = s6a.getProduceEndTime();
//			procedureStartTime = s6a.getProduceStartTime();
//			break;
//		case S11:
//			XdrSingleSourceS10S11 s11 = (XdrSingleSourceS10S11) xdr;
//			procedureType = s11.getProcedureType();
//			procedureStatus = s11.getProcedureStatus();
//			procedureEndTime = s11.getProduceEndTime();
//			procedureStartTime = s11.getProduceStartTime();
//			break;
//		case SGS:
//			XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
//			procedureType = sgs.getProcedureType();
//			procedureStatus = sgs.getProcedureStatus();
//			procedureEndTime = sgs.getProduceEndTime();
//			serviceIndicator = sgs.getServiceIndicator();
//			procedureStartTime = sgs.getProduceStartTime();
//			break;
//		default:
//			break;
//		}
//		CompUsefulInfo info = new CompUsefulInfo(procedureType,
//				procedureStatus, procedureEndTime, Interface, keyword,
//				procedureStartTime);
//		info.setServiceIndicator(serviceIndicator);
//		return info;
//	}
//
//	/**
//	 * 更新合成状态
//	 * 
//	 * @param isCheckEndTime
//	 * @param checkProcedure
//	 * @param checkEndTime
//	 * @param procedureEndIndex
//	 * @param procedureEndStatus
//	 * @param compStatus
//	 * @return
//	 */
//	private CompStatus updateStatus(boolean isCheckEndTime, int checkProcedure,
//			long checkEndTime, int procedureEndIndex, int procedureEndStatus,
//			CompStatus compStatus) {
//		if (compStatus == null) {
//			compStatus = new CompStatus();
//		}
//		compStatus.setCheckEndTime(isCheckEndTime);
//		compStatus.setCheckEndTime(checkEndTime);
//		compStatus.setCheckProcedure(checkProcedure);
//		compStatus.setProcedureEndIndex(procedureEndIndex);
//		compStatus.setProcedureEndStatus(procedureEndStatus);
//		return compStatus;
//	}
//
//	/**
//	 * 重置合成状态
//	 * 
//	 * @param compStatus
//	 * @return
//	 */
//	private CompStatus resetStatus(CompStatus compStatus) {
//		return updateStatus(false, -1, Long.MIN_VALUE, -1, 0, compStatus);
//	}
//
//	/**
//	 * 流程结束时更新index
//	 * 
//	 * @param xdrs
//	 *            单接口xdr集合
//	 * @param startMap
//	 *            保存开始流程index哈希表
//	 * @param indexPairs
//	 *            流程开始结束信息集合
//	 * @param compStatus
//	 *            合成状态信息
//	 * @param size
//	 *            单接口xdr数量
//	 * @param index
//	 *            当前index
//	 * @param checkProcedure
//	 *            待确认的合成流程
//	 * @param procedureEndTime
//	 *            合成流程结束时间
//	 * @param procedureStatus
//	 *            合成流程状态
//	 * @return
//	 */
//	private int updateIndexWhenProcedureEnd(List<XdrSingleSource> xdrs,
//			Map<Integer, Integer> startMap, List<IndexPair> indexPairs,
//			CompStatus compStatus, int size, int index, int checkProcedure,
//			long procedureEndTime, int procedureStatus) {
//		Integer startIndex = startMap.get(checkProcedure);
//		if (startIndex == null) {// 没有开始条件则直接返回
//			return index;
//		}
//		compStatus = updateStatus(true, checkProcedure, procedureEndTime,
//				index, procedureStatus, compStatus);
//		while (compStatus.isCheckEndTime()) {
//			if (index + 1 < size) {
//				CompUsefulInfo tmp = getCompUsefulInfo(xdrs.get(index + 1));
//				if (tmp.procedureEndTime > compStatus.getCheckEndTime()) {
//					indexPairs.add(new IndexPair(startIndex, index,
//							checkProcedure, compStatus.getProcedureEndIndex(),
//							compStatus.getProcedureEndStatus()));
//					startMap.remove(checkProcedure);
//					compStatus = resetStatus(compStatus);
//				} else {
//					index++;
//				}
//			} else {
//				startMap.remove(checkProcedure);
//				compStatus = resetStatus(compStatus);
//			}
//		}
//		return index;
//	}
//
//	/**
//	 * 通过检查最接近的S1MME主流程来判断当前开始条件属于何种合成流程
//	 * 
//	 * @param xdrs
//	 * @param startMap
//	 * @param size
//	 * @param index
//	 */
//	private int checkUuLastestS1mme(List<XdrSingleSource> xdrs,
//			Map<Integer, Integer> startMap, int size, int index) {
//		boolean checkLatestS1mme = true;
//		int startIndex = index;
//		while (checkLatestS1mme) {
//			if (index + 1 < size) {
//				CompUsefulInfo tmp = getCompUsefulInfo(xdrs.get(index + 1));
//				int checkInterface = tmp.Interface;
//				int checkProcedureType = tmp.procedureType;
//				if (checkInterface == S1MME) {
//					switch (tmp.procedureType) {
//					case 1:
//						startMap.put(ATTACH, startIndex);// ATTACH流程开始
//						checkLatestS1mme = false;
//						break;
//					case 2:
//						startMap.put(SERVICE_REQUEST, startIndex);// SERVICE_REQUEST流程开始
//						checkLatestS1mme = false;
//						break;
//					case 3:
//						if (tmp.keyword == 0 || tmp.keyword == 2
//								|| tmp.keyword == 3 || tmp.keyword == 4) {
//							startMap.put(CSFB, startIndex);// CSFB主叫流程开始
//						}
//						checkLatestS1mme = false;
//						break;
//					case 7:
//						// 判断是否属于attach流程开始
//						long checkStartTime = tmp.procedureStartTime;
//						while (true) {
//							index++;
//							if (index + 1 < size) {
//								tmp = getCompUsefulInfo(xdrs.get(index + 1));
//								if (checkStartTime != tmp.procedureStartTime) {
//									break;
//								} else {
//									if (tmp.procedureType == 1) {
//										startMap.put(ATTACH, startIndex);
//										break;
//									}
//								}
//							} else {
//								break;
//							}
//						}
//						checkLatestS1mme = false;
//						break;
//					default:
//						checkLatestS1mme = false;
//						break;
//					}
//				} else if (checkInterface == UU) {
//					if (checkProcedureType == 1) {
//						System.out
//								.println("uu procedure type 1  reset start index ");
//						startIndex = index + 1;// 重置开始index
//					}
//					index++;
//				} else {
//					index++;
//				}
//			} else {
//				checkLatestS1mme = false;
//			}
//		}
//		return index;
//	}
//
//	/**
//	 * 通过检查最接近的S1MME主流程来判断当前开始条件属于何种合成流程
//	 * 
//	 * @param xdrs
//	 * @param startMap
//	 * @param size
//	 * @param index
//	 */
//	private int checkSgsLastestS1mme(List<XdrSingleSource> xdrs,
//			Map<Integer, Integer> startMap, int size, int index) {
//		boolean checkLatestS1mme = true;
//		int startIndex = index;
//		while (checkLatestS1mme) {
//			if (index + 1 < size) {
//				CompUsefulInfo tmp = getCompUsefulInfo(xdrs.get(index + 1));
//				int checkInterface = tmp.Interface;
//				int checkProcedureType = tmp.procedureType;
//				if (checkInterface == S1MME) {
//					switch (tmp.procedureType) {
//					case 31:
//						if (tmp.keyword == 0) {
//							startMap.put(SMS, startIndex);// SMS下行开始流程（收短信）
//						}
//						checkLatestS1mme = false;
//						break;
//					default:
//						checkLatestS1mme = false;
//						break;
//					}
//				} else if (checkInterface == SGS) {
//					if (checkProcedureType == 3) {
//						if (tmp.getServiceIndicator() == 3) {
//							System.out
//									.println("sgs procedure type 3 ,service indicator 3 reset start index ");
//							startIndex = index + 1;// 重置开始index
//						}
//					}
//					index++;
//				} else {
//					index++;
//				}
//			} else {
//				checkLatestS1mme = false;
//			}
//		}
//		return index;
//	}
//
//	/**
//	 * 获取完整流程开始结束index
//	 * 
//	 * @param xdrs
//	 * @return
//	 */
//	public List<IndexPair> getIndexPairs(List<XdrSingleSource> xdrs) {
//		Map<Integer, Integer> startMap = new ConcurrentHashMap<>(); // 保存各流程开始流程index
//		List<IndexPair> indexPairs = new ArrayList<>(); // 保存合成信令流程开始结束index
//		CompStatus compStatus = new CompStatus();// 保存合成信息所需状态
//		int size = xdrs.size();
//		for (int index = 0; index < size; index++) {
//			XdrSingleSource xdr = xdrs.get(index);
//			if (StringUtils.isBlank(xdr.getCommon().getImsi())) {
//				continue;
//			}
//			CompUsefulInfo compUsefulInfo = getCompUsefulInfo(xdr);
//			int Interface = compUsefulInfo.Interface;
//			int procedureType = compUsefulInfo.procedureType;
//			int procedureStatus = compUsefulInfo.procedureStatus;
//			long procedureEndTime = compUsefulInfo.procedureEndTime;
//			int keyword = compUsefulInfo.keyword;
//			// 获取各流程开始结束index
//			switch (Interface) {
//			case UU:
//				if (procedureType == 1) {
//					index = checkUuLastestS1mme(xdrs, startMap, size, index);
//				}
//				break;
//			case S1MME:
//				switch (procedureType) {
//				case 1:
//					if (procedureStatus == 0 || procedureStatus == 1
//							|| procedureStatus == 255) {// ATTACH结束流程(important!!)
//						index = updateIndexWhenProcedureEnd(xdrs, startMap,
//								indexPairs, compStatus, size, index, ATTACH,
//								procedureEndTime, procedureStatus);
//					}
//					break;
//				case 2:
//					if (procedureStatus == 0 || procedureStatus == 1
//							|| procedureStatus == 255) {// SERVICE_REQUEST结束流程(important!!)
//						index = updateIndexWhenProcedureEnd(xdrs, startMap,
//								indexPairs, compStatus, size, index,
//								SERVICE_REQUEST, procedureEndTime,
//								procedureStatus);
//					}
//					break;
//				case 3:
//					if (keyword == 1) {// CSFB被叫结束流程(important!!)
//						if (procedureStatus == 1 || procedureStatus == 255) {
//							index = updateIndexWhenProcedureEnd(xdrs, startMap,
//									indexPairs, compStatus, size, index, CSFB,
//									procedureEndTime, procedureStatus);
//						}
//					}
//					break;
//				case 15:
//					startMap.put(S1_HANDOVER, index);// S1_HANDOVER开始流程(important!!)
//					if (procedureStatus == 1 || procedureStatus == 255) {// S1_HANDOVER结束流程(important!!)
//						index = updateIndexWhenProcedureEnd(xdrs, startMap,
//								indexPairs, compStatus, size, index,
//								S1_HANDOVER, procedureEndTime, procedureStatus);
//					}
//					break;
//				case 16:
//					if (procedureStatus == 0 || procedureStatus == 1
//							|| procedureStatus == 255) {// S1_HANDOVER结束流程(important!!)
//						index = updateIndexWhenProcedureEnd(xdrs, startMap,
//								indexPairs, compStatus, size, index,
//								S1_HANDOVER, procedureEndTime, procedureStatus);
//					}
//					break;
//				case 19:
//					if (procedureStatus == 0 || procedureStatus == 255) {// CSFB结束流程(important!!)
//						index = updateIndexWhenProcedureEnd(xdrs, startMap,
//								indexPairs, compStatus, size, index, CSFB,
//								procedureEndTime, procedureStatus);
//					}
//					break;
//				case 31:
//					if (keyword == 1) {
//						startMap.put(SMS, index);// SMS上行开始流程（发短信）
//						if (procedureStatus == 0 || procedureStatus == 1
//								|| procedureStatus == 255) {// SMS结束流程
//							index = updateIndexWhenProcedureEnd(xdrs, startMap,
//									indexPairs, compStatus, size, index, SMS,
//									procedureEndTime, procedureStatus);
//						}
//					}
//					break;
//				default:
//					break;
//				}
//				break;
//			case SGS:
//				if (procedureType == 1) {
//					if (compUsefulInfo.getServiceIndicator() == 1) {
//						startMap.put(CSFB, index);// CSFB开始流程(important!!)
//						if (procedureStatus == 1 || procedureStatus == 255) {// CSFB结束流程(important!!)
//							index = updateIndexWhenProcedureEnd(xdrs, startMap,
//									indexPairs, compStatus, size, index, CSFB,
//									procedureEndTime, procedureStatus);
//						}
//					}
//				} else if (procedureType == 3) {
//					if (compUsefulInfo.getServiceIndicator() == 2) {// SMS下行开始流程（收短信）
//						checkSgsLastestS1mme(xdrs, startMap, size, index);
//					}
//				} else if (procedureType == 4) {
//					if (compUsefulInfo.getServiceIndicator() == 2) {
//						if (procedureStatus == 0 || procedureStatus == 1
//								|| procedureStatus == 255) {// SMS结束流程
//							index = updateIndexWhenProcedureEnd(xdrs, startMap,
//									indexPairs, compStatus, size, index, SMS,
//									procedureEndTime, procedureStatus);
//						}
//					}
//				}
//				break;
//			default:
//				break;
//			}
//		}
//		return indexPairs;
//	}
//}
