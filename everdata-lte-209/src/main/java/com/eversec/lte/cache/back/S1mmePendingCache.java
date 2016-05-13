package com.eversec.lte.cache.back;
//package com.eversec.lte.cache;
//
//import static com.eversec.lte.processor.data.StaticData.REPLACED_COUNT;
//import com.eversec.common.cache.DataStore;
//import com.eversec.lte.model.single.XdrSingleSourceS1MME;
//import com.eversec.lte.processor.backfill.AbstractCmBackFill;
//import com.google.common.cache.RemovalCause;
//import com.google.common.cache.RemovalNotification;
//
//public class S1mmePendingCache extends DataStore<XdrSingleSourceS1MME> {
//
//	int index;
//
//	public S1mmePendingCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//	}
//
//	public S1mmePendingCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess, int index) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//		this.index = index;
//	}
//
//	@Override
//	public void onRemoval(
//			RemovalNotification<String, XdrSingleSourceS1MME> notification) {
//		if (notification.getCause() != RemovalCause.REPLACED) {
//			AbstractCmBackFill
//					.getBackFill(Thread.currentThread().getId())
//					.refillS1mme(notification.getKey(), notification.getValue());
//		} else {
//			REPLACED_COUNT.incrementAndGet();
//		}
//
//	}
//
//	@Override
//	public XdrSingleSourceS1MME load(String key) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
