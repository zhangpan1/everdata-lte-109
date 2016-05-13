package com.eversec.lte.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * mina 发送消息客户端
 * 
 * @author bieremayi
 * 
 */
public class MinaClient {

	private NioSocketConnector connector;

	private long connectTimeOut = 30 * 1000l;

	public MinaClient() {
		init();
	}

	public MinaClient(long connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		connector = new NioSocketConnector();
		// Configure the service.
		connector.setConnectTimeoutMillis(connectTimeOut);
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		// connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new OutHandler());
	}

	/**
	 * 打开session
	 * 
	 * @param hostname
	 * @param port
	 * @return
	 * @throws InterruptedException
	 */
	public IoSession openSession(String hostname, int port)
			throws InterruptedException {
		IoSession session = null;
		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(
						hostname, port));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			} catch (Exception e) {
				System.err.println("Failed to connect.");
				e.printStackTrace();
				TimeUnit.MILLISECONDS.sleep(5000);
			}
		}
		return session;
	}

	/**
	 * 关闭session
	 * 
	 * @param session
	 * @return
	 */
	public CloseFuture closeSession(IoSession session) {
		CloseFuture closeFuture = null;
		if (session == null) {
			throw new NullPointerException();
		} else {
			if (!session.isClosing())
				closeFuture = session.close(false).awaitUninterruptibly();
		}
		return closeFuture;
	}

	/**
	 * 关闭client
	 */
	public void closeClient() {
		if (connector != null && !connector.isDisposed()) {
			connector.dispose();
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param session
	 * @param msg
	 * @throws InterruptedException
	 */
	public WriteFuture sendMsg(IoSession session, final String msg)
			throws InterruptedException {
		if (session == null)
			throw new NullPointerException("IoSession is Null!");
		WriteFuture writeFuture = null;
		if (msg != null)
			writeFuture = session.write(msg);
		return writeFuture;
	}

	/**
	 * 
	 * @param session
	 * @param msgs
	 */
	public void sendMsg(IoSession session, final Iterator<?> msgs) {
		if (session == null)
			throw new NullPointerException("IoSession is Null!");
		if (msgs != null) {
			while (msgs.hasNext()) {
				session.write(msgs.next());
			}
		}
	}

	class OutHandler extends IoHandlerAdapter {

		@Override
		public void sessionCreated(IoSession session) throws Exception {
			super.sessionCreated(session);
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			super.sessionOpened(session);
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			super.sessionClosed(session);
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			super.sessionIdle(session, status);
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			super.messageReceived(session, message);
		}

		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			super.messageSent(session, message);
		}
	}

	public static void main(String[] args) throws Exception {
		long count = 0;
		if (args != null && args.length == 3) {
			String hostname = args[0];
			int port = Integer.valueOf(args[1]);
			//1.获取client
			MinaClient client = new MinaClient();
			//2.open session
			IoSession session = client.openSession(hostname, port);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[2])));
			String line = br.readLine();
			while (line != null && line.length() > 0) {
				count++;
				client.sendMsg(session, line);//3.发送消息
				line = br.readLine();
			}
			br.close();
			client.closeSession(session);//4.close session
			client.closeClient();//5.关闭client
			System.out.println(count);
		}
	}
}
