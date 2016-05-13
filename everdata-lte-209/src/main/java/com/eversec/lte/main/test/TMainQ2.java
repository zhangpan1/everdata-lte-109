package com.eversec.lte.main.test;
//package com.eversec.lte.main;
//
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.LinkedTransferQueue;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TransferQueue;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.kafka.consumer.KafkaConsumer;
//import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
//
//public class TMainQ2 implements KafkaConsumerHandler {
//
//	private static Logger LOGGER = LoggerFactory.getLogger(TMainQ2.class);
//	
//
//	protected static long LAST_SOURCE_COUNT = 0;
//
//	protected static AtomicLong COUNT = new AtomicLong();
//
//	
//
//	protected static long Q_LAST_SOURCE_COUNT = 0;
//
//	protected static AtomicLong Q_COUNT = new AtomicLong();
//
//	
//	private static ArrayBlockingQueue<byte[]> QUEUE = new  ArrayBlockingQueue<byte[]>(SdtpConfig.getDataQueueCapacity());
//
//	public static void main(String[] args) {
//
//		initLogTask();
//		
//		getQueue();
//
//		String topic = SdtpConfig.getKafkaXdrTopic();// "xdr";
//		for (int i = 0; i < SdtpConfig.getKafkaXdrConsumerThread(); i++) {
//			LteMain.EXEC.execute(new KafkaConsumer(topic, SdtpConfig
//					.getKafkaConsumerHostAndPort(), new TMainQ2()));
//		}
//	}
//
//	private static void getQueue() {
//		for (int i = 0; i < SdtpConfig.getXdrProcessThread(); i++) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while(true ){
//						try {
//							QUEUE.take();
//							Q_COUNT.incrementAndGet();
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//	}
//
//	@Override
//	public void messageReceived(byte[] message) throws Exception {
//		COUNT.incrementAndGet();
//		QUEUE.put(message);
//	}
//
//	/**
//	 * 定时打印模拟应用层系统统计日志信息
//	 * 
//	 * @param delay
//	 * @param period
//	 * @param unit
//	 */
//	private static void initLogTask() {
//		ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(
//				5);
//
//		scheduler.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				long currentSourceCount = COUNT.get();
//				long qcurrentSourceCount = Q_COUNT.get();
//				LOGGER.info(
//						"######################### rat : {}/s ,q_rat : {}/s",
//						new Object[] { (currentSourceCount - LAST_SOURCE_COUNT) / 30,
//								(qcurrentSourceCount - Q_LAST_SOURCE_COUNT) / 30});
//				LAST_SOURCE_COUNT = currentSourceCount;
//				Q_LAST_SOURCE_COUNT = qcurrentSourceCount;
//			}
//		}, 5, 30, TimeUnit.SECONDS);
//	}
//}
