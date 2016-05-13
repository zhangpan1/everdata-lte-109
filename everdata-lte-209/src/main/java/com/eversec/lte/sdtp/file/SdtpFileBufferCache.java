package com.eversec.lte.sdtp.file;

import java.util.concurrent.ArrayBlockingQueue;

public class SdtpFileBufferCache extends FileBufferCache {
	protected String Interface;
	protected int apptype;

	public SdtpFileBufferCache(String path, String maxlimit, String expire,
			ArrayBlockingQueue<FileBuffer> fileBufferQueue, String Interface,
			int apptype) {
		super(path, maxlimit, expire, fileBufferQueue);
		this.Interface = Interface;
		this.apptype = apptype;
	}

	public SdtpFileBufferCache(String path, String maxlimit, String expire,
			ArrayBlockingQueue<FileBuffer> fileBufferQueue, int queueSize,
			String Interface, int apptype) {
		super(path, maxlimit, expire, fileBufferQueue, queueSize);
		this.Interface = Interface;
		this.apptype = apptype;
	}

	@Override
	public Object[] getParameters(FileOutputItem item) {
		return new Object[] { Interface, apptype, item.time };
	}
}
