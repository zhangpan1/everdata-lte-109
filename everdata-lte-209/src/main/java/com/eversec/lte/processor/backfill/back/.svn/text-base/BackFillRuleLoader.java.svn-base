package com.eversec.lte.processor.backfill.back;
//package com.eversec.lte.processor.backfill;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//
//import com.eversec.common.constant.CommonConstants;
//import com.eversec.lte.config.SdtpConfig;
//
//public class BackFillRuleLoader {
//	public static void load() {
//		try {
//			File file = new File(SdtpConfig.getBackFillRuleFile());
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					new FileInputStream(file)));
//			String line = null;
//			BackFillWithLocalCache bf = new BackFillWithLocalCache();
//			
//			while ((line = br.readLine()) != null) {
//				String[] strs = line.split("_",3);
//				String msisdn = strs[0];
//				String imsi = strs[1];
//				String imei = strs[2];
//				bf.buildImsiAndImeiFillRule(imsi, imei,
//						getValue(msisdn, imsi, imei));
//
//			}
//			System.out.println("load backfill rules size:"+BackFillWithLocalCache.PERSISTENT_RULE_CACHE.size());
//			br.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static String getValue(String msisdn, String imsi, String imei) {
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(msisdn).append(CommonConstants.UNDERLINE).append(imsi)
//				.append(CommonConstants.UNDERLINE).append(imei);
//		return tmp.toString();
//	}
//
//}
