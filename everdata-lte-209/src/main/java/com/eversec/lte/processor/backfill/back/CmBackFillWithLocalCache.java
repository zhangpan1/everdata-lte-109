package com.eversec.lte.processor.backfill.back;
//package com.eversec.lte.processor.backfill;
//
///**
// * 通过本地缓存获取回填信息，同时可以配置是否将规则保存到redis，以及是否从redis获取回填信息
// * 
// * @author bieremayi
// * 
// */
//@SuppressWarnings("serial")
//public class CmBackFillWithLocalCache extends AbstractCmBackFill {
//
//	BackFillWithLocalCache backFillWithLocalCache = new BackFillWithLocalCache();
//
//	protected void buildImsiRule(final String imsi, final String value) {
//		backFillWithLocalCache.buildImsiRule(imsi, value);
//	}
//
//	protected void buildImeiRule(final String imei, final String value) {
//		backFillWithLocalCache.buildImeiRule(imei, value);
//	}
//
//	@Override
//	protected void buildRule0(final String rule0, final String value) {
//		backFillWithLocalCache.buildRule0(rule0, value);
//	}
//
//	@Override
//	protected void buildRule0(String userIpv4, String sgwTeid, String value) {
//		backFillWithLocalCache.buildRule0(userIpv4, sgwTeid, value);
//	}
//
//	@Override
//	protected void buildRule1(final String rule1, final String value) {
//		backFillWithLocalCache.buildRule1(rule1, value);
//	}
//
//	@Override
//	protected void buildRule1(String mmeS1apID, String mmeGroupID,
//			String mmeCode, final String value) {
//		backFillWithLocalCache
//				.buildRule1(mmeS1apID, mmeGroupID, mmeCode, value);
//	}
//
//	@Override
//	protected void buildRule2(final String rule2, final String value) {
//		backFillWithLocalCache.buildRule2(rule2, value);
//	}
//
//	@Override
//	protected void buildRule2(String mmeGroupID, String mmeCode, String mTmsi,
//			final String value) {
//		backFillWithLocalCache.buildRule2(mmeGroupID, mmeCode, mTmsi, value);
//	}
//
//	@Override
//	protected void buildRule3(String additionalMmeGroupID,
//			String additionalMmeCode, String additionalMTmsi, String value) {
//		backFillWithLocalCache.buildRule3(additionalMmeGroupID,
//				additionalMmeCode, additionalMTmsi, value);
//	}
//
//	@Override
//	protected void buildS11FillRule(final String imsi, final String imei,
//			String userIpv4, String sgwTeid, final String value) {
//		backFillWithLocalCache.buildS11FillRule(imsi, imei, userIpv4, sgwTeid,
//				value);
//	}
//
//	@Override
//	protected void buildRule4(String mmeS1apID, String cellID, String value) {
//		backFillWithLocalCache.buildRule4(mmeS1apID, cellID, value);
//
//	}
//
//	@Override
//	protected void buildRule4(String rule4, String value) {
//		backFillWithLocalCache.buildRule4(rule4, value);
//	}
//
//	protected void buildS1mmeFillRule(String imsi, String imei,
//			String userIpv4, String sgwTeid, String mmeS1apID,
//			String mmeGroupID, String mmeCode, String mTmsi, String cellID,
//			String value) {
//		backFillWithLocalCache.buildS1mmeFillRule(imsi, imei, userIpv4,
//				sgwTeid, mmeS1apID, mmeGroupID, mmeCode, mTmsi, cellID, value);
//
//	}
//
//	protected void buildImsiAndImeiFillRule(String imsi, String imei,
//			String value) {
//		backFillWithLocalCache.buildImsiAndImeiFillRule(imsi, imei, value);
//	}
//
//	/**
//	 * 获取回填信息，msisdn_imsi_imei
//	 * 
//	 * @param key
//	 * @return
//	 */
//	public BackfillInfo getInfo(String key, RuleType type) {
//		return backFillWithLocalCache.getInfo(key, type);
//	}
//
//}
