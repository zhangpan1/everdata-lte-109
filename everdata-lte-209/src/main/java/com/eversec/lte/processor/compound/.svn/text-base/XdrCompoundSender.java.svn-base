package com.eversec.lte.processor.compound;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kafka.producer.KafkaProducer;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.sdtp.client.SdtpClientLteSendHandler;
import com.eversec.lte.sdtp.client.SdtpLteClient;
import com.eversec.lte.vo.compound.CompInfo;
import com.eversec.lte.vo.compound.CompStatisInfo;

public class XdrCompoundSender {
	private KafkaProducer producer;
	private SdtpClientLteSendHandler handler;
	private final String signal_cxdr_with_uemr_tryagain = "signal-cxdr-with-uemr-tryagain";

	public XdrCompoundSender(SdtpLteClient client, KafkaProducer producer) {
		this.producer = producer;
		this.handler = client.startAndGetHandler();
	}

	/**
	 * 发送合成xdr至sdtp server
	 * 
	 * @param cxdr
	 */
	public void send(XdrCompoundSource cxdr) {
		handler.doCompDataSendReq(cxdr);
	}

	/**
	 * 发送自定义xdr至kafka(topic : signal-cxdr-with-uemr-tryagain)
	 * 
	 * @param xdr
	 */
	public void send(XdrSingleSource xdr) {
		String imsi = xdr.getCommon().getImsi();
		if (StringUtils.isNotBlank(imsi)) {
			byte type = (byte) xdr.getCommon().getInterface();
			IoBuffer buffer = IoBuffer.allocate(xdr.getCommon().getBodyLength()
					+ xdr.getBodyLength() + 1);
			buffer.put(type);
			buffer.put(xdr.getCommon().toByteArray());
			buffer.put(xdr.toByteArray());
			buffer.flip();
			producer.sendBytes(signal_cxdr_with_uemr_tryagain, imsi,
					buffer.array());
		}
	}

	/**
	 * 发送合成xdr至sdtp , 自定义单接口xdr至kafka(topic : signal-cxdr-with-uemr-tryagain)
	 * 
	 * @param compInfo
	 */
	public void send(CompInfo compInfo) {
		List<XdrCompoundSource> cxdrs = compInfo.getCxdrs();
		if (cxdrs != null && cxdrs.size() > 0) {
			for (XdrCompoundSource cxdr : cxdrs) {
				send(cxdr);
			}
		}
		List<XdrSingleSource> xdrs = compInfo.getXdrs();
		if (xdrs != null && xdrs.size() > 0) {
			for (XdrSingleSource xdr : xdrs) {
				send(xdr);
			}
		}
	}

	/**
	 * 关闭session连接，producer连接
	 */
	public void close() {
		handler.close();
		producer.close();
	}

	/**
	 * 获取统计结果信息
	 * 
	 * @return
	 */
	public CompStatisInfo getCompStatisInfo() {
		return handler.getCompStatisInfo();
	}

	public static void main(String[] args) {
		SdtpLteClient client = new SdtpLteClient();
		KafkaProducer producer = new KafkaProducer();
		XdrCompoundSender sender = new XdrCompoundSender(client, producer);
		sender.close();
		client.close();
	}

}
