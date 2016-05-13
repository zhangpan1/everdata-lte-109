package com.eversec.lte.processor.backfill2.tmp;
//
//package com.eversec.lte.processor.backfill2;
//
//import static com.eversec.lte.config.SdtpConfig.IS_GET_FORM_REDIS;
//import static com.eversec.lte.config.SdtpConfig.IS_SAVE_TO_REDIS;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT_STR;
//
//import java.util.Arrays;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//
//import com.eversec.common.constant.CommonConstants;
//import com.eversec.lte.cache.data.RuleData;
//import com.eversec.lte.processor.backfill.AbstractBackFill;
//
///**
// * use ehcache  replace redis  13000/s
// *
// */
//public class BackFillEhcache extends AbstractBackFill {
//
//	public void buildImsiRule(final String imsi, final String value) {
//		// imsi
//		Callable<? extends RuleData> imsiValueLoader = new Callable<RuleData>() {
//			@Override
//			public RuleData call() throws Exception {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(imsi, value);
//				return new RuleData(value);
//			}
//		};
//		RuleData rule;
//		try {
//			rule = PERSISTENT_RULE_CACHE.get(imsi, imsiValueLoader);
//			if (!rule.getRule().equals(value)) {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(imsi, value);
//				rule.setRule(value);
//			}
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public void buildImeiRule(final String imei, final String value) {
//		// imei
//		Callable<? extends RuleData> imeiValueLoader = new Callable<RuleData>() {
//			@Override
//			public RuleData call() throws Exception {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(imei, value);
//				return new RuleData(value);
//			}
//		};
//		RuleData rule;
//		try {
//			rule = PERSISTENT_RULE_CACHE.get(imei, imeiValueLoader);
//			if (!rule.getRule().equals(value)) {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(imei, value);
//				rule.setRule(value);
//			}
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void buildRule0(final String rule0, final String value) {
//		if (isNotBlank(rule0)) {
//			Callable<? extends RuleData> rule0ValueLoader = new Callable<RuleData>() {
//				@Override
//				public RuleData call() throws Exception {
//					if (IS_SAVE_TO_REDIS)
//						resource.set(rule0, value, NX, EX, RULE0_TTL_SECOND);
//					return new RuleData(value);
//				}
//			};
//			RuleData rule;
//			try {
//				rule = RULE0_CACHE.get(rule0, rule0ValueLoader);
//				if (!rule.getRule().equals(value)) {
//					if (IS_SAVE_TO_REDIS)
//						resource.set(rule0, value, NX, EX, RULE0_TTL_SECOND);
//					rule.setRule(value);
//				}
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//			// RULE0_COUNT.incrementAndGet();
//		}
//
//	}
//
//	@Override
//	public void buildRule0(String userIpv4, String sgwTeid, final String value) {
//		// userIpv4_sgwTeid
//		if (isNotBlank(userIpv4, sgwTeid)) {
//			final String rule0 = getRule0(userIpv4, sgwTeid);
//			buildRule0(rule0, value);
//		}
//	}
//
//	@Override
//	public void buildRule1(final String rule1, final String value) {
//		Callable<? extends RuleData> rule1ValueLoader = new Callable<RuleData>() {
//			@Override
//			public RuleData call() throws Exception {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(rule1, value, NX, EX, RULE1_TTL_SECOND);
//				return new RuleData(value);
//			}
//		};
//		RuleData rule;
//		try {
//			rule = RULE1_CACHE.get(rule1, rule1ValueLoader);
//			if (!rule.getRule().equals(value)) {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(rule1, value, NX, EX, RULE1_TTL_SECOND);
//				rule.setRule(value);
//			}
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		// RULE1_COUNT.incrementAndGet();
//	}
//
//	@Override
//	public void buildRule1(String mmeS1apID, String mmeGroupID, String mmeCode,
//			final String value) {
//		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)) {
//			final String rule1 = getRule1(mmeS1apID, mmeGroupID, mmeCode);
//			buildRule1(rule1, value);
//		}
//	}
//
//	@Override
//	public void buildRule2(final String rule2, final String value) {
//		Callable<? extends RuleData> rule2ValueLoader = new Callable<RuleData>() {
//			@Override
//			public RuleData call() throws Exception {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(rule2, value, NX, EX, RULE2_TTL_SECOND);
//				return new RuleData(value);
//			}
//		};
//		RuleData rule;
//		try {
//			rule = RULE2_CACHE.get(rule2, rule2ValueLoader);
//			if (!rule.getRule().equals(value)) {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(rule2, value, NX, EX, RULE2_TTL_SECOND);
//				rule.setRule(value);
//			}
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		// RULE2_COUNT.incrementAndGet();
//	}
//
//	@Override
//	public void buildRule2(String mmeGroupID, String mmeCode, String mTmsi,
//			final String value) {
//		if (!mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)
//				&& !mTmsi.equals(MAX_UNSIGNED_INT_STR)) {
//			final String rule2 = getRule2(mmeGroupID, mmeCode, mTmsi);
//			buildRule2(rule2, value);
//		}
//	}
//
//	@Override
//	public void buildRule3(String additionalMmeGroupID,
//			String additionalMmeCode, String additionalMTmsi, String value) {
//		buildRule2(additionalMmeGroupID, additionalMmeCode, additionalMTmsi,
//				value);
//	}
//
//	@Override
//	protected void buildRule4(String mmeS1apID, String cellID, String value) {
//		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)) {
//			final String rule4 = getRule4(mmeS1apID, cellID);
//			buildRule4(rule4, value);
//		}
//
//	}
//
//	@Override
//	protected void buildRule4(final String rule4, final String value) {
//		Callable<? extends RuleData> rule4ValueLoader = new Callable<RuleData>() {
//			@Override
//			public RuleData call() throws Exception {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(rule4, value, NX, EX, RULE4_TTL_SECOND);
//				return new RuleData(value);
//			}
//		};
//		RuleData rule;
//		try {
//			rule = RULE4_CACHE.get(rule4, rule4ValueLoader);
//			if (!rule.getRule().equals(value)) {
//				if (IS_SAVE_TO_REDIS)
//					resource.set(rule4, value, NX, EX, RULE4_TTL_SECOND);
//				rule.setRule(value);
//			}
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		// RULE4_COUNT.incrementAndGet();
//
//	}
//
//	public void buildS11FillRule(final String imsi, final String imei,
//			String userIpv4, String sgwTeid, final String value) {
//		buildImsiRule(imsi, value);
//		buildImeiRule(imei, value);
//		buildRule0(userIpv4, sgwTeid, value);
//	}
//
//	public void buildS1mmeFillRule(String imsi, String imei, String userIpv4,
//			String sgwTeid, String mmeS1apID, String mmeGroupID,
//			String mmeCode, String mTmsi, String cellID, String value) {
//		buildImsiRule(imsi, value);
//		buildImeiRule(imei, value);
//		buildRule0(userIpv4, sgwTeid, value);
//		buildRule1(mmeS1apID, mmeGroupID, mmeCode, value);
//		buildRule2(mmeGroupID, mmeCode, mTmsi, value);
//		buildRule4(mmeS1apID, cellID, value);
//
//	}
//
//	public void buildImsiAndImeiFillRule(String imsi, String imei, String value) {
//		buildImsiRule(imsi, value);
//		buildImeiRule(imei, value);
//	}
//
//	/**
//	 * 获取回填信息，msisdn_imsi_imei
//	 * 
//	 * @param key
//	 * @return
//	 */
//	public BackfillInfo getInfo(String key, RuleType type) {
//		boolean inLocalCache = false;
//		BackfillInfo info = null;
//		if (isNotBlank(key)) {
//			RuleData ruleData = null;
//			String value = null;
//			switch (type) {
//			case IMSI:
//				ruleData = PERSISTENT_RULE_CACHE.getIfPresent(key);
//				break;
//			case IMEI:
//				ruleData = PERSISTENT_RULE_CACHE.getIfPresent(key);
//				break;
//			case RULE0:
//				ruleData = RULE0_CACHE.getIfPresent(key);
//				break;
//			case RULE1:
//				ruleData = RULE1_CACHE.getIfPresent(key);
//				break;
//			case RULE2:
//				ruleData = RULE2_CACHE.getIfPresent(key);
//				break;
//			case RULE4:
//				ruleData = RULE4_CACHE.getIfPresent(key);
//				break;
//			default:
//				break;
//			}
//			if (ruleData != null){
//				value = ruleData.getRule();
//			}
//			
//			
//			if (value != null) {
//				inLocalCache = true;
//			} else if (IS_GET_FORM_REDIS) {
//				value = resource.get(key);
//				if (value != null && value.equals("OK")) {
//					resource.resetState();
//					value = resource.get(key);
//				}
//			}
//			if (value != null && !value.equals("null") && !value.equals("OK")) {
//				if (!inLocalCache) {
//					switch (type) {
//					case IMSI:
//						buildImsiRule(key, value);
//						break;
//					case IMEI:
//						buildImeiRule(key, value);
//						break;
//					case RULE0:
//						buildRule0(key, value);
//						break;
//					case RULE1:
//						buildRule1(key, value);
//						break;
//					case RULE2:
//						buildRule2(key, value);
//						break;
//					case RULE4:
//						buildRule4(key, value);
//						break;
//					default:
//						break;
//					}
//				}
//				String[] id = value.split(CommonConstants.UNDERLINE,3);
//				if (id.length >= 3) {
//					info = new BackfillInfo(id[0], id[1], id[2]);
//				} else {
//					logger.warn("error key : {} , value : {}", key, value);
//				}
//			}
//		}
//		return info;
//	}
//	
//	public static void main(String[] args) {
//		String value = "_460028573251795_";
//		System.out.println(Arrays.toString(value.split(CommonConstants.UNDERLINE,3)));
//	}
//}