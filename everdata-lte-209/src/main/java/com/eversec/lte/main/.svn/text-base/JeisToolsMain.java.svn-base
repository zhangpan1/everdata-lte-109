package com.eversec.lte.main;

import com.eversec.lte.cache.ValueGetHandler;
import com.eversec.lte.sdtp.redis.JedisTools;

public class JeisToolsMain {
	public static void main(String[] args) {
		final JedisTools tools = new JedisTools(1, false, true);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					tools.destroy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		tools.getAsyn(null,args[0], new ValueGetHandler() {
			@Override
			public void onValueReturn(String type,String key, String value) {
				System.out.println(key + ":" + value);
				
				System.exit(0);
			}
		});

	}
}
