package com.eversec.lte.ttl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.utils.LteThreadFactory;

public class TTLTools {

	private int size = 1;
	private int threads = 10;
	private ScheduledThreadPoolExecutor[] sche;

	private int index = 0;

	public TTLTools(int size, int threads) {
		super();
		this.size = size;
		this.threads = threads;
		sche = new ScheduledThreadPoolExecutor[size];
		for (int i = 0; i < size; i++) {
			sche[i] = (ScheduledThreadPoolExecutor) Executors
					.newScheduledThreadPool(threads, new LteThreadFactory("TTLTools"));
		}
	}

	public void delay(Runnable run) {
		try {
			sche[index++ % sche.length].schedule(run, 10, TimeUnit.SECONDS);
		} catch (Exception e) {
			index = 0;
			e.printStackTrace();
		}
	}

	public int[] size() {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = sche[i].getQueue().size();
		}
		return result;
	}

	public String report() {
		String str = "TTLTools queue size:";
		int[] sizes = size();
		int max = 0;
		for (int i = 0; i < sizes.length; i++) {
			max += sizes[i];
		}
		str += max;
		return str;
	}

	public void shutdown() {
		for (int i = 0; i < size; i++) {
			if (sche[i] != null) {
				sche[i].shutdown();
			}
		}

	}

	public static void main(String[] args) {
		final TTLTools ttl = new TTLTools(1, 10);
		ScheduledExecutorService as = Executors.newScheduledThreadPool(1);
		as.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				String str = "ss:";
				int[] sizes = ttl.size();
				for (int i = 0; i < sizes.length; i++) {
					str += sizes[i] + ",";
				}
				System.out.println(str);
				System.out.println(ttl.report());

			}
		}, 2, 2, TimeUnit.SECONDS);
		int size = 150000;
		for (int x = 0; x < 100000; x++) {
			final CountDownLatch dl = new CountDownLatch(size);
			long t = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				ttl.delay(new Runnable() {
					@Override
					public void run() {
						dl.countDown();
					}
				});
			}
			System.out.println("add:" + (System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			try {
				dl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("all:" + (System.currentTimeMillis() - t));
		}

	}
}
