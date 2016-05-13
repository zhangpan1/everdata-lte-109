package com.eversec.lte.output;

import static com.eversec.lte.main.LteMain.FILE_EXEC;
import static com.eversec.lte.main.LteMain.FILE_OUTPUT_TASK;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

import com.eversec.common.output.FileOutput;
import com.eversec.common.output.IOutput;
import com.eversec.lte.config.SdtpConfig;

public class OriginalRawFileOutput {

	public static ArrayBlockingQueue<IOutput> RAW_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
			SdtpConfig.getDataQueueCapacity());

	@SuppressWarnings("rawtypes")
	public static FileOutput RAW_OUTPUT;

	public static void output(String... data) {
		try {
			RAW_OUTPUT_QUEUE.put(createIOutput(data));
		} catch (InterruptedException e) {
		}
	}

	private static IOutput createIOutput(final String[] data) {
		return new IOutput() {
			private static final long serialVersionUID = 1L;
			@Override
			public int getMemoryBytes() {
				return 	16 + 1 + 8 * data.length;
			}
			@Override
			public String[] toStringArr() {
				return data;
			}
		};
	}

	/**
	 * 初始化输出任务
	 */
	@SuppressWarnings("unchecked")
	public static void initOutputTask() {
		String max_memory_size = "30m";//SdtpConfig.getOutputFileSize();
		String expire_time = "1m";//SdtpConfig.getOutputFilePeriod();
		String compoundDir = SdtpConfig.getSourceDir();
		String RAW = "raw";
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_FILE) {
			RAW_OUTPUT = new LteFileOutput(max_memory_size, expire_time,
					compoundDir + File.separator + RAW, RAW);
			FILE_OUTPUT_TASK.addOutput(RAW_OUTPUT);
			FILE_EXEC.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							IOutput data = RAW_OUTPUT_QUEUE.take();
							RAW_OUTPUT.flush(data);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
}
