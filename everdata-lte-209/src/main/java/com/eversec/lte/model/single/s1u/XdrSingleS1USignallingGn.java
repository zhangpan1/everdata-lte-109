package com.eversec.lte.model.single.s1u;

import java.util.Date;

public class XdrSingleS1USignallingGn {
	int rat;
	String imsi;
	String imei;
	String msisdn;
	String apn;
	int msgType;
	Date startTime;
	int reqNum;
	int result;
	int respDelay;
	int gtpType;
	int initiator;
	int sgsnDataTeid;
	int sgsnSignallingTeid;
	int ggsnDataTeid;
	int ggsnSignallingTeid;
	int sgsnCIp;
	int ggsnCIp;

	public int getRat() {
		return rat;
	}

	public void setRat(int rat) {
		this.rat = rat;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

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

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getReqNum() {
		return reqNum;
	}

	public void setReqNum(int reqNum) {
		this.reqNum = reqNum;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRespDelay() {
		return respDelay;
	}

	public void setRespDelay(int respDelay) {
		this.respDelay = respDelay;
	}

	public int getGtpType() {
		return gtpType;
	}

	public void setGtpType(int gtpType) {
		this.gtpType = gtpType;
	}

	public int getInitiator() {
		return initiator;
	}

	public void setInitiator(int initiator) {
		this.initiator = initiator;
	}

	public int getSgsnDataTeid() {
		return sgsnDataTeid;
	}

	public void setSgsnDataTeid(int sgsnDataTeid) {
		this.sgsnDataTeid = sgsnDataTeid;
	}

	public int getSgsnSignallingTeid() {
		return sgsnSignallingTeid;
	}

	public void setSgsnSignallingTeid(int sgsnSignallingTeid) {
		this.sgsnSignallingTeid = sgsnSignallingTeid;
	}

	public int getGgsnDataTeid() {
		return ggsnDataTeid;
	}

	public void setGgsnDataTeid(int ggsnDataTeid) {
		this.ggsnDataTeid = ggsnDataTeid;
	}

	public int getGgsnSignallingTeid() {
		return ggsnSignallingTeid;
	}

	public void setGgsnSignallingTeid(int ggsnSignallingTeid) {
		this.ggsnSignallingTeid = ggsnSignallingTeid;
	}

	public int getSgsnCIp() {
		return sgsnCIp;
	}

	public void setSgsnCIp(int sgsnCIp) {
		this.sgsnCIp = sgsnCIp;
	}

	public int getGgsnCIp() {
		return ggsnCIp;
	}

	public void setGgsnCIp(int ggsnCIp) {
		this.ggsnCIp = ggsnCIp;
	}

	public XdrSingleS1USignallingGn(int rat, String imsi, String imei,
			String msisdn, String apn, int msgType, Date startTime, int reqNum,
			int result, int respDelay, int gtpType, int initiator,
			int sgsnDataTeid, int sgsnSignallingTeid, int ggsnDataTeid,
			int ggsnSignallingTeid, int sgsnCIp, int ggsnCIp) {
		super();
		this.rat = rat;
		this.imsi = imsi;
		this.imei = imei;
		this.msisdn = msisdn;
		this.apn = apn;
		this.msgType = msgType;
		this.startTime = startTime;
		this.reqNum = reqNum;
		this.result = result;
		this.respDelay = respDelay;
		this.gtpType = gtpType;
		this.initiator = initiator;
		this.sgsnDataTeid = sgsnDataTeid;
		this.sgsnSignallingTeid = sgsnSignallingTeid;
		this.ggsnDataTeid = ggsnDataTeid;
		this.ggsnSignallingTeid = ggsnSignallingTeid;
		this.sgsnCIp = sgsnCIp;
		this.ggsnCIp = ggsnCIp;
	}

}
