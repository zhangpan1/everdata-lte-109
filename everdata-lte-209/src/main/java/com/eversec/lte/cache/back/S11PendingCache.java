package com.eversec.lte.cache.back;
//package com.eversec.lte.cache;
//
//import static com.eversec.lte.processor.data.StaticData.REPLACED_COUNT;
//
//import com.eversec.common.cache.DataStore;
//import com.eversec.lte.model.single.XdrSingleSourceS10S11;
//import com.eversec.lte.processor.backfill.AbstractCmBackFill;
//import com.google.common.cache.RemovalCause;
//import com.google.common.cache.RemovalNotification;
//
//public class S11PendingCache extends DataStore<XdrSingleSourceS10S11> {
//
//	public S11PendingCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//	}
//
//	@Override
//	public void onRemoval(
//			RemovalNotification<String, XdrSingleSourceS10S11> notification) {
//		if (notification.getCause() != RemovalCause.REPLACED) {
//			AbstractCmBackFill.getBackFill(Thread.currentThread().getId())
//					.refillS11(notification.getKey(), notification.getValue());
//		} else {
//			REPLACED_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	public XdrSingleSourceS10S11 load(String key) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
