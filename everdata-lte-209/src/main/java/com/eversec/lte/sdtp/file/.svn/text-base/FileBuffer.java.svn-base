package com.eversec.lte.sdtp.file;

import org.apache.mina.core.buffer.IoBuffer;

public class FileBuffer {

	private String path;
	private IoBuffer buffer;
	private Object[] parameters;
	private boolean append;

	public FileBuffer(String path, IoBuffer buffer, Object[] parameters,
			boolean append) {
		super();
		this.path = path;
		this.buffer = buffer;
		this.parameters = parameters;
		this.append = append;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public IoBuffer getBuffer() {
		return buffer;
	}

	public String getPath() {
		return path;
	}

	public boolean isAppend() {
		return append;
	}
}
