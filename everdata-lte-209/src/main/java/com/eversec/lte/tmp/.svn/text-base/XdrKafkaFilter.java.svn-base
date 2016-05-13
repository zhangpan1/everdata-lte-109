package com.eversec.lte.tmp;

import static com.eversec.lte.constant.SdtpConstants.XDRInterface.CELL_MR;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S11;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S1MME;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.S6A;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.SGS;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UE_MR;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.UU;
import static com.eversec.lte.constant.SdtpConstants.XDRInterface.X2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.kafka.consumer.KafkaConsumer;
import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
import com.eversec.lte.kafka.producer.KafkaByteProducer;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceCellMR;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.processor.decoder.XdrSingleBytesDecoder;
import com.eversec.lte.processor.statistics.StaticUtil;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.utils.SdtpUtils;


/**
 * 测试工具类，用于整理部分数据
 * 
 */
public class XdrKafkaFilter {

	public static volatile boolean FINISH = false;

	public static List<XdrInfo> CACHE_DATA_LIST = Collections
			.synchronizedList(new ArrayList<XdrInfo>());

	public static long XDR_STARTTIME = -1;
	public static long XDR_ENDTIME = -1;

	public static List<KafkaConsumer> CONSUMERS = new ArrayList<>();
	public static Map<Short, AtomicLong> COUNTER = new HashMap<>();

	public static Map<Short, StartIndex> START_INDEX = new HashMap<>();

	public static AtomicLong ALL_RECE = new AtomicLong(0);

	public static void main(String[] args) {
		if (args == null || args.length != 5) {
			args = new String[5];
			args[0] = "2014:09:25:03:00:00";
			args[1] = "2014:09:25:04:00:00";
			args[2] = "10";
			args[3] = "xdr3";
		}
		System.out
				.println("-----------------------XdrKafkaFilter-----------------------------");
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy:MM:dd:HH:mm:ss");
			XDR_STARTTIME = format.parse(args[0]).getTime();
			XDR_ENDTIME = format.parse(args[1]).getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ExecutorService exec = Executors.newCachedThreadPool();
		initXdrDataConsumer(exec);

		ScheduledExecutorService sexec = Executors
				.newSingleThreadScheduledExecutor();
		sexec.schedule(new XdrCacheSender(args[3]), Integer.parseInt(args[2]),
				TimeUnit.MINUTES);

		sexec.scheduleAtFixedRate(new LogDetail(), 10, 10, TimeUnit.SECONDS);
	}

	/**
	 * 从kafka获取xdr数据
	 */
	public static void initXdrDataConsumer(ExecutorService exec) {
		for (int i = 0; i < SdtpConfig.getKafkaXdrConsumerThread(); i++) {
			KafkaConsumer consumer = new KafkaConsumer("xdr",
					SdtpConfig.getKafkaConsumerHostAndPort(),
					new XdrFilterHandler());
			exec.execute(consumer);
			CONSUMERS.add(consumer);
		}
	}

	static class LogDetail implements Runnable {

		@Override
		public void run() {
			System.out.println(ALL_RECE);
			System.out.println(COUNTER);
		}
	}

	static class XdrFilterHandler implements KafkaConsumerHandler {
		XdrSingleBytesDecoder d = new XdrSingleBytesDecoder();

		@Override
		public void messageReceived(byte[] message) throws Exception {
			if (FINISH) {
				return;
			}
			NotifyXDRDataReq req = decodeXDRData(message);
			List<XdrSingleSource> list = d.decode(req.getLoad());
			for (XdrSingleSource xdr : list) {
				ALL_RECE.incrementAndGet();
				XdrSingleCommon common = xdr.getCommon();
				startIndexXdrCount(common);
				StaticUtil.statXdrType(common.getInterface());
				long startTime = getProcedureStartTime(xdr);
				if(xdr instanceof XdrSingleSourceCellMR){
					System.out.println("cellMr:"+startTime+","+common);
					FINISH=true;
					return;
				}
				if (startTime >= XDR_STARTTIME && startTime <= XDR_ENDTIME) {
					CACHE_DATA_LIST.add(new XdrInfo(xdr, startTime));
					statXdr(common);
					startIndexXdrCountPrint(common);
				}
			}
		}
	}

	static NotifyXDRDataReq decodeXDRData(byte[] bytes) {
		IoBuffer in = IoBuffer.wrap(bytes);
		SdtpHeader header = SdtpUtils.getHeader(in);
		byte[] load = new byte[bytes.length - SdtpConstants.SDTP_HEADER_LENGTH];
		in.get(load);
		NotifyXDRDataReq req = new NotifyXDRDataReq(load);
		req.setHeader(header);
		return req;
	}

	public static void startIndexXdrCountPrint(XdrSingleCommon common) {
		
		short inf = common.getInterface();
		StartIndex si = START_INDEX.get(inf);
		if(si == null){
			System.out.println("error "+inf);
		}else{
			if( ! si.find ){
				System.out.println("IndexXdrStart:" + inf + "-"
						+ START_INDEX.get(inf).start.get());
				 si.find = true;
			}
		}

	}

