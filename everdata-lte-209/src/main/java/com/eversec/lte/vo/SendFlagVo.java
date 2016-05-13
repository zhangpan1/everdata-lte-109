package com.eversec.lte.vo;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 检测包顺序标签
 * 
 * @author bieremayi
 * 
 */
public class SendFlagVo {

	private AtomicLong sendFlag = new AtomicLong();
	private long createTime = new Date().getTime();
	private long ttl = 60 * 60 * 1000;

	public SendFlagVo() {
		super();
	}

	public SendFlagVo(long ttl) {
		super();
		this.ttl = ttl;
	}

	public long getSendFlag() {
		if (new Date().getTime() - createTime >= ttl)
			sendFlag = new AtomicLong();
		return sendFlag.incrementAndGet();
	}
}
