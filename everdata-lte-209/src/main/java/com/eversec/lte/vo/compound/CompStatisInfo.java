package com.eversec.lte.vo.compound;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 合成统计
 * 
 */
public class CompStatisInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4683251660112280772L;
	protected AtomicLong sourceCount = new AtomicLong(0);// xdr数
	protected AtomicLong xdr2sdtpPackage = new AtomicLong(0);// xdr 包数
	protected AtomicLong xdr2sdtpBytes = new AtomicLong(0);// xdr字节数
	protected Map<String, AtomicLong> cxdrTypeCache = new HashMap<>();// cdr类型

	public void addSourceCount(int val) {
		sourceCount.addAndGet(val);
	}

	public void addXdr2sdtpPackage(int val) {
		xdr2sdtpPackage.addAndGet(val);
	}

	public void addXdr2sdtpBytes(int val) {
		xdr2sdtpBytes.addAndGet(val);
	}

	public void addXdrTypeCache(String type, int val) {
		if (cxdrTypeCache.containsKey(type)) {
			cxdrTypeCache.get(type).addAndGet(val);
		} else {
			cxdrTypeCache.put(type, new AtomicLong(val));
		}
	}

	public AtomicLong getSourceCount() {
		return sourceCount;
	}

	public AtomicLong getXdr2sdtpPackage() {
		return xdr2sdtpPackage;
	}

	public AtomicLong getXdr2sdtpBytes() {
		return xdr2sdtpBytes;
	}

	public Map<String, AtomicLong> getCxdrTypeCache() {
		return cxdrTypeCache;
	}

}
