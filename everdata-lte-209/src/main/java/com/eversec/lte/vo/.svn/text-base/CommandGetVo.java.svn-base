package com.eversec.lte.vo;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class CommandGetVo {
	long took;
	long total;
	long from;
	@JSONField(name = "_shard_failed")
	int shardFailed;
	@JSONField(name = "_shard_successful")
	int shardSuccessful;
	@JSONField(name = "_shard_total")
	int shardTotal;
	String[] fields;
	List<String[]> rows;

	public long getTook() {
		return took;
	}

	public void setTook(long took) {
		this.took = took;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public int getShardFailed() {
		return shardFailed;
	}

	public void setShardFailed(int shardFailed) {
		this.shardFailed = shardFailed;
	}

	public int getShardSuccessful() {
		return shardSuccessful;
	}

	public void setShardSuccessful(int shardSuccessful) {
		this.shardSuccessful = shardSuccessful;
	}

	public int getShardTotal() {
		return shardTotal;
	}

	public void setShardTotal(int shardTotal) {
		this.shardTotal = shardTotal;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public List<String[]> getRows() {
		return rows;
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "CommandGetVo [took=" + took + ", total=" + total + ", from="
				+ from + ", shardFailed=" + shardFailed + ", shardSuccessful="
				+ shardSuccessful + ", shardTotal=" + shardTotal + ", fields="
				+ Arrays.toString(fields) + ", rows=" + rows + "]";
	}
}
