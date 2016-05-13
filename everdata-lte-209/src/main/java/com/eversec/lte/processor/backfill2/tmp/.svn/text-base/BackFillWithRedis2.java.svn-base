package com.eversec.lte.processor.backfill2.tmp;
//package com.eversec.lte.processor.backfill2;
//
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT_STR;
//
//import java.util.HashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.common.constant.CommonConstants;
//import com.eversec.lte.cache.data.RuleData;
//import com.eversec.lte.sdtp.redis.JedisValueGetHandler;
//
///**
// * 
// */
//@SuppressWarnings("serial")
//public class BackFillWithRedis2 extends AbstractCmBackFill2 {
//
//	private static Logger LOGGER = LoggerFactory
//			.getLogger(BackFillWithRedis2.class);
//
//	private static BackFillWithRedis2 instance = new BackFillWithRedis2();
//
//	public static BackFillWithRedis2 getInstance() {
//		return instance;
//	}
//
//	private BackFillWithRedis2() {
//	}
//
//	private static AtomicLong HIT = new AtomicLong(0);
//	private static AtomicLong LOST = new AtomicLong(0);
//	
//	public static AtomicLong SET_COUNT = new AtomicLong(0);
//	public static AtomicLong GET_COUNT = new AtomicLong(0);
//	
//	public static void statSet(long period) {
//		String str = "BackFillWithRedis2 :";
//		str += ",";
//		str += "set : " + (SET_COUNT.get() / period) + "/s";
//		str += ",";
//		str += "get : " + (GET_COUNT.get() / period) + "/s";
//		SET_COUNT.set(0);
//		GET_COUNT.set(0);
//		LOGGER.info(str);
//	}
//
//	static{
////		JedisTools.init();
//	}
////	public static JedisTools redisSet = new JedisTools(true, false);
////	public static JedisTools redisGet = new JedisTools(false, true);
//
//	static HashMap<RuleType, BackFillValueGetHandler> getHandlerMap = new HashMap<>();
//
//	private static boolean test =true;
//	static{
//		getHandlerMap.put(RuleType.IMSI, new BackFillValueGetHandler(
//				RuleType.IMSI));
//		getHandlerMap.put(RuleType.IMEI, new BackFillValueGetHandler(
//				RuleType.IMEI));
//		getHandlerMap.put(RuleType.RULE0, new BackFillValueGetHandler(
//				RuleType.RULE0));
//		getHandlerMap.put(RuleType.RULE1, new BackFillValueGetHandler(
//				RuleType.RULE1));
//		getHandlerMap.put(RuleType.RULE2, new BackFillValueGetHandler(
//				RuleType.RULE2));
//		getHandlerMap.put(RuleType.RULE3, new BackFillValueGetHandler(
//				RuleType.RULE3));
//		getHandlerMap.put(RuleType.RULE4, new BackFillValueGetHandler(
//				RuleType.RULE4));
//	}
//	
//	public static void report3() {
//		LOGGER.info(
//				"BackFillWithRedis2 persistent_rule : {} , rule0 : {} , rule1 : {} , rule2 : {} ,rule4 : {}",
//				new Object[] { PERSISTENT_RULE_CACHE.size(),
//						RULE0_CACHE.size(), RULE1_CACHE.size(),
//						RULE2_CACHE.size(), RULE4_CACHE.size() });
//	}
//
//	public static void report() {
//		LOGGER.info("BackFillWithRedis2: hit:"
//				+ HIT.get() + ",lost:" + LOST.get() + ",hit_count:"
//				+ (HIT.get() * 1.0d / (HIT.get() + LOST.get())));
//	}
//	
//	public void set(RuleType type, String key, String value) {
////		redisSet.set(key, value);
//		if(value != null){
//			setWithOutRedis(type,key,value);
//		}
//	}
//
//	private static void setWithOutRedis(RuleType type, String key, String value) {
//		if(test ){
//			return;
//		}
//		switch (type) {
//		case IMSI:
//			PERSISTENT_RULE_CACHE.update(key, new RuleData2(value));
//			break;
//		case IMEI:
//			PERSISTENT_RULE_CACHE.update(key, new RuleData2(value));
//			break;
//		case RULE0:
//			RULE0_CACHE.update(key, new RuleData2(value));
//			break;
//		case RULE1:
//			RULE1_CACHE.update(key, new RuleData2(value));
//			break;
//		case RULE2:
//			RULE2_CACHE.update(key, new RuleData2(value));
//			break;
//		case RULE4:
//			RULE4_CACHE.update(key, new RuleData2(value));
//			break;
//		default:
//			break;
//		}
//		
//		SET_COUNT.incrementAndGet();
//		
//	}
//
//	public void set(RuleType type, String key, String value, int ttl) {
////		redisSet.set(key, value, ttl);
//		setWithOutRedis(type,key,value);
//	}
//
//	private RuleData2 get(RuleType type, String key) {
////		redisGet.get(key, getHandlerMap.get(type));
//		RuleData2 result = getWithOutRedis(type,key);
//		return result;
//	}
//	
//	private static RuleData2 getWithOutRedis(RuleType type, String key ) {
//		RuleData2 result = null;
//		if(test){
//			return null;
//		}
//		switch (type) {
//		case IMSI:
//			result = PERSISTENT_RULE_CACHE.getIfPresent(key); 
//			break;
//		case IMEI:
//			result = PERSISTENT_RULE_CACHE.getIfPresent(key); 
//			break;
//		case RULE0:
//			result =RULE0_CACHE.getIfPresent(key); 
//			break;
//		case RULE1:
//			result =RULE1_CACHE.getIfPresent(key); 
//			break;
//		case RULE2:
//			result =RULE2_CACHE.getIfPresent(key); 
//			break;
//		case RULE4:
//			result =RULE4_CACHE.getIfPresent(key); 
//			break;
//		default:
//			break;
//		}
//		
//		GET_COUNT.incrementAndGet();
//		return result;
//		
//	}
//
//	@Override
//	protected void buildS11FillRule(String imsi, String imei, String userIpv4,
//			String sgwTeid, String value) {
//		set(RuleType.IMEI, imsi, value);
//		set(RuleType.IMEI, imei, value);
//		if (isNotBlank(userIpv4, sgwTeid)) {
//			set(RuleType.RULE0, getRule0(userIpv4, sgwTeid), value,
//					RULE0_TTL_SECOND);
//			RULE0_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	protected void buildS1mmeFillRule(String imsi, String imei,
//			String userIpv4, String sgwTeid, String mmeS1apID,
//			String mmeGroupID, String mmeCode, String mTmsi, String cellID,
//			String value) {
//		set(RuleType.IMEI, imsi, value);
//		set(RuleType.IMEI, imei, value);
//		if (isNotBlank(userIpv4, sgwTeid)) {
//			set(RuleType.RULE0, getRule0(userIpv4, sgwTeid), value,
//					RULE0_TTL_SECOND);
//			RULE0_COUNT.incrementAndGet();
//		}
//		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)) {
//			set(RuleType.RULE1, getRule1(mmeS1apID, mmeGroupID, mmeCode),
//					value, RULE1_TTL_SECOND);
//			RULE1_COUNT.incrementAndGet();
//		}
//		if (!mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)
//				&& !mTmsi.equals(MAX_UNSIGNED_INT_STR)) {
//			set(RuleType.RULE2, getRule2(mmeGroupID, mmeCode, mTmsi), value,
//					RULE2_TTL_SECOND);
//			RULE2_COUNT.incrementAndGet();
//		}
//		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)) {
//			set(RuleType.RULE4, getRule4(mmeS1apID, cellID), value,
//					RULE4_TTL_SECOND);
//			RULE4_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	protected void buildImsiAndImeiFillRule(String imsi, String imei,
//			String value) {
//		set(RuleType.IMEI, imsi, value);
//		set(RuleType.IMEI, imei, value);
//	}
//
//	@Override
//	protected void buildRule0(String rule0, String value) {
//		if (isNotBlank(rule0, value)) {
//			set(RuleType.RULE0, rule0, value, RULE0_TTL_SECOND);
//			RULE0_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	public void buildRule0(String userIpv4, String sgwTeid, String value) {
//		if (isNotBlank(userIpv4, sgwTeid, value)) {
//			buildRule0(getRule0(userIpv4, sgwTeid), value);
//		}
//	}
//
//	@Override
//	protected void buildRule1(String rule1, String value) {
//		if (isNotBlank(rule1, value)) {
//			set(RuleType.RULE1, rule1, value, RULE1_TTL_SECOND);
//			RULE1_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	public void buildRule1(String mmeS1apID, String mmeGroupID, String mmeCode,
//			String value) {
//		if (isNotBlank(mmeS1apID, mmeGroupID, mmeCode, value)
//				&& !mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_BYTE_STR)) {
//			buildRule1(getRule1(mmeS1apID, mmeGroupID, mmeCode), value);
//		}
//	}
//
//	@Override
//	protected void buildRule2(String rule2, String value) {
//		if (isNotBlank(rule2, value)) {
//			set(RuleType.RULE2, rule2, value, RULE2_TTL_SECOND);
//			RULE2_COUNT.incrementAndGet();
//		}
//
//	}
//
//	@Override
//	public void buildRule2(String mmeGroupID, String mmeCode, String mTmsi,
//			String value) {
//		if (isNotBlank(mmeGroupID, mmeCode, mTmsi, value)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)
//				&& !mTmsi.equals(MAX_UNSIGNED_INT_STR)) {
//			buildRule2(getRule2(mmeGroupID, mmeCode, mTmsi), value);
//		}
//	}
//
//	@Override
//	protected void buildRule3(String additionalMmeGroupID,
//			String additionalMmeCode, String additionalMTmsi, String value) {
//		buildRule2(additionalMmeGroupID, additionalMmeCode, additionalMTmsi,
//				value);
//	}
//
//	@Override
//	protected void buildRule4(String mmeS1apID, String cellID, String value) {
//		if (isNotBlank(mmeS1apID, cellID, value)
//				&& !mmeS1apID.equals(MAX_UNSIGNED_INT_STR)) {
//			buildRule4(getRule4(mmeS1apID, cellID), value);
//		}
//
//	}
//
//	@Override
//	protected void buildRule4(String rule4, String value) {
//		if (isNotBlank(rule4, value)) {
//			set(RuleType.RULE4, rule4, value, RULE4_TTL_SECOND);
//			RULE4_COUNT.incrementAndGet();
//		}
//	}
//
//	public static class BackFillValueGetHandler implements JedisValueGetHandler {
//		RuleType type;
//
//		public BackFillValueGetHandler(RuleType type) {
//			super();
//			this.type = type;
//		}
//
//		@Override
//		public void onValueReturn(String key, String value) {
//			setWithOutRedis(type,key,value);
//		}
//
//	}
//
//	@Override
//	public BackfillInfo getInfo(String key, RuleType type) {
//		BackfillInfo info = null;
//		try {
//			if (isNotBlank(key)) {
//				// RULE_TYPE_COUNT.get(type).add(key);
//				RuleData2 rule = get(type, key);
//
//				if (rule == null) {
//					LOST.incrementAndGet();
//				} else {
//					String value = rule.getRule();
//					// redisGet.get(key );
//					if (value != null) {
//						if (value.equals("OK")) {
//							LOST.incrementAndGet();
//						}
//						if (value != null && !value.equals("null")
//								&& !value.equals("OK")) {
//							String[] id = value
//									.split(CommonConstants.UNDERLINE);
//							if (id.length >= 3) {
//								info = new BackfillInfo(id[0], id[1], id[2]);
//							} else {
////								 logger.warn("error key : {} , value : {}", key, value);
//							}
//
//							HIT.incrementAndGet();
//						}
//					}
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return info;
//	}
//
//	public static void main(String[] args) {
//		// GET_BACK_CACHE.update("1", new RuleData("2"));
//		// System.out.println(GET_BACK_CACHE.getIfPresent("1"));
//		// String value = "8618204107799_460026041013191_";
//		// String[] id = value.split(CommonConstants.UNDERLINE);
//		// System.out.println(id.length);
//	}
//}
