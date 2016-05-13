package com.eversec.lte.processor.compound;

import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.ATTACH;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.CSFB;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.DEDICATED_EPS_BEARER_CONTEXT_ACTIVATION;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.DETACH;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.EPS_BEARER_CONTEXT_DEACTIVATION;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.EPS_BEARER_CONTEXT_MODIFICATION;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.EPS_BEARER_RELEASE;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.EPS_BEARER_RESOURCE_ALLOCATION;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.EPS_BEARER_RESOURCE_MODIFY;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.PAGING;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.PDN_CONNECTIVITY;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.PDN_DISCONNECTION;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.S1_HANDOVER;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.SERVICE_REQUEST;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.SMS;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.TAU;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.UE_CONTEXT_RELEASE;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.UNKNOWN;
import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.X2_HANDOVER;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S11;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S1MME;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S6A;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.SGS;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UU;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.X2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import scala.Tuple2;

import com.eversec.common.constant.CommonConstants;
import com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType;
import com.eversec.lte.constant.SdtpConstants.CompXDRType;
import com.eversec.lte.model.compound.XdrCompoundCommon;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.EpsBearer;
import com.eversec.lte.model.compound.XdrCompoundSourceSignaling.XdrSingle;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.processor.decoder.XdrCustomBytesDecoder;
import com.eversec.lte.utils.FormatUtils;
import com.eversec.lte.vo.compound.CompIndexInfo;
import com.eversec.lte.vo.compound.CompInfo;
import com.eversec.lte.vo.compound.CompMessage;
import com.eversec.lte.vo.compound.CompMessage.MrInfo;
import com.eversec.lte.vo.compound.MainProcedureInfo;

/**
 * 信令面合成XDR工具类
 * 
 * @author bieremayi
 */