	public static void startIndexXdrCount(XdrSingleCommon common) {
		short inf = common.getInterface();
		StartIndex si = START_INDEX.get(inf);
		if(si == null){
			START_INDEX.put(inf, new StartIndex());
		}else{
			if( ! si.find ){
				si.start.incrementAndGet();
			}
		}

	}

	static class StartIndex {
		protected transient AtomicLong start = new AtomicLong(0);
		protected transient boolean find = false;
	}

	public static void statXdr(XdrSingleCommon common) {
		short inf = common.getInterface();
		if (COUNTER.containsKey(inf)) {
			COUNTER.get(inf).incrementAndGet();
		} else {
			COUNTER.put(inf, new AtomicLong(0));
		}

	}

	static class XdrInfo {
		XdrSingleSource xdr;
		long startTime;

		public XdrInfo(XdrSingleSource xdr, long startTime) {
			this.xdr = xdr;
			this.startTime = startTime;
		}
	}

	static class XdrCacheSender implements Runnable {

		private String topic;

		public XdrCacheSender(String topic) {
			this.topic = topic;
		}

		@Override
		public void run() {
			FINISH = true;
//			for (KafkaConsumer consumer : CONSUMERS) {
//				// consumer.close();
//			}
			CONSUMERS.clear();

			System.out
					.println("--------------------------------------------------");
			System.out.println("finish");
			System.out.println("find:" + CACHE_DATA_LIST.size());
			Collections.sort(CACHE_DATA_LIST, new Comparator<XdrInfo>() {
				@Override
				public int compare(XdrInfo o1, XdrInfo o2) {
					return (int) (o1.startTime - o2.startTime);
				}
			});
			KafkaByteProducer producer = new KafkaByteProducer(SdtpConfig.getKafkaMetadataBrokerList());
			long num = 0;
			for (XdrInfo xinfo : CACHE_DATA_LIST) {
				try {
					byte[] load = getXdrToBytes(xinfo.xdr);
					NotifyXDRDataReq req = new NotifyXDRDataReq(load);
					req.setHeader(new SdtpHeader(load.length + 9, 5, num++,
							(short) 1));
					producer.sendBytes(topic, req.toIoBuffer().array());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			producer.close();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			System.out.println(COUNTER);
			System.exit(0);
		}
	}

	public static byte[] getXdrToBytes(XdrSingleSource data) {
		int totalLength = 0;
		int commonLen = (data instanceof XdrSingleSourceS1U) ? 0 : data
				.getCommon().getBodyLength();
		totalLength += commonLen + data.getBodyLength();
		IoBuffer buffer = IoBuffer.allocate(totalLength);
		buffer.put(data.getCommon().toByteArray());
		buffer.put(data.toByteArray());
		return buffer.array();
	}

	/**
	 * 获取合成信令所需的信息
	 * 
	 * @param xdr
	 * @return
	 */
	private static long getProcedureStartTime(XdrSingleSource xdr) {
		XdrSingleCommon common = xdr.getCommon();
		int Interface = common.getInterface();
		long procedureStartTime = -1;
		@SuppressWarnings("unused")
		long procedureEndTime = -1;
		switch (Interface) {
		case UU:
			XdrSingleSourceUu uu = (XdrSingleSourceUu) xdr;
			procedureStartTime = uu.getProduceStartTime();
			procedureEndTime = uu.getProduceEndTime();
			break;
		case X2:
			XdrSingleSourceX2 x2 = (XdrSingleSourceX2) xdr;
			procedureStartTime = x2.getProduceStartTime();
			procedureEndTime = x2.getProduceEndTime();
			break;
		case UE_MR:
			XdrSingleSourceUEMR uemr = (XdrSingleSourceUEMR) xdr;
			procedureStartTime = uemr.getProduceStartTime();
			procedureEndTime = uemr.getProduceEndTime();
			break;
		case CELL_MR:
			XdrSingleSourceCellMR cellmr = (XdrSingleSourceCellMR) xdr;
			procedureStartTime = cellmr.getProduceStartTime();
			procedureEndTime = cellmr.getProduceEndTime();
			break;
		case S1MME:
			XdrSingleSourceS1MME s1mme = (XdrSingleSourceS1MME) xdr;
			procedureStartTime = s1mme.getProduceStartTime();
			procedureEndTime = s1mme.getProduceEndTime();
			break;
		case S6A:
			XdrSingleSourceS6a s6a = (XdrSingleSourceS6a) xdr;
			procedureStartTime = s6a.getProduceStartTime();
			procedureEndTime = s6a.getProduceEndTime();
			break;
		case S11:
			XdrSingleSourceS10S11 s11 = (XdrSingleSourceS10S11) xdr;
			procedureStartTime = s11.getProduceStartTime();
			procedureEndTime = s11.getProduceEndTime();
			break;
		case SGS:
			XdrSingleSourceSGs sgs = (XdrSingleSourceSGs) xdr;
			procedureStartTime = sgs.getProduceStartTime();
			procedureEndTime = sgs.getProduceEndTime();
			break;
		default:
			break;
		}
		return procedureStartTime;
	}
}
