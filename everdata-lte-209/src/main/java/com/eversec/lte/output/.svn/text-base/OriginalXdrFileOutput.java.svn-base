//package com.eversec.lte.output;
//
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.CELL_MR;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S11;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S1MME;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S1U;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S6A;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.SGS;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UE_MR;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UU;
//import static com.eversec.lte.constant.SdtpConstants.XDRInterface.X2;
//import static com.eversec.lte.main.LteMain.EXEC;
//import static com.eversec.lte.main.LteMain.FILE_OUTPUT_TASK;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.common.output.FileOutput;
//import com.eversec.common.output.IOutput;
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//
//@SuppressWarnings("rawtypes")
//public class OriginalXdrFileOutput {
//	private static Logger logger = LoggerFactory
//			.getLogger(OriginalXdrFileOutput.class);
//	public static ArrayBlockingQueue<IOutput> S6A_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> SGS_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> S1MME_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> S11_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> S1U_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> UEMR_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> X2_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> CELLMR_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//	public static ArrayBlockingQueue<IOutput> UU_SOURCE_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity());
//
//	public static FileOutput S6A_SOURCE_OUTPUT;
//	public static FileOutput SGS_SOURCE_OUTPUT;
//	public static List<FileOutput> S1MME_SOURCE_OUTPUTS;
//	public static List<FileOutput> S11_SOURCE_OUTPUTS;
//	// public static List<FileOutput> S1U_SOURCE_OUTPUTS;
//	public static Map<String, List<FileOutput>> S1U_SOURCE_OUTPUTS_MAP;
//
//	public static FileOutput X2_SOURCE_OUTPUT;
//	public static FileOutput UU_SOURCE_OUTPUT;
//	public static FileOutput UEMR_SOURCE_OUTPUT;
//	public static FileOutput CELLMR_SOURCE_OUTPUT;
//
//	private static AtomicLong PERIOD_STAT = new AtomicLong(0);
//	/**
//	 * 输出队列状态
//	 */
//	public static void report() {
//		logger.info(
//				"s11_source : {} , sgs_source : {} , s1mme_source : {} , s6a_source : {} ",
//				new Object[] { S11_SOURCE_QUEUE.size(),
//						SGS_SOURCE_QUEUE.size(), S1MME_SOURCE_QUEUE.size(),
//						S6A_SOURCE_QUEUE.size() });
//		logger.info(
//				"s1u_source : {} , uu_source : {} , x2_source : {} , uemr_source : {} , cellmr_source : {} ",
//				new Object[] { S1U_SOURCE_QUEUE.size(), UU_SOURCE_QUEUE.size(),
//						X2_SOURCE_QUEUE.size(), UEMR_SOURCE_QUEUE.size(),
//						CELLMR_SOURCE_QUEUE.size() });
//		
//		logger.info("OriginalXdrFileOutput PERIOD_STAT:{}",PERIOD_STAT.get());
//		PERIOD_STAT.set(0); 
//	}
//
//	/**
//	 * 回填后的单接口XDR数据写入
//	 * 
//	 * @param data
//	 */
//	public static void output(XdrSingleSource data) {
//		flushSource(data.getCommon().getInterface(), data);
//	}
//
//	/**
//	 * 原数据写入
//	 * 
//	 * @param Interface
//	 * @param data
//	 */
//	public static void flushSource(String Interface, IOutput data) {
//		flushSource(Integer.parseInt(Interface), data);
//	}
//
//	/**
//	 * 原数据写入
//	 * 
//	 * @param Interface
//	 * @param data
//	 */
//	public static void flushSource(int Interface, IOutput data) {
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
//			try {
//				switch (Interface) {
//				case UU:
//					UU_SOURCE_QUEUE.put(data);
//					break;
//				case X2:
//					X2_SOURCE_QUEUE.put(data);
//					break;
//				case UE_MR:
//					UEMR_SOURCE_QUEUE.put(data);
//					break;
//				case CELL_MR:
//					CELLMR_SOURCE_QUEUE.put(data);
//					break;
//				case S1MME:
//					S1MME_SOURCE_QUEUE.put(data);
//					break;
//				case S6A:
//					S6A_SOURCE_QUEUE.put(data);
//					break;
//				case S11:
//					S11_SOURCE_QUEUE.put(data);
//					break;
//				case SGS:
//					SGS_SOURCE_QUEUE.put(data);
//					break;
//				case S1U:
//					S1U_SOURCE_QUEUE.put(data);
//					break;
//				default:
//					break;
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//			PERIOD_STAT.incrementAndGet();
//		}
//	}
//
//	/**
//	 * 初始化输出任务
//	 */
//	@SuppressWarnings("unchecked")
//	public static void initOutputTask() {
////		String max_memory_size = SdtpConfig.getOutputFileSize();
////		String expire_time = SdtpConfig.getOutputFilePeriod();
//
//		String sourceScaDir = SdtpConfig.getSourceScaDir();
//		String sourceSigDir = SdtpConfig.getSourceSigDir();
//		String sourceS1uDir = SdtpConfig.getSourceS1uDir();
//
//		String S6A = "s6a";
//		String SGS = "sgs";
//		String S1MME = "s1mme";
//		String S11 = "s11";
//		String S1U = "s1u";
//
//		String UU = "uu";
//		String X2 = "x2";
//		String UEMR = "uemr";
//		String CELLMR = "cellmr";
//		// 初始化output工具类
//		// s6a
//		S6A_SOURCE_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs(S6A).max_memory_size, 
//				SdtpConfig.getFileOutputConfigs(S6A).expire_time,
//				sourceSigDir + File.separator + S6A, S6A);
//		FILE_OUTPUT_TASK.addOutput(S6A_SOURCE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = S6A_SOURCE_QUEUE.take();
//						S6A_SOURCE_OUTPUT.flush(data);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		// sgs
//		SGS_SOURCE_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs(SGS).max_memory_size, 
//				SdtpConfig.getFileOutputConfigs(SGS).expire_time,
//				sourceSigDir + File.separator + SGS, SGS);
//		FILE_OUTPUT_TASK.addOutput(SGS_SOURCE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = SGS_SOURCE_QUEUE.take();
//						SGS_SOURCE_OUTPUT.flush(data);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		// s1mme
//		S1MME_SOURCE_OUTPUTS = new ArrayList<FileOutput>(
//				SdtpConfig.getS1mmeOutputThread());
//		for (int i = 0; i < SdtpConfig.getS1mmeOutputThread(); i++) {
//			LteFileOutput output = new LteFileOutput(SdtpConfig.getFileOutputConfigs(S1MME).max_memory_size,
//					SdtpConfig.getFileOutputConfigs(S1MME).expire_time, sourceSigDir + File.separator + S1MME
//							+ File.separator + i, S1MME);
//			S1MME_SOURCE_OUTPUTS.add(output);
//			FILE_OUTPUT_TASK.addOutput(output);
//		}
//		for (int i = 0; i < SdtpConfig.getS1mmeOutputThread(); i++) {
//			final int index = i;
//			EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//						try {
//							IOutput data = S1MME_SOURCE_QUEUE.take();
//							S1MME_SOURCE_OUTPUTS.get(index).flush(data);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//		// s11
//		S11_SOURCE_OUTPUTS = new ArrayList<FileOutput>(
//				SdtpConfig.getS11OutputThread());
//		for (int i = 0; i < SdtpConfig.getS11OutputThread(); i++) {
//			LteFileOutput output = new LteFileOutput(SdtpConfig.getFileOutputConfigs(S11).max_memory_size,
//					SdtpConfig.getFileOutputConfigs(S11).expire_time, sourceSigDir + File.separator + S11
//							+ File.separator + i, S11);
//			S11_SOURCE_OUTPUTS.add(output);
//			FILE_OUTPUT_TASK.addOutput(output);
//		}
//		for (int i = 0; i < SdtpConfig.getS11OutputThread(); i++) {
//			final int index = i;
//			EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//						try {
//							IOutput data = S11_SOURCE_QUEUE.take();
//							S11_SOURCE_OUTPUTS.get(index).flush(data);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//		// s1u
//		S1U_SOURCE_OUTPUTS_MAP = initS1uOutputMap();
//		for (String appTypeCode : S1U_SOURCE_OUTPUTS_MAP.keySet()) {
//			for (int i = 0; i < SdtpConfig.getS1UOutputThread(); i++) {
//				LteFileOutput output = new LteFileOutput(SdtpConfig.getFileOutputConfigs(appTypeCode.toLowerCase()).max_memory_size,
//						SdtpConfig.getFileOutputConfigs(appTypeCode.toLowerCase()).expire_time, sourceS1uDir + File.separator + S1U+"_"+appTypeCode
//								+ File.separator + i, S1U);
//				S1U_SOURCE_OUTPUTS_MAP.get(appTypeCode).add(output);
//				FILE_OUTPUT_TASK.addOutput(output);
//			}
//		}
//		for (int i = 0; i < SdtpConfig.getS1UOutputThread(); i++) {
//			final int index = i;
//			EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//						XdrSingleSourceS1U data = null;
//						try {
//							 data = (XdrSingleSourceS1U) S1U_SOURCE_QUEUE
//									.take();
//							 short code = data.getBusinessCommon()
//								.getAppTypeCode();
//							String appTypeCode = SdtpConstants.S1U_APP_TYPE_CODE_MAP.get(code);
//							S1U_SOURCE_OUTPUTS_MAP.get(appTypeCode).get(index)
//							.flush(data);
//						} catch ( Exception e) {
//							System.err.println(S1U_SOURCE_OUTPUTS_MAP);
//							System.err.println(data);
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//		// uu
//		UU_SOURCE_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs(UU).max_memory_size,
//				SdtpConfig.getFileOutputConfigs(UU).expire_time,
//				sourceScaDir + File.separator + UU, UU);
//		FILE_OUTPUT_TASK.addOutput(UU_SOURCE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = UU_SOURCE_QUEUE.take();
//						UU_SOURCE_OUTPUT.flush(data);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//
//		// x2
//		X2_SOURCE_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs(X2).max_memory_size, 
//				SdtpConfig.getFileOutputConfigs(X2).expire_time,
//				sourceScaDir + File.separator + X2, X2);
//		FILE_OUTPUT_TASK.addOutput(X2_SOURCE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = X2_SOURCE_QUEUE.take();
//						X2_SOURCE_OUTPUT.flush(data);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//
//		// uemr
//		UEMR_SOURCE_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs(UEMR).max_memory_size, 
//				SdtpConfig.getFileOutputConfigs(UEMR).expire_time,
//				sourceScaDir + File.separator + UEMR, UEMR);
//		FILE_OUTPUT_TASK.addOutput(UEMR_SOURCE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = UEMR_SOURCE_QUEUE.take();
//						UEMR_SOURCE_OUTPUT.flush(data);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//
//		// cellmr
//		CELLMR_SOURCE_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs(CELLMR).max_memory_size, 
//				SdtpConfig.getFileOutputConfigs(CELLMR).expire_time,
//				sourceScaDir + File.separator + CELLMR, CELLMR);
//		FILE_OUTPUT_TASK.addOutput(CELLMR_SOURCE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = CELLMR_SOURCE_QUEUE.take();
//						CELLMR_SOURCE_OUTPUT.flush(data);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//	}
//
//	private static Map<String, List<FileOutput>> initS1uOutputMap() {
//		Map<String, List<FileOutput>> result = new HashMap<>();
//		for(String value:SdtpConstants.S1U_APP_TYPE_CODE_MAP.values()){
//			result.put(value,
//					new ArrayList<FileOutput>(SdtpConfig.getS1UOutputThread())); 
//		}
//		return result;
//	}
//}