public class SignalingXdrCompound extends Compounder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4235657083884450159L;

	XdrCustomBytesDecoder decoder = new XdrCustomBytesDecoder();

	public static long PAGING_S1MME_AFTER_MILLS = 1000;
	public static long DETACH_S1MME_AFTER_MILLS = 1000;
	public static long PDN_CONNECTIVITY_S1MME_BEFORE_MILLS = 0;
	public static long EPS_BEARER_CONTEXT_DEACTIVATION_S1MME_BEFORE_MILLS = 1000;
	public static long EPS_BEARER_CONTEXT_MODIFICATION_S1MME_BEFORE_MILLS = 1000;
	public static long DEDICATED_EPS_BEARER_CONTEXT_ACTIVATION_S1MME_BEFORE_MILLS = 1000;
	public static long S1_HANDOVER_S1MME_AFTER_MILLS = 1000;
	public static long UE_CONTEXT_RELEASE_S1MME_BEFORE_MILLS = 1000;
	public static long EPS_BEARER_RELEASE_S1MME_BEFORE_MILLS = 1000;
	public static long CSFB_S1MME_BEFORE_MILLS = 1000;
	public static long CSFB_SGS_BEFORE_MILLS = 10000;
	public static long CSFB_SGS_AFTER_MILLS = 0;
	public static long SMS_SGS_BEFORE_MILLS = 1000;
	public static long SMS_SGS_AFTER_MILLS = 1000;
	public static long DIFF_MILLS = 10000;

	public static long ATTACH_UU_AFTER_MILLS = 1000;

	@Override
	public Iterable<CompInfo> call(Tuple2<String, Iterable<byte[]>> t)
			throws Exception {
		TreeMap<Long, MrInfo> mrInfos = new TreeMap<>();
		List<XdrSingleSource> xdrs = new ArrayList<>();
		for (byte[] load : t._2) {
			CompMessage compMessage = decoder.decode(load);
			mrInfos.putAll(compMessage.getMrInfos());
			xdrs.addAll(compMessage.getXdrs());
		}
		/*
		 * 主流程信息集合
		 */
		List<MainProcedureInfo> mpInfos = new ArrayList<>();
		/*
		 * 存储s1mme xdr位置信息 key : lowID(xdr低8位HEX) value : 单接口s1mme xdr index集合
		 */
		Map<String, List<XdrSingleSource>> lowIDS1mmeMap = new HashMap<>();

		/*
		 * 存储s1mme xdr位置信息 key : 开始时间时间戳 value : 单接口 s1mme xdr index集合
		 */
		TreeMap<Long, List<XdrSingleSource>> s1mmeStartTimeMap = new TreeMap<>();

		/*
		 * 存储s6a xdr位置信息 key : 开始时间时间戳 value : 单接口 s6a xdr index集合
		 */
		TreeMap<Long, List<XdrSingleSource>> s6aStartTimeMap = new TreeMap<>();

		/*
		 * 存储sgs xdr位置信息 key : 开始时间时间戳 value : 单接口sgs xdr index集合
		 */
		TreeMap<Long, List<XdrSingleSource>> sgsStartTimeMap = new TreeMap<>();
		/*
		 * 存储sgs xdr位置信息 key : 开始时间时间戳 value : 单接口s11 xdr index集合
		 */
		TreeMap<Long, List<XdrSingleSource>> s11StartTimeMap = new TreeMap<>();
		/*
		 * 存储uu xdr位置信息 key : 开始时间时间戳 value : 单接口uu xdr index集合
		 */
		TreeMap<Long, List<XdrSingleSource>> uuStartTimeMap = new TreeMap<>();

		/*
		 * 存储x2 xdr位置信息 key : 开始时间时间戳 value : 单接口x2 xdr index集合
		 */
		TreeMap<Long, List<XdrSingleSource>> x2StartTimeMap = new TreeMap<>();

		prepare(xdrs, mpInfos, lowIDS1mmeMap, s1mmeStartTimeMap,
				s6aStartTimeMap, sgsStartTimeMap, s11StartTimeMap,
				uuStartTimeMap, x2StartTimeMap);
		List<CompIndexInfo> compIndexs = search(xdrs, mpInfos, lowIDS1mmeMap,
				s1mmeStartTimeMap, s6aStartTimeMap, sgsStartTimeMap,
				s11StartTimeMap, uuStartTimeMap, x2StartTimeMap);
		CompInfo compInfo = compound(xdrs, mrInfos, compIndexs);
		return Arrays.asList(compInfo);
	}

	/**
	 * 一些准备操作
	 * 
	 * @param xdrs
	 * @param x2StartTimeMap
	 * @param uuStartTimeMap
	 */
	private void prepare(List<XdrSingleSource> xdrs,
			List<MainProcedureInfo> mpInfos,
			Map<String, List<XdrSingleSource>> lowIDS1mmeMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeS1mmeMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeS6aMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeSgsMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeS11Map,
			TreeMap<Long, List<XdrSingleSource>> startTimeUuMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeX2Map) {
		int size = xdrs.size();
		for (int i = 0; i < size; i++) {
			XdrSingleSource xdr = xdrs.get(i);
			XdrSingleCommon common = xdr.getCommon();
			int Interface = common.getInterface();
			switch (Interface) {
			case S1MME:
				XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
				String lowID = s1mme.getLowID();
				// 保存主流程s1mme xdr
				int compSigProType = getCompSignalingProcedureType(s1mme);
				if (compSigProType != UNKNOWN) {
					String rule1 = StringUtils.join(
							new String[] {
									String.valueOf(s1mme.getMmeUeS1apID()),
									String.valueOf(s1mme.getMmeGroupID()),
									String.valueOf(s1mme.getMmeCode()) },
							CommonConstants.UNDERLINE);
					mpInfos.add(new MainProcedureInfo(s1mme, compSigProType,
							lowID, s1mme.getProduceStartTime(), s1mme
									.getProduceEndTime(), s1mme.getKeyword1(),
							rule1));
				}
				// 保存s1mme xdr
				List<XdrSingleSource> lowIDList = lowIDS1mmeMap.get(lowID);
				if (lowIDList == null) {
					lowIDList = new ArrayList<>();
					lowIDS1mmeMap.put(lowID, lowIDList);
				}
				lowIDList.add(s1mme);
				long s1mmeStartTime = s1mme.getProduceStartTime();
				List<XdrSingleSource> s1mmeList = startTimeS1mmeMap
						.get(s1mmeStartTime);
				if (s1mmeList == null) {
					s1mmeList = new ArrayList<>();
					startTimeS1mmeMap.put(s1mmeStartTime, s1mmeList);
				}
				s1mmeList.add(s1mme);
				break;
			case S6A:
				XdrSingleSourceS6a s6a = (XdrSingleSourceS6a) xdr;
				long s6aStartTime = s6a.getProduceStartTime();
				List<XdrSingleSource> s6aList = startTimeS6aMap
						.get(s6aStartTime);
				if (s6aList == null) {
					s6aList = new ArrayList<>();
					startTimeS6aMap.put(s6aStartTime, s6aList);
				}
				s6aList.add(s6a);
				break;
			case SGS:
				XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
				long sgsStartTime = sgs.getProduceStartTime();
				List<XdrSingleSource> sgsList = startTimeSgsMap
						.get(sgsStartTime);
				if (sgsList == null) {
					sgsList = new ArrayList<>();
					startTimeSgsMap.put(sgsStartTime, sgsList);
				}
				sgsList.add(sgs);
				break;
			case S11:
				XdrSingleSourceS10S11 s11 = (XdrSingleSourceS10S11) xdr;
				long s11StartTime = s11.getProduceStartTime();
				List<XdrSingleSource> s11List = startTimeS11Map
						.get(s11StartTime);
				if (s11List == null) {
					s11List = new ArrayList<>();
					startTimeS11Map.put(s11StartTime, s11List);
				}
				s11List.add(s11);
				break;
			case UU:
				XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
				long uuStartTime = uu.getProduceStartTime();
				List<XdrSingleSource> uuList = startTimeUuMap.get(uuStartTime);
				if (uuList == null) {
					uuList = new ArrayList<>();
					startTimeUuMap.put(uuStartTime, uuList);
				}
				uuList.add(uu);
				break;
			case X2:
				XdrSingleSourceX2 x2 = (XdrSingleSourceX2) xdr;
				long x2StartTime = x2.getProduceStartTime();
				List<XdrSingleSource> x2List = startTimeX2Map.get(x2StartTime);
				if (x2List == null) {
					x2List = new ArrayList<>();
					startTimeX2Map.put(x2StartTime, x2List);
				}
				x2List.add(x2);
				break;
			default:
				break;
			}

		}
	}

	private CompInfo compound(List<XdrSingleSource> xdrs,
			TreeMap<Long, MrInfo> mrInfos, List<CompIndexInfo> compIndexs) {
		CompInfo compInfo = new CompInfo();
		long maxStartTime = Long.MIN_VALUE;
		// 根据流程开始结束信息输出合成信令XDR
		for (CompIndexInfo compIndex : compIndexs) {
			XdrSingleSourceS1MME mainS1mmeXdr = (XdrSingleSourceS1MME) compIndex
					.getMainXdr();
			if(mainS1mmeXdr == null){
				continue;
			}
			maxStartTime = Math.max(mainS1mmeXdr.getProduceStartTime(),
					maxStartTime);
			XdrSingleCommon mainCommon = mainS1mmeXdr.getCommon();
			// 合成信令XDR公共信息
			String city = mainCommon.getCity();
			short rat = mainCommon.getRat();
			short xdrType = CompXDRType.CXDR_SIGNALING;
			byte[] xdrId = XdrIDGenerater.getXdrID();
			String imsi = mainCommon.getImsi();
			String imei = mainCommon.getImei();
			String msisdn = mainCommon.getMsisdn();

			int requestCause = mainS1mmeXdr.getRequestCause();
			short failureProcedureType = MAX_UNSIGNED_BYTE;

			long failureStartTime = Long.MIN_VALUE;// 失败接口开始时间（记录最晚失败开始时间）
			long failureEndTime = Long.MAX_VALUE;// 失败接口结束时间（记录最早失败结束时间）
			// 通用信令信息格式
			short procedureType = (short) compIndex.getCspt();
			short procedureStatus = (short) mainS1mmeXdr.getProcedureStatus();
			short failureInterface = MAX_UNSIGNED_BYTE;// 失败接口，失败原因获取规则：1.取所有失败接口中结束时间最靠前（小）的接口填充，如果多个失败xdr结束时间相同，则取开始时间最靠后（大）的xdr填充
			int failureCause = MAX_UNSIGNED_SHORT;
			short keyword1 = mainS1mmeXdr.getKeyword1();
			short keyword2 = mainS1mmeXdr.getKeyword2();
			short keyword3 = mainS1mmeXdr.getKeyword3();
			short keyword4 = mainS1mmeXdr.getKeyword4();
			long enbId = MAX_UNSIGNED_INT;
			long cellId = mainS1mmeXdr.getCellID();
			int mmeGroupId = mainS1mmeXdr.getMmeGroupID();
			short mmeCode = mainS1mmeXdr.getMmeCode();
			int tac = mainS1mmeXdr.getTac();
			String userIpv4 = FormatUtils.getIp(mainS1mmeXdr.getUserIpv4());
			String userIpv6 = FormatUtils.getIp(mainS1mmeXdr.getUserIpv6());
			long newEnbId = MAX_UNSIGNED_INT;
			long newCellId = MAX_UNSIGNED_INT;
			int newMmeGroupId = MAX_UNSIGNED_SHORT;
			short newMmeCode = MAX_UNSIGNED_BYTE;
			int newTac = MAX_UNSIGNED_SHORT;
			short epsBearerNumber = 0;
			List<EpsBearer> bearers = new ArrayList<>();
			short xdrNumber = 0;
			List<XdrSingle> singles = new ArrayList<>();
			long singleXdrStartTime = Long.MAX_VALUE;// 单接口xdr开始时间
			int singleXdrStartIndex = 0;// 单接口xdr开始index
			long singleXdrEndTime = Long.MIN_VALUE;// 单接口xdr结束时间
			int singleXdrEndIndex = 0;// 单接口xdr结束index
			// 生成单接口xdrs
			XdrSingle single = null;
			// 合成s1mme话单
			List<XdrSingleSource> s1mmeXdrs = compIndex.getS1mmeXdrs();
			if (s1mmeXdrs != null && s1mmeXdrs.size() > 0) {
				// 根据s1mme主流程设置合成信息·
				for (XdrSingleSource xdr : s1mmeXdrs) {
					XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
					xdr.setComped(true);
					xdrNumber++;
					// 设置承载
					if (s1mme.getBearers().size() > 0 && bearers.size() == 0) {
						for (com.eversec.lte.model.single.XdrSingleSourceS1MME.Bearer bearer : s1mme
								.getBearers()) {
							epsBearerNumber++;
							EpsBearer epsBearer = new EpsBearer(
									bearer.getBearerID(),
									bearer.getBearerType(),
									bearer.getBearerQCI(),
									bearer.getBearerStatus(),
									bearer.getRequestCause(),
									bearer.getFailureCause(),
									bearer.getBearerEnbGtpTeid(),
									bearer.getBearerSGWGtpTeid());
							bearers.add(epsBearer);
						}
					}
					double startlongitude = 0;
					double startlatitude = 0;
					double endlongitude = 0;
					double endlatitude = 0;
					long produceStartTime = s1mme.getProduceStartTime();
					if (singleXdrStartTime >= produceStartTime) {
						singleXdrStartTime = produceStartTime;
						singleXdrStartIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> startEntry = mrInfos
							.ceilingEntry(produceStartTime);
					if (startEntry != null) {
						startlongitude = startEntry.getValue().getLongitude();
						startlatitude = startEntry.getValue().getLatitude();
					}
					long produceEndTime = s1mme.getProduceEndTime();
					if (singleXdrEndTime <= produceEndTime) {
						singleXdrEndTime = produceEndTime;
						singleXdrEndIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> endEntry = mrInfos
							.ceilingEntry(produceEndTime);
					if (endEntry != null) {
						endlongitude = endEntry.getValue().getLongitude();
						endlatitude = endEntry.getValue().getLatitude();
					}
					short cause = MAX_UNSIGNED_BYTE;
					int s1mmeProcedureType = s1mme.getProcedureType();// 当前s1mme流程类型
					if (procedureStatus != 0) {
						int s1mmeProcedureStatus = s1mme.getProcedureStatus();// 流程状态
						if (s1mmeProcedureStatus == 1
								|| s1mmeProcedureStatus == 255) {
							long startTime = s1mme.getStartTime().getTime();
							long endTime = s1mme.getEndTime().getTime();
							if (endTime < failureEndTime) {
								failureStartTime = startTime;
								failureEndTime = endTime;
								failureInterface = (short) S1MME;
								failureCause = s1mme.getFailureCause();
							} else if (endTime == failureEndTime) {
								if (startTime > failureStartTime) {
									failureStartTime = startTime;
									failureEndTime = endTime;
									failureInterface = (short) S1MME;
									failureCause = s1mme.getFailureCause();
								}
							}
						}
					}
					// 特殊流程取值
					if (procedureType == S1_HANDOVER) {// S1切换流程特殊取值
						if (s1mmeProcedureType == 15) {
							newCellId = s1mme.getOtherEci();
						} else if (s1mmeProcedureType == 16) {
							newMmeGroupId = s1mme.getMmeGroupID();
							newMmeCode = s1mme.getMmeCode();
						}
					} else if (procedureType == TAU) {// TAU流程newtac取值
						if (s1mmeProcedureType == 5) {
							newTac = s1mme.getOtherTac();
						}
					} else if (procedureType == CSFB) {// CSFB流程keyword取值
						keyword1 = (short) compIndex.getKeyword();
					}
					// cause = checkCause((short) s1mme.getCause());
					single = new XdrSingle((short) S1MME, s1mme.getCommon()
							.getXdrId(), s1mme.getProcedureType(),
							s1mme.getStartTime(), s1mme.getEndTime(),
							startlongitude, startlatitude, endlongitude,
							endlatitude, s1mme.getProcedureStatus(),
							s1mme.getRequestCause(), s1mme.getFailureCause());
					singles.add(single);
				}
			}

			// 合成S6a话单
			List<XdrSingleSource> s6aXdrs = compIndex.getS6aXdrs();
			if (s6aXdrs != null && s6aXdrs.size() > 0) {
				for (XdrSingleSource xdr : s6aXdrs) {
					xdr.setComped(true);
					xdrNumber++;
					XdrSingleSourceS6a s6a = (XdrSingleSourceS6a) xdr;
					double startlongitude = 0;
					double startlatitude = 0;
					double endlongitude = 0;
					double endlatitude = 0;
					long produceStartTime = s6a.getProduceStartTime();
					if (singleXdrStartTime >= produceStartTime) {
						singleXdrStartTime = produceStartTime;
						singleXdrStartIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> startEntry = mrInfos
							.ceilingEntry(produceStartTime);
					if (startEntry != null) {
						startlongitude = startEntry.getValue().getLongitude();
						startlatitude = startEntry.getValue().getLatitude();
					}
					long produceEndTime = s6a.getProduceEndTime();
					if (singleXdrEndTime <= produceEndTime) {
						singleXdrEndTime = produceEndTime;
						singleXdrEndIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> endEntry = mrInfos
							.ceilingEntry(produceEndTime);
					if (endEntry != null) {
						endlongitude = endEntry.getValue().getLongitude();
						endlatitude = endEntry.getValue().getLatitude();
					}
					short cause = MAX_UNSIGNED_BYTE;

					if (procedureStatus != 0) {
						int s6aProcedureStatus = s6a.getProcedureStatus();// 流程状态
						if (s6aProcedureStatus == 1
								|| s6aProcedureStatus == 255) {
							long startTime = s6a.getStartTime().getTime();
							long endTime = s6a.getEndTime().getTime();
							if (endTime < failureEndTime) {
								failureStartTime = startTime;
								failureEndTime = endTime;
								failureInterface = (short) S6A;
								failureCause = s6a.getCause();
							} else if (endTime == failureEndTime) {
								if (startTime > failureStartTime) {
									failureStartTime = startTime;
									failureEndTime = endTime;
									failureInterface = (short) S6A;
									failureCause = s6a.getCause();
								}
							}
						}
					}
					single = new XdrSingle((short) S6A, s6a.getCommon()
							.getXdrId(), s6a.getProcedureType(),
							s6a.getStartTime(), s6a.getEndTime(),
							startlongitude, startlatitude, endlongitude,
							endlatitude, s6a.getProcedureStatus(),
							MAX_UNSIGNED_BYTE, s6a.getCause());
					singles.add(single);
				}
			}

			// 合成sgs话单
			List<XdrSingleSource> sgsXdrs = compIndex.getSgsXdrs();
			if (sgsXdrs != null && sgsXdrs.size() > 0) {
				for (XdrSingleSource xdr : sgsXdrs) {
					xdr.setComped(true);
					xdrNumber++;
					XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
					double startlongitude = 0;
					double startlatitude = 0;
					double endlongitude = 0;
					double endlatitude = 0;
					long produceStartTime = sgs.getProduceStartTime();
					if (singleXdrStartTime >= produceStartTime) {
						singleXdrStartTime = produceStartTime;
						singleXdrStartIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> startEntry = mrInfos
							.ceilingEntry(produceStartTime);
					if (startEntry != null) {
						startlongitude = startEntry.getValue().getLongitude();
						startlatitude = startEntry.getValue().getLatitude();
					}
					long produceEndTime = sgs.getProduceEndTime();
					if (singleXdrEndTime <= produceEndTime) {
						singleXdrEndTime = produceEndTime;
						singleXdrEndIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> endEntry = mrInfos
							.ceilingEntry(produceEndTime);
					if (endEntry != null) {
						endlongitude = endEntry.getValue().getLongitude();
						endlatitude = endEntry.getValue().getLatitude();
					}
					short cause = MAX_UNSIGNED_BYTE;

					// 设置faliureInterface,failureCause
					if (procedureStatus != 0) {
						int sgsProcedureStatus = sgs.getProcedureStatus();// 流程状态
						if (sgsProcedureStatus == 1
								|| sgsProcedureStatus == 255) {
							long startTime = sgs.getStartTime().getTime();
							long endTime = sgs.getEndTime().getTime();
							if (endTime < failureEndTime) {
								failureStartTime = startTime;
								failureEndTime = endTime;
								failureInterface = (short) SGS;
								if (sgs.getSgsCause() != MAX_UNSIGNED_BYTE) {
									failureCause = sgs.getSgsCause();
								} else if (sgs.getRejectCause() != MAX_UNSIGNED_BYTE) {
									failureCause = sgs.getRejectCause();
								}
							} else if (endTime == failureEndTime) {
								if (startTime > failureStartTime) {
									failureStartTime = startTime;
									failureEndTime = endTime;
									failureInterface = (short) SGS;
									if (sgs.getSgsCause() != MAX_UNSIGNED_BYTE) {
										failureCause = sgs.getSgsCause();
									} else if (sgs.getRejectCause() != MAX_UNSIGNED_BYTE) {
										failureCause = sgs.getRejectCause();
									}
								}
							}
						}
					}
					single = new XdrSingle((short) SGS, sgs.getCommon()
							.getXdrId(), sgs.getProcedureType(),
							sgs.getStartTime(), sgs.getEndTime(),
							startlongitude, startlatitude, endlongitude,
							endlatitude, sgs.getProcedureStatus(),
							MAX_UNSIGNED_BYTE, sgs.getRejectCause());
					singles.add(single);
				}
			}
			List<XdrSingleSource> s11Xdrs = compIndex.getS11Xdrs();
			if (s11Xdrs != null && s11Xdrs.size() > 0) {
				for (XdrSingleSource xdr : s11Xdrs) {
					XdrSingleSourceS10S11 s11 = (XdrSingleSourceS10S11) xdr;
					double startlongitude = 0;
					double startlatitude = 0;
					double endlongitude = 0;
					double endlatitude = 0;
					long produceStartTime = s11.getProduceStartTime();
					if (singleXdrStartTime >= produceStartTime) {
						singleXdrStartTime = produceStartTime;
						singleXdrStartIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> startEntry = mrInfos
							.ceilingEntry(produceStartTime);
					if (startEntry != null) {
						startlongitude = startEntry.getValue().getLongitude();
						startlatitude = startEntry.getValue().getLatitude();
					}
					long produceEndTime = s11.getProduceEndTime();
					if (singleXdrEndTime <= produceEndTime) {
						singleXdrEndTime = produceEndTime;
						singleXdrEndIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> endEntry = mrInfos
							.ceilingEntry(produceEndTime);
					if (endEntry != null) {
						endlongitude = endEntry.getValue().getLongitude();
						endlatitude = endEntry.getValue().getLatitude();
					}
					single = new XdrSingle((short) S11, s11.getCommon()
							.getXdrId(), s11.getProcedureType(),
							s11.getStartTime(), s11.getEndTime(),
							startlongitude, startlatitude, endlongitude,
							endlatitude, s11.getProcedureStatus(), 
							s11.getReqCause(),s11.getFailureCause());
					singles.add(single);
				}
			}

			List<XdrSingleSource> uuXdrs = compIndex.getUuXdrs();
			if (uuXdrs != null && uuXdrs.size() > 0) {
				enbId = ((XdrSingleSourceUu) uuXdrs.get(0)).getEnbID();
				for (XdrSingleSource xdr : uuXdrs) {
					XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
					double startlongitude = 0;
					double startlatitude = 0;
					double endlongitude = 0;
					double endlatitude = 0;
					long produceStartTime = uu.getProduceStartTime();
					if (singleXdrStartTime >= produceStartTime) {
						singleXdrStartTime = produceStartTime;
						singleXdrStartIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> startEntry = mrInfos
							.ceilingEntry(produceStartTime);
					if (startEntry != null) {
						startlongitude = startEntry.getValue().getLongitude();
						startlatitude = startEntry.getValue().getLatitude();
					}
					long produceEndTime = uu.getProduceEndTime();
					if (singleXdrEndTime <= produceEndTime) {
						singleXdrEndTime = produceEndTime;
						singleXdrEndIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> endEntry = mrInfos
							.ceilingEntry(produceEndTime);
					if (endEntry != null) {
						endlongitude = endEntry.getValue().getLongitude();
						endlatitude = endEntry.getValue().getLatitude();
					}
					single = new XdrSingle((short) UU, uu.getCommon()
							.getXdrId(), uu.getProcedureType(),
							uu.getStartTime(), uu.getEndTime(),
							startlongitude, startlatitude, endlongitude,
							endlatitude, uu.getProcedureStatus(),
							MAX_UNSIGNED_BYTE,MAX_UNSIGNED_BYTE);
					singles.add(single);
				}
			}

			List<XdrSingleSource> x2Xdrs = compIndex.getX2Xdrs();
			if (x2Xdrs != null && x2Xdrs.size() > 0) {
				for (XdrSingleSource xdr : x2Xdrs) {
					XdrSingleSourceX2 x2 = (XdrSingleSourceX2) xdr;
					double startlongitude = 0;
					double startlatitude = 0;
					double endlongitude = 0;
					double endlatitude = 0;
					long produceStartTime = x2.getProduceStartTime();
					if (singleXdrStartTime >= produceStartTime) {
						singleXdrStartTime = produceStartTime;
						singleXdrStartIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> startEntry = mrInfos
							.ceilingEntry(produceStartTime);
					if (startEntry != null) {
						startlongitude = startEntry.getValue().getLongitude();
						startlatitude = startEntry.getValue().getLatitude();
					}
					long produceEndTime = x2.getProduceEndTime();
					if (singleXdrEndTime <= produceEndTime) {
						singleXdrEndTime = produceEndTime;
						singleXdrEndIndex = xdrNumber - 1;
					}
					Entry<Long, MrInfo> endEntry = mrInfos
							.ceilingEntry(produceEndTime);
					if (endEntry != null) {
						endlongitude = endEntry.getValue().getLongitude();
						endlatitude = endEntry.getValue().getLatitude();
					}
					single = new XdrSingle((short) X2, x2.getCommon()
							.getXdrId(), x2.getProcedureType(),
							x2.getStartTime(), x2.getEndTime(),
							startlongitude, startlatitude, endlongitude,
							endlatitude, x2.getProcedureStatus(),
							x2.getRequestCause(),x2.getFailureCause());
					singles.add(single);
				}
			}

			if (xdrNumber > 0 && xdrNumber <= 255) {
				// 设置合成信息XDR时间、经纬度信息
				Date startTime = null;
				Date endTime = null;
				double startLongitude = 0;
				double startLatitude = 0;
				double endLongitude = 0;
				double endLatitude = 0;
				XdrSingle startXdr = singles.get(singleXdrStartIndex);
				XdrSingle endXdr = singles.get(singleXdrEndIndex);
				startTime = startXdr.getStartTime();
				endTime = endXdr.getEndTime();
				startLongitude = startXdr.getStartlongitude();
				startLatitude = startXdr.getStartlatitude();
				endLongitude = endXdr.getEndlongitude();
				endLatitude = endXdr.getEndlatitude();
				// XdrCompoundSourceSignaling compSignalingXdr = new
				// XdrCompoundSourceSignaling(
				// procedureType, startTime, endTime, startLongitude,
				// startLatitude, endLongitude, endLatitude,
				// procedureStatus, failureInterface, failureCause,
				// keyword, enbId, cellId, mmeGroupId, mmeCode, tac,
				// userIpv4, userIpv6, newEnbId, newCellId, newMmeGroupId,
				// newMmeCode, newTac, epsBearerNumber, bearers,
				// xdrNumber, singles);

				XdrCompoundSourceSignaling compSignalingXdr = new XdrCompoundSourceSignaling(
						procedureType, startTime, endTime, startLongitude,
						startLatitude, endLongitude, endLatitude,
						procedureStatus, requestCause, failureInterface,
						failureProcedureType, failureCause, keyword1, keyword2,
						keyword3, keyword4, enbId, cellId, mmeGroupId, mmeCode,
						tac, userIpv4, userIpv6, newEnbId, newCellId,
						newMmeGroupId, newMmeCode, newTac, epsBearerNumber,
						bearers, xdrNumber, singles);
				Collections.sort(singles,
						XdrCompoundSourceSignaling.XDR_SINGLE_COMPARATOR);// 重新按照开始时间排序
				XdrCompoundCommon common = new XdrCompoundCommon(0, city, rat,
						xdrType, xdrId, imsi, imei, msisdn);
				common.setLength(common.getBodyLength()
						+ compSignalingXdr.getBodyLength());// 重设xdr length
				compSignalingXdr.setCommon(common);
				compInfo.getCxdrs().add(compSignalingXdr);
			}
		}
		// 将未成功合成的xdr保存到kafka(topic : signal-cxdr-with-uemr-tryagain)
		for (XdrSingleSource xdr : xdrs) {
			if (!xdr.isComped()) {
				if (xdr.getProduceStartTime() >= maxStartTime - DIFF_MILLS) {
					compInfo.getXdrs().add(xdr);
				}
			}
		}
		return compInfo;
	}

	// /**
	// * 单接口xdr中cause为2个字节，合成xdr单接口为一个字节，需要判断是否溢出
	// *
	// * @param cause
	// * @return
	// */
	// private short checkCause(short cause) {
	// if (cause < 0 || cause > MAX_UNSIGNED_BYTE) {
	// cause = MAX_UNSIGNED_BYTE;
	// }
	// return cause;
	// }

	private List<CompIndexInfo> search(List<XdrSingleSource> xdrs,
			List<MainProcedureInfo> mpInfos,
			Map<String, List<XdrSingleSource>> lowIDS1mmeMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeS1mmeMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeS6aMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeSgsMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeS11Map,
			TreeMap<Long, List<XdrSingleSource>> startTimeUuMap,
			TreeMap<Long, List<XdrSingleSource>> startTimeX2Map) {
		List<CompIndexInfo> ret = new ArrayList<>();
		for (MainProcedureInfo info : mpInfos) {
			CompIndexInfo indexInfo = new CompIndexInfo();
			boolean isComp = true;// 判断是否需要合成输出
			String lowID = info.getLowID();
			long startTime = info.getStartTime();
			long endTime = info.getEndTime();
			int keyword = info.getKeyword();
			int cspt = info.getCspt();
			String rule1 = info.getRule1();
			XdrSingleSource mainXdr = info.getMainXdr();
			indexInfo.setKeyword(keyword);// 设置keyword
			indexInfo.setCspt(cspt);// 合成流程类型
			switch (cspt) {
			case ATTACH:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
				// s6a
				indexInfo.setS6aXdrs(getBetween(startTimeS6aMap, startTime,
						endTime));
				// sgs
				indexInfo.setSgsXdrs(getBetween(startTimeSgsMap, startTime,
						endTime));

				// uu
				indexInfo.setUuXdrs(getUuXdrsStartTimeAfterType1(
						startTimeUuMap, startTime, endTime, rule1));

				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case SERVICE_REQUEST:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
				// s6a
				indexInfo.setS6aXdrs(getBetween(startTimeS6aMap, startTime,
						endTime));
				// uu
				indexInfo.setUuXdrs(getUuXdrsStartTimeAfterType1(
						startTimeUuMap, startTime, endTime, rule1));

				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case PAGING:
				List<XdrSingleSource> pagingJudgeList = getAfterInDiff(
						startTimeUuMap, endTime, ATTACH_UU_AFTER_MILLS);
				if (pagingJudgeList != null && pagingJudgeList.size() > 0) {
					for (XdrSingleSource xdr : pagingJudgeList) {
						XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
						if (s1mme.getProcedureType() == 3
								&& s1mme.getProduceStartTime() == endTime) {
							isComp = false;
							break;
						}
					}
				}
				if (isComp) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
				}
				break;
			case TAU:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
				// s6a
				indexInfo.setS6aXdrs(getBetween(startTimeS6aMap, startTime,
						endTime));
				// sgs
				indexInfo.setSgsXdrs(getBetween(startTimeSgsMap, startTime,
						endTime));

				// uu
				indexInfo.setUuXdrs(getUuXdrsStartTimeAfterType1(
						startTimeUuMap, startTime, endTime, rule1));

				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case DETACH:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				// 查找主流程开始时间1秒内，proceduretype为19且rule1相等的s1mme话单
				List<XdrSingleSource> detachJudgeList = getAfterInDiff(
						startTimeS1mmeMap, startTime, DETACH_S1MME_AFTER_MILLS);
				if (detachJudgeList != null && detachJudgeList.size() > 0) {
					for (XdrSingleSource xdr : detachJudgeList) {
						XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
						String detachRule1 = getRule1(s1mme);
						if (s1mme.getProcedureType() == 20
								&& detachRule1.equals(rule1)) {
							indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr,s1mme));
							endTime = s1mme.getProduceEndTime();// 修改流程结束时间,最靠后的endtime
							break;
						}
					}
				}
				indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
				if (indexInfo.getS1mmeXdrs() == null) {
					indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
				}
				// sgs
				indexInfo.setSgsXdrs(getBetween(startTimeSgsMap, startTime,
						endTime));

				// uu
				indexInfo.setUuXdrs(getUuXdrsStartTimeAfterType1(
						startTimeUuMap, startTime, endTime, rule1));

				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case PDN_CONNECTIVITY:
				List<XdrSingleSource> pdnConnJudgeList = getBeforeInDiff(
						startTimeS1mmeMap, startTime,
						PDN_CONNECTIVITY_S1MME_BEFORE_MILLS);
				if (pdnConnJudgeList != null && pdnConnJudgeList.size() > 0) {
					for (XdrSingleSource xdr : pdnConnJudgeList) {
						XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
						if (s1mme.getProcedureType() == 1
								&& s1mme.getLowID().equals(lowID)) {
							isComp = false;
							break;
						}
					}
				}
				if (isComp) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));

					endTime = getLastS1mmeProduceEndTime(indexInfo, endTime);
					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
					// s11
					indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
							endTime));
				}
				break;
			case PDN_DISCONNECTION:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
				// uu
				indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
						startTime, endTime, rule1));
				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case EPS_BEARER_RESOURCE_ALLOCATION:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));

				endTime = getLastS1mmeProduceEndTime(indexInfo, endTime);
				// uu
				indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
						startTime, endTime, rule1));
				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case EPS_BEARER_RESOURCE_MODIFY:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));

				endTime = getLastS1mmeProduceEndTime(indexInfo, endTime);
				// uu
				indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
						startTime, endTime, rule1));
				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case EPS_BEARER_CONTEXT_DEACTIVATION:
				isComp = isCompEpsBearerContext(startTimeS1mmeMap, startTime,
						lowID, 8, 10);

				if (isComp) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
				}
				break;
			case EPS_BEARER_CONTEXT_MODIFICATION:
				isComp = isCompEpsBearerContext(startTimeS1mmeMap, startTime,
						lowID, 9, 10);

				if (isComp) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
				}
				break;
			case DEDICATED_EPS_BEARER_CONTEXT_ACTIVATION:
				isComp = isCompEpsBearerContext(startTimeS1mmeMap, startTime,
						lowID, 9, 10);

				if (isComp) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
				}
				break;
			case X2_HANDOVER:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));

				// x2
				List<XdrSingleSource> x2HandoverJudgeList = getBeforeInDiff(
						startTimeX2Map, startTime,
						PDN_CONNECTIVITY_S1MME_BEFORE_MILLS);
				for (XdrSingleSource xdr : x2HandoverJudgeList) {
					XdrSingleSourceX2 x2 = (XdrSingleSourceX2) xdr;
					// 且Source Cell ID、Target Cell ID分别与主流程的Other ECI、Cell ID相同
					XdrSingleSourceS1MME s1mmeMain = (XdrSingleSourceS1MME) mainXdr;
					String x2rule1 = getRule1(x2);
					if (x2.getProcedureType() == 1 && rule1.equals(x2rule1)
							&& x2.getSourceCellId() == s1mmeMain.getOtherEci()
							&& x2.getTargetCellId() == s1mmeMain.getCellID()) {
						indexInfo.setX2Xdrs(Arrays.asList(xdr));
						startTime = x2.getProduceStartTime();
						break;
					}
				}

				// uu
				indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
						startTime, endTime, rule1));
				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case S1_HANDOVER:
				// 设置主流程index
				indexInfo.setMainXdr(mainXdr);
				// s1mme
				List<XdrSingleSource> s1HandoverJudgeList = getAfterInDiff(
						startTimeS1mmeMap, startTime,
						S1_HANDOVER_S1MME_AFTER_MILLS);
				if (s1HandoverJudgeList != null
						&& s1HandoverJudgeList.size() > 0) {
					for (XdrSingleSource xdr : s1HandoverJudgeList) {
						XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
						if (s1mme.getProcedureType() == 15
								&& s1mme.getProduceStartTime() >= startTime
								&& s1mme.getProduceStartTime() <= endTime) {
							indexInfo.setS1mmeXdrs(Arrays.asList(xdr, mainXdr));
							break;
						}
					}
				}
				if (indexInfo.getS1mmeXdrs() == null) {
					indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
				}
				endTime = getLastS1mmeProduceEndTime(indexInfo, endTime);

				// uu
				indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
						startTime, endTime, rule1));
				// s11
				indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
						endTime));
				break;
			case UE_CONTEXT_RELEASE:
				List<XdrSingleSource> ueReleaseJudgeList = getBeforeInDiff(
						startTimeS1mmeMap, startTime,
						UE_CONTEXT_RELEASE_S1MME_BEFORE_MILLS);
				if (ueReleaseJudgeList != null && ueReleaseJudgeList.size() > 0) {
					for (XdrSingleSource xdr : ueReleaseJudgeList) {
						XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
						String ueReleaseRule1 = getRule1(s1mme);
						if (s1mme.getProcedureType() == 6
								&& ueReleaseRule1.equals(rule1)) {
							isComp = false;
							break;
						}
					}
				}
				if (isComp) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
					// s11
					indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
							endTime));
				}
				break;
			case EPS_BEARER_RELEASE:
				List<XdrSingleSource> epsReleaseJudgeList = getBeforeInDiff(
						startTimeS1mmeMap, startTime,
						EPS_BEARER_RELEASE_S1MME_BEFORE_MILLS);
				if (epsReleaseJudgeList != null
						&& epsReleaseJudgeList.size() > 0) {
					for (XdrSingleSource xdr : epsReleaseJudgeList) {
						XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
						if (s1mme.getProcedureType() == 11
								&& s1mme.getLowID().equals(lowID)) {
							isComp = false;
							break;
						}
					}
				}
				if (isComp) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
				}
				break;
			case CSFB:
				if (keyword == 0 || keyword == 2 || keyword == 3
						|| keyword == 4) {// CSFB主叫
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(lowIDS1mmeMap.get(lowID));
					// s6a
					indexInfo.setS6aXdrs(getBetween(startTimeS6aMap, startTime,
							endTime));

					// uu
					indexInfo.setUuXdrs(getUuXdrsStartTimeAfterType1(
							startTimeUuMap, startTime, endTime, rule1));
					// s11
					indexInfo.setS11Xdrs(getBetween(startTimeS11Map, startTime,
							endTime));

					// 合成话单特殊取值
					indexInfo.setKeyword(0);
				} else if (keyword == 1) {// CSFB被叫
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					List<XdrSingleSource> s1mmeXdrs = new ArrayList<>();
					s1mmeXdrs.addAll(lowIDS1mmeMap.get(lowID));
					List<XdrSingleSource> csfbS1mmeJudgeList = getBeforeInDiff(
							startTimeS1mmeMap, startTime,
							CSFB_S1MME_BEFORE_MILLS);
					if (csfbS1mmeJudgeList != null
							&& csfbS1mmeJudgeList.size() > 0) {
						for (XdrSingleSource xdr : csfbS1mmeJudgeList) {
							XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
							String csfbRule1 = getRule1(s1mme);
							if (s1mme.getProcedureType() == 4
									&& s1mme.getProduceEndTime() == startTime
									&& rule1.equals(csfbRule1)) {
								s1mmeXdrs.add(xdr);
								break;
							}
						}
					}
					indexInfo.setS1mmeXdrs(s1mmeXdrs);
					// s6a
					indexInfo.setS6aXdrs(getBetween(startTimeS6aMap, startTime,
							endTime));
					// sgs
					List<XdrSingleSource> sgsXdrs = new ArrayList<>();
					List<XdrSingleSource> csfbSgsBeforeJudgeList = getBeforeInDiff(
							startTimeSgsMap, startTime, CSFB_SGS_BEFORE_MILLS);
					XdrSingleSourceSGs pt1Si1 = null;
					if (csfbSgsBeforeJudgeList != null
							&& csfbSgsBeforeJudgeList.size() > 0) {
						// 主流程的开始时间之前10秒内在SGS接口查找最近的Procedure
						// Type取值为1，Service Indicator取值为1的话单
						for (int i = csfbSgsBeforeJudgeList.size() - 1; i >= 0; i--) {
							XdrSingleSource xdr = csfbSgsBeforeJudgeList.get(i);
							XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
							if (sgs.getProcedureType() == 1
									&& sgs.getServiceIndicator() == 1) {
								pt1Si1 = sgs;
								sgsXdrs.add(xdr);
								break;
							}
						}
					}
					if (pt1Si1 != null) {
						List<XdrSingleSource> csfbSgsAfterJudgeList = getAfterInDiff(
								startTimeSgsMap, pt1Si1.getProduceEndTime(),
								CSFB_SGS_AFTER_MILLS);
						// 查找Procedure Type取值为2的话单（IMSI相同，Procedure Start
						// Time与Procedure Type取值为1，Service
						// Indicator取值为1的话单的Procedure End Time相等
						for (XdrSingleSource xdr : csfbSgsAfterJudgeList) {
							XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
							if (sgs.getProcedureType() == 2
									&& sgs.getProduceStartTime() == pt1Si1
											.getProduceEndTime()) {
								sgsXdrs.add(xdr);
								break;
							}
						}
					}
					indexInfo.setSgsXdrs(sgsXdrs);

					if (pt1Si1 != null) {
						// uu
						startTime = pt1Si1.getProduceStartTime();
						indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
								startTime, endTime, rule1));
					}
				}
				break;
			case SMS:
				if (keyword == 1) {
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
					// sgs
					indexInfo.setSgsXdrs(getBetween(startTimeSgsMap, startTime,
							endTime));
				} else if (keyword == 0) { // 下行
					// 设置主流程index
					indexInfo.setMainXdr(mainXdr);
					// s1mme
					indexInfo.setS1mmeXdrs(Arrays.asList(mainXdr));
					// sgs
					List<XdrSingleSource> sgsXdrs = new ArrayList<>();
					List<XdrSingleSource> smsBeforeJudgeList = getBeforeInDiff(
							startTimeSgsMap, startTime, SMS_SGS_BEFORE_MILLS);
					if (smsBeforeJudgeList != null
							&& smsBeforeJudgeList.size() > 0) {
						for (int i = smsBeforeJudgeList.size() - 1; i >= 0; i--) {
							XdrSingleSource xdr = smsBeforeJudgeList.get(i);
							XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
							if (sgs.getProcedureType() == 3
									&& sgs.getServiceIndicator() == 2) {
								sgsXdrs.add(xdr);
								break;
							}
						}
					}
					List<XdrSingleSource> smsAfterJudgeList = getAfterInDiff(
							startTimeSgsMap, startTime, SMS_SGS_AFTER_MILLS);
					if (smsAfterJudgeList != null
							&& smsAfterJudgeList.size() > 0) {
						for (XdrSingleSource xdr : smsAfterJudgeList) {
							XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
							if (sgs.getProcedureType() == 4
									&& sgs.getServiceIndicator() == 2) {
								sgsXdrs.add(xdr);
								break;
							}
						}
					}
					indexInfo.setSgsXdrs(sgsXdrs);

					// uu
					indexInfo.setUuXdrs(getBetweenWithRule1(startTimeUuMap,
							startTime, endTime, rule1));
				}
				break;
			}
			if (isComp) {
				ret.add(indexInfo);
			}
		}
		return ret;
	}

	private boolean isCompEpsBearerContext(
			TreeMap<Long, List<XdrSingleSource>> startTimeS1mmeMap,
			long startTime, String lowID, int type1, int type2) {
		List<XdrSingleSource> epsDeactivationJudgeList = getBeforeInDiff(
				startTimeS1mmeMap, startTime,
				EPS_BEARER_CONTEXT_DEACTIVATION_S1MME_BEFORE_MILLS);
		if (epsDeactivationJudgeList != null
				&& epsDeactivationJudgeList.size() > 0) {
			for (XdrSingleSource xdr : epsDeactivationJudgeList) {
				XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
				if ((s1mme.getProcedureType() == type1 || s1mme
						.getProcedureType() == type2)
						&& s1mme.getLowID().equals(lowID)) {
					return false;
				}
			}
		}
		return true;
	}

	private long getLastS1mmeProduceEndTime(CompIndexInfo indexInfo,
			long endTime) {
		XdrSingleSource lastS1mme = indexInfo.getS1mmeXdrs().get(
				indexInfo.getS1mmeXdrs().size() - 1);
		if (lastS1mme != null) {
			return lastS1mme.getProduceEndTime();
		}
		return endTime;
	}

	private List<XdrSingleSource> getBetweenWithRule1(
			TreeMap<Long, List<XdrSingleSource>> map, long startTime,
			long endTime, String rule1) {
		List<XdrSingleSource> result = new ArrayList<>();
		List<XdrSingleSource> list = getBetween(map, startTime, endTime);
		for (XdrSingleSource xdr : list) {
			String xdrRule1 = getRule1(xdr);
			if (rule1.equals(xdrRule1)) {
				result.add(xdr);
			}
		}
		return result;
	}

	// UU 合成规则
	private List<XdrSingleSource> getUuXdrsStartTimeAfterType1(
			TreeMap<Long, List<XdrSingleSource>> startTimeUuMap,
			long startTime, long endTime, String rule1) {
		List<XdrSingleSource> uuXdrs = new ArrayList<>();
		List<XdrSingleSource> uufirstList = getBetween(startTimeUuMap,
				startTime - ATTACH_UU_AFTER_MILLS, startTime
						+ ATTACH_UU_AFTER_MILLS);
		for (XdrSingleSource xdr : uufirstList) {
			XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
			String attachRule1 = getRule1(uu);
			if (uu.getProcedureType() == 1 && attachRule1.equals(rule1)) {
				startTime = uu.getProduceStartTime(); // 开始时间
				break;
			}
		}
		List<XdrSingleSource> attachJudgeList = getBetween(startTimeUuMap,
				startTime, endTime);
		for (XdrSingleSource xdr : attachJudgeList) {
			XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
			String attachRule1 = getRule1(uu);
			if (attachRule1.equals(rule1)) {
				uuXdrs.add(uu);
			}
		}
		return uuXdrs;
	}

	private String getRule1(XdrSingleSource xdr) {
		if (xdr instanceof XdrSingleSourceS1MME) {
			XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
			return StringUtils.join(
					new String[] { String.valueOf(s1mme.getMmeUeS1apID()),
							String.valueOf(s1mme.getMmeGroupID()),
							String.valueOf(s1mme.getMmeCode()) },
					CommonConstants.UNDERLINE);
		} else if (xdr instanceof XdrSingleSourceUu) {
			XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
			return StringUtils.join(
					new String[] { String.valueOf(uu.getMmeUeS1apId()),
							String.valueOf(uu.getMmeGroupID()),
							String.valueOf(uu.getMmeCode()) },
					CommonConstants.UNDERLINE);
		} else if (xdr instanceof XdrSingleSourceX2) {
			XdrSingleSourceX2 x2 = (XdrSingleSourceX2) xdr;
			return StringUtils.join(
					new String[] { String.valueOf(x2.getMmeUeS1apId()),
							String.valueOf(x2.getMmeGroupID()),
							String.valueOf(x2.getMmeCode()) },
					CommonConstants.UNDERLINE);
		} else {
			throw new RuntimeException(
					"createRule1 only support s1mme x2 uu ,but this is :"
							+ xdr.getClass());
		}

	}

	/**
	 * 
	 * @param map
	 *            (key:时间戳，value:xdrs index)
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public List<XdrSingleSource> getBetween(
			TreeMap<Long, List<XdrSingleSource>> map, long startTime,
			long endTime) {
		List<XdrSingleSource> rets = new ArrayList<>();
		for (Entry<Long, List<XdrSingleSource>> entry : map.entrySet()) {
			long time = entry.getKey();
			if (time >= startTime && time <= endTime) {
				rets.addAll(entry.getValue());
			} else if (time > endTime) {
				break;
			}
		}
		return rets;
	}

	/**
	 * 
	 * @param map
	 *            (key:时间戳，value:xdrs index)
	 * @param time
	 *            查询时间
	 * @param diff
	 *            （时间差，单位毫秒）
	 * @return
	 */
	public List<XdrSingleSource> getAfterInDiff(
			TreeMap<Long, List<XdrSingleSource>> map, long time, long diff) {
		List<XdrSingleSource> ret = new ArrayList<>();
		for (Entry<Long, List<XdrSingleSource>> entry : map.entrySet()) {
			long key = entry.getKey();
			if (key >= time && key <= time + diff) {
				ret.addAll(entry.getValue());
			} else if (key > time + diff) {
				break;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param map
	 *            (key:时间戳，value:xdrs index)
	 * @param time
	 *            查询时间
	 * @param diff
	 *            （时间差，单位毫秒）
	 * @return
	 */
	public List<XdrSingleSource> getBeforeInDiff(
			TreeMap<Long, List<XdrSingleSource>> map, long time, long diff) {
		List<XdrSingleSource> ret = new ArrayList<>();
		for (Entry<Long, List<XdrSingleSource>> entry : map.entrySet()) {
			long key = entry.getKey();
			if (key >= time - diff && key <= time) {
				ret.addAll(entry.getValue());
			} else if (key > time) {
				break;
			}
		}
		return ret;
	}

	public List<XdrSingleSource> getBeforeAndAfterInDiff(
			TreeMap<Long, List<XdrSingleSource>> map, long time, long diff) {
		List<XdrSingleSource> ret = new ArrayList<>();
		for (Entry<Long, List<XdrSingleSource>> entry : map.entrySet()) {
			long key = entry.getKey();
			if (key >= time - diff && key <= time + diff) {
				ret.addAll(entry.getValue());
			} else if (key > time + diff) {
				break;
			}
		}
		return ret;
	}

	/**
	 * 判断s1mme是否为主流程，如果是返回相应的主流程,否则返回255（UNKNOW）
	 * 
	 * @param s1mme
	 * @return
	 */
	private int getCompSignalingProcedureType(XdrSingleSourceS1MME s1mme) {
		int ret = CompSignalingProcedureType.UNKNOWN;
		int procedureType = s1mme.getProcedureType();
		switch (procedureType) {
		case 1:
			ret = ATTACH;
			break;
		case 2:
			ret = SERVICE_REQUEST;
			break;
		case 3:
			ret = CSFB;
			break;
		case 4:
			ret = PAGING;
			break;
		case 5:
			ret = TAU;
			break;
		case 6:
			ret = DETACH;
			break;
		case 7:
			ret = PDN_CONNECTIVITY;
			break;
		case 8:
			ret = PDN_DISCONNECTION;
			break;
		case 9:
			ret = EPS_BEARER_RESOURCE_ALLOCATION;
			break;
		case 10:
			ret = EPS_BEARER_RESOURCE_MODIFY;
			break;
		case 11:
			ret = EPS_BEARER_CONTEXT_DEACTIVATION;
			break;
		case 12:
			ret = EPS_BEARER_CONTEXT_MODIFICATION;
			break;
		case 13:
			ret = DEDICATED_EPS_BEARER_CONTEXT_ACTIVATION;
			break;
		case 14:
			ret = X2_HANDOVER;
			break;
		case 15:
			ret = S1_HANDOVER;
			break;
		case 19:
			ret = UE_CONTEXT_RELEASE;
			break;
		case 20:
			ret = EPS_BEARER_RELEASE;
			break;
		case 31:
			ret = SMS;
			break;
		default:
			break;
		}
		return ret;
	}

	public static class TestVo {
		private String name;
		private int age;

		public TestVo(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}

		@Override
		public String toString() {
			return "TestVo [name=" + name + ", age=" + age + "]";
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

	}

	public static void main(String[] args) {
		TreeMap<Long, List<Integer>> map = new TreeMap<>();
		map.put(501l, Arrays.asList(1, 2));
		map.put(10001l, Arrays.asList(1, 2));
		map.put(10501l, Arrays.asList(3, 4));
		map.put(12000l, Arrays.asList(5, 6));
		map.put(12324l, Arrays.asList(7, 8));
		map.put(10025l, Arrays.asList(11, 12));
		map.put(11890l, Arrays.asList(21, 22));
		map.put(12025l, Arrays.asList(31, 32));
		map.put(13025l, Arrays.asList(41, 42));
		map.put(11925l, Arrays.asList(61, 52));
		map.put(11290l, Arrays.asList(21, 22));
		map.put(10400l, Arrays.asList(21, 22));
		// getAfterInDiff(map, 10001l, 1000);
		// getBeforeInDiff(map, 10501l, 1000);
		// getBeforeAndAfterInDiff(map, 10501l, 1000);
		LinkedList<TestVo> list = new LinkedList<>();
		TestVo t = new TestVo("na2", 22);
		list.add(new TestVo("na1", 22));
		list.add(t);
		list.add(new TestVo("na3", 22));
		list.add(new TestVo("na4", 22));
		list.add(new TestVo("na15", 22));
		HashMap<String, TestVo> mm = new HashMap<>();
		mm.put("tt", list.get(1));
		// list.remove(t);
		// list.remove(new TestVo("na2", 22));
		for (TestVo vo : list) {
			System.out.println(vo);
			vo.setAge(10);
		}
		System.out.println(mm);
		short a = 14;
		int b = 14;
		System.out.println(a == b);
	}
}
