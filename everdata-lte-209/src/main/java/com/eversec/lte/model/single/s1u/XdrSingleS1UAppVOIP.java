package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.mina.core.buffer.IoBuffer;

public class XdrSingleS1UAppVOIP extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4532769092472391461L;
	short dircet;// /呼叫方向 1
	String callNum;// 主叫号码 32
	String calledNum;// 被叫号码 32
	short callType;// /呼叫类型 1
	int flow;// VOIP中的数据流数量 2
	short hangCase;// 挂机原因 1
	short msgType;// 信令协议类型 1

	public XdrSingleS1UAppVOIP(short dircet, String callNum, String calledNum,
			short callType, int flow, short hangCase, short msgType) {
		super();
		this.dircet = dircet;
		this.callNum = callNum;
		this.calledNum = calledNum;
		this.callType = callType;
		this.flow = flow;
		this.hangCase = hangCase;
		this.msgType = msgType;
	}

	@Override
	public String toString() {
		return dircet + _S1U_DELIMITER_ + callNum + _S1U_DELIMITER_ + calledNum
				+ _S1U_DELIMITER_ + callType + _S1U_DELIMITER_ + flow + _S1U_DELIMITER_
				+ hangCase + _S1U_DELIMITER_ + msgType;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(dircet);// 1
		setString(buffer, callNum, 32);// 32
		setString(buffer, calledNum, 32);// 32
		buffer.putUnsigned(callType);// 1
		buffer.putUnsignedShort(flow);// 2
		buffer.putUnsigned(hangCase);// 1
		buffer.putUnsigned(msgType);// 1
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 32 + 32 + 1 + 2 + 1 + 1;
	}

}
