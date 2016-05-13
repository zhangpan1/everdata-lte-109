//package com.eversec.lte.output;
//
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//
//import org.apache.mina.core.session.IoSession;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.main.LteMain;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.vo.DataQueueCache;
//
//public class OriginalXdrSdtpOutput {
//
//	/**
//	 * 回填后待发送至SDTP中的单接口XDR
//	 */
//	public static final ArrayBlockingQueue<XdrSingleSource[]> ORIGINAL_XDR_2_SDTP_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity() / DataQueueCache.SIZE);
//
//	protected static final String CACHE_TYPE = "original";
//
//	private static final ThreadLocal<DataQueueCache<XdrSingleSource>> ORIGINAL_XDR_CACHE = new ThreadLocal<DataQueueCache<XdrSingleSource>>() {
//		protected DataQueueCache<XdrSingleSource> initialValue() {
//			return new DataQueueCache<XdrSingleSource>(CACHE_TYPE) {
//				public XdrSingleSource[] createCache(int size) {
//					return new XdrSingleSource[SIZE];
//				}
//			};
//		};
//	};
//
//	/**
//	 * 发送单接口xdr到模拟应用层
//	 * 
//	 * @param data
//	 */
//	public static void output(XdrSingleSource data) {
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_SDTP) {
//			try {
//				XdrSingleSource[] cache = ORIGINAL_XDR_CACHE.get().addAndGet(
//						data);
//				if (cache != null) {
//					ORIGINAL_XDR_2_SDTP_QUEUE.put(cache);
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void cleanUp() {
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_SDTP) {
//			try {
//				List<XdrSingleSource[]> caches = DataQueueCache
//						.cleanUp(CACHE_TYPE);
//				if (caches != null && caches.size() > 0) {
//					for (XdrSingleSource[] cache : caches) {
//						ORIGINAL_XDR_2_SDTP_QUEUE.put(cache);
//					}
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void initOutputTask(final IoSession session) {
//		/**
//		 * 判断是否启用sdtp原始单接口xdr发送
//		 */
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_SDTP) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
////					while (session.isConnected()) {
////						try {
////							NotifyXDRDataReq data = QueueData.ORIGINAL_XDR_SDTP_OUTPUT_QUEUE
////									.take();
////							try {
////								// TODO
////							} catch (Exception e) {
////								session.close(true);
////								return;
////							}
////						} catch (Exception e) {
////							e.printStackTrace();
////						}
////					}
//				}
//
//			});
//		}
//	}
//	
//	public static void report() {
//		
//	}
//}
