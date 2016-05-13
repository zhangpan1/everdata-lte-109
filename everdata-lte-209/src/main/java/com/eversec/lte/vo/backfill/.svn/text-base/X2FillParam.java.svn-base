package com.eversec.lte.vo.backfill;

/**
 * s1mme回填参数
 * 
 * @author bieremayi
 * 
 */
public class X2FillParam extends FillParam {
	String mmeS1apID;
	String mmeGroupID;
	String mmeCode;

	public X2FillParam(String msisdn, String imsi, String imei,
			String mmeS1apID, String mmeGroupID, String mmeCode) {
		super(msisdn, imsi, imei);
		this.mmeS1apID = mmeS1apID;
		this.mmeGroupID = mmeGroupID;
		this.mmeCode = mmeCode;
	}

	public X2FillParam(String msisdn, String imsi, String imei,
			long mmeS1apID, int mmeGroupID, short mmeCode) {
		super(msisdn, imsi, imei);
		this.mmeS1apID = String.valueOf(mmeS1apID);
		this.mmeGroupID = String.valueOf(mmeGroupID);
		this.mmeCode = String.valueOf(mmeCode);
	}
	
	public String getMmeS1apID() {
		return mmeS1apID;
	}

	public void setMmeS1apID(String mmeS1apID) {
		this.mmeS1apID = mmeS1apID;
	}

	public String getMmeGroupID() {
		return mmeGroupID;
	}

	public void setMmeGroupID(String mmeGroupID) {
		this.mmeGroupID = mmeGroupID;
	}

	public String getMmeCode() {
		return mmeCode;
	}

	public void setMmeCode(String mmeCode) {
		this.mmeCode = mmeCode;
	}

}
