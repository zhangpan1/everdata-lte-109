//package com.eversec.lte.output;
//
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.kafka.KafkaOutputUtils;
//import com.eversec.lte.kafka.producer.KafkaProducer;
//import com.eversec.lte.main.LteMain;
//import com.eversec.lte.model.XdrData;
//import com.eversec.lte.vo.DataQueueCache;
//
///**
// * 合成XDR输出至kafka
// */
//public class CompXdrKafkaOutput {
//
//	protected static final String CACHE_TYPE = "comp_kafka";
//
//	public static ArrayBlockingQueue<XdrData[]> COMP_XDR_2_KAFKA_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity() / DataQueueCache.SIZE);
//
//	private static final ThreadLocal<DataQueueCache<XdrData>> KAFKA_DATA_CACHE = new ThreadLocal<DataQueueCache<XdrData>>() {
//		protected DataQueueCache<XdrData> initialValue() {
//			return new DataQueueCache<XdrData>(CACHE_TYPE) {
//				public XdrData[] createCache(int size) {
//					return new XdrData[SIZE];
//				}
//			};
//		};
//	};
//
//	static {
//		initOutputTask();
//	}
//
//	public static void output(XdrData data) {
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_KAFKA) {
//			try {
//				XdrData[] cache = KAFKA_DATA_CACHE.get().addAndGet(data);
//				if (cache != null) {
//					COMP_XDR_2_KAFKA_QUEUE.put(cache);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void cleanUp() {
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_KAFKA) {
//			try {
//				List<XdrData[]> caches = DataQueueCache.cleanUp(CACHE_TYPE);
//				if (caches != null && caches.size() > 0) {
//					for (XdrData[] cache : caches) {
//						COMP_XDR_2_KAFKA_QUEUE.put(cache);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 启动输出任务
//	 */
//	public static void initOutputTask() {
//		for (int i = 0; i < SdtpConfig.getKafkaOutputThread(); i++) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					KafkaOutputUtils utils = new KafkaOutputUtils();
//					while (true) {
//						KafkaProducer producer = new KafkaProducer();
//						try {
//							while (true) {
//								XdrData[] datas = COMP_XDR_2_KAFKA_QUEUE.take();
////								for (int j = 0; j < datas.length; j++) {
//									utils.output(producer, datas );
////								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						} finally {
//							if (producer != null) {
//								producer.close();
//							}
//						}
//					}
//				}
//			});
//		}
//	}
//
//	public static void report() {
//		
//	}
//}
