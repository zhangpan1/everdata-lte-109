package com.eversec.lte.model.single.s1u;

import static com.eversec.lte.utils.FormatUtils.setString;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

public class XdrSingleS1UAppHttp extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5992245237019956919L;
	short ver;// HTTP版本 1
	int type;// 事务类型 2
	int transState;// HTTP/WAP事务状态 2
	long firstPkgDelay;// ;第一个HTTP响应包时延(MS) 4
	long lastPkgDelay;// 最后一个HTTP内容包的时延(MS) 4
	long lastAckDelay;// 最后一个ACK确认包的时延（ms） 4
	String host;// HOST 64
	String uri;// 512
	String xOnlineHost;// 128
	String userAgent;// 256
	String httpContentType;// 128
	String referUri;// 128
	String cookie;// 256
	long contentLength;// 4
	short targetAction;// 目标行为 1
	short wtpBreakType;// wtp中断类型 1
	short wtpBreakCause;// ;wtp中断原因 1
	String title;// 256
	String keyWord;// 256
	short businessActionType;// 业务行为标识 1
	short businessFinishType;// 业务完成标识 1
	long businessDelay;// 业务时延(ms) 4
	short browser;// 浏览工具 1
	short protalType;// 门户应用集合 1

	public XdrSingleS1UAppHttp(short ver, int type, int transState,
			long firstPkgDelay, long lastPkgDelay, long lastAckDelay,
			String host, String uri, String xOnlineHost, String userAgent,
			String httpContentType, String referUri, String cookie,
			long contentLength, short targetAction, short wtpBreakType,
			short wtpBreakCause, String title, String keyWord,
			short businessActionType, short businessFinishType,
			long businessDelay, short browser, short protalType) {
		super();
		this.ver = ver;
		this.type = type;
		this.transState = transState;
		this.firstPkgDelay = firstPkgDelay;
		this.lastPkgDelay = lastPkgDelay;
		this.lastAckDelay = lastAckDelay;
		this.host = host;
		this.uri = uri;
		this.xOnlineHost = xOnlineHost;
		this.userAgent = userAgent;
		this.httpContentType = httpContentType;
		this.referUri = referUri;
		this.cookie = cookie;
		this.contentLength = contentLength;
		this.targetAction = targetAction;
		this.wtpBreakType = wtpBreakType;
		this.wtpBreakCause = wtpBreakCause;
		this.title = title;
		this.keyWord = keyWord;
		this.businessActionType = businessActionType;
		this.businessFinishType = businessFinishType;
		this.businessDelay = businessDelay;
		this.browser = browser;
		this.protalType = protalType;
	}

	public short getVer() {
		return ver;
	}

	public void setVer(short ver) {
		this.ver = ver;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTransState() {
		return transState;
	}

	public void setTransState(int transState) {
		this.transState = transState;
	}

	public long getFirstPkgDelay() {
		return firstPkgDelay;
	}

	public void setFirstPkgDelay(long firstPkgDelay) {
		this.firstPkgDelay = firstPkgDelay;
	}

	public long getLastPkgDelay() {
		return lastPkgDelay;
	}

	public void setLastPkgDelay(long lastPkgDelay) {
		this.lastPkgDelay = lastPkgDelay;
	}

	public long getLastAckDelay() {
		return lastAckDelay;
	}

	public void setLastAckDelay(long lastAckDelay) {
		this.lastAckDelay = lastAckDelay;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getxOnlineHost() {
		return xOnlineHost;
	}

	public void setxOnlineHost(String xOnlineHost) {
		this.xOnlineHost = xOnlineHost;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getHttpContentType() {
		return httpContentType;
	}

	public void setHttpContentType(String httpContentType) {
		this.httpContentType = httpContentType;
	}

	public String getReferUri() {
		return referUri;
	}

	public void setReferUri(String referUri) {
		this.referUri = referUri;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public short getTargetAction() {
		return targetAction;
	}

	public void setTargetAction(short targetAction) {
		this.targetAction = targetAction;
	}

	public short getWtpBreakType() {
		return wtpBreakType;
	}

	public void setWtpBreakType(short wtpBreakType) {
		this.wtpBreakType = wtpBreakType;
	}

	public short getWtpBreakCause() {
		return wtpBreakCause;
	}

	public void setWtpBreakCause(short wtpBreakCause) {
		this.wtpBreakCause = wtpBreakCause;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public short getBusinessActionType() {
		return businessActionType;
	}

	public void setBusinessActionType(short businessActionType) {
		this.businessActionType = businessActionType;
	}

	public short getBusinessFinishType() {
		return businessFinishType;
	}

	public void setBusinessFinishType(short businessFinishType) {
		this.businessFinishType = businessFinishType;
	}

	public long getBusinessDelay() {
		return businessDelay;
	}

	public void setBusinessDelay(long businessDelay) {
		this.businessDelay = businessDelay;
	}

	public short getBrowser() {
		return browser;
	}

	public void setBrowser(short browser) {
		this.browser = browser;
	}

	public short getProtalType() {
		return protalType;
	}

	public void setProtalType(short protalType) {
		this.protalType = protalType;
	}

	@Override
	public String toString() {

		String host = FormatUtils.getEncodeStr(this.host);// HOST 64
		String uri = FormatUtils.getEncodeStr(this.uri);// 512
		String xOnlineHost = FormatUtils.getEncodeStr(this.xOnlineHost);// 128
		String userAgent = FormatUtils.getEncodeStr(this.userAgent);// 256
		String httpContentType = FormatUtils.getEncodeStr(this.httpContentType);// 128
		String referUri = FormatUtils.getEncodeStr(this.referUri);// 128
		String cookie = FormatUtils.getEncodeStr(this.cookie);// 256

		return ver + _S1U_DELIMITER_ + type + _S1U_DELIMITER_ + transState
				+ _S1U_DELIMITER_ + firstPkgDelay + _S1U_DELIMITER_ + lastPkgDelay
				+ _S1U_DELIMITER_ + lastAckDelay + _S1U_DELIMITER_ + host + _S1U_DELIMITER_
				+ uri + _S1U_DELIMITER_ + xOnlineHost + _S1U_DELIMITER_ + userAgent
				+ _S1U_DELIMITER_ + httpContentType + _S1U_DELIMITER_ + referUri
				+ _S1U_DELIMITER_ + cookie + _S1U_DELIMITER_ + contentLength
				+ _S1U_DELIMITER_ + targetAction + _S1U_DELIMITER_ + wtpBreakType
				+ _S1U_DELIMITER_ + wtpBreakCause + _S1U_DELIMITER_ + title
				+ _S1U_DELIMITER_ + keyWord + _S1U_DELIMITER_ + businessActionType
				+ _S1U_DELIMITER_ + businessFinishType + _S1U_DELIMITER_
				+ businessDelay + _S1U_DELIMITER_ + browser + _S1U_DELIMITER_
				+ protalType;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		buffer.putUnsigned(ver);// 1
		buffer.putUnsignedShort(type);// 2
		buffer.putUnsignedShort(transState);// 2
		buffer.putUnsignedInt(firstPkgDelay);// 4
		buffer.putUnsignedInt(lastPkgDelay);// 4
		buffer.putUnsignedInt(lastAckDelay);// 4
		setString(buffer, host, 64);// 64
		setString(buffer, uri, 512);// 512
		setString(buffer, xOnlineHost, 128);// 128
		setString(buffer, userAgent, 256);// 256
		setString(buffer, httpContentType, 128);// 128
		setString(buffer, referUri, 128);// 128
		setString(buffer, cookie, 256);// 256
		buffer.putUnsignedInt(contentLength);// 4
		buffer.putUnsigned(targetAction);// 1
		buffer.putUnsigned(wtpBreakType);// 1
		buffer.putUnsigned(wtpBreakCause);// 1
		setString(buffer, title, 256);// 256
		setString(buffer, keyWord, 256);// 256
		buffer.putUnsigned(businessActionType);// 1
		buffer.putUnsigned(businessFinishType);// 1
		buffer.putUnsignedInt(businessDelay);// 4
		buffer.putUnsigned(browser);// 1
		buffer.putUnsigned(protalType);// 1

		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 1 + 2 + 2 + 4 + 4 + 4 + 64 + 512 + 128 + 256 + 128 + 128 + 256
				+ 4 + 1 + 1 + 1 + 256 + 256 + 1 + 1 + 4 + 1 + 1;
	}

}
