package com.eversec.lte.kpi.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;

public class TAG {
	public long startdate;
	public String city;
	public String imsi;
	public String msisdn;
	public int terclass_id;
	public String cardID;
	public String belong_area;
	public int rat;
	
	public int apptype;
	public int appsubtype;
	
	public long throughput;
	public int times;
	public long delay_sum;
	
	public long ulthroughput;
	public long dlthroughput;
	public long nightthroughput;
	
	public int type;
	
	public TAG(TAG_Value v){
		getValue(v);
	}
	
	public void getValue(TAG_Value v){
		this.type = v.getType();
		getPub(v.getData());
		if(type == 1||type == 2){
			this.throughput = v.getThroughput();
			this.times = v.getTimes();
			this.delay_sum = v.getDelay_sum();
		}
		if(type == 3){
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.nightthroughput = v.getNightthroughput();
		}
	}
	
	public void getPub(byte[] data) {
		IoBuffer dis = IoBuffer.wrap(data);
		long startdate = dis.getLong();
		this.startdate = ConvertUtils.min2millis_tag(startdate);
		
		byte[] city = new byte[2];
		dis.get(city);
		this.city = FormatUtils.TBCDFormat(city);
		
		byte[] imsi = new byte[8];
		dis.get(imsi);
		this.imsi = FormatUtils.TBCDFormat(imsi);
		
		byte[] msisdn = new byte[16];
		dis.get(msisdn);
		this.msisdn = FormatUtils.TBCDFormat(msisdn);
		
		byte[] terid = new byte[4];
		dis.get(terid);
		this.terclass_id = ConvertUtils.bytesToInt(terid, 0);
		
		this.rat = dis.getUnsigned();
		
		this.cardID = "";
		this.belong_area = "";
		
		if(type == 1){
			this.apptype = dis.getUnsignedShort();
		}
		if(this.type == 2){
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.startdate);
		str.append("|");
		str.append(this.city);
		str.append("|");
		str.append(this.imsi);
		str.append("|");
		str.append(this.msisdn);
		str.append("|");
		str.append(this.terclass_id);
		str.append("|");
		str.append(this.cardID);
		str.append("|");
		str.append(this.belong_area);
		str.append("|");
		str.append(this.rat);
		str.append("|");
		if(type == 1){
			str.append(this.apptype);
			str.append("|");
		}
		if(type == 2){
			str.append(this.apptype);
			str.append("|");
			str.append(this.appsubtype);
			str.append("|");
		}
		if(type == 1||type == 2){
			str.append(this.throughput);
			str.append("|");
			str.append(this.times);
			str.append("|");
			str.append(this.delay_sum);
		}
		if(type == 3){
			str.append(this.ulthroughput);
			str.append("|");
			str.append(this.dlthroughput);
			str.append("|");
			str.append(this.nightthroughput);
		}
		str.append("\n");
		return str.toString();
	}

	public String getStartDate() {
		return ConvertUtils.long2timeMillis(this.startdate);
	}

	public int getType() {
		return type;
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