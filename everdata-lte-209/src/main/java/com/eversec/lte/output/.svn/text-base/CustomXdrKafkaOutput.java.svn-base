//package com.eversec.lte.output;
//
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.mina.core.buffer.IoBuffer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.kafka.KafkaOutputUtils2;
//import com.eversec.lte.kafka.producer.KafkaProducer;
//import com.eversec.lte.main.LteMain;
//import com.eversec.lte.model.XdrData;
//import com.eversec.lte.vo.DataQueueCache;
//
///**
// * 自定义XDR数据输出至kafka
// * 
// * @author bieremayi
// * 
// */
//public class CustomXdrKafkaOutput {
//	
//	private static Logger LOGGER = LoggerFactory.getLogger(CustomXdrKafkaOutput.class);
//
//	protected static final String CACHE_TYPE = "custom_kafka";
//	
//	public static final int SIZE_CUSTOM_KAFKA= DataQueueCache.SIZE * 10;
//
//	public static ArrayBlockingQueue<XdrData[]> CUSTOM_XDR_2_KAFKA_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity() / SIZE_CUSTOM_KAFKA);
//
//	private static final ThreadLocal<DataQueueCache<XdrData>> KAFKA_DATA_CACHE = new ThreadLocal<DataQueueCache<XdrData>>() {
//		protected DataQueueCache<XdrData> initialValue() {
//			return new DataQueueCache<XdrData>(CACHE_TYPE) {
//				public XdrData[] createCache(int size) {
//					return new XdrData[SIZE_CUSTOM_KAFKA];
//				}
//			};
//		};
//	};
//
//	public static void output(XdrData data) {
//		if (SdtpConfig.IS_OUTPUT_CUSTOM_XDR_2_KAFKA) {
//			try {
//				XdrData[] cache = KAFKA_DATA_CACHE.get().addAndGet(data);
//				if (cache != null) {
//					CUSTOM_XDR_2_KAFKA_QUEUE.put(cache);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void cleanUp() {
//		if (SdtpConfig.IS_OUTPUT_CUSTOM_XDR_2_KAFKA) {
//			try {
//				List<XdrData[]> caches = DataQueueCache.cleanUp(CACHE_TYPE);
//				if (caches != null && caches.size() > 0) {
//					for (XdrData[] cache : caches) {
//						CUSTOM_XDR_2_KAFKA_QUEUE.put(cache);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 启动自定义xdr输出任务
//	 */
//	public static void initOutputTask() {
//		for (int i = 0; i < SdtpConfig.getKafkaOutputThread(); i++) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
////					KafkaOutputUtils utils = new KafkaOutputUtils();
//					KafkaOutputUtils2 utils = new KafkaOutputUtils2();
//					while (true) {
//						KafkaProducer producer = null;
//						//test 
//						IoBuffer destList = IoBuffer.allocate(1024).setAutoExpand(true);
//						IoBuffer s1udestList = IoBuffer.allocate(1024).setAutoExpand(true);
//						try {
//						    producer = new KafkaProducer();
//							while (true) {
//								XdrData[] datas = CUSTOM_XDR_2_KAFKA_QUEUE
//										.take();
////								for (XdrData data : datas) {
////									utils.output(producer, datas);
////								}
//								
//								utils.outputX(producer, datas, destList, s1udestList);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						} finally {
//							if (producer != null) {
//								producer.close();
//							}
//						}
//						try {
//							TimeUnit.SECONDS.sleep(3);
//						} catch (InterruptedException e) {
//						}
//					}
//				}
//			});
//		}
//	}
//	public static void report() {
//		LOGGER.info(CustomXdrKafkaOutput.class.getSimpleName()+":"+CUSTOM_XDR_2_KAFKA_QUEUE.size());
//	}
//}
