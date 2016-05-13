package com.eversec.lte.cache;

import java.util.concurrent.ConcurrentMap;

import com.eversec.common.cache.DataStore;
import com.eversec.lte.cache.data.RuleData;
import com.google.common.cache.RemovalNotification;

public class RuleCache extends DataStore<RuleData> {

	public RuleCache(long maxSize, String expireAfterWrite,
			String expireAfterAccess, boolean isRemovalAsyn) {
		super(maxSize, expireAfterWrite, expireAfterAccess, isRemovalAsyn);
	}

	public RuleCache(long maxSize, String expireAfterWrite,
			String expireAfterAccess) {
		super(maxSize, expireAfterWrite, expireAfterAccess);
	}

	public RuleCache(String maxMemorySize, String expireAfterWrite,
			String expireAfterAccess, boolean isRemovalAsyn) {
		super(maxMemorySize, expireAfterWrite, expireAfterAccess, isRemovalAsyn);
	}

	public RuleCache(String maxMemorySize, String expireAfterWrite,
			String expireAfterAccess) {
		super(maxMemorySize, expireAfterWrite, expireAfterAccess);
	}

	@Override
	public void onRemoval(RemovalNotification<String, RuleData> notification) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RuleData load(String key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public  ConcurrentMap<String,RuleData> asMap(){
		return cache.asMap();
	}

}
