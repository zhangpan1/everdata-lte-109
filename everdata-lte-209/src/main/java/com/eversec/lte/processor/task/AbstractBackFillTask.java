package com.eversec.lte.processor.task;

import java.util.concurrent.ArrayBlockingQueue;

import com.eversec.lte.config.SdtpConfig;

public abstract class AbstractBackFillTask<T> implements Runnable {
	protected ArrayBlockingQueue<T> queue;
	protected int drainMaxElements = SdtpConfig.getDrainMaxElements();
	protected long drainTaskSleepMills = SdtpConfig.getDrainTaskSleepMills();
}
