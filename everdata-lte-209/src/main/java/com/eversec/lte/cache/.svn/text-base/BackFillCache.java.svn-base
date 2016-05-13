//package com.eversec.lte.cache;
//
//import com.eversec.common.cache.DataStore;
//import com.eversec.lte.processor.backfill.AbstractBackFill;
//import com.google.common.cache.RemovalNotification;
//
//public class BackFillCache extends DataStore<AbstractBackFill> {
//
//	public BackFillCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//	}
//
//	@Override
//	public void onRemoval(
//			RemovalNotification<String, AbstractBackFill> notification) {
//		AbstractBackFill backfill = notification.getValue();
//		if (backfill != null) {
//			backfill.returnResource();
//		}
//	}
//
//	@Override
//	public AbstractBackFill load(String key) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
