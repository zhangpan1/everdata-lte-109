package com.eversec.lte.sdtp.server;

import static com.eversec.lte.constant.SdtpConstants.SDTP_HEADER_LENGTH;
import static com.eversec.lte.constant.SdtpConstants.SDTP_TOTALLENGTH_FIELD_LENGTH;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_AUTH_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_CHECK_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_DATA_CHECK_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_REL_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.VER_NEGO_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.XDR_RAW_DATA_QUERY_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.XDR_RAW_DATA_SEND_REQ;

import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.eversec.lte.exception.UnknowSdtpMessageType;
import com.eversec.lte.sdtp.model.LinkAuthReq;
import com.eversec.lte.sdtp.model.LinkCheckReq;
import com.eversec.lte.sdtp.model.LinkDataCheckReq;
import com.eversec.lte.sdtp.model.LinkRelReq;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.sdtp.model.SdtpRequest;
import com.eversec.lte.sdtp.model.VerNegoReq;
import com.eversec.lte.sdtp.model.XDRRawDataQueryReq;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;
import com.eversec.lte.utils.FormatUtils;
import com.eversec.lte.utils.SdtpUtils;

/**
 * 解析SDTP请求
 * 
 * @author bieremayi
 * 
 */
public class SdtpServerLteDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < SDTP_TOTALLENGTH_FIELD_LENGTH)
			return false;
		// 读取消息长度
		in.mark();
		int totalLength = in.getUnsignedShort();
		in.reset();
		if (in.remaining() >= totalLength) {
			SdtpHeader header = readHeader(in);
			SdtpRequest req = null;
			int messageType = header.getMessageType();
			switch (messageType) {
			case VER_NEGO_REQ:
				req = processVerNegoReq(header, in);
				break;
			case LINK_AUTH_REQ:
				req = processLinkAuthReq(header, in);
				break;
			case LINK_CHECK_REQ:
				req = processLinkCheckReq(header, in);
				break;
			case LINK_REL_REQ:
				req = processLinkRelReq(header, in);
				break;
			case NOTIFY_XDR_DATA_REQ:
				req = processNotifyXDRDataReq(header, in);
				break;
			case XDR_RAW_DATA_SEND_REQ:
				req = processXDRRawDataSendReq(header, in);
				break;
			case XDR_RAW_DATA_QUERY_REQ:
				req = processXDRRawDataQueryReq(header, in);
				break;
			case LINK_DATA_CHECK_REQ:
				req = processLinkDataCheckReq(header, in);
				break;
			default:
				throw new UnknowSdtpMessageType(String.valueOf(messageType));
			}
			req.setHeader(header);
			out.write(req);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * sdtp版本请求
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected VerNegoReq processVerNegoReq(SdtpHeader header, IoBuffer in)
			throws Exception {
		byte version = in.get();
		byte subVersion = in.get();
		return new VerNegoReq(version, subVersion);
	}

	/**
	 * sdtp鉴权请求
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected LinkAuthReq processLinkAuthReq(SdtpHeader header, IoBuffer in)
			throws Exception {
		String loginID = FormatUtils.getString(in, 12).trim();
		String digest = Hex.encodeHexString(FormatUtils.getBytes(in, 64));
		long timestamp = in.getUnsignedInt();
		int rand = in.getUnsignedShort();
		LinkAuthReq req = new LinkAuthReq(loginID, digest, timestamp, rand);
		req.setHeader(header);
		return req;
	}

	/**
	 * sdtp链路检测请求
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected LinkCheckReq processLinkCheckReq(SdtpHeader header, IoBuffer in)
			throws Exception {
		LinkCheckReq req = new LinkCheckReq();
		req.setHeader(header);
		return req;
	}

	/**
	 * 链路数据发送校验
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected LinkDataCheckReq processLinkDataCheckReq(SdtpHeader header,
			IoBuffer in) throws Exception {
		int sendflag = in.getInt();// 4;
		int sendDataInfo = in.getInt();// 4;
		LinkDataCheckReq req = new LinkDataCheckReq(sendflag, sendDataInfo);
		req.setHeader(header);
		return req;
	}

	/**
	 * 链路释放
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected LinkRelReq processLinkRelReq(SdtpHeader header, IoBuffer in)
			throws Exception {
		byte reason = in.get();
		LinkRelReq req = new LinkRelReq(reason);
		req.setHeader(header);
		return req;
	}

	/**
	 * XDR信令数据通知
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected NotifyXDRDataReq processNotifyXDRDataReq(SdtpHeader header,
			IoBuffer in) throws Exception {
		byte[] load = new byte[header.getTotalLength() - SDTP_HEADER_LENGTH];
		in.get(load);
		NotifyXDRDataReq req = new NotifyXDRDataReq(load);
		req.setHeader(header);
		return req;
	}

	/**
	 * XDR对应原始数据传输
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected XDRRawDataSendReq processXDRRawDataSendReq(SdtpHeader header,
			IoBuffer in) throws Exception {
		byte[] load = new byte[header.getTotalLength() - SDTP_HEADER_LENGTH];
		in.get(load);
		XDRRawDataSendReq req = new XDRRawDataSendReq(load);
		req.setHeader(header);
		return req;
	}

	protected XDRRawDataQueryReq processXDRRawDataQueryReq(SdtpHeader header,
			IoBuffer in) {
		short Interface = in.getUnsigned();
		byte[] xdrID = FormatUtils.getBytes(in, 16);
		Date startTime = FormatUtils.getDate(in);
		XDRRawDataQueryReq req = new XDRRawDataQueryReq(Interface, xdrID,
				startTime);
		req.setHeader(header);
		return req;
	}

	/**
	 * 读取sdtp header信息
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	protected SdtpHeader readHeader(IoBuffer in) throws Exception {

		return SdtpUtils.getHeader(in);
	}

}
