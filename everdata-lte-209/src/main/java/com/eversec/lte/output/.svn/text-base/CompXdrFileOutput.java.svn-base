package com.eversec.lte.output;

import static com.eversec.lte.constant.SdtpConstants.CompXDRType.CXDR_APPLICATION;
import static com.eversec.lte.constant.SdtpConstants.CompXDRType.CXDR_SIGNALING;
import static com.eversec.lte.constant.SdtpConstants.CompXDRType.CXDR_UEMR;
import static com.eversec.lte.main.LteMain.FILE_EXEC;
import static com.eversec.lte.main.LteMain.FILE_OUTPUT_TASK;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

import com.eversec.common.output.FileOutput;
import com.eversec.common.output.IOutput;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;

@SuppressWarnings("rawtypes")
public class CompXdrFileOutput {

	public static ArrayBlockingQueue<IOutput> COMPOUND_APP_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
			SdtpConfig.getDataQueueCapacity());
	public static ArrayBlockingQueue<IOutput> COMPOUND_SIGNALING_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
			SdtpConfig.getDataQueueCapacity());
	public static ArrayBlockingQueue<IOutput> COMPOUND_UEMR_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
			SdtpConfig.getDataQueueCapacity());

	public static FileOutput COMPOUND_APP_OUTPUT;
	public static FileOutput COMPOUND_SIGNALING_OUTPUT;
	public static FileOutput COMPOUND_UEMR_OUTPUT;

	/**
	 * 回填后的单接口XDR数据写入
	 * 
	 * @param Interface
	 * @param data
	 */
	public static void flushComp(String Interface, IOutput data) {
		flushComp(Integer.parseInt(Interface), data);
	}

	/**
	 * 回填后的单接口XDR数据写入
	 * 
	 * @param data
	 */
	public static void output(XdrCompoundSource data) {
		flushComp(data.getCommon().getXdrType(), data);
	}

	/**
	 * 回填后的单接口XDR数据写入
	 * 
	 * @param Interface
	 * @param data
	 */
	public static void flushComp(int xdrType, IOutput data) {
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_FILE) {
			try {
				switch (xdrType) {
				case CXDR_APPLICATION:
					COMPOUND_APP_OUTPUT_QUEUE.put(data);
					break;
				case CXDR_SIGNALING:
					COMPOUND_SIGNALING_OUTPUT_QUEUE.put(data);
					break;
				case CXDR_UEMR:
					COMPOUND_UEMR_OUTPUT_QUEUE.put(data);
					break;
				default:
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化输出任务
	 */
	@SuppressWarnings("unchecked")
	public static void initOutputTask() {
//		String max_memory_size = SdtpConfig.getOutputFileSize();
//		String expire_time = SdtpConfig.getOutputFilePeriod();
		String compoundDir = SdtpConfig.getCompoundDir();
		String APP = "userdata";
		String SIGNAL = "sig";
		String UEMR = "uemr";
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_FILE) {
			COMPOUND_APP_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs("comp_"+APP).max_memory_size,
					SdtpConfig.getFileOutputConfigs("comp_"+APP).expire_time, compoundDir + File.separator + APP, APP);
			COMPOUND_SIGNALING_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs("comp_"+SIGNAL).max_memory_size,
					SdtpConfig.getFileOutputConfigs("comp_"+SIGNAL).expire_time, compoundDir + File.separator + SIGNAL, SIGNAL);
			COMPOUND_UEMR_OUTPUT = new LteFileOutput(SdtpConfig.getFileOutputConfigs("comp_"+UEMR).max_memory_size,
					SdtpConfig.getFileOutputConfigs("comp_"+UEMR).expire_time, compoundDir + File.separator + UEMR, UEMR);
			FILE_OUTPUT_TASK.addOutput(COMPOUND_APP_OUTPUT);
			FILE_OUTPUT_TASK.addOutput(COMPOUND_SIGNALING_OUTPUT);
			FILE_OUTPUT_TASK.addOutput(COMPOUND_UEMR_OUTPUT);
			if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_FILE) {
				FILE_EXEC.execute(new Runnable() {
					@Override
					public void run() {
						while (true) {
							try {
								IOutput data = COMPOUND_APP_OUTPUT_QUEUE.take();
								COMPOUND_APP_OUTPUT.flush(data);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				FILE_EXEC.execute(new Runnable() {
					@Override
					public void run() {
						while (true) {
							try {
								IOutput data = COMPOUND_SIGNALING_OUTPUT_QUEUE
										.take();
								COMPOUND_SIGNALING_OUTPUT.flush(data);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				FILE_EXEC.execute(new Runnable() {
					@Override
					public void run() {
						while (true) {
							try {
								IOutput data = COMPOUND_UEMR_OUTPUT_QUEUE
										.take();
								XdrCompoundSourceUEMR uemr = (XdrCompoundSourceUEMR) data;
								COMPOUND_UEMR_OUTPUT.flush(uemr);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		}

	}
}
