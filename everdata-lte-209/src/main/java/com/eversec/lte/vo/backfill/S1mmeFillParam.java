package com.eversec.lte.vo.backfill;

/**
 * s1mme回填参数
 * 
 * @author bieremayi
 * 
 */
public class S1mmeFillParam extends FillParam {
	String mmeS1apID;
	String mmeGroupID;
	String mmeCode;
	String mTmsi;
	String userIpv4;
	String sgwTeid;
	String additionalMmeGroupID;
	String additionalMmeCode;
	String additionalMTmsi;
	String cellID;

	public S1mmeFillParam(String msisdn, String imsi, String imei,
			String mmeS1apID, String mmeGroupID, String mmeCode, String mTmsi,
			String userIpv4, String sgwTeid, String additionalMmeGroupID,
			String additionalMmeCode, String additionalMTmsi, String cellID) {
		super(msisdn, imsi, imei);
		this.mmeS1apID = mmeS1apID;
		this.mmeGroupID = mmeGroupID;
		this.mmeCode = mmeCode;
		this.mTmsi = mTmsi;
		this.userIpv4 = userIpv4;
		this.sgwTeid = sgwTeid;
		this.additionalMmeGroupID = additionalMmeGroupID;
		this.additionalMmeCode = additionalMmeCode;
		this.additionalMTmsi = additionalMTmsi;
		this.cellID = cellID;
	}

	public S1mmeFillParam(String msisdn, String imsi, String imei,
			String mmeS1apID, String mmeGroupID, String mmeCode, String mTmsi,
			String userIpv4, String sgwTeid, String cellID) {
		super(msisdn, imsi, imei);
		this.mmeS1apID = mmeS1apID;
		this.mmeGroupID = mmeGroupID;
		this.mmeCode = mmeCode;
		this.mTmsi = mTmsi;
		this.userIpv4 = userIpv4;
		this.sgwTeid = sgwTeid;
		this.cellID = cellID;
	}

	public S1mmeFillParam(String msisdn, String imsi, String imei,
			long mmeS1apID, int mmeGroupID, short mmeCode, long mTmsi,
			String userIpv4, String sgwTeid, long cellID) {
		super(msisdn, imsi, imei);
		this.mmeS1apID = String.valueOf(mmeS1apID);
		this.mmeGroupID = String.valueOf(mmeGroupID);
		this.mmeCode = String.valueOf(mmeCode);
		this.mTmsi = String.valueOf(mTmsi);
		this.userIpv4 = userIpv4;
		this.sgwTeid = sgwTeid;
		this.cellID = String.valueOf(cellID);
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

	public String getmTmsi() {
		return mTmsi;
	}

	public void setmTmsi(String mTmsi) {
		this.mTmsi = mTmsi;
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

	public String getAdditionalMmeGroupID() {
		return additionalMmeGroupID;
	}

	public void setAdditionalMmeGroupID(String additionalMmeGroupID) {
		this.additionalMmeGroupID = additionalMmeGroupID;
	}

	public String getAdditionalMmeCode() {
		return additionalMmeCode;
	}

	public void setAdditionalMmeCode(String additionalMmeCode) {
		this.additionalMmeCode = additionalMmeCode;
	}

	public String getAdditionalMTmsi() {
		return additionalMTmsi;
	}

	public void setAdditionalMTmsi(String additionalMTmsi) {
		this.additionalMTmsi = additionalMTmsi;
	}

	public String getCellID() {
		return cellID;
	}

	public void setCellID(String cellID) {
		this.cellID = cellID;
	}
}
