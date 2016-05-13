package com.eversec.lte.kpi.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;

public class SGS {

	public long startdate;

	public String city;

	public String mmeip;

	public long tai;

	public long cellid;

	public String mscip;

	public long newlai;

	public long oldlai;

	public int rejectcause;

	public int service_indicator;

	public int sgscause;

	public int status;

	public int type;

	public String area;

	public long delaysum;

	public long reqnum;

	public SGS(byte[] data, long delay, long count) {
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

		this.tai = dis.getUnsignedShort();

		this.cellid = dis.getUnsignedInt();

		dis.get(ip);
		this.mscip = FormatUtils.getIp(ip);

		this.newlai = dis.getUnsignedShort();

		this.oldlai = dis.getUnsignedShort();

		this.rejectcause = dis.getUnsigned();

		this.service_indicator = dis.getUnsigned();

		this.sgscause = dis.getUnsigned();

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
		if (this.type == 9 || this.type == 10) {
			str.append(this.type);
			str.append("|");
		}
		str.append(this.city);
		str.append("|");
		str.append(this.area);
		str.append("|");
		str.append(this.mmeip);
		str.append("|");
		str.append(this.tai);
		str.append("|");
		str.append(this.cellid);
		str.append("|");
		str.append(this.mscip);
		str.append("|");
		str.append(this.newlai);
		str.append("|");
		if (this.type == 5) {
			str.append(this.oldlai);
			str.append("|");
			str.append(this.rejectcause);
			str.append("|");
		}
		if (this.type == 1) {
			str.append(this.service_indicator);
			str.append("|");
		}

		if (this.type == 1 || this.type == 9 || this.type == 10) {
			str.append(this.sgscause);
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
