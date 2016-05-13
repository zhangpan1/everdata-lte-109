package com.eversec.lte.sdtp.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.eversec.lte.exception.UnknowSdtpMessageType;
import com.eversec.lte.sdtp.model.LinkAuthResp;
import com.eversec.lte.sdtp.model.LinkCheckResp;
import com.eversec.lte.sdtp.model.LinkDataCheckResp;
import com.eversec.lte.sdtp.model.LinkRelResp;
import com.eversec.lte.sdtp.model.NotifyXDRDataResp;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.sdtp.model.SdtpResponse;
import com.eversec.lte.sdtp.model.VerNegoResp;
import com.eversec.lte.sdtp.model.XDRRawDataSendResp;
import com.eversec.lte.utils.FormatUtils;
import com.eversec.lte.utils.SdtpUtils;

import static com.eversec.lte.constant.SdtpConstants.*;
import static com.eversec.lte.constant.SdtpConstants.MessageType.*;

/**
 * 解析SDTP响应
 * 
 * @author bieremayi
 * 
 */
public class SdtpClientLteDecoder extends CumulativeProtocolDecoder {

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
			SdtpResponse resp = null;
			int messageType = header.getMessageType();
			switch (messageType) {
			case VER_NEGO_RESP:
				resp = processVerNegoResp(header, in);
				break;
			case LINK_AUTH_RESP:
				resp = processLinkAuthResp(header, in);
				break;
			case LINK_CHECK_RESP:
				resp = processLinkCheckResp(header, in);
				break;
			case LINK_REL_RESP:
				resp = processLinkRelResp(header, in);
				break;
			case NOTIFY_XDR_DATA_RESP:
				resp = processNotifyXDRDataResp(header, in);
				break;
			case XDR_RAW_DATA_SEND_RESP:
				resp = processXDRRawDataSendResp(header, in);
				break;
			case LINK_DATA_CHECK_RESP:
				resp = processLinkDataCheckResp(header, in);
				break;
			default:
				throw new UnknowSdtpMessageType();
			}
			resp.setHeader(header);
			out.write(resp);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * sdtp版本响应
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private VerNegoResp processVerNegoResp(SdtpHeader header, IoBuffer in) {
		VerNegoResp resp = new VerNegoResp(in.get());
		resp.setHeader(header);
		return resp;
	}

	/**
	 * sdtp鉴权响应
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private LinkAuthResp processLinkAuthResp(SdtpHeader header, IoBuffer in) {
		LinkAuthResp resp = new LinkAuthResp(in.get(), FormatUtils.getString(
				in, 64));
		resp.setHeader(header);
		return resp;
	}

	/**
	 * sdtp链路检测响应
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private LinkCheckResp processLinkCheckResp(SdtpHeader header, IoBuffer in) {
		LinkCheckResp resp = new LinkCheckResp();
		resp.setHeader(header);
		return resp;
	}

	/**
	 * 链路数据发送校验
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private LinkDataCheckResp processLinkDataCheckResp(SdtpHeader header,
			IoBuffer in) throws Exception {
		long sendflag = in.getUnsignedInt();
		byte result = in.get();
		long sendDataInfo = in.getUnsignedInt();
		long recDataInfo = in.getUnsignedInt();
		LinkDataCheckResp resp = new LinkDataCheckResp(sendflag, result,
				sendDataInfo, recDataInfo);
		resp.setHeader(header);
		return resp;
	}

	/**
	 * 链路释放
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private LinkRelResp processLinkRelResp(SdtpHeader header, IoBuffer in)
			throws Exception {
		LinkRelResp resp = new LinkRelResp(in.get());
		resp.setHeader(header);
		return resp;
	}

	/**
	 * XDR对应原始数据传输
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private XDRRawDataSendResp processXDRRawDataSendResp(SdtpHeader header,
			IoBuffer in) throws Exception {
		XDRRawDataSendResp resp = new XDRRawDataSendResp(in.get());
		resp.setHeader(header);
		return resp;
	}

	/**
	 * XDR信令数据通知
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private NotifyXDRDataResp processNotifyXDRDataResp(SdtpHeader header,
			IoBuffer in) throws Exception {
		NotifyXDRDataResp resp = new NotifyXDRDataResp(in.get());
		resp.setHeader(header);
		return resp;
	}

	/**
	 * 读取sdtp header信息
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private SdtpHeader readHeader(IoBuffer in) throws Exception {
		return SdtpUtils.getHeader(in);
	}

}
