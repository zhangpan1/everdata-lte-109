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
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.LinkedTransferQueue;
//import java.util.concurrent.TransferQueue;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.common.output.IOutput;
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants;
//import com.eversec.lte.model.IOutputSingleAdapter;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//import com.eversec.lte.utils.FormatUtils;
//
///**
// * 针对集团考核数据输出
// * 
// * 《附件1 LTE信令采集方案-修正及补充V2.docx》
// * 
// * <pre>
// * 一、数据采集要求
// * 1、原始信令采集
// * 每200MB保存为一个文件；单文件命名原则：城市（城市全拼）+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.pcap（例如:chengdu2013111300001.pcap）。原始数据必须存储为PCAP格式，S1-U和S11/S1-MME接口原始数据应汇入同一链路存储。
// * 2、S1-U XDR采集文件命名规范
// * 每200M保存为一个文件；单文件命名：城市（城市全拼）+ XDR类型编码+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.txt（例如:chengdu1002013111300001.txt）；
// * 3、S1-MME XDR采集文件命名规范
// * 每200M保存为一个文件；单文件命名：城市（城市全拼）+ MME+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.txt（例如:chengduMME2013111300001.txt）；
// * 4、XDR 类型编码
// * 100	通用业务xDR
// * 101	DNS
// * 102	MMS
// * 103	HTTP
// * 104	FTP
// * 105	Email
// * 106	VOIP
// * 107	RTSP
// * 108	即时通信
// * 109	P2P
// * 0	Gn信令
// * 1	S11信令
// * 5、硬盘数据存放文件夹命名规则
// * 一级目录：城市：二级目录：采集时间；三级目录：pcap和xdr；四级目录：xdr下的通用话单、HTTP话单、VoIP话单、即时通信话单及S1-MME话单
// * </pre>
// * 
// * @author lirongzhi
// * 
// */
//@SuppressWarnings("rawtypes")
//public class ExamineXdrFileOutput {
//
//	private static Logger logger = LoggerFactory
//			.getLogger(ExamineXdrFileOutput.class);
//
//	// 存放回填后等待输出的单接口XDR队列
//	public static TransferQueue<IOutput> S6A_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//	public static TransferQueue<IOutput> SGS_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//	public static TransferQueue<IOutput> S1MME_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//	public static TransferQueue<IOutput> S11_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//	public static TransferQueue<IOutput> S1U_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//
//	public static TransferQueue<IOutput> UEMR_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//	public static TransferQueue<IOutput> X2_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//	public static TransferQueue<IOutput> CELLMR_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//	public static TransferQueue<IOutput> UU_EXAMINE_QUEUE = new LinkedTransferQueue<>();
//
//	// 文件输出工具
//	public static ExamineLteFileOutput S6A_EXAMINE_OUTPUT;
//	public static ExamineLteFileOutput SGS_EXAMINE_OUTPUT;
//	public static List<ExamineLteFileOutput> S1MME_EXAMINE_OUTPUTS = new ArrayList();
//	public static List<ExamineLteFileOutput> S11_EXAMINE_OUTPUTS = new ArrayList();
//	// public static List<FileOutput> S1U_EXAMINE_OUTPUTS;
//
//	public static Map<String, List<ExamineLteFileOutput>> S1U_EXAMINE_OUTPUTS_MAP;
//
//	public static ExamineLteFileOutput X2_EXAMINE_OUTPUT;
//	public static ExamineLteFileOutput UU_EXAMINE_OUTPUT;
//	public static ExamineLteFileOutput UEMR_EXAMINE_OUTPUT;
//	public static ExamineLteFileOutput CELLMR_EXAMINE_OUTPUT;
//
//	public static void report() {
//		logger.info(
//				"s1mme_examine : {} , sgs_examine : {} , s6a_examine : {} , s11_examine : {} ",
//				new Object[] { S1MME_EXAMINE_QUEUE.size(),
//						SGS_EXAMINE_QUEUE.size(), S6A_EXAMINE_QUEUE.size(),
//						S11_EXAMINE_QUEUE.size() });
//		logger.info(
//				"  uu_examine : {} , x2_examine : {} , uemr_examine : {} , cellmr_examine : {} ",
//				new Object[] { UU_EXAMINE_QUEUE.size(),
//						X2_EXAMINE_QUEUE.size(), UEMR_EXAMINE_QUEUE.size(),
//						CELLMR_EXAMINE_QUEUE.size() });
//
//		logger.info("s1u_examine : {} ",
//				new Object[] { S1U_EXAMINE_QUEUE.size() });
//	}
//
//	/**
//	 * 回填后的单接口XDR数据写入
//	 * 
//	 * @param Interface
//	 * @param data
//	 */
//	public static void output(String Interface, XdrSingleSource data) {
//		output(Integer.parseInt(Interface), data);
//	}
//
//	/**
//	 * 回填后的单接口XDR数据写入
//	 * 
//	 * @param data
//	 */
//	public static void output(XdrSingleSource data) {
//		output(data.getCommon().getInterface(), data);
//	}
//
//	/**
//	 * 回填后的单接口XDR数据写入
//	 * 
//	 * @param Interface
//	 * @param data
//	 */
//	public static void output(int Interface, XdrSingleSource data) {
//		if (SdtpConfig.IS_OUTPUT_EXAMINE_XDR_2_FILE) {
//			try {
//				if (SdtpConfig.IS_FILTER_OUTPUT_FILLED_2_SDTP) {
//					XdrSingleSource source = (XdrSingleSource) data;
//					String[] cityCodes = SdtpConfig.FILTER_OUTPUT_FILLED_2_SDTP_CITYCODE;
//					String[] interfaces = SdtpConfig.FILTER_OUTPUT_FILLED_2_SDTP_INTERFACE;
//					short Interface1 = source.getCommon().getInterface();
//					String city = source.getCommon().getCity();
//					if (!FormatUtils.arrayContans(cityCodes, city)
//							|| !FormatUtils.arrayContans(interfaces, ""
//									+ Interface1)) {
//						return;
//					}
//				}
//				switch (Interface) {
//				case UU:
//					UU_EXAMINE_QUEUE.transfer(data);
//					break;
//				case X2:
//					X2_EXAMINE_QUEUE.transfer(data);
//					break;
//				case UE_MR:
//					UEMR_EXAMINE_QUEUE.transfer(data);
//					break;
//				case CELL_MR:
//					CELLMR_EXAMINE_QUEUE.transfer(data);
//					break;
//				case S1MME:
//					S1MME_EXAMINE_QUEUE.transfer(data);
//					break;
//				case S6A:
//					S6A_EXAMINE_QUEUE.transfer(data);
//					break;
//				case S11:
//					S11_EXAMINE_QUEUE.transfer(data);
//					break;
//				case SGS:
//					SGS_EXAMINE_QUEUE.transfer(data);
//					break;
//				case S1U:
//					S1U_EXAMINE_QUEUE.transfer(data);
//					break;
//				default:
//					break;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public static void initOutputTask() {
//		String max_memory_size = SdtpConfig.getOutputFileSize();
//		String expire_time = SdtpConfig.getOutputFilePeriod();
//		String examineDir = SdtpConfig.getExamineDir();
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
//		S6A_EXAMINE_OUTPUT = new ExamineLteFileOutput(max_memory_size,
//				expire_time, examineDir, S6A, S6A);
//		FILE_OUTPUT_TASK.addOutput(S6A_EXAMINE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = S6A_EXAMINE_QUEUE.take();
//						S6A_EXAMINE_OUTPUT.flush(data);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		// sgs
//		SGS_EXAMINE_OUTPUT = new ExamineLteFileOutput(max_memory_size,
//				expire_time, examineDir, SGS, SGS);
//		FILE_OUTPUT_TASK.addOutput(SGS_EXAMINE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = SGS_EXAMINE_QUEUE.take();
//						SGS_EXAMINE_OUTPUT.flush(data);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		// s1mme
//		for (int i = 0; i < SdtpConfig.getS1mmeOutputThread(); i++) {
//			ExamineLteFileOutput out = new ExamineLteFileOutput(
//					max_memory_size, expire_time, examineDir, S1MME, "MME");
//			out.setStartPos(i);
//			S1MME_EXAMINE_OUTPUTS.add(out);
//			FILE_OUTPUT_TASK.addOutput(out);
//		}
//		for (int i = 0; i < SdtpConfig.getS1mmeOutputThread(); i++) {
//			final int index = i;
//			EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//						try {
//							IOutput data = S1MME_EXAMINE_QUEUE.take();
//							S1MME_EXAMINE_OUTPUTS.get(index).flush(data);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//		// s11
//		for (int i = 0; i < SdtpConfig.getS11OutputThread(); i++) {
//			ExamineLteFileOutput out = new ExamineLteFileOutput(
//					max_memory_size, expire_time, examineDir, S11, S11);
//			out.setStartPos(i);
//			S11_EXAMINE_OUTPUTS.add(out);
//			FILE_OUTPUT_TASK.addOutput(out);
//		}
//
//		for (int i = 0; i < SdtpConfig.getS11OutputThread(); i++) {
//			final int index = i;
//			EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//						try {
//							IOutput data = S11_EXAMINE_QUEUE.take();
//							S11_EXAMINE_OUTPUTS.get(index).flush(data);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		}
//		// s1u
//		S1U_EXAMINE_OUTPUTS_MAP = new HashMap<>();
//		for (short code : SdtpConstants.S1U_APP_TYPE_CODE_MAP.keySet()) {
//			String appTypeCode = SdtpConstants.S1U_APP_TYPE_CODE_MAP.get(code);
//
//			ArrayList<ExamineLteFileOutput> outputs = new ArrayList<>();
//			for (int i = 0; i < SdtpConfig.getS1UOutputThread(); i++) {
//				ExamineLteFileOutput output = new ExamineLteFileOutput(
//						max_memory_size, expire_time, examineDir, appTypeCode,
//						code + "");
//				output.setStartPos(i);
//				outputs.add(output);
//				FILE_OUTPUT_TASK.addOutput(output);
//			}
//			S1U_EXAMINE_OUTPUTS_MAP.put(appTypeCode, outputs);
//		}
//		for (int i = 0; i < SdtpConfig.getS1UOutputThread(); i++) {
//			final int index = i;
//			EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//						IOutput data2 = null;
//						try {
//							data2 = S1U_EXAMINE_QUEUE.take();
//							if (data2 instanceof XdrSingleSourceS1U) {
//
//								XdrSingleSourceS1U data = (XdrSingleSourceS1U) data2;
//								short code = data.getBusinessCommon()
//										.getAppTypeCode();
//								String appTypeCode = SdtpConstants.S1U_APP_TYPE_CODE_MAP
//										.get(code);
//								S1U_EXAMINE_OUTPUTS_MAP.get(appTypeCode)
//										.get(index).flush(data);
//
//							} else if (data2 instanceof IOutputSingleAdapter) {
//
//								IOutputSingleAdapter data = (IOutputSingleAdapter) data2;
//								short code = data.getBusinessCommon()
//										.getAppTypeCode();
//								String appTypeCode = SdtpConstants.S1U_APP_TYPE_CODE_MAP
//										.get(code);
//								S1U_EXAMINE_OUTPUTS_MAP.get(appTypeCode)
//										.get(index).flush(data);
//
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//			});
//		}
//
//		// uu
//		UU_EXAMINE_OUTPUT = new ExamineLteFileOutput(max_memory_size,
//				expire_time, examineDir, UU, UU);
//		FILE_OUTPUT_TASK.addOutput(UU_EXAMINE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = UU_EXAMINE_QUEUE.take();
//						UU_EXAMINE_OUTPUT.flush(data);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//
//		// x2
//		X2_EXAMINE_OUTPUT = new ExamineLteFileOutput(max_memory_size,
//				expire_time, examineDir, X2, X2);
//		FILE_OUTPUT_TASK.addOutput(X2_EXAMINE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = X2_EXAMINE_QUEUE.take();
//						X2_EXAMINE_OUTPUT.flush(data);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//
//		// uemr
//		UEMR_EXAMINE_OUTPUT = new ExamineLteFileOutput(max_memory_size,
//				expire_time, examineDir, UEMR, UEMR);
//		FILE_OUTPUT_TASK.addOutput(UEMR_EXAMINE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = UEMR_EXAMINE_QUEUE.take();
//						UEMR_EXAMINE_OUTPUT.flush(data);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//
//		// cellmr
//		CELLMR_EXAMINE_OUTPUT = new ExamineLteFileOutput(max_memory_size,
//				expire_time, examineDir, CELLMR, CELLMR);
//		FILE_OUTPUT_TASK.addOutput(CELLMR_EXAMINE_OUTPUT);
//		EXEC.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						IOutput data = CELLMR_EXAMINE_QUEUE.take();
//						CELLMR_EXAMINE_OUTPUT.flush(data);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//	}
//}
