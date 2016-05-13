package com.eversec.lte.kpi.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.eversec.lte.config.SdtpConfig;

public class KPIConfig {

	private static final String CONFIG = "kpi.properties";

	public static Properties PROPERTIES = new Properties();

	static {
		ClassLoader classLoader = SdtpConfig.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream(CONFIG);
		try {
			PROPERTIES.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getMiddleCacheSize() {
		return Integer.parseInt(PROPERTIES.getProperty("middle_cache_size",	"100000"));
	}

	public static int getTimeFlashInterval() {
		return Integer.parseInt(PROPERTIES.getProperty("time_flash_interval", "1000"));
	}

	public static int getTimeCacheSize() {
		return Integer.parseInt(PROPERTIES.getProperty("time_cache_size", "500000"));
	}

	public static int getBlockNum() {
		return Integer.parseInt(PROPERTIES.getProperty("block_num", "2"));
	}

	public static int getBlockTime() {
		return Integer.parseInt(PROPERTIES.getProperty("block_time", "5"));
	}
	
	public static int getBlockTimeS1u() {
		return Integer.parseInt(PROPERTIES.getProperty("block_time_s1u", "60"));
	}
	
	public static int getBlockTimeTerm() {
		return Integer.parseInt(PROPERTIES.getProperty("block_time_term", "60"));
	}
	
	public static int getBlockTimeTag() {
		return Integer.parseInt(PROPERTIES.getProperty("block_time_tag", "60"));
	}

	public static String getS1mmeOutFile(int type) {
		return PROPERTIES.getProperty("s1mme_out_file_" + type, "/home/name_s1mme_null");
	}
	
	public static String getS6aOutFile(int type) {
		return PROPERTIES
				.getProperty("s6a_out_file_" + type, "/home/name_null");
	}

	public static String getSgsOutFile(int type) {
		return PROPERTIES
				.getProperty("sgs_out_file_" + type, "/home/name_null");
	}

	public static String getS11OutFile(int type) {
		return PROPERTIES
				.getProperty("s11_out_file_" + type, "/home/name_null");
	}

	public static String getS1uOutFile(int type) {
		return PROPERTIES.getProperty("s1u_out_file_" + type, "/home/name_null");
	}

	public static boolean getCountWork() {
		return Boolean.parseBoolean(PROPERTIES.getProperty("count_work", "false"));
	}
	
	public static String getKpiImeiFile() {
		return PROPERTIES.getProperty("kpi_imei_file", "/home/kpi/kpi_imei.txt");
	}
	
	public static String getTAGOutFile(int type) {
		return PROPERTIES.getProperty("s1u_tag_file_" + type, "/home/name_tag");
	}
	
	public static String getTermOutFile(int type) {
		return PROPERTIES
				.getProperty("term_file_" + type, "/home/term_null");
	}
	
	public static String getTermUserOutFile(int type) {
		return PROPERTIES
				.getProperty("term_file_" + type, "/home/term_null");
	}
	
	public static Integer getThreadNum() {
		return Integer.valueOf(PROPERTIES.getProperty("thread_num", "1").trim());
	}
	
	public static boolean getKpiS1u() {
		return Boolean.parseBoolean(PROPERTIES.getProperty("kpi_s1u", "false"));
	}
	
	public static boolean getKpiSig() {
		return Boolean.parseBoolean(PROPERTIES.getProperty("kpi_sig", "false"));
	}
	
	public static boolean getKpiTerm() {
		return Boolean.parseBoolean(PROPERTIES.getProperty("kpi_term", "false"));
	}
}
