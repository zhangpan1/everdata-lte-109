package com.eversec.lte.kpi.coll;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.kpi.bean.S1U_Value;
import com.eversec.lte.kpi.config.KPIConfig;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class ByteHashMap<K, V> extends HashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		System.out.println(1);
		return super.hashCode();
	}
	
	public static void main1(String[] args){
		ByteHashMap<ByteValue, String> bb = new ByteHashMap<ByteValue, String>();
//		b.put(new ByteValue(), "");
		byte[] a = {1,2,3,4,5,6,7,8,9,0,2,3,4,5,6,7,8,9,0,2,3,4,5,6,7,8,9,0,2,3,4,5,6,7,8,9,0,2,3,4,5,6,7,8,9,0,2,3,4,5,6,7,8,9,0,2,3,4,5,6,7,8,9,0};
		Map<BigInteger,String> m1 = new HashMap<BigInteger, String>();
		Map<ByteValue,String> m2 = new HashMap<ByteValue, String>();
		Random r = new Random();
		byte[][] data = new byte[1000000][100];
		for(int i=0;i<1000000;i++){
			for(int j=0;j<100;j++){
				data[i][j] = (byte)r.nextInt(255);
			}
		}
		long begin2 = System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			m2.put(new ByteValue(data[i]), "123");
//			new ByteValue(a).hashCode();
//			new ByteValue(a);
		}
		long end2 = System.currentTimeMillis();
		System.out.println(end2-begin2);
		
		
		long begin = System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			m1.put(new BigInteger(data[i]), "123");
//			new BigInteger(a).hashCode();
//			new BigInteger(a);
		}
		long end = System.currentTimeMillis();
		System.out.println(end-begin);
		
	}
	
	public static void main(String[] args){
		CacheLoader<ByteValue, String> loader = new CacheLoader<ByteValue, String>() {

			@Override
			public String load(ByteValue key) throws Exception {
				return "";
			}
		};

		RemovalListener<ByteValue, String> listener = new RemovalListener<ByteValue, String>() {

			@Override
			public void onRemoval(
					RemovalNotification<ByteValue, String> data) {
				System.out.println("remove");
			}
		};

		LoadingCache<ByteValue, String> cache = CacheBuilder
				.newBuilder()
				.removalListener(listener)
				.expireAfterWrite(KPIConfig.getTimeFlashInterval(),
						TimeUnit.MILLISECONDS)
				.maximumSize(KPIConfig.getTimeCacheSize()).build(loader);
		ByteValue b = new ByteValue(new byte[]{0,1,2,3});
		String v = cache.getUnchecked(b);
	}

}
