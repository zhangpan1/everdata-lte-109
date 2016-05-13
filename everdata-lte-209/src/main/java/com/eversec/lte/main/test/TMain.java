package com.eversec.lte.main.test;
//package com.eversec.lte.main;
//
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.kafka.consumer.KafkaConsumer;
//import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
//
//public class TMain implements KafkaConsumerHandler {
//
//	private static Logger LOGGER = LoggerFactory.getLogger(TMain.class);
//
//	public static void main(String[] args) {
//
//		initLogTask();
//
//		String topic = SdtpConfig.getKafkaXdrTopic();// "xdr";
//		for (int i = 0; i < SdtpConfig.getKafkaXdrConsumerThread(); i++) {
//			LteMain.EXEC.execute(new KafkaConsumer(topic, SdtpConfig
//					.getKafkaConsumerHostAndPort(), new TMain()));
//		}
//	}
//
//	protected static long LAST_SOURCE_COUNT = 0;
//
//	protected static AtomicLong COUNT = new AtomicLong();
//
//	@Override
//	public void messageReceived(byte[] message) throws Exception {
//		COUNT.incrementAndGet();
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
//				LOGGER.info(
//						"######################### rat : {}/s",
//						new Object[] { (currentSourceCount - LAST_SOURCE_COUNT) / 30 });
//				LAST_SOURCE_COUNT = currentSourceCount;
//			}
//		}, 5, 30, TimeUnit.SECONDS);
//	}
//}
