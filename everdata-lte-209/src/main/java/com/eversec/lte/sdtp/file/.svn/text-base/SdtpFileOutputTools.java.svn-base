package com.eversec.lte.sdtp.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.single.XdrSingleSource;

/**
 * output -> find type arrayBlock -> threads each have a buffer IoBuffer -> 1
 * arrayblock -> 1thread file output
 * 
 * @author lirongzhi
 * 
 */
public class SdtpFileOutputTools extends FileOutputTools {

	// private static

	private static SdtpFileOutputTools instance = new SdtpFileOutputTools();

	public static SdtpFileOutputTools getInstance() {
		return instance;
	}

	protected SdtpFileOutputTools() {
		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_FILE
				|| SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE
				|| SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE) {
			init();
		}
	}

	public String ScaDir() {
		return SdtpConfig.getFilledScaDir();
	}

	public String SigDir() {
		return SdtpConfig.getFilledSigDir();
	}

	public String S1uDir() {
		return SdtpConfig.getFilledS1uDir();
	}

	public void init() {
		String filledScaDir = ScaDir();
		String filledSigDir = SigDir();
		String filledS1uDir = S1uDir();

		String[] SIGS = { S6A, SGS, S1MME, S11 };
		String[] SCAS = { UU, X2, UEMR, CELLMR };

		file_output = new SdtpFileBufferOutput();

		ArrayBlockingQueue<FileBuffer> fileBufferQueue = file_output
				.getFileBufferQueue();

		for (String SIG : SIGS) {
			buffer_cache_map.put(
					SIG,
					new SdtpFileBufferCache(
							filledSigDir + File.separator + SIG, SdtpConfig
									.getFileOutputConfigs(SIG).max_memory_size,
							SdtpConfig.getFileOutputConfigs(SIG).expire_time,
							fileBufferQueue,1000, SIG, 0));
		}

		for (String SCA : SCAS) {
			buffer_cache_map.put(
					SCA,
					new SdtpFileBufferCache(
							filledScaDir + File.separator + SCA, SdtpConfig
									.getFileOutputConfigs(SCA).max_memory_size,
							SdtpConfig.getFileOutputConfigs(SCA).expire_time,
							fileBufferQueue,1000, SCA, 0));
		}

		buffer_cache_map
				.putAll(initS1uOutputMap(filledS1uDir, fileBufferQueue));

		LteMain.FILE_EXEC.execute(file_output);
		for (FileBufferCache cache : buffer_cache_map.values()) {
			LteMain.FILE_EXEC.execute(cache);
		}
		LteMain.SCHEDULER.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				for (FileBufferCache cache : buffer_cache_map.values()) {
					cache.reflush();
				}
			}
		}, 1, 1, TimeUnit.MINUTES);

		LteMain.SCHEDULER.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				log();
				file_output.log(1000 * 10);
			}

		}, 10, 10, TimeUnit.SECONDS);

	}

	protected void log() {
		long num = count.get();
		count.set(0);
		StringBuilder sb = new StringBuilder("reset num:" + num * 1000 / 10000
				+ ",");
		for (FileBufferCache bc : buffer_cache_map.values()) {
			SdtpFileBufferCache sbc = (SdtpFileBufferCache) bc;
			sb.append(sbc.Interface + ":" + sbc.queue.size() + ",");
		}
		logger.info("cache_all_size:" + sb.toString());
	}

	protected Map<String, FileBufferCache> initS1uOutputMap(
			String filledS1uDir, ArrayBlockingQueue<FileBuffer> fileBufferQueue) {
		Map<String, FileBufferCache> result = new HashMap<>();
		for (short key : SdtpConstants.S1U_APP_TYPE_CODE_MAP.keySet()) {
			String appTypeCode = SdtpConstants.S1U_APP_TYPE_CODE_MAP.get(key);
			int queueSize = 1000;
			if(key == SdtpConstants.XDRType.HTTP){
				queueSize = 500000;
			}else if(key == SdtpConstants.XDRType.XDR_BUSINESS){
				queueSize = 500000;
			}else{
			}
			result.put(
					"app_" + key,
					new SdtpFileBufferCache(filledS1uDir + File.separator + S1U
							+ "_" + appTypeCode, SdtpConfig
							.getFileOutputConfigs(S1U).max_memory_size,
							SdtpConfig.getFileOutputConfigs(S1U).expire_time,
							fileBufferQueue,queueSize, S1U, key));
		}
		return result;
	}

}
