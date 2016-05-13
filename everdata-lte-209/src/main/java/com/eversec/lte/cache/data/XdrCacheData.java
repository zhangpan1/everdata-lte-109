package com.eversec.lte.cache.data;

import com.eversec.common.cache.IData;

public class XdrCacheData<T> implements IData {

	private static final long serialVersionUID = 1L;

	private T value;

	public XdrCacheData() {
	}

	public XdrCacheData(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		if(value != null){
			return value.toString();
		}
		return super.toString();
	}

	@Override
	public int getMemoryBytes() {
		return 0;
	}

}
