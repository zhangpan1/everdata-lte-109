package com.eversec.lte.processor.backfill;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 中间缓存，用于缓存长时间不变数据
 * @author lirongzhi
 *
 */
//暂不实现map接口
public class BatchHashMap {
	
	public  Map<Character, ConcurrentHashMap<String,  String >> newMap = new HashMap();
	public final  char DEFALUT_KEY = ' ';
	 {
		for (int i = 0; i <= 9; i++) {
			newMap.put((i+"").charAt(0), new ConcurrentHashMap<String, String>());
		}
		newMap.put(DEFALUT_KEY , new ConcurrentHashMap<String, String>());
	}
	
	public  String get(String key){
		ConcurrentHashMap<String, String> map = getKey(key);
		if( null  == map){
			return null;
		}
		return map.get(key);
	}
	
	public  void put(String key,String data ){
		ConcurrentHashMap<String, String> map = getKey(key);
		if( null  == map){
			return  ;
		}
		map.put(key,data);
	}
	
	public  String putIfAbsent(String key,String data ){
		ConcurrentHashMap<String, String> map = getKey(key);
		if( null  == map){
			return  null;
		}
		return map.putIfAbsent(key, data);
	}

	private  ConcurrentHashMap<String, String> getKey(String key) {
		if (key == null || key.length() == 0) {
			return newMap.get(DEFALUT_KEY);
		}
		char last = key.charAt(key.length() - 1);
		ConcurrentHashMap<String, String> result = newMap.get(last );
		if (null == result) {
			return newMap.get(DEFALUT_KEY);
		}
		return result;
	}

	private  ConcurrentHashMap<String, String> getChar(char lastCharacter) {
		ConcurrentHashMap<String, String> result = newMap.get(lastCharacter);
		if (null == result) {
			return newMap.get(DEFALUT_KEY);
		}
		return result;
	}

	public  String sizes() {
		String result = "";
		for (Entry<Character,ConcurrentHashMap<String, String>> m : newMap.entrySet()) {
			result += m.getKey() +":"+m.getValue().size()+",";
		}
		return result;
	}
	
	public  int size() {
		int result = 0;
		for (Entry<Character,ConcurrentHashMap<String, String>> m : newMap.entrySet()) {
			result += m.getValue().size() ;
		}
		return result;
	}

//	public  void main(String[] args) {
//		System.out.println(newMap);
// 		System.out.println(BatchHashMap.getKey("12345") == BatchHashMap.getKey("12345"));
// 		System.out.println(BatchHashMap.getKey("12345") != BatchHashMap.getKey("12346"));
// 		System.out.println(BatchHashMap.getKey("12345") == BatchHashMap.getKey("55555"));
// 		System.out.println(BatchHashMap.getKey("aaaa") == BatchHashMap.getKey("bbb"));
// 		System.out.println(BatchHashMap.getKey("cccc") == BatchHashMap.getKey(null));
// 		System.out.println("---------------------------------------");
// 		BatchHashMap.getKey("12321") ;
// 		BatchHashMap.getKey("222");
// 		BatchHashMap.getKey("33");
// 		BatchHashMap.getKey("44");
// 		BatchHashMap.getKey("11");
// 		BatchHashMap.getKey("555");
// 		BatchHashMap.getKey("77");
// 		System.out.println(sizes());
//	}
}
