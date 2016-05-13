package com.eversec.lte.kpi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.kafka.consumer.KafkaConsumer;
import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
import com.eversec.lte.kpi.config.KPIConfig;
import com.eversec.lte.kpi.util.FormatUtils;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.processor.decoder.XdrCustomBytesDecoder;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.utils.SdtpUtils;
import com.eversec.lte.vo.compound.CompMessage;

public class KafkaConsumerXdr {
	
	public static void main4(String[] args) {
		String topic = SdtpConfig.getKafkaXdrTopic();
		String host = SdtpConfig.getKafkaConsumerHostAndPort();
		System.out.println(topic);
		System.out.println(host);
		LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host, new KafkaConsumerHandler() {
			int ex = 0;
			@Override
			public void messageReceived(byte[] message) throws Exception {
				System.out.println(message.length);
				FileOutputStream o = new FileOutputStream(new File("/home/kpi/s1u_"+(++ex)+".data"));
				o.write(message);
				o.write(message);
				o.write(message);
				o.flush();
				o.close();
				System.out.println("flush");
				TimeUnit.SECONDS.sleep(5);
			}
		}));
	}

	public static void mainR(String[] args) {
		System. out .println("内存信息 :" + toMemoryInfo ());
		String topic = SdtpConfig.getKafkaXdrTopic();
		String host = SdtpConfig.getKafkaConsumerHostAndPort();
		Integer threadNum = KPIConfig.getThreadNum();
		System.out.println(topic);
		System.out.println(host);
		System.out.println(threadNum);
		for (int i = 0; i < threadNum; i++) {
			LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host,
				new KafkaConsumerXdrSignalling_S1U("work-"+i)     // 改版后-用户面
			)); 
		}
	}
	
	
	public static void mainO(String[] args) {
		System. out .println("内存信息 :" + toMemoryInfo ());
		String topic = SdtpConfig.getKafkaXdrTopic();
		String host = SdtpConfig.getKafkaConsumerHostAndPort();
		Integer threadNum = KPIConfig.getThreadNum();
		System.out.println(topic);
		System.out.println(host);
		System.out.println(threadNum);
		for (int i = 0; i < threadNum; i++) {
			if(KPIConfig.getKpiS1u()){
	//			new KafkaConsumerXdrSignallingKPI("work-"+i)   //4G   信令控制面
	//			new KafkaConsumerXdrUserKPI("work-"+i)          // 4G 用户面
	//			new KafkaConsumerXdrSignalling("work-"+i)          // 4G 用户面/信令控制面
	//			new KafkaConsumerXdrSignalling_all("work-"+i)          // 4G 用户面/信令控制面/Term终端
				LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host,
						new KafkaConsumerXdrSignalling_S1U("work-"+i)      // 改版后-用户面
				)); 
			}else if(KPIConfig.getKpiSig()){
				LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host,
						new KafkaConsumerXdrSignalling_S1C("work-"+i)   // 改版后-信令 控制面
				)); 
			}else if(KPIConfig.getKpiTerm()){
				LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host,
						new KafkaConsumerXdrSignalling_Term("work-"+i)      //Term终端
				)); 
			}
		}
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void mainOP(String[] args) {
		System. out .println("内存信息 :" + toMemoryInfo ());
		String topic = SdtpConfig.getKafkaXdrTopic();
		String host = SdtpConfig.getKafkaConsumerHostAndPort();
		Integer threadNum = KPIConfig.getThreadNum();
		System.out.println(topic);
		System.out.println(host);
		System.out.println(threadNum);
		for (int i = 0; i < threadNum; i++) {
			LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host,
					new KafkaConsumerHandlerAdapter(new KafkaConsumerHandler[]{
							new KafkaConsumerXdrSignalling_S1U("work-s1u-"+i),  // 改版后-用户面
							new KafkaConsumerXdrSignalling_S1C("work-s1c-"+i),  // 改版后-信令 控制面
							new KafkaConsumerXdrSignalling_Term("work-term-"+i), // 终端
							
					} )         
			));
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System. out .println("内存信息 :" + toMemoryInfo ());
		String topic = SdtpConfig.getKafkaXdrTopic();
		String host = SdtpConfig.getKafkaConsumerHostAndPort();
		Integer threadNum = KPIConfig.getThreadNum();
		System.out.println(topic);
		System.out.println(host);
		System.out.println(threadNum);
		for (int i = 0; i < threadNum; i++) {
			LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host,
					new KafkaConsumerHandlerAdapter(new KafkaConsumerHandler[]{
							new KafkaConsumerXdrSignalling_S1U_City("work-s1u-"+i),  // 改版后-用户面
							new KafkaConsumerXdrSignalling_S1C_City("work-s1c-"+i),  // 改版后-信令 控制面
							new KafkaConsumerXdrSignalling_Term_City("work-term-"+i),  // 终端
							new KafkaConsumerXdrSignalling_TAG_City("work-tag-"+i), // 用户标签 TAG
					})         
			));
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void mainDB(String[] args) {
		System. out .println("内存信息 :" + toMemoryInfo ());
		String topic = SdtpConfig.getKafkaXdrTopic();
		String host = SdtpConfig.getKafkaConsumerHostAndPort();
		Integer threadNum = KPIConfig.getThreadNum();
		System.out.println(topic);
		System.out.println(host);
		System.out.println(threadNum);
		for (int i = 0; i < threadNum; i++) {
			LteMain.KAFKA_EXEC.execute(new KafkaConsumer(topic, host,
					new KafkaConsumerHandlerAdapter(new KafkaConsumerHandler[]{
							new KafkaConsumerXdrSignalling_S1U_City("work-s1u-"+i),  // 改版后-用户面
							new KafkaConsumerXdrSignalling_S1C_City("work-s1c-"+i),  // 改版后-信令 控制面
							new KafkaConsumerXdrSignalling_Term_City("work-term-"+i)							
					} )         
			));
		}
	}
	
	
	
	
	
