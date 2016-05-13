package com.eversec.lte.processor.backfill.back;
//package com.eversec.lte.processor.backfill;
//
///**
// * 直接使用redis作为缓存实现
// * 
// * @author bieremayi
// * 
// */
//@SuppressWarnings("serial")
//public class CmBackFillWithRedis extends AbstractCmBackFill {
//
//	BackFillWithRedis backFillWithRedis = new BackFillWithRedis();
//
//	@Override
//	protected void buildS11FillRule(String imsi, String imei, String userIpv4,
//			String sgwTeid, String value) {
//		backFillWithRedis
//				.buildS11FillRule(imsi, imei, userIpv4, sgwTeid, value);
//	}
//
//	@Override
//	protected void buildS1mmeFillRule(String imsi, String imei,
//			String userIpv4, String sgwTeid, String mmeS1apID,
//			String mmeGroupID, String mmeCode, String mTmsi, String cellID,
//			String value) {
//		backFillWithRedis.buildS1mmeFillRule(imsi, imei, userIpv4, sgwTeid,
//				mmeS1apID, mmeGroupID, mmeCode, mTmsi, cellID, value);
//	}
//
//	@Override
//	protected void buildImsiAndImeiFillRule(String imsi, String imei,
//			String value) {
//		backFillWithRedis.buildImsiAndImeiFillRule(imsi, imei, value);
//	}
//
//	@Override
//	protected void buildRule0(String rule0, String value) {
//		backFillWithRedis.buildRule0(rule0, value);
//	}
//
//	@Override
//	public void buildRule0(String userIpv4, String sgwTeid, String value) {
//		backFillWithRedis.buildRule0(userIpv4, sgwTeid, value);
//	}
//
//	@Override
//	protected void buildRule1(String rule1, String value) {
//		backFillWithRedis.buildRule1(rule1, value);
//	}
//
//	@Override
//	public void buildRule1(String mmeS1apID, String mmeGroupID, String mmeCode,
//			String value) {
//		backFillWithRedis.buildRule1(mmeS1apID, mmeGroupID, mmeCode, value);
//	}
//
//	@Override
//	protected void buildRule2(String rule2, String value) {
//		backFillWithRedis.buildRule2(rule2, value);
//	}
//
//	@Override
//	public void buildRule2(String mmeGroupID, String mmeCode, String mTmsi,
//			String value) {
//		backFillWithRedis.buildRule2(mmeGroupID, mmeCode, mTmsi, value);
//	}
//
//	@Override
//	protected void buildRule3(String additionalMmeGroupID,
//			String additionalMmeCode, String additionalMTmsi, String value) {
//		backFillWithRedis.buildRule3(additionalMmeGroupID, additionalMmeCode,
//				additionalMTmsi, value);
//	}
//
//	@Override
//	protected void buildRule4(String mmeS1apID, String cellID, String value) {
//		backFillWithRedis.buildRule4(mmeS1apID, cellID, value);
//
//	}
//
//	@Override
//	protected void buildRule4(String rule4, String value) {
//		backFillWithRedis.buildRule4(rule4, value);
//	}
//
//	@Override
//	public BackfillInfo getInfo(String key, RuleType type) {
//		return backFillWithRedis.getInfo(key, type);
//	}
//
//}
