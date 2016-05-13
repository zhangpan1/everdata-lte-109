package com.eversec.lte.processor.backfill;

import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE_STR;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT_STR;
import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT_STR;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.common.constant.CommonConstants;
import com.eversec.lte.cache.ExternalCache;
import com.eversec.lte.cache.ValueGetHandler;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.kafka.KafkaRuleTools;
//import com.eversec.lte.cache.data.RuleData;
import com.eversec.lte.sdtp.redis.JedisTools;

/**
 * 
 */
@SuppressWarnings("serial")
public class BackFillWithExternalCache extends AbstractBackFill {

	private static Logger LOGGER = LoggerFactory
			.getLogger(BackFillWithExternalCache.class);

	private static BackFillWithExternalCache instance = new BackFillWithExternalCache();

	public static BackFillWithExternalCache getInstance() {
		return instance;
	}

	private boolean misdnCache = true;

	private BackFillWithExternalCache() {
	}

	private static AtomicLong HIT = new AtomicLong(0);
	private static AtomicLong LOST = new AtomicLong(0);

	public static AtomicLong SET_COUNT = new AtomicLong(0);
	public static AtomicLong GET_COUNT = new AtomicLong(0);

	public static AtomicLong SET_RULEX_COUNT = new AtomicLong(0);
	public static AtomicLong GET_RULEX_COUNT = new AtomicLong(0);
	public static AtomicLong SET_RULEX_KAFKA_COUNT = new AtomicLong(0);

	public static JedisTools redisSet = null;
	// public static JedisTools redisGet = null;

	public static boolean useExternalCache = SdtpConfig.isUseExternalCache();

	// public static InfinispanCache cache = null;

	public static ExternalCache cache;

	// public static KafkaRuleTools rulex = null;

	static {
		if (useExternalCache) {
			redisSet = new JedisTools(true, false);
			// redisGet = new JedisTools(false, true);

			// cache = InfinispanCache.getInstance();
			cache = new KafkaRuleTools(new BackFillValueGetKafkaHandler());
		}
	}

	static HashMap<RuleType, BackFillValueGetHandler> getHandlerMap = new HashMap<>();
	static {
		getHandlerMap.put(RuleType.IMSI, new BackFillValueGetHandler(
				RuleType.IMSI));
		getHandlerMap.put(RuleType.IMEI, new BackFillValueGetHandler(
				RuleType.IMEI));
		getHandlerMap.put(RuleType.RULE0, new BackFillValueGetHandler(
				RuleType.RULE0));
		getHandlerMap.put(RuleType.RULE1, new BackFillValueGetHandler(
				RuleType.RULE1));
		getHandlerMap.put(RuleType.RULE2, new BackFillValueGetHandler(
				RuleType.RULE2));
		getHandlerMap.put(RuleType.RULE3, new BackFillValueGetHandler(
				RuleType.RULE3));
		getHandlerMap.put(RuleType.RULE4, new BackFillValueGetHandler(
				RuleType.RULE4));
	}

	private static BatchHashMap batchHashMap = new BatchHashMap();

	public static BatchWeakHashMap batchWeakHashMap = new BatchWeakHashMap();

	public static void report() {
		LOGGER.info("ruleSize: " + batchHashMap.size() + ",ruleSizeX:"
				+ batchWeakHashMap.size() + ",hit:" + HIT.get() + ",lost:"
				+ LOST.get() + ",hit_count:"
				+ (HIT.get() * 1.0d / (HIT.get() + LOST.get())));

	}

	public static void statSet(long period) {
		String str = "ruleSetGet: ";
		str += "set : " + (SET_COUNT.getAndSet(0)  / period) + "/s";
		str += ",";
		str += "get : " + (GET_COUNT.getAndSet(0)  / period) + "/s";
		str += ",";
		str += "setRuleX : " + (SET_RULEX_COUNT.getAndSet(0)  / period) + "/s";
		str += ",";
		str += "getRuleX : " + (GET_RULEX_COUNT.getAndSet(0)  / period) + "/s";
		str += ",";
		str += "setRuleXKafKa : " + (SET_RULEX_KAFKA_COUNT.getAndSet(0)  / period)
				+ "/s";
		LOGGER.info(str);

		if (useExternalCache) {
			cache.log(period);
		}
	}

