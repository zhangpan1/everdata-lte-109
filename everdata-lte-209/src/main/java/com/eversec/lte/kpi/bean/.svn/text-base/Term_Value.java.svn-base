package com.eversec.lte.kpi.bean;

import java.util.Arrays;

public class Term_Value {
	byte[] data;
	
	public long ulthroughput;
	public long dlthroughput;
	public long responsetime;
	public long ulthroughput2;
	public long dlthroughput2;
	public long delay_sum;
	public long req_num;
	
	public int type;
	public boolean init;
	
	public void initTermS1mme(byte[] data, long delay_sum, long req_num, int type) {
		this.data = data;
		this.delay_sum = delay_sum;
		this.req_num = req_num;
		this.type = type;
		this.init = true;
	}
	
	public void initFlow(byte[] data, long ulthroughput, long dlthroughput, long req_num, int type) {
		this.data = data;
		this.ulthroughput = ulthroughput;
		this.dlthroughput = dlthroughput;
		this.req_num = req_num;
		this.type = type;
		this.init = true;
	}
	
	public void initTermDns(byte[] data, long delay_sum, long req_num, int type) {
		this.data = data;
		this.delay_sum = delay_sum;
		this.req_num = req_num;
		this.type = type;
		this.init = true;
	}
	
	public void initTermTcp(byte[] data, long delay_sum, long req_num, int type) {
		this.data = data;
		this.delay_sum = delay_sum;
		this.req_num = req_num;
		this.type = type;
		this.init = true;
	}
	
	public void initTermService(byte[] data, long ulthroughput, long dlthroughput,
			long responsetime, long ulthroughput2, long dlthroughput2, 
			long delay_sum, long req_num , int type) {
		this.data = data;
		this.ulthroughput = ulthroughput;
		this.dlthroughput = dlthroughput;
		this.responsetime = responsetime;
		this.ulthroughput2 = ulthroughput2;
		this.dlthroughput2 = dlthroughput2;
		this.delay_sum = delay_sum;
		this.req_num = req_num;
		
		this.init = true;
		this.type = type;
	}
	
	public void addAll(Term_Value value) {
		int type = value.getType();
		if (type == 1) {
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
			addResponsetime(value.getResponsetime());
			addUlthroughput2(value.getUlthroughput2());
			addDlthroughput2(value.getDlthroughput2());
		}
		if(this.type == 1||this.type == 2||this.type == 3
				||this.type == 5||this.type == 6||this.type == 7||this.type == 8){
			addDelay_sum(value.getDelay_sum());
		}
		if(type == 4){
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
		}
		addReq_num(value.getReq_num());
	}	
	
	public Term_Value(){
		this.init = false;
	}
	
	public boolean init() {
		return this.init;
	}

	public void addUlthroughput(long ulthroughput) {
		this.ulthroughput += ulthroughput;
	}
	public void addDlthroughput(long dlthroughput) {
		this.dlthroughput += dlthroughput;
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
	public void addReq_num(long req_num) {
		this.req_num += req_num;
	}

	public byte[] getData() {
		return data;
	}

	public long getUlthroughput() {
		return ulthroughput;
	}

	public long getDlthroughput() {
		return dlthroughput;
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

	public long getReq_num() {
		return req_num;
	}

	public int getType() {
		return type;
	}

	public boolean isInit() {
		return init;
	}

	@Override
	public String toString() {
		return "TermUser_Value [data=" + Arrays.toString(data)
				+ ", ulthroughput=" + ulthroughput + ", dlthroughput="
				+ dlthroughput + ", responsetime=" + responsetime
				+ ", ulthroughput2=" + ulthroughput2 + ", dlthroughput2="
				+ dlthroughput2 + ", delay_sum=" + delay_sum + ", req_num="
				+ req_num + ", type=" + type + ", init=" + init + "]";
	}
	
	
}
