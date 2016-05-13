//package com.eversec.lte.processor.statistics;
//
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.main.LteMain;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS1MME;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//import com.eversec.lte.model.single.s1u.XdrSingleS1UApp;
//import com.eversec.lte.model.single.s1u.XdrSingleS1UAppDNS;
//import com.eversec.lte.model.single.s1u.XdrSingleS1UAppHttp;
//
//public class XdrDelayStat {
//	
//	private static Logger LOGGER = LoggerFactory.getLogger(XdrDelayStat.class);
//
//	public static AtomicLong s1ccount = new AtomicLong(0);
//	public static AtomicLong httpcount = new AtomicLong(0);
//	public static AtomicLong dnscount = new AtomicLong(0);
//
//	public static AtomicLong s1cdelay = new AtomicLong(0);
//	public static AtomicLong httpdelay = new AtomicLong(0);
//	public static AtomicLong dnsdelay = new AtomicLong(0);
//
//	public static void addXdr(XdrSingleSource source) {
//		if (source instanceof XdrSingleSourceS1MME) {
//			XdrSingleSourceS1MME s1c = (XdrSingleSourceS1MME) source;
//			s1cdelay.addAndGet(System.currentTimeMillis()
//					- s1c.getEndTime().getTime());
//			s1ccount.incrementAndGet();
//		} else if (source instanceof XdrSingleSourceS1U) {
//			XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) source;
//			XdrSingleS1UApp app = s1u.getApp();
//			if (app instanceof XdrSingleS1UAppHttp) {
//				httpdelay.addAndGet(System.currentTimeMillis()
//						- s1u.getBusinessCommon().getEndTime().getTime());
//				httpcount.incrementAndGet();
//			} else if (app instanceof XdrSingleS1UAppDNS) {
//				dnsdelay.addAndGet(System.currentTimeMillis()
//						- s1u.getBusinessCommon().getEndTime().getTime());
//				dnscount.incrementAndGet();
//			}
//		}
//
//	}
//
//	public static void reportAndReset() {
//		report();
//		reset();
//	}
//
//	private static void report() {
//		LOGGER.info("s1ccount:" + s1ccount.get() + ",httpcount:"
//				+ httpcount.get() + ",dnscount:" + dnscount.get() + ",s1cdelay:"
//				+ s1cdelay.get() + ",httpdelay:" + httpdelay.get() + ",dnsdelay:"
//				+ dnsdelay.get());
//	}
//
//	public static void reset() {
//		s1ccount.set(0);
//		httpcount.set(0);
//		dnscount.set(0);
//		s1cdelay.set(0);
//		httpdelay.set(0);
//		dnsdelay.set(0);
//	}
//}
