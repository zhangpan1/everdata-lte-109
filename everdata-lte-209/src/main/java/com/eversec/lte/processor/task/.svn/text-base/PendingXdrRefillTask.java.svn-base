//package com.eversec.lte.processor.task;
//
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS10S11;
//import com.eversec.lte.model.single.XdrSingleSourceS1MME;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//import com.eversec.lte.model.single.XdrSingleSourceS6a;
//import com.eversec.lte.model.single.XdrSingleSourceSGs;
//import com.eversec.lte.model.single.XdrSingleSourceUEMR;
//import com.eversec.lte.model.single.XdrSingleSourceUu;
//import com.eversec.lte.model.single.XdrSingleSourceX2;
//import com.eversec.lte.processor.data.CacheData;
//import com.eversec.lte.processor.data.QueueData;
//import com.eversec.lte.utils.UUIDUtils;
//
///**
// * 初始化需重新回填的pending xdr
// * 
// * @author bieremayi
// * 
// */
//public class PendingXdrRefillTask implements Runnable {
//
//	@Override
//	public void run() {
//		while (true) {
//			try {
//				String uuid = UUIDUtils.getUUID();
//				XdrSingleSource data = QueueData.PENDING_XDR_DATA_QUEUE.take();
//				if (data instanceof XdrSingleSourceS10S11) {
//					CacheData.S11_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceS10S11) data);
//				} else if (data instanceof XdrSingleSourceS1MME) {
//					CacheData.S1MME_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceS1MME) data);
//				} else if (data instanceof XdrSingleSourceSGs) {
//					CacheData.SGS_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceSGs) data);
//				} else if (data instanceof XdrSingleSourceS6a) {
//					CacheData.S6A_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceS6a) data);
//				} else if (data instanceof XdrSingleSourceUEMR) {
//					CacheData.UEMR_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceUEMR) data);
//				} else if (data instanceof XdrSingleSourceX2) {
//					CacheData.X2_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceX2) data);
//				} else if (data instanceof XdrSingleSourceS1U) {
//					CacheData.S1U_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceS1U) data);
//				} else if (data instanceof XdrSingleSourceUu) {
//					CacheData.UU_PENDING_CACHE.update(uuid,
//							(XdrSingleSourceUu) data);
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//}
