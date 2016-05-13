package com.eversec.lte.vo.backfill;

import java.util.HashMap;
import java.util.Map;

/**
 * 回填信息
 * 
 * @author bieremayi
 * 
 */
public class BackFillVo {
	private String imei;
	private String msisdn;
	private String imsi;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	
	public BackFillVo(String imei, String msisdn, String imsi) {
		super();
		this.imei = imei;
		this.msisdn = msisdn;
		this.imsi = imsi;
	}
	
	public BackFillVo() {
		super();
	}

	@Override
	public String toString() {
		return imei + "," + msisdn + "," + imsi;
	}

	public static void main(String[] args) {
		Map<String, BackFillVo> map = new HashMap<String, BackFillVo>();
		String msisdn = "8613400000427";
		String imei = "861576000024155";
		String imsi = "460010100000427";
		BackFillVo vo1 = new BackFillVo(imei, msisdn, imsi);
		map.put(imsi, vo1);
//		BackFillVo vo2 = new BackFillVo(imei, msisdn, imsi);
//		map.put(imei, vo1);
//		BackFillVo vo3 = new BackFillVo(imei, msisdn, imsi);
		map.put(msisdn, vo1);
		for (String str : map.keySet()) {
			System.out.println("value:" + map.get(str) + "--HashCode:"
					+ map.get(str).hashCode());
		}
		map.get(msisdn).setImei("88");
		for (String str : map.keySet()) {
			System.out.println("value:" + map.get(str) + "--HashCode:"
					+ map.get(str).hashCode());
		}
	}

}
