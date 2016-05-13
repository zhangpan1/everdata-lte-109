package com.eversec.lte.sdtp.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;

/**
 * output -> find type arrayBlock -> threads each have a buffer IoBuffer -> 1
 * arrayblock -> 1thread file output
 * 
 * support grouping by city and imsi last num
 * 
 * @author lirongzhi
 * 
 */
public class SdtpFileGroupingOutputTools extends FileOutputTools {
	// private static

	private static SdtpFileGroupingOutputTools instance = new SdtpFileGroupingOutputTools();

	public static SdtpFileGroupingOutputTools getInstance() {
		return instance;
	}

	public static void output(XdrSingleSource xdr) {
		SdtpFileGroupingOutputTools.getInstance().outputXdr(xdr);
	}

	protected SdtpFileGroupingOutputTools() {

		if ( (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_FILE
				|| SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE
				|| SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE)
				&& SdtpConfig.IS_USE_GROUPING_OUTPUT) {
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
	
	SdtpFileGroupingBufferOutput file_outputs[] ;

	public void init() {
		String filledScaDir = ScaDir();
		String filledSigDir = SigDir();
		String filledS1uDir = S1uDir();

		String[] SIGS = { S6A, SGS, S1MME, S11 };
		String[] SCAS = { UU, X2, UEMR, CELLMR };

//		file_output = new SdtpFileGroupingBufferOutput();
		file_outputs = new SdtpFileGroupingBufferOutput[]{
				new SdtpFileGroupingBufferOutput(),
				new SdtpFileGroupingBufferOutput(),
				new SdtpFileGroupingBufferOutput(),
		};
		

		ArrayBlockingQueue<FileBuffer> fileBufferQueue1 = file_outputs[0]
				.getFileBufferQueue();
		ArrayBlockingQueue<FileBuffer> fileBufferQueue2 = file_outputs[1]
				.getFileBufferQueue();
		ArrayBlockingQueue<FileBuffer> fileBufferQueue3 = file_outputs[2]
				.getFileBufferQueue();

		for (String SIG : SIGS) {
			buffer_cache_map.put(
					SIG,
					new SdtpFileGroupingBufferCache(filledSigDir
							+ File.separator + SIG, SdtpConfig
							.getFileOutputConfigs(SIG).max_memory_size,
							SdtpConfig.getFileOutputConfigs(SIG).expire_time,
							fileBufferQueue1, SIG, 0, null));
		}

		for (String SCA : SCAS) {
			buffer_cache_map.put(
					SCA,
					new SdtpFileGroupingBufferCache(filledScaDir
							+ File.separator + SCA, SdtpConfig
							.getFileOutputConfigs(SCA).max_memory_size,
							SdtpConfig.getFileOutputConfigs(SCA).expire_time,
							fileBufferQueue1, SCA, 0, null));
		}

		buffer_cache_map
				.putAll(initS1uOutputMap(filledS1uDir,    fileBufferQueue2,fileBufferQueue3));

		for(SdtpFileGroupingBufferOutput file_output:file_outputs ){
			
			LteMain.FILE_EXEC.execute(file_output);
		}
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
				for(SdtpFileGroupingBufferOutput file_output:file_outputs ){
					file_output.log(1000 * 10);
				}
//				file_output.log(1000 * 10);
			}

		}, 10, 10, TimeUnit.SECONDS);

	}

	protected void log() {
		long num = count.get();
		count.set(0);
		StringBuilder sb = new StringBuilder("reset num:" + num * 1000 / 10000
				+ ",");
		logger.info("file output cache_all_size:" + sb.toString() + buffer_cache_map.values());
	}

	//是否需要做自动平衡？？？
	protected Map<String, FileBufferCache> initS1uOutputMap(
			String filledS1uDir, ArrayBlockingQueue<FileBuffer>  fileBufferQueue1,ArrayBlockingQueue<FileBuffer> fileBufferQueue2) {
		Map<String, FileBufferCache> result = new HashMap<>();
		for (short key : SdtpConstants.S1U_APP_TYPE_CODE_MAP.keySet()) {
			String appTypeCode = SdtpConstants.S1U_APP_TYPE_CODE_MAP.get(key);
			int queueSize = 1000;
			ArrayBlockingQueue<FileBuffer>  fileBufferQueue = null;
			if(key == SdtpConstants.XDRType.HTTP){
				queueSize = 500000;
				fileBufferQueue = fileBufferQueue1;
			}else if(key == SdtpConstants.XDRType.XDR_BUSINESS){
				queueSize = 500000;
				fileBufferQueue = fileBufferQueue2;
			}else{
				fileBufferQueue = fileBufferQueue2;
			}
			result.put("app_" + key, new SdtpFileGroupingBufferCache(
					filledS1uDir + File.separator + S1U + "_" + appTypeCode,
					SdtpConfig.getFileOutputConfigs(S1U).max_memory_size,
					SdtpConfig.getFileOutputConfigs(S1U).expire_time,
					fileBufferQueue,queueSize, S1U, key,
					SdtpConstants.S1U_APP_TYPE_CODE_MAP.get(key)));
		}
		return result;
	}

	public void outputXdr(XdrSingleSource xdr) {

		long time = xdr.getProduceStartTime();
		short Interface = -1;
		int apptype = -1;
		String city = null;
		String imsi = null;
		if (xdr instanceof XdrSingleSourceS1U) {
			XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) xdr;
			Interface = s1u.getS1uCommon().getInterface();
			apptype = s1u.getBusinessCommon().getAppTypeCode();
			city = s1u.getS1uCommon().getCity();
			imsi = s1u.getMobileCommon().getImsi();

		} else {
			Interface = xdr.getCommon().getInterface();
			city = xdr.getCommon().getCity();
			imsi = xdr.getCommon().getImsi();
		}

		StringBuilder sb = new StringBuilder();
		String[] strs = xdr.toStringArr();
		for (String str : strs) {
			sb.append(str);
			sb.append("|");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}

		SdtpFileGroupingOutputItem data = new SdtpFileGroupingOutputItem(
				sb.toString(), time, Interface, apptype, city, imsi);
		try {
			switch (Interface) {
			case SdtpConstants.XDRInterface.UU:
				buffer_cache_map.get(UU).putData(data);
				break;
			case SdtpConstants.XDRInterface.X2:
				buffer_cache_map.get(X2).putData(data);
				break;
			case SdtpConstants.XDRInterface.UE_MR:
				buffer_cache_map.get(UEMR).putData(data);
				break;
			case SdtpConstants.XDRInterface.CELL_MR:
				buffer_cache_map.get(CELLMR).putData(data);
				break;
			case SdtpConstants.XDRInterface.S1MME:
				buffer_cache_map.get(S1MME).putData(data);
				break;
			case SdtpConstants.XDRInterface.S6A:
				buffer_cache_map.get(S6A).putData(data);
				break;
			case SdtpConstants.XDRInterface.S11:
				buffer_cache_map.get(S11).putData(data);
				break;
			case SdtpConstants.XDRInterface.SGS:
				buffer_cache_map.get(SGS).putData(data);
				break;
			case SdtpConstants.XDRInterface.S1U:
				buffer_cache_map.get("app_" + apptype).putData(data);
				break;
			default:
				break;
			}

			count.incrementAndGet();
		} catch (Exception e) {
			System.err.println(buffer_cache_map);
			System.err.println("Interface:" + Interface + ",apptype" + apptype);

			e.printStackTrace();
		}

	}

}
