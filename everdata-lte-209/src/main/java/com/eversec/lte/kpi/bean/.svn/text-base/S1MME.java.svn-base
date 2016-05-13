package com.eversec.lte.kpi.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;

public class S1MME {

	public long startdate;

	public String city;

	public String area;

	public String mmeip;

	public String enbip;

	public long tac;

	public long cellid;

	public int keyword1;

	public int failurecause;

	public int status;

	public long delaysum;

	public long reqnum;

	public int type;

	public int requestcause;

	public int keyword2;

	public String apn;

	public int othertac;

	public long othercell;

	public S1MME(byte[] data, long delay, long count) {
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

		this.status = dis.getUnsigned();

		this.failurecause = dis.getUnsignedShort();

		this.keyword1 = dis.getUnsigned();

		byte[] ip = new byte[16];
		dis.get(ip);
		this.mmeip = FormatUtils.getIp(ip);

		dis.get(ip);
		this.enbip = FormatUtils.getIp(ip);

		this.tac = dis.getUnsignedShort();

		this.cellid = dis.getUnsignedInt();

		this.area = "";

		this.delaysum = delay;

		this.reqnum = count;

		this.requestcause = dis.getUnsignedShort();

		this.keyword2 = dis.getUnsigned();

		this.apn = FormatUtils.formatStr(dis, 32);

		this.othertac = dis.getUnsignedShort();

		this.othercell = dis.getUnsignedInt();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.startdate);
		str.append("|");
		if (this.type == 25 || this.type == 26 || this.type == 15
				|| this.type == 16) {
			str.append(this.type);
			str.append("|");
		}
		str.append(this.city);
		str.append("|");
		str.append(this.area);
		str.append("|");
		str.append(this.mmeip);
		str.append("|");
		str.append(this.enbip);
		str.append("|");
		str.append(this.tac);
		str.append("|");
		str.append(this.cellid);
		str.append("|");
		if (this.type == 1 || this.type == 4
				|| this.type == 6 || this.type == 21 || this.type == 7
				|| this.type == 5 || this.type == 15 || this.type == 16) {
			str.append(this.keyword1);
			str.append("|");
		}

		if (this.type == 13 || this.type == 7 || this.type == 8||type == 100) {
			str.append(this.apn);
			str.append("|");
		}

		if (this.type == 6) {
			str.append(this.keyword2);
			str.append("|");
		}

		if (this.type == 5 || this.type == 15 || this.type == 16) {
			str.append(this.othertac);
			str.append("|");
		}

		if (this.type == 15 || this.type == 16) {
			str.append(this.othercell);
			str.append("|");
		}

		if (this.type == 1 ||this.type == 2 || this.type == 29 || this.type == 30
				|| this.type == 31 || this.type == 9 || this.type == 10
				|| this.type == 12 || this.type == 13 || this.type == 7
				|| this.type == 8 || this.type == 5 || this.type == 15
				|| this.type == 16||type == 100) {
			str.append(this.failurecause);
			str.append("|");
		}
		if (this.type == 17 || this.type == 15 || this.type == 16) {
			str.append(this.requestcause);
			str.append("|");
		}
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
