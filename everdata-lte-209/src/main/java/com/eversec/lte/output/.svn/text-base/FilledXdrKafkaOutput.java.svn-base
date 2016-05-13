//package com.eversec.lte.output;
//
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.kafka.KafkaOutputUtils;
//import com.eversec.lte.kafka.producer.KafkaByteProducer;
//import com.eversec.lte.main.LteMain;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.vo.DataQueueCache;
//
///**
// * 发送回填后的xdr到kafka（添加了模拟应用层所需的xdr类型标识）
// */
//public class FilledXdrKafkaOutput {
//
//	protected static final String CACHE_TYPE = "filled_kafka";
//
//	public static ArrayBlockingQueue<XdrSingleSource[]> FILLED_XDR_2_KAFKA_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity() / DataQueueCache.SIZE);
//
//	private static final ThreadLocal<DataQueueCache<XdrSingleSource>> KAFKA_DATA_CACHE = new ThreadLocal<DataQueueCache<XdrSingleSource>>() {
//		protected DataQueueCache<XdrSingleSource> initialValue() {
//			return new DataQueueCache<XdrSingleSource>(CACHE_TYPE) {
//				public XdrSingleSource[] createCache(int size) {
//					return new XdrSingleSource[SIZE];
//				}
//			};
//		};
//	};
//
//	public static void output(XdrSingleSource data) {
//		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_KAFKA) {
//			try {
//				XdrSingleSource[] cache = KAFKA_DATA_CACHE.get()
//						.addAndGet(data);
//				if (cache != null) {
//					FILLED_XDR_2_KAFKA_QUEUE.put(cache);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void cleanUp() {
//		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_KAFKA) {
//			try {
//				List<XdrSingleSource[]> caches = DataQueueCache
//						.cleanUp(CACHE_TYPE);
//				if (caches != null && caches.size() > 0) {
//					for (XdrSingleSource[] cache : caches) {
//						FILLED_XDR_2_KAFKA_QUEUE.put(cache);
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
//					KafkaByteProducer producer = new KafkaByteProducer(
//							SdtpConfig.getKafkaXdrBrokerList());
//					while (true) {
//						try {
//							while (true) {
//								XdrSingleSource[] datas = FILLED_XDR_2_KAFKA_QUEUE
//										.take();
////								for (int j = 0; j < datas.length; j++) {
////									utils.outputFilledXdr(producer, datas[j]);
////								}
//								
//								utils.outputFilledXdr(producer, datas );
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
