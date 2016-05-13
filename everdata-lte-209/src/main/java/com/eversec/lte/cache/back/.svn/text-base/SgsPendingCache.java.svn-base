package com.eversec.lte.cache.back;
//package com.eversec.lte.cache;
//
//import com.eversec.common.cache.DataStore;
//import com.eversec.lte.model.single.XdrSingleSourceSGs;
//import com.eversec.lte.processor.backfill.AbstractCmBackFill;
//import com.google.common.cache.RemovalCause;
//import com.google.common.cache.RemovalNotification;
//
//import static com.eversec.lte.processor.data.StaticData.REPLACED_COUNT;
//
//public class SgsPendingCache extends DataStore<XdrSingleSourceSGs> {
//
//	public SgsPendingCache(long maxSize, String expireAfterWrite,
//			String expireAfterAccess) {
//		super(maxSize, expireAfterWrite, expireAfterAccess);
//	}
//
//	@Override
//	public void onRemoval(
//			RemovalNotification<String, XdrSingleSourceSGs> notification) {
//		if (notification.getCause() != RemovalCause.REPLACED) {
//			AbstractCmBackFill.getBackFill(Thread.currentThread().getId())
//					.refillSgs(notification.getKey(), notification.getValue());
//		} else {
//			REPLACED_COUNT.incrementAndGet();
//		}
//
//	}
//
//	@Override
//	public XdrSingleSourceSGs load(String key) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
