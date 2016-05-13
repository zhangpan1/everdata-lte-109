package com.eversec.lte.kpi.bean;

public class S1U_Value {
	byte[] data;

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

	boolean init;
	int type;

	public S1U_Value() {
		this.init = false;
	}

	public boolean init() {
		return this.init;
	}
	
	//信令面 2015年10月8日15:56:29 合并用户面和控制面，公用同一个类
	public void initKpi(byte[] data, long delay_sum, int req_num, int type) {
		this.data = data;
		this.delay_sum = delay_sum;
		this.req_num = req_num;
		this.type = type;
		this.init = true;
	}
	
	public void initFlow(byte[] data, long flow_ulthroughput, long flow_dlthroughput, int flow_req_num, int type) {
		this.data = data;
		this.ulthroughput = flow_ulthroughput;
		this.dlthroughput = flow_dlthroughput;
		this.req_num = flow_req_num;
		this.type = type;
		this.init = true;
	}

	public void initTCP(byte[] data, long tcp_delay_sum, int tcp_req_num, int type) {
		this.data = data;
		this.delay_sum = tcp_delay_sum;
		this.req_num = tcp_req_num;
		this.init = true;
		this.type = type;
	}
	public void initTcpPacket(byte[] data, long tcp_ultcp_packets, long tcp_dltcp_packets,
			long tcp_ultcp_disord_packets, long tcp_dltcp_disord_packets,
			long tcp_ultcp_retrans_packets, long tcp_dltcp_retrans_packets, int type) {
		this.data = data;
		this.ulpackets = tcp_ultcp_packets;
		this.dlpackets = tcp_dltcp_packets;
		this.ulthroughput = tcp_ultcp_disord_packets;
		this.dlthroughput = tcp_dltcp_disord_packets;
		this.ulthroughput2 = tcp_ultcp_retrans_packets;
		this.dlthroughput2 = tcp_dltcp_retrans_packets;
		this.init = true;
		this.type = type;
	}

	public void initDNS(byte[] data, long delay_sum, int req_num, int type) {
		this.data = data;
		this.delay_sum = delay_sum;
		this.req_num = req_num;
		this.init = true;
		this.type = type;
	}

	public void initHttp(byte[] data, long http_ulthroughput,
			long http_dlthroughput, long http_ulpackets, long http_dlpackets,
			long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
			long http_delay_sum, int http_req_num, int http_rate_num,int type) {
		this.data = data;
		this.ulthroughput = http_ulthroughput;
		this.dlthroughput = http_dlthroughput;
		this.ulpackets = http_ulpackets;
		this.dlpackets = http_dlpackets;
		this.responsetime = http_responsetime;
		this.ulthroughput2 = http_ulthroughput2;
		this.dlthroughput2 = http_dlthroughput2;
		this.delay_sum = http_delay_sum;
		this.req_num = http_req_num;
		this.rate_num =http_rate_num;
		this.init = true;
		this.type = type;
	}
	
	public void initHttpLarge(byte[] data, long http_ulthroughput,
			long http_dlthroughput, long http_ulpackets, long http_dlpackets,
			long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
			long http_delay_sum, int http_req_num, int type) {
		this.data = data;
		this.ulthroughput = http_ulthroughput;
		this.dlthroughput = http_dlthroughput;
		this.ulpackets = http_ulpackets;
		this.dlpackets = http_dlpackets;
		this.responsetime = http_responsetime;
		this.ulthroughput2 = http_ulthroughput2;
		this.dlthroughput2 = http_dlthroughput2;
		this.delay_sum = http_delay_sum;
		this.req_num = http_req_num;
		this.init = true;
		this.type = type;
	}
	
	public void initAppStore(byte[] data, long http_ulthroughput,
			long http_dlthroughput, long http_ulpackets, long http_dlpackets,
			long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
			long http_delay_sum, int http_req_num, int type) {
		this.data = data;
		this.ulthroughput = http_ulthroughput;
		this.dlthroughput = http_dlthroughput;
		this.ulpackets = http_ulpackets;
		this.responsetime = http_responsetime;
		this.ulthroughput2 = http_ulthroughput2;
		this.dlthroughput2 = http_dlthroughput2;
		this.delay_sum = http_delay_sum;
		this.req_num = http_req_num;
		this.init = true;
		this.type = type;
	}
	
