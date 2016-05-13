package com.eversec.lte.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.eversec.lte.vo.HostAndPortVo;

/**
 * 
 * @author bieremayi
 * 
 */
public class SdtpConfig {

	private static final String CONFIG = "sdtp.properties";
	public static String PROVINCE_CODE;
	public static int OPERATOR_CODE;
	public static final String UNKNOW = "unknow";

	public static boolean HAS_TA;
	public static boolean IS_SAVE_TO_REDIS;
	public static boolean IS_GET_FORM_REDIS;
	public static boolean IS_BACKFILL;
	public static boolean IS_COMPOUND_UEMR;
	public static int MAX_TRY_NUM;
	public static String LIST_DELIMITER = ",";

	public static boolean IS_OUTPUT_EXAMINE_XDR_2_FILE;

	public static boolean IS_OUTPUT_ORIGINAL_XDR_2_FILE;
	public static boolean IS_OUTPUT_ORIGINAL_XDR_2_KAFKA;
	public static boolean IS_OUTPUT_ORIGINAL_XDR_2_SDTP;

	public static boolean IS_OUTPUT_FILLED_XDR_2_FILE;
	public static boolean IS_OUTPUT_FILLED_XDR_2_KAFKA;
	public static boolean IS_OUTPUT_FILLED_XDR_2_SDTP;

	public static boolean IS_OUTPUT_ORIGINAL_RAW_2_FILE;
	public static boolean IS_OUTPUT_ORIGINAL_RAW_2_KAFKA;
	public static boolean IS_OUTPUT_ORIGINAL_RAW_2_SDTP;

	public static boolean IS_OUTPUT_ORIGINAL_CXDR_2_FILE;
	public static boolean IS_OUTPUT_ORIGINAL_CXDR_2_SDTP;
	public static boolean IS_OUTPUT_ORIGINAL_CXDR_2_KAFKA;

	public static boolean IS_OUTPUT_CUSTOM_XDR_2_KAFKA;

	public static boolean IS_OUTPUT_ORIGINAL_XDR_2_EMPTY;

	public static boolean IS_RESPONESE;

	public static boolean IS_FILL_S11 = true;

	public static boolean IS_APPLICATION_LAYER = false;

	public static boolean IS_BACKFILL_PROCESS_BATCH = false;

	public static Properties PROPERTIES = new Properties();

	/**
	 * 是否处理原始码流
	 */
	public static volatile boolean IS_PROCESS_RAW = true;

	public static volatile boolean IS_PROCESS_XDR = true;

	/**
	 * 是否发送回填处理后的s1u话单至sdtp
	 */
	public static volatile boolean IS_OUTPUT_FILLED_S1U_2_SDTP = true;

	/**
	 * sdtp 过滤发送
	 */
	public static volatile boolean IS_FILTER_OUTPUT_FILLED_2_SDTP = true;

	public static volatile String[] FILTER_OUTPUT_FILLED_2_SDTP_CITYCODE;

	public static volatile String[] FILTER_OUTPUT_FILLED_2_SDTP_INTERFACE;

	public static volatile boolean IS_OUTPUT_XDR_FOR_STAT = true; // 为统计增加

	public static volatile boolean IS_OUTPUT_XDR_FOR_COMP = true;

	// public static volatile boolean IS_REFILL_CITY_CODE = false;

	public static volatile boolean IS_EXAMINE_SKIP_START_POS = false;

	public static volatile boolean IS_USE_NEW_EXAMINE = false;

	public static Map<String, FileOutputConfig> FILE_OUTPUT_CONFIGS = new HashMap<String, SdtpConfig.FileOutputConfig>();
	public static volatile boolean IS_OUTPUT_ORIGINAL_RAW_2_PCAP;

	public static volatile boolean IS_USE_GROUPING_OUTPUT = false;

