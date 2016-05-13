package com.eversec.lte.processor.backfill;

import com.eversec.lte.processor.backfill.AbstractBackFill.RuleType;

public class XdrBackFillInfo {
	public String msisdn, imsi, imei;
	// public int fillType;
	public RuleType fillType;

	public XdrBackFillInfo(String msisdn, String imsi, String imei) {
		super();
		this.msisdn = msisdn;
		this.imsi = imsi;
		this.imei = imei;
	}

}