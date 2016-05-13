package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.mina.core.buffer.IoBuffer;

public class XdrSingleS1UAppIM extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6673462760725991720L;
	String account;// 登录账号 32
	String version;// 软件版本 32
	String type;// 客户端类型 32
	short actionType;// 操作类型 1

	public XdrSingleS1UAppIM(String account, String version, String type,
			short actionType) {
		super();
		this.account = account;
		this.version = version;
		this.type = type;
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return account + _S1U_DELIMITER_ + version + _S1U_DELIMITER_ + type
				+ _S1U_DELIMITER_ + actionType;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		setString(buffer, account, 32);// 32
		setString(buffer, version, 32);// 32
		setString(buffer, type, 32);// 32
		buffer.putUnsigned(actionType);// 1
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 32 + 32 + 32 + 1;
	}

}
