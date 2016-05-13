package com.eversec.lte.processor.backfill.back;
//package com.eversec.lte.processor.backfill;
//
//import static com.eversec.lte.processor.data.StaticData.*;
//import static com.eversec.lte.processor.data.CacheData.*;
//import static com.eversec.lte.processor.data.QueueData.PENDING_XDR_DATA_QUEUE;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants;
//import com.eversec.lte.model.single.XdrSingleCommon;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS10S11;
//import com.eversec.lte.model.single.XdrSingleSourceS1MME;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//import com.eversec.lte.model.single.XdrSingleSourceS6a;
//import com.eversec.lte.model.single.XdrSingleSourceSGs;
//import com.eversec.lte.model.single.XdrSingleSourceUEMR;
//import com.eversec.lte.model.single.XdrSingleSourceUu;
//import com.eversec.lte.model.single.XdrSingleSourceX2;
//import com.eversec.lte.model.single.XdrSingleSourceS10S11.Bearer;
//import com.eversec.lte.output.FilledXdrKafkaOutput;
//import com.eversec.lte.output.FilledXdrSdtpOutput;
//import com.eversec.lte.output.FilledXdrFileOutput;
//import com.eversec.lte.output.CustomXdrKafkaOutput;
//import com.eversec.lte.output.UemrXdrOutput;
//import com.eversec.lte.utils.UUIDUtils;
//import com.eversec.lte.vo.backfill.S11FillParam;
//import com.eversec.lte.vo.backfill.S1UFillParam;
//import com.eversec.lte.vo.backfill.S1mmeFillParam;
//import com.eversec.lte.vo.backfill.S6aFillParam;
//import com.eversec.lte.vo.backfill.SgsFillParam;
//import com.eversec.lte.vo.backfill.UemrFillParam;
//import com.eversec.lte.vo.backfill.UuFillParam;
//import com.eversec.lte.vo.backfill.X2FillParam;
//
///**
// * 中国移动LTE单接口回填工具</br><B>注意事项：每个线程创建一个实例</B>
// * 
// * @author bieremayi
// * 
// */
//@SuppressWarnings("serial")
//public abstract class AbstractCmBackFill extends AbstractBackFill {
//
//	private static final short Interface = 0;
//
//	public static AbstractCmBackFill getBackFill(long threadId) {
//		String key = String.valueOf(threadId);
//		AbstractCmBackFill backfill = (AbstractCmBackFill) BACKFILLS
//				.getIfPresent(key);
//		if (backfill == null) {
//			if (SdtpConfig.isBackFillOnlyRedis()) {
//				backfill = new CmBackFillWithRedis();
//			} else {
//				backfill = new CmBackFillWithLocalCache();
//			}
//			BACKFILLS.update(key, backfill);
//			logger.info("create cmbackfill ,thread id : {}", threadId);
//		}
//		return backfill;
//	}
//
//	/**
//	 * 移动版本设置回填信息
//	 * 
//	 * @param data
//	 * @param info
//	 */
//	protected void setFillInfo(XdrSingleSource data, BackfillInfo info) {
//		if (info != null && data != null) {
//			XdrSingleCommon common = data.getCommon();
//			if (common != null) {
//				common.setMsisdn(info.msisdn);
//				common.setImsi(info.imsi);
//				common.setImei(info.imei);
//			}
//		}
//	}
//
//	/**
//	 * UU回填逻辑
//	 * 
//	 * @param param
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo fillUu(UuFillParam param, XdrSingleSourceUu data) {
//		String mmeS1apID = param.getMmeS1apID(), mmeGroupID = param
//				.getMmeGroupID(), mmeCode = param.getMmeCode();
//		BackfillInfo info = getInfo(getRule1(mmeS1apID, mmeGroupID, mmeCode),
//				RuleType.RULE1);
//		if (info != null) {
//			setFillInfo(data, info);
//			SUCC_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		} else {
//			UU_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//		}
//		return info;
//	}
//
//	/**
//	 * UU重新尝试回填
//	 * 
//	 * @param key
//	 * @param data
//	 */
//	public void refillUu(String key, XdrSingleSourceUu data) {
//		BackfillInfo info = null;
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			info = getInfo(
//					getRule1(String.valueOf(data.getMmeUeS1apId()),
//							String.valueOf(data.getMmeGroupID()),
//							String.valueOf(data.getMmeCode())), RuleType.RULE1);
//			if (info != null) {
//				setFillInfo(data, info);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		}
//	}
//
//	/**
//	 * X2回填逻辑
//	 * 
//	 * @param param
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo fillX2(X2FillParam param, XdrSingleSourceX2 data) {
//		String mmeS1apID = param.getMmeS1apID(), mmeGroupID = param
//				.getMmeGroupID(), mmeCode = param.getMmeCode();
//		BackfillInfo info = getInfo(getRule1(mmeS1apID, mmeGroupID, mmeCode),
//				RuleType.RULE1);
//		if (info != null) {
//			setFillInfo(data, info);
//			SUCC_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		} else {
//			X2_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//		}
//		return info;
//	}
//
//	/**
//	 * X2重新尝试回填
//	 * 
//	 * @param key
//	 * @param data
//	 */
//	public void refillX2(String key, XdrSingleSourceX2 data) {
//		BackfillInfo info = null;
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			info = getInfo(
//					getRule1(String.valueOf(data.getMmeUeS1apId()),
//							String.valueOf(data.getMmeGroupID()),
//							String.valueOf(data.getMmeCode())), RuleType.RULE1);
//			if (info != null) {
//				setFillInfo(data, info);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);// 需要重新尝试回填，在guava cache
//					// onRemoval中调用
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		}
//	}
//
//	/**
//	 * UE_MR回填逻辑
//	 * 
//	 * @param param
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo filledUemr(UemrFillParam param, XdrSingleSourceUEMR data) {
//		String mmeS1apID = param.getMmeS1apID();
//		String cellID = param.getCellID();
//		BackfillInfo info = getInfo(getRule4(mmeS1apID, cellID), RuleType.RULE4);
//		// cellID非默认值时，获取不到用户信息需重新尝试
//		if (info == null && !cellID.equals(SdtpConstants.MAX_UNSIGNED_INT_STR)) {
//			info = getInfo(
//					getRule4(mmeS1apID, SdtpConstants.MAX_UNSIGNED_INT_STR),
//					RuleType.RULE4);
//		}
//		if (info != null) {
//			setFillInfo(data, info);
//			SUCC_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			UemrXdrOutput.output(data);
//		} else {
//			UEMR_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//		}
//		return info;
//	}
//
//	/**
//	 * UE_MR重新尝试回填
//	 * 
//	 * @param key
//	 * @param data
//	 */
//	public void refillUEMR(String key, XdrSingleSourceUEMR data) {
//		BackfillInfo info = null;
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			String mmeS1apID = String.valueOf(data.getMmeUeS1apId());
//			String cellID = String.valueOf(data.getCellID());
//			info = getInfo(getRule4(mmeS1apID, cellID), RuleType.RULE4);
//			// cellID非默认值时，获取不到用户信息需重新尝试
//			if (info == null
//					&& !cellID.equals(SdtpConstants.MAX_UNSIGNED_INT_STR)) {
//				info = getInfo(
//						getRule4(mmeS1apID, SdtpConstants.MAX_UNSIGNED_INT_STR),
//						RuleType.RULE4);
//			}
//			if (info != null) {
//				setFillInfo(data, info);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				UemrXdrOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					UemrXdrOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			UemrXdrOutput.output(data);
//		}
//	}
//
//	/**
//	 * s11回填逻辑
//	 * 
//	 * <pre>
//	 * 通过s11建立3种映射关系
//	 * 1.userIpv4_sgwTeid --> msisdn_imsi_imei
//	 * 2.imsi --> msisdn_imsi_imei
//	 * 3.imei --> msisdn_imsi_imei
//	 * </pre>
//	 * 
//	 * @param param
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo fillS11(S11FillParam param, XdrSingleSourceS10S11 data) {
//		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
//				.getImei(), userIpv4 = param.getUserIpv4(), sgwTeid = param
//				.getSgwTeid();
//		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
//		String value = null;
//		BackfillInfo info = null;
//		if (isNeedFill) {
//			info = getInfo(imsi, RuleType.IMSI);
//			if (info != null) {
//				setFillInfo(data, info);
//				value = getValue(info.msisdn, info.imsi, info.imei);// 注意使用回填后获取的信息
//				buildRule0(userIpv4, sgwTeid, value);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (SdtpConfig.IS_FILL_S11) {
//					S11_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			value = getValue(msisdn, imsi, imei);
//			// s11回填规则
//			buildS11FillRule(imsi, imei, userIpv4, sgwTeid, value);
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//			FULLINFO_COUNT.incrementAndGet();
//		}
//		return info;
//	}
//
//	/**
//	 * s11重新尝试回填
//	 * 
//	 * @param data
//	 */
//	public void refillS11(String key, XdrSingleSourceS10S11 data) {
//		BackfillInfo info = null;
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			info = getInfo(data.getCommon().getImsi(), RuleType.IMSI);
//			if (info != null) {
//				setFillInfo(data, info);
//				String userIpv4 = data.getUserIpv4();
//				String sgwTeid = null;
//				List<Bearer> bearers = data.getBearers();
//				if (bearers != null && bearers.size() > 0) {
//					sgwTeid = String.valueOf(bearers.get(0)
//							.getBearerSGWGtpTeid());
//				}
//				String value = getValue(info.msisdn, info.imsi, info.imei);// 注意使用回填后获取的信息
//				buildRule0(userIpv4, sgwTeid, value);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);// 需要重新尝试回填
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		}
//	}
//
//	/**
//	 * s1mme回填逻辑
//	 * 
//	 * <pre>
//	 * 建立5种回填规则
//	 * 1.userIpv4_sgwTeid --> msisdn_imsi_imei
//	 * 2.imsi --> msisdn_imsi_imei
//	 * 3.imei --> msisdn_imsi_imei
//	 * 4.mmeS1apID_mmeGroupId_mmeCode --> msisdn_imsi_imei
//	 * 5.mmeGroupId_mmeCode_m_tmsi --> msisdn_imsi_imei
//	 * </pre>
//	 * 
//	 * @param param
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo fillS1mme(S1mmeFillParam param,
//			XdrSingleSourceS1MME data) {
//		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
//				.getImei(), mmeS1apID = param.getMmeS1apID(), mmeGroupID = param
//				.getMmeGroupID(), mmeCode = param.getMmeCode(), mTmsi = param
//				.getmTmsi(), userIpv4 = param.getUserIpv4(), sgwTeid = param
//				.getSgwTeid(), cellID = param.getCellID();
//		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
//		String value = null;
//		BackfillInfo info = null;
//		if (isNeedFill) {
//			info = getS1mmeFillInfo(param, false);
//			if (info != null) {
//				setFillInfo(data, info);
//				value = getValue(info.msisdn, info.imsi, info.imei);// 注意使用回填后获取的信息
//				buildS1mmeFillRule(info.imsi, info.imei, userIpv4, sgwTeid,
//						mmeS1apID, mmeGroupID, mmeCode, mTmsi, cellID, value);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				// 建立mme_s1apid->msisdn_imsi_imei对应关系（当imsi存在时就建立规则）
//				if (StringUtils.isNotBlank(imsi)) {
//					value = getValue(msisdn, imsi, imei);
//					buildRule1(mmeS1apID, mmeGroupID, mmeCode, value);
//					buildRule4(mmeS1apID, cellID, value);
//				}
//				S1MME_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//			}
//		} else {
//			value = getValue(msisdn, imsi, imei);
//			// s1mme回填规则
//			buildS1mmeFillRule(imsi, imei, userIpv4, sgwTeid, mmeS1apID,
//					mmeGroupID, mmeCode, mTmsi, cellID, value);
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//			FULLINFO_COUNT.incrementAndGet();
//		}
//		return info;
//	}
//
//	/**
//	 * s1mme重新尝试回填
//	 * 
//	 * @param key
//	 * @param data
//	 */
//	public void refillS1mme(String key, XdrSingleSourceS1MME data) {
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			String sgwTeid = null;
//			List<XdrSingleSourceS1MME.Bearer> bearers = data.getBearers();
//			if (bearers != null && bearers.size() > 0) {
//				sgwTeid = String.valueOf(bearers.get(0).getBearerSGWGtpTeid());
//			}
//			XdrSingleCommon common = data.getCommon();
//			String msisdn = common.getMsisdn();
//			String imsi = common.getImsi();
//			String imei = common.getImei();
//			String mmeS1apID = String.valueOf(data.getMmeUeS1apID());
//			String mmeGroupID = String.valueOf(data.getMmeGroupID());
//			String mmeCode = String.valueOf(data.getMmeCode());
//			String mTmsi = String.valueOf(data.getmTmsi());
//			String userIpv4 = data.getUserIpv4();
//			String cellID = String.valueOf(data.getCellID());
//			S1mmeFillParam param = new S1mmeFillParam(msisdn, imsi, imei,
//					mmeS1apID, mmeGroupID, mmeCode, mTmsi, userIpv4, sgwTeid,
//					cellID);
//			BackfillInfo info = getS1mmeFillInfo(param, false);
//			if (info != null) {
//				setFillInfo(data, info);
//				String value = getValue(info.msisdn, info.imsi, info.imei);
//				buildS1mmeFillRule(info.imsi, info.imei, userIpv4, sgwTeid,
//						mmeS1apID, mmeGroupID, mmeCode, mTmsi, cellID, value);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);// 需要重新尝试回填
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		}
//	}
//
//	/**
//	 * s6a回填逻辑
//	 * 
//	 * @param msisdn
//	 * @param imsi
//	 * @param imei
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo fillS6a(S6aFillParam param, XdrSingleSourceS6a data) {
//		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
//				.getImei();
//		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
//		BackfillInfo info = null;
//		if (isNeedFill) {
//			if (isNotBlank(imsi)) {
//				info = getInfo(imsi, RuleType.IMSI);
//				if (info != null) {
//					setFillInfo(data, info);
//					SUCC_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				} else {
//					S6A_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//				}
//			} else {
//				FAILING_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			}
//		} else {
//			String value = getValue(msisdn, imsi, imei);
//			buildImsiAndImeiFillRule(imsi, imei, value);
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//			FULLINFO_COUNT.incrementAndGet();
//		}
//		return info;
//	}
//
//	/**
//	 * 重新尝试s6a回填
//	 * 
//	 * @param data
//	 */
//	public void refillS6a(String key, XdrSingleSourceS6a data) {
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			String imsi = data.getCommon().getImsi();
//			BackfillInfo info = getInfo(imsi, RuleType.IMSI);
//			if (info != null) {
//				setFillInfo(data, info);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);// 需要重新尝试回填
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		}
//	}
//
//	/**
//	 * sgs回填逻辑
//	 * 
//	 * @param msisdn
//	 * @param imsi
//	 * @param imei
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo fillSgs(SgsFillParam param, XdrSingleSourceSGs data) {
//		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
//				.getImei();
//		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
//		BackfillInfo info = null;
//		if (isNeedFill) {
//			if (isNotBlank(imsi)) {
//				info = getInfo(imsi, RuleType.IMSI);
//				if (info != null) {
//					setFillInfo(data, info);
//					SUCC_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				} else {
//					SGS_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//				}
//			} else {
//				FAILING_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			}
//		} else {
//			String value = getValue(msisdn, imsi, imei);
//			buildImsiAndImeiFillRule(imsi, imei, value);
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//			FULLINFO_COUNT.incrementAndGet();
//		}
//		return info;
//	}
//
//	/**
//	 * 重新尝试sgs回填
//	 * 
//	 * @param data
//	 */
//	public void refillSgs(String key, XdrSingleSourceSGs data) {
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			String imsi = data.getCommon().getImsi();
//			BackfillInfo info = getInfo(imsi, RuleType.IMSI);
//			if (info != null) {
//				setFillInfo(data, info);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);// 需要重新尝试回填
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		}
//	}
//
//	/**
//	 * s1u回填逻辑
//	 * 
//	 * @param msisdn
//	 * @param imsi
//	 * @param imei
//	 * @param data
//	 * @return
//	 */
//	public BackfillInfo fillS1U(S1UFillParam param, XdrSingleSourceS1U data) {
//		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
//				.getImei();
//		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
//		BackfillInfo info = null;
//		if (isNeedFill) {
//			if (isNotBlank(imsi)) {
//				info = getInfo(imsi, RuleType.IMSI);
//				if (info != null) {// 设置s1u回填信息
//					setFillInfo(data, info);
//					data.getMobileCommon().setImei(info.imei);
//					data.getMobileCommon().setImsi(info.imsi);
//					data.getMobileCommon().setMsisdn(info.msisdn);
//					SUCC_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				} else {
//					if (SdtpConfig.IS_FILL_S11) {
//						S1U_PENDING_CACHE.update(UUIDUtils.getUUID(), data);
//					} else {
//						FAILING_FILL_COUNT.incrementAndGet();
//						FilledXdrFileOutput.output(data);
//						FilledXdrSdtpOutput.output(data);
//						FilledXdrKafkaOutput.output(data);
//						CustomXdrKafkaOutput.output(data);
//					}
//				}
//			} else {
//				FAILING_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			}
//		} else {
//			String value = getValue(msisdn, imsi, imei);
//			buildImsiAndImeiFillRule(imsi, imei, value);
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//			FULLINFO_COUNT.incrementAndGet();
//		}
//		return info;
//	}
//
//	/**
//	 * 重新尝试s1u回填
//	 * 
//	 * @param data
//	 */
//	public void refillS1U(String key, XdrSingleSourceS1U data) {
//		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
//			REFILL_COUNT.incrementAndGet();
//			String imsi = data.getCommon().getImsi();
//			BackfillInfo info = getInfo(imsi, RuleType.IMSI);
//			if (info != null) {
//				setFillInfo(data, info);
//				SUCC_FILL_COUNT.incrementAndGet();
//				FilledXdrFileOutput.output(data);
//				FilledXdrSdtpOutput.output(data);
//				FilledXdrKafkaOutput.output(data);
//				CustomXdrKafkaOutput.output(data);
//			} else {
//				if (data.getTryTimeLeft().get() != 0) {
//					PENDING_XDR_DATA_QUEUE.add(data);// 需要重新尝试回填，在guava cache
//														// onRemoval中调用
//				} else {
//					FAILING_FILL_COUNT.incrementAndGet();
//					FilledXdrFileOutput.output(data);
//					FilledXdrSdtpOutput.output(data);
//					FilledXdrKafkaOutput.output(data);
//					CustomXdrKafkaOutput.output(data);
//				}
//			}
//		} else {
//			FAILING_FILL_COUNT.incrementAndGet();
//			FilledXdrFileOutput.output(data);
//			FilledXdrSdtpOutput.output(data);
//			FilledXdrKafkaOutput.output(data);
//			CustomXdrKafkaOutput.output(data);
//		}
//	}
//
//	public static void main(String[] args) {
////		AbstractCmBackFill fill = new CmBackFillWithLocalCache();
////		short procedureType = 0;
////		Date startTime = null;
////		Date endTime = null;
////		short procedureStatus = 0;
////		int cause = 0;
////		short keyword1 = 0;
////		long mmeUeS1apID = 5566;
////		int mmeGroupID = 0;
////		short mmeCode = 0;
////		long mTmsi = 0;
////		long tmsi = 0;
////		String userIpv4 = null;
////		String userIpv6 = null;
////		String mmeIpAdd = null;
////		String enbIpAdd = null;
////		int mmePort = 0;
////		int enbPort = 0;
////		int tac = 0;
////		long cellID = Long.MAX_VALUE;
////		int otherTac = 0;
////		long otherEci = 0;
////		String apn = null;
////		short epsBearerNumber = 0;
////		List<com.eversec.lte.model.single.XdrSingleSourceS1MME.Bearer> bearers = new ArrayList<>();
////		XdrSingleSourceS1MME data = new XdrSingleSourceS1MME(procedureType,
////				startTime, endTime, procedureStatus, cause, keyword1,
////				mmeUeS1apID, mmeGroupID, mmeCode, mTmsi, tmsi, userIpv4,
////				userIpv6, mmeIpAdd, enbIpAdd, mmePort, enbPort, tac, cellID,
////				otherTac, otherEci, apn, epsBearerNumber, bearers);
////		int length = 0;
////		String city = null;
////		byte[] xdrId = null;
////		short rat = 0;
////		String imsi = "460000";
////		String imei = null;
////		String msisdn = null;
////		XdrSingleCommon common = new XdrSingleCommon(length, city, Interface,
////				xdrId, rat, imsi, imei, msisdn);
////		data.setCommon(common);
////		S1mmeFillParam param = new S1mmeFillParam(msisdn, imsi, imei,
////				mmeUeS1apID, mmeGroupID, mmeCode, mTmsi, userIpv4, null, cellID);
////		fill.fillS1mme(param, data);
////		System.out.println(RULE4_CACHE.size());
//	}
//}
