//package com.eversec.lte.tmp;
//
//import static com.eversec.lte.config.SdtpConfig.IS_BACKFILL;
//import static com.eversec.lte.config.SdtpConfig.IS_COMPOUND_UEMR;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_CUSTOM_XDR_2_KAFKA;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_FILLED_XDR_2_FILE;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_FILLED_XDR_2_KAFKA;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_FILLED_XDR_2_SDTP;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_FILE;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_SDTP;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_FILE;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_KAFKA;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_SDTP;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_EMPTY;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_KAFKA;
//import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_SDTP;
//import static com.eversec.lte.config.SdtpConfig.getHttpPort;
//import static com.eversec.lte.config.SdtpConfig.getKafkaOutputThread;
//import static com.eversec.lte.config.SdtpConfig.getXdrProcessThread;
//import static com.eversec.lte.config.SdtpConfig.isApplicationLayer;
//import static com.eversec.lte.config.SdtpConfig.isBackFillOnlyRedis;
//import static com.eversec.lte.constant.SdtpConstants.DataSource.KAFKA;
//import static com.eversec.lte.constant.SdtpConstants.DataSource.SDTP;
//import static com.eversec.lte.processor.data.CacheData.S11_PENDING_CACHE;
//import static com.eversec.lte.processor.data.CacheData.S1MME_PENDING_CACHE;
//import static com.eversec.lte.processor.data.CacheData.S1U_PENDING_CACHE;
//import static com.eversec.lte.processor.data.CacheData.S6A_PENDING_CACHE;
//import static com.eversec.lte.processor.data.CacheData.SGS_PENDING_CACHE;
//import static com.eversec.lte.processor.data.CacheData.UEMR_PENDING_CACHE;
//import static com.eversec.lte.processor.data.CacheData.UU_PENDING_CACHE;
//import static com.eversec.lte.processor.data.CacheData.X2_PENDING_CACHE;
//import static com.eversec.lte.processor.data.QueueData.MIX_XDR_FILE_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.ORIGINAL_RAW_FILE_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.ORIGINAL_RAW_KAFKA_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.ORIGINAL_RAW_SDTP_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.ORIGINAL_XDR_EMPTY_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.ORIGINAL_XDR_FILE_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.ORIGINAL_XDR_KAFKA_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.ORIGINAL_XDR_SDTP_OUTPUT_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.PROCESS_XDR_DATA_QUEUE;
//import static com.eversec.lte.processor.data.QueueData.UEMR_QUEUE;
//import static com.eversec.lte.processor.data.StaticData.FAILING_FILL_COUNT;
//import static com.eversec.lte.processor.data.StaticData.FULLINFO_COUNT;
//import static com.eversec.lte.processor.data.StaticData.RAW_2_SDTP_BYTES;
//import static com.eversec.lte.processor.data.StaticData.RAW_2_SDTP_PACKAGE;
//import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_BYTES;
//import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_COUNT;
//import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_PACKAGE;
//import static com.eversec.lte.processor.data.StaticData.REFILL_COUNT;
//import static com.eversec.lte.processor.data.StaticData.SOURCE_COUNT;
//import static com.eversec.lte.processor.data.StaticData.SUCC_FILL_COUNT;
//import static com.eversec.lte.processor.data.StaticData.XDR_2_SDTP_BYTES;
//import static com.eversec.lte.processor.data.StaticData.XDR_2_SDTP_PACKAGE;
//import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_BYTES;
//import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_COUNT;
//import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_PACKAGE;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.common.output.FileOutputTask;
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.kafka.KafkaLteServer;
//import com.eversec.lte.model.single.XdrSingleSourceUu;
//import com.eversec.lte.output.CompXdrFileOutput;
//import com.eversec.lte.output.CompXdrKafkaOutput;
//import com.eversec.lte.output.CompXdrSdtpOutput;
//import com.eversec.lte.output.CustomXdrKafkaOutput;
//import com.eversec.lte.output.FilledXdrFileOutput;
//import com.eversec.lte.output.FilledXdrKafkaOutput;
//import com.eversec.lte.output.FilledXdrSdtpOutput;
//import com.eversec.lte.output.OriginalRawFileOutput;
//import com.eversec.lte.output.OriginalRawSdtpOutput;
//import com.eversec.lte.output.OriginalXdrFileOutput;
//import com.eversec.lte.output.OriginalXdrSdtpOutput;
//import com.eversec.lte.processor.backfill.AbstractBackFill;
//import com.eversec.lte.processor.backfill.AbstractCmBackFill;
//import com.eversec.lte.processor.backfill.BackFillRuleLoader;
//import com.eversec.lte.processor.backfill.CmBackFillWithLocalCache;
//import com.eversec.lte.processor.backfill.CmBackFillWithRedis;
//import com.eversec.lte.processor.data.CacheData;
//import com.eversec.lte.processor.statistics.HttpStaticServer;
//import com.eversec.lte.processor.task.MixXdrDataFileOutputTask;
//import com.eversec.lte.processor.task.PendingXdrRefillTask;
//import com.eversec.lte.processor.task.RawDataFileOutputTask;
//import com.eversec.lte.processor.task.RawDataKafkaOutputTask;
//import com.eversec.lte.processor.task.UemrComponudOutputTask;
//import com.eversec.lte.processor.task.XdrDataBackFillTask;
//import com.eversec.lte.processor.task.XdrDataEmptyOutputTask;
//import com.eversec.lte.processor.task.XdrDataFileOutputTask;
//import com.eversec.lte.processor.task.XdrDataKafkaOutputTask;
//import com.eversec.lte.sdtp.client.SdtpLteClient;
//import com.eversec.lte.sdtp.server.SdtpLteServer;
//import com.eversec.lte.utils.HttpUtil;
//import com.eversec.lte.vo.DataQueueCache;
//
///**
// * 程序入口
// * 
// * @author bieremayi
// * 
// */
//public class CopyOfLteMain {
//
//	private static Logger LOGGER = LoggerFactory.getLogger(CopyOfLteMain.class);
//	public static ExecutorService EXEC = Executors.newCachedThreadPool();
//	public static ScheduledThreadPoolExecutor SCHEDULER = new ScheduledThreadPoolExecutor(
//			5);
//	public static FileOutputTask FILE_OUTPUT_TASK = new FileOutputTask();
//
//	public static void main(String[] args) throws NumberFormatException, DecoderException {
//		// 1.初始化单接口xdr数据源
//		int xdrSource = SdtpConfig.getXdrSource();
//		if (xdrSource == KAFKA) {
//			KafkaLteServer.initXdrDataConsumer();
//		}
//		// 2.初始化原始码流数据源
//		int rawSource = SdtpConfig.getRawSource();
//		if (rawSource == KAFKA) {
//			KafkaLteServer.initRawDataConsumer();
//		}
//		if (xdrSource == SDTP || rawSource == SDTP) {
//			SdtpLteServer.start();
//		}
//		// 3.初始化http统计server
//		initHttpServer();
//		// 4.是否启动sdtp client发送数据
//		if (IS_OUTPUT_FILLED_XDR_2_SDTP || IS_OUTPUT_ORIGINAL_CXDR_2_SDTP
//				|| IS_OUTPUT_ORIGINAL_RAW_2_SDTP
//				|| IS_OUTPUT_ORIGINAL_XDR_2_SDTP) {
//			initSdtpClient();
//		}
//		// 5.初始化文件输出工具类
//		if (IS_OUTPUT_ORIGINAL_CXDR_2_FILE) {
//			CompXdrFileOutput.initOutputTask();
//		}
//		if (IS_OUTPUT_FILLED_XDR_2_FILE) {
//			FilledXdrFileOutput.initOutputTask();
//		}
//		if (IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
//			OriginalXdrFileOutput.initOutputTask();
//		}
//		// 6.判断程序使用场景
//		if (isApplicationLayer()) {// 模拟应用层
//			/*
//			 * a.接收未处理单接口xdr b.接收回填后的单接口xdr c.接收合成xdr d.接收原始码流
//			 */
//			if (IS_OUTPUT_ORIGINAL_CXDR_2_FILE || IS_OUTPUT_FILLED_XDR_2_FILE
//					|| IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
//				initAppLogTask(5, 30, TimeUnit.SECONDS);
//				for (int i = 0; i < getXdrProcessThread(); i++) {
//					EXEC.execute(new MixXdrDataFileOutputTask(
//							MIX_XDR_FILE_OUTPUT_QUEUE));
//				}
//			}
//			LOGGER.info(
//					"启动模拟应用层，输出原始单接口xdr ： {} ，输出回填后单接口xdr ： {} ，输出合成xdr数据 ： {}",
//					new Object[] { IS_OUTPUT_ORIGINAL_XDR_2_FILE,
//							IS_OUTPUT_FILLED_XDR_2_FILE,
//							IS_OUTPUT_ORIGINAL_CXDR_2_FILE });
//		} else {// 合成层
//			if (IS_BACKFILL) {// 回填处理
//				if (SdtpConfig.isBackFillRuleLoad()) {
//					BackFillRuleLoader.load();
//				}
//
//				initCompLogTask(5, 30, TimeUnit.SECONDS);
//				EXEC.execute(new PendingXdrRefillTask());// 初始化需重新回填的pending xdr
//				if (IS_OUTPUT_CUSTOM_XDR_2_KAFKA) {// 判断是否发送自定义xdr至kafka（signal-cxdr-with-uemr,s1u-cxdr-with-uemr）
//					CustomXdrKafkaOutput.initOutputTask();
//				}
//				if (IS_OUTPUT_FILLED_XDR_2_KAFKA) {// 发送回填后的xdr到kafka（添加了模拟应用层所需的xdr类型标识）
//					FilledXdrKafkaOutput.initOutputTask();
//				}
//				// 开启多线程处理回填XDR任务
//				for (int i = 0; i < getXdrProcessThread(); i++) {
//					AbstractCmBackFill backfill = null;
//					if (isBackFillOnlyRedis()) {
//						backfill = new CmBackFillWithRedis();
//					} else {
//						backfill = new CmBackFillWithLocalCache();
//					}
//					EXEC.execute(new XdrDataBackFillTask(backfill,
//							PROCESS_XDR_DATA_QUEUE));
//				}
//				// 是否合成uemr话单
//				if (IS_COMPOUND_UEMR) {
//					EXEC.execute(new UemrComponudOutputTask(UEMR_QUEUE));
//				}
//			} else {// 未处理
//				// 仅做计数统计用，不向外输出数据
//				if (IS_OUTPUT_ORIGINAL_XDR_2_EMPTY) {
//					EXEC.execute(new XdrDataEmptyOutputTask(
//							ORIGINAL_XDR_EMPTY_OUTPUT_QUEUE));
//				}
//				// 未处理原始单接口xdr保存
//				if (IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
//					EXEC.execute(new XdrDataFileOutputTask(
//							ORIGINAL_XDR_FILE_OUTPUT_QUEUE));
//				}
//				if (IS_OUTPUT_ORIGINAL_XDR_2_KAFKA) {
//					for (int i = 0; i < getKafkaOutputThread(); i++) {
//						EXEC.execute(new XdrDataKafkaOutputTask(
//								ORIGINAL_XDR_KAFKA_OUTPUT_QUEUE));
//					}
//				}
//				if (IS_OUTPUT_ORIGINAL_XDR_2_SDTP) {
//
//				}
//				// 未处理原始码流保存
//				if (IS_OUTPUT_ORIGINAL_RAW_2_KAFKA) {
//					for (int i = 0; i < getKafkaOutputThread(); i++) {
//						EXEC.execute(new RawDataKafkaOutputTask(
//								ORIGINAL_RAW_KAFKA_OUTPUT_QUEUE));
//					}
//				}
//				if (IS_OUTPUT_ORIGINAL_XDR_2_KAFKA
//						|| IS_OUTPUT_ORIGINAL_RAW_2_KAFKA) {
//					SCHEDULER.scheduleAtFixedRate(new Runnable() {
//						@Override
//						public void run() {
//							LOGGER.info(
//									"xdr_package : {} , xdr_bytes : {} , xdr_count : {} , raw_package : {} , raw_bytes : {} , raw_count : {}",
//									new Object[] { XDR_RECEIVE_PACKAGE,
//											XDR_RECEIVE_BYTES,
//											XDR_RECEIVE_COUNT,
//											RAW_RECEIVE_PACKAGE,
//											RAW_RECEIVE_BYTES,
//											RAW_RECEIVE_COUNT });
//							LOGGER.info(
//									"xdr_2_kafka_queue : {} , xdr_2_file_queue : {} , xdr_2_sdtp_queue : {} ,raw_2_kafka_queue : {} , raw_2_file_queue : {} , raw_2_sdtp_queue : {}",
//									new Object[] {
//											ORIGINAL_XDR_KAFKA_OUTPUT_QUEUE
//													.size(),
//											ORIGINAL_XDR_FILE_OUTPUT_QUEUE
//													.size(),
//											ORIGINAL_XDR_SDTP_OUTPUT_QUEUE
//													.size(),
//											ORIGINAL_RAW_KAFKA_OUTPUT_QUEUE
//													.size(),
//											ORIGINAL_RAW_FILE_OUTPUT_QUEUE
//													.size(),
//											ORIGINAL_RAW_SDTP_OUTPUT_QUEUE
//													.size() });
//						}
//					}, 5, 30, TimeUnit.SECONDS);
//				}
//			}
//		}
//
//		// 加入原始码率写入file功能 2015.1.16 add
//		if (IS_OUTPUT_ORIGINAL_RAW_2_FILE) {
//			EXEC.execute(new RawDataFileOutputTask(
//					ORIGINAL_RAW_FILE_OUTPUT_QUEUE));
//			OriginalRawFileOutput.initOutputTask();
//		}
//
//		// 判断是否启动文件输出定时检查功能
//		if (IS_OUTPUT_ORIGINAL_XDR_2_FILE || IS_OUTPUT_ORIGINAL_CXDR_2_FILE
//				|| IS_OUTPUT_FILLED_XDR_2_FILE) {
//			SCHEDULER.scheduleAtFixedRate(FILE_OUTPUT_TASK, 5, 30,
//					TimeUnit.SECONDS);
//		}
//
//		// 启动定时清空DataQueueCache功能
//		if (IS_OUTPUT_ORIGINAL_CXDR_2_SDTP || IS_OUTPUT_FILLED_XDR_2_SDTP
//				|| IS_OUTPUT_FILLED_XDR_2_KAFKA
//				|| IS_OUTPUT_ORIGINAL_RAW_2_SDTP) {
//			SCHEDULER.scheduleAtFixedRate(new Runnable() {
//				@Override
//				public void run() {
//					CompXdrKafkaOutput.cleanUp();
//					CompXdrSdtpOutput.cleanUp();
//					CustomXdrKafkaOutput.cleanUp();
//					FilledXdrKafkaOutput.cleanUp();
//					FilledXdrSdtpOutput.cleanUp();
//					OriginalRawSdtpOutput.cleanUp();
//					OriginalXdrSdtpOutput.cleanUp();
//				}
//			}, 30, 10, TimeUnit.SECONDS);
//		}
//		CopyOfLteMain.initSdtpClient();
//		String dir = "/home/uu";
//		Collection<File> files = FileUtils.listFiles(new File(dir),
//				new String[] { "csv", "txt" }, true);
//		System.out.println(files.size());
//		for (File f : files) {
//			try {
//				List<XdrSingleSourceUu> uus = XdrSingleSourceUu.getUuXdrList(f
//						.getAbsolutePath());
//				for (XdrSingleSourceUu uu : uus) {
//					FilledXdrSdtpOutput.output(uu);
//				}
//				System.out.println(uus.size());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private static long LAST_SOURCE_COUNT = 0;
//
//	/**
//	 * 定时打印模拟应用层系统统计日志信息
//	 * 
//	 * @param delay
//	 * @param period
//	 * @param unit
//	 */
//	private static void initAppLogTask(final long delay, final long period,
//			TimeUnit unit) {
//		SCHEDULER.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				LOGGER.info(
//						"xdr_package : {} , xdr_bytes : {} , xdr_count : {} , raw_package : {} , raw_bytes : {} , raw_count : {} , output_queue : {} ",
//						new Object[] { XDR_RECEIVE_PACKAGE, XDR_RECEIVE_BYTES,
//								XDR_RECEIVE_COUNT, RAW_RECEIVE_PACKAGE,
//								RAW_RECEIVE_BYTES, RAW_RECEIVE_COUNT,
//								MIX_XDR_FILE_OUTPUT_QUEUE.size() });
//				// 未处理原始单接口xdr队列状态
//				if (IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
//					OriginalXdrFileOutput.report();
//				}
//				// 回填后单接口xdr队列状态
//				if (IS_OUTPUT_FILLED_XDR_2_FILE) {
//					FilledXdrFileOutput.report();
//				}
//			}
//		}, delay, period, unit);
//	}
//
//	/**
//	 * 定时打印合成层系统统计日志信息
//	 * 
//	 * @param delay
//	 * @param period
//	 * @param unit
//	 */
//	private static void initCompLogTask(final long delay, final long period,
//			TimeUnit unit) {
//		SCHEDULER.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				long currentSourceCount = SOURCE_COUNT.get();
//				LOGGER.info(
//						"count : {} , full : {} , succ : {} , fail : {} , refill : {} , rat : {}/s",
//						new Object[] {
//								currentSourceCount,
//								FULLINFO_COUNT,
//								SUCC_FILL_COUNT,
//								FAILING_FILL_COUNT,
//								REFILL_COUNT,
//								(currentSourceCount - LAST_SOURCE_COUNT)
//										/ period });
//				// 缓存存储状态
//				CacheData.report();
//				// 规则存储状态
//				AbstractBackFill.report();
//				// 未处理原始单接口xdr队列状态
//				if (IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
//					OriginalXdrFileOutput.report();
//				}
//				// 回填后单接口xdr队列状态
//				if (IS_OUTPUT_FILLED_XDR_2_FILE) {
//					FilledXdrFileOutput.report();
//				}
//				if (IS_OUTPUT_FILLED_XDR_2_SDTP
//						|| IS_OUTPUT_ORIGINAL_CXDR_2_SDTP
//						|| IS_OUTPUT_ORIGINAL_RAW_2_SDTP
//						|| IS_OUTPUT_ORIGINAL_XDR_2_SDTP
//						|| IS_OUTPUT_CUSTOM_XDR_2_KAFKA) {
//					LOGGER.info(
//							"custom_xdr_2_kafka_queue : {} ,filled_xdr_2_sdtp_queue : {} ,original_cxdr_2_sdtp_queue : {}, original_raw_2_sdtp_queue : {} ",
//							new Object[] {
//									CustomXdrKafkaOutput.CUSTOM_XDR_2_KAFKA_QUEUE
//											.size() * DataQueueCache.SIZE,
//									FilledXdrSdtpOutput.FILLED_XDR_2_SDTP_QUEUE
//											.size() * DataQueueCache.SIZE,
//									CompXdrSdtpOutput.ORIGINAL_CXDR_2_SDTP_QUEUE
//											.size(),
//									ORIGINAL_RAW_SDTP_OUTPUT_QUEUE.size() });
//					LOGGER.info(
//							"xdr_2_sdtp_package : {} , raw_2_sdtp_package : {} , xdr_2_sdtp_bytes : {} ,  raw_2_sdtp_bytes : {} ",
//							new Object[] { XDR_2_SDTP_PACKAGE.get(),
//									RAW_2_SDTP_PACKAGE.get(),
//									XDR_2_SDTP_BYTES.get(),
//									RAW_2_SDTP_BYTES.get() });
//				}
//				S11_PENDING_CACHE.cleanUp();
//				SGS_PENDING_CACHE.cleanUp();
//				S1MME_PENDING_CACHE.cleanUp();
//				S6A_PENDING_CACHE.cleanUp();
//				S1U_PENDING_CACHE.cleanUp();
//				UU_PENDING_CACHE.cleanUp();
//				X2_PENDING_CACHE.cleanUp();
//				UEMR_PENDING_CACHE.cleanUp();
//				LAST_SOURCE_COUNT = currentSourceCount;
//			}
//		}, delay, period, unit);
//		LOGGER.info("file output timer start after : {}  sec,period : {}  sec",
//				delay, period);
//	}
//
//	/**
//	 * 初始化sdtp client
//	 */
//	public static void initSdtpClient() {
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					SdtpLteClient.main(null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * http统计server
//	 */
//	public static void initHttpServer() {
//		int port = getHttpPort();
//		while (HttpUtil.isPortBind("localhost", port)) {
//			port++;
//			if (port > 65535) {
//				break;
//			}
//		}
//		try {
//			HttpStaticServer.start(port);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
