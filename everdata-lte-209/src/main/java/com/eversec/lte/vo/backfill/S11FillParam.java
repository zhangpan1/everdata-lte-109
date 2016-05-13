package com.eversec.lte.vo.backfill;

/**
 * s11回填参数
 * 
 * @author bieremayi
 * 
 */
public class S11FillParam extends FillParam {
	private String userIpv4;
	private String sgwTeid;

	public S11FillParam(String msisdn, String imsi, String imei,
			String userIpv4, String sgwTeid) {
		super(msisdn, imsi, imei);
		this.msisdn = msisdn;
		this.imsi = imsi;
		this.imei = imei;
		this.userIpv4 = userIpv4;
		this.sgwTeid = sgwTeid;
	}

	public String getUserIpv4() {
		return userIpv4;
	}

	public void setUserIpv4(String userIpv4) {
		this.userIpv4 = userIpv4;
	}

	public String getSgwTeid() {
		return sgwTeid;
	}

	public void setSgwTeid(String sgwTeid) {
		this.sgwTeid = sgwTeid;
	}

}
