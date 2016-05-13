package com.eversec.lte.output;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.vo.DataQueueCache;

public class OriginalRawSdtpOutput {
	
	protected static final String CACHE_TYPE = "rawSdtp";


	/**
	 * 待发送至SDTP中的合成XDR
	 */
	public static final ArrayBlockingQueue<byte[][]> ORIGINAL_RAW_2_SDTP_QUEUE = new ArrayBlockingQueue<>(
			SdtpConfig.getDataQueueCapacity() / DataQueueCache.SIZE);

	private static final ThreadLocal<DataQueueCache<byte[]>> ORIGINAL_RAW_CACHE = new ThreadLocal<DataQueueCache<byte[]>>() {
		protected DataQueueCache<byte[]> initialValue() {
			return new DataQueueCache<byte[]>(CACHE_TYPE) {
				public byte[][] createCache(int size) {
					return new byte[SIZE][];
				}
			};
		};
	};

	/**
	 * 发送原始码流到模拟应用层
	 * 
	 * @param data
	 */
	public static void outputOriginalRaw(byte[] data) {
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_SDTP) {
			try {
				byte[][] cache = ORIGINAL_RAW_CACHE.get().addAndGet(data);
				if (cache != null) {
					ORIGINAL_RAW_2_SDTP_QUEUE.put(cache);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	 
	public static void cleanUp() {
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_SDTP) {
			try {
				List<byte[][]> caches = DataQueueCache.cleanUp(CACHE_TYPE);
				if (caches != null && caches.size() > 0) {
					for (byte[][] cache : caches) {
						ORIGINAL_RAW_2_SDTP_QUEUE.put(cache);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void report() {
		
	}
}
