package com.eversec.lte.processor.statistics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.binary.Hex;

import com.eversec.lte.config.SdtpConfig;

public class RawStat {
	private static boolean rawXdrIdStat = SdtpConfig.isRawXdrIdStat();
	private static final ConcurrentHashMap<String, Long> xdrIds = new ConcurrentHashMap<String, Long>();
	private static Long startTime = System.currentTimeMillis();
	
	public static AtomicLong RAW_COUNT = new AtomicLong();

	public static Long queryByXdrId(String xdrId) {
		return xdrIds.get(xdrId);
	}
	
	public static Long getStartTime() {
		return startTime;
	}
	

	static {
		if (rawXdrIdStat) {
			ScheduledThreadPoolExecutor sch = new ScheduledThreadPoolExecutor(1);
			sch.schedule(new Runnable() {

				@Override
				public void run() {
					System.out.println("RawData xdrIds clear!");
					startTime = System.currentTimeMillis();
					xdrIds.clear();

				}
			}, 30, TimeUnit.MINUTES);
		}
	}

	public static void statRaw(byte[] xdrId) {
		if (rawXdrIdStat) {
			xdrIds.put(Hex.encodeHexString(xdrId), System.currentTimeMillis());
		}
		RAW_COUNT.incrementAndGet();
	}

}
