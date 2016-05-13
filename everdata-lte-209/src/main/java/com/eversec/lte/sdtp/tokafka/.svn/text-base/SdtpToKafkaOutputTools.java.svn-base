package com.eversec.lte.sdtp.tokafka;

import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_CUSTOM_XDR_2_KAFKA;
import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_FILLED_XDR_2_KAFKA;
import static com.eversec.lte.constant.SdtpConstants.SDTP_HEADER_LENGTH;
import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_REQ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants.NotifyXDRType;
import com.eversec.lte.constant.SdtpConstants.XDRInterface;
import com.eversec.lte.kafka.producer.KafkaProducer;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.XdrData;
import com.eversec.lte.model.compound.XdrCompoundSourceUEMR;
import com.eversec.lte.model.compound.XdrUEMRSimple;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.sdtp.model.SdtpHeader;

/**
 * 
 * @author lirongzhi
 * 
 */
public class SdtpToKafkaOutputTools {

	private static final boolean TEST = true;

	private static Logger LOGGER = LoggerFactory
			.getLogger(SdtpToKafkaOutputTools.class);

	private static SdtpToKafkaOutputTools instance = new SdtpToKafkaOutputTools();

	public static void output(XdrData xdr) {
		SdtpToKafkaOutputTools.getInstance().output(new XdrData[] { xdr });
	}

	public static void outputFilled(XdrSingleSource xdr) {
		SdtpToKafkaOutputTools.getInstance().outputFilledXdr(
				new XdrSingleSource[] { xdr });
	}

	public static SdtpToKafkaOutputTools getInstance() {
		return instance;
	}

	public final String SIGNAL_CXDR_WITH_UEMR = "signal-cxdr-with-uemr";
	public final String S1U_CXDR_WITH_UEMR = "s1u-cxdr-with-uemr";
	public final String SIGNAL_CXDR_WITH_UEMR_TRYAGAIN = "signal-cxdr-with-uemr-tryagain";
	public final String FILLED_XDR = "filled-xdr";

	public final String SIGNAL_CXDR_FOR_STAT = "signal-cxdr-for-stat";
	public final String SIGNAL_CXDR_FOR_STAT_S1U = "signal-cxdr-for-stat-s1u";

	protected ArrayBlockingQueue<KafkaMessageItem> filledQueue = new ArrayBlockingQueue<KafkaMessageItem>(
			10000);

	protected ArrayBlockingQueue<KafkaMessageItem> customQueue = new ArrayBlockingQueue<KafkaMessageItem>(
			10000);

	private SdtpToKafkaOutputTools() {
		init();
	}

	public void report() {
		LOGGER.info("to_kafka filledQueue:" + filledQueue.size()
				+ "customQueue:" + customQueue.size());
	}

