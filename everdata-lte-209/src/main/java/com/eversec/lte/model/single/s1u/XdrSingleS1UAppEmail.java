package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.mina.core.buffer.IoBuffer;

public class XdrSingleS1UAppEmail extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8894154111784811991L;
	int type;// 事务类型 2
	int state;// 返回状态码 2
	String username;// 用户名 32
	String sendInfo;// 发送方信息 128
	long length;// Email长度 4
	String smtpHost;// SMTP域名 128
	String recAccount;// 邮件接收方账号 128
	String headerInfo;// 要获取的邮件头信息 128
	short linkType; // 接入方式 1

	public XdrSingleS1UAppEmail(int type, int state, String username,
			String sendInfo, long length, String smtpHost, String recAccount,
			String headerInfo, short linkType) {
		super();
		this.type = type;
		this.state = state;
		this.username = username;
		this.sendInfo = sendInfo;
		this.length = length;
		this.smtpHost = smtpHost;
		this.recAccount = recAccount;
		this.headerInfo = headerInfo;
		this.linkType = linkType;
	}

	@Override
	public String toString() {
		return type + _S1U_DELIMITER_ + state + _S1U_DELIMITER_ + username
				+ _S1U_DELIMITER_ + sendInfo + _S1U_DELIMITER_ + length + _S1U_DELIMITER_
				+ smtpHost + _S1U_DELIMITER_ + recAccount + _S1U_DELIMITER_
				+ headerInfo + _S1U_DELIMITER_ + linkType;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsignedShort(type);// 2
		buffer.putUnsignedShort(state);// 2
		setString(buffer, username, 32);// 32
		setString(buffer, sendInfo, 128);// 128
		buffer.putUnsignedInt(length);// 4
		setString(buffer, smtpHost, 128);// 128
		setString(buffer, recAccount, 128);// 128
		setString(buffer, headerInfo, 128);// 128
		buffer.putUnsigned(linkType);// 1
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 2 + 2 + 32 + 128 + 4 + 128 + 128 + 128 + 1;
	}

}
