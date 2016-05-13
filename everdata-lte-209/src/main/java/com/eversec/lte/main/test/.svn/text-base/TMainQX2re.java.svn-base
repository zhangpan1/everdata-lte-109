package com.eversec.lte.main.test;
//package com.eversec.lte.main;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.apache.mina.core.buffer.IoBuffer;
//import org.apache.mina.util.ConcurrentHashSet;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.common.constant.CommonConstants;
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants;
//import com.eversec.lte.kafka.consumer.KafkaConsumer;
//import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;
//import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
//import com.eversec.lte.sdtp.model.SdtpHeader;
//import com.eversec.lte.sdtp.redis.JedisTimerTools;
//import com.eversec.lte.sdtp.redis.JedisValueGetHandler;
//import com.eversec.lte.utils.SdtpUtils;
//
////conusmer 数不要超过 partition 数 ，切记
//public class TMainQX2re implements KafkaConsumerHandler {
//
//	private static Logger LOGGER = LoggerFactory.getLogger(TMainQX2re.class);
//
//	protected static long LAST_SOURCE_COUNT = 0;
//
//	protected static AtomicLong COUNT = new AtomicLong();
//
//	protected static long Q_LAST_SOURCE_COUNT = 0;
//
//	protected static long M_LAST_SOURCE_COUNT = 0;
//
//	protected static long LAST_IMSI_FIND = 0;
//
//	protected static long S_LAST_SOURCE_COUNT = 0;
//
//	protected static AtomicLong Q_COUNT = new AtomicLong();
//
//	protected static AtomicLong S_COUNT = new AtomicLong();
//
//	protected static AtomicLong M_COUNT = new AtomicLong();
//
//	protected static ConcurrentHashSet<String> IMSI = new ConcurrentHashSet<>();
//	protected static ConcurrentHashSet<String> IMEI = new ConcurrentHashSet<>();
//	protected static ConcurrentHashSet<String> MISDN = new ConcurrentHashSet<>();
//
//	protected static AtomicLong IMSI_FIND_COUNT = new AtomicLong(0);
//
//	protected static volatile String TEST_RETURN_VALUE;
//
//	// protected static volatile boolean TEST_REC = SdtpConfig.testRec();
//
//	// protected static ConcurrentHashMap<String,Boolean> IMSI_FIND = new
//	// ConcurrentHashMap<>();
//
//	// 太大可能会引发gc 导致kafka 断开连接 maybe ... kafka 不停重来你都考虑一下gc
//	private static BlockingQueue<byte[]> QUEUE = new ArrayBlockingQueue<byte[]>(
//			SdtpConfig.getDataQueueCapacity() /* 5000000 */);
//
//	public static void main(String[] args) {
//
//		try {
//			initLogTask();
//
//			getQueue();
//
//			getValueQueue();
//
//			String topic = SdtpConfig.getKafkaXdrTopic();// "xdr";
//			for (int i = 0; i < SdtpConfig.getKafkaXdrConsumerThread(); i++) {
//				LteMain.EXEC.execute(new KafkaConsumer(topic, SdtpConfig
//						.getKafkaConsumerHostAndPort(), new TMainQX2re()));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void getValueQueue() {
//		for (int i = 0; i < 5; i++) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					final JedisTimerTools tools = new JedisTimerTools(false,
//							true);
//					while (true) {
//						try {
//
//							for (String imsi : IMSI) {
//								tools.get(imsi, new JedisValueGetHandler() {
//									@Override
//									public void onValueReturn(String key,
//											String value) {
//										TEST_RETURN_VALUE = key + ":" + value;
//										IMSI_FIND_COUNT.incrementAndGet();
//									}
//								});
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//	}
//
//	static XdrSingleBytesDecoder xdrDecoder = new XdrSingleBytesDecoder();
//
//	private static void getQueue() {
//		final boolean testSet = SdtpConfig.testSet();
//		final int drainMaxElements = SdtpConfig.getDrainMaxElements();
//		final long drainTaskSleepMills = SdtpConfig.getDrainTaskSleepMills();
//
//		for (int i = 0; i < SdtpConfig.getXdrProcessThread(); i++) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					final JedisTimerTools tools = new JedisTimerTools(true,
//							false);
//					while (true) {
//						try {
//							Collection<byte[]> coll = new ArrayList<>();
//							while (true) {
//								int count = QUEUE.drainTo(coll,
//										drainMaxElements);
//								if (count > 0) {
//									for (byte[] data : coll) {
//										NotifyXDRDataReq req = decodeXDRData(data);
//										List<XdrSingleSource> xdrs = xdrDecoder
//												.decode(req.getLoad());
//
//										for (XdrSingleSource xdr : xdrs) {
//											if (testSet) {
//												StringBuilder tmp = new StringBuilder();
//												tmp.append(
//														xdr.getCommon()
//																.getMsisdn())
//														.append(CommonConstants.UNDERLINE)
//														.append(xdr.getCommon()
//																.getImsi())
//														.append(CommonConstants.UNDERLINE)
//														.append(xdr.getCommon()
//																.getImei());
//
//												tools.set(xdr.getCommon()
//														.getImsi(), tmp
//														.toString());
//
//											}
//											IMSI.add(xdr.getCommon().getImsi());
//											IMEI.add(xdr.getCommon().getImei());
//											MISDN.add(xdr.getCommon()
//													.getMsisdn());
//											M_COUNT.incrementAndGet();
//										}
//
//										Q_COUNT.incrementAndGet();
//									}
//									coll.clear();
//								} else {
//									try {
//										TimeUnit.MILLISECONDS
//												.sleep(drainTaskSleepMills);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
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
//		try {
//			COUNT.incrementAndGet();
//			QUEUE.put(message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
//				try {
//					long currentSourceCount = COUNT.get();
//					long qcurrentSourceCount = Q_COUNT.get();
//					long mcurrentSourceCount = M_COUNT.get();
//					long imsi_find = IMSI_FIND_COUNT.get();
//					long scurrentSourceCount = S_COUNT.get();
//					LOGGER.info(
//							"######################### rat : {}/s ,q_rat : {}/s,m_rat : {}/s,  size:{},IMSI:{},IMEI:{},MISDN:{},imsi_find:{}/s,s_rat:{}/s ",
//							new Object[] {
//									(currentSourceCount - LAST_SOURCE_COUNT) / 30,
//									(qcurrentSourceCount - Q_LAST_SOURCE_COUNT) / 30,
//									(mcurrentSourceCount - M_LAST_SOURCE_COUNT) / 30,
//									QUEUE.size(), IMSI.size(),
//									IMEI.size(),
//									MISDN.size(),
//									(imsi_find - LAST_IMSI_FIND) / 30,
//									// JedisTools.statSet(30),
//									(scurrentSourceCount - S_LAST_SOURCE_COUNT) / 30, });
//					LAST_SOURCE_COUNT = currentSourceCount;
//					Q_LAST_SOURCE_COUNT = qcurrentSourceCount;
//					M_LAST_SOURCE_COUNT = mcurrentSourceCount;
//					LAST_IMSI_FIND = imsi_find;
//					S_LAST_SOURCE_COUNT = scurrentSourceCount;
//					
//					JedisTimerTools.statSet(30*1000);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}, 5, 30, TimeUnit.SECONDS);
//	}
//}
