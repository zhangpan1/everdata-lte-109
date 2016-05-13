package com.eversec.lte.processor.data;

import java.util.concurrent.ArrayBlockingQueue;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;

/**
 * 
 * @author bieremayi
 * 
 */
public class QueueData {

	/**
	 * 队列容量
	 */
	public static int QUEUE_CAPACITY;

	/**
	 * 保存原始XDR字节数组队列
	 */
	public static ArrayBlockingQueue<NotifyXDRDataReq> ORIGINAL_XDR_SDTP_OUTPUT_QUEUE;

	/**
	 * 保存原始码流字节数组队列
	 */
	public static ArrayBlockingQueue<XDRRawDataSendReq> ORIGINAL_RAW_SDTP_OUTPUT_QUEUE;

	/**
	 * 保存原始合成XDR字节数组队列
	 */
	public static ArrayBlockingQueue<XDRRawDataSendReq> ORIGINAL_CXDR_SDTP_OUTPUT_QUEUE;

	/**
	 * 保存原始XDR字节数组队列
	 */
	public static ArrayBlockingQueue<NotifyXDRDataReq> ORIGINAL_XDR_FILE_OUTPUT_QUEUE;
	
	/**
	 * 保存集团考核XDR字节数组队列
	 */
	public static ArrayBlockingQueue<NotifyXDRDataReq> EXAMINE_XDR_FILE_OUTPUT_QUEUE;

	/**
	 * 保存原始合成XDR字节数组队列
	 */
	public static ArrayBlockingQueue<NotifyXDRDataReq> ORIGINAL_CXDR_FILE_OUTPUT_QUEUE;

	/**
	 * 保存原始码流字节数组队列
	 */
	public static ArrayBlockingQueue<XDRRawDataSendReq> ORIGINAL_RAW_FILE_OUTPUT_QUEUE;

	/**
	 * 保存原始XDR字节数组队列
	 */
	public static ArrayBlockingQueue<NotifyXDRDataReq> ORIGINAL_XDR_KAFKA_OUTPUT_QUEUE;

	/**
	 * 保存原始码流字节数组队列
	 */
	public static ArrayBlockingQueue<XDRRawDataSendReq> ORIGINAL_RAW_KAFKA_OUTPUT_QUEUE;

	/**
	 * 待处理XDR字节数组队列
	 */
	public static ArrayBlockingQueue<byte[]> PROCESS_XDR_DATA_QUEUE;

	/**
	 * 保存发送至模拟应用层的字节数组队列(包含合成xdr,单接口xdr)
	 */
	public static ArrayBlockingQueue<NotifyXDRDataReq> MIX_XDR_FILE_OUTPUT_QUEUE;

	/**
	 * 保存单接口uemr xdr数据队列
	 */
	public static ArrayBlockingQueue<XdrSingleSourceUEMR> UEMR_QUEUE;

	/**
	 * 存储需要重新尝试回填话单的队列
	 */
	public static ArrayBlockingQueue<XdrSingleSource> PENDING_XDR_DATA_QUEUE = new ArrayBlockingQueue<>(
			SdtpConfig.getDataQueueCapacity());
	
	/**
	 * 原始xdr统计队列
	 */
	public static ArrayBlockingQueue<NotifyXDRDataReq> ORIGINAL_XDR_EMPTY_OUTPUT_QUEUE;

	static {
		QUEUE_CAPACITY = SdtpConfig.getDataQueueCapacity();
		ORIGINAL_XDR_SDTP_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);
		ORIGINAL_RAW_SDTP_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);
		ORIGINAL_CXDR_SDTP_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);

		ORIGINAL_XDR_FILE_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);
		EXAMINE_XDR_FILE_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);
				
		ORIGINAL_CXDR_FILE_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);
		ORIGINAL_RAW_FILE_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);

		ORIGINAL_XDR_KAFKA_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);
		ORIGINAL_RAW_KAFKA_OUTPUT_QUEUE = new ArrayBlockingQueue<>(
				QUEUE_CAPACITY);
		
		ORIGINAL_XDR_EMPTY_OUTPUT_QUEUE = new  ArrayBlockingQueue<>(
				QUEUE_CAPACITY); 

		PROCESS_XDR_DATA_QUEUE = new ArrayBlockingQueue<>(QUEUE_CAPACITY );
		MIX_XDR_FILE_OUTPUT_QUEUE = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
		UEMR_QUEUE = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
	}
	
	public static void main(String[] args) throws Exception {
		PROCESS_XDR_DATA_QUEUE.put(new byte[0]);
	}
}
