package com.eversec.lte.kpi.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;

public class Term {
	public long startdate;
	public int terfac_id;  //4
	public int tertype_id;  //4
	public int rat;
	public String city;
	public String area;
	public String sgw_or_ggsn_ip;
	public String eNB_or_sgsn_ip;
	public long tac;
	public long cellid;
	public int apptype;
	public int appsubtype;
	public int code;
	
	public int failure_cause;
	public int status;
	public int dnscode;
	
	public int protocoltype;
	
	public long ulthroughput;
	public long dlthroughput;
	public long responsetime;
	public long ulthroughput2;
	public long dlthroughput2;
	public long delay_sum;
	public long req_num;
	
	public int type;
	
	public Term(Term_Value v){
		getValue(v);
	}
	
	public void getValue(Term_Value v){
		getPub(v.getData());
		if(type == 1){
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.responsetime = v.getResponsetime();
			this.ulthroughput2 = v.getUlthroughput2();
			this.dlthroughput2 = v.getDlthroughput2();
		}
		if(type == 4){
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
		}
		if(this.type == 1||this.type == 2||this.type == 3
				||this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			this.delay_sum = v.getDelay_sum();
		}
		this.req_num = v.getReq_num();
	}

	public void getPub(byte[] data) {
		IoBuffer dis = IoBuffer.wrap(data);
		this.type = dis.getUnsigned();
		//System.out.println("解码类型:"+type);
		long startdate = dis.getLong();
		this.startdate = ConvertUtils.min2millis_term(startdate);
		
		if(type == 1||type == 2||type == 3||this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			byte[] terid = new byte[4];
			dis.get(terid);
			this.terfac_id = ConvertUtils.bytesToInt(terid, 0);
			dis.get(terid);
			this.tertype_id = ConvertUtils.bytesToInt(terid, 0);
		}
		
		this.rat = dis.getUnsigned();
		
		byte[] city = new byte[2];
		dis.get(city);
		this.city = FormatUtils.TBCDFormat(city);
		
		this.area = "";
		
		byte[] ip = new byte[16];
		dis.get(ip);
		this.sgw_or_ggsn_ip = FormatUtils.getIp(ip);
		dis.get(ip);
		this.eNB_or_sgsn_ip = FormatUtils.getIp(ip);
		
		this.tac = dis.getUnsignedShort();

		this.cellid = dis.getUnsignedInt();
		if(type == 1){
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.code = dis.getUnsignedShort();
		}
		if(this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			this.failure_cause = dis.getUnsignedShort();
		}
		if(type == 2||this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			this.status = dis.getUnsigned();
		}
		if(type == 3){
			this.dnscode = dis.getUnsigned();
		}
		if(type == 4){
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.protocoltype = dis.getUnsignedShort();
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.startdate);
		str.append("|");
		if (this.type == 1||this.type == 2||this.type == 3
				||this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			str.append(this.terfac_id);
			str.append("|");
			str.append(this.tertype_id);
			str.append("|");
		}
		str.append(this.rat);
		str.append("|");
		str.append(this.city);
		str.append("|");
		str.append(this.area);
		str.append("|");
		str.append(this.sgw_or_ggsn_ip);
		str.append("|");
		str.append(this.eNB_or_sgsn_ip);
		str.append("|");
		str.append(this.tac);
		str.append("|");
		str.append(this.cellid);
		str.append("|");
		if (this.type == 1) {
			str.append(this.apptype);
			str.append("|");
			str.append(this.appsubtype);
			str.append("|");
			str.append(this.code);
			str.append("|");
			str.append(this.ulthroughput);
			str.append("|");
			str.append(this.dlthroughput);
			str.append("|");
			str.append(this.responsetime);
			str.append("|");
			str.append(this.ulthroughput2);
			str.append("|");
			str.append(this.dlthroughput2);
			str.append("|");
		}
		if(this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			str.append(this.failure_cause);
			str.append("|");
		}
		if (this.type == 2||this.type == 5||this.type == 6||this.type == 7||this.type == 8) {
			str.append(this.status);
			str.append("|");
		}
		if (this.type == 3) {
			str.append(this.dnscode);
			str.append("|");
		}
		if (this.type == 4){
			str.append(this.apptype);
			str.append("|");
			str.append(this.appsubtype);
			str.append("|");
			str.append(this.protocoltype);
			str.append("|");
			str.append(this.ulthroughput);
			str.append("|");
			str.append(this.dlthroughput);
			str.append("|");
		}
		if(this.type == 1||this.type == 2||this.type == 3
				||this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			str.append(this.delay_sum);
			str.append("|");
		}
		str.append(this.req_num);
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
