//package com.eversec.lte.processor.data;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.cache.S11PendingCache;
//import com.eversec.lte.cache.S1UPendingCache;
//import com.eversec.lte.cache.S1mmePendingCache;
//import com.eversec.lte.cache.S6aPendingCache;
//import com.eversec.lte.cache.SgsPendingCache;
//import com.eversec.lte.cache.UemrPendingCache;
//import com.eversec.lte.cache.UuPendingCache;
//import com.eversec.lte.cache.X2PendingCache;
//import com.eversec.lte.config.SdtpConfig;
//
///**
// * 缓存未回填成功xdr数据
// * 
// * @author bieremayi
// * 
// */
//public class CacheData {
//
//	private static Logger logger = LoggerFactory.getLogger(CacheData.class);
//
//	public static S11PendingCache S11_PENDING_CACHE = new S11PendingCache(
//			SdtpConfig.getPendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//	public static S1mmePendingCache S1MME_PENDING_CACHE = new S1mmePendingCache(
//			SdtpConfig.getS1mmePendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//	public static S6aPendingCache S6A_PENDING_CACHE = new S6aPendingCache(
//			SdtpConfig.getPendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//	public static SgsPendingCache SGS_PENDING_CACHE = new SgsPendingCache(
//			SdtpConfig.getPendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//	public static UuPendingCache UU_PENDING_CACHE = new UuPendingCache(
//			SdtpConfig.getPendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//	public static X2PendingCache X2_PENDING_CACHE = new X2PendingCache(
//			SdtpConfig.getPendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//	public static UemrPendingCache UEMR_PENDING_CACHE = new UemrPendingCache(
//			SdtpConfig.getPendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//	public static S1UPendingCache S1U_PENDING_CACHE = new S1UPendingCache(
//			SdtpConfig.getPendingSize(), SdtpConfig.getPendingTTL(),
//			SdtpConfig.getPendingTTL());
//
////	public static String getStr1(int n, String s, String s1, String s2) {
////		return String.format("format %d you know that %s %s %s", n, s, s1, s2);
////	}
////
////	public static String getStr2(int n, String s, String s1, String s2) {
////		return "format " + n + " you know that" + s + " " + s1 + " " + s2;
////	}
////
////	public static String getStr3(int n, String s, String s1, String s2) {
////		StringBuilder sb = new StringBuilder(150);
////		sb.append("format ").append(n).append(" you know that").append(s)
////				.append(" ").append(s1).append(" ").append(s2);
////		return sb.toString();
////	}
////
////	public static String getStr4(int n, String s, String s1, String s2) {
////		StringBuffer sb = new StringBuffer(150);
////		sb.append("format ").append(n).append(" you know that").append(s)
////				.append(" ").append(s1).append(" ").append(s2);
////		return sb.toString();
////	}
//
//	public static void report() {
//		logger.info(
//				"xdr_process : {} ,xdr_pending : {} , s1mme_pending : {} , sgs_pending : {} , s6a_pending : {} ",
//				new Object[] { QueueData.PROCESS_XDR_DATA_QUEUE.size(),
//						QueueData.PENDING_XDR_DATA_QUEUE.size(),
//						S1MME_PENDING_CACHE.size(), SGS_PENDING_CACHE.size(),
//						S6A_PENDING_CACHE.size() });
//		logger.info(
//				"s11_pending : {} , s1u_pending : {} , uu_pending : {} ,  x2_pending : {} , uemr_pending : {} ",
//				new Object[] { S11_PENDING_CACHE.size(),
//						S1U_PENDING_CACHE.size(), UU_PENDING_CACHE.size(),
//						X2_PENDING_CACHE.size(), UEMR_PENDING_CACHE.size() });
//	}
//
//	public static void main(String[] args) {
//		int n = 5566;
//		String s = "world", s1 = "bieremayi", s2 = "xiaomizhou";
//		for (int j = 0; j < 5; j++) {
//			long start = System.currentTimeMillis();
//			for (int i = 0; i < 2500000; i++) {
////				getStr3(n + i, s + i, s1 + i, s2 + i);
//			}
//			System.out.println(System.currentTimeMillis() - start);
//		}
//	}
//}
