//package com.eversec.lte.sdtp.redis;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.concurrent.ArrayBlockingQueue;
//
//import redis.clients.jedis.Jedis;
//
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.processor.backfill2.GuauaBatchHashMap;
//import com.eversec.lte.processor.backfill2.RuleCache2;
//import com.eversec.lte.processor.backfill2.RuleData2;
//
//public class JedisBatchTools {
//	
//	
//	public static Map<Character, JedisToken> newMap = new HashMap();
//	
//	public final static char DEFALUT_KEY = ' ';
//	static {
//
//		for (int i = 0; i <= 9; i++) {
//			newMap.put((i + "").charAt(0), new JedisToken("host1", 8000));
//		}
//		newMap.put(DEFALUT_KEY, new JedisToken("host1", 8000));
//	}
//
//	public static String get(String key) {
//		RuleCache2 map = getKey(key);
//		if (null == map) {
//			return null;
//		}
//		RuleData2 rule2 = map.getIfPresent(key);
//		if (rule2 != null) {
//			return rule2.getRule();
//		}
//		return null;
//	}
//
//	public static void put(String key, String data) {
//		Jedis map = getKey(key);
//		if (null == map) {
//			return;
//		}
//		map.update(key, new RuleData2(data));
//	}
//
//	private static JedisToken getKey(String key) {
//		if (key == null || key.length() == 0) {
//			return newMap.get(DEFALUT_KEY);
//		}
//		char last = key.charAt(key.length() - 1);
//		JedisToken result = newMap.get(last);
//		if (null == result) {
//			return newMap.get(DEFALUT_KEY);
//		}
//		return result;
//	}
//
//	private static JedisToken getChar(char lastCharacter) {
//		JedisToken result = newMap.get(lastCharacter);
//		if (null == result) {
//			return newMap.get(DEFALUT_KEY);
//		}
//		return result;
//	}
//
//	public static String sizes() {
////		String result = "";
////		for (Entry<Character, Jedis> m : newMap.entrySet()) {
////			result += m.getKey() + ":" + m.getValue().size() + ",";
////		}
////		return result;
//	}
//	
//	private static class JedisToken{
//		Character c;
//		Jedis jedis;
//		
//		int setIndex = 0;
//		int getIndex = 0;
//		public void put(String key,String value){
//			  
//		}
//		public void get(String key,JedisValueGetHandler handler){
//			
//		}
//	}
//
//	public static void main(String[] args) {
//		 
//	}
//}