	public void set(RuleType type, String key, String value) {
		if (type == RuleType.IMSI || type == RuleType.IMEI) {
			if (!value.equals(batchHashMap.get(key))) {
				batchHashMap.put(key, value);
				if (useExternalCache) {
					// redisSet.set(key, value);
					if (misdnCache) {
						msisdnCache(value);
					}
					// cache.put(key, value);
					cache.set(type.name(), key, value);
				}
				SET_COUNT.incrementAndGet();
			}
		} else {
			if (!value.equals(batchWeakHashMap.get(key))) {
				batchWeakHashMap.put(key, value);

				cache.set("other", key, value);
				SET_RULEX_COUNT.incrementAndGet();
			}
		}
	}

	private void msisdnCache(String value) {
		String[] id = value.split(CommonConstants.UNDERLINE);
		if (id.length >= 3) {
			String msisdn = id[0];
			redisSet.set(RuleType.IMEI.name(),msisdn, value);
		}

	}

	public void set(RuleType type, String key, String value, int ttl) {
		if (type == RuleType.IMSI || type == RuleType.IMEI) {
			if (!value.equals(batchHashMap.get(key))) {
				batchHashMap.put(key, value);
				if (useExternalCache) {
					// redisSet.set(key, value, ttl);
					// cache.put(key, value, ttl);
					cache.set(type.name(), key, value);
				}
				SET_COUNT.incrementAndGet();
			}
		} else {
			if (!value.equals(batchWeakHashMap.get(key))) {
				batchWeakHashMap.put(key, value);
				cache.set("other", key, value);

				SET_RULEX_COUNT.incrementAndGet();
			}
		}
	}

	private String get(RuleType type, String key) {

		if (type == RuleType.IMSI || type == RuleType.IMEI) {
			String wk = batchHashMap.get(key);
			if (wk != null) {
				String result = wk;
				return result;
			} else {
				if (useExternalCache) {
					// redisGet.get(key, getHandlerMap.get(type));
					// cache.getAsync(key, getHandlerMap.get(type));
				}
				GET_COUNT.incrementAndGet();
			}
			return null;
		} else {
			String result = batchWeakHashMap.get(key);
			if (result != null) {
				GET_RULEX_COUNT.incrementAndGet();
			}
			return result;
			// return null;
		}
	}

	@Override
	protected void buildS11FillRule(String imsi, String imei, String userIpv4,
			String sgwTeid, String value) {
		set(RuleType.IMSI, imsi, value);
		set(RuleType.IMEI, imei, value);
		if (isNotBlank(userIpv4, sgwTeid)) {
			set(RuleType.RULE0, getRule0(userIpv4, sgwTeid), value,
					RULE0_TTL_SECOND);
			RULE0_COUNT.incrementAndGet();
		}
	}

