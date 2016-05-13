package com.eversec.lte.cache.back;
//package com.eversec.lte.cache;
//
//import static com.eversec.lte.processor.data.StaticData.REPLACED_COUNT;
//
//import com.eversec.common.cache.DataStore;
//import com.eversec.lte.model.single.XdrSingleSourceUEMR;
//import com.eversec.lte.processor.backfill.AbstractCmBackFill;
//import com.google.common.cache.RemovalCause;
//import com.google.common.cache.RemovalNotification;
//
//public class UemrPendingCache extends DataStore<XdrSingleSourceUEMR> {
//
//	public UemrPendingCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//	}
//
//	@Override
//	public void onRemoval(
//			RemovalNotification<String, XdrSingleSourceUEMR> notification) {
//		if (notification.getCause() != RemovalCause.REPLACED) {
//			AbstractCmBackFill.getBackFill(Thread.currentThread().getId())
//					.refillUEMR(notification.getKey(), notification.getValue());
//		} else {
//			REPLACED_COUNT.incrementAndGet();
//		}
//	}
//
//	@Override
//	public XdrSingleSourceUEMR load(String key) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
