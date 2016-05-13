package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.mina.core.buffer.IoBuffer;

public class XdrSingleS1UAppRTSP extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3160043911052554070L;
	String url;// RTSP请求中的URL信息 128
	String userAgent;// ;User-Agent字段 128
	String ip;// 用户访问的目标RTP服务器IP 128
	int userStartPort;// RTP会话中客户端的起始端口 2
	int userEndPort;// RTP会话中客户端的结束端口 2
	int serverStartPort;// RTP会话中服务器的起始端口 2
	int serverEndPort;// RTP会话中服务器的结束端口 2
	int vidoNum;// RTSP session中的Video流数 2
	int audioNum;// RTSP session中的Audio流数 2
	long delay;// 响应时延 4

	public XdrSingleS1UAppRTSP(String url, String userAgent, String ip,
			int userStartPort, int userEndPort, int serverStartPort,
			int serverEndPort, int vidoNum, int audioNum, long delay) {
		super();
		this.url = url;
		this.userAgent = userAgent;
		this.ip = ip;
		this.userStartPort = userStartPort;
		this.userEndPort = userEndPort;
		this.serverStartPort = serverStartPort;
		this.serverEndPort = serverEndPort;
		this.vidoNum = vidoNum;
		this.audioNum = audioNum;
		this.delay = delay;
	}

	@Override
	public String toString() {
		return url + _S1U_DELIMITER_ + userAgent + _S1U_DELIMITER_ + ip + _S1U_DELIMITER_
				+ userStartPort + _S1U_DELIMITER_ + userEndPort + _S1U_DELIMITER_
				+ serverStartPort + _S1U_DELIMITER_ + serverEndPort + _S1U_DELIMITER_
				+ vidoNum + _S1U_DELIMITER_ + audioNum + _S1U_DELIMITER_ + delay;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		setString(buffer, url, 128);// 128
		setString(buffer, userAgent, 128);// 128
		setString(buffer, ip, 128);// 128
		buffer.putUnsignedShort(userStartPort);// 2
		buffer.putUnsignedShort(userEndPort);// 2
		buffer.putUnsignedShort(serverStartPort);// 2
		buffer.putUnsignedShort(serverEndPort);// 2
		buffer.putUnsignedShort(vidoNum);// 2
		buffer.putUnsignedShort(audioNum);// 2
		buffer.putUnsignedInt(delay);// 4
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 128 + 128 + 128 + 2 + 2 + 2 + 2 + 2 + 2 + 4;
	}

}
