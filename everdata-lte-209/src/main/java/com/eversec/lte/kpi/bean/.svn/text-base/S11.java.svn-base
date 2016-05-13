package com.eversec.lte.kpi.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;

public class S11 {

	public long startdate;

	public String city;

	public String mmeip;

	public String oldmme_or_sgw_c_ip;

	public String apn;

	public long failure_cause;

	public long request_cause;

	public int status;

	public String area;

	public long delaysum;

	public long reqnum;

	public S11(byte[] data, long delay, long count) {
		getValue(data, delay, count);
	}

	public void getValue(byte[] data, long delay, long count) {
		IoBuffer dis = IoBuffer.wrap(data);
		dis.getUnsigned();
		dis.getUnsigned();

		long startdate = dis.getLong();
		this.startdate = ConvertUtils.min2millis(startdate);

		byte[] city = new byte[2];
		dis.get(city);
		this.city = FormatUtils.TBCDFormat(city);

		byte[] ip = new byte[16];
		dis.get(ip);
		this.mmeip = FormatUtils.getIp(ip);

		dis.get(ip);
		this.oldmme_or_sgw_c_ip = FormatUtils.getIp(ip);

		this.apn = FormatUtils.formatStr(dis, 32);

		this.failure_cause = dis.getUnsignedShort();

		this.request_cause = dis.getUnsignedShort();

		this.status = dis.getUnsigned();

		this.area = "";

		this.delaysum = delay;

		this.reqnum = count;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.startdate);
		str.append("|");
		str.append(this.city);
		str.append("|");
		str.append(this.mmeip);
		str.append("|");
		str.append(this.oldmme_or_sgw_c_ip);
		str.append("|");
		str.append(this.apn);
		str.append("|");
		str.append(this.failure_cause);
		str.append("|");
		str.append(this.request_cause);
		str.append("|");
		str.append(this.status);
		str.append("|");
		str.append(this.delaysum);
		str.append("|");
		str.append(this.reqnum);
		str.append("\n");
		
		return str.toString();
	}
	
	public String getStartDate() {
		return ConvertUtils.long2timeMillis(this.startdate);
	}
	
	public String getCity() {
		String _city = this.city;
		if(_city.length() == 0){
			_city = "0000";
		}
		if(_city.length() == 3){
			_city = "0" + _city;
		}
		return _city;
	}
}
