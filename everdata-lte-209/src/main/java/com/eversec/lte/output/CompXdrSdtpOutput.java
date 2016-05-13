//package com.eversec.lte.output;
//
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//
//import org.apache.mina.core.session.IoSession;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.main.LteMain;
//import com.eversec.lte.model.compound.XdrCompoundSource;
//import com.eversec.lte.vo.DataQueueCache;
//
//public class CompXdrSdtpOutput {
//
//	/**
//	 * 待发送至SDTP中的合成XDR
//	 */
//	public static final ArrayBlockingQueue<XdrCompoundSource[]> ORIGINAL_CXDR_2_SDTP_QUEUE = new ArrayBlockingQueue<>(
//			SdtpConfig.getDataQueueCapacity() / DataQueueCache.SIZE);
//
//	protected static final String CACHE_TYPE = "comp_sdtp";
//
//	private static final ThreadLocal<DataQueueCache<XdrCompoundSource>> OGIGINAL_CXDR_CACHE = new ThreadLocal<DataQueueCache<XdrCompoundSource>>() {
//		protected DataQueueCache<XdrCompoundSource> initialValue() {
//			return new DataQueueCache<XdrCompoundSource>(CACHE_TYPE) {
//				public XdrCompoundSource[] createCache(int size) {
//					return new XdrCompoundSource[SIZE];
//				}
//			};
//		};
//	};
//
//	/**
//	 * 发送合成xdr到模拟应用层
//	 * 
//	 * @param data
//	 */
//	public static void output(XdrCompoundSource data) {
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_SDTP) {
//			try {
//				XdrCompoundSource[] cache = OGIGINAL_CXDR_CACHE.get()
//						.addAndGet(data);
//				if (cache != null) {
//					ORIGINAL_CXDR_2_SDTP_QUEUE.put(cache);
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void cleanUp() {
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_SDTP) {
//			try {
//				List<XdrCompoundSource[]> caches = DataQueueCache
//						.cleanUp(CACHE_TYPE);
//				if (caches != null && caches.size() > 0) {
//					for (XdrCompoundSource[] cache : caches) {
//						ORIGINAL_CXDR_2_SDTP_QUEUE.put(cache);
//					}
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * @param session
//	 */
//	public static void initOutputTask(final IoSession session) {
//		/**
//		 * 判断是否启用sdtp原始合成xdr发送
//		 */
//		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_SDTP) {
//			LteMain.EXEC.execute(new Runnable() {
//				@Override
//				public void run() {
////					while (true) {
////						XdrCompoundSource[] data = null;
////						try {
////							data = CompXdrSdtpOutput.ORIGINAL_CXDR_2_SDTP_QUEUE
////									.take();
////							for (int i = 0; i < data.length; i++) {
////								try {
////									// TODO
////								} catch (Exception e) {
////									session.close(true);
////									return;
////								}
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
