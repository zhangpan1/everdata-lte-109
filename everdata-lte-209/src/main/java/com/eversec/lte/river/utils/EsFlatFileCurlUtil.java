package com.eversec.lte.river.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * flatfile
 * @author lirongzhi
 *
 */
@SuppressWarnings("rawtypes") 
public class EsFlatFileCurlUtil extends EsCurlUtil {

	String idField = null;
	String inputMode = null;

	LinkedHashMap<String, String> exIndexMap = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> exAllinoneMap = new LinkedHashMap<String, String>();
	
	String preName = "";

	public EsFlatFileCurlUtil setIdField(String idField) {
		this.idField = idField;
		return this;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public EsFlatFileCurlUtil putExIndexMap(String key, String value) {
		exIndexMap.put(key, value);
		return this;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public EsFlatFileCurlUtil putExAllinoneMap(String key, String value) {
		exAllinoneMap.put(key, value);
		return this;
	}

	public EsFlatFileCurlUtil setInputMode(String inputMode) {
		this.inputMode = inputMode;
		return this;
	}
	
	/**
	 * river前缀名
	 * @param preName
	 */
	public void setPreName(String preName) {
		if(null == preName){
			this.preName = "";
		}else{
			this.preName = preName;
		}
	}

	// #XdrSingleSourceCellMR
	// curl -XPUT
	// 'http://192.168.30.39:9200/sdtp-lte/xdr_single_cellmr/_mapping' -d '
	@SuppressWarnings("unused")
	public void main(String[] args) throws Exception {
		String host = "192.168.30.39";
		int port = 9200;
		String index = "sdtp-lte";
		// new EsRiverCurlUtil().printRiver(host, port, index, CdrModel.class);
	}

	public void printRiver(String host, String folder,
			boolean first_line_is_header, String field_separator,
			String filename_pattern, String index, Class clazz)
			throws Exception {
		String type = createTypeName(clazz);
		System.out.println(createCommon(clazz));
		String json = createRiverJson(folder, first_line_is_header,
				field_separator, filename_pattern, index, type, clazz);
		json = formatJson(json);
		System.out.println(createCurlRiver(host, 9200, index, type, json));

	}

	public void printRiverName(Class clazz) throws Exception {
		String type = createTypeName(clazz);
		System.out.println(preName + "river_flatfile_" + type);
	}
	
	public void printRiverNames(Class... clazzs) throws Exception {
		StringBuilder sb = new StringBuilder("#");
		for (Class clazz : clazzs) {
			String type = createTypeName(clazz);
			sb.append(preName + "river_flatfile_" + type);
			sb.append(",");
		}
		System.out.println(sb);
	}

	public void printRiverNameInOne(Class... clazzs) throws Exception {
		for (int i = 0; i < clazzs.length; i++) {
			String type = createTypeName(clazzs[i]);
			System.out.print(preName + "river_flatfile_" + type);
			if (i != clazzs.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();

	}

	public String createCurlRiver(String host, int port, String index,
			String type, String json) {
		// curl -XPUT 192.168.200.126:9200/_river/myriver_gn_cell_1m/_meta -d '
		return "curl -XPUT 'http://" + host + ":" + port
				+ "/_river/"+preName+"river_flatfile_" + type + "/_meta'  -d '\n" + json
				+ "\n'";
	}

	public String createRiverJson(String folder, boolean first_line_is_header,
			String field_separator, String filename_pattern, String index,
			String type, Class clazz) throws Exception {
		FieldType[] fields = createClazzFields(clazz);
		LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> allinone = createAllinoneMap(clazz,
				fields, folder, first_line_is_header, field_separator,
				filename_pattern);
		LinkedHashMap<String, Object> indexMap = createIndexeMap(clazz, index,
				type);
		data.put("type", "allinone");
		data.put("allinone", allinone);
		data.put("index", indexMap);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(data);

	}

	public LinkedHashMap<String, Object> createIndexeMap(Class clazz,
			String index, String type) {
		LinkedHashMap<String, Object> indexMap = new LinkedHashMap<String, Object>();
		indexMap.put("index", index);
		indexMap.put("type", type);
		indexMap.put("bulk_action", 10000);
		indexMap.put("bulk_size", 20);
		indexMap.put("bulk_threshold", 20);
		indexMap.put("bulk_interval_seconds", 60);
		indexMap.put("index_auto_create", false);
		if (inputMode != null) {
			indexMap.put("input_mode", inputMode);
		}
		indexMap.putAll(exIndexMap);

		return indexMap;
	}

	public LinkedHashMap<String, Object> createAllinoneMap(Class clazz,
			FieldType[] fields, String folder, boolean first_line_is_header,
			String field_separator, String filename_pattern) {
		LinkedHashMap<String, Object> allinone = new LinkedHashMap<String, Object>();
		List<String> filedNames = new ArrayList<String>();
		for (FieldType field : fields) {
			EsMetaDataInfo mdInfo = getMetaDataInfo(field);
			if(mdInfo != null && ! mdInfo.isUse()){
				continue;
			}
			if (mdInfo != null &&  mdInfo.getFiledName()!= null) {
				filedNames.add(mdInfo.getFiledName());
			}else{
				filedNames.add(field.getName());
			}
		}
		//
		// "river_type":"flatfile",
		// "folder":"/home/fakeData",
		// "first_line_is_header":"false",
		// "field_separator":",",
		// "filename_pattern":".*\\.csv$",
		allinone.put("river_type", "flatfile");
		allinone.put("folder", folder);
		allinone.put("first_line_is_header", first_line_is_header);
		allinone.put("field_separator", field_separator);
		allinone.put("filename_pattern", filename_pattern);
		allinone.put("fields", filedNames);

		if (idField != null) {
			allinone.put("id_fields", idField);
		}
		allinone.putAll(exAllinoneMap);

		return allinone;
	}
}
