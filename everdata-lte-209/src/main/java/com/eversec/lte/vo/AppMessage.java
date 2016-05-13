package com.eversec.lte.vo;

import java.util.ArrayList;
import java.util.List;

import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.model.single.XdrSingleSource;

/**
 * 
 * @author bieremayi
 * 
 */
public class AppMessage {
	List<XdrSingleSource> xdrs = new ArrayList<>();
	List<XdrCompoundSource> cxdrs = new ArrayList<>();

	public List<XdrSingleSource> getXdrs() {
		return xdrs;
	}

	public void setXdrs(List<XdrSingleSource> xdrs) {
		this.xdrs = xdrs;
	}

	public List<XdrCompoundSource> getCxdrs() {
		return cxdrs;
	}

	public void setCxdrs(List<XdrCompoundSource> cxdrs) {
		this.cxdrs = cxdrs;
	}

}
