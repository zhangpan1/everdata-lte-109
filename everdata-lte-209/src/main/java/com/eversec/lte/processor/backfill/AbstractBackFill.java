package com.eversec.lte.processor.backfill;

import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE_STR;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT_STR;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT_STR;
import static com.eversec.lte.processor.data.StaticData.FAILING_FILL_COUNT;
import static com.eversec.lte.processor.data.StaticData.FULLINFO_COUNT;
import static com.eversec.lte.processor.data.StaticData.REFILL_COUNT;
import static com.eversec.lte.processor.data.StaticData.SUCC_FILL_COUNT;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.common.constant.CommonConstants;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS10S11.Bearer;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.processor.statistics.BackFillStat;
import com.eversec.lte.sdtp.file.SdtpFileFillOutputTools;
import com.eversec.lte.sdtp.file.SdtpFileGroupingFillOutputTools;
import com.eversec.lte.sdtp.tokafka.SdtpToKafkaOutputTools;
import com.eversec.lte.sdtp.tosdtp.SdtpToSdtpOutputTools;
import com.eversec.lte.ttl.TTLTools;
import com.eversec.lte.ttl.XdrPendingDelay;
import com.eversec.lte.utils.FormatUtils;
import com.eversec.lte.vo.backfill.S11FillParam;
import com.eversec.lte.vo.backfill.S1UFillParam;
import com.eversec.lte.vo.backfill.S1mmeFillParam;
import com.eversec.lte.vo.backfill.S6aFillParam;
import com.eversec.lte.vo.backfill.SgsFillParam;
import com.eversec.lte.vo.backfill.UemrFillParam;
import com.eversec.lte.vo.backfill.UuFillParam;
import com.eversec.lte.vo.backfill.X2FillParam;

/**
 * <pre>
 * LTE单接口回填工具</br>
 * </pre>
 * 
 * 
 */
