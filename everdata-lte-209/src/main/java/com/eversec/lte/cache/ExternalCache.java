package com.eversec.lte.cache;

/**
 * 综合各种功能
 * 
 * @author lirongzhi
 * 
 */
public interface ExternalCache {

	void log(long period);

	void set(String type, String key, String value);

	void set(String type, String key, String value, int ttl);
	
	String get(String type, String key);
	
	void getAsyn(String type, String key,ValueGetHandler handler);

}
