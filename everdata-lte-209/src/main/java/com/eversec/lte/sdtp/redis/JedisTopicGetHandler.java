package com.eversec.lte.sdtp.redis;

public interface JedisTopicGetHandler {
	
	public void onReceive(String topic,  String message);
}
