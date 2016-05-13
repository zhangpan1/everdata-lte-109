package com.eversec.lte.kafka.consumer;

import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_BYTES;
import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_PACKAGE;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.processor.data.QueueData;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;
import com.eversec.lte.utils.SdtpUtils;

public class RawKafkaMessageHandler implements KafkaConsumerHandler {

	public void messageReceived(byte[] message) throws Exception {
		XDRRawDataSendReq raw = decodeRawData(message);
		QueueData.ORIGINAL_RAW_SDTP_OUTPUT_QUEUE.put(raw);
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_FILE) {
			QueueData.ORIGINAL_RAW_FILE_OUTPUT_QUEUE.put(raw);
		}
		RAW_RECEIVE_PACKAGE.incrementAndGet();
		RAW_RECEIVE_BYTES.addAndGet(message.length);
	}

	private XDRRawDataSendReq decodeRawData(byte[] bytes) {
		IoBuffer in = IoBuffer.wrap(bytes);
		SdtpHeader header = SdtpUtils.getHeader(in);
		byte[] load = new byte[bytes.length - SdtpConstants.SDTP_HEADER_LENGTH];
		in.get(load);
		XDRRawDataSendReq raw = new XDRRawDataSendReq(load);
		raw.setHeader(header);
		return raw;
	}

}