	@Override
	protected void buildS1mmeFillRule(String imsi, String imei,
			String userIpv4, String sgwTeid, String mmeS1apID,
			String mmeGroupID, String mmeCode, String mTmsi, String cellID,
			String value) {
		set(RuleType.IMSI, imsi, value);
		set(RuleType.IMEI, imei, value);
		if (isNotBlank(userIpv4, sgwTeid)) {
			set(RuleType.RULE0, getRule0(userIpv4, sgwTeid), value,
					RULE0_TTL_SECOND);
			RULE0_COUNT.incrementAndGet();
		}
		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)) {
			set(RuleType.RULE1, getRule1(mmeS1apID, mmeGroupID, mmeCode),
					value, RULE1_TTL_SECOND);
			RULE1_COUNT.incrementAndGet();
		}
		if (!mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)
				&& !mTmsi.equals(MAX_UNSIGNED_INT_STR)) {
			set(RuleType.RULE2, getRule2(mmeGroupID, mmeCode, mTmsi), value,
					RULE2_TTL_SECOND);
			RULE2_COUNT.incrementAndGet();
		}
		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)) {
			set(RuleType.RULE4, getRule4(mmeS1apID, cellID), value,
					RULE4_TTL_SECOND);
			RULE4_COUNT.incrementAndGet();
		}
	}

	@Override
	protected void buildImsiAndImeiFillRule(String imsi, String imei,
			String value) {
		set(RuleType.IMSI, imsi, value);
		set(RuleType.IMEI, imei, value);
	}

	@Override
	protected void buildRule0(String rule0, String value) {
		if (isNotBlank(rule0, value)) {
			set(RuleType.RULE0, rule0, value, RULE0_TTL_SECOND);
			RULE0_COUNT.incrementAndGet();
		}
	}

	@Override
	public void buildRule0(String userIpv4, String sgwTeid, String value) {
		if (isNotBlank(userIpv4, sgwTeid, value)) {
			buildRule0(getRule0(userIpv4, sgwTeid), value);
		}
	}

	@Override
	protected void buildRule1(String rule1, String value) {
		if (isNotBlank(rule1, value)) {
			set(RuleType.RULE1, rule1, value, RULE1_TTL_SECOND);
			RULE1_COUNT.incrementAndGet();
		}
	}

	@Override
	public void buildRule1(String mmeS1apID, String mmeGroupID, String mmeCode,
			String value) {
		if (isNotBlank(mmeS1apID, mmeGroupID, mmeCode, value)
				&& !mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
				&& !mmeGroupID.equals(MAX_UNSIGNED_BYTE_STR)) {
			buildRule1(getRule1(mmeS1apID, mmeGroupID, mmeCode), value);
		}
	}

	@Override
	protected void buildRule2(String rule2, String value) {
		if (isNotBlank(rule2, value)) {
			set(RuleType.RULE2, rule2, value, RULE2_TTL_SECOND);
			RULE2_COUNT.incrementAndGet();
		}

	}

	@Override
	public void buildRule2(String mmeGroupID, String mmeCode, String mTmsi,
			String value) {
		if (isNotBlank(mmeGroupID, mmeCode, mTmsi, value)
				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)
				&& !mTmsi.equals(MAX_UNSIGNED_INT_STR)) {
			buildRule2(getRule2(mmeGroupID, mmeCode, mTmsi), value);
		}
	}

	@Override
	protected void buildRule3(String additionalMmeGroupID,
			String additionalMmeCode, String additionalMTmsi, String value) {
		buildRule2(additionalMmeGroupID, additionalMmeCode, additionalMTmsi,
				value);
	}

	@Override
	protected void buildRule4(String mmeS1apID, String cellID, String value) {
		if (isNotBlank(mmeS1apID, cellID, value)
				&& !mmeS1apID.equals(MAX_UNSIGNED_INT_STR)) {
			buildRule4(getRule4(mmeS1apID, cellID), value);
		}

	}

	@Override
	protected void buildRule4(String rule4, String value) {
		if (isNotBlank(rule4, value)) {
			set(RuleType.RULE4, rule4, value, RULE4_TTL_SECOND);
			RULE4_COUNT.incrementAndGet();
		}
	}

	public static class BackFillValueGetKafkaHandler implements ValueGetHandler {

		@Override
		public void onValueReturn(String type, String key, String value) {
			if (value != null) {
				if (RuleType.IMSI.name().equals(type)
						|| RuleType.IMEI.name().equals(type)) {
					String[] id = value.split(CommonConstants.UNDERLINE);
					if (id.length >= 3) {
						batchHashMap.put(key, value);
						SET_RULEX_KAFKA_COUNT.incrementAndGet();
					}
				} else {
					batchWeakHashMap.put(key, value);
					SET_RULEX_KAFKA_COUNT.incrementAndGet();
				}
			}

		}
	}

	public static class BackFillValueGetHandler implements ValueGetHandler {

		RuleType type;

		public BackFillValueGetHandler(RuleType type) {
			super();
			this.type = type;
		}

		@Override
		public void onValueReturn(String type, String key, String value) {
			if (value != null) {
				if (RuleType.IMSI == this.type
						|| RuleType.IMEI  == this.type ) {
					String[] id = value.split(CommonConstants.UNDERLINE);
					if (id.length >= 3) {
						batchHashMap.put(key, value);
					}
				}
			}
		}
	}

	@Override
	public XdrBackFillInfo getInfo(String key, RuleType type) {
		XdrBackFillInfo info = null;
		try {
			if (isNotBlank(key)) {
				// RULE_TYPE_COUNT.get(type).add(key);
				String value = get(type, key);

				if (value == null) {
					LOST.incrementAndGet();
				} else {
					// redisGet.get(key );
					if (value != null) {
						if (value.equals("OK")) {
							LOST.incrementAndGet();
						}
						if (value != null && !value.equals("null")
								&& !value.equals("OK")) {
							String[] id = value
									.split(CommonConstants.UNDERLINE);
							if (id.length >= 3) {
								info = new XdrBackFillInfo(id[0], id[1], id[2]);
							} else {
								// logger.warn("error key : {} , value : {}",
								// key, value);
							}

							HIT.incrementAndGet();
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static void main(String[] args) {
		// GET_BACK_CACHE.update("1", new RuleData("2"));
		// System.out.println(GET_BACK_CACHE.getIfPresent("1"));
		// String value = "8618204107799_460026041013191_";
		// String[] id = value.split(CommonConstants.UNDERLINE);
		// System.out.println(id.length);
	}

}
