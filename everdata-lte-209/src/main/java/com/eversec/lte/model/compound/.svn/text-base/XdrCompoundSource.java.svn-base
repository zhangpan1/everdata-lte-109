package com.eversec.lte.model.compound;

import com.eversec.lte.model.XdrData;

@SuppressWarnings("serial")
public abstract class XdrCompoundSource extends XdrData {
	XdrCompoundCommon common;

	public XdrCompoundCommon getCommon() {
		return common;
	}

	public void setCommon(XdrCompoundCommon common) {
		this.common = common;
	}
	
	@Override
	public String[] toStringArr() {
		return new String[] { common.toString(), toString() };
	}

	@Override
	public boolean isXdrSingle() {
		return false;
	}
}
