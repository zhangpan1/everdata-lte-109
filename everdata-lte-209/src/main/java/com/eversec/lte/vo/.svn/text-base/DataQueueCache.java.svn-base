package com.eversec.lte.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.util.ConcurrentHashSet;

import com.eversec.lte.config.SdtpConfig;

/**
 * 批量发送用
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class DataQueueCache<T> {

	public static final int SIZE = SdtpConfig.getDataQueueCacheSize();
	public static final long CLEANUP_TIME = SdtpConfig
			.getDataQueueCacheCleanUpTime();

	public static Map<String, List<DataQueueCache>> CACHE_MAP = new HashMap<String, List<DataQueueCache>>();
	public static Map<String, ConcurrentHashSet<Long>> sizes = new HashMap<String,  ConcurrentHashSet<Long>>();
	
	public static String report(){
		String str = "DataQueueCache:";
		for (Entry<String, ConcurrentHashSet<Long>> t:sizes.entrySet()) {
			str += t.getKey()+":"+t.getValue().size()+",";
		}
		return str;
	}
	
	private String type;

	public DataQueueCache(String type) {
		synchronized (CACHE_MAP) {
			if (CACHE_MAP.containsKey(type)) {
				CACHE_MAP.get(type).add(this);
			} else {
				ArrayList<DataQueueCache> list = new ArrayList<>();
				CACHE_MAP.put(type, list);
				list.add(this);
			}
			
			this.type = type;
			if(!sizes.containsKey(type)){
				sizes.put(type, new ConcurrentHashSet<Long>());
			}
		}
	}

	T[] cache = createCache(SIZE);
	int index = 0;

	volatile long lastRecTime;

	public T[] addAndGet(T source) {
		
		sizes.get(type).add(Thread.currentThread().getId());
		
		this.lastRecTime = System.currentTimeMillis();
		try {
			cache[index++] = source;
			if (index == cache.length) {
				index = 0;
				T[] result = cache;
				cache = createCache(SIZE);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T[]> cleanUp(String type) {
		try {
			List<DataQueueCache> list = CACHE_MAP.get(type);
			if (null == list || list.size() == 0) {
				return null;
			}
			long now = System.currentTimeMillis();
			ArrayList<T[]> result = new ArrayList<>();
			StringBuffer sb = new StringBuffer();
			sb.append("type:" + type + "[");
			for (DataQueueCache qc : list) {
				sb.append(qc.index + "->act:");
				if (now - qc.lastRecTime >= CLEANUP_TIME && qc.index > 0) {
					sb.append("cleanUp");
					int nowIndex = qc.index;
					qc.index = 0;
					T[] qcResult = (T[]) qc.cache;
					qc.cache = qc.createCache(SIZE);
					T[] array = Arrays.copyOf(qcResult, nowIndex);
					result.add(array);
				} else {
					sb.append("None");
				}
				sb.append(",");
			}
			sb.append("]");
			if (result.size() > 0) {
				return result;
			}
			return null;
		} finally {
		}
	}

	public static String Info() {
		try {
			StringBuilder sb = new StringBuilder();
			Set<String> types = CACHE_MAP.keySet();
			for (String type : types) {
				sb.append("type:");
				sb.append(type);
				sb.append("\t");
				List<DataQueueCache> list = CACHE_MAP.get(type);
				if (null == list || list.size() == 0) {
					continue;
				}
				int i = 1;
				for (DataQueueCache qc : list) {
					sb.append(i);
					sb.append(":");
					sb.append(qc.index);
					sb.append(",");
				}
				sb.append("|");
			}
			return sb.toString();
		} finally {
		}
	}

	public T[] getCache() {
		return cache;
	}

	public abstract T[] createCache(int size);

	public static void main(String[] args) {
		final DataQueueCache<String> dqc = new DataQueueCache<String>("a") {
			@Override
			public String[] createCache(int size) {
				return new String[size];
			}
		};
		ExecutorService exec = Executors.newCachedThreadPool();

		exec.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					dqc.addAndGet("12345");
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		exec.execute(new Runnable() {

			@Override
			public void run() {
				while (true) {
					List<String[]> c = cleanUp("a");
					System.err.println(c == null ? "null" : c.size());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}
}
