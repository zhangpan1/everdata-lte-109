package com.eversec.lte.sdtp.file;


public class FileOutputItem {

	protected long time;
	protected String line;

	public FileOutputItem(String line, long time) {
		super();
		this.line = line;
		this.time = time;
	}

}
