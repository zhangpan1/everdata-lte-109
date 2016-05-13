package com.eversec.lte.vo.compound;

import java.io.Serializable;

import com.eversec.lte.model.single.XdrSingleSourceS1MME;

/**
 * s1mme主流程信息
 * 
 * @author bieremayi
 * 
 */
public class MainProcedureInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6842211595070793460L;
	private XdrSingleSourceS1MME mainXdr;
	private int cspt;
	private String lowID;
	private long startTime;
	private long endTime;
	private int keyword;
	private String rule1;// mmeUeS1apID_mmeGroupID_mmeCode

	public MainProcedureInfo(XdrSingleSourceS1MME mainXdr, int cspt,
			String lowID, long startTime, long endTime, int keyword,
			String rule1) {
		super();
		this.mainXdr = mainXdr;
		this.cspt = cspt;
		this.lowID = lowID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.keyword = keyword;
		this.rule1 = rule1;
	}

	/**
	 * 合成主流程s1mme话单
	 * 
	 * @return
	 */
	public XdrSingleSourceS1MME getMainXdr() {
		return mainXdr;
	}

	public void setMainXdr(XdrSingleSourceS1MME mainXdr) {
		this.mainXdr = mainXdr;
	}

	/**
	 * 合成信令xdr流程类型
	 * 
	 * @return
	 */
	public int getCspt() {
		return cspt;
	}

	public void setCspt(int cspt) {
		this.cspt = cspt;
	}

	/**
	 * xdrID低8位字节HEX编码字符串
	 * 
	 * @return
	 */
	public String getLowID() {
		return lowID;
	}

	public void setLowID(String lowID) {
		this.lowID = lowID;
	}

	/**
	 * 流程开始时间（时间戳）
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 流程结束时间(时间戳)
	 * 
	 * @return
	 */
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * s1mme keyword
	 * 
	 * @return
	 */
	public int getKeyword() {
		return keyword;
	}

	public void setKeyword(int keyword) {
		this.keyword = keyword;
	}

	/**
	 * mmeUeS1apID_mmeGroupID_mmeCode
	 * 
	 * @return
	 */
	public String getRule1() {
		return rule1;
	}

	public void setRule1(String rule1) {
		this.rule1 = rule1;
	}

}
