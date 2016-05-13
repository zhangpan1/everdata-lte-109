package com.eversec.lte.sdtp.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.utils.FormatUtils;

/**
 * output -> find type arrayBlock -> threads each have a buffer IoBuffer -> 1
 * arrayblock -> 1thread file output
 * 
 * @author lirongzhi
 * 
 */
public class ExamineFileOutputTools extends FileOutputTools {

	private static ExamineFileOutputTools instance = new ExamineFileOutputTools();

	public static ExamineFileOutputTools getInstance() {
		return instance;
	}

	public static void output(XdrSingleSource source) {
		if (SdtpConfig.IS_OUTPUT_EXAMINE_XDR_2_FILE) {
			if (SdtpConfig.IS_FILTER_OUTPUT_FILLED_2_SDTP) {
				String[] cityCodes = SdtpConfig.FILTER_OUTPUT_FILLED_2_SDTP_CITYCODE;
				String[] interfaces = SdtpConfig.FILTER_OUTPUT_FILLED_2_SDTP_INTERFACE;
				short Interface1 = source.getCommon().getInterface();
				String city = source.getCommon().getCity();
				if (!FormatUtils.arrayContans(cityCodes, city)
						|| !FormatUtils.arrayContans(interfaces, ""
								+ Interface1)) {
					return;
				}
			}

			ExamineFileOutputTools.getInstance().outputXdr(source);
		}

	}

	private ExamineFileOutputTools() {
		if (SdtpConfig.IS_OUTPUT_EXAMINE_XDR_2_FILE) {
			init();
		}
	}

	public void init() {
		String max_memory_size = SdtpConfig.getOutputFileSize();
		String expire_time = SdtpConfig.getOutputFilePeriod();
		String examineDir = SdtpConfig.getExamineDir();

		String[] SIGS = { S6A, SGS, S1MME, S11 };
		String[] SCAS = { UU, X2, UEMR, CELLMR };
		String[] VOLTES = {SIP,SV,DIA,RX,HARASS};
		file_output = new ExamineFileBufferOutput();

		ArrayBlockingQueue<FileBuffer> fileBufferQueue = file_output
				.getFileBufferQueue();

		for (String SIG : SIGS) {
			buffer_cache_map.put(SIG, new SdtpFileBufferCache(examineDir,
					max_memory_size, expire_time, fileBufferQueue, SIG, 0));
		}

		for (String SCA : SCAS) {
			buffer_cache_map.put(SCA, new SdtpFileBufferCache(examineDir,
					max_memory_size, expire_time, fileBufferQueue, SCA, 0));
		}
		for (String VOLTE:VOLTES) {
			buffer_cache_map.put(VOLTE,new SdtpFileBufferCache(examineDir,
					max_memory_size, expire_time, fileBufferQueue, VOLTE, 0));
		}
		buffer_cache_map.putAll(initS1uOutputMap(examineDir, max_memory_size,
				expire_time, fileBufferQueue));
		//线程初始化
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

	protected Map<String, FileBufferCache> initS1uOutputMap(
			String examineS1uDir, String max_memory_size, String expire_time,
			ArrayBlockingQueue<FileBuffer> fileBufferQueue) {
		Map<String, FileBufferCache> result = new HashMap<>();
		for (short key : SdtpConstants.S1U_APP_TYPE_CODE_MAP.keySet()) {
			result.put("app_" + key, new SdtpFileBufferCache(examineS1uDir,
					max_memory_size, expire_time, fileBufferQueue, S1U, key));
		}
		return result;
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

}