	static {
		try {
			ClassLoader classLoader = SdtpConfig.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream(CONFIG);
			if (is != null) {
				PROPERTIES.load(is);
				PROVINCE_CODE = getProvinceCode();
				OPERATOR_CODE = getOperatorCode();
				IS_SAVE_TO_REDIS = isSaveToRedis();
				IS_GET_FORM_REDIS = isGetFromRedis();
				IS_BACKFILL = SdtpConfig.isBackFill();
				IS_COMPOUND_UEMR = SdtpConfig.isCompoundUemr();
				MAX_TRY_NUM = getMaxTry();
				LIST_DELIMITER = getListDelimiter();

				IS_OUTPUT_EXAMINE_XDR_2_FILE = isOutputExamineXdr2file();

				IS_EXAMINE_SKIP_START_POS = isExamineSkipStartPos();

				IS_USE_NEW_EXAMINE = isUseNewExamine();

				IS_OUTPUT_ORIGINAL_XDR_2_FILE = isOutputOriginalXdr2File();
				IS_OUTPUT_ORIGINAL_XDR_2_KAFKA = isOutputOriginalXdr2Kafka();
				IS_OUTPUT_ORIGINAL_XDR_2_SDTP = isOutputOriginalXdr2Sdtp();
				IS_OUTPUT_FILLED_XDR_2_FILE = isOutputFilledXdr2File();
				IS_OUTPUT_FILLED_XDR_2_KAFKA = isOutputFilledXdr2Kafka();
				IS_OUTPUT_FILLED_XDR_2_SDTP = isOutputFilledXdr2Sdtp();
				IS_OUTPUT_ORIGINAL_RAW_2_FILE = isOutputOriginalRaw2File();
				IS_OUTPUT_ORIGINAL_RAW_2_KAFKA = isOutputOriginalRaw2Kafka();
				IS_OUTPUT_ORIGINAL_RAW_2_SDTP = isOutputOriginalRaw2Sdtp();
				IS_OUTPUT_ORIGINAL_CXDR_2_FILE = isOutputOriginalCXdr2File();
				IS_OUTPUT_ORIGINAL_CXDR_2_SDTP = isOutputOriginalCXdr2Sdtp();
				IS_OUTPUT_ORIGINAL_CXDR_2_KAFKA = isOutputOriginalCXdr2Kafka();
				IS_OUTPUT_CUSTOM_XDR_2_KAFKA = isOutputCustomXdr2Kafka();
				IS_OUTPUT_ORIGINAL_XDR_2_EMPTY = isOutputFilledXdr2Empty();
				IS_RESPONESE = isResponse();
				IS_FILL_S11 = isFillS11();
				HAS_TA = hasTa();
				IS_APPLICATION_LAYER = isApplicationLayer();

				IS_OUTPUT_XDR_FOR_STAT = isOutputXdrForStat();

				IS_OUTPUT_XDR_FOR_COMP = isOutputXdrForComp();

				FILE_OUTPUT_CONFIGS = initFileOutputConfigs();

				// IS_REFILL_CITY_CODE = initIsRefillCityCode();

				IS_BACKFILL_PROCESS_BATCH = SdtpConfig.isBackFillProcessBatch();

				IS_FILTER_OUTPUT_FILLED_2_SDTP = SdtpConfig
						.isFilterOutputFilled2Sdtp();

				FILTER_OUTPUT_FILLED_2_SDTP_CITYCODE = filterOutputFilled2SdtpCityCode();

				FILTER_OUTPUT_FILLED_2_SDTP_INTERFACE = filterOutputFilled2SdtpInterface();

				IS_OUTPUT_ORIGINAL_RAW_2_PCAP = isOutputRaw2Pcap();

				IS_USE_GROUPING_OUTPUT = isUseGroupingOutput();

				System.out.println(PROPERTIES);
			} else {
				System.err.println("file not found!");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 账号ID
	 * 
	 * @return
	 */
	public static String getLoginID() {
		String loginID = PROPERTIES.getProperty("loginID", "sdtp");
		return loginID;
	}

	/**
	 * 共享密钥
	 * 
	 * @return
	 */
	public static String getPassword() {
		return PROPERTIES.getProperty("password", "123456");
	}

	/**
	 * sdtp版本号
	 * 
	 * @return
	 */
	public static int getSdtpVersion() {
		return Integer.parseInt(PROPERTIES.getProperty("version", "3"));
	}

	/**
	 * sdtp子版本号
	 * 
	 * @return
	 */
	public static int getSdtpSubVersion() {
		return Integer.parseInt(PROPERTIES.getProperty("sub_version", "0"));
	}

	/**
	 * SDTP服务器端监听端口
	 * 
	 * @return
	 */
	public static int getSdtpListeningPort() {
		return Integer.parseInt(PROPERTIES
				.getProperty("listening_port", "9999"));
	}

	/**
	 * 是否响应
	 * 
	 * @return
	 */
	public static boolean isResponse() {
		String ret = PROPERTIES.getProperty("is_response", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	// /**
	// * SDTP客户端发送IP
	// *
	// * @return
	// */
	// public static String getSdtpServerIp() {
	// return PROPERTIES.getProperty("server_ip", "192.168.200.144");
	// }
	//
	// /**
	// * SDTP客户端发送端口
	// *
	// * @return
	// */
	// public static int getSdtpServerPort() {
	// return Integer.parseInt(PROPERTIES.getProperty("server_port", "9999"));
	// }

	/**
	 * 是否回填操作
	 * 
	 * @return
	 */
	public static boolean isBackFill() {
		String ret = PROPERTIES.getProperty("is_backfill", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 关联缓存时间配置，（默认10s）
	 * 
	 * @return
	 */
	public static String getPendingTTL() {
		return PROPERTIES.getProperty("pending_ttl", "10s");
	}

	/**
	 * 关联缓存最大容量配置，（默认500000）
	 * 
	 * @return
	 */
	public static int getPendingSize() {
		return Integer
				.valueOf(PROPERTIES.getProperty("pending_size", "500000"));
	}

	/**
	 * userIpv4_sgwTeid规则保存时间（单位：秒,默认:3600秒）
	 * 
	 * @return
	 */
	public static int getRule0TTLSecond() {
		return Integer.valueOf(PROPERTIES.getProperty("rule0_ttl_second",
				"3600"));
	}

	/**
	 * mmeUeS1apID_mmeGroupID_mmeCode规则保存时间（单位：秒,默认:3600秒）
	 * 
	 * @return
	 */
	public static int getRule1TTLSecond() {
		return Integer.valueOf(PROPERTIES.getProperty("rule1_ttl_second",
				"3600"));
	}

	/**
	 * mmeGroupID_mmeCode_mTmsi规则保存时间（单位：秒,默认:3600秒）
	 * 
	 * @return
	 */
	public static int getRule2TTLSecond() {
		return Integer.valueOf(PROPERTIES.getProperty("rule2_ttl_second",
				"3600"));
	}

	/**
	 * mmeUeS1apID规则保存时间（单位：秒,默认:3600秒）
	 * 
	 * @return
	 */
	public static int getRule4TTLSecond() {
		return Integer.valueOf(PROPERTIES.getProperty("rule4_ttl_second",
				"3600"));
	}

	/**
	 * 最大关联尝试次数（默认1）
	 * 
	 * @return
	 */
	public static int getMaxTry() {
		int maxTry = 1;
		String str = PROPERTIES.getProperty("max_try_num");
		if (str != null && str.trim().length() > 0) {
			try {
				maxTry = Integer.valueOf(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return maxTry;
	}

	/**
	 * 队列容量,默认50万
	 * 
	 * @return
	 */
	public static int getDataQueueCapacity() {
		return Integer.parseInt(PROPERTIES.getProperty("data_queue_capacity",
				"500000"));
	}

	/**
	 * redis的host,port配置
	 * 
	 * @return
	 */
	public static List<HostAndPortVo> getRedisHostAndPort() {
		String str = PROPERTIES.getProperty("redis_hostandport");
		List<HostAndPortVo> ret = new ArrayList<>();
		if (str != null && str.trim().length() > 0) {
			String[] arrs = str.split(",");
			for (String arr : arrs) {
				if (arr.contains(":")) {
					String[] tmp = arr.split(":", 2);
					ret.add(new HostAndPortVo(tmp[0], Integer.valueOf(tmp[1])));
				}
			}
		} else {
			System.err.println("redis_hostandport config not found.");
		}
		return ret;
	}

	/**
	 * elasticsearch river的host,port配置
	 * 
	 * @return
	 */
	public static List<HostAndPortVo> getRiverHostAndPorts() {
		String str = PROPERTIES.getProperty("river_hostandport");
		List<HostAndPortVo> ret = new ArrayList<>();
		if (str != null && str.trim().length() > 0) {
			String[] arrs = str.split(",");
			for (String arr : arrs) {
				if (arr.contains(":")) {
					String[] tmp = arr.split(":", 2);
					ret.add(new HostAndPortVo(tmp[0], Integer.valueOf(tmp[1])));
				}
			}
		} else {
			System.err.println("river_hostandport config not found.");
		}
		return ret;
	}

	/**
	 * elasticsearch river的host,port配置
	 * 
	 * @return
	 */
	public static HostAndPortVo getRiverHostAndPort() {
		List<HostAndPortVo> list = getRiverHostAndPorts();
		HostAndPortVo ret = new HostAndPortVo();
		if (list != null && list.size() > 0) {
			ret = list.get(0);
		}
		return ret;
	}

	/**
	 * xdr data req处理线程数（默认20）
	 * 
	 * @return
	 */
	public static int getXdrProcessThread() {
		return Integer.valueOf(PROPERTIES.getProperty("xdr_process_thread",
				"20"));
	}

	/**
	 * xdr raw data req处理线程数(默认20)
	 * 
	 * @return
	 */
	public static int getRawProcessThread() {
		return Integer.valueOf(PROPERTIES.getProperty("raw_process_thread",
				"20"));
	}

	/**
	 * 获取省份code
	 * 
	 * @return
	 */
	public static String getProvinceCode() {
		return PROPERTIES.getProperty("province_code", "0");
	}

	/**
	 * 获取运营商code，移动：1，联通：2，电信：3
	 * 
	 * @return
	 */
	public static Integer getOperatorCode() {
		return Integer.valueOf(PROPERTIES.getProperty("operator_code", "1"));
	}

	/**
	 * 是否压缩
	 * 
	 * @return
	 */
	public static boolean isCompress() {
		String ret = PROPERTIES.getProperty("is_compress", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 单接口原始数据输出目录
	 * 
	 * @return
	 */
	public static String getSourceDir() {
		return PROPERTIES.getProperty("source_dir", "/home/river/source");
	}

	/**
	 * 单接口回填后数据输出目录
	 * 
	 * @return
	 */
	public static String getSourceScaDir() {
		return PROPERTIES.getProperty("source_sca_dir", "/home/eversec/sca");
	}

	/**
	 * 单接口回填后数据输出目录
	 * 
	 * @return
	 */
	public static String getSourceSigDir() {
		return PROPERTIES.getProperty("source_sig_dir", "/home/eversec/sig");
	}

	/**
	 * 单接口回填后数据输出目录
	 * 
	 * @return
	 */
	public static String getSourceS1uDir() {
		return PROPERTIES.getProperty("source_s1u_dir", "/home/eversec/s1u");
	}

	/**
	 * 单接口回填后数据输出目录
	 * 
	 * @return
	 */
	@Deprecated
	public static String getFilledDir() {
		return PROPERTIES.getProperty("filled_dir", "/home/river/filled");
	}

	/**
	 * 单接口回填后数据输出目录
	 * 
	 * @return
	 */
	public static String getFilledScaDir() {
		return PROPERTIES.getProperty("filled_sca_dir", "/home/eversec/sca");
	}

	/**
	 * 单接口回填后数据输出目录
	 * 
	 * @return
	 */
	public static String getFilledSigDir() {
		return PROPERTIES.getProperty("filled_sig_dir", "/home/eversec/sig");
	}

	/**
	 * 单接口回填后数据输出目录
	 * 
	 * @return
	 */
	public static String getFilledS1uDir() {
		return PROPERTIES.getProperty("filled_s1u_dir", "/home/eversec/s1u");
	}

	/**
	 * 文件输出大小阈值
	 * 
	 * @return
	 */
	public static String getOutputFileSize() {
		return PROPERTIES.getProperty("output_file_size", "200m");
	}

	/**
	 * 文件生成周期
	 * 
	 * @return
	 */
	public static String getOutputFilePeriod() {
		return PROPERTIES.getProperty("output_file_period", "5m");
	}

	/**
	 * 信令分析设备编码
	 * 
	 * @return
	 */
	public static String getPID() {
		return PROPERTIES.getProperty("pid", "001");
	}

	/**
	 * 设置cityCode
	 * 
	 * @return
	 */
	public static String getCitycode() {
		return PROPERTIES.getProperty("citycode", "100");
	}

	/**
	 * 是否将规则保存到redis
	 * 
	 * @return
	 */
	public static boolean isSaveToRedis() {
		String ret = PROPERTIES.getProperty("is_save_to_redis", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否从redis获取规则
	 * 
	 * @return
	 */
	public static boolean isGetFromRedis() {
		String ret = PROPERTIES.getProperty("is_get_from_redis", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否只启用redis缓存
	 * 
	 * @return
	 */
	public static boolean isBackFillOnlyRedis() {
		String ret = PROPERTIES.getProperty("is_backfill_only_redis", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * s1mme输出文件线程数，默认1
	 * 
	 * @return
	 */
	public static int getS1mmeOutputThread() {
		return Integer.valueOf(PROPERTIES.getProperty("s1mme_output_thread",
				"1"));
	}

	/**
	 * s11输出文件线程数,默认1
	 * 
	 * @return
	 */
	public static int getS11OutputThread() {
		return Integer
				.valueOf(PROPERTIES.getProperty("s11_output_thread", "1"));
	}

	/**
	 * s1u输出文件线程数,默认1
	 * 
	 * @return
	 */
	public static int getS1UOutputThread() {
		return Integer
				.valueOf(PROPERTIES.getProperty("s1u_output_thread", "1"));
	}

	/**
	 * stat输出目录
	 * 
	 * @return
	 */
	public static String getStatDir() {
		return PROPERTIES.getProperty("stat_dir", "/home/eversec/stat");
	}

	/**
	 * stat周期
	 * 
	 * @return
	 */
	public static String getStatPeriod() {
		return PROPERTIES.getProperty("stat_period", "1m,5m,1h,1d");
	}

	/**
	 * 是否合成话单
	 * 
	 * @return
	 */
	public static boolean isCompoundUemr() {
		String ret = PROPERTIES.getProperty("is_compound_uemr", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 合成xdr输出目录
	 * 
	 * @return
	 */
	public static String getCompoundDir() {
		return PROPERTIES.getProperty("compound_dir", "/home/eversec/hecheng");
	}

	/**
	 * 是否配置mina参数
	 * 
	 * @return
	 */
	public static boolean isConfigMina() {
		String ret = PROPERTIES.getProperty("is_config_mina", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * mina read buffer
	 * 
	 * @return
	 */
	public static int getReadBufferSize() {
		return Integer.valueOf(PROPERTIES.getProperty("read_buffer_size",
				"1024"));
	}

	/**
	 * mina send buffer
	 * 
	 * @return
	 */
	public static int getSendBufferSize() {
		return Integer.valueOf(PROPERTIES.getProperty("send_buffer_size",
				"1024"));
	}

	/**
	 * mina receive buffer
	 * 
	 * @return
	 */
	public static int getReceiveBufferSize() {
		return Integer.valueOf(PROPERTIES.getProperty("receive_buffer_size",
				"1024"));
	}

	/**
	 * kafka xdr consumer线程
	 * 
	 * @return
	 */
	public static int getKafkaXdrConsumerThread() {
		return Integer.valueOf(PROPERTIES.getProperty(
				"kafka_xdr_consumer_thread", "1"));
	}

	/**
	 * kafka xdr consumer的host,port
	 * 
	 * @return
	 */
	public static String getKafkaConsumerHostAndPort() {
		return PROPERTIES.getProperty("kafka_xdr_consumer_host_port",
				"192.168.200.127:2181");
	}

	/**
	 * kafka raw consumer线程
	 * 
	 * @return
	 */
	public static int getKafkaRawConsumerThread() {
		return Integer.valueOf(PROPERTIES.getProperty(
				"kafka_raw_consumer_thread", "5"));
	}

	/**
	 * kafka raw consumer的host,port
	 * 
	 * @return
	 */
	public static String getKafkaRawConsumerHostAndPort() {
		return PROPERTIES.getProperty("kafka_raw_consumer_host_port",
				"192.168.200.127:2181");
	}

	/**
	 * zookeeper 超时时间
	 * 
	 * @return
	 */
	public static String getZookeeperTimeout() {
		return PROPERTIES.getProperty("zookeeper_timeout", "60000");
	}

	/**
	 * kafka xdr consumer group id
	 * 
	 * @return
	 */
	public static String getKafkaXdrConsumerGroupId() {
		return PROPERTIES
				.getProperty("kafka_xdr_consumer_group_id", "group001");
	}

	/**
	 * kafka输出线程数(默认5)
	 * 
	 * @return
	 */
	public static int getKafkaOutputThread() {
		return Integer.valueOf(PROPERTIES.getProperty("kafka_output_thread",
				"5"));
	}

	/**
	 * kafka broker list
	 * 
	 * @return
	 */
	public static String getKafkaMetadataBrokerList() {
		return PROPERTIES.getProperty("kafka_metadata_broker_list",
				"192.168.200.126:9092");
	}

	/**
	 * http 统计端口(默认10250)
	 * 
	 * @return
	 */
	public static int getHttpPort() {
		int port = Integer
				.valueOf(PROPERTIES.getProperty("http_port", "10250"));
		if (port < 0 || port > 65535) {
			port = 10250;
		}
		return port;
	}

	/**
	 * xdr id 初始值
	 * 
	 * @return
	 */
	public static long getSeqInitialValue() {
		return Long.valueOf(PROPERTIES.getProperty("seq_initial_value", "0"));
	}

	/**
	 * sdtp client启动连接数量(默认10)
	 * 
	 * @return
	 */
	public static int getSdtpClientThread() {
		return Integer.valueOf(PROPERTIES.getProperty("sdtp_client_thread",
				"10"));
	}

	/**
	 * 输出文件list 分隔符
	 * 
	 * @return
	 */
	public static String getListDelimiter() {
		return PROPERTIES.getProperty("list_delimiter", "|");
	}

	/**
	 * 输出文件s1u app 分隔符
	 * 
	 * @return
	 */
	public static String getS1uAppDelimiter() {
		return PROPERTIES.getProperty("s1u_delimiter", "|");
	}

	/**
	 * 批量读取队列数据（默认：1000）
	 * 
	 * @return
	 */
	public static int getDrainMaxElements() {
		return Integer.valueOf(PROPERTIES.getProperty("drain_max_elements",
				"1000"));
	}

	/**
	 * 批量读取队列数据时，未取得数据情况下，线程的休眠时间（单位：毫秒，默认1）
	 * 
	 * @return
	 */
	public static long getDrainTaskSleepMills() {
		return Long.valueOf(PROPERTIES.getProperty("drain_task_sleep_mills",
				"1"));
	}

	/**
	 * 是否输出集团考核的文件格式
	 * 
	 * @return
	 */
	public static boolean isOutputExamineXdr2file() {
		String ret = PROPERTIES.getProperty("is_output_examine_xdr_2_file",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出原始XDR数据到文件
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalXdr2File() {
		String ret = PROPERTIES.getProperty("is_output_original_xdr_2_file",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出原始XDR数据到kafka
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalXdr2Kafka() {
		String ret = PROPERTIES.getProperty("is_output_original_xdr_2_kafka",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出原始XDR数据到sdtp
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalXdr2Sdtp() {
		String ret = PROPERTIES.getProperty("is_output_original_xdr_2_sdtp",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否解析出原始XDR数据，不做后续处理，用于统计
	 * 
	 * @return
	 */
	public static boolean isOutputFilledXdr2Empty() {
		String ret = PROPERTIES.getProperty("is_output_original_xdr_2_empty",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出回填处理后的XDR数据到文件
	 * 
	 * @return
	 */
	public static boolean isOutputFilledXdr2File() {
		String ret = PROPERTIES.getProperty("is_output_filled_xdr_2_file",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出回填处理后的XDR数据到kafka
	 * 
	 * @return
	 */
	public static boolean isOutputFilledXdr2Kafka() {
		String ret = PROPERTIES.getProperty("is_output_filled_xdr_2_kafka",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出回填处理后的XDR数据到sdtp
	 * 
	 * @return
	 */
	public static boolean isOutputFilledXdr2Sdtp() {
		String ret = PROPERTIES.getProperty("is_output_filled_xdr_2_sdtp",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出原始码流数据到文件
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalRaw2File() {
		String ret = PROPERTIES.getProperty("is_output_original_raw_2_file",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出原始码流数据到kafka
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalRaw2Kafka() {
		String ret = PROPERTIES.getProperty("is_output_original_raw_2_kafka",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出原始码流数据到sdtp
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalRaw2Sdtp() {
		String ret = PROPERTIES.getProperty("is_output_original_raw_2_sdtp",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否为模拟应用层
	 */
	public static boolean isApplicationLayer() {
		String ret = PROPERTIES.getProperty("is_application_layer", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否发送kafak kpi预统计信息
	 * 
	 * @return
	 */
	public static boolean isOutputXdrForStat() {
		String ret = PROPERTIES.getProperty("is_output_xdr_for_stat", "true");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否发送kafak comp合成信息
	 * 
	 * @return
	 */
	public static boolean isOutputXdrForComp() {
		String ret = PROPERTIES.getProperty("is_output_xdr_for_comp", "true");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出合成XDR数据到文件
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalCXdr2File() {
		String ret = PROPERTIES.getProperty("is_output_original_cxdr_2_file",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出合成XDR数据到sdtp
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalCXdr2Sdtp() {
		String ret = PROPERTIES.getProperty("is_output_original_cxdr_2_sdtp",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出合成XDR数据到kafka
	 * 
	 * @return
	 */
	public static boolean isOutputOriginalCXdr2Kafka() {
		String ret = PROPERTIES.getProperty("is_output_original_cxdr_2_kafka",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否输出自定义xdr到kafka
	 * 
	 * @return
	 */
	public static boolean isOutputCustomXdr2Kafka() {
		String ret = PROPERTIES.getProperty("is_output_custom_xdr_2_kafka",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 单接口xdr获取数据源（1为sdtp;2为kafka）
	 * 
	 * @return
	 */
	public static int getXdrSource() {
		return Integer.valueOf(PROPERTIES.getProperty("xdr_source", "1"));
	}

	/**
	 * 合成xdr获取数据源（1为sdtp;2为kafka）
	 * 
	 * @return
	 */
	public static int getCXdrSource() {
		return Integer.valueOf(PROPERTIES.getProperty("cxdr_source", "1"));
	}

	/**
	 * 原始码流获取数据源（1为sdtp;2为kafka）
	 * 
	 * @return
	 */
	public static int getRawSource() {
		return Integer.valueOf(PROPERTIES.getProperty("raw_source", "1"));
	}

	/**
	 * What to do when there is no initial offset in ZooKeeper or if an offset
	 * is out of range: smallest : automatically reset the offset to the
	 * smallest offset largest : automatically reset the offset to the largest
	 * offset anything else: throw exception to the consumer
	 * 
	 * @return
	 */
	public static String getKafkaConsumerAutoOffsetReset() {
		return PROPERTIES.getProperty("kafka_consumer_auto_offset_reset",
				"smallest");
	}

	/**
	 * <pre>
	 *  This value controls when a produce request is considered completed. Specifically, how many other brokers must have committed the data to their log and acknowledged this to the leader? Typical values are
	 *  
	 *  0, which means that the producer never waits for an acknowledgement from the broker (the same behavior as 0.7). 
	 *  	This option provides the lowest latency but the weakest durability guarantees (some data will be lost when a server fails).
	 *  1, which means that the producer gets an acknowledgement after the leader replica has received the data. 
	 *  	This option provides better durability as the client waits until the server acknowledges the request as successful (only messages that were written to the now-dead leader but not yet replicated will be lost).
	 * -1, which means that the producer gets an acknowledgement after all in-sync replicas have received the data. 
	 *   This option provides the best durability, we guarantee that no messages will be lost as long as at least one in sync replica remains.
	 * </pre>
	 * 
	 * @return
	 */
	public static String getKafkaRequestRequiredAcks() {
		return PROPERTIES.getProperty("kafka_request_required_acks", "0");
	}

	/**
	 * 基站经纬度配置信息文件存放路径
	 * 
	 * @return
	 */
	public static String getCellResource() {
		return PROPERTIES.getProperty("cell_resource", "cell.txt");
	}

	/**
	 * DataQueueCache 缓冲size 大小
	 * 
	 * @return
	 */
	public static int getDataQueueCacheSize() {
		return Integer.valueOf(PROPERTIES.getProperty("data_queue_cache_size",
				"10"));
	}

	public static long getDataQueueCacheCleanUpTime() {
		return Long.valueOf(PROPERTIES.getProperty(
				"data_queue_cache_cleanup_time", "30000"));
	}

	/**
	 * imsi，imei,msisdn 持久化规则最大保存数量（默认：5000000）
	 * 
	 * @return
	 */
	public static long getPersistentRuleSize() {
		return Long.valueOf(PROPERTIES.getProperty("persistent_rule_size",
				"5000000"));
	}

	/**
	 * 是否开启原始码流xdr统计
	 * 
	 * @return
	 */
	public static boolean isRawXdrIdStat() {
		String ret = PROPERTIES.getProperty("raw_xdrid_stat", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否开启s11回填功能（默认开启）
	 * 
	 * @return
	 */
	public static boolean isFillS11() {
		String ret = PROPERTIES.getProperty("is_fill_s11", "true");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * s1mme重新回填的cache容量(默认：2000000)
	 * 
	 * @return
	 */
	public static int getS1mmePendingSize() {
		return Integer.valueOf(PROPERTIES.getProperty("s1mme_pending_size",
				"2000000"));
	}

	/**
	 * 保存未处理的原始XDR的topic
	 * 
	 * @return
	 */
	public static String getKafkaXdrTopic() {
		return PROPERTIES.getProperty("kafka_xdr_topic", "xdr");
	}

	/**
	 * 保存原始码流的topic
	 * 
	 * @return
	 */
	public static String getKafkaRawTopic() {
		return PROPERTIES.getProperty("kafka_raw_topic", "raw");
	}

	/**
	 * xdr数据发送至kafka的broker配置
	 * 
	 * @return
	 */
	public static String getKafkaXdrBrokerList() {
		return PROPERTIES.getProperty("kafka_xdr_broker_list", "xdr");
	}

	/**
	 * 原始码流数据发送至kafka的broker配置
	 * 
	 * @return
	 */
	public static String getKafkaRawBrokerList() {
		return PROPERTIES.getProperty("kafka_raw_broker_list", "raw");
	}

	/**
	 * 是否提供ta信息
	 * 
	 * @return
	 */
	public static boolean hasTa() {
		String ret = PROPERTIES.getProperty("has_ta", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	public static void main(String[] args) {
		System.out.println(IS_BACKFILL);
		System.out.println(PROVINCE_CODE);
		System.out.println(isCompress());
		System.out.println(isOutputFilledXdr2File());
		System.out.println(isOutputOriginalXdr2File());
		System.out.println(MAX_TRY_NUM);
		System.out.println(getPendingTTL());
		System.out.println(getOutputFilePeriod());
		System.out.println(getOutputFileSize());
		System.out.println(getPID());
		System.out.println(IS_OUTPUT_ORIGINAL_XDR_2_KAFKA);
		System.out.println(isApplicationLayer());
		System.out.println(isOutputOriginalXdr2Kafka());
		System.out.println(getKafkaRequestRequiredAcks());
		System.out.println(IS_RESPONESE);
		System.out.println(getScaOffsetMills());
	}

	public static List<HostAndPortVo> getSdtpServerHostAndPort() {
		String str = PROPERTIES.getProperty("sdtp_server_host_port");
		List<HostAndPortVo> ret = new ArrayList<>();
		if (str != null && str.trim().length() > 0) {
			String[] arrs = str.split(",");
			for (String arr : arrs) {
				if (arr.contains(":")) {
					String[] tmp = arr.split(":", 2);
					ret.add(new HostAndPortVo(tmp[0], Integer.valueOf(tmp[1])));
				}
			}
		} else {
			System.err.println("sdtp_server_host_port config not found.");
		}
		return ret;
	}

	@Deprecated
	public static String getBackFillRuleFile() {
		return PROPERTIES.getProperty("backfill_rule_file", "/home/export.csv");
	}

	/**
	 * 用户经纬度信息缓存时间（单位：秒）
	 * 
	 * @return
	 */
	public static int getUserUemrTTLSecond() {
		return Integer.valueOf(PROPERTIES.getProperty("user_uemr_ttl_second",
				"3600"));
	}

	/**
	 * 用户经纬度信息缓存最大容量(默认：5000000)
	 * 
	 * @return
	 */
	public static long getUserUemrSize() {
		return Long
				.valueOf(PROPERTIES.getProperty("user_uemr_size", "5000000"));
	}

	@Deprecated
	public static boolean isBackFillRuleLoad() {
		String ret = PROPERTIES.getProperty("backfill_rule_load", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 软采时间偏移量(单位：毫秒，默认0)
	 * 
	 * @return
	 */
	public static long getScaOffsetMills() {
		return Long.valueOf(PROPERTIES.getProperty("sca_offset_mills", "0"));
	}

	public static boolean isOriginalRawUseGbiupsFormat() {
		String ret = PROPERTIES.getProperty(
				"is_original_raw_use_gbiups_format", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 考核输出文件的dir
	 * 
	 * @return
	 */
	public static String getExamineDir() {
		return PROPERTIES.getProperty("examine_dir", "/home/eversec/examine");
	}

	/**
	 * 考核输出文件的城市名
	 * 
	 * @return
	 */
	public static String getExamineCity() {
		return PROPERTIES.getProperty("examine_city", "liaoning");
	}

	/**
	 * 考核文件输出越过批量
	 * 
	 * @return
	 */
	private static boolean isExamineSkipStartPos() {
		String ret = PROPERTIES.getProperty("is_examine_skip_start_pos",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 考核处理线程
	 * 
	 * @return
	 */
	public static int getExamineThreads() {
		return Integer.valueOf(PROPERTIES.getProperty("examine_threads", "10"));
	}

	/**
	 * 使用新的巡检输出逻辑
	 * 
	 * @return
	 */
	private static boolean isUseNewExamine() {
		String ret = PROPERTIES.getProperty("is_use_new_examine", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * kafka 批量发送数量
	 * 
	 * @return
	 */
	public static String getKafkaBatchNumMessages() {
		return PROPERTIES.getProperty("kafka_batch_num_messages", "500");
	}

	/*
	 * 由于不同文件对应不同的输出逻辑，故可以对每个文件单独配置
	 */
	public static class FileOutputConfig {
		public String max_memory_size;
		public String expire_time;

		public FileOutputConfig(String max_memory_size, String expire_time) {
			this.max_memory_size = max_memory_size;
			this.expire_time = expire_time;
		}
	}

	private static Map<String, FileOutputConfig> initFileOutputConfigs() {
		Map<String, FileOutputConfig> map = new HashMap<>();
		// #文件输出大小大小
		// output_file_size=100m
		// #文件输出周期(ms[毫秒]，s[秒]，m[分]，h[小时]，d[天])
		// output_file_period=5m
		// map.put("s1mme", new FileOutputConfig("380m", "5m"));
		// map.put("s6a", new FileOutputConfig("380m", "5m"));
		// map.put("sgs", new FileOutputConfig("380m", "5m"));
		// map.put("s11", new FileOutputConfig("380m", "5m"));
		//
		// map.put("http", new FileOutputConfig("250m", "30m"));
		// map.put("other", new FileOutputConfig("250m", "30m"));
		// map.put("dns", new FileOutputConfig("250m", "30m"));
		// map.put("email", new FileOutputConfig("250m", "30m"));
		// map.put("ftp", new FileOutputConfig("250m", "30m"));
		// map.put("mms", new FileOutputConfig("250m", "30m"));
		// map.put("rtsp", new FileOutputConfig("250m", "30m"));
		// map.put("voip", new FileOutputConfig("250m", "30m"));
		// map.put("im", new FileOutputConfig("250m", "30m"));
		// map.put("p2p", new FileOutputConfig("250m", "30m"));

		String defalutVal = getOutputFileSize() + "," + getOutputFilePeriod();

		String s1mme_value = PROPERTIES.getProperty("s1mme_output_file_config",
				defalutVal);
		String sgs_value = PROPERTIES.getProperty("sgs_output_file_config",
				defalutVal);
		String s6a_value = PROPERTIES.getProperty("s6a_output_file_config",
				defalutVal);
		String s11_value = PROPERTIES.getProperty("s11_output_file_config",
				defalutVal);

		String http_value = PROPERTIES.getProperty("http_output_file_config",
				defalutVal);
		String other_value = PROPERTIES.getProperty("other_output_file_config",
				defalutVal);
		String dns_value = PROPERTIES.getProperty("dns_output_file_config",
				defalutVal);
		String email_value = PROPERTIES.getProperty("email_output_file_config",
				defalutVal);
		String ftp_value = PROPERTIES.getProperty("ftp_output_file_config",
				defalutVal);
		String mms_value = PROPERTIES.getProperty("mms_output_file_config",
				defalutVal);
		String rtsp_value = PROPERTIES.getProperty("rtsp_output_file_config",
				defalutVal);
		String voip_value = PROPERTIES.getProperty("voip_output_file_config",
				defalutVal);
		String im_value = PROPERTIES.getProperty("im_output_file_config",
				defalutVal);
		String p2p_value = PROPERTIES.getProperty("p2p_output_file_config",
				defalutVal);

		String uu_value = PROPERTIES.getProperty("uu_output_file_config",
				defalutVal);
		String x2_value = PROPERTIES.getProperty("x2_output_file_config",
				defalutVal);
		String cellmr_value = PROPERTIES.getProperty(
				"cellmr_output_file_config", defalutVal);
		String uemr_value = PROPERTIES.getProperty("uemr_output_file_config",
				defalutVal);

		String comp_userdata_value = PROPERTIES.getProperty(
				"comp_output_file_config", defalutVal);
		String comp_sig_value = PROPERTIES.getProperty(
				"comp_sig_output_file_config", defalutVal);
		String comp_uemr_value = PROPERTIES.getProperty(
				"comp_uemr_output_file_config", defalutVal);

		map.put("s1mme", new FileOutputConfig(s1mme_value.split(",")[0],
				s1mme_value.split(",")[1]));
		map.put("s6a",
				new FileOutputConfig(sgs_value.split(",")[0], sgs_value
						.split(",")[1]));
		map.put("sgs",
				new FileOutputConfig(s6a_value.split(",")[0], s6a_value
						.split(",")[1]));
		map.put("s11",
				new FileOutputConfig(s11_value.split(",")[0], s11_value
						.split(",")[1]));

		map.put("http", new FileOutputConfig(http_value.split(",")[0],
				http_value.split(",")[1]));
		map.put("other", new FileOutputConfig(other_value.split(",")[0],
				other_value.split(",")[1]));
		map.put("dns",
				new FileOutputConfig(dns_value.split(",")[0], dns_value
						.split(",")[1]));
		map.put("email", new FileOutputConfig(email_value.split(",")[0],
				email_value.split(",")[1]));
		map.put("ftp",
				new FileOutputConfig(ftp_value.split(",")[0], ftp_value
						.split(",")[1]));
		map.put("mms",
				new FileOutputConfig(mms_value.split(",")[0], mms_value
						.split(",")[1]));
		map.put("rtsp", new FileOutputConfig(rtsp_value.split(",")[0],
				rtsp_value.split(",")[1]));
		map.put("voip", new FileOutputConfig(voip_value.split(",")[0],
				voip_value.split(",")[1]));
		map.put("im",
				new FileOutputConfig(im_value.split(",")[0], im_value
						.split(",")[1]));
		map.put("p2p",
				new FileOutputConfig(p2p_value.split(",")[0], p2p_value
						.split(",")[1]));

		map.put("uu",
				new FileOutputConfig(uu_value.split(",")[0], uu_value
						.split(",")[1]));
		map.put("x2",
				new FileOutputConfig(x2_value.split(",")[0], x2_value
						.split(",")[1]));
		map.put("cellmr", new FileOutputConfig(cellmr_value.split(",")[0],
				cellmr_value.split(",")[1]));
		map.put("uemr", new FileOutputConfig(uemr_value.split(",")[0],
				uemr_value.split(",")[1]));

		map.put("comp_userdata",
				new FileOutputConfig(comp_userdata_value.split(",")[0],
						comp_userdata_value.split(",")[1]));
		map.put("comp_sig", new FileOutputConfig(comp_sig_value.split(",")[0],
				comp_sig_value.split(",")[1]));
		map.put("comp_uemr", new FileOutputConfig(
				comp_uemr_value.split(",")[0], comp_uemr_value.split(",")[1]));

		return map;
	}

	/**
	 * 获得文件输出配置
	 * 
	 * @param type
	 * @return
	 */
	public static FileOutputConfig getFileOutputConfigs(String type) {
		FileOutputConfig foc = FILE_OUTPUT_CONFIGS.get(type);
		if (foc == null) {
			return new FileOutputConfig(getOutputFileSize(),
					getOutputFilePeriod());
		}
		return foc;
	}

	// /**
	// * 是否在回填前根据ip回填cityCode
	// * @return
	// */
	// private static boolean initIsRefillCityCode() {
	// String ret = PROPERTIES.getProperty("is_refill_city_code", "false");
	// return ret.trim().equalsIgnoreCase(String.valueOf(true));
	// }

	/**
	 * 是否在回填前根据ip回填cityCode
	 * 
	 * @return
	 */
	private static boolean isBackFillProcessBatch() {
		String ret = PROPERTIES.getProperty("is_backfill_process_batch",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 回填模块跳过回填逻辑
	 * 
	 * @return
	 */
	public static boolean isSkipBackFill() {
		String ret = PROPERTIES.getProperty("is_skip_back_fill", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * redis 批量获取大小
	 * 
	 * @return
	 */
	public static int getRedisBatchNum() {
		return Integer.valueOf(PROPERTIES.getProperty("redis_batch_num",
				"100000"));
	}

	/**
	 * 接收测试
	 * 
	 * @return
	 */
	@Deprecated
	public static boolean testGet() {
		String ret = PROPERTIES.getProperty("testGet", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 测试发送
	 * 
	 * @return
	 */
	@Deprecated
	public static boolean testSet() {
		String ret = PROPERTIES.getProperty("testSet", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 测试接收2
	 * 
	 * @return
	 */
	@Deprecated
	public static boolean testRec() {
		String ret = PROPERTIES.getProperty("testRec", "true");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 测试redis线程
	 * 
	 * @return
	 */
	@Deprecated
	public static int getRedisThreads() {
		return Integer.valueOf(PROPERTIES.getProperty("redis_threads", "5"));
	}

	/**
	 * 使用回填延迟计算
	 * 
	 * @return
	 */
	@Deprecated
	public static boolean isUseBackfillTTL() {
		String ret = PROPERTIES.getProperty("use_backfill_ttl", "true");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 是否使用外部存储
	 * 
	 * @return
	 */
	@Deprecated
	public static boolean isUseExternalCache() {
		String ret = PROPERTIES.getProperty("use_external_cache", "true");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	public static String getRuleXIdentifyId() {
		return PROPERTIES.getProperty("rulex_identify_id", "0");
	}

	/**
	 * 过滤部分发送sdtp 话单
	 * 
	 * @return
	 */
	private static boolean isFilterOutputFilled2Sdtp() {
		String ret = PROPERTIES.getProperty("is_filter_output_filled_2_sdtp",
				"true");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 过滤cityCode
	 * 
	 * @see #isFilterOutputFilled2Sdtp()
	 * 
	 * @return
	 */
	private static String[] filterOutputFilled2SdtpCityCode() {
		String ret = PROPERTIES.getProperty(
				"filter_output_filled_2_sdtp_citycode", "");
		if (ret.length() > 0) {
			return ret.split(",");
		}
		return new String[0];
	}

	/**
	 * 过滤Interface
	 * 
	 * @see #isFilterOutputFilled2Sdtp()
	 * @return
	 */
	private static String[] filterOutputFilled2SdtpInterface() {
		String ret = PROPERTIES.getProperty(
				"filter_output_filled_2_sdtp_interface", "");
		if (ret.length() > 0) {
			return ret.split(",");
		}
		return new String[0];
	}

	/**
	 * 是否输出原始码率到pcap文件
	 * 
	 * @return
	 */
	public static boolean isOutputRaw2Pcap() {
		String ret = PROPERTIES.getProperty("output_original_raw_2_pcap",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 输出原始码率到pcap文件目录
	 * 
	 * @return
	 */
	public static String getPcapDir() {
		return PROPERTIES.getProperty("pcap_dir", "/data/eversec/raw/");
	}

	/**
	 * 输出原始码率到pcap文件周期
	 * 
	 * @return
	 */
	public static long getPcapSpecTime() {
		return Long.valueOf(PROPERTIES.getProperty("pcap_spec_time", "60000"));

	}

	/**
	 * 是否使用分组输出
	 * 
	 * @return
	 */
	private static boolean isUseGroupingOutput() {
		String ret = PROPERTIES.getProperty("use_grouping_output", "false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * NewIO buffer cache size
	 * 
	 * @return
	 */
	public static long getFileBufferCacheSize() {
		String ret = PROPERTIES.getProperty("file_buffer_cache_size",
				1024 * 1024 * 10 + "");
		return Long.parseLong(ret); // 10m;
	}

	/**
	 * 外部存储使用硬盘保存
	 * 
	 * @return
	 */
	public static boolean isExternalCacheUsePersistence() {
		String ret = PROPERTIES.getProperty("is_externalcache_use_persistence",
				"false");
		return ret.trim().equalsIgnoreCase(String.valueOf(true));
	}

	/**
	 * 外部存储本地缓存dir
	 * 
	 * @return
	 */
	public static String getExternalCachePersistenceDir() {
		return PROPERTIES.getProperty("externalcache_persistence_dir",
				"/home/tmp");
	}

	/**
	 * kafkaRuleTools 唯一标识
	 * 
	 * @return
	 */
	public static String getKafkaRuleToolsIdentify() {
		return PROPERTIES.getProperty("kafka_ruletools_identify", "");
	}
}
