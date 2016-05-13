package com.eversec.lte.sdtp.client;

import static com.eversec.lte.constant.SdtpConstants.MessageType.*;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.exception.UnknowSdtpMessageType;
import com.eversec.lte.sdtp.model.LinkAuthReq;
import com.eversec.lte.sdtp.model.LinkCheckReq;
import com.eversec.lte.sdtp.model.LinkDataCheckReq;
import com.eversec.lte.sdtp.model.LinkRelReq;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.SdtpRequest;
import com.eversec.lte.sdtp.model.VerNegoReq;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;

/**
 * 
 * @author bieremayi
 * 
 */
public class SdtpClientLteEncoder implements ProtocolEncoder  {// implements ProtocolEncoder

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		SdtpRequest req = (SdtpRequest) message;
		int messageType = req.getHeader().getMessageType();
		IoBuffer buffer = null;
		switch (messageType) {
		case VER_NEGO_REQ:
			buffer = ((VerNegoReq) req).toIoBuffer();
			break;
		case LINK_AUTH_REQ:
			buffer = ((LinkAuthReq) req).toIoBuffer();
			break;
		case LINK_CHECK_REQ:
			buffer = ((LinkCheckReq) req).toIoBuffer();
			break;
		case LINK_REL_REQ:
			buffer = ((LinkRelReq) req).toIoBuffer();
			break;
		case NOTIFY_XDR_DATA_REQ:
			buffer = ((NotifyXDRDataReq) req).toIoBuffer();
			if (SdtpConfig.IS_RESPONESE){
				buffer = null;
			} 
			break;
		case XDR_RAW_DATA_SEND_REQ:
			buffer = ((XDRRawDataSendReq) req).toIoBuffer();
			break;
		case LINK_DATA_CHECK_REQ:
			buffer = ((LinkDataCheckReq) req).toIoBuffer();
			break;
		default:
			throw new UnknowSdtpMessageType();
		}
		if (buffer != null){
			out.write(buffer);
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
