package com.eversec.lte.sdtp.server;

import static com.eversec.lte.config.SdtpConfig.IS_OUTPUT_EXAMINE_XDR_2_FILE;
import static com.eversec.lte.constant.SdtpConstants.SDTP_HEADER_LENGTH;
import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.XDR_RAW_DATA_SEND_RESP;
import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_BYTES;
import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_COUNT;
import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_BYTES;
import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_COUNT;
import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_PACKAGE;

import org.apache.mina.core.session.IoSession;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.processor.data.QueueData;
import com.eversec.lte.processor.data.StaticData;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.NotifyXDRDataResp;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;
import com.eversec.lte.sdtp.model.XDRRawDataSendResp;

/**
 * 模拟应用层handler
 * 
 * @author bieremayi
 * 
 */
public class SdtpServerApplicationLayerHandler extends
		SdtpServerCompLayerHandler {

	@Override
	protected NotifyXDRDataResp processNotifyXDRDataResp(SdtpHeader header,
			NotifyXDRDataReq req, IoSession session) throws Exception {
		if( SdtpConfig.IS_PROCESS_XDR){
			if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_FILE
					|| SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_FILE
					|| IS_OUTPUT_EXAMINE_XDR_2_FILE) {
				QueueData.MIX_XDR_FILE_OUTPUT_QUEUE.put(req);
			}
		}else{
			StaticData.ABANDON_XDR_COUNT.incrementAndGet();
		}
		XDR_RECEIVE_PACKAGE.incrementAndGet();
		XDR_RECEIVE_BYTES.addAndGet(req.getLoad().length + SDTP_HEADER_LENGTH);
		XDR_RECEIVE_COUNT.addAndGet(req.getHeader().getTotalContents());
		byte result = 1;
		NotifyXDRDataResp resp = new NotifyXDRDataResp(result);
		header.setMessageType(NOTIFY_XDR_DATA_RESP);
		header.setTotalLength(SDTP_HEADER_LENGTH + resp.getBodyLength());
		resp.setHeader(header);
		return resp;
	}

	@Override
	protected XDRRawDataSendResp processXDRRawDataSendResp(SdtpHeader header,
			XDRRawDataSendReq req) throws Exception {
		if( SdtpConfig.IS_PROCESS_RAW){
			if (SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_FILE) {
				QueueData.ORIGINAL_RAW_FILE_OUTPUT_QUEUE.put(req);
			}
			RAW_RECEIVE_PACKAGE.incrementAndGet();
			RAW_RECEIVE_BYTES.addAndGet(req.getLoad().length + SDTP_HEADER_LENGTH);
			RAW_RECEIVE_COUNT.addAndGet(req.getHeader().getTotalContents());
		}else{
			StaticData.ABANDON_RAW_COUNT.incrementAndGet();
		}
		
		byte result = 1;
		XDRRawDataSendResp resp = new XDRRawDataSendResp(result);
		header.setMessageType(XDR_RAW_DATA_SEND_RESP);
		header.setTotalLength(SDTP_HEADER_LENGTH + resp.getBodyLength());
		resp.setHeader(header);
		return resp;
	}

}
