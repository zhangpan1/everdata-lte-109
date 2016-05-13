package com.eversec.lte.processor.backfill.back;
//package com.eversec.lte.processor.backfill;
//
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT_STR;
//
//import java.util.Random;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import redis.clients.jedis.ShardedJedis;
//
//import com.eversec.common.cache.IData;
//import com.eversec.common.constant.CommonConstants;
//import com.eversec.lte.cache.BackFillCache;
//import com.eversec.lte.cache.RuleCache;
//import com.eversec.lte.cache.data.RuleData;
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.utils.JeditClient;
//import com.eversec.lte.vo.backfill.S1mmeFillParam;
//
///**
// * <pre>
// * LTE单接口回填工具</br>
// * <B>注意事项：每个线程创建一个实例</B>
// * </pre>
// * 
// * @author bieremayi
// * 
// */
//public abstract class AbstractBackFill implements IData {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -1508026110656118362L;
//
//	protected static Logger logger = LoggerFactory
//			.getLogger(AbstractBackFill.class);
//
//	public static BackFillCache BACKFILLS;
//
//	protected ShardedJedis resource = JeditClient.SHARDED_JEDIS_POOL
//			.getResource();// 单线程调用
//	/**
//	 * userIpv4_sgwTeid
//	 */
//	public static int RULE0_TTL_SECOND;
//	/**
//	 * mmeS1apID_mmeGroupID_mmeCode
//	 */
//	public static int RULE1_TTL_SECOND;
//	/**
//	 * mmeGroupID_mmeCode_mTmsi
//	 */
//	public static int RULE2_TTL_SECOND;
//	/**
//	 * mmeS1apID_cellID
//	 */
//	public static int RULE4_TTL_SECOND;
//
//	/**
//	 * userIpv4_sgwTeid
//	 */
//	public static AtomicLong RULE0_COUNT = new AtomicLong();
//	/**
//	 * mmeS1apID_mmeGroupID_mmeCode
//	 */
//	public static AtomicLong RULE1_COUNT = new AtomicLong();
//	/**
//	 * mmeGroupID_mmeCode_mTmsi
//	 */
//	public static AtomicLong RULE2_COUNT = new AtomicLong();
//	/**
//	 * mmeS1apID_cellID
//	 */
//	public static AtomicLong RULE4_COUNT = new AtomicLong();
//
//	/**
//	 * imsi->value imei->value
//	 */
//	public static RuleCache PERSISTENT_RULE_CACHE;
//
//	/**
//	 * userIpv4_sgwTeid
//	 */
//	public static RuleCache RULE0_CACHE;
//	/**
//	 * mmeS1apID_mmeGroupID_mmeCode
//	 */
//	public static RuleCache RULE1_CACHE;
//	/**
//	 * mmeGroupID_mmeCode_mTmsi
//	 */
//	public static RuleCache RULE2_CACHE;
//	/**
//	 * mmeS1apID_cellID
//	 */
//	public static RuleCache RULE4_CACHE;
//
//	public static final String EX = "ex";// 单位：秒
//	public static final String NX = "nx";// key不存在，则存储，存在则不存。
//
//	public static enum RuleType {
//		IMSI, IMEI, RULE0, RULE1, RULE2, RULE3, RULE4
//	}
//
//	static {
//		long rule_size = SdtpConfig.getPersistentRuleSize();
//		BACKFILLS = new BackFillCache(1000, "-1", "1d");
//		RULE0_TTL_SECOND = SdtpConfig.getRule0TTLSecond();
//		RULE1_TTL_SECOND = SdtpConfig.getRule1TTLSecond();
//		RULE2_TTL_SECOND = SdtpConfig.getRule2TTLSecond();
//		RULE4_TTL_SECOND = SdtpConfig.getRule4TTLSecond();
//		System.out.println("RULE0_TTL_SECOND : " + RULE0_TTL_SECOND);
//		System.out.println("RULE1_TTL_SECOND : " + RULE1_TTL_SECOND);
//		System.out.println("RULE2_TTL_SECOND : " + RULE2_TTL_SECOND);
//		System.out.println("RULE4_TTL_SECOND : " + RULE4_TTL_SECOND);
//		PERSISTENT_RULE_CACHE = new RuleCache(rule_size, "-1", "-1");
//		RULE0_CACHE = new RuleCache(SdtpConfig.getPersistentRuleSize(),
//				String.valueOf(RULE0_TTL_SECOND * 1000),
//				String.valueOf(RULE0_TTL_SECOND * 1000));
//		RULE1_CACHE = new RuleCache(rule_size,
//				String.valueOf(RULE1_TTL_SECOND * 1000),
//				String.valueOf(RULE1_TTL_SECOND * 1000));
//		RULE2_CACHE = new RuleCache(rule_size,
//				String.valueOf(RULE2_TTL_SECOND * 1000),
//				String.valueOf(RULE2_TTL_SECOND * 1000));
//		RULE4_CACHE = new RuleCache(rule_size,
//				String.valueOf(RULE4_TTL_SECOND * 1000),
//				String.valueOf(RULE4_TTL_SECOND * 1000));
//	}
//
//	public static void report() {
//		logger.info(
//				"persistent_rule : {} , rule0 : {} , rule1 : {} , rule2 : {} ,rule4 : {}",
//				new Object[] { PERSISTENT_RULE_CACHE.size(),
//						RULE0_CACHE.size(), RULE1_CACHE.size(),
//						RULE2_CACHE.size(), RULE4_CACHE.size() });
//	}
//
//	/**
//	 * 初始化
//	 */
//	public static void init() {
//		logger.info("init backfill util !");
//	}
//
//	/**
//	 * 释放redis资源
//	 */
//	public void returnResource() {
//		JeditClient.SHARDED_JEDIS_POOL.returnResource(resource);
//	}
//
//	/**
//	 * 获取存储value字符串
//	 * 
//	 * @param msisdn
//	 * @param imsi
//	 * @param imei
//	 * @return msisdn_imsi_imei
//	 */
//	protected String getValue(String msisdn, String imsi, String imei) {
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(msisdn).append(CommonConstants.UNDERLINE).append(imsi)
//				.append(CommonConstants.UNDERLINE).append(imei);
//		return tmp.toString();
//	}
//
//	/**
//	 * 获取规则0字符串
//	 * 
//	 * @param userIpv4
//	 * @param sgwTeid
//	 * @return userIpv4_sgwTeid
//	 */
//	protected String getRule0(String userIpv4, String sgwTeid) {
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(userIpv4).append(CommonConstants.UNDERLINE).append(sgwTeid);
//		return tmp.toString();
//	}
//
//	/**
//	 * 获取规则1字符串
//	 * 
//	 * @param mmeS1apID
//	 * @param mmeGroupID
//	 * @param mmeCode
//	 * @return mmeS1apID_mmeGroupID_mmeCode
//	 */
//	protected String getRule1(String mmeS1apID, String mmeGroupID,
//			String mmeCode) {
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(mmeS1apID).append(CommonConstants.UNDERLINE)
//				.append(mmeGroupID).append(CommonConstants.UNDERLINE)
//				.append(mmeCode);
//		return tmp.toString();
//	}
//
//	/**
//	 * 获取规则2字符串
//	 * 
//	 * @param mmeGroupID
//	 * @param mmeCode
//	 * @param mTmsi
//	 * @return mmeGroupID_mmeCode_mTmsi
//	 */
//	protected String getRule2(String mmeGroupID, String mmeCode, String mTmsi) {
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(mmeGroupID).append(CommonConstants.UNDERLINE)
//				.append(mmeCode).append(CommonConstants.UNDERLINE)
//				.append(mTmsi);
//		return tmp.toString();
//	}
//
//	/**
//	 * 获取规则3字符串
//	 * 
//	 * @param additionalMmeGroupID
//	 * @param additionalMmeCode
//	 * @param additionalMTmsi
//	 * @return
//	 */
//	protected String getRule3(String additionalMmeGroupID,
//			String additionalMmeCode, String additionalMTmsi) {
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(additionalMmeGroupID).append(CommonConstants.UNDERLINE)
//				.append(additionalMmeCode).append(CommonConstants.UNDERLINE)
//				.append(additionalMTmsi);
//		return tmp.toString();
//	}
//
//	/**
//	 * 获取规则4字符串
//	 * 
//	 * @param mmeS1apID
//	 * @param cellID
//	 * @return mmeS1apID_cellID
//	 */
//	protected String getRule4(String mmeS1apID, String cellID) {
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(mmeS1apID).append(CommonConstants.UNDERLINE).append(cellID);
//		return tmp.toString();
//	}
//
//	/**
//	 * <pre>
//	 * 创建s11回填规则
//	 * 1.imsi->value
//	 * 2.imei->value
//	 * 3.rule0->value
//	 * </pre>
//	 * 
//	 * @param imsi
//	 * @param imei
//	 * @param userIpv4
//	 * @param sgwTeid
//	 * @param value
//	 */
//	protected abstract void buildS11FillRule(String imsi, String imei,
//			String userIpv4, String sgwTeid, String value);
//
//	/**
//	 * <pre>
//	 * 创建s1mme回填规则
//	 *  1.imsi->value
//	 *  2.imei->value
//	 *  3.rule0->value
//	 *  4.rule1->value
//	 *  5.rule2->value
//	 *  6.rule4->value
//	 * </pre>
//	 * 
//	 * @param imsi
//	 * @param imei
//	 * @param userIpv4
//	 * @param sgwTeid
//	 * @param mmeS1apID
//	 * @param mmeGroupID
//	 * @param mmeCode
//	 * @param mTmsi
//	 * @param cellID
//	 * @param value
//	 */
//	protected abstract void buildS1mmeFillRule(String imsi, String imei,
//			String userIpv4, String sgwTeid, String mmeS1apID,
//			String mmeGroupID, String mmeCode, String mTmsi, String cellID,
//			String value);
//
//	/**
//	 * <pre>
//	 * 创建imei/imsi回填规则
//	 *  1.imsi->value
//	 *  2.imei->value
//	 *  3.rule0->value
//	 * </pre>
//	 * 
//	 * @param imsi
//	 * @param imei
//	 * @param value
//	 */
//	protected abstract void buildImsiAndImeiFillRule(String imsi, String imei,
//			String value);
//
//	/**
//	 * 建立userIpv4_sgwTeid与回填信息的映射关系
//	 * 
//	 * @param userIpv4
//	 * @param sgwTeid
//	 * @param value
//	 */
//	protected abstract void buildRule0(String userIpv4, String sgwTeid,
//			String value);
//
//	/**
//	 * 建立userIpv4_sgwTeid与回填信息的映射关系
//	 * 
//	 * @param rule0
//	 * @param value
//	 */
//	protected abstract void buildRule0(String rule0, String value);
//
//	/**
//	 * 建立mmeS1apID_mmeGroupID_mmeCode与回填信息的映射关系
//	 * 
//	 * @param mmeS1apID
//	 * @param mmeGroupID
//	 * @param mmeCode
//	 * @param value
//	 */
//	protected abstract void buildRule1(String mmeS1apID, String mmeGroupID,
//			String mmeCode, String value);
//
//	/**
//	 * 建立mmeS1apID_mmeGroupID_mmeCode与回填信息的映射关系
//	 * 
//	 * @param rule1
//	 * @param value
//	 */
//	protected abstract void buildRule1(String rule1, String value);
//
//	/**
//	 * 建立mmeGroupID_mmeCode_mTmsi与回填信息的映射关系
//	 * 
//	 * @param mmeGroupID
//	 * @param mmeCode
//	 * @param mTmsi
//	 * @param value
//	 */
//	protected abstract void buildRule2(String mmeGroupID, String mmeCode,
//			String mTmsi, String value);
//
//	/**
//	 * 建立mmeGroupID_mmeCode_mTmsi与回填信息的映射关系
//	 * 
//	 * @param rule2
//	 * @param value
//	 */
//	protected abstract void buildRule2(String rule2, String value);
//
//	/**
//	 * 
//	 * @param additionalMmeGroupID
//	 * @param additionalMmeCode
//	 * @param additionalMTmsi
//	 * @param value
//	 */
//	protected abstract void buildRule3(String additionalMmeGroupID,
//			String additionalMmeCode, String additionalMTmsi, String value);
//
//	/**
//	 * 建立mmeS1apID_cellID与回填信息的映射关系(用于UEMR回填)
//	 * 
//	 * @param mmeS1apID
//	 * @param cellID
//	 * @param value
//	 */
//	protected abstract void buildRule4(String mmeS1apID, String cellID,
//			String value);
//
//	/**
//	 * 建立mmeS1apID_cellID与回填信息的映射关系(用于UEMR回填)
//	 * 
//	 * @param rule4
//	 * @param value
//	 */
//	protected abstract void buildRule4(String rule4, String value);
//
//	/**
//	 * 
//	 * @param userIpv4
//	 * @param sgwTeid
//	 * @return
//	 */
//	public BackfillInfo getInfoByRule0(String userIpv4, String sgwTeid) {
//		BackfillInfo fillInfo = null;
//		if (isNotBlank(userIpv4, sgwTeid)) {
//			fillInfo = getInfo(getRule0(userIpv4, sgwTeid), RuleType.RULE0);
//		}
//		return fillInfo;
//	}
//
//	/**
//	 * 通过mmeS1apID_mmeGroupID_mmeCode查询回填信息
//	 * 
//	 * @param mmeS1apID
//	 * @param mmeGroupID
//	 * @param mmeCode
//	 * @return
//	 */
//	public BackfillInfo getInfoByRule1(String mmeS1apID, String mmeGroupID,
//			String mmeCode) {
//		BackfillInfo fillInfo = null;
//		if (isNotBlank(mmeS1apID, mmeGroupID, mmeCode)
//				&& !mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)) {
//			fillInfo = getInfo(getRule1(mmeS1apID, mmeGroupID, mmeCode),
//					RuleType.RULE1);
//		}
//		return fillInfo;
//	}
//
//	/**
//	 * 通过mmeGroupID_mmeCode_mTmsi(guti)查询回填信息
//	 * 
//	 * @param mmeGroupID
//	 * @param mmeCode
//	 * @param mTmsi
//	 * @return
//	 */
//	public BackfillInfo getInfoByRule2(String mmeGroupID, String mmeCode,
//			String mTmsi) {
//		BackfillInfo fillInfo = null;
//		if (isNotBlank(mmeGroupID, mmeCode, mTmsi)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE)
//				&& !mTmsi.equals(MAX_UNSIGNED_INT)) {
//			fillInfo = getInfo(getRule2(mmeGroupID, mmeCode, mTmsi),
//					RuleType.RULE2);
//		}
//		return fillInfo;
//	}
//
//	/**
//	 * 通过additionalMmeGroupID_additionalMmeCode_additionalMTmsi(guti)查询回填信息
//	 * 
//	 * @param additionalMmeGroupID
//	 * @param additionalMmeCode
//	 * @param additionalMTmsi
//	 * @return
//	 */
//	public BackfillInfo getInfoByRule3(String additionalMmeGroupID,
//			String additionalMmeCode, String additionalMTmsi) {
//		return getInfoByRule2(additionalMmeGroupID, additionalMmeCode,
//				additionalMTmsi);
//	}
//
//	/**
//	 * 通过多种方式获取S1MME回填信息
//	 * 
//	 * @param param
//	 * @param isAdditional
//	 * @return
//	 */
//	public BackfillInfo getS1mmeFillInfo(S1mmeFillParam param,
//			boolean isAdditional) {
//		String imsi = param.getImsi(), imei = param.getImei(), userIpv4 = param
//				.getUserIpv4(), sgwTeid = param.getSgwTeid(), mmeS1apID = param
//				.getMmeS1apID(), mmeGroupID = param.getMmeGroupID(), mmeCode = param
//				.getMmeCode(), mTmsi = param.getmTmsi();
//		String additionalMmeGroupID = param.getAdditionalMmeGroupID();
//		String additionalMmeCode = param.getAdditionalMmeCode();
//		String additionalMTmsi = param.getAdditionalMTmsi();
//		BackfillInfo info = null;
//		// 1.通过imsi获取回填信息
//		if ((info = getInfo(imsi, RuleType.IMSI)) == null) {
//			// 2.通过imei获取回填信息
//			if ((info = getInfo(imei, RuleType.IMEI)) == null) {
//				// 3.通过mmeS1apID_mmeGroupID_mmeCode获取回填信息
//				if ((info = getInfoByRule1(mmeS1apID, mmeGroupID, mmeCode)) == null) {
//					// 4.通过mmeGroupID_mmeCode_mTmsi获取回填信息
//					if ((info = getInfoByRule2(mmeGroupID, mmeCode, mTmsi)) == null) {
//						// 5.通过userIpv4_sgwTeid获取回填信息
//						if ((info = getInfoByRule0(userIpv4, sgwTeid)) == null) {
//							// 6.通过additionalMmeGroupID_additionalMmeCode_additionalMTmsi获取回填信息
//							if (isAdditional
//									&& (info = getInfoByRule3(
//											additionalMmeGroupID,
//											additionalMmeCode, additionalMTmsi)) != null) {
//								info.fillType = 6;// additionalMmeGroupID_additionalMmeCode_additionalMTmsi
//							}
//						} else {
//							info.fillType = 5;// userIpv4_sgwTeid
//						}
//					} else {
//						info.fillType = 4;// mmeGroupID_mmeCode_mTmsi
//					}
//				} else {
//					info.fillType = 3;// mmeS1apID_mmeGroupID_mmeCode回填
//				}
//			} else {
//				info.fillType = 2;// imei回填
//			}
//		} else {
//			info.fillType = 1;// imsi回填
//		}
//		return info;
//	}
//
//	/**
//	 * 获取回填信息，msisdn_imsi_imei
//	 * 
//	 * @param key
//	 * @return
//	 */
//	public abstract BackfillInfo getInfo(String key, RuleType type);
//
//	// 回填信息
//	public static class BackfillInfo {
//		public String msisdn, imsi, imei;
//		public int fillType;
//
//		public BackfillInfo(String msisdn, String imsi, String imei) {
//			super();
//			this.msisdn = msisdn;
//			this.imsi = imsi;
//			this.imei = imei;
//		}
//	}
//
//	public static boolean isNotBlank(String... arrs) {
//		for (String str : arrs) {
//			if (StringUtils.isBlank(str))
//				return false;
//		}
//		return true;
//	}
//
//	@Override
//	public int getMemoryBytes() {
//		return 0;
//	}
//
//	public static void main(String[] args) {
//		for (int i = 0; i < 10000; i++) {
//			RULE4_CACHE.update("test" + new Random().nextLong(), new RuleData(
//					""));
//			RULE4_CACHE.update("test" + 5566, new RuleData(""));
//		}
//		System.out.println(RULE4_CACHE.size());
//		final long start = System.currentTimeMillis();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					System.out.println(RULE4_CACHE.size());
//					System.out.println("cost : "
//							+ (System.currentTimeMillis() - start) / 1000);
//					RULE4_CACHE.getIfPresent("test" + 5566);
//					try {
//						RULE4_CACHE.get("test" + 5566);
//					} catch (ExecutionException e1) {
//					}
//					System.out.println(RULE4_CACHE.stats());
//					RULE4_CACHE.cleanUp();
//					try {
//						TimeUnit.SECONDS.sleep(2);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//	}
//}
