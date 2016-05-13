//package com.eversec.lte.output;
//
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.LinkedTransferQueue;
//import java.util.concurrent.TransferQueue;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.processor.data.StaticData;
//import com.eversec.lte.processor.statistics.BackFillStat;
//import com.eversec.lte.utils.FormatUtils;
//import com.eversec.lte.utils.SdtpUtils;
//import com.eversec.lte.vo.DataQueueCache;
//
//public class FilledXdrSdtpOutput {
//	private static Logger LOGGER = LoggerFactory.getLogger(FilledXdrSdtpOutput.class);
//	/**
//	 * 回填后待发送至SDTP中的单接口XDR
//	 */
////	public static final ArrayBlockingQueue<XdrSingleSource[]> FILLED_XDR_2_SDTP_QUEUE = new ArrayBlockingQueue<>(
////			SdtpConfig.getDataQueueCapacity() / DataQueueCache.SIZE);
//	
//	public static final TransferQueue<XdrSingleSource[]> FILLED_XDR_2_SDTP_QUEUE = new LinkedTransferQueue<>();
//
//	protected static final String CACHE_TYPE = "filled_sdtp";
//
//	private static final ThreadLocal<DataQueueCache<XdrSingleSource>> FILLED_XDR_CACHE = new ThreadLocal<DataQueueCache<XdrSingleSource>>() {
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
//		
//		BackFillStat.addAfterXdr(data);
//		
//		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_SDTP) {
//			//控制s1u发送
//			if(! SdtpConfig.IS_OUTPUT_FILLED_S1U_2_SDTP){
//				short Interface = data.getCommon().getInterface();
//				if(Interface == SdtpConstants.XDRInterface.S1U  ){
//					StaticData.ABANDON_S1U_XDR_2_SDTP_COUNT.incrementAndGet();
//					return ;
//				}
//			}
//			
//			
//			if( SdtpConfig.IS_FILTER_OUTPUT_FILLED_2_SDTP){
//				String[] cityCodes = SdtpConfig.FILTER_OUTPUT_FILLED_2_SDTP_CITYCODE;
//				String[] interfaces = SdtpConfig.FILTER_OUTPUT_FILLED_2_SDTP_INTERFACE;
//				short Interface = data.getCommon().getInterface();
//				String city = data.getCommon().getCity();
//				if(! FormatUtils.arrayContans(cityCodes, city) || ! FormatUtils.arrayContans(interfaces, ""+Interface)){
//					return;
//				}
//			}
//			
//			try {
//				XdrSingleSource[] cache = FILLED_XDR_CACHE.get()
//						.addAndGet(data);
//				if (cache != null) {
//					FILLED_XDR_2_SDTP_QUEUE.transfer(cache);
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void cleanUp() {
//		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_SDTP) {
//			try {
//				List<XdrSingleSource[]> caches = DataQueueCache
//						.cleanUp(CACHE_TYPE);
//				if (caches != null && caches.size() > 0) {
//					for (XdrSingleSource[] cache : caches) {
//						FILLED_XDR_2_SDTP_QUEUE.transfer(cache);
//					}
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
////	public static void initOutputTask(final IoSession session) {
////		/**
////		 * 判断是否通过sdtp发送回填后单接口xdr
////		 */
////		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_SDTP) {
////			LteMain.EXEC.execute(new Runnable() {
////				@Override
////				public void run() {
////					while (session.isConnected()) {
////						XdrSingleSource[] data = null;
////						try {
////							data = FilledXdrSdtpOutput.FILLED_XDR_2_SDTP_QUEUE
////									.take();
////							for (XdrSingleSource xdr : data) {
////								try {
////									// TODO
////								} catch (Exception e) {
////									e.printStackTrace();
////									session.close(true);
////									return;
////								}
////							}
////						} catch (Exception e) {
////							e.printStackTrace();
////						}
////					}
////				}
////
////			});
////		}
////	}
//	
//	public static void report() {
//		LOGGER.info(FilledXdrSdtpOutput.class.getSimpleName()+":"+FILLED_XDR_2_SDTP_QUEUE.size());
//	}
//}
