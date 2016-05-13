package com.eversec.lte.sdtp.client;

import static com.eversec.lte.constant.SdtpConstants.SDTP_HEADER_LENGTH;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_AUTH_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_AUTH_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_CHECK_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_CHECK_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_DATA_CHECK_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_DATA_CHECK_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_REL_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.LINK_REL_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.NOTIFY_XDR_DATA_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.VER_NEGO_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.VER_NEGO_RESP;
import static com.eversec.lte.constant.SdtpConstants.MessageType.XDR_RAW_DATA_SEND_REQ;
import static com.eversec.lte.constant.SdtpConstants.MessageType.XDR_RAW_DATA_SEND_RESP;
import static com.eversec.lte.processor.data.StaticData.RAW_2_SDTP_BYTES;
import static com.eversec.lte.processor.data.StaticData.RAW_2_SDTP_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.XDR_2_SDTP_BYTES;
import static com.eversec.lte.processor.data.StaticData.XDR_2_SDTP_PACKAGE;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomUtils;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.exception.UnknowSdtpMessageType;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.processor.data.QueueData;
import com.eversec.lte.sdtp.model.LinkAuthReq;
import com.eversec.lte.sdtp.model.LinkAuthResp;
import com.eversec.lte.sdtp.model.LinkCheckReq;
import com.eversec.lte.sdtp.model.LinkCheckResp;
import com.eversec.lte.sdtp.model.LinkDataCheckReq;
import com.eversec.lte.sdtp.model.LinkDataCheckResp;
import com.eversec.lte.sdtp.model.LinkRelReq;
import com.eversec.lte.sdtp.model.LinkRelResp;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.NotifyXDRDataResp;
import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.sdtp.model.SdtpRequest;
import com.eversec.lte.sdtp.model.SdtpResponse;
import com.eversec.lte.sdtp.model.VerNegoReq;
import com.eversec.lte.sdtp.model.VerNegoResp;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;
import com.eversec.lte.sdtp.model.XDRRawDataSendResp;
import com.eversec.lte.sdtp.tosdtp.SdtpToSdtpOutputTools;
import com.eversec.lte.utils.SdtpUtils;
import com.eversec.lte.vo.HostAndPortVo;
import com.eversec.lte.vo.SendFlagVo;

@SuppressWarnings("unused")
public class SdtpClientLteIoHandler implements IoHandler {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected byte version = (byte) SdtpConfig.getSdtpVersion();
	protected byte subVersion = (byte) SdtpConfig.getSdtpSubVersion();

