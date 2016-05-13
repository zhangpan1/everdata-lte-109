package com.eversec.lte.sdtp.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.vo.HostAndPortVo;

/**
 * sdtp client
 * 
 * @author bieremayi
 * 
 */
public class SdtpLteClient {

	private static Logger logger = LoggerFactory.getLogger(SdtpLteClient.class);
	public static final String LTE_SOCKET_CONNECTOR = "LTE_SOCKET_CONNECTOR";
	public static final String HOST_AND_PORT = "HOST_AND_PORT";

	public static void main(String[] args) throws IOException {
		new SdtpLteClient().start();
	}

	private SocketConnector socketConnector;
	private boolean supportClose;

	public void close() {
		if (supportClose) {
			if (socketConnector != null) {
				socketConnector.dispose();
			}
		}
	}

	private SocketConnector openSocketConnector(SdtpClientLteIoHandler handler) {
		SocketConnector socketConnector = new NioSocketConnector();
		socketConnector.getFilterChain().addLast(
				"protocol",
				new ProtocolCodecFilter(new SdtpClientLteEncoder(),
						new SdtpClientLteDecoder()));
		socketConnector.setHandler(handler);
		return socketConnector;
	}

	public SdtpClientLteSendHandler startAndGetHandler() {
		List<HostAndPortVo> hostAndPorts = SdtpConfig
				.getSdtpServerHostAndPort();
		supportClose = true;
		final SdtpClientLteSendHandler handler = new SdtpClientLteSendHandler();
		this.socketConnector = openSocketConnector(handler);
		HostAndPortVo hostAndPort = hostAndPorts.get(0);
		IoSession session = createSession(socketConnector, hostAndPort);
		handler.setSession(session);
		handler.doVerNegoReq();
		return handler;
	}

	public void start() {
		List<HostAndPortVo> hostAndPorts = SdtpConfig
				.getSdtpServerHostAndPort();
		supportClose = false;
		final SdtpClientLteIoHandler handler = new SdtpClientLteIoHandler();
		this.socketConnector = openSocketConnector(handler);
		for (final HostAndPortVo hostAndPort : hostAndPorts) {
			for (int i = 0; i < SdtpConfig.getSdtpClientThread(); i++) {
				LteMain.SDTP_EXEC.execute(new Runnable() {
					@Override
					public void run() {
						connentAndInit(socketConnector, handler, hostAndPort);
					}
				});
			}
			logger.info("sdtp client ip : {} , port : {} , type : {}",
					new Object[] { hostAndPort.getHost(),
							hostAndPort.getPort(), "LTE" });
		}

	}

	public static IoSession createSession(IoConnector socketConnector,
			HostAndPortVo hostAndPort) {
		ConnectFuture connect;
		IoSession session;
		while (true) {
			try {
				connect = socketConnector.connect(new InetSocketAddress(
						hostAndPort.getHost(), hostAndPort.getPort()));
				connect.awaitUninterruptibly();
				session = connect.getSession();
				session.setAttribute(LTE_SOCKET_CONNECTOR, socketConnector);
				session.setAttribute(HOST_AND_PORT, hostAndPort);
				return session;
			} catch (Exception ex) {
				logger.info(
						"can't connect to app server,ip : {} , port : {} , type : {},cause :{}",
						new Object[] { hostAndPort.getHost(),
								hostAndPort.getPort(), "LTE", ex.getClass() });
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static void connentAndInit(final IoConnector socketConnector,
			final SdtpClientLteIoHandler handler, HostAndPortVo hostAndPort) {

		IoSession session = createSession(socketConnector, hostAndPort);
		try {
			handler.sessionCreatedAwaitUninterruptibly(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
