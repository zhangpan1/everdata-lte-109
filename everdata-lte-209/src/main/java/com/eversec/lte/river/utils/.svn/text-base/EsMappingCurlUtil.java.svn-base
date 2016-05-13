package com.eversec.lte.river.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

@SuppressWarnings("rawtypes")
public class EsMappingCurlUtil extends EsCurlUtil {

	// #XdrSingleSourceCellMR
	// curl -XPUT
	// 'http://192.168.30.39:9200/sdtp-lte/xdr_single_cellmr/_mapping' -d '
	@SuppressWarnings("unused")
	public void main(String[] args) throws Exception {
		String host = "192.168.30.39";
		int port = 9200;
		String index = "sdtp-lte";
		// new EsMappingCurlUtil().printMapping(host, port, index,
		// CdrModel.class);
	}

	private String dateFormat = "yyyyMMddHHmm";
	private boolean dateTypeLong;

	public String createMappingJson(Class clazz) throws Exception {
		FieldType[] fields = createClazzFields(clazz);
		LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();

		for (int i = 0; i < fields.length; i++) {
			FieldType field = fields[i];
			Class<?> ftype = field.getType();
			String wtype;
			if (ftype == int.class || ftype == Integer.class
					|| ftype == short.class || ftype == Short.class
					|| ftype == byte.class || ftype == Byte.class) {
				wtype = "integer";
			} else if (ftype == long.class || ftype == Long.class) {
				wtype = "long";
			} else if (ftype == float.class || ftype == Float.class
					|| ftype == double.class || ftype == Double.class) {
				wtype = "double";
			} else if (ftype == String.class || ftype == byte[].class) {
				wtype = "string";
			} else if (ftype == Date.class) {
				wtype = "date";
			} else if (ftype == List.class || ftype == ArrayList.class) {
				// List 暂时用 json表示
				wtype = "string";
			} else {
				wtype = "string";
			}

			EsMetaDataInfo mdinfo = getMetaDataInfo(field);

			if (mdinfo != null && !mdinfo.isUse()) {
				continue;
			}

			LinkedHashMap<String, Object> fieldType = new LinkedHashMap<String, Object>();
			data.put("properties", properties);
			
			if (mdinfo != null &&  mdinfo.getFiledName()!= null) {
				properties.put(mdinfo.getFiledName(), fieldType);
			}else{
				properties.put(field.getName(), fieldType);
			}
			
			
			if (mdinfo != null && mdinfo.getDataType() != null) {
				fieldType.put("type", mdinfo.getDataType());
			} else {
				fieldType.put("type", wtype);
			}

			if (mdinfo != null) {
				if (!mdinfo.isStore()) {
					fieldType.put("store", "no");
				}else{
					fieldType.put("store", "yes");
				}
			} else {
				fieldType.put("store", "yes");
			}

			if (mdinfo != null) {
				if (!mdinfo.isIndex()) {
					fieldType.put("index", "no");
				}else{
					if (mdinfo.getIndexType() != null) {
						fieldType.put("index", mdinfo.getIndexType());
					}else{
						fieldType.put("index", "not_analyzed");
					}
				}
			}else{
				if ("string".equals(fieldType.get("type"))) {
					fieldType.put("index", "not_analyzed");//string 默认不分词
				}
			}

			if ("date".equals(fieldType.get("type"))) {
				if (dateTypeLong) {
					fieldType.put("type", "long");
				} else {
					if (mdinfo != null && mdinfo.getFormat() != null) {
						fieldType.put("format", mdinfo.getFormat());
					} else {
						fieldType.put("format", getDateFormat());
					}
				}
			}
		}

		data.put("properties", properties);
		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(data);

	}

	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * 设置时间日期格式 默认为 yyyyMMddHHmm
	 * 
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isDateTypeLong() {
		return dateTypeLong;
	}

	/**
	 * 是否用long表示date
	 * 
	 * @param dateTypeLong
	 */
	public void setDateTypeLong(boolean dateTypeLong) {
		this.dateTypeLong = dateTypeLong;
	}

	public String createCurlMapping(String host, int port, String index,
			String type, String json) throws Exception {
		// curl -XPUT
		// 'http://192.168.30.39:9200/sdtp-lte/xdr_single_cellmr/_mapping' -d '

		return "curl -XPUT 'http://" + host + ":" + port + "/" + index + "/"
				+ type + "/_mapping'  -d '\n" + json + "\n'";
	}

	public void printMapping(String host, int port, String index, Class clazz)
			throws Exception {
		System.out.println(createCommon(clazz));
		String json = createMappingJson(clazz);
		json = formatJson(json);
		System.out.println(createCurlMapping(host, port, index,
				createTypeName(clazz), json));

	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param index
	 */
	public void printCreateIndex(String host, int port, String index) {
	}

}
