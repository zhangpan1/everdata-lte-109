package com.eversec.lte.kpi.bean;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;

public class S1U {

	/**
	 * public
	 */
	long startdate;
	String city;
	String area;
	String sgw_or_ggsn_ip;
	String enb_or_sgsn_ip;
	long tac;
	long cellid;
	String appserver_ipv4;
	String appserver_ipv6;
	
	long apptype;
	long appsubtype;
	int code; 

	long ulthroughput;
	long dlthroughput;
	long ulpackets;
	long dlpackets;
	long responsetime;
	long ulthroughput2;
	long dlthroughput2;
	
	long delay_sum;
	int req_num;
	int rate_num;
	
	int type;

	public S1U(S1U_Value v) {
		getValue(v);
	}

	public void getValue(S1U_Value v) {
		this.type = v.getType();
		IoBuffer dis = IoBuffer.wrap(v.getData());
		if (type == 1) {   //TCPLink预统计
			getPublic(dis);
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.code = dis.getUnsigned();
			this.delay_sum = v.getDelay_sum();
			this.req_num = v.getReq_num();
		} else if (type == 2) { //DNS 
			getPub(dis);
			this.code = dis.getUnsigned();
			this.delay_sum = v.getDelay_sum();
			this.req_num = v.getReq_num();
		} else if (type == 3) { //HTTP
			getPublic(dis);
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.code = dis.getUnsignedShort();
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.ulpackets = v.getUlpackets();
			this.dlpackets = v.getDlpackets();
			this.responsetime = v.getResponsetime();
			this.ulthroughput2 =  v.getUlthroughput2();
			this.dlthroughput2 = v.getDlthroughput2();
			this.delay_sum = v.getDelay_sum();
			this.req_num = v.getReq_num();
			this.rate_num = v.getRate_num();
		} else if (type == 4){ //TCPPACKET
			getPublic(dis);
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.ulpackets = v.getUlpackets();
			this.dlpackets = v.getDlpackets();
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.ulthroughput2 = v.getUlthroughput2();
			this.dlthroughput2 = v.getDlthroughput2();
		} else if (type == 5){ //FLOW
			getFlow(dis);
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.req_num = v.getReq_num();
		} else if(type == 6){ //AppStore
			getPub(dis);
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.code = dis.getUnsignedShort();
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.ulpackets = v.getUlpackets();
			this.dlpackets = v.getDlpackets();
			this.responsetime = v.getResponsetime();
			this.ulthroughput2 =  v.getUlthroughput2();
			this.dlthroughput2 = v.getDlthroughput2();
			this.delay_sum = v.getDelay_sum();
			this.req_num = v.getReq_num();
		} else if(type == 7){ //IM
			long startdate = dis.getLong();
			this.startdate = ConvertUtils.min2millis_s1u(startdate);
			byte[] city = new byte[2];
			dis.get(city);
			this.city = FormatUtils.TBCDFormat(city);
			this.area = "";
			
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.code = dis.getUnsigned(); //appcontent
			
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.delay_sum = v.getDelay_sum();
			this.req_num = v.getReq_num();
		} else if(type == 8){//HTTP Large
			getPublic(dis);
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.code = dis.getUnsignedShort();
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.ulpackets = v.getUlpackets();
			this.dlpackets = v.getDlpackets();
			this.responsetime = v.getResponsetime();
			this.ulthroughput2 =  v.getUlthroughput2();
			this.dlthroughput2 = v.getDlthroughput2();
			this.delay_sum = v.getDelay_sum();
			this.req_num = v.getReq_num();
		} else if(type == 9){//APP Large
			getPub(dis);
			this.apptype = dis.getUnsignedShort();
			this.appsubtype = dis.getUnsignedShort();
			this.code = dis.getUnsignedShort();
			this.ulthroughput = v.getUlthroughput();
			this.dlthroughput = v.getDlthroughput();
			this.ulpackets = v.getUlpackets();
			this.dlpackets = v.getDlpackets();
			this.responsetime = v.getResponsetime();
			this.ulthroughput2 =  v.getUlthroughput2();
			this.dlthroughput2 = v.getDlthroughput2();
			this.delay_sum = v.getDelay_sum();
			this.req_num = v.getReq_num();
		}
	}

	void getPub(IoBuffer dis) {
		long startdate = dis.getLong();
		this.startdate = ConvertUtils.min2millis_s1u(startdate);

		byte[] city = new byte[2];
		dis.get(city);
		this.city = FormatUtils.TBCDFormat(city);

		this.area = "";

		int ip_type = dis.getUnsigned();
		if (ip_type == 1) {
			byte[] ip = new byte[4];
			dis.get(ip);
			this.sgw_or_ggsn_ip = FormatUtils.getIp(ip);
			dis.get(ip);
			this.enb_or_sgsn_ip = FormatUtils.getIp(ip);
			dis.getLong();
			dis.getLong();
			dis.getLong();
		} else {
			byte[] ip = new byte[16];
			dis.get(ip);
			this.sgw_or_ggsn_ip = FormatUtils.getIp(ip);
			dis.get(ip);
			this.enb_or_sgsn_ip = FormatUtils.getIp(ip);
		}

		this.tac = dis.getUnsignedShort();

		this.cellid = dis.getUnsignedInt();

		byte[] ip = new byte[4];
		dis.get(ip);
		this.appserver_ipv4 = FormatUtils.getIp(ip);
		ip = new byte[16];
		dis.get(ip);
		this.appserver_ipv6 = FormatUtils.getIp(ip);
	}
	
