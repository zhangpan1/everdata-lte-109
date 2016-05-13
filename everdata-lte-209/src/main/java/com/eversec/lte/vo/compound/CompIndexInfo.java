package com.eversec.lte.vo.compound;

import java.io.Serializable;
import java.util.List;

import com.eversec.lte.model.single.XdrSingleSource;

/**
 * 合成流程信息
 * 
 * @author bieremayi
 * 
 */
public class CompIndexInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8299035888503746852L;

	private XdrSingleSource mainXdr;
	private List<XdrSingleSource> s1mmeXdrs;
	private List<XdrSingleSource> s6aXdrs;
	private List<XdrSingleSource> sgsXdrs;
	private List<XdrSingleSource> s11Xdrs;
	private List<XdrSingleSource> uuXdrs;
	private List<XdrSingleSource> x2Xdrs;
	private int keyword;
	private int cspt;

	public int getKeyword() {
		return keyword;
	}

	public void setKeyword(int keyword) {
		this.keyword = keyword;
	}

	public int getCspt() {
		return cspt;
	}

	public void setCspt(int cspt) {
		this.cspt = cspt;
	}

	public XdrSingleSource getMainXdr() {
		return mainXdr;
	}

	public void setMainXdr(XdrSingleSource mainXdr) {
		this.mainXdr = mainXdr;
	}

	public List<XdrSingleSource> getS1mmeXdrs() {
		return s1mmeXdrs;
	}

	public void setS1mmeXdrs(List<XdrSingleSource> s1mmeXdrs) {
		this.s1mmeXdrs = s1mmeXdrs;
	}

	public List<XdrSingleSource> getS6aXdrs() {
		return s6aXdrs;
	}

	public void setS6aXdrs(List<XdrSingleSource> s6aXdrs) {
		this.s6aXdrs = s6aXdrs;
	}

	public List<XdrSingleSource> getSgsXdrs() {
		return sgsXdrs;
	}

	public void setSgsXdrs(List<XdrSingleSource> sgsXdrs) {
		this.sgsXdrs = sgsXdrs;
	}

	public List<XdrSingleSource> getS11Xdrs() {
		return s11Xdrs;
	}

	public void setS11Xdrs(List<XdrSingleSource> s11Xdrs) {
		this.s11Xdrs = s11Xdrs;
	}

	public List<XdrSingleSource> getUuXdrs() {
		return uuXdrs;
	}

	public void setUuXdrs(List<XdrSingleSource> uuXdrs) {
		this.uuXdrs = uuXdrs;
	}

	public List<XdrSingleSource> getX2Xdrs() {
		return x2Xdrs;
	}

	public void setX2Xdrs(List<XdrSingleSource> x2Xdrs) {
		this.x2Xdrs = x2Xdrs;
	}
	
	

}
