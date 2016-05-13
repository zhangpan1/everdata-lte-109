package com.eversec.lte.cache.back;
//package com.eversec.lte.cache;
//
//import static com.eversec.lte.processor.data.StaticData.REPLACED_COUNT;
//
//import com.eversec.common.cache.DataStore;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//import com.eversec.lte.processor.backfill.AbstractCmBackFill;
//import com.google.common.cache.RemovalCause;
//import com.google.common.cache.RemovalNotification;
//
//public class S1UPendingCache extends DataStore<XdrSingleSourceS1U> {
//
//	public S1UPendingCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//	}
//
//	@Override
//	public void onRemoval(
//			RemovalNotification<String, XdrSingleSourceS1U> notification) {
//		if (notification.getCause() != RemovalCause.REPLACED) {
//			AbstractCmBackFill.getBackFill(Thread.currentThread().getId())
//					.refillS1U(notification.getKey(), notification.getValue());
//		} else {
//			REPLACED_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	public XdrSingleSourceS1U load(String key) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
