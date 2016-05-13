package com.eversec.lte.cache.data;

import com.eversec.common.cache.IData;

public class RuleData implements IData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6768966482861266860L;
	private String rule;

	public RuleData(String rule) {
		super();
		this.rule = rule;
	}

	@Override
	public int getMemoryBytes() {
		return rule.getBytes().length;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

}
