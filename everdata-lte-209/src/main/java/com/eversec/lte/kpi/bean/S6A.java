package com.eversec.lte.kpi.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;

public class S6A {

	public long startdate;

	public String city;

	public String mmeip;

	public String hssip;

	public String o_realm;

	public String d_realm;

	public String o_host;

	public String d_host;

	public int failurecause;

	public int status;

	public int type;

	public String area;

	public long delaysum;

	public long reqnum;

	public S6A(byte[] data, long delay, long count) {
		getValue(data, delay, count);
	}

	public void getValue(byte[] data, long delay, long count) {
		IoBuffer dis = IoBuffer.wrap(data);
		dis.getUnsigned();

		this.type = dis.getUnsigned();

		long startdate = dis.getLong();
		this.startdate = ConvertUtils.min2millis(startdate);
		byte[] city = new byte[2];
		dis.get(city);
		this.city = FormatUtils.TBCDFormat(city);
		byte[] ip = new byte[16];
		dis.get(ip);
		this.mmeip = FormatUtils.getIp(ip);
		dis.get(ip);
		this.hssip = FormatUtils.getIp(ip);
		this.o_realm = FormatUtils.formatStr(dis, 44);
		this.d_realm = FormatUtils.formatStr(dis, 44);
		this.o_host = FormatUtils.formatStr(dis, 64);
		this.d_host = FormatUtils.formatStr(dis, 64);
		this.failurecause = dis.getUnsignedShort();
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
		// str.append(this.area);
		// str.append("|");
		str.append(this.mmeip);
		str.append("|");
		str.append(this.hssip);
		str.append("|");
		str.append(this.o_realm);
		str.append("|");
		str.append(this.d_realm);
		str.append("|");
		str.append(this.o_host);
		str.append("|");
		str.append(this.d_host);
		str.append("|");
		str.append(this.failurecause);
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
