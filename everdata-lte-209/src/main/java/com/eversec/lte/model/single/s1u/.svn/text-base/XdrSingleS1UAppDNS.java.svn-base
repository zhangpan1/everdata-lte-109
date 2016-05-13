package com.eversec.lte.model.single.s1u;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.utils.FormatUtils;

import static com.eversec.lte.utils.FormatUtils.*;

public class XdrSingleS1UAppDNS extends XdrSingleS1UApp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2421583669229767143L;
	String dns;// 请求查询的DNS域名 64
	String ip;// 查询结果IP 15
	short dnsResult;// DNS响应码 1
	short dnsTimes;// DNS的请求次数 1
	short dnsRespTimes;// 响应数目 1
	short grantContentNum;// 授权内容数目 1
	short additionContentNum;// //附加内容数目 1

	public XdrSingleS1UAppDNS(String dns, String ip, short dnsResult,
			short dnsTimes, short dnsRespTimes, short grantContentNum,
			short additionContentNum) {
		super();
		this.dns = dns;
		this.ip = ip;
		this.dnsResult = dnsResult;
		this.dnsTimes = dnsTimes;
		this.dnsRespTimes = dnsRespTimes;
		this.grantContentNum = grantContentNum;
		this.additionContentNum = additionContentNum;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public short getDnsResult() {
		return dnsResult;
	}

	public void setDnsResult(short dnsResult) {
		this.dnsResult = dnsResult;
	}

	public short getDnsTimes() {
		return dnsTimes;
	}

	public void setDnsTimes(short dnsTimes) {
		this.dnsTimes = dnsTimes;
	}

	public short getDnsRespTimes() {
		return dnsRespTimes;
	}

	public void setDnsRespTimes(short dnsRespTimes) {
		this.dnsRespTimes = dnsRespTimes;
	}

	public short getGrantContentNum() {
		return grantContentNum;
	}

	public void setGrantContentNum(short grantContentNum) {
		this.grantContentNum = grantContentNum;
	}

	public short getAdditionContentNum() {
		return additionContentNum;
	}

	public void setAdditionContentNum(short additionContentNum) {
		this.additionContentNum = additionContentNum;
	}

	@Override
	public String toString() {
		String dns = FormatUtils.getEncodeStr(this.dns);
		String ip = FormatUtils.getStringRepZeroWithDot(this.ip);
		return dns + _S1U_DELIMITER_ + ip + _S1U_DELIMITER_ + dnsResult + _S1U_DELIMITER_
				+ dnsTimes + _S1U_DELIMITER_ + dnsRespTimes + _S1U_DELIMITER_
				+ grantContentNum + _S1U_DELIMITER_ + additionContentNum;
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		setString(buffer, dns, 64);// 64
		setString(buffer, ip, 15);// 15
		buffer.putUnsigned(dnsResult);// 1
		buffer.putUnsigned(dnsTimes);// 1
		buffer.putUnsigned(dnsRespTimes);// 1
		buffer.putUnsigned(grantContentNum);// 1
		buffer.putUnsigned(additionContentNum);// 1
		buffer.flip();
		return buffer;
	}

	@Override
	public int getBodyLength() {
		return 64 + 15 + 1 + 1 + 1 + 1 + 1;
	}
}
