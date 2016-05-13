//package com.eversec.lte.kafka;
//
//import org.apache.mina.core.buffer.IoBuffer;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.constant.SdtpConstants.XDRInterface;
//import com.eversec.lte.kafka.producer.KafkaProducer;
//import com.eversec.lte.model.XdrData;
//import com.eversec.lte.model.single.XdrSingleSource;
//import com.eversec.lte.model.single.XdrSingleSourceS1U;
//
//public class KafkaOutputUtils2 extends KafkaOutputUtils{
//	public void outputX(KafkaProducer producer, XdrData[] datas,
//			IoBuffer destList, IoBuffer s1udestList) {
//		if (SdtpConfig.IS_OUTPUT_XDR_FOR_COMP) {
//			for (XdrData data : datas) {
//				outputForComp(producer, data);
//			}
//		}
//		destList.clear();
//		s1udestList.clear();
//
//		if (SdtpConfig.IS_OUTPUT_XDR_FOR_STAT) {
//			outputForStatX(producer, datas,  destList,   s1udestList);
//		}
//		destList.clear();
//		s1udestList.clear();
//	}
//
//	public  void outputForStatX(KafkaProducer producer, XdrData[] datas,
//			IoBuffer destList, IoBuffer s1udestList) {
//		try {
//			int destSize = 0;
//			// IoBuffer destList = IoBuffer.allocate(1024).setAutoExpand(true);
//
//			int s1udestSize = 0;
//			// IoBuffer s1udestList =
//			// IoBuffer.allocate(1024).setAutoExpand(true);
//
//			for (XdrData data : datas) {
//				if (data instanceof XdrSingleSourceS1U) {// s1u imsi
//					XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) data;
//					byte[] src = s1u.toByteArray();
//					s1udestSize += src.length + 1;
//					s1udestList.put((byte) XDRInterface.S1U);
//					s1udestList.put(src);
//
//				} else if (data instanceof XdrSingleSource) {
//					XdrSingleSource single = (XdrSingleSource) data;
//					byte[] src1 = single.getCommon().toByteArray();
//					byte[] src2 = single.toByteArray();
//					destSize += 1 + src1.length + src2.length;
//					destList.put((byte) single.getCommon().getInterface());
//					destList.put(src1);
//					destList.put(src2);
//				}
//			}
//
//			if (s1udestSize > 0) {
//				byte[] s1udestAll = new byte[s1udestSize];
//				s1udestList.flip();
//				s1udestList.get(s1udestAll);
//				producer.sendBytes(SIGNAL_CXDR_FOR_STAT_S1U, null, s1udestAll);
//			}
//			if (destSize > 0) {
//				byte[] destAll = new byte[destSize];
//				destList.flip();
//				destList.get(destAll);
//				producer.sendBytes(SIGNAL_CXDR_FOR_STAT, null, destAll);// 为统计增加//为统计增加
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
