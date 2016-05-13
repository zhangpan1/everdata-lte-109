package com.eversec.lte.kpi.bean;

public class TAG_Value {
	byte[] data;
	
	public long throughput;
	public int times;
	public long delay_sum;
	
	public long ulthroughput;
	public long dlthroughput;
	public long nightthroughput;
	
	public int type;
	public boolean init;
	
	public boolean init() {
		return this.init;
	}
	
	public TAG_Value(){
		this.init = false;
	}
	
	public void initApptypeTag(byte[] data, long throughput, int times, long delay_sum, int type) {
		this.data = data;
		this.throughput = throughput;
		this.times = times;
		this.delay_sum = delay_sum;
		
		this.init = true;
		this.type = type;
	}
	
	public void initAppsubtypeTag(byte[] data, long throughput, int times, long delay_sum, int type) {
		this.data = data;
		this.throughput = throughput;
		this.times = times;
		this.delay_sum = delay_sum;
		
		this.init = true;
		this.type = type;
	}
	
	public void initNightTag(byte[] data, long ulthroughput, long dlthroughput, long nightthroughput, int type) {
		this.data = data;
		this.ulthroughput = ulthroughput;
		this.dlthroughput = dlthroughput;
		this.nightthroughput = nightthroughput;
		
		this.init = true;
		this.type = type;
	}
	
	public void addAll(TAG_Value value) {
		int type = value.getType();
		if (type == 1||type == 2) {
			addThroughput(value.getThroughput());
			addTimes(value.getTimes());
			addDelay_sum(value.getDelay_sum());
		}
		if(this.type == 3){
			addUlthroughput(value.getUlthroughput());
			addDlthroughput(value.getDlthroughput());
			addNightThroughput(value.getNightthroughput());
		}
	}
	
	
	

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getThroughput() {
		return throughput;
	}

	public void setThroughput(long throughput) {
		this.throughput = throughput;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public long getDelay_sum() {
		return delay_sum;
	}

	public void setDelay_sum(long delay_sum) {
		this.delay_sum = delay_sum;
	}

	public long getUlthroughput() {
		return ulthroughput;
	}

	public void setUlthroughput(long ulthroughput) {
		this.ulthroughput = ulthroughput;
	}

	public long getDlthroughput() {
		return dlthroughput;
	}

	public void setDlthroughput(long dlthroughput) {
		this.dlthroughput = dlthroughput;
	}

	public long getNightthroughput() {
		return nightthroughput;
	}

	public void setNightthroughput(long nightthroughput) {
		this.nightthroughput = nightthroughput;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setInit(boolean init) {
		this.init = init;
	}
	
	public void addThroughput(long throughput) {
		this.throughput += throughput;
	}
	public void addTimes(long times) {
		this.times += times;
	}
	public void addDelay_sum(long delay_sum) {
		this.delay_sum += delay_sum;
	}
	
	public void addUlthroughput(long ulthroughput) {
		this.ulthroughput += ulthroughput;
	}
	public void addDlthroughput(long dlthroughput) {
		this.dlthroughput += dlthroughput;
	}
	public void addNightThroughput(long nightthroughput) {
		this.nightthroughput += nightthroughput;
	}
	
}
