package com.eversec.lte.processor.backfill;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.util.internal.ConcurrentWeakKeyHashMap;

/**
 * 临时缓存 weak key 增加时间设定
 * 
 * @author lirongzhi
 * 
 */
// 暂不实现map接口
public class BatchWeakHashMap {

	private class WeakValue {
		String value;
		long ttl = -1;
		long time = System.currentTimeMillis();

		public WeakValue(String value, long ttl) {
			super();
			this.value = value;
			this.ttl = ttl;
		}

		public boolean expire() {
			if (ttl < 0) {
				return false;
			} else if (System.currentTimeMillis() - time < ttl) {
				return true;
			} else {
				return false;
			}
		}
	}

	public Map<Character, ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>>> newMap = new HashMap();
	public final char DEFALUT_KEY = ' ';
	{
		for (int i = 0; i <= 9; i++) {
			newMap.put(
					(i + "").charAt(0),
					new ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>>());
		}
		newMap.put(
				DEFALUT_KEY,
				new ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>>());
	}

	public String get(String key) {
		ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>> map = getKey(key);
		if (null == map) {
			return null;
		}
		WeakReference<WeakValue> ref = map.get(key);
		if (ref != null) {
			WeakValue wv = ref.get();
			if (wv != null) {
				if (wv.expire()) {
					map.remove(key);
					return null;
				} else {
					return wv.value;
				}
			}
		}
		return null;
	}

	public void put(String key, String data) {
		ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>> map = getKey(key);
		if (null == map) {
			return;
		}
		map.put(key, new WeakReference<WeakValue>(new WeakValue(data, -1)));
	}

	public void put(String key, String data, long ttl) {
		ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>> map = getKey(key);
		if (null == map) {
			return;
		}
		map.put(key, new WeakReference<WeakValue>(new WeakValue(data,
				ttl * 1000)));
	}

	private ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>> getKey(
			String key) {
		if (key == null || key.length() == 0) {
			return newMap.get(DEFALUT_KEY);
		}
		char last = key.charAt(key.length() - 1);
		ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>> result = newMap
				.get(last);
		if (null == result) {
			return newMap.get(DEFALUT_KEY);
		}
		return result;
	}

	private ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>> getChar(
			char lastCharacter) {
		ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>> result = newMap
				.get(lastCharacter);
		if (null == result) {
			return newMap.get(DEFALUT_KEY);
		}
		return result;
	}

	public String sizes() {
		String result = "";
		for (Entry<Character, ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>>> m : newMap
				.entrySet()) {
			result += m.getKey() + ":" + m.getValue().size() + ",";
		}
		return result;
	}

	public int size() {
		int result = 0;
		for (Entry<Character, ConcurrentWeakKeyHashMap<String, WeakReference<WeakValue>>> m : newMap
				.entrySet()) {
			result += m.getValue().size();
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		// Thread.sleep(10000);
		// // WeakHashMap<Object, Object> wh = new WeakHashMap<>();
		// ConcurrentWeakKeyHashMap<Object, Object> wh = new
		// ConcurrentWeakKeyHashMap<>();
		// for(long i = 0 ;;i++){
		// wh.put(i, new WeakReference<>(new byte[1024*1024]));
		// }
		// -Xmx5m -XX:+PrintGCDetails
		// -Xms1024m -Xmx1024m 500000

		final BatchWeakHashMap map = new BatchWeakHashMap();
		for (int i = 0; i < 1000000; i++) {
			map.put(new String(i + ""), new String(i + ""));
		}
		new Thread() {
			public void run() {
				for (;;) {
				}
			}

			private void test() {
			};
		}.start();

		for (int i = 0;; i++) {
			System.out.println(map.size());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
