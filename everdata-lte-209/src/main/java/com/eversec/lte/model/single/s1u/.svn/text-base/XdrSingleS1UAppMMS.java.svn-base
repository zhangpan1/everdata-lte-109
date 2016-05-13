package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

public class XdrSingleS1UAppMMS extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7012020817922589303L;
	short transType;// 1
	short successFlag;// 1
	short httpOrWap;// 1
	int httpWapCode;// 2
	int mmseRspStatus;// 2
	String mmsSendAddr;// 128
	String mmsMsgId;// 64
	String mmsTransactionId;// 32
	String mmsRetriveAddr;// 256
	int mmsRetriveAddrNum;// 2
	String mmsCcBccAddr;// 256
	int mmsCcBccAddrNum;// 2
	String mmsSubject;// 256
	long mmsDataSize;// 4
	String mmscAddr;// 64
	String host;// 64
	String uri;// 128
	String xOnlineHost;// 128

	public XdrSingleS1UAppMMS(short transType, short successFlag,
			short httpOrWap, int httpWapCode, int mmseRspStatus,
			String mmsSendAddr, String mmsMsgId, String mmsTransactionId,
			String mmsRetriveAddr, int mmsRetriveAddrNum, String mmsCcBccAddr,
			int mmsCcBccAddrNum, String mmsSubject, long mmsDataSize,
			String mmscAddr, String host, String uri, String xOnlineHost) {
		super();
		this.transType = transType;
		this.successFlag = successFlag;
		this.httpOrWap = httpOrWap;
		this.httpWapCode = httpWapCode;
		this.mmseRspStatus = mmseRspStatus;
		this.mmsSendAddr = mmsSendAddr;
		this.mmsMsgId = mmsMsgId;
		this.mmsTransactionId = mmsTransactionId;
		this.mmsRetriveAddr = mmsRetriveAddr;
		this.mmsRetriveAddrNum = mmsRetriveAddrNum;
		this.mmsCcBccAddr = mmsCcBccAddr;
		this.mmsCcBccAddrNum = mmsCcBccAddrNum;
		this.mmsSubject = mmsSubject;
		this.mmsDataSize = mmsDataSize;
		this.mmscAddr = mmscAddr;
		this.host = host;
		this.uri = uri;
		this.xOnlineHost = xOnlineHost;
	}

	@Override
	public String toString() {
		String mmsSubject = FormatUtils.getEncodeStr(this.mmsSubject);
		return transType + _S1U_DELIMITER_ + successFlag + _S1U_DELIMITER_ + httpOrWap
				+ _S1U_DELIMITER_ + httpWapCode + _S1U_DELIMITER_ + mmseRspStatus
				+ _S1U_DELIMITER_ + mmsSendAddr + _S1U_DELIMITER_ + mmsMsgId
				+ _S1U_DELIMITER_ + mmsTransactionId + _S1U_DELIMITER_ + mmsRetriveAddr
				+ _S1U_DELIMITER_ + mmsRetriveAddrNum + _S1U_DELIMITER_ + mmsCcBccAddr
				+ _S1U_DELIMITER_ + mmsCcBccAddrNum + _S1U_DELIMITER_ + mmsSubject
				+ _S1U_DELIMITER_ + mmsDataSize + _S1U_DELIMITER_ + mmscAddr
				+ _S1U_DELIMITER_ + host + _S1U_DELIMITER_ + uri + _S1U_DELIMITER_
				+ xOnlineHost;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(transType);// 1
		buffer.putUnsigned(successFlag);// 1
		buffer.putUnsigned(httpOrWap);// 1
		buffer.putUnsignedShort(httpWapCode);// 2
		buffer.putUnsignedShort(mmseRspStatus);// 2
		setString(buffer, mmsSendAddr, 128);// 128
		setString(buffer, mmsMsgId, 64);// 64
		setString(buffer, mmsTransactionId, 32);// 32
		setString(buffer, mmsRetriveAddr, 256);// 256
		buffer.putUnsignedShort(mmsRetriveAddrNum);// 2
		setString(buffer, mmsCcBccAddr, 256);// 256
		buffer.putUnsignedShort(mmsCcBccAddrNum);// 2
		setString(buffer, mmsSubject, 256);// 256
		buffer.putUnsignedInt(mmsDataSize);// 4
		setString(buffer, mmscAddr, 64);// 64
		setString(buffer, host, 64);// 64
		setString(buffer, uri, 128);// 128
		setString(buffer, xOnlineHost, 128);// 128
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 1 + 1 + 2 + 2 + 128 + 64 + 32 + 256 + 2 + 256 + 2 + 256 + 4
				+ 64 + 64 + 128 + 128;
	}

}