	/**
	 * 不带服务器 IP 的公共部分
	 */
	void getPublic(IoBuffer dis) {
		long startdate = dis.getLong();
		this.startdate = ConvertUtils.min2millis_s1u(startdate);

		byte[] city = new byte[2];
		dis.get(city);
		this.city = FormatUtils.TBCDFormat(city);

		this.area = "";

		int ip_type = dis.getUnsigned();
		if (ip_type == 1) {
			byte[] ip = new byte[4];
			dis.get(ip);
			this.sgw_or_ggsn_ip = FormatUtils.getIp(ip);
			dis.get(ip);
			this.enb_or_sgsn_ip = FormatUtils.getIp(ip);
			dis.getLong();
			dis.getLong();
			dis.getLong();
		} else {
			byte[] ip = new byte[16];
			dis.get(ip);
			this.sgw_or_ggsn_ip = FormatUtils.getIp(ip);
			dis.get(ip);
			this.enb_or_sgsn_ip = FormatUtils.getIp(ip);
		}

		this.tac = dis.getUnsignedShort();

		this.cellid = dis.getUnsignedInt();
	}
	
	void getFlow(IoBuffer dis) {
		long startdate = dis.getLong();
		this.startdate = ConvertUtils.min2millis_s1u(startdate);

		byte[] city = new byte[2];
		dis.get(city);
		this.city = FormatUtils.TBCDFormat(city);

		this.area = "";

		byte[] ip = new byte[16];
		dis.get(ip);
		this.sgw_or_ggsn_ip = FormatUtils.getIp(ip);
		dis.get(ip);
		this.enb_or_sgsn_ip = FormatUtils.getIp(ip);

		this.tac = dis.getUnsignedShort();

		this.cellid = dis.getUnsignedInt();

		this.apptype = dis.getUnsignedShort();
		this.appsubtype = dis.getUnsignedShort();
		this.code = dis.getUnsignedShort();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.startdate);
		str.append("|");
		str.append(this.city);
		str.append("|");
		str.append(this.area);
		str.append("|");
		if(type != 7){
			str.append(this.sgw_or_ggsn_ip);
			str.append("|");
			str.append(this.enb_or_sgsn_ip);
			str.append("|");
			str.append(this.tac);
			str.append("|");
			str.append(this.cellid);
			str.append("|");
		}
		
		if (type == 3||type == 6||type == 7||type == 8||type == 9) {
			str.append(this.apptype);
			str.append("|");
			str.append(this.appsubtype);
			str.append("|");
			str.append(this.code);
			str.append("|");
		}
		
		if (type == 2) {
			str.append(this.code);
			str.append("|");
		}
		
		//appserver_ip
		if (this.appserver_ipv4 != null && !this.appserver_ipv4.equals("")) {
			str.append(this.appserver_ipv4);
			str.append("|");
		} else if(type == 2||type == 6||type == 9) {
			str.append(this.appserver_ipv6);
			str.append("|");
		}
		
		if (type == 1) {
			str.append(this.apptype);
			str.append("|");
			str.append(this.appsubtype);
			str.append("|");
			str.append(this.code);
			str.append("|");
			str.append(this.delay_sum);
			str.append("|");
			str.append(this.req_num);
		}
		if (type == 2) {
			str.append(this.delay_sum);
			str.append("|");
			str.append(this.req_num);
		}
		if (type == 3||type == 6||type == 8||type == 9) {
			str.append(this.ulthroughput);
			str.append("|");
			str.append(this.dlthroughput);
			str.append("|");
			str.append(this.ulpackets);
			str.append("|");
			str.append(this.dlpackets);
			str.append("|");
			str.append(this.responsetime);
			str.append("|");
			str.append(this.ulthroughput2);
			str.append("|");
			str.append(this.dlthroughput2);
			str.append("|");
			str.append(this.delay_sum);
			str.append("|");
			str.append(this.req_num);
		}
		if (type == 4) {
			str.append(this.apptype);
			str.append("|");
			str.append(this.appsubtype);
			str.append("|");
			str.append(this.ulpackets);
			str.append("|");
			str.append(this.dlpackets);
			str.append("|");
			str.append(this.ulthroughput);
			str.append("|");
			str.append(this.dlthroughput);
			str.append("|");
			str.append(this.ulthroughput2);
			str.append("|");
			str.append(this.dlthroughput2);
		}
		if (type == 5) {
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
			str.append(this.req_num);
		}
		if (type == 7) {
			str.append(this.ulthroughput);
			str.append("|");
			str.append(this.dlthroughput);
			str.append("|");
			str.append(this.delay_sum);
			str.append("|");
			str.append(this.req_num);
		}
		if(type == 3){
			str.append("|");
			str.append(this.rate_num);
		}
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
