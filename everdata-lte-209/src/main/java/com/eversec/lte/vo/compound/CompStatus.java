package com.eversec.lte.vo.compound;

/**
 * 合成逻辑所需的状态信息
 * 
 * @author bieremayi
 * 
 */
public class CompStatus {
	private boolean isCheckEndTime;// 是否检查结束时间，用于同一条话单为开始结束标识的情况
	private long checkEndTime = Long.MIN_VALUE; // 检查结束时间
	private int checkProcedure = -1; // 需检查结束时间的信令流程
	private int procedureEndIndex = -1;// 流程结束标识index
	private int procedureEndStatus = 0;// 流程结束标识状态

	public boolean isCheckEndTime() {
		return isCheckEndTime;
	}

	public void setCheckEndTime(boolean isCheckEndTime) {
		this.isCheckEndTime = isCheckEndTime;
	}

	public long getCheckEndTime() {
		return checkEndTime;
	}

	public void setCheckEndTime(long checkEndTime) {
		this.checkEndTime = checkEndTime;
	}

	public int getCheckProcedure() {
		return checkProcedure;
	}

	public void setCheckProcedure(int checkProcedure) {
		this.checkProcedure = checkProcedure;
	}

	public int getProcedureEndIndex() {
		return procedureEndIndex;
	}

	public void setProcedureEndIndex(int procedureEndIndex) {
		this.procedureEndIndex = procedureEndIndex;
	}

	public int getProcedureEndStatus() {
		return procedureEndStatus;
	}

	public void setProcedureEndStatus(int procedureEndStatus) {
		this.procedureEndStatus = procedureEndStatus;
	}

}