public abstract class AbstractBackFill implements IXdrBackFill  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1508026110656118362L;

	protected static Logger logger = LoggerFactory
			.getLogger(AbstractBackFill.class);
	/**
	 * userIpv4_sgwTeid
	 */
	public static int RULE0_TTL_SECOND;
	/**
	 * mmeS1apID_mmeGroupID_mmeCode
	 */
	public static int RULE1_TTL_SECOND;
	/**
	 * mmeGroupID_mmeCode_mTmsi
	 */
	public static int RULE2_TTL_SECOND;
	/**
	 * mmeS1apID_cellID
	 */
	public static int RULE4_TTL_SECOND;

	/**
	 * userIpv4_sgwTeid
	 */
	public static AtomicLong RULE0_COUNT = new AtomicLong();
	/**
	 * mmeS1apID_mmeGroupID_mmeCode
	 */
	public static AtomicLong RULE1_COUNT = new AtomicLong();
	/**
	 * mmeGroupID_mmeCode_mTmsi
	 */
	public static AtomicLong RULE2_COUNT = new AtomicLong();
	/**
	 * mmeS1apID_cellID
	 */
	public static AtomicLong RULE4_COUNT = new AtomicLong();

	public static final String EX = "ex";// 单位：秒
	public static final String NX = "nx";// key不存在，则存储，存在则不存。
	//规则库
	public static enum RuleType {
		IMSI, IMEI, RULE0, RULE1, RULE2, RULE3, RULE4
	}

	static {
		long rule_size = SdtpConfig.getPersistentRuleSize();
		RULE0_TTL_SECOND = SdtpConfig.getRule0TTLSecond();
		RULE1_TTL_SECOND = SdtpConfig.getRule1TTLSecond();
		RULE2_TTL_SECOND = SdtpConfig.getRule2TTLSecond();
		RULE4_TTL_SECOND = SdtpConfig.getRule4TTLSecond();
		
		System.out.println("RULE0_TTL_SECOND : " + RULE0_TTL_SECOND);
		System.out.println("RULE1_TTL_SECOND : " + RULE1_TTL_SECOND);
		System.out.println("RULE2_TTL_SECOND : " + RULE2_TTL_SECOND);
		System.out.println("RULE4_TTL_SECOND : " + RULE4_TTL_SECOND);
	}

	/**
	 * 初始化
	 */
	public static void init() {
		logger.info("init backfill util !");
	}
	
	

	static boolean useBackfillTTL = SdtpConfig.isUseBackfillTTL();

	static TTLTools ttlTools  = new TTLTools(2,10);
	
	public void delayXdr(XdrSingleSource source){
		try {
			if (useBackfillTTL) {
				ttlTools.delay(new XdrPendingDelay(source, this));
			}else{
				new XdrPendingDelay(source, this).run();;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String report2(){
		if (useBackfillTTL) {
			return ttlTools.report();
		} 
		return "";
	}
	
	public void output(XdrSingleSource  data) {
//		FilledXdrFileOutput.output(data);
//		FilledXdrSdtpOutput.output(data);
		if(SdtpConfig.IS_USE_GROUPING_OUTPUT){
			SdtpFileGroupingFillOutputTools.output(data);
		}else{
			SdtpFileFillOutputTools.output(data);
		}
		SdtpToSdtpOutputTools.output(data);

//		FilledXdrKafkaOutput.output(data);
//		CustomXdrKafkaOutput.output(data);
		
		SdtpToKafkaOutputTools.outputFilled(data);
		SdtpToKafkaOutputTools.output(data);
		
		
		BackFillStat.addAfterXdr(data);
	}

	/**
	 * 移动版本设置回填信息
	 * 
	 * @param data
	 * @param info
	 */
	protected void setFillInfo(XdrSingleSource data, XdrBackFillInfo info) {
		if (info != null && data != null) {
			XdrSingleCommon common = data.getCommon();
			if (common != null) {
				common.setMsisdn(info.msisdn);
				common.setImsi(info.imsi);
				common.setImei(info.imei);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#fillUu(com.eversec.lte.vo.backfill.UuFillParam, com.eversec.lte.model.single.XdrSingleSourceUu)
	 */
	public void fillUu(UuFillParam param, XdrSingleSourceUu data) {
		String mmeS1apID = param.getMmeS1apID(), mmeGroupID = param
				.getMmeGroupID(), mmeCode = param.getMmeCode();
		XdrBackFillInfo info = getInfo(getRule1(mmeS1apID, mmeGroupID, mmeCode),
				RuleType.RULE1);
		if (info != null) {
			setFillInfo(data, info);
			SUCC_FILL_COUNT.incrementAndGet();
			
			output(data);
			
		} else {
			delayXdr( data);
		}
	}

	

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillUu(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceUu)
	 */
	public void refillUu(  XdrSingleSourceUu data) {
		XdrBackFillInfo info = null;
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			info = getInfo(
					getRule1(String.valueOf(data.getMmeUeS1apId()),
							String.valueOf(data.getMmeGroupID()),
							String.valueOf(data.getMmeCode())), RuleType.RULE1);
			if (info != null) {
				setFillInfo(data, info);
				SUCC_FILL_COUNT.incrementAndGet();
				
				output(data);
				
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);
				} else {
					FAILING_FILL_COUNT.incrementAndGet();
					
					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();
			
			output(data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#fillX2(com.eversec.lte.vo.backfill.X2FillParam, com.eversec.lte.model.single.XdrSingleSourceX2)
	 */
	public void fillX2(X2FillParam param, XdrSingleSourceX2 data) {
		String mmeS1apID = param.getMmeS1apID(), mmeGroupID = param
				.getMmeGroupID(), mmeCode = param.getMmeCode();
		XdrBackFillInfo info = getInfo(getRule1(mmeS1apID, mmeGroupID, mmeCode),
				RuleType.RULE1);
		if (info != null) {
			setFillInfo(data, info);
			SUCC_FILL_COUNT.incrementAndGet();
			
			output(data);
			
		} else {
			delayXdr( data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillX2(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceX2)
	 */
	public void refillX2( XdrSingleSourceX2 data) {
		XdrBackFillInfo info = null;
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			info = getInfo(
					getRule1(String.valueOf(data.getMmeUeS1apId()),
							String.valueOf(data.getMmeGroupID()),
							String.valueOf(data.getMmeCode())), RuleType.RULE1);
			if (info != null) {
				setFillInfo(data, info);
				SUCC_FILL_COUNT.incrementAndGet();
				
				output(data);
				
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);// 需要重新尝试回填，在guava cache
					// onRemoval中调用
				} else {
					FAILING_FILL_COUNT.incrementAndGet();
					
					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();
			
			output(data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#filledUemr(com.eversec.lte.vo.backfill.UemrFillParam, com.eversec.lte.model.single.XdrSingleSourceUEMR)
	 */
	public void filledUemr(UemrFillParam param, XdrSingleSourceUEMR data) {
		String mmeS1apID = param.getMmeS1apID();
		String cellID = param.getCellID();
		XdrBackFillInfo info = getInfo(getRule4(mmeS1apID, cellID), RuleType.RULE4);
		// cellID非默认值时，获取不到用户信息需重新尝试
		if (info == null && !cellID.equals(SdtpConstants.MAX_UNSIGNED_INT_STR)) {
			info = getInfo(
					getRule4(mmeS1apID, SdtpConstants.MAX_UNSIGNED_INT_STR),
					RuleType.RULE4);
		}
		if (info != null) {
			setFillInfo(data, info);
			SUCC_FILL_COUNT.incrementAndGet();

			output(data);
		} else {
			delayXdr( data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillUEMR(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceUEMR)
	 */
	public void refillUEMR( XdrSingleSourceUEMR data) {
		XdrBackFillInfo info = null;
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			String mmeS1apID = String.valueOf(data.getMmeUeS1apId());
			String cellID = String.valueOf(data.getCellID());
			info = getInfo(getRule4(mmeS1apID, cellID), RuleType.RULE4);
			// cellID非默认值时，获取不到用户信息需重新尝试
			if (info == null
					&& !cellID.equals(SdtpConstants.MAX_UNSIGNED_INT_STR)) {
				info = getInfo(
						getRule4(mmeS1apID, SdtpConstants.MAX_UNSIGNED_INT_STR),
						RuleType.RULE4);
			}
			if (info != null) {
				setFillInfo(data, info);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);
				} else {
					FAILING_FILL_COUNT.incrementAndGet();
					
					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();
			
			output(data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#fillS11(com.eversec.lte.vo.backfill.S11FillParam, com.eversec.lte.model.single.XdrSingleSourceS10S11)
	 */
	public void fillS11(S11FillParam param, XdrSingleSourceS10S11 data) {
		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
				.getImei(), userIpv4 = param.getUserIpv4(), sgwTeid = param
				.getSgwTeid();
		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
		String value = null;
		XdrBackFillInfo info = null;
		if (isNeedFill) {
			info = getInfo(imsi, RuleType.IMSI);
			if (info != null) {
				setFillInfo(data, info);
				value = getValue(info.msisdn, info.imsi, info.imei);// 注意使用回填后获取的信息
				buildRule0(userIpv4, sgwTeid, value);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
				
			} else {
				if (SdtpConfig.IS_FILL_S11) {
					delayXdr( data);
				} else {
					FAILING_FILL_COUNT.incrementAndGet();

					output(data);
				}
			}
		} else {
			value = getValue(msisdn, imsi, imei);
			// s11回填规则
			buildS11FillRule(imsi, imei, userIpv4, sgwTeid, value);

			output(data);
			FULLINFO_COUNT.incrementAndGet();
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillS11(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceS10S11)
	 */
	public void refillS11(  XdrSingleSourceS10S11 data) {
		XdrBackFillInfo info = null;
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			info = getInfo(data.getCommon().getImsi(), RuleType.IMSI);
			if (info != null) {
				setFillInfo(data, info);
				String userIpv4 = FormatUtils.getIp(data.getUserIpv4());
				String sgwTeid = null;
				List<Bearer> bearers = data.getBearers();
				if (bearers != null && bearers.size() > 0) {
					sgwTeid = String.valueOf(bearers.get(0)
							.getBearerSGWGtpTeid());
				}
				String value = getValue(info.msisdn, info.imsi, info.imei);// 注意使用回填后获取的信息
				buildRule0(userIpv4, sgwTeid, value);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);// 需要重新尝试回填
				} else {
					FAILING_FILL_COUNT.incrementAndGet();

					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();

			output(data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#fillS1mme(com.eversec.lte.vo.backfill.S1mmeFillParam, com.eversec.lte.model.single.XdrSingleSourceS1MME)
	 */
	public void fillS1mme(S1mmeFillParam param,
			XdrSingleSourceS1MME data) {
		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
				.getImei(), mmeS1apID = param.getMmeS1apID(), mmeGroupID = param
				.getMmeGroupID(), mmeCode = param.getMmeCode(), mTmsi = param
				.getmTmsi(), userIpv4 = param.getUserIpv4(), sgwTeid = param
				.getSgwTeid(), cellID = param.getCellID();
		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
		String value = null;
		XdrBackFillInfo info = null;
		if (isNeedFill) {
			info = getS1mmeFillInfo(param, false);
			if (info != null) {
				setFillInfo(data, info);
				value = getValue(info.msisdn, info.imsi, info.imei);// 注意使用回填后获取的信息
				buildS1mmeFillRule(info.imsi, info.imei, userIpv4, sgwTeid,
						mmeS1apID, mmeGroupID, mmeCode, mTmsi, cellID, value);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
			} else {
				// 建立mme_s1apid->msisdn_imsi_imei对应关系（当imsi存在时就建立规则）
				if (StringUtils.isNotBlank(imsi)) {
					value = getValue(msisdn, imsi, imei);
					buildRule1(mmeS1apID, mmeGroupID, mmeCode, value);
					buildRule4(mmeS1apID, cellID, value);
				}
				delayXdr( data);
			}
		} else {
			value = getValue(msisdn, imsi, imei);
			// s1mme回填规则
			buildS1mmeFillRule(imsi, imei, userIpv4, sgwTeid, mmeS1apID,
					mmeGroupID, mmeCode, mTmsi, cellID, value);

			output(data);
			FULLINFO_COUNT.incrementAndGet();
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillS1mme(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceS1MME)
	 */
	public void refillS1mme( XdrSingleSourceS1MME data) {
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			String sgwTeid = null;
			List<XdrSingleSourceS1MME.Bearer> bearers = data.getBearers();
			if (bearers != null && bearers.size() > 0) {
				sgwTeid = String.valueOf(bearers.get(0).getBearerSGWGtpTeid());
			}
			XdrSingleCommon common = data.getCommon();
			String msisdn = common.getMsisdn();
			String imsi = common.getImsi();
			String imei = common.getImei();
			String mmeS1apID = String.valueOf(data.getMmeUeS1apID());
			String mmeGroupID = String.valueOf(data.getMmeGroupID());
			String mmeCode = String.valueOf(data.getMmeCode());
			String mTmsi = String.valueOf(data.getmTmsi());
			String userIpv4 = FormatUtils.getIp(data.getUserIpv4());
			String cellID = String.valueOf(data.getCellID());
			S1mmeFillParam param = new S1mmeFillParam(msisdn, imsi, imei,
					mmeS1apID, mmeGroupID, mmeCode, mTmsi, userIpv4, sgwTeid,
					cellID);
			XdrBackFillInfo info = getS1mmeFillInfo(param, false);
			if (info != null) {
				setFillInfo(data, info);
				String value = getValue(info.msisdn, info.imsi, info.imei);
				buildS1mmeFillRule(info.imsi, info.imei, userIpv4, sgwTeid,
						mmeS1apID, mmeGroupID, mmeCode, mTmsi, cellID, value);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);// 需要重新尝试回填
				} else {
					FAILING_FILL_COUNT.incrementAndGet();

					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();

			output(data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#fillS6a(com.eversec.lte.vo.backfill.S6aFillParam, com.eversec.lte.model.single.XdrSingleSourceS6a)
	 */
	public void fillS6a(S6aFillParam param, XdrSingleSourceS6a data) {
		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
				.getImei();
		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
		XdrBackFillInfo info = null;
		if (isNeedFill) {
			if (isNotBlank(imsi)) {
				info = getInfo(imsi, RuleType.IMSI);
				if (info != null) {
					setFillInfo(data, info);
					SUCC_FILL_COUNT.incrementAndGet();

					output(data);
				} else {
					delayXdr( data);
				}
			} else {
				FAILING_FILL_COUNT.incrementAndGet();

				output(data);
			}
		} else {
			String value = getValue(msisdn, imsi, imei);
			buildImsiAndImeiFillRule(imsi, imei, value);

			output(data);
			FULLINFO_COUNT.incrementAndGet();
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillS6a(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceS6a)
	 */
	public void refillS6a( XdrSingleSourceS6a data) {
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			String imsi = data.getCommon().getImsi();
			XdrBackFillInfo info = getInfo(imsi, RuleType.IMSI);
			if (info != null) {
				setFillInfo(data, info);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);// 需要重新尝试回填
				} else {
					FAILING_FILL_COUNT.incrementAndGet();

					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();

			output(data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#fillSgs(com.eversec.lte.vo.backfill.SgsFillParam, com.eversec.lte.model.single.XdrSingleSourceSGs)
	 */
	public void fillSgs(SgsFillParam param, XdrSingleSourceSGs data) {
		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
				.getImei();
		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
		XdrBackFillInfo info = null;
		if (isNeedFill) {
			if (isNotBlank(imsi)) {
				info = getInfo(imsi, RuleType.IMSI);
				if (info != null) {
					setFillInfo(data, info);
					SUCC_FILL_COUNT.incrementAndGet();

					output(data);
				} else {
					delayXdr( data);
				}
			} else {
				FAILING_FILL_COUNT.incrementAndGet();

				output(data);
			}
		} else {
			String value = getValue(msisdn, imsi, imei);
			buildImsiAndImeiFillRule(imsi, imei, value);

			output(data);
			FULLINFO_COUNT.incrementAndGet();
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillSgs(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceSGs)
	 */
	public void refillSgs( XdrSingleSourceSGs data) {
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			String imsi = data.getCommon().getImsi();
			XdrBackFillInfo info = getInfo(imsi, RuleType.IMSI);
			if (info != null) {
				setFillInfo(data, info);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);// 需要重新尝试回填
				} else {
					FAILING_FILL_COUNT.incrementAndGet();

					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();

			output(data);
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#fillS1U(com.eversec.lte.vo.backfill.S1UFillParam, com.eversec.lte.model.single.XdrSingleSourceS1U)
	 */
	public void fillS1U(S1UFillParam param, XdrSingleSourceS1U data) {
		String msisdn = param.getMsisdn(), imsi = param.getImsi(), imei = param
				.getImei();
		boolean isNeedFill = !isNotBlank(msisdn, imsi, imei);
		XdrBackFillInfo info = null;
		if (isNeedFill) {
			if (isNotBlank(imsi)) {
				info = getInfo(imsi, RuleType.IMSI);
				if (info != null) {// 设置s1u回填信息
					setFillInfo(data, info);
					data.getMobileCommon().setImei(info.imei);
					data.getMobileCommon().setImsi(info.imsi);
					data.getMobileCommon().setMsisdn(info.msisdn);
					SUCC_FILL_COUNT.incrementAndGet();

					output(data);
				} else {
					if (SdtpConfig.IS_FILL_S11) {
						delayXdr( data);
					} else {
						FAILING_FILL_COUNT.incrementAndGet();

						output(data);
					}
				}
			} else {
				FAILING_FILL_COUNT.incrementAndGet();

				output(data);
			}
		} else {
			String value = getValue(msisdn, imsi, imei);
			buildImsiAndImeiFillRule(imsi, imei, value);

			output(data);
			FULLINFO_COUNT.incrementAndGet();
		}
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#refillS1U(java.lang.String, com.eversec.lte.model.single.XdrSingleSourceS1U)
	 */
	public void refillS1U( XdrSingleSourceS1U data) {
		if (data.getTryTimeLeft().decrementAndGet() >= 0) {
			REFILL_COUNT.incrementAndGet();
			String imsi = data.getCommon().getImsi();
			XdrBackFillInfo info = getInfo(imsi, RuleType.IMSI);
			if (info != null) {
				setFillInfo(data, info);
				SUCC_FILL_COUNT.incrementAndGet();

				output(data);
			} else {
				if (data.getTryTimeLeft().get() != 0) {
					delayXdr(data);// 需要重新尝试回填，在guava cache
														// onRemoval中调用
				} else {
					FAILING_FILL_COUNT.incrementAndGet();

					output(data);
				}
			}
		} else {
			FAILING_FILL_COUNT.incrementAndGet();

			output(data);
		}
	}


	/**
	 * 获取存储value字符串
	 * 
	 * @param msisdn
	 * @param imsi
	 * @param imei
	 * @return msisdn_imsi_imei
	 */
	protected String getValue(String msisdn, String imsi, String imei) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(msisdn).append(CommonConstants.UNDERLINE).append(imsi)
				.append(CommonConstants.UNDERLINE).append(imei);
		return tmp.toString();
	}

	/**
	 * 获取规则0字符串
	 * 
	 * @param userIpv4
	 * @param sgwTeid
	 * @return userIpv4_sgwTeid
	 */
	protected String getRule0(String userIpv4, String sgwTeid) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(userIpv4).append(CommonConstants.UNDERLINE).append(sgwTeid);
		return tmp.toString();
	}

	/**
	 * 获取规则1字符串
	 * 
	 * @param mmeS1apID
	 * @param mmeGroupID
	 * @param mmeCode
	 * @return mmeS1apID_mmeGroupID_mmeCode
	 */
	protected String getRule1(String mmeS1apID, String mmeGroupID,
			String mmeCode) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(mmeS1apID).append(CommonConstants.UNDERLINE)
				.append(mmeGroupID).append(CommonConstants.UNDERLINE)
				.append(mmeCode);
		return tmp.toString();
	}

	/**
	 * 获取规则2字符串
	 * 
	 * @param mmeGroupID
	 * @param mmeCode
	 * @param mTmsi
	 * @return mmeGroupID_mmeCode_mTmsi
	 */
	protected String getRule2(String mmeGroupID, String mmeCode, String mTmsi) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(mmeGroupID).append(CommonConstants.UNDERLINE)
				.append(mmeCode).append(CommonConstants.UNDERLINE)
				.append(mTmsi);
		return tmp.toString();
	}

	/**
	 * 获取规则3字符串
	 * 
	 * @param additionalMmeGroupID
	 * @param additionalMmeCode
	 * @param additionalMTmsi
	 * @return
	 */
	protected String getRule3(String additionalMmeGroupID,
			String additionalMmeCode, String additionalMTmsi) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(additionalMmeGroupID).append(CommonConstants.UNDERLINE)
				.append(additionalMmeCode).append(CommonConstants.UNDERLINE)
				.append(additionalMTmsi);
		return tmp.toString();
	}

	/**
	 * 获取规则4字符串
	 * 
	 * @param mmeS1apID
	 * @param cellID
	 * @return mmeS1apID_cellID
	 */
	protected String getRule4(String mmeS1apID, String cellID) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(mmeS1apID).append(CommonConstants.UNDERLINE).append(cellID);
		return tmp.toString();
	}

	/**
	 * <pre>
	 * 创建s11回填规则
	 * 1.imsi->value
	 * 2.imei->value
	 * 3.rule0->value
	 * </pre>
	 * 
	 * @param imsi
	 * @param imei
	 * @param userIpv4
	 * @param sgwTeid
	 * @param value
	 */
	protected abstract void buildS11FillRule(String imsi, String imei,
			String userIpv4, String sgwTeid, String value);

	/**
	 * <pre>
	 * 创建s1mme回填规则
	 *  1.imsi->value
	 *  2.imei->value
	 *  3.rule0->value
	 *  4.rule1->value
	 *  5.rule2->value
	 *  6.rule4->value
	 * </pre>
	 * 
	 * @param imsi
	 * @param imei
	 * @param userIpv4
	 * @param sgwTeid
	 * @param mmeS1apID
	 * @param mmeGroupID
	 * @param mmeCode
	 * @param mTmsi
	 * @param cellID
	 * @param value
	 */
	protected abstract void buildS1mmeFillRule(String imsi, String imei,
			String userIpv4, String sgwTeid, String mmeS1apID,
			String mmeGroupID, String mmeCode, String mTmsi, String cellID,
			String value);

	/**
	 * <pre>
	 * 创建imei/imsi回填规则
	 *  1.imsi->value
	 *  2.imei->value
	 *  3.rule0->value
	 * </pre>
	 * 
	 * @param imsi
	 * @param imei
	 * @param value
	 */
	protected abstract void buildImsiAndImeiFillRule(String imsi, String imei,
			String value);

	/**
	 * 建立userIpv4_sgwTeid与回填信息的映射关系
	 * 
	 * @param userIpv4
	 * @param sgwTeid
	 * @param value
	 */
	protected abstract void buildRule0(String userIpv4, String sgwTeid,
			String value);

	/**
	 * 建立userIpv4_sgwTeid与回填信息的映射关系
	 * 
	 * @param rule0
	 * @param value
	 */
	protected abstract void buildRule0(String rule0, String value);

	/**
	 * 建立mmeS1apID_mmeGroupID_mmeCode与回填信息的映射关系
	 * 
	 * @param mmeS1apID
	 * @param mmeGroupID
	 * @param mmeCode
	 * @param value
	 */
	protected abstract void buildRule1(String mmeS1apID, String mmeGroupID,
			String mmeCode, String value);

	/**
	 * 建立mmeS1apID_mmeGroupID_mmeCode与回填信息的映射关系
	 * 
	 * @param rule1
	 * @param value
	 */
	protected abstract void buildRule1(String rule1, String value);

	/**
	 * 建立mmeGroupID_mmeCode_mTmsi与回填信息的映射关系
	 * 
	 * @param mmeGroupID
	 * @param mmeCode
	 * @param mTmsi
	 * @param value
	 */
	protected abstract void buildRule2(String mmeGroupID, String mmeCode,
			String mTmsi, String value);

	/**
	 * 建立mmeGroupID_mmeCode_mTmsi与回填信息的映射关系
	 * 
	 * @param rule2
	 * @param value
	 */
	protected abstract void buildRule2(String rule2, String value);

	/**
	 * 
	 * @param additionalMmeGroupID
	 * @param additionalMmeCode
	 * @param additionalMTmsi
	 * @param value
	 */
	protected abstract void buildRule3(String additionalMmeGroupID,
			String additionalMmeCode, String additionalMTmsi, String value);

	/**
	 * 建立mmeS1apID_cellID与回填信息的映射关系(用于UEMR回填)
	 * 
	 * @param mmeS1apID
	 * @param cellID
	 * @param value
	 */
	protected abstract void buildRule4(String mmeS1apID, String cellID,
			String value);

	/**
	 * 建立mmeS1apID_cellID与回填信息的映射关系(用于UEMR回填)
	 * 
	 * @param rule4
	 * @param value
	 */
	protected abstract void buildRule4(String rule4, String value);

	/**
	 * 
	 * @param userIpv4
	 * @param sgwTeid
	 * @return
	 */
	public XdrBackFillInfo getInfoByRule0(String userIpv4, String sgwTeid) {
		XdrBackFillInfo fillInfo = null;
		if (isNotBlank(userIpv4, sgwTeid)) {
			fillInfo = getInfo(getRule0(userIpv4, sgwTeid), RuleType.RULE0);
		}
		return fillInfo;
	}

	/**
	 * 通过mmeS1apID_mmeGroupID_mmeCode查询回填信息
	 * 
	 * @param mmeS1apID
	 * @param mmeGroupID
	 * @param mmeCode
	 * @return
	 */
	public XdrBackFillInfo getInfoByRule1(String mmeS1apID, String mmeGroupID,
			String mmeCode) {
		XdrBackFillInfo fillInfo = null;
		if (isNotBlank(mmeS1apID, mmeGroupID, mmeCode)
				&& !mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)) {
			fillInfo = getInfo(getRule1(mmeS1apID, mmeGroupID, mmeCode),
					RuleType.RULE1);
		}
		return fillInfo;
	}

	/**
	 * 通过mmeGroupID_mmeCode_mTmsi(guti)查询回填信息
	 * 
	 * @param mmeGroupID
	 * @param mmeCode
	 * @param mTmsi
	 * @return
	 */
	public XdrBackFillInfo getInfoByRule2(String mmeGroupID, String mmeCode,
			String mTmsi) {
		XdrBackFillInfo fillInfo = null;
		if (isNotBlank(mmeGroupID, mmeCode, mTmsi)
				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT)
				&& !mmeCode.equals(MAX_UNSIGNED_BYTE)
				&& !mTmsi.equals(MAX_UNSIGNED_INT)) {
			fillInfo = getInfo(getRule2(mmeGroupID, mmeCode, mTmsi),
					RuleType.RULE2);
		}
		return fillInfo;
	}

	/**
	 * 通过additionalMmeGroupID_additionalMmeCode_additionalMTmsi(guti)查询回填信息
	 * 
	 * @param additionalMmeGroupID
	 * @param additionalMmeCode
	 * @param additionalMTmsi
	 * @return
	 */
	public XdrBackFillInfo getInfoByRule3(String additionalMmeGroupID,
			String additionalMmeCode, String additionalMTmsi) {
		return getInfoByRule2(additionalMmeGroupID, additionalMmeCode,
				additionalMTmsi);
	}

	/**
	 * 通过多种方式获取S1MME回填信息
	 * 
	 * @param param
	 * @param isAdditional
	 * @return
	 */
	public XdrBackFillInfo getS1mmeFillInfo(S1mmeFillParam param,
			boolean isAdditional) {
		String imsi = param.getImsi(), imei = param.getImei(), userIpv4 = param
				.getUserIpv4(), sgwTeid = param.getSgwTeid(), mmeS1apID = param
				.getMmeS1apID(), mmeGroupID = param.getMmeGroupID(), mmeCode = param
				.getMmeCode(), mTmsi = param.getmTmsi();
		String additionalMmeGroupID = param.getAdditionalMmeGroupID();
		String additionalMmeCode = param.getAdditionalMmeCode();
		String additionalMTmsi = param.getAdditionalMTmsi();
		XdrBackFillInfo info = null;
		// 1.通过imsi获取回填信息
		if ((info = getInfo(imsi, RuleType.IMSI)) == null) {
			// 2.通过imei获取回填信息
			if ((info = getInfo(imei, RuleType.IMEI)) == null) {
				// 3.通过mmeS1apID_mmeGroupID_mmeCode获取回填信息
				if ((info = getInfoByRule1(mmeS1apID, mmeGroupID, mmeCode)) == null) {
					// 4.通过mmeGroupID_mmeCode_mTmsi获取回填信息
					if ((info = getInfoByRule2(mmeGroupID, mmeCode, mTmsi)) == null) {
						// 5.通过userIpv4_sgwTeid获取回填信息
						if ((info = getInfoByRule0(userIpv4, sgwTeid)) == null) {
							// 6.通过additionalMmeGroupID_additionalMmeCode_additionalMTmsi获取回填信息
							if (isAdditional
									&& (info = getInfoByRule3(
											additionalMmeGroupID,
											additionalMmeCode, additionalMTmsi)) != null) {
								info.fillType = RuleType.RULE3;// additionalMmeGroupID_additionalMmeCode_additionalMTmsi
							}
						} else {
							info.fillType = RuleType.RULE0;// userIpv4_sgwTeid
						}
					} else {
						info.fillType =  RuleType.RULE2;// mmeGroupID_mmeCode_mTmsi
					}
				} else {
					info.fillType =  RuleType.RULE1;// mmeS1apID_mmeGroupID_mmeCode回填
				}
			} else {
				info.fillType = RuleType.IMEI;// imei回填
			}
		} else {
			info.fillType = RuleType.IMSI;// imsi回填
		}
		return info;
	}

	/* (non-Javadoc)
	 * @see com.eversec.lte.processor.backfill2.IXdrBackFill#getInfo(java.lang.String, com.eversec.lte.processor.backfill2.AbstractBackFill.RuleType)
	 */
	public abstract XdrBackFillInfo getInfo(String key, RuleType type);

	public static boolean isNotBlank(String... arrs) {
		for (String str : arrs) {
			if (StringUtils.isBlank(str))
				return false;
		}
		return true;
	}

}
