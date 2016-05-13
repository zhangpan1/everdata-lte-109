package com.eversec.lte.vo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author bieremayi
 *
 */
public class SendDataInfoVo {
	private AtomicLong sendDataInfo = new AtomicLong();

	public long getSendDataInfo() {
		return sendDataInfo.incrementAndGet();
	}

	public void reset() {
		sendDataInfo.set(0);
	}
	
	
}
