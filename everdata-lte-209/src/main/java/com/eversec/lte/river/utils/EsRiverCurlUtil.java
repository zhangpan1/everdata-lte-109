package com.eversec.lte.river.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * mina
 * @author lirongzhi
 *
 */
@SuppressWarnings("rawtypes") 
public class EsRiverCurlUtil extends EsCurlUtil {

	// allinone.put("id_fields", "xdrId");
	// indexe.put("input_mode", "upsert");

	String idField = null;
	String inputMode = null;

	LinkedHashMap<String, String> exIndexMap = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> exAllinoneMap = new LinkedHashMap<String, String>();

	public EsRiverCurlUtil setIdField(String idField) {
		this.idField = idField;
		return this;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public EsRiverCurlUtil putExIndexMap(String key, String value) {
		exIndexMap.put(key, value);
		return this;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public EsRiverCurlUtil putExAllinoneMap(String key, String value) {
		exAllinoneMap.put(key, value);
		return this;
	}

	public EsRiverCurlUtil setInputMode(String inputMode) {
		this.inputMode = inputMode;
		return this;
	}


	public void printRiver(String host, int port, String index, Class clazz)
			throws Exception {
		String type = createTypeName(clazz);
		System.out.println(createCommon(clazz));
		String json = createRiverJson(host, port, index, type, clazz);
		json = formatJson(json);
		System.out.println(createCurlRiver(host, 9200, index, type, json));

	}

	public void printRiverName(Class clazz) throws Exception {
		String type = createTypeName(clazz);
		System.out.println("river_" + type);
	}
	
	public void printRiverNames(Class... clazzs) throws Exception {
		StringBuilder sb = new StringBuilder("#");
		for (Class clazz : clazzs) {
			String type = createTypeName(clazz);
			sb.append("river_" + type);
			sb.append(",");
		}
		System.out.println(sb);
	}
	
	public void printRiverNameInOne(Class... clazzs) throws Exception {
		for (int i = 0; i < clazzs.length; i++) {
			String type = createTypeName(clazzs[i]);
			System.out.print("river_" + type);
			if( i != clazzs.length - 1){
				System.out.print(",");
			}
		}
		System.out.println();
		

	}

	public String createCurlRiver(String host, int port, String index,
			String type, String json) {
		// curl -XPUT 192.168.200.126:9200/_river/myriver_gn_cell_1m/_meta -d '
		return "curl -XPUT 'http://" + host + ":" + port + "/_river/river_"
				+ type + "/_meta'  -d '\n" + json + "\n'";
	}

	public String createCurlDeleteRiver(String host, int port, String index,
			String type) {
		// curl -XPUT 192.168.200.126:9200/_river/myriver_gn_cell_1m/_meta -d '
		return "curl -XDELETE 'http://" + host + ":" + port + "/_river/river_"
				+ type + "/'";
	}

	public void printDelete(String host, int port, String index, Class clazz)
			throws Exception {
		String type = createTypeName(clazz);
		System.out.println(createCurlDeleteRiver(host, 9200, index, type));

	}

	public String createRiverJson(String host, int port, String index,
			String type, Class clazz) throws Exception {
		FieldType[] fields = createClazzFields(clazz);
		LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> allinone = createAllinoneMap(clazz,
				fields, host, port);
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
			FieldType[] fields, String host, int port) {
		LinkedHashMap<String, Object> allinone = new LinkedHashMap<String, Object>();
		List<String> filedNames = new ArrayList<String>();
		for (FieldType field : fields) {
			EsMetaDataInfo mdInfo = getMetaDataInfo(field);
			if (mdInfo != null &&  mdInfo.getFiledName()!= null) {
				filedNames.add(mdInfo.getFiledName());
			}else{
				filedNames.add(field.getName());
			}
		}
		allinone.put("river_type", "mina");
		allinone.put("host", host);
		allinone.put("port", port);
		allinone.put("protocol", "tcp");
		allinone.put("field_separator", "|");
		allinone.put("fields", filedNames);

		if (idField != null) {
			allinone.put("id_fields", idField);
		}
		allinone.putAll(exAllinoneMap);

		return allinone;
	}
}
