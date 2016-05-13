package com.eversec.lte.sdtp.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;

import com.eversec.lte.model.compound.XdrCompoundSource;
import com.eversec.lte.sdtp.model.NotifyXDRDataReq;
import com.eversec.lte.sdtp.model.XDRRawDataSendReq;
import com.eversec.lte.sdtp.tosdtp.SdtpToSdtpOutputTools;
import com.eversec.lte.vo.HostAndPortVo;
import com.eversec.lte.vo.compound.CompStatisInfo;

public class SdtpClientLteSendHandler extends SdtpClientLteIoHandler {
	protected volatile IoSession session;
	protected volatile boolean running = true;

	protected CompStatisInfo compStatisInfo = new CompStatisInfo();

	public void setSession(IoSession session) {
		this.session = session;
	}
	
	public void doVerNegoReq() {
		try {
			if (session != null && session.isConnected()) {
				super.doVerNegoReq(session);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close(true);
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (running) {
			logger.info("session {} closed!", session.getId());
			Thread.sleep(5000);
			logger.info("session {} reconnection!", session.getId());
			IoConnector socketConnector = (IoConnector) session
					.getAttribute(SdtpLteClient.LTE_SOCKET_CONNECTOR);
			HostAndPortVo hostAndPort = (HostAndPortVo) session.getAttribute(SdtpLteClient.HOST_AND_PORT);
			this.session = SdtpLteClient.createSession(socketConnector,hostAndPort);
		}
	}

	public void close() {
		running = false;
		CloseFuture future = session.close(false);
		while (!future.isClosed()) {
			future.awaitUninterruptibly();
		}
		session = null;
	}

	public void doCompDataSendReq(List<XdrCompoundSource> datas) {
		try {
			if (session != null && session.isConnected()) {
				NotifyXDRDataReq req = SdtpToSdtpOutputTools.getInstance().createCompXdrDataSendReq(datas) ;
				super.doCompXdrDataSendReq(session, req);
				statisXDRData(req, datas);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close(true);
			}
		}

	}

	public void doCompDataSendReq(XdrCompoundSource data) {
		try {
			if (session != null && session.isConnected()) {
				NotifyXDRDataReq req = super
						.doCompXdrDataSendReq(session, data);
				statisXDRData(req, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close(true);
			}
		}
	}

	private void statisXDRData(NotifyXDRDataReq req,
			List<XdrCompoundSource> datas) {
		// count
		compStatisInfo.addSourceCount(datas.size());
		// package
		compStatisInfo.addXdr2sdtpPackage(1);
		// bytes
		compStatisInfo.addXdr2sdtpBytes(req.getHeader().getTotalLength());
		for (XdrCompoundSource xdr : datas) {
			compStatisInfo
					.addXdrTypeCache(xdr.getCommon().getXdrType() + "", 1);
		}
	}

	private void statisXDRData(NotifyXDRDataReq req, XdrCompoundSource data) {
		ArrayList<XdrCompoundSource> list = new ArrayList<XdrCompoundSource>();
		list.add(data);
		statisXDRData(req, list);
	}

	protected void doRawOriginalDataSendReq(XDRRawDataSendReq data) {
		try {
			if (session != null && session.isConnected()) {
				super.doRawOriginalDataSendReq(session, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close(true);
			}
		}
	}

	protected void doXDROriginalDataSendReq(NotifyXDRDataReq data) {

		try {
			if (session != null && session.isConnected()) {
				super.doXDROriginalDataSendReq(session, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close(true);
			}
		}
	}

	protected void doXDRRawDataSendReq(byte[] load) {

		try {
			if (session != null && session.isConnected()) {
				super.doXDRRawDataSendReq(session, load);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.close(true);
			}
		}
	}

	public CompStatisInfo getCompStatisInfo() {
		return compStatisInfo;
	}
}
