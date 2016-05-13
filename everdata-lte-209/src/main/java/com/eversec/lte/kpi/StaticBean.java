package com.eversec.lte.kpi;

public class StaticBean {

	private String interfaceName;
	private String compareFieldCount;
	private String computeFieldCount;
	
	
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getCompareFieldCount() {
		return compareFieldCount;
	}
	public void setCompareFieldCount(String compareFieldCount) {
		this.compareFieldCount = compareFieldCount;
	}
	public String getComputeFieldCount() {
		return computeFieldCount;
	}
	public void setComputeFieldCount(String computeFieldCount) {
		this.computeFieldCount = computeFieldCount;
	}
	public StaticBean(String interfaceName, String compareFieldCount, String computeFieldCount) {
		super();
		this.interfaceName = interfaceName;
		this.compareFieldCount = compareFieldCount;
		this.computeFieldCount = computeFieldCount;
	}
	
	
	
	
	
	
}
