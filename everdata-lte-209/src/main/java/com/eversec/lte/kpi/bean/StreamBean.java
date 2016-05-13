package com.eversec.lte.kpi.bean;

import java.io.BufferedOutputStream;

public class StreamBean {
	public BufferedOutputStream out;
	public String path;
	
	
	public BufferedOutputStream getOut() {
		return out;
	}
	public void setOut(BufferedOutputStream out) {
		this.out = out;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
