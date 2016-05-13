package com.eversec.lte.processor.backfill.back;
//package com.eversec.lte.processor.backfill;
//
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_BYTE_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_INT_STR;
//import static com.eversec.lte.constant.SdtpConstants.MAX_UNSIGNED_SHORT_STR;
//import redis.clients.jedis.ShardedJedisPipeline;
//
//import com.eversec.common.constant.CommonConstants;
//
///**
// * 直接使用redis作为缓存实现
// * 
// * @author bieremayi
// * 
// */
//@SuppressWarnings("serial")
//public class BackFillWithRedis extends AbstractBackFill {
//
//	@Override
//	protected void buildS11FillRule(String imsi, String imei, String userIpv4,
//			String sgwTeid, String value) {
//		ShardedJedisPipeline pipeline = resource.pipelined();
//		pipeline.set(imsi, value);
//		pipeline.set(imei, value);
//		if (isNotBlank(userIpv4, sgwTeid)) {
//			pipeline.set(getRule0(userIpv4, sgwTeid), value, NX, EX,
//					RULE0_TTL_SECOND);
////			RULE0_COUNT.incrementAndGet();
//		}
//		pipeline.sync();
//	}
//
//	@Override
//	protected void buildS1mmeFillRule(String imsi, String imei,
//			String userIpv4, String sgwTeid, String mmeS1apID,
//			String mmeGroupID, String mmeCode, String mTmsi, String cellID,
//			String value) {
//		ShardedJedisPipeline pipeline = resource.pipelined();
//		pipeline.set(imsi, value);
//		pipeline.set(imei, value);
//		if (isNotBlank(userIpv4, sgwTeid)) {
//			pipeline.set(getRule0(userIpv4, sgwTeid), value, NX, EX,
//					RULE0_TTL_SECOND);
////			RULE0_COUNT.incrementAndGet();
//		}
//		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)
//				&& !mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)) {
//			pipeline.set(getRule1(mmeS1apID, mmeGroupID, mmeCode), value, NX,
//					EX, RULE1_TTL_SECOND);
////			RULE1_COUNT.incrementAndGet();
//		}
//		if (!mmeGroupID.equals(MAX_UNSIGNED_SHORT_STR)
//				&& !mmeCode.equals(MAX_UNSIGNED_BYTE_STR)
//				&& !mTmsi.equals(MAX_UNSIGNED_INT_STR)) {
//			pipeline.set(getRule2(mmeGroupID, mmeCode, mTmsi), value, NX, EX,
//					RULE2_TTL_SECOND);
////			RULE2_COUNT.incrementAndGet();
//		}
//		if (!mmeS1apID.equals(MAX_UNSIGNED_INT_STR)) {
//			pipeline.set(getRule4(mmeS1apID, cellID), value, NX, EX,
//					RULE4_TTL_SECOND);
////			RULE4_COUNT.incrementAndGet();
//		}
//		pipeline.sync();
//	}
//
//	@Override
//	protected void buildImsiAndImeiFillRule(String imsi, String imei,
//			String value) {
//		ShardedJedisPipeline pipeline = resource.pipelined();
//		pipeline.set(imsi, value);
//		pipeline.set(imei, value);
//		pipeline.sync();
//	}
//
//	@Override
//	protected void buildRule0(String rule0, String value) {
//		if (isNotBlank(rule0, value)) {
//			resource.set(rule0, value, NX, EX, RULE0_TTL_SECOND);
////			RULE0_COUNT.incrementAndGet();
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
//			resource.set(rule1, value, NX, EX, RULE1_TTL_SECOND);
////			RULE1_COUNT.incrementAndGet();
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
//			resource.set(rule2, value, NX, EX, RULE2_TTL_SECOND);
////			RULE2_COUNT.incrementAndGet();
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
//			resource.set(rule4, value, NX, EX, RULE4_TTL_SECOND);
////			RULE4_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	public BackfillInfo getInfo(String key, RuleType type) {
//		BackfillInfo info = null;
//		if (isNotBlank(key)) {
//			String value = resource.get(key);
//			if (value != null) {
//				if (value.equals("OK")) {
//					resource.resetState();
//					value = resource.get(key);
//				}
//				if (value != null && !value.equals("null")
//						&& !value.equals("OK")) {
//					String[] id = value.split(CommonConstants.UNDERLINE);
//					if (id.length >= 3) {
//						info = new BackfillInfo(id[0], id[1], id[2]);
//					} else {
//						logger.warn("error key : {} , value : {}", key, value);
//					}
//				}
//			}
//		}
//		return info;
//	}
//}
