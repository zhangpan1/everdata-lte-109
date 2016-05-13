package com.eversec.lte.main.test;
//package com.eversec.lte.main;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedTransferQueue;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TransferQueue;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.apache.mina.core.buffer.IoBuffer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants;
//import com.eversec.lte.kafka.consumer.KafkaConsumer;
//import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;
//import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
//import com.eversec.lte.sdtp.model.SdtpHeader;
//import com.eversec.lte.utils.SdtpUtils;
//
//public class TMainQX2 implements KafkaConsumerHandler {
//
//	private static Logger LOGGER = LoggerFactory.getLogger(TMainQX2.class);
//
//	protected static long LAST_SOURCE_COUNT = 0;
//
//	protected static AtomicLong COUNT = new AtomicLong();
//
//	protected static long Q_LAST_SOURCE_COUNT = 0;
//
//	protected static AtomicLong Q_COUNT = new AtomicLong();
//	
//
//	private static BlockingQueue<byte[]> QUEUE = new ArrayBlockingQueue<byte[]>(
//			50000000);
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
//					.getKafkaConsumerHostAndPort(), new TMainQX2()));
//		}
//	}
//
//	static XdrSingleBytesDecoder xdrDecoder = new XdrSingleBytesDecoder();
//
//	private static void getQueue() {
//		final int drainMaxElements = SdtpConfig.getDrainMaxElements();
//		final long drainTaskSleepMills = SdtpConfig.getDrainTaskSleepMills();
//		for (int i = 0; i < SdtpConfig.getXdrProcessThread(); i++) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//						Collection<byte[]> coll = new ArrayList<>();
//						while (true) {
//							int count = QUEUE.drainTo(coll, drainMaxElements);
//							if (count > 0) {
//								for (byte[] data : coll) {
//									NotifyXDRDataReq req = decodeXDRData(data);
//									List<XdrSingleSource> xdrs = xdrDecoder
//											.decode(req.getLoad());
//									Q_COUNT.incrementAndGet();
//								}
//								coll.clear();
//							} else {
//								try {
//									TimeUnit.MILLISECONDS
//											.sleep(drainTaskSleepMills);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//							}
//						}
//					}
//
//				}
//			});
//		}
//
//	}
//
//	private static NotifyXDRDataReq decodeXDRData(byte[] bytes) {
//		IoBuffer in = IoBuffer.wrap(bytes);
//		SdtpHeader header = SdtpUtils.getHeader(in);
//		byte[] load = new byte[bytes.length - SdtpConstants.SDTP_HEADER_LENGTH];
//		in.get(load);
//		NotifyXDRDataReq req = new NotifyXDRDataReq(load);
//		req.setHeader(header);
//		return req;
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
//						new Object[] {
//								(currentSourceCount - LAST_SOURCE_COUNT) / 30,
//								(qcurrentSourceCount - Q_LAST_SOURCE_COUNT) / 30 });
//				LAST_SOURCE_COUNT = currentSourceCount;
//				Q_LAST_SOURCE_COUNT = qcurrentSourceCount;
//			}
//		}, 5, 30, TimeUnit.SECONDS);
//	}
//}