//	final XdrCustomBytesDecoder d = new XdrCustomBytesDecoder();
//	new KafkaConsumerHandler() {
//		@Override
//		public void messageReceived(byte[] message)
//				throws Exception {
////			CompMessage xdrs = d.decode(message);
////			for (XdrSingleSource xdr : xdrs.getXdrs()) {
////				byte[] id = xdr.getCommon().getXdrId();
////				StringBuilder str = new StringBuilder();
////				for (int i = 0; i < id.length; i++) {
////					int dd = id[i] & 0xff;
////					String hex = Integer.toHexString(dd);
////					str.append(hex.length() > 1 ? hex : "0" + hex);
////				}
////				String s = str.toString();
////				System.out.println(s);
////				if (s.equals("f80000000eb15cc3640000009c7226eb")
////						|| s.equals("f80000000ea74ea7640000009c05b89c")
////						|| s.equals("f80000000ea4a1d7640000009be97f62")
////						|| s.equals("f80000000ebdecb2640000009cf6fa8d")
////						|| s.equals("f80000000e90a7bd640000009b142b12")
////						|| s.equals("f80000000e9ac3d2640000009b815464")
////						|| s.equals("f80000000e8df1f0640000009af7957b")
////						|| s.equals("f80000000ec86877640000009d692023")) {
////					System.out.println(s);
////				}
////			}
//		}
//	}
	
	
	
	
	
	

	public static NotifyXDRDataReq decodeXDRData(byte[] bytes) {
		IoBuffer in = IoBuffer.wrap(bytes);
		SdtpHeader header = SdtpUtils.getHeader(in);
		byte[] load = new byte[bytes.length - SdtpConstants.SDTP_HEADER_LENGTH];
		in.get(load);
		NotifyXDRDataReq req = new NotifyXDRDataReq(load);
		req.setHeader(header);
		return req;
	}

	public static void main2(String[] args) {
		try {
			XdrCustomBytesDecoder d = new XdrCustomBytesDecoder();
			File f = new File("D://test//xdr//s1-mm1.data");
			byte[] b = new byte[(int) f.length()];
			FileInputStream fin = new FileInputStream(f);
			fin.read(b);
			fin.close();
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 1; i++) {
				CompMessage xdrs = d.decode(b);
				for (XdrSingleSource xdr : xdrs.getXdrs()) {
					XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
					System.out.println(s1mme.getCommon().getCity());
					int a = xdr.getCommon().getInterface();
					System.out.println(a);
					System.out.println(s1mme.getProcedureType());
					System.out.println(s1mme.getStartTime().getTime());
					System.out.println(s1mme.getEndTime().getTime());
					System.out.println(s1mme.getProcedureStatus());
					System.out.println(s1mme.getFailureCause());
					System.out.println(s1mme.getKeyword1());
					System.out.println(s1mme.getMmeIpAdd());
					System.out.println(s1mme.getEnbIpAdd());
					System.out.println(s1mme.getTac());
					System.out.println(s1mme.getCellID());
				}
			}
			long end = System.currentTimeMillis();
			System.out.println((end - begin) + "ms");
			System.out.println("------------------------");
			long begin1 = System.currentTimeMillis();
			// for(int i=0;i<1;i++){
			// DataInputStream dis=new DataInputStream(new
			// ByteArrayInputStream(b));
			// dis.skipBytes(3);
			// byte[] city = new byte[2];
			// dis.read(city);
			// System.out.println(FormatUtils.TBCDFormat(city));//city
			// System.out.println(dis.readUnsignedByte());//接口类型
			// dis.skipBytes(49);
			// System.out.println(dis.readUnsignedByte());//流程类型
			// byte[] time = new byte[8];
			// dis.read(time);
			// System.out.println(ConvertUtils.getLong(time));//starttime
			// dis.read(time);
			// System.out.println(ConvertUtils.getLong(time));//endtime
			// System.out.println(dis.readUnsignedByte());//流程状态
			// dis.skipBytes(2);
			// System.out.println(dis.readUnsignedShort());//失败原因
			// System.out.println(dis.readUnsignedByte());//keyword1
			// dis.skipBytes(45);
			// byte[] ip = new byte[16];
			// dis.read(ip);
			// System.out.println(FormatUtils.getIp(ip));//mmeip
			// dis.read(ip);
			// System.out.println(FormatUtils.getIp(ip));//enbip
			// dis.skipBytes(4);
			// System.out.println(dis.readUnsignedShort());//tac
			// System.out.println(dis.readInt());//cellid
			// }
			for (int i = 0; i < 1; i++) {
				IoBuffer dis = IoBuffer.wrap(b);
				dis.skip(3);
				byte[] city = new byte[2];
				dis.get(city);
				System.out.println(FormatUtils.TBCDFormat(city));// city
				System.out.println(dis.getUnsigned());// 接口类型
				dis.skip(49);
				System.out.println(dis.getUnsigned());// 流程类型
				System.out.println(dis.getLong());// starttime
				System.out.println(dis.getLong());// endtime
				System.out.println(dis.getUnsigned());// 流程状态
				dis.skip(2);
				System.out.println(dis.getUnsignedShort());// 失败原因
				System.out.println(dis.getUnsigned());// keyword1
				dis.skip(45);
				byte[] ip = new byte[16];
				dis.get(ip);
				System.out.println(FormatUtils.getIp(ip));// mmeip
				dis.get(ip);
				System.out.println(FormatUtils.getIp(ip));// enbip
				dis.skip(4);
				System.out.println(dis.getUnsignedShort());// tac
				System.out.println(dis.getUnsignedInt());// cellid
			}
			long end1 = System.currentTimeMillis();
			System.out.println((end1 - begin1) + "ms");

			System.out.println("------------------------");

			long begin2 = System.currentTimeMillis();
			KafkaConsumerXdrSignallingKPI kpi = new KafkaConsumerXdrSignallingKPI("");
			for (int i = 0; i < 1; i++) {
				kpi.filterAndFetch(b);
			}
			long end2 = System.currentTimeMillis();
			System.out.println((end2 - begin2) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main3(String[] args) {
		byte b = 110;
		System.out.println(Integer.toHexString(b));
	}
	
	public static void main1(String[] args){
		KafkaConsumerXdrSignallingKPI kpi1 = new KafkaConsumerXdrSignallingKPI("work1");
		KafkaConsumerXdrSignallingKPI kpi2 = new KafkaConsumerXdrSignallingKPI("work2");
		KafkaConsumerXdrSignallingKPI kpi3 = new KafkaConsumerXdrSignallingKPI("work3");
		KafkaConsumerXdrSignallingKPI kpi4 = new KafkaConsumerXdrSignallingKPI("work4");
		KafkaConsumerXdrSignallingKPI kpi5 = new KafkaConsumerXdrSignallingKPI("work5");
		KafkaConsumerXdrSignallingKPI kpi6 = new KafkaConsumerXdrSignallingKPI("work6");
		
		KafkaConsumerXdrSignallingKPI.owner.add(kpi1);
		KafkaConsumerXdrSignallingKPI.owner.add(kpi2);
		KafkaConsumerXdrSignallingKPI.owner.add(kpi3);
		KafkaConsumerXdrSignallingKPI.owner.add(kpi4);
		KafkaConsumerXdrSignallingKPI.owner.add(kpi5);
		KafkaConsumerXdrSignallingKPI.owner.add(kpi6);
		
		try {
			TimeUnit.MINUTES.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main5(String[] args) {
		// try{
		// XdrCustomBytesDecoder d = new XdrCustomBytesDecoder();
		// File f = new File("D://test//xdr//s1-mm1.data");
		// byte[] b = new byte[(int)f.length()];
		// FileInputStream fin = new FileInputStream(f);
		// fin.read(b);
		// fin.close();
		// byte[] c = new byte[b.length-1];
		// System.arraycopy(b, 1, c, 0, c.length);
		// for(int i=0;i<c.length;i++){
		// if(b[i+1]!=c[i])
		// System.out.println("error!");
		// }
		// FileOutputStream fo = new FileOutputStream(new
		// File("D://test//xdr//s1-mme.data"));
		// fo.write(b);
		// fo.write(c);
		// fo.write(c);
		// fo.write(c);
		// fo.write(c);
		// fo.write(c);
		// fo.flush();
		// fo.close();
		// }catch(Exception e){
		//
		// }
		try {
			for(int i=0;i<1000;i++){
				File f = new File("D://test//kpi//s1-mme.data");
				byte[] b = new byte[(int) f.length()];
				FileInputStream fin = new FileInputStream(f);
				fin.read(b);
				fin.close();

				KafkaConsumerXdrSignallingKPI kpi = new KafkaConsumerXdrSignallingKPI("");
				System.out.println(i+"...");
				kpi.filterAndFetch(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String toMemoryInfo() {
	       Runtime currRuntime = Runtime.getRuntime ();
	       int nFreeMemory = ( int ) (currRuntime.freeMemory() / 1024 / 1024);
	       int nTotalMemory = ( int ) (currRuntime.totalMemory() / 1024 / 1024);
	       return nFreeMemory + "M/" + nTotalMemory + "M(free/total)" ;
	}
	   
}