	private void init() {
		if (TEST) {
			// xdr
			if (IS_OUTPUT_CUSTOM_XDR_2_KAFKA) {// 判断是否发送自定义xdr至kafka（signal-cxdr-with-uemr,s1u-cxdr-with-uemr）
				initKafkaOutput(filledQueue);
			}
			if (IS_OUTPUT_FILLED_XDR_2_KAFKA) {// 发送回填后的xdr到kafka（添加了模拟应用层所需的xdr类型标识）
				initKafkaOutput(filledQueue);
			}
			if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_KAFKA) {// ?
				initKafkaOutput(customQueue);
			}
		}
	}

	private void initKafkaOutput(final BlockingQueue<KafkaMessageItem> queue) {
		for (int i = 0; i < SdtpConfig.getKafkaOutputThread(); i++) {
			LteMain.KAFKA_EXEC.execute(new Runnable() {
				@Override
				public void run() {

					while (true) {
						KafkaProducer producer = null;
						try {
							producer = new KafkaProducer();
							while (true) {
								Collection<KafkaMessageItem> list = new ArrayList<>();
								int count = queue.drainTo(list/* , 10000 */);
								if (count > 0) {
									for (KafkaMessageItem item : list) {
										String topic = item.getTopic();
										String key = item.getKey();
										byte[] bytes = item.getBytes();

										producer.sendBytes(topic, key, bytes);
									}
								} else {
									try {
										Thread.sleep(1);
									} catch (InterruptedException e) {
									}
								}
								list.clear();
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (producer != null) {
								producer.close();
							}
						}
						try {
							TimeUnit.SECONDS.sleep(3);
						} catch (InterruptedException e) {
						}
					}
				}
			});
		}
	}

	/**
	 * 发送回填后的xdr到kafka（添加了模拟应用层所需的xdr类型标识）
	 * 
	 * @param producer
	 * @param xdr
	 */
	@Deprecated
	public void outputFilledXdr(XdrSingleSource xdr) {
		try {
			if (xdr instanceof XdrSingleSourceS1U) {
				XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) xdr;
				byte[] src = s1u.toByteArray();
				byte[] dest = new byte[1 + s1u.getBodyLength()];
				dest[0] = (byte) NotifyXDRType.SINGLE;
				System.arraycopy(src, 0, dest, 1, src.length);
				// producer.sendBytes(FILLED_XDR, dest);
				if (TEST) {
					filledQueue
							.put(new KafkaMessageItem(FILLED_XDR, null, dest));
				}
			} else if (xdr instanceof XdrSingleSource) {
				byte[] src1 = xdr.getCommon().toByteArray();
				byte[] src2 = xdr.toByteArray();
				byte[] dest = new byte[1 + xdr.getCommon().getBodyLength()
						+ xdr.getBodyLength()];
				dest[0] = (byte) NotifyXDRType.SINGLE;
				System.arraycopy(src1, 0, dest, 1, src1.length);
				System.arraycopy(src2, 0, dest, 1 + src1.length, src2.length);
				// producer.sendBytes(FILLED_XDR, dest);
				if (TEST) {
					filledQueue
							.put(new KafkaMessageItem(FILLED_XDR, null, dest));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * filled xdr format to normal
	 * 
	 * @param producer
	 * @param xdr
	 */
	public void outputFilledXdr(XdrSingleSource[] xdrs) {
		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_KAFKA) {
			try {
				int destSize = 0;
				List<byte[]> destList = new ArrayList<>();
				for (XdrSingleSource data : xdrs) {
					if (data instanceof XdrSingleSourceS1U) {// s1u imsi
						XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) data;
						// String imsi = s1u.getMobileCommon().getImsi();
						byte[] src = s1u.toByteArray();
						destSize += src.length;
						destList.add(src);
					} else if (data instanceof XdrSingleSource) {
						XdrSingleSource single = (XdrSingleSource) data;
						// String imsi = single.getCommon().getImsi();

						byte[] src1 = single.getCommon().toByteArray();
						byte[] src2 = single.toByteArray();
						byte[] dest = new byte[single.getCommon()
								.getBodyLength() + single.getBodyLength()];
						System.arraycopy(src1, 0, dest, 0, src1.length);
						System.arraycopy(src2, 0, dest, src1.length,
								src2.length);

						destSize += dest.length;
						destList.add(dest);
					}
				}
				if (destSize > 0) {
					byte[] destAll = new byte[destSize];
					int pos = 0;
					for (byte[] b : destList) {
						System.arraycopy(b, 0, destAll, pos, b.length);
						pos += b.length;
					}

					SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
							+ destAll.length, NOTIFY_XDR_DATA_REQ, 1, (short) 1);
					byte[] sdtpAll = new byte[destSize + 9];
					System.arraycopy(SdtpHeader.toByteArray(header), 0,
							sdtpAll, 0, 9);
					System.arraycopy(destAll, 0, sdtpAll, 9, destAll.length);

					// producer.sendBytes(FILLED_XDR, sdtpAll);
					if (TEST) {
						filledQueue.put(new KafkaMessageItem(FILLED_XDR, null,
								sdtpAll));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 发送自定义xdr至kafka
	 * 
	 * @param producer
	 * @param data
	 */
	public void output(XdrData[] datas) {
		if (SdtpConfig.IS_OUTPUT_CUSTOM_XDR_2_KAFKA) {
			if (SdtpConfig.IS_OUTPUT_XDR_FOR_COMP) {
				for (XdrData data : datas) {
					outputForComp(data);
				}
			}
			if (SdtpConfig.IS_OUTPUT_XDR_FOR_STAT) {
				outputForStat(datas);
			}
		}
	}

	private void outputForStat(XdrData[] datas) {
		try {
			int destSize = 0;
			List<byte[]> destList = new ArrayList<>();

			int s1udestSize = 0;
			List<byte[]> s1udestList = new ArrayList<>();

			for (XdrData data : datas) {
				if (data instanceof XdrSingleSourceS1U) {// s1u imsi
					XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) data;
					// String imsi = s1u.getMobileCommon().getImsi();
					byte[] src = s1u.toByteArray();
					byte[] dest = new byte[1 + s1u.getBodyLength()];
					dest[0] = (byte) XDRInterface.S1U;
					System.arraycopy(src, 0, dest, 1, src.length);

					s1udestSize += dest.length;
					s1udestList.add(dest);
				} else if (data instanceof XdrSingleSource) {
					XdrSingleSource single = (XdrSingleSource) data;
					// String imsi = single.getCommon().getImsi();

					byte[] src1 = single.getCommon().toByteArray();
					byte[] src2 = single.toByteArray();
					byte[] dest = new byte[1
							+ single.getCommon().getBodyLength()
							+ single.getBodyLength()];
					dest[0] = (byte) single.getCommon().getInterface();
					System.arraycopy(src1, 0, dest, 1, src1.length);
					System.arraycopy(src2, 0, dest, 1 + src1.length,
							src2.length);

					destSize += dest.length;
					destList.add(dest);
				}
			}

			if (s1udestSize > 0) {
				byte[] s1udestAll = new byte[s1udestSize];
				int pos = 0;
				for (byte[] b : s1udestList) {
					System.arraycopy(b, 0, s1udestAll, pos, b.length);
					pos += b.length;
				}
				// SEND_FOR_STAT_S1U.addAndGet(s1udestList.size());
				// producer.sendBytes(SIGNAL_CXDR_FOR_STAT_S1U, null,
				// s1udestAll);
				if (TEST) {
					filledQueue.put(new KafkaMessageItem(
							SIGNAL_CXDR_FOR_STAT_S1U, null, s1udestAll));

				}
			}
			if (destSize > 0) {

				byte[] destAll = new byte[destSize];
				int pos = 0;
				for (byte[] b : destList) {
					System.arraycopy(b, 0, destAll, pos, b.length);
					pos += b.length;
				}
				// SEND_FOR_STAT.addAndGet(destList.size());
				// producer.sendBytes(SIGNAL_CXDR_FOR_STAT, null, destAll);//
				// 为统计增加//为统计增加
				if (TEST) {
					filledQueue.put(new KafkaMessageItem(SIGNAL_CXDR_FOR_STAT,
							null, destAll));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void outputForComp(XdrData data) {
		try {
			if (data instanceof XdrSingleSourceS1U) {// s1u imsi
				XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) data;
				String imsi = s1u.getMobileCommon().getImsi();
				byte[] src = s1u.toByteArray();
				byte[] dest = new byte[1 + s1u.getBodyLength()];
				dest[0] = (byte) XDRInterface.S1U;
				System.arraycopy(src, 0, dest, 1, src.length);
				if (StringUtils.isNoneBlank(imsi)) {
					// producer.sendBytes(S1U_CXDR_WITH_UEMR, imsi, dest);
					if (TEST) {
						customQueue.put(new KafkaMessageItem(
								S1U_CXDR_WITH_UEMR, imsi, dest));
					}
				}
			} else if (data instanceof XdrSingleSource) {
				if (data instanceof XdrSingleSourceUEMR) {// 信令合成不包含单接口uemr
					return;
				}
				XdrSingleSource single = (XdrSingleSource) data;
				String imsi = single.getCommon().getImsi();

				byte[] src1 = single.getCommon().toByteArray();
				byte[] src2 = single.toByteArray();
				byte[] dest = new byte[1 + single.getCommon().getBodyLength()
						+ single.getBodyLength()];
				dest[0] = (byte) single.getCommon().getInterface();
				System.arraycopy(src1, 0, dest, 1, src1.length);
				System.arraycopy(src2, 0, dest, 1 + src1.length, src2.length);
				if (StringUtils.isNoneBlank(imsi)) {
					// producer.sendBytes(SIGNAL_CXDR_WITH_UEMR, imsi, dest);
					if (TEST) {
						customQueue.put(new KafkaMessageItem(
								SIGNAL_CXDR_WITH_UEMR, imsi, dest));
					}
				}
			} else if (data instanceof XdrCompoundSourceUEMR) {// 通过合成uemr话单存放自定义uemr话单信息
				XdrCompoundSourceUEMR uemr = (XdrCompoundSourceUEMR) data;
				String imsi = uemr.getCommon().getImsi();
				if (SdtpConfig.IS_OUTPUT_XDR_FOR_COMP) {
					if (StringUtils.isNoneBlank(imsi)) {
						XdrUEMRSimple simpleUemrXdr = new XdrUEMRSimple(imsi,
								uemr.getLongitude(), uemr.getLatitude(), uemr
										.getDateTime().getTime());
						byte[] src = simpleUemrXdr.toByteArray();
						byte[] dest = new byte[1 + simpleUemrXdr
								.getBodyLength()];
						dest[0] = XDRInterface.SIMPLE_UEMR;
						System.arraycopy(src, 0, dest, 1, src.length);
						// producer.sendBytes(SIGNAL_CXDR_WITH_UEMR, imsi,
						// dest);
						// producer.sendBytes(S1U_CXDR_WITH_UEMR, imsi, dest);

						if (TEST) {
							customQueue.put(new KafkaMessageItem(
									SIGNAL_CXDR_WITH_UEMR, imsi, dest));
							customQueue.put(new KafkaMessageItem(
									S1U_CXDR_WITH_UEMR, imsi, dest));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main1(String[] args) {
		byte[] src1 = new byte[] { 1, 2, 3, 4, 5, 6 };
		byte[] src2 = new byte[] { 7, 8, 9 };
		byte[] dest = new byte[1 + src1.length + src2.length];
		dest[0] = 100;
		System.arraycopy(src1, 0, dest, 1, src1.length);
		System.arraycopy(src2, 0, dest, 1 + src1.length, src2.length);
		System.out.println(Arrays.toString(dest));
	}

	public static void main(String[] args) {
	}

	private void test() {
		// TODO Auto-generated method stub

	}
}
