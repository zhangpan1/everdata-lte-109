package com.eversec.lte.cache.back;
//package com.eversec.lte.cache;
//
//import static com.eversec.lte.processor.data.StaticData.REPLACED_COUNT;
//
//import com.eversec.common.cache.DataStore;
//import com.eversec.lte.model.single.XdrSingleSourceX2;
//import com.eversec.lte.processor.backfill.AbstractCmBackFill;
//import com.google.common.cache.RemovalCause;
//import com.google.common.cache.RemovalNotification;
//
//public class X2PendingCache extends DataStore<XdrSingleSourceX2> {
//
//	public X2PendingCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//	}
//
//	@Override
//	public void onRemoval(
//			RemovalNotification<String, XdrSingleSourceX2> notification) {
//		if (notification.getCause() != RemovalCause.REPLACED) {
//			AbstractCmBackFill.getBackFill(Thread.currentThread().getId())
//					.refillX2(notification.getKey(), notification.getValue());
//		} else {
//			REPLACED_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	public XdrSingleSourceX2 load(String key) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
