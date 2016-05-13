//package com.eversec.lte.kafka;
//
//import static com.eversec.lte.constant.SdtpConstants.SDTP_HEADER_LENGTH;
//import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_REQ;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.mina.core.buffer.IoBuffer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants.NotifyXDRType;
//import com.eversec.lte.constant.SdtpConstants.XDRInterface;
//import com.eversec.lte.kafka.producer.KafkaByteProducer;
//import com.eversec.lte.kafka.producer.KafkaProducer;
//import com.eversec.lte.model.XdrData;
//import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;
//import com.eversec.lte.model.compound.XdrUEMRSimple;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//import com.eversec.lte.model.single.XdrSingleSourceUEMR;
//import com.eversec.lte.sdtp.model.SdtpHeader;
//
///**
// * 
// * 发送自定义xdr到kafka中
// */
//public class KafkaOutputUtils {
//	private static Logger LOGGER = LoggerFactory.getLogger(KafkaOutputUtils.class);
//	
//	public static final String SIGNAL_CXDR_WITH_UEMR = "signal-cxdr-with-uemr";
//	public static final String S1U_CXDR_WITH_UEMR = "s1u-cxdr-with-uemr";
//	public static final String SIGNAL_CXDR_WITH_UEMR_TRYAGAIN = "signal-cxdr-with-uemr-tryagain";
//	public static final String FILLED_XDR = "filled-xdr";
//	
//	
//	public static final String SIGNAL_CXDR_FOR_STAT = "signal-cxdr-for-stat";
//	public static final String SIGNAL_CXDR_FOR_STAT_S1U = "signal-cxdr-for-stat-s1u";
//	
//	
////	public static final AtomicLong SEND_FOR_STAT = new AtomicLong(0);
////	public static final AtomicLong SEND_FOR_STAT_S1U = new AtomicLong(0);
////	
////	public static void  report(){
////		long statCount = SEND_FOR_STAT.get();
////		long s1ustatCount = SEND_FOR_STAT_S1U.get();
////		LOGGER.info("SEND_FOR_STAT:"+statCount +", SEND_FOR_STAT_S1U:"+s1ustatCount);
////		SEND_FOR_STAT.set(0);
////		SEND_FOR_STAT_S1U.set(0);
////	}
//
//	/**
//	 * 发送回填后的xdr到kafka（添加了模拟应用层所需的xdr类型标识）
//	 * 
//	 * @param producer
//	 * @param xdr
//	 */
//	@Deprecated
//	public void outputFilledXdr(KafkaByteProducer producer, XdrSingleSource xdr) {
//		if (xdr instanceof XdrSingleSourceS1U) {
//			XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) xdr;
//			byte[] src = s1u.toByteArray();
//			byte[] dest = new byte[1 + s1u.getBodyLength()];
//			dest[0] = (byte) NotifyXDRType.SINGLE;
//			System.arraycopy(src, 0, dest, 1, src.length);
//			producer.sendBytes(FILLED_XDR, dest);
//		} else if (xdr instanceof XdrSingleSource) {
//			byte[] src1 = xdr.getCommon().toByteArray();
//			byte[] src2 = xdr.toByteArray();
//			byte[] dest = new byte[1 + xdr.getCommon().getBodyLength()
//					+ xdr.getBodyLength()];
//			dest[0] = (byte) NotifyXDRType.SINGLE;
//			System.arraycopy(src1, 0, dest, 1, src1.length);
//			System.arraycopy(src2, 0, dest, 1 + src1.length, src2.length);
//			producer.sendBytes(FILLED_XDR, dest);
//		}
//	}
//	
//	
//	
//	
//	/**
//	 * filled xdr format to normal
//	 * 
//	 * @param producer
//	 * @param xdr
//	 */
//	public void outputFilledXdr(KafkaByteProducer producer, XdrSingleSource[] xdrs) {
//		
//		int  destSize = 0;
//		List<byte[]> destList = new ArrayList<>();
//		for (XdrSingleSource data : xdrs) {
//			if (data instanceof XdrSingleSourceS1U) {// s1u imsi
//				XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) data;
////				String imsi = s1u.getMobileCommon().getImsi();
//				byte[] src = s1u.toByteArray();
//				destSize += src.length;
//				destList.add(src);
//			} else if (data instanceof XdrSingleSource) {
//				XdrSingleSource single = (XdrSingleSource) data;
////				String imsi = single.getCommon().getImsi();
//
//				byte[] src1 = single.getCommon().toByteArray();
//				byte[] src2 = single.toByteArray();
//				byte[] dest = new byte[ single.getCommon().getBodyLength()
//						+ single.getBodyLength()];
//				System.arraycopy(src1, 0, dest, 0, src1.length);
//				System.arraycopy(src2, 0, dest, src1.length, src2.length);
//				
//				destSize += dest.length;
//				destList.add(dest);
//			}  
//		}
//		if (destSize>0) {
//			byte[] destAll = new byte[destSize];
//			int pos = 0;
//			for (byte[] b : destList) {
//				System.arraycopy(b, 0, destAll, pos, b.length);
//				pos += b.length;
//			}
//			
//			SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
//					+ destAll.length, NOTIFY_XDR_DATA_REQ,
//					1, (short)1);
//			byte[] sdtpAll = new byte[destSize+9];
//			System.arraycopy(SdtpHeader.toByteArray(header) , 0, sdtpAll, 0, 9);
//			System.arraycopy(destAll, 0, sdtpAll, 9, destAll.length);
//			producer.sendBytes(FILLED_XDR, sdtpAll);
//		}
//	}
//
//	/**
//	 * 发送自定义xdr至kafka
//	 * 
//	 * @param producer
//	 * @param data
//	 */
//	public void output(KafkaProducer producer, XdrData[] datas) {
//		if(SdtpConfig.IS_OUTPUT_XDR_FOR_COMP){
//			for (XdrData data : datas) {
//				outputForComp(producer, data);
//			}
//		}
//		if(SdtpConfig.IS_OUTPUT_XDR_FOR_STAT){
//			outputForStat(producer, datas);
//		}
//	}
//	
//	private void outputForStat(KafkaProducer producer, XdrData[] datas) {
//		try {
//			int  destSize = 0;
//			List<byte[]> destList = new ArrayList<>();
//			
//			int  s1udestSize = 0;
//			List<byte[]> s1udestList = new ArrayList<>();
//			
//			for (XdrData data:datas) {
//				if (data instanceof XdrSingleSourceS1U) {// s1u imsi
//					XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) data;
////					String imsi = s1u.getMobileCommon().getImsi();
//					byte[] src = s1u.toByteArray();
//					byte[] dest = new byte[1 + s1u.getBodyLength()];
//					dest[0] = (byte) XDRInterface.S1U;
//					System.arraycopy(src, 0, dest, 1, src.length);
//
//					s1udestSize += dest.length;
//					s1udestList.add(dest);
//				} else if (data instanceof XdrSingleSource) {
//					XdrSingleSource single = (XdrSingleSource) data;
////					String imsi = single.getCommon().getImsi();
//
//					byte[] src1 = single.getCommon().toByteArray();
//					byte[] src2 = single.toByteArray();
//					byte[] dest = new byte[1
//							+ single.getCommon().getBodyLength()
//							+ single.getBodyLength()];
//					dest[0] = (byte) single.getCommon().getInterface();
//					System.arraycopy(src1, 0, dest, 1, src1.length);
//					System.arraycopy(src2, 0, dest, 1 + src1.length,
//							src2.length);
//					
//					destSize += dest.length;
//					destList.add(dest);
//				}  
//			}
//			
//			if (s1udestSize>0) {
//				byte[] s1udestAll = new byte[s1udestSize];
//				int pos = 0;
//				for (byte[] b : s1udestList) {
//					System.arraycopy(b, 0, s1udestAll, pos, b.length);
//					pos += b.length;
//				}
////				SEND_FOR_STAT_S1U.addAndGet(s1udestList.size());
//				producer.sendBytes(SIGNAL_CXDR_FOR_STAT_S1U, null, s1udestAll);
//			}
//			if (destSize>0) {
//
//				byte[] destAll = new byte[destSize];
//				int pos = 0;
//				for (byte[] b : destList) {
//					System.arraycopy(b, 0, destAll,pos, b.length);
//					pos += b.length;
//				}
////				SEND_FOR_STAT.addAndGet(destList.size());
//				producer.sendBytes(SIGNAL_CXDR_FOR_STAT, null, destAll);//为统计增加//为统计增加
//			}
//			 
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	public void outputForComp(KafkaProducer producer, XdrData data) {
//		try {
//			if (data instanceof XdrSingleSourceS1U) {// s1u imsi
//				XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) data;
//				String imsi = s1u.getMobileCommon().getImsi();
//				byte[] src = s1u.toByteArray();
//				byte[] dest = new byte[1 + s1u.getBodyLength()];
//				dest[0] = (byte) XDRInterface.S1U;
//				System.arraycopy(src, 0, dest, 1, src.length);
//				if (StringUtils.isNoneBlank(imsi)) {
//					producer.sendBytes(S1U_CXDR_WITH_UEMR, imsi, dest);
//				}
//			} else if (data instanceof XdrSingleSource) {
//				if (data instanceof XdrSingleSourceUEMR) {// 信令合成不包含单接口uemr
//					return;
//				}
//				XdrSingleSource single = (XdrSingleSource) data;
//				String imsi = single.getCommon().getImsi();
//				
//				
//				byte[] src1 = single.getCommon().toByteArray();
//				byte[] src2 = single.toByteArray();
//				byte[] dest = new byte[1
//						+ single.getCommon().getBodyLength()
//						+ single.getBodyLength()];
//				dest[0] = (byte) single.getCommon().getInterface();
//				System.arraycopy(src1, 0, dest, 1, src1.length);
//				System.arraycopy(src2, 0, dest, 1 + src1.length,
//						src2.length);
//				if (StringUtils.isNoneBlank(imsi)) {
//					producer.sendBytes(SIGNAL_CXDR_WITH_UEMR, imsi, dest);
//				} 
//			} else if (data instanceof XdrCompoundSourceUEMR) {// 通过合成uemr话单存放自定义uemr话单信息
//				XdrCompoundSourceUEMR uemr = (XdrCompoundSourceUEMR) data;
//				String imsi = uemr.getCommon().getImsi();
//				if(SdtpConfig.IS_OUTPUT_XDR_FOR_COMP){
//					if (StringUtils.isNoneBlank(imsi)) {
//						XdrUEMRSimple simpleUemrXdr = new XdrUEMRSimple(imsi,
//								uemr.getLongitude(), uemr.getLatitude(), uemr
//										.getDateTime().getTime());
//						byte[] src = simpleUemrXdr.toByteArray();
//						byte[] dest = new byte[1 + simpleUemrXdr.getBodyLength()];
//						dest[0] = XDRInterface.SIMPLE_UEMR;
//						System.arraycopy(src, 0, dest, 1, src.length);
//						producer.sendBytes(SIGNAL_CXDR_WITH_UEMR, imsi, dest);
//						producer.sendBytes(S1U_CXDR_WITH_UEMR, imsi, dest);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) {
//		byte[] src1 = new byte[] { 1, 2, 3, 4, 5, 6 };
//		byte[] src2 = new byte[] { 7, 8, 9 };
//		byte[] dest = new byte[1 + src1.length + src2.length];
//		dest[0] = 100;
//		System.arraycopy(src1, 0, dest, 1, src1.length);
//		System.arraycopy(src2, 0, dest, 1 + src1.length, src2.length);
//		System.out.println(Arrays.toString(dest));
//	}
//}
