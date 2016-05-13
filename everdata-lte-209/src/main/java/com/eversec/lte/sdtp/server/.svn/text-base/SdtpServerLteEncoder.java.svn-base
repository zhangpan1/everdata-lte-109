package com.eversec.lte.sdtp.server;

import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_AUTH_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_CHECK_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_DATA_CHECK_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_REL_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.VER_NEGO_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.XDR_RAW_DATA_QUERY_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.XDR_RAW_DATA_SEND_RESP;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.exception.UnknowSdtpMessageType;
import com.eversec.lte.sdtp.model.LinkAuthResp;
import com.eversec.lte.sdtp.model.LinkCheckResp;
import com.eversec.lte.sdtp.model.LinkDataCheckResp;
import com.eversec.lte.sdtp.model.LinkRelResp;
import com.eversec.lte.sdtp.model.NotifyXDRDataResp;
import com.eversec.lte.sdtp.model.SdtpResponse;
import com.eversec.lte.sdtp.model.VerNegoResp;
import com.eversec.lte.sdtp.model.XDRRawDataQueryResp;
import com.eversec.lte.sdtp.model.XDRRawDataSendResp;

/**
 * 
 * @author bieremayi
 * 
 */
public class SdtpServerLteEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		SdtpResponse resp = (SdtpResponse) message;
		int messageType = resp.getHeader().getMessageType();
		IoBuffer buffer = null;
		switch (messageType) {
		case VER_NEGO_RESP:
			buffer = ((VerNegoResp) resp).toIoBuffer();
			break;
		case LINK_AUTH_RESP:
			buffer = ((LinkAuthResp) resp).toIoBuffer();
			break;
		case LINK_CHECK_RESP:
			buffer = ((LinkCheckResp) resp).toIoBuffer();
			break;
		case LINK_REL_RESP:
			buffer = ((LinkRelResp) resp).toIoBuffer();
			break;
		case NOTIFY_XDR_DATA_RESP:
			if (!SdtpConfig.IS_RESPONESE) {
				buffer = null;
			} else {
				buffer = ((NotifyXDRDataResp) resp).toIoBuffer();
			}
			break;
		case XDR_RAW_DATA_SEND_RESP:
			if (!SdtpConfig.IS_RESPONESE) {
				buffer = null;
			} else {
				buffer = ((XDRRawDataSendResp) resp).toIoBuffer();
			}
			break;
		case XDR_RAW_DATA_QUERY_RESP:
			buffer = ((XDRRawDataQueryResp) resp).toIoBuffer();
			break;
		case LINK_DATA_CHECK_RESP:
			buffer = ((LinkDataCheckResp) resp).toIoBuffer();
			break;
		default:
			throw new UnknowSdtpMessageType();
		}
		if (buffer != null)
			out.write(buffer);
	}

}
