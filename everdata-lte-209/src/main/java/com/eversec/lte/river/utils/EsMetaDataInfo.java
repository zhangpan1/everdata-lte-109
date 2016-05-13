package com.eversec.lte.river.utils;

public class EsMetaDataInfo {
	String indexName;
	
	boolean use;
	String filedName;
	String dataType;
	String format;
	boolean store;
	boolean index;
	String indexType;

	public EsMetaDataInfo(EsMetaData md) {
		if (md.classIndexName() != null && md.classIndexName().length() > 0) {
			this.indexName = md.classIndexName();
		}
		this.use = md.use();
		if (md.filedName() != null && md.filedName().length() > 0) {
			this.filedName = md.filedName();
		}
		if (md.dataType() != null && md.dataType().length() > 0) {
			this.dataType = md.dataType();
		}
		if (md.format() != null && md.format().length() > 0) {
			this.format = md.format();
		}
		this.store = md.store();
		
		this.index = md.index();

		if (md.indexType() != null && md.indexType().length() > 0) {
			this.indexType = md.indexType();
		}
	}
	
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean isStore() {
		return store;
	}

	public void setStore(boolean store) {
		this.store = store;
	}

	public String getIndexType() {
		return indexType;
	}
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	public boolean isIndex() {
		return index;
	}
	public void setIndex(boolean index) {
		this.index = index;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
