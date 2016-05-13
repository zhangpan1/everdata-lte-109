package com.eversec.lte.processor.data;

import java.util.concurrent.atomic.AtomicLong;

import com.eversec.lte.cache.back.StaticCache;

/**
 * 统计值
 * 
 * @author bieremayi
 * 
 */
public class StaticData {

	/**
	 * 回填成功的XDR数
	 */
	public static final AtomicLong SUCC_FILL_COUNT = new AtomicLong();
	/**
	 * 回填失败的XDR数据
	 */
	public static final AtomicLong FAILING_FILL_COUNT = new AtomicLong();
	/**
	 * 重复pending key导致的xdr淘汰数
	 */
	public static final AtomicLong REPLACED_COUNT = new AtomicLong();
	/**
	 * 处理的XDR数
	 */
	public static final AtomicLong SOURCE_COUNT = new AtomicLong();
	/**
	 * 重新回填的次数
	 */
	public static final AtomicLong REFILL_COUNT = new AtomicLong();

	/**
	 * 包含完整的imsi,imei,msisdn等信息的xdr数
	 */
	public static final AtomicLong FULLINFO_COUNT = new AtomicLong();

	/**
	 * 接收到的XDR包数
	 */
	public static final AtomicLong XDR_RECEIVE_PACKAGE = new AtomicLong(0);
	/**
	 * 接收到的XDR字节数
	 */
	public static final AtomicLong XDR_RECEIVE_BYTES = new AtomicLong(0);
	/**
	 * 接收到的XDR条数
	 */
	public static final AtomicLong XDR_RECEIVE_COUNT = new AtomicLong(0);
	/**
	 * 接收到的原始码流包数
	 */
	public static final AtomicLong RAW_RECEIVE_PACKAGE = new AtomicLong(0);
	/**
	 * 接收到的原始码流字节数
	 */
	public static final AtomicLong RAW_RECEIVE_BYTES = new AtomicLong(0);
	/**
	 * 接收到的原始码流条数
	 */
	public static final AtomicLong RAW_RECEIVE_COUNT = new AtomicLong(0);

	/**
	 * 发送至sdtp的xdr包数
	 */
	public static final AtomicLong XDR_2_SDTP_PACKAGE = new AtomicLong(0);

	/**
	 * 发送至sdtp的原始码流包数
	 */
	public static final AtomicLong RAW_2_SDTP_PACKAGE = new AtomicLong(0);

	/**
	 * 发送至sdtp的xdr字节数
	 */
	public static final AtomicLong XDR_2_SDTP_BYTES = new AtomicLong(0);

	/**
	 * 发送至sdtp的原始码流字节数
	 */
	public static final AtomicLong RAW_2_SDTP_BYTES = new AtomicLong(0);

	/**
	 * 发送至kafka的xdr包数
	 */
	public static final AtomicLong XDR_2_KAFKA_PACKAGE = new AtomicLong();

	/**
	 * 发送至sdtp的xdr条数
	 */
	public static final AtomicLong XDR_2_KAFKA_COUNT = new AtomicLong();

	/**
	 * 发送至sdtp的xdr字节数
	 */
	public static final AtomicLong XDR_2_KAFKA_BYTES = new AtomicLong();

	/**
	 * 发送至sdtp的原始码流包数
	 */
	public static final AtomicLong RAW_2_KAFKA_PACKAGE = new AtomicLong();

	/**
	 * 发送至sdtp的原始码流条数
	 */
	public static final AtomicLong RAW_2_KAFKA_COUNT = new AtomicLong();

	/**
	 * 发送至sdtp的原始码流字节数
	 */
	public static final AtomicLong RAW_2_KAFKA_BYTES = new AtomicLong();

	/**
	 * 统计xdr接口类型缓存
	 */
	public static final StaticCache XDR_TYPE_CACHE = new StaticCache();

	/**
	 * 统计xdr流程类型缓存
	 */
	public static final StaticCache XDR_PROCEDURE_TYPE_CACHE = new StaticCache();

	/**
	 * 统计s1u apptype缓存
	 */
	public static final StaticCache S1U_APPTYPE_CACHE = new StaticCache();

	/**
	 * 统计合成xdr类型缓存
	 */
	public static final StaticCache CXDR_TYPE_CACHE = new StaticCache();
	
	/**
	 * 统计合成信令xdr具体类型
	 */
	public static final StaticCache CXDR_SIGNALING_CACHE= new StaticCache();
	
	/**
	 *  丢去原始码流包数
	 */
	public static final AtomicLong ABANDON_RAW_COUNT = new AtomicLong();
	
	/**
	 *  丢去原始码流包数
	 */
	public static final AtomicLong ABANDON_XDR_COUNT = new AtomicLong();
	
	/**
	 *  丢弃发往app的s1u数量
	 */
	public static final AtomicLong ABANDON_S1U_XDR_2_SDTP_COUNT = new AtomicLong();

}
