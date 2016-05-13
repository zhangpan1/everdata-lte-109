package com.eversec.lte.cache.back;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.eversec.common.cache.DataStore;
import com.eversec.lte.cache.data.XdrCacheData;
import com.google.common.cache.RemovalNotification;

public class StaticCache extends DataStore<XdrCacheData<AtomicLong>> {

	public StaticCache( ) {
		super(-1, "-1", "-1");
	}
	
	public StaticCache(long maxSize, String expireAfterWrite,
			String expireAfterAccess) {
		super(maxSize, expireAfterWrite, expireAfterAccess);
	}

	@Override
	public void onRemoval(
			RemovalNotification<String, XdrCacheData<AtomicLong>> notification) {
	}

	@Override
	public XdrCacheData<AtomicLong> load(String key) throws Exception {
		return new XdrCacheData<AtomicLong>(new AtomicLong(0));
	}

	public ConcurrentMap<String, XdrCacheData<AtomicLong>> asMap(){
		return cache.asMap();
	}
	
	public void invalidateAll(){
		 cache.invalidateAll();
	}

}