	public static AtomicLong SEQ = new AtomicLong();
	public static SendFlagVo SEND_FLAG_VO = new SendFlagVo();
	public static AtomicLong SEND_DATA_INFO = new AtomicLong();

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		SdtpRequest res = null;
		SdtpResponse resp = (SdtpResponse) message;
		SdtpHeader header = resp.getHeader();
		int messageType = header.getMessageType();
		switch (messageType) {
		case VER_NEGO_RESP:
			res = processVerNegoResp(header, (VerNegoResp) resp);
			break;
		case LINK_AUTH_RESP:
			res = processLinkAuthResp(header, (LinkAuthResp) resp);
			break;
		case LINK_CHECK_RESP:
			res = processLinkCheckResp(header, (LinkCheckResp) resp);
			break;
		case LINK_REL_RESP:
			processLinkRelResp(session, (LinkRelResp) resp);
			break;
		case NOTIFY_XDR_DATA_RESP:
			processNotifyXDRDataResp(header, (NotifyXDRDataResp) resp);
			break;
		case XDR_RAW_DATA_SEND_RESP:
			processXDRRawDataSendResp(header, (XDRRawDataSendResp) resp);
			break;
		case LINK_DATA_CHECK_RESP:
			processLinkDataCheckResp(header, (LinkDataCheckResp) resp);
			break;
		default:
			throw new UnknowSdtpMessageType();
		}
		if (res != null) {
			session.write(res);
		}
	}

	/**
	 * 发起版本协商请求
	 * 
	 * @return
	 */
	public void doVerNegoReq(IoSession session) {
		VerNegoReq req = new VerNegoReq(version, subVersion);
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), VER_NEGO_REQ, SEQ.incrementAndGet(),
				(short) 1);
		req.setHeader(header);
		session.write(req);
	}

	/**
	 * 发起版本协商请求
	 * 
	 * @return
	 */
	private VerNegoReq doVerNegoReq() {
		VerNegoReq req = new VerNegoReq(version, subVersion);
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), VER_NEGO_REQ, SEQ.incrementAndGet(),
				(short) 1);
		req.setHeader(header);
		return req;
	}

	/**
	 * 发起鉴权请求
	 * 
	 * @return
	 */
	private LinkAuthReq doLinkAuthReq() {
		long timestamp = new Date().getTime() / 1000;
		short rand = (short) RandomUtils.nextInt(0, Short.MAX_VALUE);
		String digest = SdtpUtils.getDigestReq(timestamp, rand);
		LinkAuthReq req = new LinkAuthReq(SdtpConfig.getLoginID(), digest,
				timestamp, rand);
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), LINK_AUTH_REQ, SEQ.incrementAndGet(),
				(short) 1);
		req.setHeader(header);
		return req;
	}

	/**
	 * 发起链路检测请求
	 * 
	 * @return
	 */
	private LinkCheckReq doLinkCheckReq() {
		LinkCheckReq req = new LinkCheckReq();
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), LINK_CHECK_REQ, SEQ.incrementAndGet(),
				(short) 1);
		req.setHeader(header);
		return req;
	}

	/**
	 * 发起链路数据检测请求
	 * 
	 * @return
	 */
	private LinkDataCheckReq doLinkDataCheckResp() {
		LinkDataCheckReq req = new LinkDataCheckReq(SEND_FLAG_VO.getSendFlag(),
				SEND_DATA_INFO.getAndSet(0));// 重置SendDataInfo
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), LINK_DATA_CHECK_REQ,
				SEQ.incrementAndGet(), (short) 1);
		req.setHeader(header);
		return req;
	}

	/**
	 * 发起链路释放请求
	 * 
	 * @param reason
	 * @return
	 */
	private LinkRelReq doLinkRelReq(byte reason) {
		LinkRelReq req = new LinkRelReq(reason);
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), LINK_REL_REQ, SEQ.incrementAndGet(),
				(short) 1);
		req.setHeader(header);
		return req;
	}

	/**
	 * 发起xdr原始raw数据传输请求
	 * 
	 * @param load
	 * @return
	 */
	protected XDRRawDataSendReq doXDRRawDataSendReq(IoSession session,
			byte[] load) {
		SEND_DATA_INFO.incrementAndGet();
		XDRRawDataSendReq req = new XDRRawDataSendReq(load);
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), XDR_RAW_DATA_SEND_REQ,
				SEQ.incrementAndGet(), (short) 1);
		req.setHeader(header);
		session.write(req);
		RAW_2_SDTP_BYTES.addAndGet(req.getBodyLength());
		RAW_2_SDTP_PACKAGE.incrementAndGet();

		return req;
	}

	/**
	 * 发起xdr原始raw数据传输请求
	 * 
	 * @param load
	 * @return
	 */
	private XDRRawDataSendReq doXDRRawDataSendReq(byte[] load) {
		SEND_DATA_INFO.incrementAndGet();
		XDRRawDataSendReq req = new XDRRawDataSendReq(load);
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), XDR_RAW_DATA_SEND_REQ,
				SEQ.incrementAndGet(), (short) 1);
		req.setHeader(header);
		return req;
	}

	/**
	 * 发送单接口xdr至模拟应用层
	 * 
	 * @param session
	 * @param datas
	 */
	private NotifyXDRDataReq doXDRDataSendReq(IoSession session,
			NotifyXDRDataReq req) {
		SEND_DATA_INFO.incrementAndGet();
		session.write(req);
		XDR_2_SDTP_BYTES.addAndGet(req.getHeader().getTotalLength());
		XDR_2_SDTP_PACKAGE.incrementAndGet();
		return req;

	}



	/**
	 * 发送合成xdr至模拟应用层
	 * 
	 * @param session
	 * @param datas
	 */
	protected NotifyXDRDataReq doCompXdrDataSendReq(IoSession session,
			NotifyXDRDataReq req) {
		SEND_DATA_INFO.incrementAndGet();
		session.write(req);
		XDR_2_SDTP_BYTES.addAndGet(req.getHeader().getTotalLength());
		XDR_2_SDTP_PACKAGE.incrementAndGet();

		return req;
	}


	/**
	 * 发送合成xdr至模拟应用层
	 * 
	 * @param session
	 * @param data
	 */
	public NotifyXDRDataReq doCompXdrDataSendReq(IoSession session,
			XdrCompoundSource data) {
		SEND_DATA_INFO.incrementAndGet();
		NotifyXDRDataReq req =SdtpToSdtpOutputTools.getInstance().createCompXdrDataSendReq(data);
		session.write(req);
		XDR_2_SDTP_BYTES.addAndGet(req.getHeader().getTotalLength());
		XDR_2_SDTP_PACKAGE.incrementAndGet();

		return req;
	}

	
	/**
	 * 处理sdtp版本响应
	 * 
	 * @param header
	 * @param resp
	 * @return
	 */
	private SdtpRequest processVerNegoResp(SdtpHeader header, VerNegoResp resp) {
		SdtpRequest req = null;
		byte result = resp.getResult();
		if (result == 1) {
			req = doLinkAuthReq();
		} else {
			req = doLinkRelReq((byte) 1);
		}
		return req;
	}

	/**
	 * 处理sdtp鉴权响应
	 * 
	 * @param header
	 * @param session
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private SdtpRequest processLinkAuthResp(SdtpHeader header, LinkAuthResp resp)
			throws Exception {
		SdtpRequest req = null;
		byte result = resp.getResult();
		if (result == 1) {

		} else if (result == 2) {
			logger.warn("linkAuth,LoginID : {} doesn't exists !",
					SdtpConfig.getLoginID());
			doLinkRelReq((byte) 1);
		} else if (result == 3) {
			logger.warn("linkAuth,SHA256 decode error !");
			doLinkRelReq((byte) 1);
		} else {
			logger.warn("linkAuth,unknow result !");
			doLinkRelReq((byte) 2);
		}
		return req;
	}

	/**
	 * 处理sdtp链路检测响应
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private SdtpRequest processLinkCheckResp(SdtpHeader header,
			LinkCheckResp resp) throws Exception {
		SdtpRequest req = null;
		return req;
	}

	/**
	 * 链路数据发送校验响应
	 * 
	 * @param header
	 * @param resp
	 * @throws Exception
	 */
	private void processLinkDataCheckResp(SdtpHeader header,
			LinkDataCheckResp resp) throws Exception {
		// TODO
	}

	/**
	 * 链路释放响应
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private void processLinkRelResp(IoSession session, LinkRelResp resp) {
		session.close(true);
	}

	/**
	 * XDR信令数据通知响应
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private void processNotifyXDRDataResp(SdtpHeader header,
			NotifyXDRDataResp resp) throws Exception {
		byte result = resp.getResult();
	}

	/**
	 * XDR对应原始数据传输响应
	 * 
	 * @param header
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private void processXDRRawDataSendResp(SdtpHeader header,
			XDRRawDataSendResp resp) throws Exception {
		byte result = resp.getResult();
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void sessionCreated(final IoSession session) throws Exception {

	}

	/**
	 * 判断是否通过sdtp发送回填后单接口xdr
	 */
	private void initFilledXdr2SdtpOutputTask(final IoSession session) {
		if (SdtpConfig.IS_OUTPUT_FILLED_XDR_2_SDTP) {
			LteMain.SDTP_EXEC.execute(new Runnable() {
				@Override
				public void run() {
					while (session.isConnected()) {
						NotifyXDRDataReq req = null;
						try {
							req = SdtpToSdtpOutputTools.getInstance().getSingleQueue().take();
							doXDRDataSendReq(session, req);
						} catch (Exception e) {
							if(req != null){
								logger.error(Hex.encodeHexString(req.getLoad()) + "", e);
							}else{
								logger.error("", e);
							}
							session.close(true);
							return;
						}
					}
				}

			});
		}
	}

	/**
	 * 判断是否启用sdtp原始合成xdr发送
	 */
	private void initOriginalCxdr2SdtpOutputTask(final IoSession session) {
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_CXDR_2_SDTP) {
			LteMain.SDTP_EXEC.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						NotifyXDRDataReq req  = null;
						try {
							req =  SdtpToSdtpOutputTools.getInstance().getCompQueue().take();
							doCompXdrDataSendReq(session, req);
						} catch (Exception e) {
							if(req != null){
								logger.error(Hex.encodeHexString(req.getLoad()) + "", e);
							}else{
								logger.error("", e);
							}
							session.close(true);
							return;
						}
					}
				}
			});
		}
	}

	/**
	 * 判断是否启用sdtp原始单接口xdr发送
	 */
	private void initOriginalXdr2SdtpOutputTask(final IoSession session) {
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_XDR_2_SDTP) {
			LteMain.SDTP_EXEC.execute(new Runnable() {
				@Override
				public void run() {
					while (session.isConnected()) {
						try {
							NotifyXDRDataReq data = QueueData.ORIGINAL_XDR_SDTP_OUTPUT_QUEUE
									.take();
							try {
								doXDROriginalDataSendReq(session, data);
							} catch (Exception e) {
								logger.error(data + "", e);
								session.close(true);
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			});
		}
	}

	/**
	 * 判断是否启用sdtp原始码流发送
	 */
	private void initOriginalRaw2SdtpOutputTask(final IoSession session) {
		if (SdtpConfig.IS_OUTPUT_ORIGINAL_RAW_2_SDTP) {
			LteMain.SDTP_EXEC.execute(new Runnable() {
				@Override
				public void run() {
					while (session.isConnected()) {
						try {
							XDRRawDataSendReq data = QueueData.ORIGINAL_RAW_SDTP_OUTPUT_QUEUE
									.take();
							try {
								doRawOriginalDataSendReq(session, data);
							} catch (Exception e) {
								e.printStackTrace();
								session.close(true);
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			});
		}
	}

	/**
	 * 
	 * @param session
	 * @throws Exception
	 */
	public void sessionCreatedAwaitUninterruptibly(final IoSession session)
			throws Exception {
		logger.info("session {} created!", session.getId());
		doVerNegoReq(session);
		initFilledXdr2SdtpOutputTask(session);
		initOriginalCxdr2SdtpOutputTask(session);
		initOriginalRaw2SdtpOutputTask(session);
		initOriginalXdr2SdtpOutputTask(session);
	}

	/**
	 * 转发原始码流，增加xdrType
	 * 
	 * @param session
	 * @param data
	 */
	protected void doRawOriginalDataSendReq(IoSession session,
			XDRRawDataSendReq data) {
		SdtpHeader origHeader = data.getHeader();
		SEND_DATA_INFO.incrementAndGet();
		XDRRawDataSendReq req = new XDRRawDataSendReq(data.getLoad());
		SdtpHeader header = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), XDR_RAW_DATA_SEND_REQ,
				SEQ.incrementAndGet(), origHeader.getTotalContents());
		req.setHeader(header);
		session.write(req);
		RAW_2_SDTP_BYTES.addAndGet(req.getBodyLength() + SDTP_HEADER_LENGTH);
		RAW_2_SDTP_PACKAGE.incrementAndGet();
	}

	/**
	 * 转发原始xdr，增加xdrType
	 * 
	 * @param session
	 * @param data
	 */
	protected void doXDROriginalDataSendReq(IoSession session,
			NotifyXDRDataReq data) {
		SdtpHeader origheader = data.getHeader();
		SEND_DATA_INFO.incrementAndGet();
		NotifyXDRDataReq req = new NotifyXDRDataReq(data.getLoad());
		SdtpHeader head = new SdtpHeader(SDTP_HEADER_LENGTH
				+ req.getBodyLength(), NOTIFY_XDR_DATA_REQ,
				SEQ.incrementAndGet(), origheader.getTotalContents());
		req.setHeader(head);
		session.write(req);
		XDR_2_SDTP_BYTES.addAndGet(req.getBodyLength() + SDTP_HEADER_LENGTH);
		XDR_2_SDTP_PACKAGE.incrementAndGet();
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("session {} opened!", session.getId());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("session {} closed!", session.getId());
		Thread.sleep(5000);
		logger.info("session {} reconnection!", session.getId());
		IoConnector socketConnector = (IoConnector) session
				.getAttribute(SdtpLteClient.LTE_SOCKET_CONNECTOR);
		HostAndPortVo hostAndPort = (HostAndPortVo) session
				.getAttribute(SdtpLteClient.HOST_AND_PORT);
		SdtpLteClient.connentAndInit(socketConnector, this,hostAndPort);

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		logger.info("session idle : {}!", status.toString());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.warn("catch exception : {}!", cause);
		session.close(false);
	}

}
