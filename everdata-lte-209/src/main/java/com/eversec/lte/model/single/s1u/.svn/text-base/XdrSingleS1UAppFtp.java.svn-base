package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.mina.core.buffer.IoBuffer;

public class XdrSingleS1UAppFtp extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5229879296049061688L;
	short ftpState;// FTP状态 1
	String login;// 登录用户名 32
	String curDir;// 当前目录 128
	short trasferMode;// 文件传输模式 1
	short trasferDirect;// 传输方向标志 1
	String fileName;// 文件名称 128
	int localPort;// FTP会话中本地数据端口 2
	int remotePort;// FTP会话中远端数据端口 2
	long fileSize;// 文件总大小 4
	long respDelay;// 响应时延（ms） 4
	long transferDuration;// 传输时长（ms） 4

	public XdrSingleS1UAppFtp(short ftpState, String login, String curDir,
			short trasferMode, short trasferDirect, String fileName,
			int localPort, int remotePort, long fileSize, long respDelay,
			long transferDuration) {
		super();
		this.ftpState = ftpState;
		this.login = login;
		this.curDir = curDir;
		this.trasferMode = trasferMode;
		this.trasferDirect = trasferDirect;
		this.fileName = fileName;
		this.localPort = localPort;
		this.remotePort = remotePort;
		this.fileSize = fileSize;
		this.respDelay = respDelay;
		this.transferDuration = transferDuration;
	}

	@Override
	public String toString() {
		return ftpState + _S1U_DELIMITER_ + login + _S1U_DELIMITER_ + curDir
				+ _S1U_DELIMITER_ + trasferMode + _S1U_DELIMITER_ + trasferDirect
				+ _S1U_DELIMITER_ + fileName + _S1U_DELIMITER_ + localPort
				+ _S1U_DELIMITER_ + remotePort + _S1U_DELIMITER_ + fileSize
				+ _S1U_DELIMITER_ + respDelay + _S1U_DELIMITER_ + transferDuration;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(ftpState);// 1
		setString(buffer, login, 32);// 32
		setString(buffer, curDir, 128);// 128
		buffer.putUnsigned(trasferMode);// 1
		buffer.putUnsigned(trasferDirect);// 1
		setString(buffer, fileName, 128);// 128
		buffer.putUnsignedShort(localPort);// 2
		buffer.putUnsignedShort(remotePort);// 2
		buffer.putUnsignedInt(fileSize);// 4
		buffer.putUnsignedInt(respDelay);// 4
		buffer.putUnsignedInt(transferDuration);// 4
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 32 + 128 + 1 + 1 + 128 + 2 + 2 + 4 + 4 + 4;
	}

}
