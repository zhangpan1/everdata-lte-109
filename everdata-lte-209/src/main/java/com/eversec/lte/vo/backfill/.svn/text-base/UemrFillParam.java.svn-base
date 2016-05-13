package com.eversec.lte.vo.backfill;

/**
 * s1mme回填参数
 * 
 * @author bieremayi
 * 
 */
public class UemrFillParam extends FillParam {
	String mmeS1apID;
	String mmeGroupID;
	String mmeCode;
	String cellID;

	// public UemrFillParam(String msisdn, String imsi, String imei,
	// String mmeS1apID, String mmeGroupID, String mmeCode) {
	// super(msisdn, imsi, imei);
	// this.mmeS1apID = mmeS1apID;
	// this.mmeGroupID = mmeGroupID;
	// this.mmeCode = mmeCode;
	// }
	//
	// public UemrFillParam(String msisdn, String imsi, String imei,
	// long mmeS1apID, int mmeGroupID, short mmeCode) {
	// super(msisdn, imsi, imei);
	// this.mmeS1apID = String.valueOf(mmeS1apID);
	// this.mmeGroupID = String.valueOf(mmeGroupID);
	// this.mmeCode = String.valueOf(mmeCode);
	// }

	public UemrFillParam(String msisdn, String imsi, String imei,
			String mmeS1apID, String cellID) {
		super(msisdn, imsi, imei);
		this.mmeS1apID = mmeS1apID;
		this.cellID = cellID;
	}

	public UemrFillParam(String msisdn, String imsi, String imei,
			long mmeS1apID, long cellID) {
		super(msisdn, imsi, imei);
		this.mmeS1apID = String.valueOf(mmeS1apID);
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

	public String getCellID() {
		return cellID;
	}

	public void setCellID(String cellID) {
		this.cellID = cellID;
	}
}