	public void initAppLarge(byte[] data, long http_ulthroughput,
			long http_dlthroughput, long http_ulpackets, long http_dlpackets,
			long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
			long http_delay_sum, int http_req_num, int type) {
		this.data = data;
		this.ulthroughput = http_ulthroughput;
		this.dlthroughput = http_dlthroughput;
		this.ulpackets = http_ulpackets;
		this.responsetime = http_responsetime;
		this.ulthroughput2 = http_ulthroughput2;
		this.dlthroughput2 = http_dlthroughput2;
		this.delay_sum = http_delay_sum;
		this.req_num = http_req_num;
		this.init = true;
		this.type = type;
	}
	
	public void initIM(byte[] data, long im_ulthroughput, long im_dlthroughput,
			long im_delay_sum, int im_req_num, int type) {
		this.data = data;
		this.ulthroughput = im_ulthroughput;
		this.dlthroughput = im_dlthroughput;
		this.delay_sum = im_delay_sum;
		this.req_num = im_req_num;
		this.init = true;
		this.type = type;
	}
	
	public void addAll(S1U_Value value) {
		int type = value.getType();
		if (type == 1) { //TCP预统计
			addDelay_sum(value.getDelay_sum());
			addReq_num(value.getReq_num());
		} else if (type == 2) { //DNS
			addDelay_sum(value.getDelay_sum());
			addReq_num(value.getReq_num());
		} else if (type == 3) { //HTTP
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
			addUlpackets(value.getUlpackets());
			addDlpackets(value.getDlpackets());
			addResponsetime(value.getResponsetime());
			addUlthroughput2(value.getUlthroughput2());
			addDlthroughput2(value.getDlthroughput2());
			addDelay_sum(value.getDelay_sum());
			addReq_num(value.getReq_num());
			addRate_num(value.getRate_num());
		} else if (type == 4) { //TCP 性能预统计
			addUlpackets(value.getUlpackets());
			addDlpackets(value.getDlpackets());
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
			addUlthroughput2(value.getUlthroughput2());
			addDlthroughput2(value.getDlthroughput2());
		} else if (type == 5) { //FLOW 全流量
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
			addReq_num(value.getReq_num());
		} else if (type == 6||type == 8||type == 9) { //6 AppStore 8 HTTPLarge 9 APPlarge 
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
			addUlpackets(value.getUlpackets());
			addDlpackets(value.getDlpackets());
			addResponsetime(value.getResponsetime());
			addUlthroughput2(value.getUlthroughput2());
			addDlthroughput2(value.getDlthroughput2());
			addDelay_sum(value.getDelay_sum());
			addReq_num(value.getReq_num());
		} else if (type == 7) { //IM
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
			addDelay_sum(value.getDelay_sum());
			addReq_num(value.getReq_num());
		}
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getUlthroughput() {
		return ulthroughput;
	}
	public long getDlthroughput() {
		return dlthroughput;
	}
	public long getUlpackets() {
		return ulpackets;
	}

	public long getDlpackets() {
		return dlpackets;
	}

	public long getResponsetime() {
		return responsetime;
	}

	public long getUlthroughput2() {
		return ulthroughput2;
	}

	public long getDlthroughput2() {
		return dlthroughput2;
	}

	public long getDelay_sum() {
		return delay_sum;
	}

	public int getReq_num() {
		return req_num;
	}

	public int getRate_num() {
		return rate_num;
	}

	public boolean isInit() {
		return init;
	}

	public int getType() {
		return this.type;
	}

	public void addUlthroughput(long ulthroughput) {
		this.ulthroughput += ulthroughput;
	}

	public void addDlthroughput(long dlthroughput) {
		this.dlthroughput += dlthroughput;
	}

	public void addUlpackets(long ulpackets) {
		this.ulpackets += ulpackets;
	}

	public void addDlpackets(long dlpackets) {
		this.dlpackets += dlpackets;
	}

	public void addResponsetime(long responsetime) {
		this.responsetime += responsetime;
	}

	public void addUlthroughput2(long ulthroughput2) {
		this.ulthroughput2 += ulthroughput2;
	}

	public void addDlthroughput2(long dlthroughput2) {
		this.dlthroughput2 += dlthroughput2;
	}
	
	public void addDelay_sum(long delay_sum) {
		this.delay_sum += delay_sum;
	}

	public void addReq_num(int req_num) {
		this.req_num += req_num;
	}
	
	public void addRate_num(int rate_num) {
		this.rate_num += rate_num;
	}
}
