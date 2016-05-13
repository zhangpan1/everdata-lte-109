package com.eversec.lte.sdtp.tosdtp;

import static com.eversec.lte.constant.SdtpConstants.SDTP_HEADER_LENGTH;
import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_REQ;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.model.XdrData;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.processor.statistics.BackFillStat;
import com.eversec.lte.sdtp.client.SdtpClientLteIoHandler;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.vo.DataQueueCache;

/**
 * sdtp 输出功能类
 * 
 * @author lirongzhi
 * 
 */
public class SdtpToSdtpOutputTools {

	private static Logger LOGGER = LoggerFactory
			.getLogger(SdtpToSdtpOutputTools.class);

	private static SdtpToSdtpOutputTools instance = new SdtpToSdtpOutputTools();

	public static SdtpToSdtpOutputTools getInstance() {
		return instance;
	}

	public static void output(XdrData xdr) {
		SdtpToSdtpOutputTools.getInstance().outputXdr(xdr);
	}

	protected ArrayBlockingQueue<NotifyXDRDataReq> singleQueue = new ArrayBlockingQueue<NotifyXDRDataReq>(10000);

	protected ArrayBlockingQueue<NotifyXDRDataReq> compQueue = new ArrayBlockingQueue<NotifyXDRDataReq>(10000);
	
	protected SdtpToSdtpOutputTools() {

		init();
	}

	private void init() {

	}

	protected static final String CACHE_TYPE_FILLED = "filled_sdtp";

	public static int SIZE = 10;

	private final ThreadLocal<DataQueueCache<XdrSingleSource>> FILLED_XDR_CACHE = new ThreadLocal<DataQueueCache<XdrSingleSource>>() {
		protected DataQueueCache<XdrSingleSource> initialValue() {
			return new DataQueueCache<XdrSingleSource>(CACHE_TYPE_FILLED) {
				public XdrSingleSource[] createCache(int size) {
					return new XdrSingleSource[SIZE];
				}
			};
		};
	};

	protected static final String CACHE_TYPE_COMP = "comp_sdtp";

	private static final ThreadLocal<DataQueueCache<XdrCompoundSource>> OGIGINAL_CXDR_CACHE = new ThreadLocal<DataQueueCache<XdrCompoundSource>>() {
		protected DataQueueCache<XdrCompoundSource> initialValue() {
			return new DataQueueCache<XdrCompoundSource>(CACHE_TYPE_COMP) {
				public XdrCompoundSource[] createCache(int size) {
					return new XdrCompoundSource[SIZE];
				}
			};
		};
	};

	public void report() {
		LOGGER.info("singleQueue:" + singleQueue.size() + ", compQueue:"
				+ compQueue.size());
	}

	public void cleanUp() {
		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_SDTP) {
			try {
				List<XdrSingleSource[]> caches = DataQueueCache
						.cleanUp(CACHE_TYPE_FILLED);
				if (caches != null && caches.size() > 0) {
					for (XdrSingleSource[] cache : caches) {
						NotifyXDRDataReq req = createNotifyXDRDataReq(Arrays
								.asList(cache));
						singleQueue.put(req);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_SDTP) {
			try {
				List<XdrCompoundSource[]> caches = DataQueueCache
						.cleanUp(CACHE_TYPE_COMP);
				if (caches != null && caches.size() > 0) {
					for (XdrCompoundSource[] cache : caches) {
						NotifyXDRDataReq req = createCompXdrDataSendReq(Arrays
								.asList(cache));
						singleQueue.put(req);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public ArrayBlockingQueue<NotifyXDRDataReq> getSingleQueue() {
		return singleQueue;
	}

	public ArrayBlockingQueue<NotifyXDRDataReq> getCompQueue() {
		return compQueue;
	}

	public void outputXdr(XdrData xdr) {
		try {
			if (xdr instanceof XdrSingleSource) {
				if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_SDTP) {
					XdrSingleSource source = (XdrSingleSource) xdr;
					XdrSingleSource[] sc = FILLED_XDR_CACHE.get().addAndGet(
							source);

					if (sc != null) {

						NotifyXDRDataReq req = createNotifyXDRDataReq(Arrays
								.asList(sc));
						singleQueue.put(req);
					}
				}
			} else if (xdr instanceof XdrCompoundSource) {
				if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_SDTP) {
					XdrCompoundSource source = (XdrCompoundSource) xdr;
					XdrCompoundSource[] sc = OGIGINAL_CXDR_CACHE.get()
							.addAndGet(source);

					if (sc != null) {
						NotifyXDRDataReq req = createCompXdrDataSendReq(Arrays
								.asList(sc));
						compQueue.put(req);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NotifyXDRDataReq createNotifyXDRDataReq(List<XdrSingleSource> datas) {
		int totalLength = 0;
		// 注意头长度变化和内容一定要对的上
		totalLength += datas.size();
		for (XdrSingleSource data : datas) {
			int commonLen = (data instanceof XdrSingleSourceS1U) ? 0 : data
					.getCommon().getBodyLength();
			totalLength += commonLen + data.getBodyLength();
		}
		IoBuffer buffer = IoBuffer.allocate(totalLength);
		try {
			for (XdrSingleSource data : datas) {
				buffer.put((byte) SdtpConstants.NotifyXDRType.SINGLE);

				if (data instanceof XdrSingleSourceS1U) {
					buffer.put(data.toByteArray());
				} else {
					buffer.put(data.getCommon().toByteArray());
					buffer.put(data.toByteArray());
				}
			}
			buffer.flip();
		} catch (Exception e) {
			e.printStackTrace();
		}
		NotifyXDRDataReq req = new NotifyXDRDataReq(buffer.array());
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), NOTIFY_XDR_DATA_REQ,
				SdtpClientLteIoHandler.SEQ.incrementAndGet(),
				(short) datas.size());
		req.setHeader(header);
		return req;
	}

	public NotifyXDRDataReq createCompXdrDataSendReq(
			List<XdrCompoundSource> datas) {
		// 注意头长度变化和内容一定要对的上
		int totalLength = 0;
		totalLength += datas.size();
		for (XdrCompoundSource data : datas) {
			totalLength += data.getCommon().getBodyLength()
					+ data.getBodyLength();
		}
		IoBuffer buffer = IoBuffer.allocate(totalLength);

		try {
			for (XdrCompoundSource data : datas) {
				buffer.put((byte) SdtpConstants.NotifyXDRType.COMP);
				buffer.put(data.getCommon().toByteArray());
				buffer.put(data.toByteArray());
			}
			buffer.flip();
		} catch (Exception e) {
			e.printStackTrace();
		}
		NotifyXDRDataReq req = new NotifyXDRDataReq(buffer.array());
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), NOTIFY_XDR_DATA_REQ,
				SdtpClientLteIoHandler.SEQ.incrementAndGet(),
				(byte) datas.size());
		req.setHeader(header);
		return req;
	}

	public NotifyXDRDataReq createCompXdrDataSendReq(XdrCompoundSource data) {
		IoBuffer buffer = IoBuffer.allocate(data.getCommon().getBodyLength()
				+ data.getBodyLength() + 1);
		buffer.put((byte) SdtpConstants.NotifyXDRType.COMP);
		buffer.put(data.getCommon().toByteArray());
		buffer.put(data.toByteArray());
		buffer.flip();
		NotifyXDRDataReq req = new NotifyXDRDataReq(buffer.array());
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), NOTIFY_XDR_DATA_REQ,
				SdtpClientLteIoHandler.SEQ.incrementAndGet(), (short) 1);
		req.setHeader(header);
		return req;
	}

}
