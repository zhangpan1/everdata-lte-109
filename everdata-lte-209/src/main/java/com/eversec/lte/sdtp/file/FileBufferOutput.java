package com.eversec.lte.sdtp.file;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FileBufferOutput implements Runnable {
	
	protected static Logger logger = LoggerFactory
			.getLogger(FileBufferOutput.class);

	protected final ArrayBlockingQueue<FileBuffer> fileBufferQueue;
	
	protected AtomicLong writeBytes = new AtomicLong();
	
	public void log(long time){
		long bytes = (writeBytes.get()*1000)/time;
		writeBytes.set(0);
		logger.info("write queue size: "+fileBufferQueue.size()+" write speed: "+bytes+"bytes/s,"+bytes/1024/1024+"Mb/s");
	}
	
	public FileBufferOutput() {
		this(1000);
	}
	
	public FileBufferOutput(int queueSize) {
		fileBufferQueue = new ArrayBlockingQueue<FileBuffer>( queueSize);
	}

	public ArrayBlockingQueue<FileBuffer> getFileBufferQueue() {
		return fileBufferQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				FileBuffer buffer = fileBufferQueue.take();
				File file = createFilePath(buffer.getPath(),
						buffer.getParameters());
				if(! file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				logger.info("ready to save file:"+file);
				file.createNewFile();
				
				FileOutputStream out = new FileOutputStream(file);
				FileChannel fs = out.getChannel();
				buffer.getBuffer().flip();
				fs.write(buffer.getBuffer().buf());
				fs.close();
				out.close();
				file.renameTo(new File(file.getAbsolutePath().replace(".tmp",
						".txt")));
				
				writeBytes.addAndGet(buffer.getBuffer().limit());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public abstract File createFilePath(String path, Object[] parameters);

}
